package net.iessochoa.lanbingo.adapters

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ToggleButton
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import net.iessochoa.lanbingo.main.GameActivity.Companion.bingo
import net.iessochoa.lanbingo.R
import net.iessochoa.lanbingo.databinding.Bingo90sheetBinding

// Clase Creada para estilizar e inicializar el RecyclerView, además de para contener los métodos
// que se activan gracias a la interacción con los cartones
// Implementación de los metodos e Inicializadores por Juan Antonio Nicolás
class Sheet90Adapter(sheets: List<List<IntArray>>, status: List<BooleanArray>) :
    RecyclerView.Adapter<Sheet90Adapter.SheetViewHolder> () {

    //Una lista que contiene todos los cartones del jugador
    private val sheetList:List<List<IntArray>> = sheets
    //Una lista que contiene el estado de todos los cartones del jugador, para evitar pérdidas de info.
    private val statusList:List<BooleanArray> = status
    //Objeto que hace referencia a la interfaz ubicada más abajo, en esta misma clase.
    lateinit var onCallListener:OnCallListener

    inner class SheetViewHolder(val binding: Bingo90sheetBinding)
        : RecyclerView.ViewHolder(binding.root) {
            init {
                binding.tgCell1.setOnCheckedChangeListener{ _, isChecked ->
                    checkSheet(this)
                    statusList[this.adapterPosition][0] = isChecked
                }
                binding.tgCell2.setOnCheckedChangeListener{ _, isChecked ->
                    checkSheet(this)
                    statusList[this.adapterPosition][1] = isChecked
                }
                binding.tgCell3.setOnCheckedChangeListener{ _, isChecked ->
                    checkSheet(this)
                    statusList[this.adapterPosition][2] = isChecked
                }
                binding.tgCell4.setOnCheckedChangeListener{ _, isChecked ->
                    checkSheet(this)
                    statusList[this.adapterPosition][3] = isChecked
                }
                binding.tgCell5.setOnCheckedChangeListener{ _, isChecked ->
                    checkSheet(this)
                    statusList[this.adapterPosition][4] = isChecked
                }
                binding.tgCell6.setOnCheckedChangeListener{ _, isChecked ->
                    checkSheet(this)
                    statusList[this.adapterPosition][5] = isChecked
                }
                binding.tgCell7.setOnCheckedChangeListener{ _, isChecked ->
                    checkSheet(this)
                    statusList[this.adapterPosition][6] = isChecked
                }
                binding.tgCell8.setOnCheckedChangeListener{ _, isChecked ->
                    checkSheet(this)
                    statusList[this.adapterPosition][7] = isChecked
                }
                binding.tgCell9.setOnCheckedChangeListener{ _, isChecked ->
                    checkSheet(this)
                    statusList[this.adapterPosition][8] = isChecked
                }
                binding.tgCell10.setOnCheckedChangeListener{ _, isChecked ->
                    checkSheet(this)
                    statusList[this.adapterPosition][9] = isChecked
                }
                binding.tgCell11.setOnCheckedChangeListener{ _, isChecked ->
                    checkSheet(this)
                    statusList[this.adapterPosition][10] = isChecked
                }
                binding.tgCell12.setOnCheckedChangeListener{ _, isChecked ->
                    checkSheet(this)
                    statusList[this.adapterPosition][11] = isChecked
                }
                binding.tgCell13.setOnCheckedChangeListener{ _, isChecked ->
                    checkSheet(this)
                    statusList[this.adapterPosition][12] = isChecked
                }
                binding.tgCell14.setOnCheckedChangeListener{ _, isChecked ->
                    checkSheet(this)
                    statusList[this.adapterPosition][13] = isChecked
                }
                binding.tgCell15.setOnCheckedChangeListener{ _, isChecked ->
                    checkSheet(this)
                    statusList[this.adapterPosition][14] = isChecked
                }
                binding.tgCell16.setOnCheckedChangeListener{ _, isChecked ->
                    checkSheet(this)
                    statusList[this.adapterPosition][15] = isChecked
                }
                binding.tgCell17.setOnCheckedChangeListener{ _, isChecked ->
                    checkSheet(this)
                    statusList[this.adapterPosition][16] = isChecked
                }
                binding.tgCell18.setOnCheckedChangeListener{ _, isChecked ->
                    checkSheet(this)
                    statusList[this.adapterPosition][17] = isChecked
                }
                binding.tgCell19.setOnCheckedChangeListener{ _, isChecked ->
                    checkSheet(this)
                    statusList[this.adapterPosition][18] = isChecked
                }
                binding.tgCell20.setOnCheckedChangeListener{ _, isChecked ->
                    checkSheet(this)
                    statusList[this.adapterPosition][19] = isChecked
                }
                binding.tgCell21.setOnCheckedChangeListener{ _, isChecked ->
                    checkSheet(this)
                    statusList[this.adapterPosition][20] = isChecked
                }
                binding.tgCell22.setOnCheckedChangeListener{ _, isChecked ->
                    checkSheet(this)
                    statusList[this.adapterPosition][21] = isChecked
                }
                binding.tgCell23.setOnCheckedChangeListener{ _, isChecked ->
                    checkSheet(this)
                    statusList[this.adapterPosition][22] = isChecked
                }
                binding.tgCell24.setOnCheckedChangeListener{ _, isChecked ->
                    checkSheet(this)
                    statusList[this.adapterPosition][23] = isChecked
                }
                binding.tgCell25.setOnCheckedChangeListener{ _, isChecked ->
                    checkSheet(this)
                    statusList[this.adapterPosition][24] = isChecked
                }
                binding.tgCell26.setOnCheckedChangeListener{ _, isChecked ->
                    checkSheet(this)
                    statusList[this.adapterPosition][25] = isChecked
                }
                binding.tgCell27.setOnCheckedChangeListener{ _, isChecked ->
                    checkSheet(this)
                    statusList[this.adapterPosition][26] = isChecked
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SheetViewHolder {
        val binding = Bingo90sheetBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return SheetViewHolder(binding)
    }

    override fun onBindViewHolder(sheetViewHolder: SheetViewHolder, pos: Int) {
        //Nos pasan la posición del item a mostrar en el viewHolder
        with(sheetViewHolder) {
            /*Cogemos la lista de cartones, creamos una lista secundaria para acceder a las lineas
            y procedemos a asignar los estilos a los toggleButton que identifican los números en
            el cartón de manera gráfica */
            with(sheetList[pos]) {
                var lines = get(0)
                binding.tgCell1.background = getResource(lines[0], binding.tgCell1)
                binding.tgCell2.background = getResource(lines[1], binding.tgCell2)
                binding.tgCell3.background = getResource(lines[2], binding.tgCell3)
                binding.tgCell4.background = getResource(lines[3], binding.tgCell4)
                binding.tgCell5.background = getResource(lines[4], binding.tgCell5)
                binding.tgCell6.background = getResource(lines[5], binding.tgCell6)
                binding.tgCell7.background = getResource(lines[6], binding.tgCell7)
                binding.tgCell8.background = getResource(lines[7], binding.tgCell8)
                binding.tgCell9.background = getResource(lines[8], binding.tgCell9)
                lines = get(1)
                binding.tgCell10.background = getResource(lines[0], binding.tgCell10)
                binding.tgCell11.background = getResource(lines[1], binding.tgCell11)
                binding.tgCell12.background = getResource(lines[2], binding.tgCell12)
                binding.tgCell13.background = getResource(lines[3], binding.tgCell13)
                binding.tgCell14.background = getResource(lines[4], binding.tgCell14)
                binding.tgCell15.background = getResource(lines[5], binding.tgCell15)
                binding.tgCell16.background = getResource(lines[6], binding.tgCell16)
                binding.tgCell17.background = getResource(lines[7], binding.tgCell17)
                binding.tgCell18.background = getResource(lines[8], binding.tgCell18)
                lines = get(2)
                binding.tgCell19.background = getResource(lines[0], binding.tgCell19)
                binding.tgCell20.background = getResource(lines[1], binding.tgCell20)
                binding.tgCell21.background = getResource(lines[2], binding.tgCell21)
                binding.tgCell22.background = getResource(lines[3], binding.tgCell22)
                binding.tgCell23.background = getResource(lines[4], binding.tgCell23)
                binding.tgCell24.background = getResource(lines[5], binding.tgCell24)
                binding.tgCell25.background = getResource(lines[6], binding.tgCell25)
                binding.tgCell26.background = getResource(lines[7], binding.tgCell26)
                binding.tgCell27.background = getResource(lines[8], binding.tgCell27)
                /* Iniciamos el texto del botón de cantar. Al principio de la partida sera 'LINEA'
                pero al pasar de ronda este cambiará a 'BINGO' usando un pequeño código ubicado en
                el clickListener de abajo. También como es obvio, debemos ocultarlo de la vista
                hasta que sea necesario */
                binding.btCall.setText(R.string.linea)
                binding.btCall.visibility = View.INVISIBLE
                //Desde aquí iniciamos el clickListener del boton para cantar. Lo inicializamos desde
                // aquí debido a que el metodo al que llama usa la lista que utilizamos con este with
                // para asignar los numeros del carton
                binding.btCall.setOnClickListener{
                    if(!bingo)
                        onCallListener.sendLineForCheck(this, findLine(pos), it as Button)
                    else
                        onCallListener.sendBingoForCheck(this)

                }
            }
            /*Cogemos la lista de estados y la aplicamos a los ToggleButton. De esta manera,
            evitaremos que los cartones se reinicien cada vez que giremos la pantalla.*/
            with(statusList[pos]){
                binding.tgCell1.isChecked = get(0)
                binding.tgCell2.isChecked = get(1)
                binding.tgCell3.isChecked = get(2)
                binding.tgCell4.isChecked = get(3)
                binding.tgCell5.isChecked = get(4)
                binding.tgCell6.isChecked = get(5)
                binding.tgCell7.isChecked = get(6)
                binding.tgCell8.isChecked = get(7)
                binding.tgCell9.isChecked = get(8)
                binding.tgCell10.isChecked = get(9)
                binding.tgCell11.isChecked = get(10)
                binding.tgCell12.isChecked = get(11)
                binding.tgCell13.isChecked = get(12)
                binding.tgCell14.isChecked = get(13)
                binding.tgCell15.isChecked = get(14)
                binding.tgCell16.isChecked = get(15)
                binding.tgCell17.isChecked = get(16)
                binding.tgCell18.isChecked = get(17)
                binding.tgCell19.isChecked = get(18)
                binding.tgCell20.isChecked = get(19)
                binding.tgCell21.isChecked = get(20)
                binding.tgCell22.isChecked = get(21)
                binding.tgCell23.isChecked = get(22)
                binding.tgCell24.isChecked = get(23)
                binding.tgCell25.isChecked = get(24)
                binding.tgCell26.isChecked = get(25)
                binding.tgCell27.isChecked = get(26)
            }
        }
    }

    override fun getItemCount(): Int = sheetList.size

    //Este método es el encargado de asignar los estilos a los toggleButton del cartón
    // según el número asignado en el array de Integer
    private fun getResource(number: Int, button:ToggleButton): Drawable? {
        //Si equivale a 0 debemos dejar la celda vacía
        if(number == 0) {
            button.isChecked = true
            button.isEnabled = false
            return AppCompatResources.getDrawable(button.context, R.drawable.ic_tgnull_bg)
        }
        //Si no equivale a 0 debemos buscar con el número pasado por parámetro el estilo
        // correspondiente a asignar en la celda
        //Si es menor que 10 debemos realizar una búsqueda algo distinta
        else if(number < 10){
            val id = button.context.resources.getIdentifier("ic_tg0${number}_bg", "drawable", button.context.packageName)
            return AppCompatResources.getDrawable(button.context, id)
        }
        val id = button.context.resources.getIdentifier("ic_tg${number}_bg", "drawable", button.context.packageName)
        return AppCompatResources.getDrawable(button.context, id)
    }

    //
    private fun checkSheet(sheetViewHolder: SheetViewHolder) {
        with(sheetViewHolder){
            if(bingo)
                binding.btCall.setText(R.string.bingo)
            val sheetGroup = arrayOf(
                arrayOf(
                    binding.tgCell1, binding.tgCell2, binding.tgCell3,
                    binding.tgCell4, binding.tgCell5, binding.tgCell6,
                    binding.tgCell7, binding.tgCell8, binding.tgCell9
                ),
                arrayOf(
                    binding.tgCell10, binding.tgCell11, binding.tgCell12,
                    binding.tgCell13, binding.tgCell14, binding.tgCell15,
                    binding.tgCell16, binding.tgCell17, binding.tgCell18
                ),
                arrayOf(
                    binding.tgCell19, binding.tgCell20, binding.tgCell21,
                    binding.tgCell22, binding.tgCell23, binding.tgCell24,
                    binding.tgCell25, binding.tgCell26, binding.tgCell27
                ))
            var lines = 0
            for(i in 0..2){
                var cell = 0
                sheetGroup[i].forEach {
                    if(it.isChecked)
                        cell++
                }
                if (cell == 9){
                    if(bingo) {
                        lines++
                        if(binding.btCall.text.equals(R.string.linea))
                            binding.btCall.setText(R.string.bingo)
                    } else {
                        binding.btCall.visibility = View.VISIBLE
                        return
                    }
                } else {
                    binding.btCall.visibility = View.INVISIBLE
                }
            }
            if(lines == 3) {
                binding.btCall.visibility = View.VISIBLE
            } else
                binding.btCall.visibility = View.INVISIBLE
        }
    }

    private fun findLine(pos: Int): Int{
        var line = 1
        var goodCells = 0
        for ((cellsChecked, b) in statusList[pos].withIndex()) {
            if(cellsChecked/9 == line && goodCells == 9)
                return line
            else if(cellsChecked/9 == line){
                line++
                goodCells = 0
            }
            if(b)
                goodCells++
        }
        return 0
    }

    /*Interfaz creada para poder enviar al servidor, a través de 2 métodos distintos, listas
    de integer que deben comprobarse.*/
    interface OnCallListener{
        fun sendLineForCheck(carton:List<IntArray>, line:Int, view: Button)

        fun sendBingoForCheck(carton: List<IntArray>)
    }
}