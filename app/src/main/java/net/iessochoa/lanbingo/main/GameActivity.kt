package net.iessochoa.lanbingo.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ToggleButton
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import net.iessochoa.lanbingo.R
import net.iessochoa.lanbingo.adapters.Sheet90Adapter
import net.iessochoa.lanbingo.databinding.ActivityGameBinding
import net.iessochoa.lanbingo.dialogs.StatusDialog
import net.iessochoa.lanbingo.main.MainScreenActivity.Companion.TAG
import net.iessochoa.lanbingo.main.MainScreenActivity.Companion.conexion
import net.iessochoa.lanbingo.main.MainScreenActivity.Companion.workingDialog
import net.iessochoa.lanbingo.viewmodels.Bingo90ViewModel
import java.io.PrintWriter
import java.util.Scanner
import java.util.concurrent.Executors

class GameActivity : AppCompatActivity() {
    //Una variable estática que permitirá al adaptador saber en qué fase se encuentra la partida.
    companion object{
        var bingo = false
        var points = 0
        var strikes = 0
    }

    private lateinit var binding: ActivityGameBinding

    // Creamos un objeto del ViewModel para poder acceder a los cartones generados
    private val model: Bingo90ViewModel by viewModels()

    // Un objeto de adaptador para asignar al recyclerView que contiene los cartones
    private lateinit var sheetAdapter: Sheet90Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        workingDialog.hideDialog()
        workingDialog.changeContext(this)

        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sheetAdapter = model.getAdapter()

        binding.btStatus.setOnClickListener{
            StatusDialog(this)
        }

        iniciaRvSheets()
        iniciaSenders()
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
    /*Con este método de aquí inicializaremos los métodos ubicados en la interface creada en el
    adapter, usados como es obvio por sus nombres para enviar arrays de números al servidor para
    que se comprueben.*/
    private fun iniciaSenders(){
        sheetAdapter.onCallListener = object:
        Sheet90Adapter.OnCallListener {
            //Este método se encargará de enviar una línea concreta al servidor para su verificación.
            override fun sendLineForCheck(carton: List<IntArray>, line: Int, view: ToggleButton) {
                workingDialog.showCheckingDialog()
                Executors.newSingleThreadExecutor().execute {
                    val pw = PrintWriter(conexion.getOutputStream())
                    val sc = Scanner(conexion.getInputStream())
                    pw.println(carton[line - 1].toString())
                    while (conexion.isConnected) {
                        if (sc.hasNextBoolean()){
                            if (sc.nextBoolean()) {
                                workingDialog.hideDialog()
                                points++
                                bingo = true
                                view.setText(R.string.bingo)
                                view.visibility = View.INVISIBLE
                                workingDialog.showCorrectDialog("Linea Correcta!")
                                Handler(Looper.getMainLooper()).postDelayed({
                                    workingDialog.hideDialog()
                                }, 2500)
                                break
                            } else {
                                workingDialog.hideDialog()
                                strikes++
                                workingDialog.showWrongDialog("Linea Incorrecta!")
                                Handler(Looper.getMainLooper()).postDelayed({
                                    workingDialog.hideDialog()
                                }, 2500)
                                break
                            }
                        }
                    }
                }
                Log.d(TAG, carton.toString())
            }
            // Este otro método se encargará de enviar un cartón completo para su verificación.
            override fun sendBingoForCheck(carton: List<IntArray>, view: ToggleButton) {
                workingDialog.showCheckingDialog()
                val pw = PrintWriter(conexion.getOutputStream())
                val sc = Scanner(conexion.getInputStream())
                pw.println(carton.toString())
                while (conexion.isConnected) {
                    if (sc.hasNextBoolean()){
                        if (sc.nextBoolean()) {
                            workingDialog.hideDialog()

                            points+3
                            workingDialog.showCorrectDialog("Bingo Correcto!")
                            Handler(Looper.getMainLooper()).postDelayed({
                                workingDialog.hideDialog()
                            }, 2500)
                            break
                        } else {
                            workingDialog.hideDialog()
                            strikes++
                            workingDialog.showWrongDialog("Bingo Incorrecto!")
                            Handler(Looper.getMainLooper()).postDelayed({
                                workingDialog.hideDialog()
                            }, 2500)
                            break
                        }
                    }
                }
            }
        }
    }
}