package net.iessochoa.lanbingo.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import net.iessochoa.lanbingo.R

class WorkingDialog(private val context: Context) {

    private lateinit var dialog: Dialog

    fun showLoadingDialog(){
        dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_loading)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val tvLoading: TextView = dialog.findViewById(R.id.tvMessage)
        tvLoading.setText(R.string.loading)
        tvLoading.textSize = 16F

        val pavIcon: LottieAnimationView = dialog.findViewById(R.id.pavIcon)
        pavIcon.setAnimation(R.raw.loading)
        pavIcon.repeatCount = LottieDrawable.INFINITE
        pavIcon.scaleX = 2.5F
        pavIcon.scaleY = 2.5F

        dialog.create()
        dialog.show()
    }

    fun showCheckingDialog(){
        dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_loading)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val tvLoading: TextView = dialog.findViewById(R.id.tvMessage)
        tvLoading.setText(R.string.checking)
        tvLoading.textSize = 15F

        val pavIcon: LottieAnimationView = dialog.findViewById(R.id.pavIcon)
        pavIcon.setAnimation(R.raw.checking)
        pavIcon.repeatCount = LottieDrawable.INFINITE

        dialog.create()
        dialog.show()
    }

    fun showCorrectDialog(title: String){
        dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_loading)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val tvLoading: TextView = dialog.findViewById(R.id.tvMessage)
        tvLoading.text = title
        tvLoading.textSize = 14F

        val pavIcon: LottieAnimationView = dialog.findViewById(R.id.pavIcon)
        pavIcon.setAnimation(R.raw.correct)
        pavIcon.scaleX = 1.3F
        pavIcon.scaleY = 1.3F

        dialog.create()
        dialog.show()
    }

    fun hideDialog(){
        dialog.dismiss()
    }
}