package com.shopping.shoppingapp.displayshops

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.ui.platform.ComposeView
import com.shopping.shoppingapp.R
import com.shopping.shoppingapp.ui.theme.MyApplicationTheme
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.shopping.shoppingapp.DB.Shop


class DisplayShopsFragment : Fragment() {
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireActivity()).apply {
            setContent {
                MyApplicationTheme {
                    displayShops()

                }
            }
        }
    }


    class displayShopViewModel : ViewModel() {

        var entrees : MutableList<String> = mutableStateListOf<String>()
        var shopi : MutableList<String> = mutableStateListOf<String>()
        var shopss : MutableList<Shop> = mutableStateListOf<Shop>()
        init{
            val dbReference = FirebaseDatabase.getInstance().getReference()
            val dbref = dbReference.child("Shop")
            dbref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (sp in snapshot.children) {
                            val arr: ArrayList<Any?> = arrayListOf(sp.value)
                            val shopname = sp.child("name").value.toString()
                            val shopicon = sp.child("logo").value.toString()
                            val shop = Shop()
                            shop.name = shopname
                            shop.logo = shopicon
                            entrees.add(shopname)
                            shopi.add(shopicon)
                            shopss.add(shop)
                        }
                    }
//                        Log.d("tag", entrees.toString())
//                    Log.d("tag", shopi.toString())
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })


        }

    }


    @Composable

    private fun displayShops(viewModel: displayShopViewModel = viewModel()) {

        val shopnames = remember { mutableStateListOf<String>() }
        val shoplogos = remember { mutableStateListOf<String>()}
        val shops = remember { mutableStateListOf<Shop>()}


        shopnames.addAll(viewModel.entrees)
        shoplogos.addAll(viewModel.shopi)
        shops.addAll(viewModel.shopss)





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
                    shopview(shop = shops[index])
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
    fun shopview(shop: Shop){
        Row(modifier = Modifier.padding(all = 8.dp)) {
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
            var isClicked by remember { mutableStateOf(false) }
            Column(modifier = Modifier.clickable { isClicked= !isClicked }) {
                Text(
                    text = shop.name,
                    color = MaterialTheme.colors.primaryVariant,
                    style = MaterialTheme.typography.h6
                )
//                Spacer(modifier = Modifier.width(8.dp))

            }

        }
    }
}