package com.example.fifabet.mvvm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fifabet.Event
import com.example.fifabet.db.Bahis
import com.example.fifabet.db.BetRepository
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HomeViewModel(private val repository: BetRepository) : ViewModel() {
    val db = Firebase.firestore
    val bets = repository.bets

    private val _listStore = MutableStateFlow<List<Map<String,Object>>>(emptyList())
    private var isUpdateOrDelete = false
    private lateinit var betToUpdateOrDelete: Bahis
    private val _betList = MutableStateFlow<List<Bahis>>(emptyList())
    private val _bet = MutableStateFlow<String>("")
    private val _odd = MutableStateFlow<String>("")
    val betList = _betList.asStateFlow()
    val bet = _bet.asStateFlow()
    val odd = _odd.asStateFlow()
    val listStore= _listStore.asStateFlow()

    private val statusMessage = MutableLiveData<Event<String>>()

    val message: LiveData<Event<String>>
        get() = statusMessage


    init{
        db.collection("bets")
            .get()
            .addOnSuccessListener {result ->
                val items = arrayListOf<Any>()
                for (document in result) {
                    Log.d("TAG", "${document.id} => ${document.data}")
                    items.add(document.data)
                    Log.d("kav",listStore.toString())
                }
                _listStore.value=items as List<Map<String, Object>>
                Log.w("ss",_listStore.value.toString())
                //    items = it.result?.toObjects(Bahis::class.java) as MutableList<Bahis>


            }
            .addOnFailureListener { exception ->
                Log.w("err", "Error getting documents.", exception)
            }
    }

    fun updateBet(input: String) {
        viewModelScope.launch {
            // async operation
            // DO NOT DO THIS. ANTI-PATTERN - updating after an async op
            _bet.value = input
        }
    }

    fun updateOdd(input: String) {
        viewModelScope.launch {
            // async operation

            // DO NOT DO THIS. ANTI-PATTERN - updating after an async op
            _odd.value = input
        }
    }

    fun saveOrUpdate() {
       /* if (isUpdateOrDelete) {
            betToUpdateOrDelete.bet = _bet.value!!
            betToUpdateOrDelete.odd = _odd.value.toFloat()!!
            update(betToUpdateOrDelete)
        } else {*/
            val bet = _bet.value!!
            val odd = _odd.value!!
            Log.d("data",bet+odd)
            insert(Bahis(0, bet, odd.toFloat()))


    }

    fun clearAllOrDelete() {
        if (isUpdateOrDelete) {
            delete(betToUpdateOrDelete)
        } else {
            clearAll()
        }
    }

    fun insert(bet: Bahis) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(bet)
        withContext(Dispatchers.Main) {
            statusMessage.value = Event("Bet Inserted Successfully!")
        }
    }

    fun update(bet: Bahis) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(bet)
        withContext(Dispatchers.Main) {
            _bet.value = ""
            _odd.value = ""
            isUpdateOrDelete = false
            // saveOrUpdateButtonText.value = "Save"
            // clearAllOrDeleteButtonText.value = "Clear All"
            statusMessage.value = Event("Bet Updated Successfully!")
        }
    }

    fun delete(bet: Bahis) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(bet)
        withContext(Dispatchers.Main) {
            _bet.value = ""
            _odd.value = ""
            isUpdateOrDelete = false
            // saveOrUpdateButtonText.value = "Save"
            //clearAllOrDeleteButtonText.value = "Clear All"
            statusMessage.value = Event("Bet Deleted Successfully!")
        }
    }

    fun clearAll() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAll()
        withContext(Dispatchers.Main) {
            statusMessage.value = Event("All Bets Deleted Successfully!")
        }
    }


}