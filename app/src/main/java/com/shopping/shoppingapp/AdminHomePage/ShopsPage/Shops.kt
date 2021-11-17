package com.shopping.shoppingapp.AdminHomePage.ShopsPage

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.shopping.shoppingapp.DB.Shop
import kotlinx.coroutines.launch

@ExperimentalCoilApi
@Composable
fun Shops(navController: NavController, shopsViewModel: ShopsViewModel = viewModel()) {
    var shopsList = remember { mutableStateListOf<Shop>() }
    val scope = rememberCoroutineScope()

    BackHandler() {
        navController.popBackStack()
    }
    LaunchedEffect(key1 = Unit) {
        shopsViewModel.getShops()
        shopsList.addAll(shopsViewModel.productsList as SnapshotStateList<Shop>)
        Log.d("kuso",shopsViewModel.productsList.toList().toString())
    }

    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(8.dp),
    horizontalAlignment = Alignment.Start,
    verticalArrangement = Arrangement.spacedBy(8.dp)

    ) {
        itemsIndexed(shopsList) { index, item ->

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp),

           ) {


            Card() {
                RoundedCornerShape(50.dp)
                Image(
                    painter = rememberImagePainter(
                        item.logo,

                        ), contentDescription = "",
                    Modifier
                        .width(70.dp)
                        .height(80.dp)
                        ,
                            contentScale = ContentScale.FillBounds,
                    
                )
            }
                Column(
                    Modifier
                        .padding(8.dp),

                    ) {
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(text = item.name, fontSize = 18.sp,fontWeight = FontWeight.Bold)
                    Row(
                        Modifier
                            .padding(8.dp),

                        ) {


                        Button(onClick = {
                            scope.launch {
                                shopsViewModel.acceptShop(item.shop_id)
                            }
                        },   colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green)
                        ) {
                            Text(text = "Accept Shop")
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(onClick = {
                            scope.launch {
                                shopsViewModel.rejectShop(item.shop_id)
                            }

                                         },   colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
                        ) {
                            Text(text = "Reject Shop")
                        }
                    }
                }
        }
        }
    }
}