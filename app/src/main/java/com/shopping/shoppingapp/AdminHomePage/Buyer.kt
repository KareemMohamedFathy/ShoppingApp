package com.shopping.shoppingapp.Admin.Buyers

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.shopping.shoppingapp.DB.User

@Composable
    fun DisplayBuyers(navController: NavController , buyersViewModel: BuyersViewModel = viewModel()){
    Log.d("tag", "Buyersssssss frontend")

    var buyernames= remember { mutableStateListOf<String>() }
        var buyerss= remember { mutableStateListOf<User>() }
    Log.d("tag", "Buyersssssss frontend"+buyerss.toString())

    BackHandler() {
            navController.popBackStack()
        }
        LaunchedEffect(key1 = Unit){
            buyersViewModel.getFromDb()
        }
        buyernames=(buyersViewModel.usersName as SnapshotStateList<String>)
        buyerss=(buyersViewModel.users as SnapshotStateList<User>)

        LazyColumn(modifier=Modifier.border(BorderStroke(2.dp, Color.Blue))){
            itemsIndexed(buyerss){
                index, item ->  Card(
                modifier = Modifier
                    .height(60.dp)
                    .fillParentMaxWidth()
                    .padding(bottom = 2.dp, top = 2.dp)) {
                    buyerView(buyer=buyerss[index])
            }
            }

        }

    }
@Composable
fun buyerView(buyer: User){
    Row(modifier=Modifier.padding(all = 8.dp)){
        Spacer(modifier = Modifier.width(8.dp))
        var isClicked by remember { mutableStateOf(false) }
        Column(modifier = Modifier.clickable { isClicked= !isClicked }) {
            Text(
                text = buyer.name,
                color = MaterialTheme.colors.primaryVariant,
                style = MaterialTheme.typography.h6
            )
//                Spacer(modifier = Modifier.width(8.dp))

        }
    }
}
