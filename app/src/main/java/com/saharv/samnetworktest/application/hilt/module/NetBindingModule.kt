package com.saharv.samnetworktest.application.hilt.module

import android.text.TextUtils
import com.google.gson.GsonBuilder
import com.saharv.samnetworktest.BuildConfig
import com.saharv.samnetworktest.data.source.articles.remote.api.ArticlesApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class NetBindingModule {

    companion object {
        /**
         * Sets the default timeout for new connections. A value of 0 means no timeout,
         * otherwise values must be between 1 and {@link Integer#MAX_VALUE} when converted to
         * milliseconds.
         *
         * <p>The connectTimeout is applied when connecting a TCP socket to the target host.
         * The default value is 10 seconds.
         */
        const val TIMEOUT_IN_SEC: Int = 10
        const val CONNECT_TIMEOUT = "CONNECT_TIMEOUT"
        const val READ_TIMEOUT = "READ_TIMEOUT"
        const val WRITE_TIMEOUT = "WRITE_TIMEOUT"
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        gsonConverterFactory: GsonConverterFactory,
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Provides
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        interceptorHeader: Interceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(TIMEOUT_IN_SEC.toLong(), TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_IN_SEC.toLong(), TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_IN_SEC.toLong(), TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(interceptorHeader)
            .build()
    }

    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor { message -> Timber.tag("OkHttp").d(message) }
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    @Provides
    fun providesHeaderInterceptor(): Interceptor {
        return Interceptor {
            val request = it.request()

            val builder = request.newBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("User-Agent", "Android")

            var connectTimeout = it.connectTimeoutMillis()
            var readTimeout = it.readTimeoutMillis()
            var writeTimeout = it.writeTimeoutMillis()

            val connectNew = request.header(CONNECT_TIMEOUT)
            val readNew = request.header(READ_TIMEOUT)
            val writeNew = request.header(WRITE_TIMEOUT)

            if (!TextUtils.isEmpty(connectNew)) connectTimeout = Integer.valueOf(connectNew)
            if (!TextUtils.isEmpty(readNew)) readTimeout = Integer.valueOf(readNew)
            if (!TextUtils.isEmpty(writeNew)) writeTimeout = Integer.valueOf(writeNew)

            return@Interceptor it
                .withConnectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                .withReadTimeout(readTimeout, TimeUnit.MILLISECONDS)
                .withWriteTimeout(writeTimeout, TimeUnit.MILLISECONDS)
                .proceed(builder.build())
        }
    }

    @Provides
    fun provideGson(): GsonConverterFactory {
        return GsonConverterFactory.create(
            GsonBuilder()
            .create())
    }

    @Provides
    fun provideArticlesAPI(retrofit: Retrofit): ArticlesApi {
        return retrofit.create(ArticlesApi::class.java)
    }

}