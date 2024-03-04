package com.example.ticketpartner.utils

import androidx.navigation.NavController
import androidx.navigation.NavGraph

object NavigateFragmentUtil {

    fun NavController.clearBackStackToDestination(destinationId: Int) {
        popBackStack(destinationId, false)
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