package com.openinapp.task.di

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.openinapp.task.R
import com.openinapp.task.helper.CONSTANTS
import com.openinapp.task.repo.network.LinkApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.InputStream
import java.util.Properties
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HiltModules {

    @Provides
    @Singleton
    fun provideRetrofit(@Named(CONSTANTS.BASE_URL) baseUrl: String, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    @Provides
    @Singleton
    fun provideLinkApi(retrofit: Retrofit): LinkApi {
        return retrofit.create(LinkApi::class.java)
    }

    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideProperties(context: Context): Properties {
        val properties = Properties()
        val inputStream: InputStream = context.resources.openRawResource(R.raw.config)
        properties.load(inputStream)
        return properties
    }

    @Named(CONSTANTS.BASE_URL)
    @Provides
    @Singleton
    fun provideBaseUrl(properties: Properties): String {
        return properties.getProperty(CONSTANTS.BASE_URL, "default_base_url")
    }

    @Named(CONSTANTS.API_KEY)
    @Provides
    @Singleton
    fun provideApiKey(properties: Properties): String {
        return properties.getProperty(CONSTANTS.API_KEY, "default_api_key")
    }
}
