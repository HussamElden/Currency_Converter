package com.example.currencyconverter.model

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.OkHttpClient.Builder
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =  getRetrofit()

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ConversionAPI =
        retrofit.create(ConversionAPI::class.java)

    @Singleton
    @Provides
    fun providerepository(apiService: ConversionAPI) = ConversionRepo(apiService)

    private fun getRetrofit(): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client: OkHttpClient = Builder().addInterceptor(interceptor).build()
        return Retrofit.Builder()
            .baseUrl("https://api.apilayer.com/fixer/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
