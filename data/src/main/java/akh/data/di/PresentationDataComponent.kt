package akh.data.di

import akh.core.di.DataToolsProvider
import akh.core.di.PresentationDataToolsProvider
import akh.data.di.module.AppToolsModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [
        DataToolsProvider::class
    ],
    modules = [
        AppToolsModule::class
    ]
)
interface PresentationDataComponent: PresentationDataToolsProvider {

    class Initializer private constructor() {

        companion object {

            fun init(
                dataToolsProvider: DataToolsProvider
            ): PresentationDataToolsProvider =
                DaggerPresentationDataComponent.builder()
                    .dataToolsProvider(dataToolsProvider)
                    .build()
        }
    }
}