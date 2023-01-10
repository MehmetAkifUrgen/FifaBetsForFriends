package com.example.fifabet

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.example.fifabet.db.Bahis
import com.example.fifabet.db.FifaBetsDatabase
import com.example.fifabet.db.BetRepository
import com.example.fifabet.mvvm.HomeViewModel
import com.example.fifabet.ui.theme.FifaBETTheme
import com.google.gson.Gson
import com.google.gson.JsonParser
import kotlinx.coroutines.flow.*
import org.json.JSONArray
import org.json.JSONTokener
import java.util.Objects


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val dao = FifaBetsDatabase.getInstance(application).betDao
        super.onCreate(savedInstanceState)
        setContent {
            FifaBETTheme {
                // A surface container using the 'background' color from the theme
                Home(homeViewModel = HomeViewModel(repository = BetRepository(dao = dao)))
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
private fun Home(homeViewModel: HomeViewModel) {





    var bet = homeViewModel.bet.collectAsState().value
    var odd = homeViewModel.odd.collectAsState().value
    val data = homeViewModel.bets.observeAsState().value
    val listStore= homeViewModel.listStore.collectAsState().value

    Log.d("adsd", data.toString() + bet.toString())

    //val map = mutableMapOf<String, Object>()
   // val list = listOf(Bahis("item1"), Bahis("item2"))
    val map = mutableMapOf<String, Object>()

    if (data != null) {
        data.forEach{ item ->
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
                    betItem(bet = bet,
                        betChange = { homeViewModel.updateBet(it) },
                        odd = odd,
                        oddChange = { homeViewModel.updateOdd(it) },
                       )
                    Button(
                        onClick = {homeViewModel.saveOrUpdate()},
                        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
                    ) {
                        Text(text = "Ekle", color = Color.White)
                    }


                    List(data)
                    Column(
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth(),
                    ) {
                        listStore.forEach { item->




                            var beti:String=""
                            var oddi:String=""
                            item.values.forEach { i ->
                                val obj = i
                                val pattern = "bet=(.+), id=(.+), odd=(.+)".toRegex()
                                val matchResult = pattern.find(obj.toString())
                                if (matchResult != null) {
                                     beti = matchResult.groupValues[1]
                                    val id = matchResult.groupValues[2]
                                     oddi = matchResult.groupValues[3]
                                }
                            }
                           Log.d("ssa", item.values.toString())
                            Card(
                                shape = RoundedCornerShape(5),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 5.dp),
                                backgroundColor = MaterialTheme.colors.primaryVariant,
                                elevation = 2.dp
                            ) {

                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.padding(5.dp),

                                    ) {

                                    val jsonArray = JSONTokener(item.values.toString()).nextValue() as JSONArray
                                    for (i in 0 until jsonArray.length()) {
                                        // ID
                                        val id = jsonArray.getJSONObject(i).getString("id")
                                        Log.i("ID: ", id)

                                        // Employee Name
                                        val bet = jsonArray.getJSONObject(i).getString("bet")
                                        Log.i("Employee Name: ", bet)

                                        // Employee Salary
                                        val odd = jsonArray.getJSONObject(i).getString("odd")
                                        Log.i("Employee Salary: ", odd)

                                        // Employee Age
                                        Text(text =bet)
                                        Text(text = odd)

                                        // Save data using your Model

                                        // Notify the adapter
                                    }

                                }
                            }
                        }
                    }


                }

            }
        }
    }
}





@Composable
private fun FloatButton(onClick: () -> Unit) {
    FloatingActionButton(
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = Color.White,
        shape = CircleShape,
        onClick = { onClick },
    ) {
        Icon(imageVector = Icons.Filled.Add, contentDescription = "icon")
    }
}


@Composable
private fun List(data: List<Bahis>?) {
    Column(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
    ) {
        data?.forEach { item ->
            Card(
                shape = RoundedCornerShape(5),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp),
                backgroundColor = MaterialTheme.colors.secondary,
                elevation = 2.dp
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.padding(5.dp),

                    ) {
                    Text(text = item.bet)
                    Text(text = item.odd.toString())
                }
            }
        }
    }
}

/*@Composable
private fun List2() {

    Column(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
    ) {
        data.forEach { item ->
            Card(
                shape = RoundedCornerShape(5),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp),
                backgroundColor = MaterialTheme.colors.primaryVariant,
                elevation = 2.dp
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.padding(5.dp),

                    ) {
                  //  Text(text =q )
                   // Text(text = item.odd.toString())
                }
            }
        }
    }
}
*/
