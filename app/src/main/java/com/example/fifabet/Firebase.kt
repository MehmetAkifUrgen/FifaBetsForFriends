package com.example.fifabet

import android.util.Log
import androidx.lifecycle.Observer
import com.example.fifabet.db.Bahis
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Objects

val db = Firebase.firestore


val map = mutableMapOf<String, Object>()


fun insertData(userData: MutableMap<String, Object>){
    if (userData != null) {
        db.collection("bets")
            .add(userData)
            .addOnSuccessListener { documentReference ->
                Log.d("data","DocumentSnapshot added with ID: ${documentReference.id}")

            }
            .addOnFailureListener { e ->
                Log.w("Error adding document", e)
            }
    }
}

/*public fun readData() : MutableList<Bahis>? {
    var items : mutableMapOf<String, Object>()
    db.collection("bets")
        .get()
        .addOnSuccessListener {result ->

            for (document in result) {
                Log.d("TAG", "${document.id} => ${document.data}")
                items[document.id.toString()] = document.data
            }
            //    items = it.result?.toObjects(Bahis::class.java) as MutableList<Bahis>


        }
        .addOnFailureListener { exception ->
            Log.w("err", "Error getting documents.", exception)
        }

    return items

}*/