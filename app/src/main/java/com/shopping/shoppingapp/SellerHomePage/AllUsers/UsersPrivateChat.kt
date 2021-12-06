package com.shopping.shoppingapp.SellerHomePage.AllUsers

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.shopping.shoppingapp.DisplayShops.PrivateChatViewModel
import com.shopping.shoppingapp.DisplayShops.UsersPrivateChatViewModel
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



@Composable
fun UsersPrivateChats(navController: NavController, usersPrivateChatViewModel: UsersPrivateChatViewModel = viewModel()) {

}