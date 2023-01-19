package com.example.fifabet.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.fifabet.db.Bahis
import com.example.fifabet.mvvm.HomeViewModel

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