package com.elevenetc.zipher.androidApp.navigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

fun BottomNavigationView.initNavigation(
    map: LinkedHashMap<Int, Fragment>,
    container: Int,
    parent: Fragment,
    savedInstanceState: Bundle?
) {

    if (savedInstanceState == null) {
        val initFragment = map.values.first()
        parent.childFragmentManager
            .beginTransaction()
            .add(container, initFragment)
            .commit()
    }

    setOnNavigationItemSelectedListener { item ->
        val itemId = item.itemId
        val newFragment = map[itemId] ?: error("Undefined fragment id: $itemId")
        val fm = parent.childFragmentManager

        val currentFragment = fm.getVisible()

        //TODO: add error message if currentFragment is null

        if (currentFragment === newFragment) {
            true
        } else {
            var transaction = fm.beginTransaction().hide(currentFragment!!)

            transaction = if (newFragment.isAdded) {
                transaction.show(newFragment)
            } else {
                transaction.add(container, newFragment)
            }

            transaction.commit()

            true
        }


    }
}