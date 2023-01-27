package com.example.fifabet.mvvm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fifabet.Event
import com.example.fifabet.db.Bahis
import com.example.fifabet.db.BetRepository
import com.example.fifabet.db.ByRoom
import com.example.fifabet.db.Kupon
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: BetRepository) : ViewModel() {
    val db = Firebase.firestore
   val bets = repository.bets
    val byRooms = repository.byRooms
    val kupons= repository.kupons

    private val _listStore = MutableStateFlow<List<Map<String, Object>>>(emptyList())
    private var isUpdateOrDelete = false
    private lateinit var betToUpdateOrDelete: Bahis
    private val _betList = MutableStateFlow<List<Bahis>>(emptyList())
    private val _bet = MutableStateFlow("")
    private val _odd = MutableStateFlow("")
    private val _room = MutableStateFlow("")
    private val _active = MutableStateFlow("false")
    private val _isError = MutableStateFlow(false)
    val betList = _betList.asStateFlow()
    val bet = _bet.asStateFlow()
    val odd = _odd.asStateFlow()
    val isError = _isError.asStateFlow()
    val room = _room.asStateFlow()
    val active = _active.asStateFlow()
    val listStore = _listStore.asStateFlow()


    private val statusMessage = MutableLiveData<Event<String>>()

    val message: LiveData<Event<String>>
        get() = statusMessage


    init {

    }

    fun validate() {
        _isError.value = !(_bet.value.isNotEmpty() && _odd.value.isNotEmpty())
    }




    fun fireRead(room: String) {
        db.collection(room)
            .get()
            .addOnSuccessListener { result ->
                val items = arrayListOf<Any>()
                for (document in result) {
                    Log.d("TAG", "${document.id} => ${document.data}")
                    items.add(document.data)
                    Log.d("kav", listStore.toString())
                }
                _listStore.value = items as List<Map<String, Object>>
                Log.w("ss", _listStore.value.toString())
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

    fun updateRoom(input: String) {
        viewModelScope.launch {
            // async operation
            // DO NOT DO THIS. ANTI-PATTERN - updating after an async op
            _room.value = input
        }
    }

    fun updateOdd(input: String) {
        viewModelScope.launch {
            // async operation

            // DO NOT DO THIS. ANTI-PATTERN - updating after an async op
            _odd.value = input
        }
    }
    fun insertByRoom(room:String,data:List<Bahis>){
        Log.d("data2",data.toString())
        insertByRoom(ByRoom(0,data,room))
    }


    fun saveOrUpdate() {
        val bet = _bet.value!!
        val odd = _odd.value!!
        val active = _active.value!!
        Log.d("data", bet + odd)
        if (bet.isNotEmpty() && odd.isNotEmpty()) {
            insert(Bahis(0, bet, odd.toFloat(), active))
            _bet.value = ""
            _odd.value = ""
            _isError.value = false
        } else {
            _isError.value = true
        }
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

    fun insertByRoom(byRoom: ByRoom) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertByRoom(byRoom)
        withContext(Dispatchers.Main) {
            statusMessage.value = Event("ByRoom Inserted Successfully!")
        }
    }
    fun insertKupon(kupon: Kupon) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertKupon(kupon)
        withContext(Dispatchers.Main) {
            statusMessage.value = Event("Kupon Inserted Successfully!")
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
    fun deleteKupon(kupon: Kupon) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteKupon(kupon)
        withContext(Dispatchers.Main) {
            statusMessage.value = Event("Kupon Deleted Successfully!")
        }
    }

    fun clearAll() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAll()
        withContext(Dispatchers.Main) {
            statusMessage.value = Event("All Bets Deleted Successfully!")
        }
    }


}