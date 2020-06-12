package akh.data.di

import akh.core.di.DataToolsProvider
import akh.core.di.MainToolsProvider
import akh.data.di.module.RateModule
import akh.data.di.module.SharedPreferenceModule
import akh.data.di.module.db.SecretDBModule
import akh.data.di.module.network.ApiModule
import akh.data.di.module.network.NetworkRequestModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [
        MainToolsProvider::class
    ],
    modules = [
        ApiModule::class,
        RateModule::class,
        NetworkRequestModule::class,
        SharedPreferenceModule::class,
        SecretDBModule::class
    ]
)
interface DataComponent : DataToolsProvider {

    class Initializer private constructor() {
        companion object {

            fun init(mainToolsProvider: MainToolsProvider): DataToolsProvider =
                DaggerDataComponent.builder()
                    .mainToolsProvider(mainToolsProvider)
                    .build()
        }
    }

}