package com.example.fifabet

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

val db = Firebase.firestore


val map = mutableMapOf<String, Object>()


fun insertData(userData: MutableMap<String, Object>,room:String){
    if (userData != null) {
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
            Toast.makeText(context,"${e.toString()}",Toast.LENGTH_LONG)
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