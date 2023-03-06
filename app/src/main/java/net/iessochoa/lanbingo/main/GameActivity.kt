package net.iessochoa.lanbingo.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import net.iessochoa.lanbingo.R
import net.iessochoa.lanbingo.adapters.Sheet90Adapter
import net.iessochoa.lanbingo.databinding.ActivityGameBinding
import net.iessochoa.lanbingo.dialogs.StatusDialog
import net.iessochoa.lanbingo.main.MainScreenActivity.Companion.conexion
import net.iessochoa.lanbingo.main.MainScreenActivity.Companion.workingDialog
import net.iessochoa.lanbingo.viewmodels.Bingo90ViewModel
import java.io.PrintWriter
import java.util.*
import java.util.concurrent.Executors

class GameActivity : AppCompatActivity() {
    companion object{
        // Una variable estática que permitirá al adaptador saber en qué fase se encuentra la partida.
        var bingo = false
        // Variables para indicar al jugador cuántos puntos/strikes tiene.
        var points = 0
        var strikes = 0
    }

    private lateinit var binding: ActivityGameBinding

    // Creamos un objeto del ViewModel para poder acceder a los cartones generados.
    private val model: Bingo90ViewModel by viewModels()

    // Un objeto de adaptador para asignar al recyclerView que contiene los cartones.
    private lateinit var sheetAdapter: Sheet90Adapter

    // Variables que son usadas para recibir/enviar info del servidor.
    private val pw: PrintWriter = PrintWriter(conexion.getOutputStream(), true)
    private val sc: Scanner = Scanner(conexion.getInputStream())

    // Una última variable que evita desastres en caso de que el jugador haya cantado.
    private var checking = false

    override fun onCreate(savedInstanceState: Bundle?) {
        workingDialog.hideDialog()
        workingDialog.changeContext(this)

        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Iniciamos la variable de adaptador
        sheetAdapter = model.getAdapter()

        // Asignamos al click del botón status la línea que invoca el diálogo de estado del jugador.
        binding.btStatus.setOnClickListener{
            StatusDialog(this)
        }

        // Iniciamos estos métodos desde aquí
        iniciaRvSheets()
        iniciaSenders()
        iniciaRecievers()
    }

    /*En este método asignamos el layout que usará el RecycleView además de asignarle el adaptador
    ubicado en el ViewModel, lo cuál permitirá que no se reinicie cada poco tiempo*/
    private fun iniciaRvSheets() {
        with(binding.rvSheets) {
            //Creamos el layoutManager
            layoutManager = LinearLayoutManager(applicationContext)
            //Asignamos Adaptador
            adapter = sheetAdapter
        }
    }

    /* Este método se encarga de la lógica para expulsar a jugadores por acomulación de strikes
     además de mantener a los jugadores que no hayan cantado línea/bingo en espera para evitar
     más envíos al servidor de los necesarios.*/
    private fun iniciaRecievers(){
        var waiting = false
        Executors.newSingleThreadExecutor().execute {
            while (conexion.isConnected){
                while(!checking){
                    if(strikes > 3){
                        sc.close()
                        pw.close()
                        conexion.close()
                        val intent = Intent(this, MainScreenActivity::class.java)
                        intent.putExtra("Explusado", true)
                        startActivity(intent)
                    }
                    /* Este condicional se encarga de poner a los jugadores en espera gracias a una
                    respuesta del servidor. Con otra respuesta del servidor los libera de la espera
                    y se encarga de cambiar de fase según la respuesta recibida.*/
                    //val answer =
                    if(sc.nextBoolean() && !checking){
                        waiting = if(!waiting){
                            workingDialog.showWaitDialog()
                            true
                        } else {
                            workingDialog.hideDialog()
                            false
                        }
                        if (waiting)
                            continue
                        else {
                            if (bingo) {
                                bingo = false
                                val intent = Intent(applicationContext, InfoActivity::class.java)
                                startActivity(intent)
                            } else
                                bingo = true
                        }
                    } else {
                        if(waiting){
                            workingDialog.hideDialog()
                            waiting = false
                        }
                    }
                }
            }
        }
    }

