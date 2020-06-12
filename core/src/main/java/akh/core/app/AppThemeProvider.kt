package akh.core.app

import androidx.annotation.StyleRes

interface AppThemeProvider {

    @StyleRes
    fun getCurrentTheme(): Int

    fun switchTheme()

}