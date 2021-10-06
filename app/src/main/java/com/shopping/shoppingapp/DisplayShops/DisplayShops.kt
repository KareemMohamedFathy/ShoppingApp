package com.shopping.shoppingapp.DisplayShops

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.shopping.shoppingapp.DB.Shop
import com.shopping.shoppingapp.Screen


@Composable
 fun DisplayShops(navController: NavController, viewModel: DisplayShopViewModel = viewModel()) {

    var shopnames = remember { mutableStateListOf<String>() }
    var shoplogos = remember { mutableStateListOf<String>() }
    var shops = remember { mutableStateListOf<Shop>() }
    BackHandler() {
        navController.popBackStack()
    }
    LaunchedEffect(key1 = Unit){
        viewModel.getFromDb()
    }
    shopnames=(viewModel.entrees as SnapshotStateList<String>)
    shoplogos=(viewModel.shopi as SnapshotStateList<String>)
    shops=(viewModel.shopss as SnapshotStateList<Shop>)





    LazyColumn (
        modifier = Modifier.fillMaxSize()
    ){


        itemsIndexed(shops){index, item ->
            Card(modifier = Modifier
                .height(80.dp)
                .fillMaxWidth()
                .padding(
                    bottom = 2.dp,
                    top = 2.dp
                ),
            ) {
                shopview(navController, shop = shops[index])
            }


//                Card(
//                    modifier = Modifier
//                        .height(80.dp)
//                        .fillMaxWidth()
//                        .padding(
//                            bottom = 4.dp,
//                            top = 4.dp
//                        ),
//                ){
//                    Text(shopnames[index])
//                }


        }

    }



}
@Composable
fun shopview(navController: NavController , shop: Shop){
    var isClicked by remember { mutableStateOf(false) }
    Row(modifier = Modifier.padding(all = 8.dp) .clickable { isClicked = true }) {
        Image(
            painter = rememberImagePainter(shop.logo),
            contentDescription = null,
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colors.secondaryVariant, CircleShape)
            ,contentScale = ContentScale.FillBounds
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column() {
            Text(
                text = shop.name,
                color = MaterialTheme.colors.primaryVariant,
                style = MaterialTheme.typography.h6
            )
//                Spacer(modifier = Modifier.width(8.dp))
//            Log.d("tag",shop.toString())

        }
        if (isClicked){
            navController.navigate(Screen.PrivateChats.withArgs(shop.shop_id.toString()))
        }

    }


}
