package rr.opencommunity.settings

import android.content.Context
import androidx.preference.PreferenceManager


object PreferenceUtils {

    fun getPreference(context: Context, key: String, defaultValue: String) : String{
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    fun setPreference(context: Context, key: String, value: String) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getUsername(context: Context) : String {
        return getPreference(context,"inboxApiUsername","")
    }
}