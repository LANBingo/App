package net.iessochoa.lanbingo.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import net.iessochoa.lanbingo.GameActivity
import net.iessochoa.lanbingo.R
import net.iessochoa.lanbingo.databinding.ActivityMainScreenBinding
import net.iessochoa.lanbingo.dialogs.*
import java.io.PrintWriter
import java.net.Socket
import java.util.Scanner
import java.util.concurrent.Executors

class MainScreenActivity : AppCompatActivity() {
    //Una variable estática que permitirá al ViewModel saber cuántos cartones quiere el juagdor.
    companion object {
        var nCartones = 1
        lateinit var conexion: Socket
        lateinit var nick: String
        const val TAG = "MainScreen"
    }

    private lateinit var binding:ActivityMainScreenBinding

    // Objeto que muestra diálogos con animaciones al usuario para una app más vistosa
    private lateinit var workingDialog: WorkingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.sbNcartones.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, progreso: Int, p2: Boolean) {
                binding.tvNcartones.text = getString(R.string.nCartones, progreso+1)
                nCartones = progreso+1
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {
            }
            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })
        binding.sbNcartones.progress=0
        binding.tvNcartones.text = getString(R.string.nCartones,1)

        workingDialog = WorkingDialog(this)

        binding.btBegin90.setOnClickListener{
            Log.d(TAG, "PRUEBA DE TAG")
            workingDialog.showLoadingDialog()
            nick = binding.etUsername.text.toString()
            connectWithServer()
        }
    }

    /*Método que se encarga de conectar al Cliente con el Servidor enviándole primero el nick y la
    dirección del jugador para diferenciar a los distintos jugadores.*/
    private fun connectWithServer(){
        /*Handler(Looper.getMainLooper()).postDelayed({
            workingDialog.hideDialog()
            val intent = Intent(applicationContext, GameActivity::class.java)
            startActivity(intent)
        }, 4000)*/

        Executors.newSingleThreadExecutor().execute {
            conexion = Socket("192.168.1.98", 19012)
            val printWriter = PrintWriter(conexion.getOutputStream(), true)
            val scanner = Scanner(conexion.getInputStream())
            while (conexion.isConnected){
                printWriter.println(nick)
                Thread.sleep(2500)
                while (scanner.hasNextLine()) {
                    val msg = scanner.nextLine()
                    if(msg.isNotEmpty()){
                        Log.d(TAG, msg)
                        break
                    }
                }
            }
        }
        val intent = Intent(applicationContext, GameActivity::class.java)
        startActivity(intent)
    }
}