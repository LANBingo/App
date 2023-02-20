package net.iessochoa.lanbingo.viewmodels

import androidx.lifecycle.ViewModel
import net.iessochoa.lanbingo.adapters.Sheet90Adapter
import net.iessochoa.lanbingo.main.MainScreenActivity
import kotlin.random.Random

// Clase creada para evitar el reinicio de los cartones con el mero hecho de girar la pantalla
// Clase implementada por Pedro Messeguer
// Métodos de la clase por Juan Antonio Nicolás
class Bingo90ViewModel : ViewModel() {

    // Aquí iniciamos una lista con los estados del cartón, para evitar que estos se reinicien cada
    // vez que la pantalla gire.
    private val sheetStatus: MutableList<BooleanArray> = mutableListOf()
    // Aquí iniciamos el adaptador con inicio tardío debido a que no podemos pasarle las listas vacías.
    private lateinit var sheetAdapter: Sheet90Adapter

    private val numCartones = MainScreenActivity.nCartones


    /* Gracias a este init llamaremos al método para generar los cartones y lo ejecutaremos en
         segundo plano para evitar que la app colapse.*/
    init{
        creaCartones()
    }

    // Con este método asignamos tantos cartones en la lista como tamaño se le haya establecido a
    // la lista que los contendrá
    private fun creaCartones() {
        val sheetList = mutableListOf<List<IntArray>>()
        for(i in 1..numCartones){
            /* Lo primero que debemos hacer es generar una Lista con 3 IntArray que simulará el
            carton de bingo.*/
            val carton:List<IntArray> = listOf(
                intArrayOf(1, 1, 1, 1, 1, 1, 1, 1, 1),
                intArrayOf(1, 1, 1, 1, 1, 1, 1, 1, 1),
                intArrayOf(1, 1, 1, 1, 1, 1, 1, 1, 1))
            /* Ahora procedemos a crear la plantilla del cartón. La plantilla consiste en modificar
            el cartón antes generado para establecer que celdas de este deben contener números y
            qué celdas no. Para ello usaremos el siguiente bucle:*/
            for(l in 0..2) {
                //Una variable que establece el número de espacios vacíos que debe contener una línea
                var zeros = 4
                //Una variable que indica la celda que estamos modificando actualmente
                var cell = 0
                //Un condicional que solo se activa al estar modificando la 3ra línea
                if(l == 2){
                    //Un bucle que permite acceder a todas las celdas de las 3 líneas
                    for(c in 0..8){
                        /*Si se da el caso de que las celdas de las 2 primeras lineas tienen
                        asignado un espacio para numero:*/
                        if(carton[0][c]+carton[1][c] == 2) {
                            //Debemos asignar un espacio vacío en la celda de la 3ra linea
                            carton[2][c] = 0
                            zeros--
                        }
                    }
                }
                //Un bucle que asignará los espacios vacíos en la linea que se está modificando actualmente
                while (zeros != 0) {
                    //Si ya se han recorrido todas las celdas volvemos a empezar a recorrerlas
                    if (cell > 8)
                        cell -= 9
                    //Si esa celda ya tiene un espacio vacío asignado o por el contrario,
                    //no debe tener un espacio asignado, nos movemos de celda
                    if (carton[l][cell] == 0 || carton[0][cell]+carton[1][cell] == 0) {
                        //Esta condicion se usa para recorrer de forma distinta las celdas de la 2da linea
                        if (l == 1)
                            cell += 2
                        else
                            cell++
                        continue
                    }
                    //Usamos un boolean aleatorio para decidir si en esa celda se asignará un número o nada
                    when (Random.nextBoolean()) {
                        true -> carton[l][cell] = 1
                        false -> {
                            carton[l][cell] = 0
                            zeros--
                        }
                    }
                    //Nos movemos de celda usando esta condición antes explicada
                    if (l == 1)
                        cell += 2
                    else
                        cell++
                }
            }
            /* Lo siguiente que haremos será generar los números del cartón, aleatoriamente también
            por supuesto. Usaremos el siguiente IntArray para ordenar los números en el cartón y un
            bucle que recorrerá nuevamente las lineas del cartón:*/
            val min = intArrayOf(1, 11, 21, 31, 41, 51, 61, 71, 81)
            for(l in 0..2) {
                for (c in 0..8) {
                    when (l) {
                        0 -> {
                            if (carton[0][c] == 1) {
                                val num = Random.nextInt(min[c], c * 10 + 7)
                                min[c] = num+1
                                carton[0][c] = num
                            }
                        }
                        1 -> {
                            if (carton[1][c] == 1) {
                                val num = Random.nextInt(min[c], c * 10 + 9)
                                min[c] = num+1
                                carton[1][c] = num
                            }
                        }
                        2 -> {
                            if (carton[2][c] == 1) {
                                val num = Random.nextInt(min[c], c * 10 + 11)
                                carton[2][c] = num
                            }
                        }
                    }
                }
            }

            /* Por último, agregamos el cartón a la lista de cartones y
            procedemos a generar el siguiente en caso de haberse solicitado más de uno.*/
            sheetList.add(carton)
        }
        generaEstados(sheetList.size)
        sheetAdapter = Sheet90Adapter(sheetList, sheetStatus)
    }

    /* Con este método devolveremos el adaptador que contiene los cartones para evitar generarlos
    de nuevo con tan solo girar la pantalla.*/
    fun getAdapter() : Sheet90Adapter {
        return sheetAdapter
    }

    /**/
    private fun generaEstados(sheets: Int){
        for (i in 1 .. sheets){
            val status = BooleanArray(27)
            for(b in 0..26)
                status[b] = false
            sheetStatus.add(status)
        }
    }
}