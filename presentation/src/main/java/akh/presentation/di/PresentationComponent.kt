package akh.presentation.di

import akh.core.di.DomainToolsProvider
import akh.core.di.PresentationDataToolsProvider
import akh.core.di.PresentationToolsProvider
import akh.presentation.di.module.presentation.AppThemeProviderModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [
        DomainToolsProvider::class,
        PresentationDataToolsProvider::class
    ],
    modules = [
        AppThemeProviderModule::class
    ]
)
interface PresentationComponent : PresentationToolsProvider {

    class Initializer private constructor() {

        companion object {

            fun init(
                domainToolsProvider: DomainToolsProvider,
                presentationDataToolsProvider: PresentationDataToolsProvider
            ): PresentationToolsProvider =
                DaggerPresentationComponent.builder()
                    .domainToolsProvider(domainToolsProvider)
                    .presentationDataToolsProvider(presentationDataToolsProvider)
                    .build()
        }
    }

}