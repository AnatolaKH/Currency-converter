package akh.presentation.di

import akh.core.di.DomainToolsProvider
import akh.core.di.PresentationToolsProvider
import akh.presentation.di.module.ActivityModule
import akh.presentation.di.module.mvvm.ViewModelFactoryModule
import akh.presentation.di.module.mvvm.ViewModelModule
import dagger.Component

@Component(
    dependencies = [
        DomainToolsProvider::class
    ]
)
interface PresentationComponent : PresentationToolsProvider {

    class Initializer private constructor() {

        companion object {

            fun init(domainToolsProvider: DomainToolsProvider): PresentationToolsProvider =
                DaggerPresentationComponent.builder()
                    .domainToolsProvider(domainToolsProvider)
                    .build()
        }
    }

}