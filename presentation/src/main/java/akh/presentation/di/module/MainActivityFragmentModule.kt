package akh.presentation.di.module

import akh.presentation.ui.features.converter.ConverterFragment
import akh.presentation.di.ConverterFragmentScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
interface MainActivityFragmentModule {

    @ConverterFragmentScope
    @ContributesAndroidInjector
    fun contributeConverterFragment(): ConverterFragment

}