package net.iessochoa.lanbingo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import net.iessochoa.lanbingo.adapters.Sheet90Adapter
import net.iessochoa.lanbingo.databinding.ActivityGameBinding
import net.iessochoa.lanbingo.dialogs.StatusDialog
import net.iessochoa.lanbingo.dialogs.WorkingDialog
import net.iessochoa.lanbingo.viewmodels.Bingo90ViewModel

class GameActivity : AppCompatActivity() {
    //Una variable estática que permitirá al adaptador saber en qué fase se encuentra la partida.
    companion object{
        var bingo = false
        var points = 0
        var strikes = 0
    }

    private lateinit var binding: ActivityGameBinding

    // Objeto que muestra diálogos con animaciones al usuario para una app más vistosa
    private lateinit var workingDialog: WorkingDialog

    // Creamos un objeto del ViewModel para poder acceder a los cartones generados
    private val model: Bingo90ViewModel by viewModels()

    // Un objeto de adaptador para asignar al recyclerView que contiene los cartones
    private lateinit var sheetAdapter: Sheet90Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        workingDialog = WorkingDialog(this)
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
            override fun sendLineForCheck(carton: List<IntArray>, line: Int): Boolean {
                workingDialog.showCheckingDialog()
                var result = false
                Handler(Looper.getMainLooper()).postDelayed({
                    workingDialog.hideDialog()
                    if(result){
                        workingDialog.showCorrectDialog("Linea Correcta!")
                        Handler(Looper.getMainLooper()).postDelayed({
                            workingDialog.hideDialog()
                            points++
                        }, 2500)
                    } else {
                        workingDialog.showWrongDialog("Linea Incorrecta!")
                        Handler(Looper.getMainLooper()).postDelayed({
                            workingDialog.hideDialog()
                        }, 2500)
                    }
                    }, 10000)
                result = when(line){
                    1-> {
                        //if(sendLine(carton[0]))
                        true
                        //else
                        //false
                    }
                    2-> {
                        //if(sendLine(carton[0]))
                        true
                        //else
                        //false
                    }
                    3-> {
                        //if(sendLine(carton[0]))
                        true
                        //else
                        //false
                    }
                    else -> false
                }
                return result
            }
            // Este otro método se encargará de enviar un cartón completo para su verificación.
            override fun sendBingoForCheck(carton: List<IntArray>): Boolean {
                workingDialog.showCheckingDialog()
                var result = false
                Handler(Looper.getMainLooper()).postDelayed({
                    workingDialog.hideDialog()
                    if(result){
                        workingDialog.showCorrectDialog("Bingo Correcto!")
                        Handler(Looper.getMainLooper()).postDelayed({
                            workingDialog.hideDialog()
                            points+=3
                        }, 2500)
                    } else {
                        workingDialog.showWrongDialog("Bingo Incorrecto!")
                        Handler(Looper.getMainLooper()).postDelayed({
                            workingDialog.hideDialog()
                            strikes++
                        }, 2500)
                    }
                }, 3000)
                result = strikes != 0
                return result
            }
        }
    }
}