    /*Con este método de aquí inicializaremos los métodos ubicados en la interface creada en el
    adapter, usados como es obvio por sus nombres para enviar arrays de números al servidor para
    que se comprueben.*/
    private fun iniciaSenders(){
        sheetAdapter.onCallListener = object:
        Sheet90Adapter.OnCallListener {

            /*Este método se encargará de enviar una línea concreta y cifrada al servidor para su
            verificación, tanto de contenido como de validez.*/
            override fun sendLineForCheck(carton: List<IntArray>, line: Int, view: Button) {
                checking = true
                var errorInSending = false
                Executors.newSingleThreadExecutor().execute {
                    val linea:IntArray = carton[line - 1]
                    pw.println(linea.contentToString())
                    if(!sc.nextBoolean()){
                        errorInSending = true
                    }
                    while (conexion.isConnected || !errorInSending) {
                        if (sc.hasNextBoolean()){
                            if (sc.nextBoolean()) {
                                points++
                                bingo = true
                                break
                            } else {
                                strikes++
                                break
                            }
                        }
                    }
                    checking = false
                }
                Handler(Looper.getMainLooper()).postDelayed({
                    if(errorInSending)
                        Toast.makeText(this@GameActivity, "Ha ocurrido un error al envíar" +
                                "la línea! Contacte a Soporte.", Toast.LENGTH_LONG).show()
                    else {
                        workingDialog.showCheckingDialog()
                        Handler(Looper.getMainLooper()).postDelayed({
                            workingDialog.hideDialog()
                            if (bingo){
                                workingDialog.showCorrectDialog("Linea Correcta!")
                                Handler(Looper.getMainLooper()).postDelayed({
                                    workingDialog.hideDialog()
                                }, 2500)
                                view.setText(R.string.bingo)
                                view.visibility = View.INVISIBLE
                            } else {
                                workingDialog.showWrongDialog("Linea Incorrecta!")
                                Handler(Looper.getMainLooper()).postDelayed({
                                    workingDialog.hideDialog()
                                }, 2500)
                            }
                        }, 3000)
                    }
                },1000)
            }

            /*Este otro método se encargará de enviar el bingo completo y
            cifrado al servidor para su verificación, tanto de contenido como de validez.*/
            override fun sendBingoForCheck(carton: List<IntArray>) {
                checking = true
                var errorInSending = false
                Executors.newSingleThreadExecutor().execute {
                    val sheet: IntArray = carton[0] + carton[1] + carton[2]
                    pw.println(sheet.contentToString())
                    if(!sc.nextBoolean()){
                        errorInSending = true
                    }
                    while (conexion.isConnected || !errorInSending) {
                        if (sc.hasNextBoolean()){
                            if (sc.nextBoolean()) {
                                points+3
                                break
                            } else {
                                strikes++
                                break
                            }
                        }
                    }
                    checking = false
                }
                Handler(Looper.getMainLooper()).postDelayed({
                    if(errorInSending)
                        Toast.makeText(this@GameActivity, "Ha ocurrido un error al envíar" +
                                "el bingo! Contacte a Soporte.", Toast.LENGTH_LONG).show()
                    else {
                        workingDialog.showCheckingDialog()
                        Handler(Looper.getMainLooper()).postDelayed({
                            workingDialog.hideDialog()
                            if(!bingo){
                                workingDialog.showCorrectDialog("Bingo Correcto!")
                                Handler(Looper.getMainLooper()).postDelayed({
                                    workingDialog.hideDialog()
                                }, 2500)
                                bingo = false
                                val intent = Intent(applicationContext, InfoActivity::class.java)
                                startActivity(intent)
                            } else {
                                workingDialog.showWrongDialog("Bingo Incorrecto!")
                                Handler(Looper.getMainLooper()).postDelayed({
                                    workingDialog.hideDialog()
                                }, 2500)
                            }
                        }, 3000)
                    }
                }, 1000)
            }
        }
    }
}