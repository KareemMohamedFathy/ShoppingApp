package com.shopping.shoppingapp.SellerHomePage.MyShop

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.google.gson.reflect.TypeToken
import com.shopping.shoppingapp.Screen
import java.lang.reflect.Type

@ExperimentalCoilApi
@Composable
fun ProductPhotos(navController: NavController,myShopViewModel: MyShopViewModel= viewModel(),index:String) {
    var imagesList = remember { mutableStateListOf<String>() }
    val type: Type = object : TypeToken<ArrayList<String>>() {}.type

    LaunchedEffect(key1 = imagesList ) {
    myShopViewModel.getShop()

    }
    BackHandler() {
        navController.navigate(Screen.MyShop.route) {
            popUpTo(Screen.MyShop.route) {
                inclusive = true
        }
        }
    }
    imagesList.clear()
    if(myShopViewModel.messagesList.getOrNull(index.toInt())!=null)
    imagesList.addAll(myShopViewModel.messagesList[index.toInt()].images)
    Log.d("kuso6", myShopViewModel.imageList.toString())

   LazyColumn(
       Modifier
           .fillMaxSize()
           .padding(8.dp)
        ,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {



        itemsIndexed(imagesList) { index, item ->
            Card(


                Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),

                ) {
                Image(
                    painter = rememberImagePainter(imagesList[index]),
                    contentDescription = "",
                    Modifier
                        .fillMaxWidth()
                        .height(380.dp),
                    contentScale = ContentScale.FillBounds
                )
                Divider(color = Color.Black, thickness = 8.dp)
            }
            }
    }
}