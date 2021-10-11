package com.shopping.shoppingapp.SellerHomePage.MyProducts

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.size.OriginalSize
import coil.size.Scale
import com.shopping.shoppingapp.DB.Product
import com.shopping.shoppingapp.DB.ShopChat
import com.shopping.shoppingapp.R
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@ExperimentalCoilApi
@Composable
fun MyProducts(navController: NavController,myProductsViewModel: MyProductsViewModel= viewModel()) {
  //  var productsList = remember { mutableStateListOf<Product>() }
    val scope = rememberCoroutineScope()

    val listState = rememberLazyListState()

    var currentImage = remember { mutableStateListOf<Int>() }
    //val productsList by myProductsViewModel.productsList.observeAsState(emptyList())
    var productsList = remember { mutableStateListOf<Product>() }

    LaunchedEffect(key1 = Unit){
       myProductsViewModel.getShop()
        productsList.addAll(myProductsViewModel.productsList)
        currentImage.addAll(List(productsList.size) {0})

    }
    var pickedImage: MutableState<String?> =remember { mutableStateOf("") }
    Log.d("hi",productsList.size.toString()+"ss")
    BackHandler() {
        navController.popBackStack()
    }
       LazyColumn(
           Modifier
               .fillMaxSize()
               .padding(start = 16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp)
            ,state = listState
        ) {
           itemsIndexed(productsList) { index, item ->
                       Card(
                           Modifier
                               .fillMaxWidth(),
                           shape = RoundedCornerShape(16.dp),
                           elevation = 16.dp
                       ) {
                           ConstraintLayout(
                               modifier = Modifier.fillMaxSize()
                           ) {
                               val (name, price,description,image,tag,delete,edit) = createRefs()
                               val guideline = createGuidelineFromStart(fraction = 0.37f)
                               Box(

                                   modifier = Modifier.constrainAs(image) {
                                       top.linkTo(parent.top, 2.dp)
                                       linkTo(start = parent.start, end = guideline)
                                       start.linkTo(parent.start, 2.dp)
                                       bottom.linkTo(parent.bottom, 2.dp)
                                       width = Dimension.fillToConstraints
                                       height = Dimension.value(150.dp)
                                   }
                               ) {

                                   Image(
                                       painter = rememberImagePainter(
                                           item.images!![currentImage[index]],
                                           builder = {
                                               size(OriginalSize)
                                               scale(Scale.FILL)
                                           }
                                       ), contentDescription = "",

                                       contentScale = ContentScale.FillBounds
                                   )

                                   Image(
                                       painterResource(id = R.drawable.ic_previous) , contentDescription = ""
                                       ,modifier = Modifier
                                           .width(15.dp)
                                           .height(15.dp)
                                           .clickable(onClick = {
                                               if (currentImage[index] <= 0) {
                                                   currentImage[index] = 0
                                               } else {
                                                   currentImage[index]--
                                               }
                                           }
                                           )
                                           .align(Alignment.CenterStart)


                                   )
                                   Image(
                                       painterResource(id = R.drawable.ic_next_button) , contentDescription = ""
                                       ,modifier = Modifier
                                           .width(15.dp)
                                           .height(15.dp)
                                           .clickable(onClick = {
                                               if (currentImage[index] + 1 >= item.images!!.size) {
                                                   currentImage[index] = item.images!!.size - 1
                                               } else {
                                                   currentImage[index]++
                                               }
                                           }
                                           )
                                           .align(Alignment.CenterEnd)

                                   )

                               }
                               Text(text = item.name, fontSize = 18.sp,modifier = Modifier.constrainAs(name){
                                   top.linkTo(parent.top,8.dp)
                                   linkTo(guideline, parent.end,startMargin = 8.dp)
                                   end.linkTo(price.start,8.dp)
                                   width= Dimension.fillToConstraints


                               },fontWeight = FontWeight.Bold)
                               Text(
                                   text = "${item.price}EGP",
                                   fontSize = 18.sp,modifier = Modifier.constrainAs(price){
                                       start.linkTo(name.end,8.dp)
                                       top.linkTo(parent.top,8.dp)
                                       end.linkTo(delete.start,8.dp)
                                       width= Dimension.fillToConstraints

                                   }
                               )
                               Text(text = item.description, fontSize = 14.sp,modifier = Modifier.constrainAs(description){
                                   linkTo(guideline, parent.end,startMargin = 8.dp)
                                   top.linkTo(name.bottom,8.dp)
                                   end.linkTo(delete.start,8.dp)

                                   width= Dimension.fillToConstraints
                                   height= Dimension.wrapContent

                               })
                               var tagString = ""
                               for (tags in item.tags!!) {
                                   tagString += "#$tags  "
                               }
                               Text(tagString, color = Color.Blue, fontSize = 14.sp,modifier = Modifier.constrainAs(tag){
                                   linkTo(guideline, parent.end,startMargin = 8.dp)

                                   top.linkTo(description.bottom,8.dp)
                                   width = Dimension.preferredWrapContent
                                   end.linkTo(delete.start,8.dp)


                               })
                               Icon( Icons.Default.Delete,
                       contentDescription = "",modifier = Modifier.constrainAs(delete)
                               {
                                   start.linkTo(price.end,8.dp)
                                   end.linkTo(parent.end,8.dp)
                                   top.linkTo(parent.top,8.dp)

                               }
                                       .size(40.dp)
                                       .clickable (onClick = {
                                           scope.launch {
                                               myProductsViewModel.removeProducts(
                                                   item.product_id,
                                                   item.shop_id,
                                                   item
                                               )
                                               productsList.remove(item)
                                           }
                                           })
                                ,tint = Color.Red
                               )
                               Icon( Icons.Default.Edit,
                                   contentDescription = "",modifier = Modifier.constrainAs(edit)
                                   {
                                       start.linkTo(price.end,8.dp)
                                       end.linkTo(parent.end,8.dp)
                                       top.linkTo(delete.bottom,16.dp)

                                   }
                                       .size(40.dp)
                                   ,tint = Color.Black
                               )
                           }


                       }
           }
    }
}