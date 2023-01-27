package com.example.fifabet

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fifabet.components.ListByRoom
import com.example.fifabet.components.ListDB
import com.example.fifabet.components.betItem
import com.example.fifabet.components.insertButton
import com.example.fifabet.db.Kupon
import com.example.fifabet.mvvm.HomeViewModel
import com.example.fifabet.ui.theme.FifaBETTheme
import dagger.hilt.android.AndroidEntryPoint

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


@OptIn(ExperimentalFoundationApi::class, ExperimentalUnitApi::class)
@Composable
private fun Home(homeViewModel: HomeViewModel = hiltViewModel()) {

    var bet = homeViewModel.bet.collectAsState().value
    var odd = homeViewModel.odd.collectAsState().value
    var room = homeViewModel.room.collectAsState().value
    var active = homeViewModel.active.collectAsState().value
    var isError = homeViewModel.isError.collectAsState().value

    val data = homeViewModel.bets.observeAsState().value
    val kupons = homeViewModel.kupons.observeAsState().value
    val byRoomData = homeViewModel.byRooms.observeAsState().value
    val listStore = homeViewModel.listStore.collectAsState().value

    Log.d("adsd", kupons.toString())

    //val map = mutableMapOf<String, Object>()
    // val list = listOf(Bahis("item1"), Bahis("item2"))
    val map = mutableMapOf<String, Object>()

    if (data != null) {
        byRoomData?.forEach { item ->
            map[item.id.toString()] = item as Object
        }
    }

    Log.i("data", data.toString())




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
                            homeViewModel.insertByRoom(room, data)
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
                    ListByRoom(byRoomData, homeViewModel)
                    //BetsList(listStore)

                    KuponList(kupons, homeViewModel)


                }

            }

        }
    }
}

@Composable
fun KuponList(
    kupons: List<Kupon>?,
    homeViewModel: HomeViewModel
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
            .padding(0.dp, 0.dp, 0.dp, 50.dp)
    ) {

        kupons?.forEach {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Box {
                    Image(
                        painter = painterResource(id = R.drawable.paper2),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .matchParentSize()
                            .zIndex(1f),
                        alpha = 0.4f

                    )

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(2.dp)
                            .padding(5.dp)

                    ) {
                        Column(

                        ) {
                            Text(
                                text = "Kupon" + it.id,
                                fontSize = 21.sp,
                                fontFamily = FontFamily.Monospace,
                                color = MaterialTheme.colors.primary
                            )
                            it.kupons?.forEach {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                )
                                {
                                    Text(
                                        text = it.bet,
                                        fontSize = 18.sp,
                                        fontFamily = FontFamily.Monospace
                                    )
                                    Text(
                                        text = it.odd.toString(),
                                        fontSize = 18.sp,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                            }
                        }
                        Column(modifier = Modifier.zIndex(2f)) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                tint = Color.Red,
                                contentDescription = "Delete",
                                modifier = Modifier
                                    .clickable {
                                        homeViewModel.deleteKupon(it)
                                    }
                                    .zIndex(2f)
                            )
                        }
                    }
                }

            }
        }
    }
}







