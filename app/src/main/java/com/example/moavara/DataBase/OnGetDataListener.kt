package com.example.moavara.DataBase

import com.google.firebase.database.DataSnapshot


interface OnGetDataListener {
    //this is for callbacks
    fun onSuccess(dataSnapshot: DataSnapshot?)
    fun onStart()
    fun onFailure()
}