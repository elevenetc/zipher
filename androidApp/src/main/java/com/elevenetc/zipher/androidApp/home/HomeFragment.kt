package com.elevenetc.zipher.androidApp.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.elevenetc.zipher.androidApp.R
import com.elevenetc.zipher.androidApp.navigation.Navigator
import com.elevenetc.zipher.androidApp.navigation.initNavigation
import com.elevenetc.zipher.androidApp.records.RecordsFragment
import com.elevenetc.zipher.androidApp.settings.SettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.android.ext.android.inject

class HomeFragment : Fragment() {

    val navigator: Navigator by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val navView: BottomNavigationView = view.findViewById(R.id.nav_view)

        navView.initNavigation(
            linkedMapOf(
                Pair(R.id.navigation_home, RecordsFragment()),
                Pair(R.id.navigation_dashboard, Fragment()),
                Pair(R.id.navigation_settings, SettingsFragment())
            ),
            R.id.main_fragment_container,
            this,
            savedInstanceState
        )
    }

    companion object {
        fun create(): HomeFragment {
            return HomeFragment()
        }
    }
}