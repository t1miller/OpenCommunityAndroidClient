package rr.opencommunity.http

import android.content.Context
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import rr.opencommunity.settings.PreferenceUtils

object Apifactory{

    private val defaultBaseUrl = "http://192.168.86.31:8000"

//    private val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val userClient = OkHttpClient()
        .newBuilder()
        .addInterceptor(configureInterceptor()).build()

    fun configureBaseUrl(context: Context): String{
        var baseUrl = PreferenceUtils.getPreference(context,"serverUrl", defaultBaseUrl)
        if(baseUrl.isNullOrBlank()){
            baseUrl = defaultBaseUrl
        }
        return baseUrl
    }

    fun configureInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor

    }

    fun retrofit(context: Context) : Retrofit = Retrofit.Builder()
        .client(userClient)
        .baseUrl(configureBaseUrl(context))
        .addConverterFactory(MoshiConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    fun getUserApi(context: Context): UserApi {
        return retrofit(context).create(UserApi::class.java)
    }

    fun getInboxApi(context: Context): InboxApi {
        return retrofit(context).create(InboxApi::class.java)
    }

    fun getFollowingApi(context: Context): FollowingApi {
        return retrofit(context).create(FollowingApi::class.java)
    }
}