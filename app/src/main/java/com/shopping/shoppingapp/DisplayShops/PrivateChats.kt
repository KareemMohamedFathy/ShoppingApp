package com.shopping.shoppingapp.DisplayShops

import android.os.Build
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.runtime.*
import androidx.compose.runtime.R
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.google.gson.Gson
import com.shopping.shoppingapp.DB.Chat
import com.shopping.shoppingapp.DB.ShopChat
import com.shopping.shoppingapp.Screen
import com.shopping.shoppingapp.SellerHomePage.MyShop.MyShopViewModel
import com.shopping.shoppingapp.SellerHomePage.MyShop.color
import kotlinx.coroutines.launch
import org.intellij.lang.annotations.JdkConstants
import java.text.SimpleDateFormat
import java.util.*


@ExperimentalCoilApi
@Composable
fun PrivateChats(navController: NavController, shopid: String, privatechatsviewModel:  PrivateChatViewModel = viewModel()){
//    privatechatsviewModel.shopId.value = shopid
    val scope = rememberCoroutineScope()
    val context= LocalContext
    var clickedMessage by remember { mutableStateOf(-1) }
    var shopName by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf("") }

    var shopLogo by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var messagesList = remember { mutableStateListOf<Chat>() }
    var shopmessagesList = remember { mutableStateListOf<Chat>() }
    val listState = rememberLazyListState()
    LaunchedEffect(key1 =Unit ) {
        messagesList.clear()
        privatechatsviewModel.getUser(shopid)
        privatechatsviewModel.getShop(shopid)
        shopName = privatechatsviewModel.shopName.value
        shopLogo = privatechatsviewModel.shopLogo.value
        userName=privatechatsviewModel.userName.value
        messagesList.addAll(privatechatsviewModel.messagesList)
        shopmessagesList.addAll(privatechatsviewModel.shopmessagesList)
//        privatechatsviewModel.sortbytime(messagesList,shopmessagesList)
        messagesList.addAll(privatechatsviewModel.shopmessagesList)
        val pattern = "dd/M/yyyy hh:mm:ss"
        val dateformat = SimpleDateFormat(pattern)
        //Log.d("kuso155",messagesList.toList().toString())


        messagesList.sortBy { SimpleDateFormat( "dd/M/yyyy hh:mm:ss").parse(it.time)}
     //   Log.d("kuso555",privatechatsviewModel.shopmessagesList.toList().toString())

        //messagesList.reverse()
        listState.animateScrollToItem(messagesList.size-1)
    }
    BackHandler() {
        navController.navigate(Screen.DisplayShops.route){
            popUpTo(Screen.PrivateChats.route){
                inclusive = true
            }
        }
    }


    Scaffold(
        content = {
            Column(
                Modifier
                    .fillMaxSize()

            ) {


                LazyColumn(

                    Modifier
                        .weight(0.5f)
                        .padding(start = 48.dp, top = 24.dp, bottom = 16.dp, end = 16.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(8.dp), state = listState
                ) {

                    itemsIndexed(messagesList) { index, item ->
                        if(messagesList[index].messagReply!=-1){
                            val idx=messagesList[index].messagReply
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(0.dp, 8.dp, 8.dp, 0.dp)
                                .background("#EEEEEE".color),
                            verticalArrangement = Arrangement.Center,
                            ) {
                            Divider(color = Color.Red, thickness = 5.dp,)
                            Box(
                                modifier = Modifier.fillMaxWidth()
                            ) {

                                if  (messagesList[idx].sender != userName) {
                                    Text(
                                        text = "Shop",
                                        color = Color.Red,
                                        fontSize = 20.sp,
                                        modifier = Modifier.padding(8.dp, 4.dp, 0.dp, 0.dp)
                                            .align(Alignment.TopStart)
                                    )
                                } else {
                                    Text(
                                        text = "You",
                                        color = Color.Red,
                                        fontSize = 20.sp,
                                        modifier = Modifier.padding(8.dp, 4.dp, 0.dp, 0.dp)
                                            .align(Alignment.TopStart)
                                    )
                                }
                            }
                            if (messagesList[idx].images!!.size != 0) {
                                Card(
                                    shape = RoundedCornerShape(16.dp),


                                    ) {
                                    Box(
                                        modifier = Modifier
                                            .width(150.dp)
                                            .height(100.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Image(
                                            painter = rememberImagePainter(
                                                messagesList[idx].images?.getOrNull(
                                                    0
                                                )
                                            ),
                                            contentDescription = "",
                                            Modifier
                                                .width(150.dp)
                                                .height(100.dp),
                                            contentScale = ContentScale.FillBounds
                                        )
                                        if (messagesList[idx].images.size > 1) {
                                            Text(
                                                text = "+${messagesList[idx].images.size}",
                                                fontSize = 35.sp,
                                                color = Color.Black,
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier.align(alignment = Alignment.Center)
                                            )
                                        }
                                    }
                                }
                            }
                            Text(
                                messagesList[idx].message,
                                color = Color.Black,
                                maxLines = 2,
                                fontSize = 28.sp,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(8.dp, 0.dp, 0.dp, 0.dp)

                            )

                        }
                        }

                            Card(


                            Modifier
                                .fillMaxWidth()
                                .pointerInput(Unit) {
                                    detectTapGestures(
                                        onLongPress = { /* Called on Long Press */
                                            clickedMessage = index

                                        },
                                    )
                                }

                                ,
                            shape = RoundedCornerShape(16.dp),

                            ) {
                            var color = "#26A69A".color
                            if (!messagesList[index].user_id.equals("")) {
                                color = Color.DarkGray
                            }
                            Column(
                                horizontalAlignment = Alignment.Start,
                                verticalArrangement = Arrangement.Top, modifier = Modifier
                                    .background(color)
                                    .padding(4.dp, 4.dp, 4.dp, 0.dp)

                            ) {
                                if (messagesList[index].images!!.size != 0&&messagesList[index].messagReply==-1) {
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
                                                        privatechatsviewModel.imageList.addAll(
                                                            messagesList[index].images
                                                        )
                                                        navController.navigate(
                                                            Screen.ViewPhotos.withArgs(
                                                                index.toString(),
                                                                shopid
                                                            )
                                                        )
                                                    }),
                                                contentScale = ContentScale.FillBounds
                                            )
                                            if (messagesList[index].images.size > 1) {
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
                                Text(
                                    messagesList[index].message,
                                    color = Color.White,
                                    fontSize = 24.sp
                                )
                                if (messagesList[index].images!!.size != 0) {

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
                var index=clickedMessage
                if(clickedMessage!=-1) {
                    Column(
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Top, modifier = Modifier
                            .padding(32.dp, 8.dp, 4.dp, 0.dp)
                            .background(Color.White)

                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp, 8.dp, 64.dp, 0.dp)
                                .background("#EEEEEE".color)

                            ,
                            verticalArrangement = Arrangement.Center,

                        ) {
                            Divider(color = Color.Red, thickness = 5.dp,)
                            Box(
                                modifier = Modifier.fillMaxWidth()
                            ) {

                                if (messagesList[index].sender!=null&&messagesList[index].sender!=userName) {
                                    Text(
                                        text = "Shop",
                                        color = Color.Red,
                                        fontSize = 20.sp,

                                        modifier = Modifier.padding(8.dp, 4.dp, 0.dp, 0.dp)
                                            .align(Alignment.TopStart)


                                    )
                                } else {
                                    Text(
                                        text = "You",
                                        color = Color.Red,
                                        fontSize = 20.sp,
                                        modifier = Modifier.padding(8.dp, 4.dp, 0.dp, 0.dp)
                                            .align(Alignment.TopStart)

                                    )
                                }
                            Image(painterResource(id = (com.shopping.shoppingapp.R.drawable.ic_close)

                            ),modifier = Modifier.align(Alignment.TopEnd)
                                .clickable(onClick = {
                                    clickedMessage=-1
                                })
                                ,contentDescription =""  )
                            }



                            if (messagesList[index].images!!.size != 0) {
                                Card(
                                    shape = RoundedCornerShape(16.dp),


                                    ) {
                                    Box(
                                        modifier = Modifier
                                            .width(150.dp)
                                            .height(100.dp),
                                        contentAlignment = Alignment.Center


                                    ) {


                                        Image(
                                            painter = rememberImagePainter(
                                                messagesList[index].images?.getOrNull(
                                                    0
                                                )
                                            ),
                                            contentDescription = "",
                                            Modifier
                                                .width(150.dp)
                                                .height(100.dp),
                                            contentScale = ContentScale.FillBounds
                                        )

                                        if (messagesList[index].images.size > 1) {
                                            Text(
                                                text = "+${messagesList[index].images.size}",
                                                fontSize = 35.sp,
                                                color = Color.Black,
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier.align(alignment = Alignment.Center)
                                            )
                                        }

                                    }
                                }
                            }
                            Text(
                                messagesList[index].message,
                                color = Color.Black,
                                maxLines = 2,
                                fontSize = 28.sp,
                                overflow = TextOverflow.Ellipsis
                            , modifier = Modifier.padding(8.dp,0.dp,0.dp,0.dp)

                            )
                        }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(4.dp, 0.dp, 4.dp, 0.dp)
                                    .background(Color.White),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {

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
                                    painter = painterResource(id = com.shopping.shoppingapp.R.drawable.ic_send),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clickable(onClick = {
                                            scope.launch {
                                                //  messagesList.clear()
                                                privatechatsviewModel.addChat(message,clickedMessage,
                                                messagesList[index].images.let {
                                                    messagesList[index].images
                                                }
                                                    )
                                                messagesList.add(privatechatsviewModel.messagesList[privatechatsviewModel.messagesList.size - 1])

                                                listState.animateScrollToItem(messagesList.size - 1)
                                                Log.d("kusoo", "hi2")

                                                message = ""
                                                clickedMessage=-1
                                            }
                                        }),
                                    contentScale = ContentScale.FillBounds,
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                            }

                    }
                }
                else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp, 0.dp, 4.dp, 0.dp)
                            .background(Color.White),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

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
                            painter = painterResource(id = com.shopping.shoppingapp.R.drawable.ic_send),
                            contentDescription = "",
                            modifier = Modifier
                                .size(40.dp)
                                .clickable(onClick = {
                                    scope.launch {
                                        //  messagesList.clear()
                                        privatechatsviewModel.addChat(message)
                                        messagesList.add(privatechatsviewModel.messagesList[privatechatsviewModel.messagesList.size - 1])

                                        listState.animateScrollToItem(messagesList.size - 1)

                                        message = ""
                                    }
                                }),
                            contentScale = ContentScale.FillBounds,
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                    }
                }

            }
        }

        ,
        bottomBar = {
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
                    modifier = Modifier.size(40.dp)

                )

                Spacer(modifier = Modifier.width(16.dp))
                Text(shopName, fontSize = 20.sp,color = Color.White)

            }
        }
    )
}