package com.example.fifabet.components

import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.fifabet.mvvm.HomeViewModel

@Composable
fun  insertButton(homeViewModel: HomeViewModel) {

    Button(
        onClick = { homeViewModel.saveOrUpdate() },
        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),

        ) {
        Text(text = "Ekle", color = Color.White)
    }

}