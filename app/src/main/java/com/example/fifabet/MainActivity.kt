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
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
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
                Home(homeViewModel)
            }
        }
    }
}

@Composable
fun Greeting(onClick: () -> Unit) {
    FloatingActionButton(onClick = onClick) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "insert")
    }
}


/*@Preview(showBackground = true)
@Composable
fun DefaultPreview() {

    FifaBETTheme {
        Home(homeViewModel = HomeViewModel())
    }
}*/

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun betItem(
    bet: String,
    betChange: (String) -> Unit,
    odd: String,
    oddChange: (String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current



    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(10.dp)
    ) {
        Row(
            modifier = Modifier.background(MaterialTheme.colors.secondary),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(value = bet,
                onValueChange = betChange,
                shape = RectangleShape,
                label = { Text("Bahis", color = Color.White) })
            TextField(

                value = odd,
                onValueChange = oddChange,
                shape = RectangleShape,
                label = { Text("Oran", color = Color.White) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number, imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),


                )
        }

    }
}


@Composable
private fun Home(homeViewModel: HomeViewModel = hiltViewModel()) {

    var bet = homeViewModel.bet.collectAsState().value
    var odd = homeViewModel.odd.collectAsState().value
    var active = homeViewModel.active.collectAsState().value
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









    Log.d("we", listStore.toString())



    Surface(
        modifier = Modifier.fillMaxSize(), color = Color.White
    ) {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
            Scaffold(floatingActionButton = {
                FloatingActionButton(
                    backgroundColor = MaterialTheme.colors.primary,
                    contentColor = Color.White,
                    shape = CircleShape,
                    onClick = {
                        if (data != null) {
                            insertData(map)
                            homeViewModel.fireRead()
                        }
                    }
                ) {
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

                Column(modifier = Modifier.padding(10.dp)) {
                    betItem(
                        bet = bet,
                        betChange = { homeViewModel.updateBet(it) },
                        odd = odd,
                        oddChange = { homeViewModel.updateOdd(it) },
                    )
                    Button(
                        onClick = { homeViewModel.saveOrUpdate() },
                        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
                    ) {
                        Text(text = "Ekle", color = Color.White)
                    }


                    List(data, homeViewModel)
                    BetsList(listStore)


                }

            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun BetsList(listStore: List<Map<String, Object>>) {

    Column(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())

    ) {
        listStore.forEach { item ->

            Card(
                shape = RoundedCornerShape(5),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp),
                backgroundColor = MaterialTheme.colors.primaryVariant,
                elevation = 2.dp
            ) {


                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(5.dp),
                    horizontalAlignment = Alignment.Start
                ) {


                    val jsonArray =
                        JSONTokener(item.values.toString()).nextValue() as JSONArray
                    for (i in 0 until jsonArray.length()) {
                        val checkedState = remember { mutableStateOf(true) }

                        // ID
                        val id = jsonArray.getJSONObject(i).getString("id")
                        Log.i("ID: ", id)

                        // var active = jsonArray.getJSONObject(i).getBoolean("active")
                        // Log.i("Active: ", active.toString())
                        // Employee Name
                        val bet = jsonArray.getJSONObject(i).getString("bet")
                        Log.i("Employee Name: ", bet)

                        // Employee Salary
                        val odd = jsonArray.getJSONObject(i).getString("odd")
                        Log.i("Employee Salary: ", odd)


                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.background(color = Color.Black)
                        ) {
                            Text(text = bet)
                            Text(text = odd)
                            Checkbox(
                                checked = checkedState.value,
                                onCheckedChange = { checkedState.value = it },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = Color.Green,
                                    uncheckedColor = Color.Red
                                )
                            )

                        }
                    }
                }

            }
        }
    }
}

@Composable
fun Boxi() {
    Box() {

    }
}


@Composable
private fun List(data: List<Bahis>?, homeViewModel: HomeViewModel) {
    Column(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
    ) {
        data?.forEach { item ->
            Column() {
                BetsListCard(homeViewModel, item)
            }
        }
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
private fun BetsListCard(
    homeViewModel: HomeViewModel,
    item: Bahis
) {

    Card(
        shape = RoundedCornerShape(5),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp)
            .clickable { homeViewModel.delete(item) },
        backgroundColor = MaterialTheme.colors.secondary,
        elevation = 2.dp
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = item.bet)
            Text(text = item.odd.toString())


        }
    }
}


