package akh.core.app

interface AppPresentationStorage {

    fun getCurrentTheme() : CurrentTheme

    fun putCurrentTheme(theme: CurrentTheme)

}