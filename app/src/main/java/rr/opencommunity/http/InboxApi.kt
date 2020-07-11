package rr.opencommunity.http

import retrofit2.Call
import retrofit2.http.*

interface InboxApi {

    @GET("/api/v1/userdata/inbox/")
    fun getInbox(@Query("person_to_follow") person_to_follow: String) : Call<InboxResponse>

    @FormUrlEncoded
    @POST("/api/v1/userdata/inbox/")
    fun addInboxRequest(@Field("follower") follower: String,
                        @Field("person_to_follow") person_to_follow: String) : Call<GenericResponse>

    @DELETE("/api/v1/userdata/inbox/{Id}/")
    fun removeInboxRequest(
        @Path("Id") id: Int,
        @Query("person_to_remove") person_to_remove: String,
        @Query("owner") owner: String
    ) : Call<GenericResponse>
}

data class InboxResponse(
    val inbox: List<String>,
    val id: Int
)

data class GenericResponse(
    val status: String
)
