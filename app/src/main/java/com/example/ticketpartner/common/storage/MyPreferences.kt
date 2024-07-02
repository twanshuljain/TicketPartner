package com.example.ticketpartner.common.storage

import android.content.Context
import android.content.SharedPreferences
import com.example.ticketpartner.common.storage.PrefConstants.LOGGED_USER_DETAILS
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.LoginWithPinResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object MyPreferences {

    private val NAME="MySharedPreferences"
    private val MODE= Context.MODE_PRIVATE
    lateinit var preferences: SharedPreferences
    private lateinit var gson:Gson

    fun init(context: Context){
        preferences =context.getSharedPreferences(NAME, MODE)
        gson = Gson()
    }
    fun putString(key:String, value: String){
        preferences.edit().putString(key,value).commit()
    }

    fun getString(key: String): String? {
        return preferences.getString(key,"")
    }

    fun putArrayList(key: String, list: ArrayList<String>) {
        val json = gson.toJson(list)
        preferences.edit().putString(key, json).apply()
    }

    fun getUserDetails(): LoginWithPinResponse? {
        val gson = Gson()
        val userJson = getString(LOGGED_USER_DETAILS)
        return userJson?.let {
            gson.fromJson(it, LoginWithPinResponse::class.java)
        }
    }

    fun getArrayList(key: String): ArrayList<String> {
        val json = preferences.getString(key, null)
        val type = object : TypeToken<ArrayList<String>>() {}.type
        return gson.fromJson(json, type) ?: ArrayList()
    }

    fun clearArrayList(key: String) {
        val json = preferences.edit().remove(key).commit()
    }

    fun clearpref(){
        preferences.edit().clear().commit()

    }


}