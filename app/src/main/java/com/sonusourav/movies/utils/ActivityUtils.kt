package com.sonusourav.movies.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

/**
 * This provides methods to help Activities load their UI.
 *
 */
object ActivityUtils {
    /**
     * The `fragment` is added to the container view with id `frameId`. The operation is
     * performed by the `fragmentManager`.
     */
    fun replaceFragmentInActivity(fragmentManager: FragmentManager,
                                  fragment: Fragment, frameId: Int) {
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(frameId, fragment)
        transaction.commit()
    }
}