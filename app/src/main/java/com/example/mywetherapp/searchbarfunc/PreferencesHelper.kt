package com.example.mywetherapp.searchbarfunc

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PreferencesHelper(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("weather_app_prefs", Context.MODE_PRIVATE)

    fun saveSearchQuery(query: String) {
        val editor = prefs.edit()
        val history = getSearchHistory()
        if (!history.contains(query)) {
            history.add(query)
            val json = Gson().toJson(history)
            editor.putString("search_history", json)
            editor.apply()
        }
    }

    fun getSearchHistory(): MutableList<String> {
        val json = prefs.getString("search_history", "")
        val type = object : TypeToken<MutableList<String>>() {}.type
        return Gson().fromJson(json, type) ?: mutableListOf()
    }
}
