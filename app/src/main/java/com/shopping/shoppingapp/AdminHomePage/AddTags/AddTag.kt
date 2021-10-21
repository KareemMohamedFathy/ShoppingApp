package com.shopping.shoppingapp.AdminHomePage.AddTags

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.firebase.database.FirebaseDatabase
import com.shopping.shoppingapp.DB.Tag

@Composable
fun AddTag(navController: NavHostController,addTagViewModel: AddTagViewModel=viewModel()) {
    var name by remember { mutableStateOf(TextFieldValue("")) }
    LaunchedEffect(key1 = Unit){

    }
    val context=LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            ,verticalArrangement = Arrangement.Center
    ,horizontalAlignment = Alignment.CenterHorizontally

    ) {
    OutlinedTextField(value = name, onValueChange = { name = it },modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth()
    )
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = {
            val dbReference=FirebaseDatabase.getInstance().getReference("Tag")
            val tagId = dbReference.push().key.toString()
            val tag= Tag(tagId,name.text)
        if(!addTagViewModel.tagsList.contains(name.text))
        {
            dbReference.child(tagId).setValue(tag)
        }
            else{
            Toast.makeText(context,"This Tag already exists",Toast.LENGTH_SHORT).show()
        }

        },

            ) {
            Text(text = "Add Tag")
        }
}
}