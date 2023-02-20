package net.iessochoa.lanbingo.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.SeekBar
import net.iessochoa.lanbingo.GameActivity
import net.iessochoa.lanbingo.R
import net.iessochoa.lanbingo.databinding.ActivityMainScreenBinding
import net.iessochoa.lanbingo.dialogs.*

class MainScreenActivity : AppCompatActivity() {

    companion object {
        var nCartones = 1
    }

    private lateinit var binding:ActivityMainScreenBinding

    //
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
            workingDialog.showLoadingDialog()
            connectWithServer()
        }
    }

    /**/
    private fun connectWithServer(){
        val timer = object: CountDownTimer(5000, 5000) {
            override fun onTick(timer: Long) {
                /*if(Random.nextBoolean()){
                    waitingDialog.hideDialog()
                    Toast.makeText(applicationContext, "Conexion Correcta", Toast.LENGTH_SHORT).show()
                    val intent = Intent(applicationContext, Bingo90Activity::class.java)
                    startActivity(intent)
                    cancel()
                }*/
            }

            override fun onFinish() {
                //Toast.makeText(applicationContext, "Error, No se ha podido conectar", Toast.LENGTH_SHORT).show()
                workingDialog.hideDialog()
                val intent = Intent(applicationContext, GameActivity::class.java)
                startActivity(intent)
            }
        }
        timer.start()
    }
}