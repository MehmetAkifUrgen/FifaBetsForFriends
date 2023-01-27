package com.example.fifabet

import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

val db = Firebase.firestore


fun insertData(userData: MutableMap<String, Object>, room: String){
    try {
        db.collection(room)
            .add(userData)
            .addOnSuccessListener { documentReference ->
                Log.d("data","DocumentSnapshot added with ID: ${documentReference.id}")

            }
            .addOnFailureListener { e ->
                Log.w("Error adding document", e)
            }
    } catch (e:Exception){
        val context = null
        Toast.makeText(context, e.toString(),Toast.LENGTH_LONG)
    }
}

