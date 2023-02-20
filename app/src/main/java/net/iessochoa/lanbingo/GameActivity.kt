package net.iessochoa.lanbingo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import net.iessochoa.lanbingo.adapters.Sheet90Adapter
import net.iessochoa.lanbingo.databinding.ActivityGameBinding
import net.iessochoa.lanbingo.dialogs.WorkingDialog
import net.iessochoa.lanbingo.viewmodels.Bingo90ViewModel

class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding

    //
    private lateinit var workingDialog: WorkingDialog

    // Creamos un objeto del ViewModel para poder acceder a los cartones generados
    private val model: Bingo90ViewModel by viewModels()

    //
    private lateinit var sheetAdapter: Sheet90Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        workingDialog = WorkingDialog(this)
        sheetAdapter = model.getAdapter()

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

    private fun iniciaSenders(){
        sheetAdapter.onCallListener = object:
        Sheet90Adapter.OnCallListener {
            /* Este método lo usaremos para enviar, según corresponda, un línea concreta o el cartón al
            completo para su comprobación desde el programa principal:*/
            override fun sendLineForCheck(carton: List<IntArray>, line: Int): Boolean {
                workingDialog.showCheckingDialog()
                var result = false
                Handler(Looper.getMainLooper()).postDelayed({
                        result = when(line){
                            1 -> {
                                workingDialog.hideDialog()
                                workingDialog.showCorrectDialog("Linea Correcta!")
                                Handler(Looper.getMainLooper()).postDelayed({
                                    workingDialog.hideDialog()
                                }, 3000)
                                true
                            }
                            2 -> {
                                workingDialog.hideDialog()
                                true
                            }
                            3 -> {
                                workingDialog.hideDialog()
                                true
                            }
                            else -> {
                                workingDialog.hideDialog()
                                /*wrongDialog.title = "Bingo Incorrecto!"
                                wrongDialog.show(parentFragmentManager, "Wrong")
                                Handler(Looper.getMainLooper()).postDelayed({
                                    wrongDialog.dismiss()
                                }, 3000)*/
                                true
                            }
                        }
                    }, 5000)
                    return result
                }

            override fun sendBingoForCheck(carton: List<IntArray>) {
                Toast.makeText(applicationContext, "No Implementado", Toast.LENGTH_SHORT).show()
            }
        }
    }
}