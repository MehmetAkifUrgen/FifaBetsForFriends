package com.example.fifabet

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.fifabet.components.*
import com.example.fifabet.db.Bahis
import com.example.fifabet.db.Kupon
import com.example.fifabet.mvvm.HomeViewModel
import com.example.fifabet.nav.BottomNavItem
import com.example.fifabet.ui.theme.FifaBETTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val homeViewModel: HomeViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FifaBETTheme {

                    nav(homeViewModel = homeViewModel)

                // A surface container using the 'background' color from the theme

            }
        }
    }
}

@Composable
fun nav(homeViewModel: HomeViewModel) {
    val items = listOf(
       BottomNavItem("Home","home",Icons.Default.Home),
        BottomNavItem("Search","search",Icons.Default.Search),
    )
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomNavigation {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    BottomNavigationItem(
                        icon = { Icon(screen.icon, contentDescription = screen.route) },
                        label = { Text(screen.name) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                // on the back stack as users select items
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(navController, startDestination = "home", Modifier.padding(innerPadding)) {
            composable("home") { Home(homeViewModel) }
            composable("search") { Search(homeViewModel) }
        }
    }
}

@Composable
fun Search(homeViewModel: HomeViewModel) {
    var search = homeViewModel.search.collectAsState().value
    Column() {
        TextField(value = search, onValueChange = {homeViewModel.updateSearch(it)} , leadingIcon = { Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "search", modifier = Modifier.wrapContentSize()
        )})
        Button(onClick = { homeViewModel.fireRead(search) }) {
            Text(text = "ara")
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),

            ) {
            var tre = arrayListOf<Bahis>()
            var data=homeViewModel.listStore.collectAsState().value
            BetsList(listStore = data)

        }
    }
}



@Composable
private fun Home(homeViewModel: HomeViewModel = hiltViewModel()) {


    var bet = homeViewModel.bet.collectAsState().value
    var odd = homeViewModel.odd.collectAsState().value
    var room = homeViewModel.room.collectAsState().value

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
    val navController = rememberNavController()
    val backStackEntry = navController.currentBackStackEntryAsState()


    val context= LocalContext.current

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
                            if (data.isNotEmpty()) {
                                insertData(map, room)
                                homeViewModel.fireRead(room)
                                homeViewModel.insertByRoom(room, data)
                                homeViewModel.clearAll()
                            }
                        }
                        else{
                            Toasts(context)
                        }
                    }) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "icon")
                }
            },
                floatingActionButtonPosition = FabPosition.End,
                isFloatingActionButtonDocked = true,
                bottomBar = {


                },

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


private fun Toasts(context: Context) {
    Toast.makeText(context, "Bahisler olmak zorunda", Toast.LENGTH_LONG).show()
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
                Card() {
                    /*  Image(
                          painter = painterResource(id = R.drawable.paper2),
                          contentDescription = null,
                          contentScale = ContentScale.Crop,
                          modifier = Modifier
                              .matchParentSize()
                              .zIndex(1f),
                          alpha = 0.4f

                      )*/

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(2.dp)


                    ) {
                        Column(
                            modifier = Modifier
                                .paint(
                                    painter = painterResource(id = R.drawable.paper2),
                                    contentScale = ContentScale.Crop
                                )
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(5.dp)
                            ) {
                                Text(
                                    text = "Kupon" + it.id,
                                    fontSize = 21.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = Color.Black
                                )
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "delete",
                                    tint = Color.Red,
                                    modifier = Modifier
                                        .clickable {
                                            homeViewModel.deleteKupon(it)
                                        }
                                )
                            }
                            it.kupons?.forEach {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(5.dp),
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

                    }
                }

            }
        }
    }
}







