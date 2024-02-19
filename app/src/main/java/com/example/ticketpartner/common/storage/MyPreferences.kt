package com.example.ticketpartner.common.storage

import android.content.Context
import android.content.SharedPreferences

object MyPreferences {

    private val NAME="MySharedPreferences"
    private val MODE= Context.MODE_PRIVATE
    lateinit var preferences: SharedPreferences

    fun init(context: Context){
        preferences =context.getSharedPreferences(NAME, MODE)
    }
    fun putString(key:String,value:String){
        preferences.edit().putString(key,value).commit()
    }

    fun getString(key: String): String? {
        return preferences.getString(key,"")
    }

    fun clearpref(){
        preferences.edit().clear().commit()

    }


}