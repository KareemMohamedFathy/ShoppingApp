package com.shopping.shoppingapp.Admin.Buyers

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.google.firebase.database.*
import com.shopping.shoppingapp.DB.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BuyersViewModel : ViewModel() {

    var usersName: MutableList<String> = mutableStateListOf<String>()
    var usersType: MutableList<String> = mutableStateListOf<String>()
    var users: MutableList<User> = mutableStateListOf<User>()



    fun getFromDb() {

        usersName.clear()
        usersType.clear()
        users.clear()

        val dbref = FirebaseDatabase.getInstance().getReference().child("User").orderByChild("type")
            .equalTo("Buyer")

        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (sp in snapshot.children) {
                        val arr: ArrayList<Any?> = arrayListOf(sp.value)
                        val buyersType = sp.child("type").value.toString()
                        val buyersEmail = sp.child("email").value.toString()
                        val userId = sp.child("user_id").value.toString()
                        val shopId = sp.child("shop_id").value.toString()
                        val status = sp.child("status").value.toString()




                            val buyersName = sp.child("name").value.toString()
                            val buyer = User(buyersName, buyersEmail, buyersType, userId, shopId,status)
                            usersName.add(buyersName)
                            users.add(buyer)



                    }

                    val dbref = FirebaseDatabase.getInstance().getReference().child("User")
                        .orderByChild("type").equalTo("Seller")

                    dbref.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (sp in snapshot.children) {

                                val buyersType = sp.child("type").value.toString()
                                val buyersEmail = sp.child("email").value.toString()
                                val userId = sp.child("user_id").value.toString()
                                val shopId = sp.child("shop_id").value.toString()
                                val buyersName = sp.child("name").value.toString()
                                val status = sp.child("status").value.toString()

                                val buyer =
                                    User(buyersName, buyersEmail, buyersType, userId, shopId,status)
                                usersName.add(buyersName)
                                users.add(buyer)


                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


    }

   suspend fun unban(user: User) {
        return withContext(Dispatchers.IO) {

            Log.d("tag", user.status + "Ss")
            val user1 =
                User(user.name, user.email, user.type, user.user_id, user.shop_id, "unbanned")
            val dbref = FirebaseDatabase.getInstance().getReference()
            dbref.child("User").child(user.user_id).setValue(user1)
        }
    }

    suspend fun ban(user: User) {
        return withContext(Dispatchers.IO) {


            Log.d("tag", user.status + "Ss")

            val user1 = User(user.name, user.email, user.type, user.user_id, user.shop_id, "banned")
            val dbref = FirebaseDatabase.getInstance().getReference()
            dbref.child("User").child(user.user_id).setValue(user1)

        }
    }
}