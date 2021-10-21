package com.shopping.shoppingapp.SellerHomePage.MyProducts

import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.shopping.shoppingapp.DB.Product
import com.shopping.shoppingapp.Screen
import kotlinx.coroutines.*


@Composable
fun EditProduct(navController: NavController,index:String,myProductsViewModel: MyProductsViewModel= viewModel()) {
    val idx = index.toInt()
    var productsList = remember { mutableStateListOf<Product>() }
    val scope = rememberCoroutineScope()
    var tagsList : MutableList<String> = remember {
        mutableStateListOf<String>()}

    var set : MutableList<String> = remember{
        mutableListOf<String>()

    }
    val mainstate = rememberScrollState()
    BackHandler() {
    navController.navigate(Screen.MyProducts.route){
        navController.popBackStack()
    }
    }

    LaunchedEffect(key1 = Unit){
        myProductsViewModel.getShop()
        productsList.addAll(myProductsViewModel.productsList)
        set.addAll(productsList[idx].tags!!)
        Log.d("kuso", set.toString()+"hi")

    }
    tagsList=myProductsViewModel.tagsList

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(mainstate)
        , horizontalAlignment = Alignment.CenterHorizontally
        ,
        verticalArrangement = Arrangement.Top
    ) {
        var name by remember { mutableStateOf(TextFieldValue(""))}
        var price by remember { mutableStateOf(TextFieldValue("")) }
        var description by remember { mutableStateOf(TextFieldValue("")) }

        if(productsList.size!=0&&TextUtils.isEmpty(name.text)&&TextUtils.isEmpty(price.text)&&TextUtils.isEmpty(description.text)) {
             name =TextFieldValue(productsList.getOrNull(idx)?.name!!)
             price= TextFieldValue(productsList.getOrNull(idx)?.price!!)
            description= TextFieldValue(productsList.getOrNull(idx)?.description!!)
        }
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = name,
            onValueChange = { name = it },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Enter Name")
            }
        )

        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            modifier = Modifier.fillMaxWidth(), label = {
                Text("Enter Description")
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = price,
            onValueChange = { price = it },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Enter price")
            }
        )
       Spacer(modifier = Modifier.height(16.dp))


        val state = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)


        ) {
            Log.d("kuso", set.toString()+"hi")

            Text(text = "Add Tags to your product (max 5)",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            for(tagslist in tagsList)
                MultipleCheckBox(tagslist,myProductsViewModel,set)

            Button(onClick = {
                scope.launch {
                    val arrayList:ArrayList<String> = ArrayList<String>()
                    for(tag in set){
                        arrayList.add(tag)
                    }


                    val pp=Product(name.text,price.text,description.text,productsList[idx].images,productsList[idx].shop_id,productsList[idx].product_id,arrayList,productsList[idx].time)
                    myProductsViewModel.editProduct(idx,pp)
                    navController.navigate(Screen.MyProducts.route){
                        navController.popBackStack()
                    }
                }
                             },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(text = "Edit Product",
                    fontSize = 20.sp,
                )
            }

        }
    }
}

@Composable
fun MultipleCheckBox(text: String, myProductsViewModel: MyProductsViewModel,set:MutableList<String>){
    Row(
        modifier = Modifier.fillMaxWidth()
        ,
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Start
    ) {
        val checkedState = remember { mutableStateOf(false) }
        if(set.contains(text)){
            checkedState.value=true

        }
        if(set.size>=5&&checkedState.value&&!set.contains(text)){
            checkedState.value=false
            Toast.makeText(LocalContext.current,"Max is 5 tags", Toast.LENGTH_SHORT).show()
        }
        else if(checkedState.value&&!set.contains(text)){
            set.add(text)
        }
        else if(!checkedState.value&&set.contains(text)){
            set.remove(text)
        }
        Checkbox(
            checked = checkedState.value,
            modifier = Modifier.padding(16.dp),
            onCheckedChange = { checkedState.value = it
                              if(!checkedState.value) {
                              set.remove(text)
                              }
                else if(set.size<5){
                    set.add(text)
                }
                              },
        )
        Text(text = text, modifier = Modifier.padding(16.dp)
            ,fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }

}


