package com.app.assisment.di

import com.app.assisment.common.Constants
import com.app.assisment.core.dispatchers.AppDispatcher
import com.app.assisment.core.dispatchers.Dispatcher
import com.app.assisment.data.remote.ApiService
import com.app.assisment.data.repository.ListingRepositoryImpl
import com.app.assisment.data.db.RoomDatabase
import com.app.assisment.domain.repository.ListingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().apply {
            addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            addInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    val original: Request = chain.request()
                    val originalHttpUrl: HttpUrl = original.url

                    val url = originalHttpUrl.newBuilder()
                        .build()
                    val requestBuilder: Request.Builder = original.newBuilder()
                        .url(url)
                    val request: Request = requestBuilder.build()
                    return chain.proceed(request)
                }
            })
        }.build()
    }

    @Provides
    @Singleton
    fun provideApiService(okHttpClient: OkHttpClient): ApiService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideListingRepository(api: ApiService, db: RoomDatabase): ListingRepository {
        return ListingRepositoryImpl(api, db)
    }

    @Provides
    @Singleton
    fun provideAppDispatcher(): Dispatcher {
        return AppDispatcher()
    }
}