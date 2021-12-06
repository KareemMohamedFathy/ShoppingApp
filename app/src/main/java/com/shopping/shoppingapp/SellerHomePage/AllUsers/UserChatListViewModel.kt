package com.shopping.shoppingapp.SellerHomePage.AllUsers


import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.core.view.View
import com.shopping.shoppingapp.DB.Shop
import com.shopping.shoppingapp.DB.User
import com.shopping.shoppingapp.SellerHomePage.MyShop.FValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class UserChatListViewModel: ViewModel() {
    var userStatus : MutableState<String> = mutableStateOf("")
    var users : MutableList<User> = mutableStateListOf<User>()
     fun getFromDb(){
        users.clear()
        val dbReference = FirebaseDatabase.getInstance().getReference()
        val dbref = dbReference.child("User")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (sp in snapshot.children) {
                        val arr: ArrayList<Any?> = arrayListOf(sp.value)
                        val username = sp.child("name").value.toString()
                        val type = sp.child("type").value.toString()
                        val userId = sp.child("user_id").value.toString()
                        val user = User(user_id = userId, type = type, name = username)
                      if(type!="Seller")
                        users.add(user)


                    }
                }
//                        Log.d("tag", entrees.toString())
//                    Log.d("tag", shopi.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        Log.d("Mikasa",users.toString()+"asd")
        Log.d("Mikasa","asasd")


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
                        },
                        onError = { continuation.resumeWithException(it.toException()) },

                        )
                )
            }

        }
    }


}