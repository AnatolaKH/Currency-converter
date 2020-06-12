package akh.presentation.common.theme

import akh.core.app.AppPresentationStorage
import akh.core.app.AppThemeProvider
import akh.core.app.CurrentTheme
import akh.presentation.R
import android.content.res.Configuration
import android.content.res.Resources
import androidx.annotation.StyleRes
import javax.inject.Inject

class AppThemeProviderImpl @Inject constructor(
    private val appPresentationStorage: AppPresentationStorage
) : AppThemeProvider {

    @StyleRes
    override fun getCurrentTheme() = when (appPresentationStorage.getCurrentTheme()) {
        CurrentTheme.DEFAULT -> getDefaultCurrentTheme()
        CurrentTheme.LIGHT -> R.style.AppTheme_Light
        CurrentTheme.DARK -> R.style.AppTheme_Dark
    }

    override fun switchTheme() = when (appPresentationStorage.getCurrentTheme()) {
        CurrentTheme.DEFAULT -> switchDefaultCurrentTheme()
        CurrentTheme.LIGHT -> appPresentationStorage.putCurrentTheme(CurrentTheme.DARK)
        CurrentTheme.DARK -> appPresentationStorage.putCurrentTheme(CurrentTheme.LIGHT)
    }

    private fun getDefaultCurrentTheme() =
        when (Resources.getSystem().configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> R.style.AppTheme_Light
            Configuration.UI_MODE_NIGHT_YES -> R.style.AppTheme_Dark
            else -> R.style.AppTheme_Light
        }

    private fun switchDefaultCurrentTheme() =
        when (Resources.getSystem().configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> appPresentationStorage.putCurrentTheme(CurrentTheme.DARK)
            Configuration.UI_MODE_NIGHT_YES -> appPresentationStorage.putCurrentTheme(CurrentTheme.LIGHT)
            else -> appPresentationStorage.putCurrentTheme(CurrentTheme.DARK)
        }
}