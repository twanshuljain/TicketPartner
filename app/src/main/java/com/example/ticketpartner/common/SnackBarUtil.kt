package com.example.ticketpartner.common

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
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
    /*   fun showSuccessSnackBar(
           view: View,
           message: String,
           length: Int = Snackbar.LENGTH_LONG,
           isSuccess: Boolean = true
       ) {
           val style = ContextThemeWrapper(view.context, R.style.CustomSnackBarSuccessTheme)
           val snackBar = Snackbar.make(style, view, message, Snackbar.LENGTH_LONG)
           val snackView = snackBar.view
           val params = snackView.layoutParams as FrameLayout.LayoutParams
           params.gravity = Gravity.TOP
           snackView.layoutParams = params
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
           val params = snackView.layoutParams as FrameLayout.LayoutParams
           params.gravity = Gravity.TOP
           snackView.layoutParams = params
           snackBar.show()
       }*/

    fun showCustomSnackBar(
        view: View,
        message: String,
        isSuccess: Boolean = false,
        length: Int = Snackbar.LENGTH_LONG
    ) {
        // Create the Snack bar
        val snackBar = Snackbar.make(view, "", length)

        // Get the Snack bar's layout
        val snackBarLayout = snackBar.view as ViewGroup
        snackBarLayout.setPadding(ZERO, ZERO, ZERO, ZERO)

        // Inflate custom layout
        val customView =
            LayoutInflater.from(view.context).inflate(R.layout.custom_snackbar_layout, null)

        // Customize the view
        val snackText = customView.findViewById<AppCompatTextView>(R.id.tvMessage)
        snackText.text = message

        val snackIcon = customView.findViewById<AppCompatImageView>(R.id.ivIcon)
        if (isSuccess) {
            customView.setBackgroundColor(ContextCompat.getColor(view.context, R.color.green))
            snackIcon.setImageResource(R.drawable.img_correct_white_circle) // Set your success icon here
        } else {
            customView.setBackgroundColor(ContextCompat.getColor(view.context, R.color.red_light))
            snackIcon.setImageResource(R.drawable.img_error_snackbar) // Set your error icon here
        }

        // Remove all views from the Snackbar's layout
        snackBarLayout.removeAllViews()

        // Add custom view to Snack bar layout with proper layout parameters
        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        snackBarLayout.addView(customView, params)

        // Set the gravity to top
        val layoutParams = snackBar.view.layoutParams as FrameLayout.LayoutParams
        layoutParams.gravity = Gravity.TOP
        snackBar.view.layoutParams = layoutParams

        snackBar.show()
    }

}