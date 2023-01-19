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
import androidx.compose.ui.unit.dp
import org.json.JSONArray
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


                    val jsonArray = JSONTokener(item.values.toString()).nextValue() as JSONArray
                    for (i in 0 until jsonArray.length()) {
                        val checkedState = remember { mutableStateOf(false) }

                        // ID
                        val id = jsonArray.getJSONObject(i).getString("id")
                        Log.i("ID: ", id)

                        var active = jsonArray.getJSONObject(i).getBoolean("active")
                        Log.i("Active: ", active.toString())
                        // Employee Name
                        val bet = jsonArray.getJSONObject(i).getString("bet")
                        Log.i("Employee Name: ", bet)

                        // Employee Salary
                        val odd = jsonArray.getJSONObject(i).getString("odd")
                        Log.i("Employee Salary: ", odd)


                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()

                        ) {
                            Text(text = bet)
                            Text(text = odd)
                            Checkbox(
                                checked = checkedState.value,
                                onCheckedChange = { checkedState.value = it },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = Color.White, uncheckedColor = Color.Red
                                )
                            )

                        }
                    }
                }

            }
        }
    }
}