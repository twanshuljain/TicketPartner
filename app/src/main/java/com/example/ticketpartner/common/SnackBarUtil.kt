package com.example.ticketpartner.common

import android.view.ContextThemeWrapper
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import com.example.ticketpartner.R
import com.google.android.material.snackbar.Snackbar


/**
 * This class is used to show snack bar for given view
 */
object SnackBarUtil {
    /**
     * This method is used to show snack bar
     * @param view view
     * @param message to be display
     * @param length duration to display
     */
    fun showSuccessSnackBar(
        view: View,
        message: String,
        length: Int = Snackbar.LENGTH_LONG,
        isSuccess: Boolean = true
    ) {
        val style = ContextThemeWrapper(view.context, R.style.CustomSnackBarSuccessTheme)
        val snackBar = Snackbar.make(style, view, message, Snackbar.LENGTH_LONG)

        val snackView = snackBar.view
        // snackView.setBackgroundColor(Color.parseColor("#147BD1"))

        // Get the SnackBar view
        val params = snackView.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP
        snackView.layoutParams = params

        // Show the SnackBar
        snackBar.show()
    }

    fun showErrorSnackBar(
        view: View,
        message: String,
        length: Int = Snackbar.LENGTH_LONG,
        isSuccess: Boolean = true
    ) {
        val style = ContextThemeWrapper(view.context, R.style.CustomSnackBarErrorTheme)
        val snackBar = Snackbar.make(style, view, message, Snackbar.LENGTH_LONG)
        val snackView = snackBar.view


        /*// Set custom animation
        val animation = AnimationUtils.loadAnimation(view.context, R.anim.slide_in_from_top)
        snackView.startAnimation(animation)
        animation.start()*/

        // Get the SnackBar view
        val params = snackView.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP
        snackView.layoutParams = params

        // Show the SnackBar

        snackBar.show()
    }
}