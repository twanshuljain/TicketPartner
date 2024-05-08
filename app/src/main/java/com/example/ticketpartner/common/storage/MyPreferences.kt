package com.example.ticketpartner.common.storage

import android.content.Context
import android.content.SharedPreferences
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