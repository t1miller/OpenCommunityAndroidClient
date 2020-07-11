package rr.opencommunity.http

import retrofit2.Call
import retrofit2.http.*

interface FollowingApi {

    @GET("/api/v1/userdata/following/")
    fun getFollowing(@Query("user") user: String) : Call<FollowingResponse>

    @POST("/api/v1/userdata/following/")
    @FormUrlEncoded
    fun updateFollowing(@Field("user") user: String,
                        @Field("person_to_add") person_to_add: String) : Call<FollowingResponse>

    @DELETE("/api/v1/userdata/following/{id}/")
    fun removeFromFollowing(@Path("id") id: Int,
                            @Query("user") user: String,
                            @Query("person_to_remove") person_to_remove: String) : Call<FollowingResponse>
}

data class FollowingResponse(
    var id: Int,
    var following: List<String>,
    var status: String?
)