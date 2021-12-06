package com.shopping.shoppingapp.Admin.Buyers

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.shopping.shoppingapp.DB.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
    fun DisplayBuyers(navController: NavController , buyersViewModel: BuyersViewModel = viewModel()){

    var buyernames= remember { mutableStateListOf<String>() }
        var buyerss= remember { mutableStateListOf<User>() }
    val scope = rememberCoroutineScope()


    BackHandler() {
            navController.popBackStack()
        }
        LaunchedEffect(key1 = Unit){
            buyersViewModel.getFromDb()
            delay(500)
            buyernames.addAll(buyersViewModel.usersName as SnapshotStateList<String>)
            buyerss.addAll(buyersViewModel.users as SnapshotStateList<User>)

        }

        LazyColumn(modifier=Modifier.border(BorderStroke(2.dp, Color.Blue))){
            itemsIndexed(buyerss){
                index, item ->  Card(
                modifier = Modifier
                    .fillParentMaxWidth()
                    .padding(all=8.dp))
            {
                Spacer(modifier = Modifier.width(8.dp))
                var isClicked by remember { mutableStateOf(false) }
                Column() {
                    Text(
                        text = item.name,
                        color = MaterialTheme.colors.primaryVariant,
                        style = MaterialTheme.typography.h6
                    )


                    Row(modifier = Modifier.padding(all = 8.dp)) {

                        Button(
                            onClick = {
                                scope.launch {
                                    buyersViewModel.unban(item)
                                }
                            }, colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green)
                        ) {
                            Text(text = "Unban user")
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = {
                                scope.launch {
                                    buyersViewModel.ban(item)
                                }

                            }, colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
                        ) {
                            Text(text = "Ban user")
                        }
                    }
                }
            }
            }

        }

    }

