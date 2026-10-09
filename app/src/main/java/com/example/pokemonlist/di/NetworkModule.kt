package com.example.pokemonlist.di

import android.content.Context
import com.example.pokemonlist.data.remote.PokemonApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(@ApplicationContext context: Context): OkHttpClient {
        val cache = Cache(File(context.cacheDir, "http_cache"), 10L * 1024 * 1024)
        return OkHttpClient.Builder()
            .cache(cache)
            .addNetworkInterceptor { chain ->
                chain.proceed(chain.request())
                    .newBuilder()
                    .header("Cache-Control", "public, max-age=3600")
                    .build()
            }
            .build()
    }

    @Provides
    @Singleton
    fun providePokemonApi(client: OkHttpClient): PokemonApi {
        return Retrofit.Builder()
            .baseUrl(PokemonApi.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PokemonApi::class.java)
    }
}
