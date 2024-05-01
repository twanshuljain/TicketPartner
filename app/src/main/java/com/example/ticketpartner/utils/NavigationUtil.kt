package com.example.ticketpartner.utils

import androidx.navigation.NavController
import androidx.navigation.NavOptions

object NavigationUtil {

    fun NavController.clearBackStackToDestination(destinationId: Int) {
        popBackStack(destinationId, false)
    }

    /**
     * Navigate to a destination by clearing specific parts of the back stack.
     *
     * @param destinationId The ID of the destination to navigate to.
     * @param inclusive Whether to remove the destination itself from the back stack.
     * @param upToId The ID of the destination up to which the back stack should be cleared. If null, it clears up to the destination.
     */
    fun NavController.navigateWithStackClear(destinationId: Int, inclusive: Boolean = true, upToId: Int? = null) {
        val navOptions = NavOptions.Builder()
            .setPopUpTo(upToId ?: destinationId, inclusive)
            .build()
        this.navigate(destinationId, null, navOptions)
    }

   /* fun NavController.exitAppWithClearAllBackStack(navGraph: l) {
        // Pop the back stack to the root destination
        popBackStack(navGraph.startDestination, false)

        // Optionally, you can finish the activity or close the app in other ways
        // For example:
        // activity?.finish()
        // Process.killProcess(Process.myPid())
    }*/
}