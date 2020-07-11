package rr.opencommunity.settings

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.api_testing_activity.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rr.opencommunity.R
import rr.opencommunity.http.Apifactory
import rr.opencommunity.http.GenericResponse
import rr.opencommunity.http.InboxResponse
import rr.opencommunity.http.User
import timber.log.Timber


class ApiTestingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.api_testing_activity)

        val logTextView: TextView = findViewById(R.id.textView4)
        val inboxGetOwner: EditText = findViewById(R.id.inboxOwner)
        val inboxRemoveId: EditText = findViewById(R.id.inboxId)
        val inboxRemoveOwner: EditText = findViewById(R.id.removeFollowerOwner)
        val inboxRemoveFollower: EditText = findViewById(R.id.followerToRemove)
        val inboxAddFollower: EditText = findViewById(R.id.addFollowerEdit)
        val inboxAddFollowerOwner: EditText = findViewById(R.id.addFollowerOwner)



        logTextView.movementMethod = ScrollingMovementMethod()

        getUsers.setOnClickListener {
            val apiUser = Apifactory.getUserApi(this)
            val request = apiUser.getUsers()
            request.enqueue(object : Callback<List<User>> {
                override fun onFailure(call: Call<List<User>>?, t: Throwable?) {
                    Timber.e("on failure %s", t.toString())
                    logTextView.text = t.toString()
                }

                override fun onResponse(call: Call<List<User>>?, response: Response<List<User>>?) {
                    if(response != null && response.isSuccessful){
                        logTextView.text = response.body().toString()
                    } else {
                        logTextView.text = response?.message()
                    }
                }

            })
        }

        getInbox.setOnClickListener {
            val apiInboxApi = Apifactory.getInboxApi(this)
            val request = apiInboxApi.getInbox(inboxGetOwner.text.toString())
            request.enqueue(object : Callback<InboxResponse> {
                override fun onFailure(call: Call<InboxResponse>?, t: Throwable?) {
                    Timber.e("on failure %s", t.toString())
                    logTextView.text = t.toString()
                }

                override fun onResponse(call: Call<InboxResponse>?, response: Response<InboxResponse>?) {
                    if(response != null && response.isSuccessful){
                        logTextView.text = response.body().toString()
                    } else {
                        logTextView.text = response?.message()
                    }
                }

            })
        }

        addFollower.setOnClickListener {
            val apiInboxApi = Apifactory.getInboxApi(this)
            val request = apiInboxApi.addInboxRequest(inboxAddFollower.text.toString(),
                                                      inboxAddFollowerOwner.text.toString())
            request.enqueue(object : Callback<GenericResponse> {
                override fun onFailure(call: Call<GenericResponse>?, t: Throwable?) {
                    Timber.e("on failure %s", t.toString())
                    logTextView.text = t.toString()
                }

                override fun onResponse(call: Call<GenericResponse>?, response: Response<GenericResponse>?) {
                    if(response != null && response.isSuccessful){
                        logTextView.text = response.body().toString()
                    } else {
                        logTextView.text = response?.message()
                    }
                }

            })
        }

        removeFollower.setOnClickListener {
            val apiInboxApi = Apifactory.getInboxApi(this)
            val request = apiInboxApi.removeInboxRequest(
                inboxRemoveId.text.toString().toInt(),
                inboxRemoveFollower.text.toString(),
                inboxRemoveOwner.text.toString())
            request.enqueue(object : Callback<GenericResponse> {
                override fun onFailure(call: Call<GenericResponse>?, t: Throwable?) {
                    Timber.e("on failure %s", t.toString())
                    logTextView.text = t.toString()
                }

                override fun onResponse(call: Call<GenericResponse>?, response: Response<GenericResponse>?) {
                    if(response != null && response.isSuccessful){
                        logTextView.text = response.body().toString()
                    } else {
                        logTextView.text = response?.message()
                    }
                }

            })
        }
    }

}
