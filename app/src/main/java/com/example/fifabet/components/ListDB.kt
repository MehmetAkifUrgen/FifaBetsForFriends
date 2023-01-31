package com.example.fifabet.components

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.traceEventEnd
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fifabet.db.Bahis
import com.example.fifabet.db.ByRoom
import com.example.fifabet.db.Kupon
import com.example.fifabet.mvvm.HomeViewModel
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONTokener

@Composable
fun ListDB(data: List<Bahis>?, homeViewModel: HomeViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        data?.forEach { item ->
            Column(modifier = Modifier.padding(5.dp)) {
                BetsListCard(homeViewModel, item)
            }
        }
    }
}

@SuppressLint("SuspiciousIndentation")
@Composable
fun ListByRoom(data: List<ByRoom>?, homeViewModel: HomeViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var tre = arrayListOf<Bahis>()


        Column(
            modifier = Modifier
                .padding(10.dp)
                .shadow(1.dp, RectangleShape),

        ) {

            data?.forEach { item ->
                Text(text = item.roomCode)
                // Log.i("item", item.betsData?.toList())
                item.betsData?.forEach {
                    val checkedState = remember { mutableStateOf(it.active.toBoolean()) }
                    Column() {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp)
                        ) {
                            Text(
                                text = it.bet,
                                textAlign = TextAlign.Start,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = it.odd.toString(),
                                textAlign = TextAlign.Start,
                                modifier = Modifier.weight(1f)
                            )
                            Checkbox(
                                checked = checkedState.value,
                                onCheckedChange = { checkedState.value = it },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = Color.Green, uncheckedColor = Color.Red
                                )
                            )

                        }
                    }
                    if (checkedState.value) {
                        tre.add(it)
                    } else tre.remove(it)

                }

            }
        }
        Log.d("tre", tre.toString())
        Button(onClick = { homeViewModel.insertKupon(Kupon(0, tre)) }) {
            Text(
                text = "Kuponu Oyna", fontSize = 18.sp,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}