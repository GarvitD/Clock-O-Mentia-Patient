package com.example.clock_o_mentiapatient.Retrofit

import com.example.clock_o_mentiapatient.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteDataSource{

    private const val BASE_URL="https://clockomentia.herokuapp.com"

    @Provides
    @Singleton
    fun getRetrofitInstance(): Retrofit {
        val httpClient = OkHttpClient.Builder()
            .connectTimeout(2,TimeUnit.MINUTES)
            .readTimeout(2,TimeUnit.MINUTES)
            .writeTimeout(2,TimeUnit.MINUTES)

        httpClient.addInterceptor(Interceptor { chain ->
            val builder = chain.request().newBuilder()
             builder.header("x-mock-match-request-body", "true")
            return@Interceptor chain.proceed(builder.build())
        })

        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            logging.redactHeader("Authorization")
            logging.redactHeader("Cookie")
            httpClient.addInterceptor(logging)

        }

        val builder: Retrofit.Builder = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())

        builder.client(httpClient.build())
        return builder.build()
    }

}