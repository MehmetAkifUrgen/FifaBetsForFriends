package com.example.fifabet.components

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.fifabet.db.Bahis
import com.example.fifabet.mvvm.HomeViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun BetsListCard(
    homeViewModel: HomeViewModel, item: Bahis
) {

    Card(
        shape = RoundedCornerShape(5),
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .clickable { homeViewModel.delete(item) },
        backgroundColor = MaterialTheme.colors.primary,
        elevation = 2.dp
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = item.bet, color = Color.White)
            Text(text = item.odd.toString(), color = Color.White)


        }
    }
}
