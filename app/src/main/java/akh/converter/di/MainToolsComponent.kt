package akh.converter.di

import akh.core.app.App
import akh.core.di.MainToolsProvider
import akh.converter.di.module.AppModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface MainToolsComponent : MainToolsProvider {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun app(app: App): Builder

        fun build(): MainToolsComponent

    }

    class Initializer private constructor() {
        companion object {

            fun init(app: App): MainToolsProvider =
                DaggerMainToolsComponent.builder()
                    .app(app)
                    .build()
        }
    }

}