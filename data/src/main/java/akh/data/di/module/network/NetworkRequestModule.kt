package akh.data.di.module.network

import akh.data.request.NetworkRequest
import akh.data.request.NetworkRequestV1Impl
import dagger.Binds
import dagger.Module
import javax.inject.Named
import javax.inject.Singleton

@Suppress("unused")
@Module
internal interface NetworkRequestModule {

    @Singleton
    @Binds
    @Named("v1")
    fun networkRequesterV1(networkRequestV1Impl: NetworkRequestV1Impl): NetworkRequest

}