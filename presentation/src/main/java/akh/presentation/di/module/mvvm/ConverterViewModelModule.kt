package akh.presentation.di.module.mvvm

import akh.presentation.ui.features.converter.ConverterViewModel
import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Suppress("unused")
@Module
interface ConverterViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(ConverterViewModel::class)
    fun bindConverterViewModels(converterViewModel: ConverterViewModel): ViewModel

}