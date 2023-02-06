package com.example.fifabet.components

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.fifabet.db.Bahis
import com.google.gson.Gson
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener




@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BetsList(listStore: List<Map<String, Object>>) {

    Column(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())

    ) {
        var tre = arrayListOf<Bahis>()
        listStore.forEach { item ->

            Card(
                shape = RoundedCornerShape(5),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp),
                backgroundColor = MaterialTheme.colors.primary,
                elevation = 2.dp
            ) {


                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(5.dp),
                    horizontalAlignment = Alignment.Start
                ) {




                       // Log.d("ww",)
                       /* var dat2a = jsonArray.getJSONObject(i)["betsData"] as JSONArray

                            val checkedState = remember { mutableStateOf(false) }
                            val id = dat2a.getJSONObject(i).getString("id")
                            Log.i("ID: ", id)

                            //var active = jsonArray.getJSONObject(i).getBoolean("active")
                            //Log.i("Active: ", active.toString())
                            // Employee Name
                            val bet = dat2a.getJSONObject(i).getString("bet")
                            Log.i("Employee Name: ", bet)

                            // Employee Salary
                            val odd = dat2a.getJSONObject(i).getString("odd")
                            Log.i("Employee Salary: ", odd)
                           if(bet.isNullOrEmpty()){

                           }
                            else{
                               Column() {
                                   Row(
                                       horizontalArrangement = Arrangement.SpaceBetween,
                                       verticalAlignment = Alignment.CenterVertically,
                                       modifier = Modifier
                                           .fillMaxWidth()
                                           .padding(5.dp)
                                   ) {
                                       Text(
                                           text = bet,
                                           textAlign = TextAlign.Start,
                                           modifier = Modifier.weight(1f)
                                       )
                                       Text(
                                           text = odd.toString(),
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
                           }
                            if (checkedState.value) {
                                tre.add(Bahis(id.toInt(),bet,odd.toFloat()))
                            } else tre.remove(Bahis(id.toInt()))

*/

                }

            }
        }
    }
}



