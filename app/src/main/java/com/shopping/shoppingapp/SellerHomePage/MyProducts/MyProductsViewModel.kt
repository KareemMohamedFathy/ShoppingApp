package com.shopping.shoppingapp.SellerHomePage.MyProducts

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.shopping.shoppingapp.DB.Product
import com.shopping.shoppingapp.DB.ShopChat
import com.shopping.shoppingapp.SellerHomePage.MyShop.FValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.StringBuilder
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class MyProductsViewModel:ViewModel() {
    var shopName : MutableState<String> = mutableStateOf("")
    var shopId : MutableState<String> = mutableStateOf("")
    var shopLogo : MutableState<String> = mutableStateOf("")
    //var productsList : MutableLiveData<ArrayList<Product>> = MutableLiveData<ArrayList<Product>>()
        var productsList : MutableList<Product> = mutableStateListOf<Product>()

    var products =   ArrayList<Product>()
        var tagsList :MutableList<String> = mutableStateListOf<String>()
        init {
            tagsList.clear()
            val dbref = FirebaseDatabase.getInstance().getReference("Tag").addValueEventListener( object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (sp in snapshot.children) {
                        val tagName = sp.child("tagName").value.toString()
                        tagsList.add(tagName)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }


    suspend fun  getShop() {
       // productsList.clear()
        val dbReference = FirebaseDatabase.getInstance().getReference()


        val query1=dbReference.child("User").orderByChild("type").equalTo("Seller")


        val auth = FirebaseAuth.getInstance()
        val query = dbReference.child("Shop").orderByChild("user_id").equalTo(auth.currentUser!!.uid)

        val snapshot = query.getSnapshotValue()
        if (snapshot.exists()) {
            for (sp in snapshot.children) {
                val arr: ArrayList<Any?> = arrayListOf(sp.value)
                shopName.value = sp.child("name").value.toString()
                shopLogo.value = sp.child("logo").value.toString()
                shopId.value=sp.child("shop_id").value.toString()
            }
        }
        getProducts()
    }

    suspend fun getProducts() {
        val dbReference = FirebaseDatabase.getInstance().getReference()
        val query = dbReference.child("Product").orderByChild("shop_id").equalTo(shopId.value)
        val snapshot = query.getSnapshotValue()
        val pp=ArrayList<Product>()

        if (snapshot.exists()) {
            for (sp in snapshot.children) {
                val productName = sp.child("name").value.toString()
                val productDescription = sp.child("description").value.toString()
                val shop_id = sp.child("shop_id").value.toString()
                val tags = sp.child("tags").value as ArrayList<String>
                val time = sp.child("time").value.toString()
                var images = sp.child("images").value as ArrayList<String>
                var productPrice = sp.child("price").value.toString()
                val product_id = sp.child("product_id").value.toString()
                   val product=Product(productName,productPrice,productDescription,images,shop_id,product_id,tags,time)
           products.add(product)
            }
            productsList=products

           // Log.d("kusoo",productsList.value.toString())
          //  Log.d("kusoo", productsList.value!!.size.toString())

        }
    }
    suspend fun DatabaseReference.getSnapshotValue(): DataSnapshot {
        return withContext(Dispatchers.IO) {
            suspendCoroutine<DataSnapshot> { continuation ->
                addListenerForSingleValueEvent(
                    FValueEventListener(
                    onDataChange = {
                        continuation.resume(it)
                        val snapshot = it
                    }
                    ,onError = { continuation.resumeWithException(it.toException())},

                    )
                )}

        }



    }


    suspend fun Query.getSnapshotValue(): DataSnapshot {
        return withContext(Dispatchers.IO) {
            suspendCoroutine<DataSnapshot> { continuation ->
                addListenerForSingleValueEvent(
                    FValueEventListener(
                    onDataChange = {
                        continuation.resume(it)
                        val snapshot = it
                    }
                    ,onError = { continuation.resumeWithException(it.toException())},

                    )
                )}
        }
    }

    suspend fun removeProducts(productId: String, shopId: String,product: Product) {
        return withContext(Dispatchers.IO) {


            FirebaseDatabase.getInstance().reference.child("Product").child(productId).removeValue()
            val query =
                FirebaseDatabase.getInstance().reference.child("ShopChat").orderByChild("time")
                    .equalTo(product.time)
            val snapshot = query.getSnapshotValue()
            if (snapshot.exists()) {
                for (sp in snapshot.children) {
                    val chat_id = sp.child("chat_id").value.toString()
                    FirebaseDatabase.getInstance().reference.child("ShopChat").child(chat_id)
                        .removeValue()
                }
            }
            //products.addAll(products)

            products.remove(product)
            productsList=products

            //Log.d("kuso", products.size.toString())
        }

    }
    suspend fun editProduct(idx: Int, product: Product){
        return withContext(Dispatchers.IO) {
        val dbref=FirebaseDatabase.getInstance().getReference("Product").child(productsList[idx].product_id).setValue(product)
            val dbReference = FirebaseDatabase.getInstance().getReference()
            val query = dbReference.child("ShopChat").orderByChild("time").equalTo(product.time)
            val snapshot = query.getSnapshotValue()
            if (snapshot.exists()) {
                for (sp in snapshot.children) {

                    val chat_id = sp.child("chat_id").value.toString()
                    val shop_id = sp.child("shop_id").value.toString()

                    val message: StringBuilder = StringBuilder()
                    message.append("Name: ${product.name}  \n")
                    message.append("Price: ${product.price}  \n")
                    message.append("Description: ${product.description} ")
                    val images = product.images
                    val tags = product.tags
                    val shopChat = ShopChat(
                        message.toString(),
                        chat_id,
                        product.time,
                        images!!,
                        tags!!,
                        shop_id
                    )

                    val dbref1 =
                        FirebaseDatabase.getInstance().getReference("ShopChat").child(chat_id)
                            .setValue(shopChat)


                }
            }


        }


        }
}