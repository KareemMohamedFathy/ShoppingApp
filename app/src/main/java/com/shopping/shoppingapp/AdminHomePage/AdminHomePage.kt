package com.shopping.shoppingapp.AdminHomePage

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.shopping.shoppingapp.Screen


@Composable
fun AdminHomePage(navController: NavController) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
        ,horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        CreateButton(name = ButtonsAdmin.SHOPS,navController)
        CreateButton(name = ButtonsAdmin.BUYERS,navController)
        CreateButton(name = ButtonsAdmin.TAGS,navController)
        CreateButton(name = ButtonsAdmin.RECENTLYADDED,navController)
    }
}

@Composable
fun CreateButton(name: String, navController: NavController){
    Spacer(modifier = Modifier.height(16.dp))
    val context = LocalContext.current
    Button(onClick = {

      if(ButtonsAdmin.SHOPS==name){
         navController.navigate(Screen.Shops.route)
      }
        if (ButtonsAdmin.BUYERS == name) {
            navController.navigate(Screen.Buyers.route)
            //   Log.d("AdmindisplayScreen", Screen.Buyers.route)}
        }
        if (ButtonsAdmin.TAGS == name) {
            navController.navigate(Screen.AddTags.route)
            //   Log.d("AdmindisplayScreen", Screen.Buyers.route)}
        }
    }
        ,modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
    ){
        Text(text = name
            ,fontSize = 30.sp
        )
    }
}