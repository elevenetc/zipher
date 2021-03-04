package com.elevenetc.zipher.androidApp.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

internal fun FragmentManager.getVisible(): Fragment? {
    fragments.forEach { f ->
        val visible = f.isVisible
        if (f != null && visible) {
            return f
        }
    }
    return null
}