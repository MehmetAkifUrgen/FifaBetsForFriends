package com.example.fifabet.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.fifabet.mvvm.HomeViewModel
import okhttp3.internal.wait

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun betItem(
    bet: String,
    betChange: (String) -> Unit,
    odd: String,
    oddChange: (String) -> Unit,
    isError: Boolean,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val configuration = LocalConfiguration.current

    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp





    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(10.dp)
            .clip(shape = RoundedCornerShape(20.dp))
    ) {
        Row(
            modifier = Modifier
                .background(MaterialTheme.colors.primary)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,

            ) {

            TextField(
                value = bet,
                colors = TextFieldDefaults.textFieldColors(textColor = Color.White),
                trailingIcon = {
                    if (isError) Icon(
                        Icons.Filled.Warning, "error", tint = MaterialTheme.colors.error
                    )
                },
                isError = isError,
                maxLines = 3,
                onValueChange = betChange,
                shape = RectangleShape,
                label = { Text("Bahis", color = Color.White) },
                modifier = Modifier.width(screenWidth / 1.5f),
                keyboardActions = KeyboardActions { keyboardController?.hide() },

            )


            TextField(
                colors = TextFieldDefaults.textFieldColors(textColor = Color.White),
                value = odd,
                isError = isError,
                onValueChange = oddChange,
                shape = RectangleShape,
                label = { Text("Oran", color = Color.White) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number, imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions( onDone =  { keyboardController?.hide() }),
                modifier = Modifier.fillMaxWidth(),

                )
        }
        if (isError) {
            Text(
                text = "Boş Olamaz",
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

    }
}