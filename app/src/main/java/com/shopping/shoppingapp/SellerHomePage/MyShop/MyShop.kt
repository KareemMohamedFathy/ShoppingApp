package com.shopping.shoppingapp.SellerHomePage.MyShop

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.google.gson.Gson
import com.shopping.shoppingapp.DB.ShopChat
import com.shopping.shoppingapp.R
import com.shopping.shoppingapp.Screen
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.min


val String.color
    get() = Color(android.graphics.Color.parseColor(this))
@ExperimentalCoilApi
@Composable
fun MyShop(navController: NavController, myShopViewModel:MyShopViewModel= viewModel()) {
    val scope = rememberCoroutineScope()
    val context= LocalContext
    var shopName by remember { mutableStateOf("") }
    var shopLogo by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var messagesList = remember { mutableStateListOf<ShopChat>() }
    val listState = rememberLazyListState()
    LaunchedEffect(key1 =Unit ) {
            Log.d("kusoo","hi")
            messagesList.clear()
            myShopViewModel.getShop()
        shopName = myShopViewModel.shopName.value
        shopLogo = myShopViewModel.shopLogo.value
        Log.d("kusoo","hi")
        messagesList.addAll(myShopViewModel.messagesList)
        listState.animateScrollToItem(messagesList.size-1)
    }
    BackHandler() {
        navController.popBackStack()
    }


    Log.d("kuso", messagesList.size.toString())



    Scaffold(
        content = {
            LazyColumn(

                Modifier
                    .fillMaxSize()
                    .padding(start = 48.dp, top = 24.dp, bottom = 70.dp, end = 16.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(8.dp)
                  ,state = listState
            ) {

                itemsIndexed(messagesList) { index, item ->
                    Card(


                        Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),

                        ) {


                        Column(
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.Top, modifier = Modifier
                                .background("#26A69A".color)
                                .padding(4.dp)

                        ) {
                            if(messagesList[index].images!!.size!=0) {
                                Card(
                                    shape = RoundedCornerShape(16.dp),

                                    ) {
                                    Box(
                                        Modifier
                                            .fillMaxWidth()
                                            .height(250.dp)
                                    ) {



                                        Image(
                                            painter = rememberImagePainter(
                                                messagesList[index].images?.getOrNull(
                                                    0
                                                )
                                            ),
                                            contentDescription = "",
                                            Modifier
                                                .fillMaxWidth()
                                                .height(250.dp)
                                                .clickable(onClick = {
                                                    var connectionsJSONString =
                                                        Gson().toJson(messagesList[index].images)
                                                    myShopViewModel.imageList.addAll(messagesList[index].images)
                                                    navController.navigate(
                                                        Screen.ProductPhotos.withArgs(index.toString())
                                                    )
                                                }),
                                            contentScale = ContentScale.FillBounds
                                        )
                                        if(messagesList[index].images.size>1) {
                                            Text(
                                                text = "+${messagesList[index].images.size}",
                                                fontSize = 35.sp,
                                                color = Color.White,
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier.align(alignment = Alignment.Center)
                                            )
                                        }

                                    }
                                }
                            }
                            Text(messagesList[index].message, color = Color.White, fontSize = 24.sp)
                            if(messagesList[index].images!!.size!=0) {

                                Row()

                                {
                                    var tagString = ""
                                    for (tags in messagesList[index].tags!!) {
                                        tagString += "#$tags  "
                                    }
                                    Text(tagString, color = Color.Blue, fontSize = 24.sp)

                                }
                            }
                        var dateText = ""
                    val sdf = SimpleDateFormat("dd/M/yyyy HH:mm:ss")
                    val date1: Date? = sdf.parse(messagesList[index].time)
                            val sdf1 = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")

                            val date2: Date? = sdf1.parse(sdf1.format(Date()))
                    val diff: Long = date2!!.getTime() - date1!!.getTime()
                            Log.d("kuso2",date1.toString())
                            Log.d("kuso3",date2.toString())
                            Log.d("kuso4",diff.toString())



                            val seconds = diff / 1000
                    val minutes = seconds / 60
                    val hours = minutes / 60
                    val days = hours / 24
                    if (days >= 1) {
                        dateText = ("$days d . ")
                    } else if (hours >= 1) {
                        dateText = ("$hours h . ")
                    } else if (minutes < 60) {
                        dateText = ("$minutes m . ")
                    } else if (seconds >= 0) {
                        dateText = ("$seconds s . ")
                    }


    Text(
        text = dateText,
        textAlign = TextAlign.End,
        modifier = Modifier
            .align(Alignment.End)
            .padding(end = 16.dp),
        fontSize = 20.sp,

        color = Color.White
    )

}
                        }

                    }
                }

        }
       ,
        bottomBar = {

            Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .background(Color.White)
            ,
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
            ){

            Spacer(modifier = Modifier.width(16.dp))



    OutlinedTextField(
        value = message,
        onValueChange = { message = it },
        label = {
            Text("Message")
        },
        shape = RoundedCornerShape(36.dp),

        modifier = Modifier
            .background(Color.White)
            .weight(0.5f)

    )
    Spacer(modifier = Modifier.width(16.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_send), contentDescription = "",
                modifier = Modifier
                    .size(40.dp)
                    .clickable(onClick = {
                        scope.launch {
                            //  messagesList.clear()
                            myShopViewModel.addChat(message)
                            messagesList.add(myShopViewModel.messagesList[messagesList.size])

                            listState.animateScrollToItem(messagesList.size - 1)
                            Log.d("kusoo", "hi2")

                            message = ""
                        }
                    }),
                contentScale = ContentScale.FillBounds,
            )
                Spacer(modifier = Modifier.width(16.dp))
        }

                    }
        ,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(8.dp))

                Image(
                    painter = rememberImagePainter(shopLogo), contentDescription = "",
                    contentScale = ContentScale.FillBounds,
                    modifier =Modifier.size(40.dp)

                    )

                Spacer(modifier = Modifier.width(16.dp))
                 Text(shopName, fontSize = 20.sp,color = Color.White)

            }
        }
    )
}

fun spacedByWithFooter(space: Dp) = object : Arrangement.Vertical {

    override val spacing = space

    override fun Density.arrange(
        totalSize: Int,
        sizes: IntArray,
        outPositions: IntArray,
    ) {
        if (sizes.isEmpty()) return
        val spacePx = space.roundToPx()

        var occupied = 0
        var lastSpace = 0

        sizes.forEachIndexed { index, size ->

            if (index == sizes.lastIndex) {
                outPositions[index] = totalSize - size
            } else {
                outPositions[index] = min(occupied, totalSize - size)
            }
            lastSpace = min(spacePx, totalSize - outPositions[index] - size)
            occupied = outPositions[index] + size + lastSpace
        }
        occupied -= lastSpace
    }
}

fun getCurrentDate():String{
    val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
    val currentDate = sdf.format(Date())
    return currentDate
}
