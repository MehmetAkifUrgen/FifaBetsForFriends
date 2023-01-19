package com.example.fifabet

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fifabet.components.BetsList
import com.example.fifabet.components.ListDB
import com.example.fifabet.components.betItem
import com.example.fifabet.components.insertButton
import com.example.fifabet.db.Bahis
import com.example.fifabet.db.BetDao
import com.example.fifabet.db.FifaBetsDatabase
import com.example.fifabet.db.BetRepository
import com.example.fifabet.mvvm.HomeViewModel
import com.example.fifabet.ui.theme.FifaBETTheme
import com.google.gson.Gson
import com.google.gson.JsonParser
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import org.json.JSONArray
import org.json.JSONTokener
import java.lang.StringBuilder
import java.util.Objects

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val homeViewModel: HomeViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FifaBETTheme {
                // A surface container using the 'background' color from the theme
                nav(homeViewModel = homeViewModel)
            }
        }
    }
}

@Composable
fun nav(homeViewModel: HomeViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { Home(homeViewModel = homeViewModel) }
        composable("friendslist") { }
        /*...*/
    }
}


@Composable
private fun Home(homeViewModel: HomeViewModel = hiltViewModel()) {

    var bet = homeViewModel.bet.collectAsState().value
    var odd = homeViewModel.odd.collectAsState().value
    var room = homeViewModel.room.collectAsState().value
    var active = homeViewModel.active.collectAsState().value
    var isError = homeViewModel.isError.collectAsState().value

    val data = homeViewModel.bets.observeAsState().value
    val listStore = homeViewModel.listStore.collectAsState().value

    Log.d("adsd", data.toString() + bet.toString())

    //val map = mutableMapOf<String, Object>()
    // val list = listOf(Bahis("item1"), Bahis("item2"))
    val map = mutableMapOf<String, Object>()

    if (data != null) {
        data.forEach { item ->
            map[item.id.toString()] = item as Object
        }
    }




    Surface(
        modifier = Modifier.fillMaxSize(), color = Color.White
    ) {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
            Scaffold(floatingActionButton = {
                FloatingActionButton(backgroundColor = MaterialTheme.colors.primary,
                    contentColor = Color.White,
                    shape = CircleShape,
                    onClick = {
                        if (data != null) {
                            insertData(map, room)
                            homeViewModel.fireRead(room)
                        }
                    }) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "icon")
                }
            },
                floatingActionButtonPosition = FabPosition.End,
                isFloatingActionButtonDocked = true,
                bottomBar = {
                    BottomAppBar(
                        backgroundColor = MaterialTheme.colors.primary, cutoutShape = CircleShape
                    ) {

                    }
                }

            ) {

                Column(
                    modifier = Modifier.padding(10.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    betItem(
                        bet = bet,
                        betChange = { homeViewModel.updateBet(it) },
                        odd = odd,
                        oddChange = { homeViewModel.updateOdd(it) },
                        isError
                    )
                    insertButton(homeViewModel)


                    ListDB(data, homeViewModel)
                    if (data != null) {
                        if (data.isNotEmpty()) {
                            Row(modifier = Modifier.clip(shape = RoundedCornerShape(20.dp))) {
                                TextField(value = room,
                                    modifier = Modifier.background(MaterialTheme.colors.primary),
                                    shape = RectangleShape,
                                    label = { Text("Oda Kodu", color = Color.White) },
                                    onValueChange = { homeViewModel.updateRoom(it) })

                            }
                        }
                    }
                    BetsList(listStore)


                }

            }
        }
    }
}






