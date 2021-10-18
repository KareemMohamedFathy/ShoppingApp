package com.shopping.shoppingapp.Admin.Buyers

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.shopping.shoppingapp.DB.User

class BuyersViewModel : ViewModel() {

    var usersName :MutableList<String> = mutableStateListOf<String>()
    var usersType :MutableList<String> = mutableStateListOf<String>()
    var users :MutableList<User> = mutableStateListOf<User>()

    fun getFromDb() {

        usersName.clear()
        usersType.clear()
        users.clear()
        Log.d("tag", "Wenabyyyyyyyyy")

        val dbref = FirebaseDatabase.getInstance().getReference().child("User").orderByChild("type").equalTo("Buyer")
        Log.d("tag", "Buyers view model")

        dbref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    Log.d("tag", "Buyers view model")

                    for(sp in snapshot.children){
                      val arr: ArrayList<Any?> = arrayListOf(sp.value)
                      val buyersType=sp.child("type").value.toString()
                        Log.d("tag", "Fi amal shwaya ")

                        if(buyersType.equals("Buyer")){
                          Log.d("tag", "Fi amal")

                          val buyersName=sp.child("name").value.toString()
                          val buyer=User()
                          buyer.name=buyersName

                      }

                    }


                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })



    }


}