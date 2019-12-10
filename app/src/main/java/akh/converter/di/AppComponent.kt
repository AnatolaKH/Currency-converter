package akh.converter.di

import akh.core.di.DataToolsProvider
import akh.core.di.DomainToolsProvider
import akh.core.di.MainToolsProvider
import akh.core.di.PresentationToolsProvider
import akh.data.di.DataComponent
import akh.domain.di.DomainComponent
import akh.presentation.di.PresentationComponent
import akh.converter.App
import akh.presentation.di.module.ActivityModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [
        MainToolsProvider::class,
        DataToolsProvider::class,
        DomainToolsProvider::class,
        PresentationToolsProvider::class
    ],
    modules = [
        AndroidInjectionModule::class,
        ActivityModule::class
    ]
)
interface AppComponent : AndroidInjector<App> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: App): Builder

        fun main(mainToolsProvider: MainToolsProvider): Builder

        fun data(dataToolsProvider: DataToolsProvider): Builder

        fun domain(domainToolsProvider: DomainToolsProvider): Builder

        fun presentation(presentationToolsProvider: PresentationToolsProvider): Builder

        fun build(): AppComponent

    }

    override fun inject(instance: App)

    class Initializer private constructor() {

        companion object {
            fun init(app: App): AppComponent {

                val mainToolsProvider = MainToolsComponent.Initializer.init(app)

                val dataToolsProvider = DataComponent.Initializer.init(mainToolsProvider)

                val domainToolsProvider = DomainComponent.Initializer.init(dataToolsProvider)

                val presentationToolsProvider =
                    PresentationComponent.Initializer.init(domainToolsProvider)

                return DaggerAppComponent.builder()
                    .application(app)
                    .main(mainToolsProvider = mainToolsProvider)
                    .data(dataToolsProvider = dataToolsProvider)
                    .domain(domainToolsProvider = domainToolsProvider)
                    .presentation(presentationToolsProvider = presentationToolsProvider)
                    .build()

            }

        }
    }

}