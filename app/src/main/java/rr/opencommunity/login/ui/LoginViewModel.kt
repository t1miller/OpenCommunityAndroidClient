package rr.opencommunity.login.ui

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rr.opencommunity.R
import rr.opencommunity.http.Apifactory
import rr.opencommunity.http.TokenResponse
import rr.opencommunity.login.model.User
import rr.opencommunity.settings.PreferenceUtils
import timber.log.Timber

class LoginViewModel(application: Application): AndroidViewModel(application) {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    fun login(username: String, password: String) {
        val apiUser = Apifactory.getUserApi(getApplication())
        val request = apiUser.login(username, password)
        request.enqueue(object : Callback<TokenResponse> {
            override fun onFailure(call: Call<TokenResponse>?, t: Throwable?) {
                Timber.e("on failure %s", t.toString())
                _user.value = User(username, password,"")
            }

            override fun onResponse(call: Call<TokenResponse>?, response: Response<TokenResponse>?) {
                val token = response?.body()?.token ?: ""
                _user.value = User(username, password, token)
                Timber.d("user logging in %s", _user.value)
                PreferenceUtils.setPreference(getApplication(), "username", username)
            }

        })
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}
