package net.iessochoa.lanbingo.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.ImageView
import android.widget.TextView
import net.iessochoa.lanbingo.main.GameActivity
import net.iessochoa.lanbingo.R
import net.iessochoa.lanbingo.main.MainScreenActivity

//Clase creada para mostrar información de necesidad al jugador tal como su nick, puntos y strikes.
//Creado por Pedro Meseguer Gelardo
class StatusDialog(context: Context) {

    init {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_status)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val tvNick: TextView = dialog.findViewById(R.id.tvNick)
        tvNick.text = MainScreenActivity.nick

        val tvPoints: TextView = dialog.findViewById(R.id.tvPoints)
        tvPoints.text = GameActivity.points.toString()

        val ivStrike1: ImageView = dialog.findViewById(R.id.ivStrike1)
        val ivStrike2: ImageView = dialog.findViewById(R.id.ivStrike2)
        val ivStrike3: ImageView = dialog.findViewById(R.id.ivStrike3)
        when(GameActivity.strikes) {
            1 -> {
                ivStrike1.setImageResource(R.drawable.strike)
                ivStrike2.setImageResource(R.drawable.empty_strike)
                ivStrike3.setImageResource(R.drawable.empty_strike)
            }
            2 -> {
                ivStrike1.setImageResource(R.drawable.strike)
                ivStrike2.setImageResource(R.drawable.strike)
                ivStrike3.setImageResource(R.drawable.empty_strike)
            }
            3 -> {
                ivStrike1.setImageResource(R.drawable.strike)
                ivStrike2.setImageResource(R.drawable.strike)
                ivStrike3.setImageResource(R.drawable.strike)
            }
            else -> {
                ivStrike1.setImageResource(R.drawable.empty_strike)
                ivStrike2.setImageResource(R.drawable.empty_strike)
                ivStrike3.setImageResource(R.drawable.empty_strike)
            }
        }

        dialog.create()
        dialog.show()
    }
}