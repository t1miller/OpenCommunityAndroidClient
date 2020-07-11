package rr.opencommunity.social

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rr.opencommunity.http.Apifactory
import rr.opencommunity.http.FollowingResponse
import rr.opencommunity.http.GenericResponse
import rr.opencommunity.http.InboxResponse
import rr.opencommunity.settings.PreferenceUtils
import timber.log.Timber

class SocialViewModel(application: Application) : AndroidViewModel(application) {

    private var inboxRequests: MutableLiveData<InboxResponse> = MutableLiveData()
    var _inboxRequests: LiveData<InboxResponse> = inboxRequests

    private var following: MutableLiveData<FollowingResponse> = MutableLiveData()
    var _following: LiveData<FollowingResponse> = following


    fun fetchInbox(inboxOwner: String) {
        val apiInboxApi = Apifactory.getInboxApi(getApplication())
        val request = apiInboxApi.getInbox(inboxOwner)
        request.enqueue(object : Callback<InboxResponse> {

            override fun onFailure(call: Call<InboxResponse>?, t: Throwable?) {
                // todo
            }

            override fun onResponse(call: Call<InboxResponse>?, response: Response<InboxResponse>?) {
                if (response?.isSuccessful == true) {
                    response.body()?.let {
                        inboxRequests.postValue(it)
                    }
                }
            }

        })
    }

    fun addInboxRequest(inboxOwner: String, personToAdd: String) {
        val apiInboxApi = Apifactory.getInboxApi(getApplication())
        val request = apiInboxApi.addInboxRequest(personToAdd, inboxOwner)
        request.enqueue(object : Callback<GenericResponse> {
            override fun onFailure(call: Call<GenericResponse>?, t: Throwable?) {
                Timber.e("on failure %s", t.toString())
                Toast.makeText(getApplication(), "No user found", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<GenericResponse>?, response: Response<GenericResponse>?) {
                // update inbox with potential new list
                fetchFollowing(PreferenceUtils.getUsername(getApplication()))
            }

        })
    }

    fun removeInboxRequest(id: Int, inboxOwner: String, personToDeny: String) {
        val inboxApi = Apifactory.getInboxApi(getApplication())
        val request = inboxApi.removeInboxRequest(id, personToDeny, inboxOwner)
        request.enqueue(object : Callback<GenericResponse> {
            override fun onFailure(call: Call<GenericResponse>?, t: Throwable?) {
                Timber.e("on failure %s", t.toString())
            }

            override fun onResponse(call: Call<GenericResponse>?, response: Response<GenericResponse>?) {
                // update inbox with potential new list
                fetchInbox(inboxOwner)
            }

        })
    }

    fun fetchFollowing(user: String) {
        val followingApi = Apifactory.getFollowingApi(getApplication())
        val request = followingApi.getFollowing(user)
        request.enqueue(object : Callback<FollowingResponse> {
            override fun onFailure(call: Call<FollowingResponse>?, t: Throwable?) {
                Timber.e("on failure %s", t.toString())
            }

            override fun onResponse(call: Call<FollowingResponse>?, response: Response<FollowingResponse>?) {
                // update inbox with potential new list
                if (response?.isSuccessful == true) {
                    response.body()?.let {
                        following.postValue(it)
                    }
                }
            }

        })
    }

    fun updateFollowing(user: String, personToAdd: String) {
        val followingApi = Apifactory.getFollowingApi(getApplication())
        val request = followingApi.updateFollowing(user, personToAdd)
        request.enqueue(object : Callback<FollowingResponse> {
            override fun onFailure(call: Call<FollowingResponse>?, t: Throwable?) {
                Timber.e("on failure %s", t.toString())
            }

            override fun onResponse(call: Call<FollowingResponse>?, response: Response<FollowingResponse>?) {
                // get new following list
//                fetchFollowing(user)
            }

        })
    }

    fun removeFollowing(id: Int, user: String, personToRemove: String) {
        val followingApi = Apifactory.getFollowingApi(getApplication())
        val request = followingApi.removeFromFollowing(id, user, personToRemove)
        request.enqueue(object : Callback<FollowingResponse> {
            override fun onFailure(call: Call<FollowingResponse>?, t: Throwable?) {
                Timber.e("on failure %s", t.toString())
            }

            override fun onResponse(call: Call<FollowingResponse>?, response: Response<FollowingResponse>?) {
                // get new following list
                fetchFollowing(user)
            }

        })
    }
}
