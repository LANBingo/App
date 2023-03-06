package net.iessochoa.lanbingo.main

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import net.iessochoa.lanbingo.R
import net.iessochoa.lanbingo.adapters.TabPageAdapter
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
        // Variable que permitirá al ViewModel saber cuántos cartones quiere el jugador.
        var nCartones = 1
        // Objeto que almacenará el Socket que ha conectado con el servidor.
        lateinit var conexion: Socket
        // Variable que almacenará el nombre del jugador.
        lateinit var nick: String
        // Variable para mostrar mensajes personalizados por el log para pruebas.
        const val TAG = "LANBingo"
        // Objeto que muestra diálogos con animaciones al usuario para una app más vistosa.
        lateinit var workingDialog: WorkingDialog
        // Un método que devuelve el estado de la conexión: CONECTADO/NO_CONECTADO.
        fun initConexion(): Boolean{
            return this::conexion.isInitialized && conexion.isConnected
        }
    }

    private lateinit var binding:ActivityMainScreenBinding

    // Variables para hacer funcionar los tabs que permiten desplazarse entre los menús de Jugar y Conexión.
    private lateinit var tabJugar: TextView
    private lateinit var tabConexion: TextView
    private lateinit var select: TextView
    private lateinit var def: ColorStateList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(intent.hasExtra("Expulsado"))
            Toast.makeText(this, "Se te ha expulsado del juego por acomular muchos strikes!", Toast.LENGTH_LONG).show()

        // Iniciamos el diálogo y le establecemos el contexto de esta activity.
        workingDialog = WorkingDialog()
        workingDialog.changeContext(this)

        /* Iniciamos el adaptador para el PageViewer y se lo asignamos al PageViewer de esta
        activity. Este adaptador no es muy necesario para funcionalidad, es más cosmético ya que
        permite al usuario cambiar entre menús deslizando a un lado u otro la pantalla.*/
        val adapter = TabPageAdapter(this, 2)
        binding.viewPager.adapter = adapter

        // Iniciamos los elementos implicados en la funcionalidad de los Tabs.
        tabJugar = findViewById(R.id.tabJugar)
        tabConexion = findViewById(R.id.tabConexion)
        select = findViewById(R.id.select)
        def = tabConexion.textColors

        /* Iniciamos los clickListener de los elementos del Tab para que cuando se pulse sobre ellos
        el menú mostrado cambie. */
        tabJugar.setOnClickListener{changeState(it)}
        tabConexion.setOnClickListener{changeState(it)}

        // Este método permite que el usuario sea capaz de cambiar los menús al deslizar.
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

    /* Y este otro método implicado en los Tab se encarga de cambiar las ventanas si se pulsa en
    los tabs, además de ejecutar la animación del TabMenu que simula el cambio y de iniciar los
    componentes de los fragmentos.*/
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

    /* Este método se encarga de inicializar los componentes del fragment Jugar, el cuál se usa para
    alamcenar el nick y los cartones solicitados por el jugador, además de llamar al método que
    envía el nick al servidor.*/
    private fun initPlayFragment(){
        val btBegin90:Button = findViewById(R.id.btBegin90)
        val etUsername:EditText = findViewById(R.id.etUsername)

        btBegin90.setOnClickListener{
            if(!initConexion()) {
                Snackbar.make(binding.root, "No se ha establecido ninguna conexión!",
                    Snackbar.LENGTH_LONG).setAction("ESTABLECER"){changeState(tabConexion)}.show()
                return@setOnClickListener
            }
            workingDialog.showLoadingDialog()
            if(etUsername.text.isEmpty()){
                Toast.makeText(this, "Necesitas un nombre para poder jugar", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            nick = etUsername.text.toString()
            connectWithServer()
        }
    }

    /* Este método de aquí se encarga de iniciar los componentes del fragmento Conexión, encargado
     de comprobar si las credenciales de conexión pasadas por el usuario sirven para establecer
     una conexión y llama al método para ello.*/
    private fun initConnectionFragment(){
        val btTryConexion:Button = findViewById(R.id.btTryConexion)
        val etAddress:EditText = findViewById(R.id.etAddress)
        val etPass:EditText = findViewById(R.id.etPass)

        btTryConexion.setOnClickListener{
            if(etAddress.text.isEmpty() || etPass.text.isEmpty()){
                Toast.makeText(this, "Completa Todos los Campos!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val address = etAddress.text.toString()
            val pass = CifradoNicolas.cifrador(etPass.text.toString())
            if(Patterns.IP_ADDRESS.matcher(address).matches())
                tryConnection(address, pass)
            else
                Toast.makeText(this, "La IP introducida no es válida", Toast.LENGTH_SHORT).show()
        }
    }

    // Método encargado de corroborar que la conexión al servidor se realice correctamente.
    private fun tryConnection(address: String, pass: String){
        Executors.newSingleThreadExecutor().execute {
            try{
                conexion = Socket(address, 5000)
                val pw = PrintWriter(conexion.getOutputStream())
                pw.println(pass)
                val sc = Scanner(conexion.getInputStream())
                if(!sc.nextBoolean()) {
                    conexion.close()
                    throw ConnectException()
                }
            }catch (e: ConnectException){
                e.printStackTrace()
            }
        }
        Handler(Looper.getMainLooper()).postDelayed({
            if(initConexion())
                Toast.makeText(this, "Conexión Correcta", Toast.LENGTH_SHORT).show()
            else
                Toast.makeText(this, "Error, no se ha podido conectar", Toast.LENGTH_SHORT).show()
        }, 1000)
    }

    /* Método que se encarga de enviar el nick del jugador al servidor para registrarlo, además de
     iniciar el juego aquí en la app.*/
    private fun connectWithServer(){
        Executors.newSingleThreadExecutor().execute {
            val printWriter = PrintWriter(conexion.getOutputStream(), true)
            val scanner = Scanner(conexion.getInputStream())
            printWriter.println(nick)
            while (conexion.isConnected){
                if(scanner.nextBoolean())
                    break
            }
            workingDialog.hideDialog()
            val intent = Intent(applicationContext, GameActivity::class.java)
            startActivity(intent)
        }
    }
}