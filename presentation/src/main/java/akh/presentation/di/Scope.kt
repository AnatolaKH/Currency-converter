package akh.presentation.di

import javax.inject.Scope


@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class MainActivityScope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class ConverterFragmentScope