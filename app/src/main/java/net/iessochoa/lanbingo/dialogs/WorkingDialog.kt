package net.iessochoa.lanbingo.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import net.iessochoa.lanbingo.R
//Clase creada para mostrar animaciones de espera y verificación al usuario para hacer la app
//más vistosa.
//Creada por Juan Antonio Nicolás
class WorkingDialog {

    private lateinit var dialog: Dialog

    fun changeContext(context: Context){
        dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_loading)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    fun showLoadingDialog(){
        val tvLoading: TextView = dialog.findViewById(R.id.tvMessage)
        tvLoading.setText(R.string.loading)
        tvLoading.textSize = 16F

        val pavIcon: LottieAnimationView = dialog.findViewById(R.id.pavIcon)
        pavIcon.setAnimation(R.raw.loading)
        pavIcon.repeatCount = LottieDrawable.INFINITE
        pavIcon.scaleX = 2.5F
        pavIcon.scaleY = 2.5F

        initDialog()
    }

    /*fun showGeneratingDialog(){

    }*/

    fun showWaitDialog(){
        val tvLoading: TextView = dialog.findViewById(R.id.tvMessage)
        tvLoading.setText(R.string.waiting)
        tvLoading.textSize = 15F

        val pavIcon: LottieAnimationView = dialog.findViewById(R.id.pavIcon)
        pavIcon.setAnimation(R.raw.waiting)
        pavIcon.repeatCount = LottieDrawable.INFINITE
        pavIcon.scaleX = 1F
        pavIcon.scaleY = 1F

        initDialog()
    }

    fun showCheckingDialog(){
        val tvLoading: TextView = dialog.findViewById(R.id.tvMessage)
        tvLoading.setText(R.string.checking)
        tvLoading.textSize = 15F

        val pavIcon: LottieAnimationView = dialog.findViewById(R.id.pavIcon)
        pavIcon.setAnimation(R.raw.checking)
        pavIcon.repeatCount = LottieDrawable.INFINITE
        pavIcon.scaleX = 1F
        pavIcon.scaleY = 1F

        initDialog()
    }

    fun showCorrectDialog(title: String){
        val tvLoading: TextView = dialog.findViewById(R.id.tvMessage)
        tvLoading.text = title
        tvLoading.textSize = 14F

        val pavIcon: LottieAnimationView = dialog.findViewById(R.id.pavIcon)
        pavIcon.setAnimation(R.raw.correct)
        pavIcon.scaleX = 1.3F
        pavIcon.scaleY = 1.3F
        pavIcon.repeatCount = 1

        initDialog()
    }

    fun showWrongDialog(title: String){
        val tvLoading: TextView = dialog.findViewById(R.id.tvMessage)
        tvLoading.text = title
        tvLoading.textSize = 14F

        val pavIcon: LottieAnimationView = dialog.findViewById(R.id.pavIcon)
        pavIcon.setAnimation(R.raw.wrong)
        pavIcon.repeatCount = 1
        pavIcon.scaleX = 1.3F
        pavIcon.scaleY = 1.3F

        initDialog()
    }

    private fun initDialog(){
        dialog.setCancelable(false)
        dialog.create()
        dialog.show()
    }

    fun hideDialog(){
        dialog.dismiss()
    }
}