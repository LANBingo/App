package net.iessochoa.lanbingo.main

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import net.iessochoa.lanbingo.R
import net.iessochoa.lanbingo.databinding.ActivityMainScreenBinding
import net.iessochoa.lanbingo.dialogs.*
import java.io.PrintWriter
import java.net.ConnectException
import java.net.Socket
import java.util.*
import java.util.concurrent.Executors

class MainScreenActivity : AppCompatActivity()  {
    // Unas cuantas variables que deben ser accesibles desde cualquier clase:
    companion object {
        // Variable que permitirá al ViewModel saber cuántos cartones quiere el juagdor.
        var nCartones = 1
        // Objeto que almacenará el Socket que ha conectado con el servidor
        lateinit var conexion: Socket
        // Variable que almacenará el nombre del jugador
        lateinit var nick: String
        // Variable para mostrar mensajes personalizados por el log para pruebas
        const val TAG = "LANBingo"
        // Objeto que muestra diálogos con animaciones al usuario para una app más vistosa
        lateinit var workingDialog: WorkingDialog
        // Un método que devuelve el estado de la conexión: CONECTADO/NO_CONECTADO
        fun initConexion(): Boolean{
            return this::conexion.isInitialized
        }
    }

    private lateinit var binding:ActivityMainScreenBinding

    // Variables para hacer funcionar los tabs que permiten desplazarse entre los menús de Jugar y Conexión
    private lateinit var tabJugar: TextView
    private lateinit var tabConexion: TextView
    private lateinit var select: TextView
    private lateinit var def: ColorStateList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Iniciamos el diálogo y le establecemos el contexto de esta activity
        workingDialog = WorkingDialog()
        workingDialog.changeContext(this)

        /* Iniciamos el adaptador para el PageViewer y se lo asignamos al PageViewer de esta
        activity. Este adaptador no es muy necesario para funcionalidad, es más cosmético ya que
        permite al usuario cambiar entre menús deslizando a un lado u otro la pantalla.*/
        val adapter = TabPageAdapter(this, 2)
        binding.viewPager.adapter = adapter

        // Iniciamos los elementos implicados en la funcionalidad de los Tabs
        tabJugar = findViewById(R.id.tabJugar)
        tabConexion = findViewById(R.id.tabConexion)
        select = findViewById(R.id.select)
        def = tabConexion.textColors

        /* Iniciamos los clickListener de los elementos del Tab para que cuando se pulse sobre ellos
        el menú mostrado cambie */
        tabJugar.setOnClickListener{changeState(it)}
        tabConexion.setOnClickListener{changeState(it)}

        // Este método permite que el usuario sea capaz de cambiar los menús al deslizar
        binding.viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> changeState(tabJugar)
                    1 -> changeState(tabConexion)
                    else -> changeState(tabJugar)
                }
            }
        })
    }

    // Y este otro método implicado en los Tab
    private fun changeState(view: View){
        if(view.id == R.id.tabJugar){
            binding.viewPager.currentItem = 0
            tabConexion.setTextColor(def)
            select.animate().x(0F).duration = 100
            tabJugar.setTextColor(Color.WHITE)
            Handler(Looper.getMainLooper()).postDelayed({
                initPlayFragment()
            }, 500)
        } else if (view.id == R.id.tabConexion){
            val size: Float = tabConexion.width.toFloat()
            select.animate().x(size).duration = 100
            tabJugar.setTextColor(def)
            tabConexion.setTextColor(Color.WHITE)
            binding.viewPager.currentItem = 1
            Handler(Looper.getMainLooper()).postDelayed({
                initConnectionFragment()
            }, 500)
        }
    }

    private fun initPlayFragment(){
        val btBegin90:Button = findViewById(R.id.btBegin90)
        val etUsername:EditText = findViewById(R.id.etUsername)

        btBegin90.setOnClickListener{
            Log.d(TAG, "PRUEBA DE TAG")
            if(!initConexion()) {
                Snackbar.make(binding.root, "No se ha establecido ninguna conexión!",
                    Snackbar.LENGTH_LONG).setAction("ESTABLECER"){changeState(tabConexion)}.show()
                return@setOnClickListener
            }
            workingDialog.showLoadingDialog()
            nick = etUsername.text.toString()
            connectWithServer()
        }
    }

    private fun initConnectionFragment(){
        val btTryConexion:Button = findViewById(R.id.btTryConexion)
        val etAddress:EditText = findViewById(R.id.etAddress)
        val etPort:EditText = findViewById(R.id.etPort)

        btTryConexion.setOnClickListener{
            if(etAddress.text.isEmpty() || etPort.text.isEmpty()){
                Toast.makeText(this, "Completa Todos los Campos!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val address = etAddress.text.toString()
            val port = Integer.parseInt(etPort.text.toString())
            if(Patterns.IP_ADDRESS.matcher(address).matches())
                tryConnection(address,port)
            else
                Toast.makeText(this, "La IP introducida no es válida", Toast.LENGTH_SHORT).show()
        }
    }

    /*Método encargado de corroborar que la conexión al servidor se realice correctamente.*/
    private fun tryConnection(address: String, port: Int){
        Executors.newSingleThreadExecutor().execute {
            try{
                conexion = Socket(address, port)
            }catch (e: ConnectException){
                e.printStackTrace()
            }
        }
        Handler(Looper.getMainLooper()).postDelayed({
            if(initConexion())
                Toast.makeText(this, "Conexión Correcta", Toast.LENGTH_SHORT).show()
            else
                Toast.makeText(this, "Error, no se ha podido conectar", Toast.LENGTH_SHORT).show()
        }, 500)
    }

    //Método que se encarga de enviar el nick del jugador al servidor para registrarlo.
    private fun connectWithServer(){
        /*Handler(Looper.getMainLooper()).postDelayed({
            workingDialog.hideDialog()
            val intent = Intent(applicationContext, GameActivity::class.java)
            startActivity(intent)
        }, 4000)*/

        Executors.newSingleThreadExecutor().execute {
            val printWriter = PrintWriter(conexion.getOutputStream(), true)
            val scanner = Scanner(conexion.getInputStream())
            printWriter.println(nick)
            val msg = intArrayOf(1, 2, 30, 4, 5)
            while (conexion.isConnected){
                printWriter.println(msg.contentToString())
            }
        }
        //val intent = Intent(applicationContext, GameActivity::class.java)
        //startActivity(intent)
    }
}