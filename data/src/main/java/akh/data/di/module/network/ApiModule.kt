package akh.data.di.module.network

import akh.data.api.RateApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [RetrofitModule::class])
class ApiModule {

    @Provides
    @Singleton
    fun provideRateApi(retrofit: Retrofit) = retrofit.create(RateApi::class.java)

}