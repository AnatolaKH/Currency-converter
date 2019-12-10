package akh.domain.di

import akh.core.di.DataToolsProvider
import akh.core.di.DomainToolsProvider
import akh.domain.di.module.RateModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [
        DataToolsProvider::class
    ],
    modules = [
        RateModule::class
    ]
)
interface DomainComponent : DomainToolsProvider {

    class Initializer private constructor() {

        companion object {

            fun init(
                dataToolsProvider: DataToolsProvider
            ): DomainToolsProvider =
                DaggerDomainComponent.builder()
                    .dataToolsProvider(dataToolsProvider)
                    .build()
        }
    }

}