package com.mtp.ticketpartner.scanner.utils

import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment

object NavigateFragmentUtil {

    //When popUpToInclusive is set to true:
    /* The back stack is cleared up to and including the destination specified by popUpTo.
     That means the destination specified is also removed from the stack.*/

    // When popUpToInclusive is set to false:When popUpToInclusive is set to false:
    /*  The back stack is cleared up to the destination specified in popUpTo,
      but the destination itself is not removed. That means after the action,
      the specified destination will be the topmost in the back stack.*/

    fun NavController.navigateWithClearAllBackStack(destinationId: Int) {
        popBackStack(destinationId, false)
    }

    fun NavController.navigateParentToChildFragment(
        currentId: Int,
        destinationId: Int,
        inclusive: Boolean = false
    ) {
        val navOptions = NavOptions.Builder()
            .setPopUpTo(currentId, inclusive).build()
        navigate(destinationId, null, navOptions)
    }

    fun NavController.navigateWithClearNavGraph(navGraph: Int,destinationId: Int){
      popBackStack(navGraph, true)  // Clear everything up to the nav_graph
       navigate(destinationId)
    }

    fun getNestedNavController(activity: FragmentActivity, viewId: Int): NavController? {
        val navHostFragment = activity.supportFragmentManager
            .findFragmentById(viewId) as NavHostFragment?
        return navHostFragment?.navController
    }

}