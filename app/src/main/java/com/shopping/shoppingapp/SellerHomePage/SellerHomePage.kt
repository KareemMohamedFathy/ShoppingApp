package com.shopping.shoppingapp.SellerHomePage

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.shopping.shoppingapp.Screen
import com.shopping.shoppingapp.SellerHomePage.AddProduct.AddProductActivity

@Composable
fun SellerHomePage(navController: NavController,sellerHomePageViewodel: SellerHomePageViewModel= viewModel()){
    var shopStatus = remember { mutableStateOf("") }
    var userStatus = remember { mutableStateOf("") }

    LaunchedEffect(key1 ="" ){
    sellerHomePageViewodel.getShopStatus()
        shopStatus.value=sellerHomePageViewodel.shopStatus.value
        userStatus.value=sellerHomePageViewodel.userStatus.value
    }
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
        ,horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {



        if(shopStatus.value=="Accepted"&&!userStatus.value.equals("banned")) {
            CreateButton(name = ButtonsName.POSTPRODUCT, navController)
            CreateButton(name = ButtonsName.MYSHOP, navController)
            CreateButton(name = ButtonsName.MESSAGES, navController)
            CreateButton(name = ButtonsName.ALLUSERS, navController)
            CreateButton(name = ButtonsName.MYPRODUCTS, navController)
        }
        else if(userStatus.value.equals("banned")){
            Spacer(modifier = Modifier.height(64.dp))
            Text(text = "Your account has been suspended", fontSize = 40.sp,
            fontWeight = FontWeight.Bold
                )

        }
        else if(shopStatus.value.equals("Rejected")){
            Spacer(modifier = Modifier.height(64.dp))
            Text(text = "Plz wait till your shop gets approved", fontSize = 40.sp,
                fontWeight = FontWeight.Bold
            )

        }

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
        if(ButtonsName.ALLUSERS==name){
            Log.d("eren","Mikasa")
            navController.navigate(Screen.UsersList.route)
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

