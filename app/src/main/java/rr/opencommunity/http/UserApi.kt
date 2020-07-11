package rr.opencommunity.http

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface UserApi {

    @GET("/api/v1/users/")
    fun getUsers(): Call<List<User>>

    @GET("/api/v1/rest-auth/logout/")
    fun logout(): Call<User>

    @FormUrlEncoded
    @POST("/api/v1/rest-auth/login/")
    fun login(@Field("username") username: String,
              @Field("password") password: String): Call<TokenResponse>

    @FormUrlEncoded
    @POST("/api/v1/rest-auth/registration/")
    fun register(@Field("username") username: String,
                 @Field("email") email: String,
                 @Field("password1") password1: String,
                 @Field("password2") password2: String): Call<TokenResponse>
}

data class TokenResponse(
    val token: String
)

data class User(
    val username: String,
    val email: String
)
