package com.shopping.shoppingapp.SellerHomePage

import android.content.Intent
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
import com.shopping.shoppingapp.SellerHomePage.AddProduct.AddProductActivity

@Composable
fun SellerHomePage(navController: NavController){
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
        ,horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        CreateButton(name = ButtonsName.POSTPRODUCT,navController)
        CreateButton(name = ButtonsName.MYSHOP,navController)
        CreateButton(name = ButtonsName.MESSAGES,navController)
        CreateButton(name = ButtonsName.ALLSHOPS,navController)
        CreateButton(name = ButtonsName.MYPRODUCTS,navController)

    }
} 
@Composable
fun CreateButton(name: String, navController: NavController){
    Spacer(modifier = Modifier.height(16.dp))
    val context = LocalContext.current
    Button(onClick = {

                     if(ButtonsName.POSTPRODUCT==name){
                         context.startActivity(Intent(context, AddProductActivity::class.java))
                     }
        if(ButtonsName.MYSHOP==name){
            navController.navigate(Screen.MyShop.route)
        }
        if(ButtonsName.ALLSHOPS==name){
            navController.navigate(Screen.DisplayShops.route)
        }
        if(ButtonsName.MYPRODUCTS==name){
            navController.navigate(Screen.MyProducts.route)
        }
    },
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
        ) {
        Text(text = name
        ,fontSize = 30.sp
        )
    }
}

