package com.shopping.shoppingapp.Authentication

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.navArgument
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.shopping.shoppingapp.AdminHomePage.AdminHomePage
import com.shopping.shoppingapp.R
import com.shopping.shoppingapp.Screen
import com.shopping.shoppingapp.DB.Shop
import com.shopping.shoppingapp.DB.User
import com.shopping.shoppingapp.DisplayShops.DisplayShops
import com.shopping.shoppingapp.DisplayShops.PrivateChats
import com.shopping.shoppingapp.SellerHomePage.MyProducts.MyProducts
import com.shopping.shoppingapp.SellerHomePage.MyShop.MyShop
import com.shopping.shoppingapp.SellerHomePage.MyShop.MyShopViewModel
import com.shopping.shoppingapp.SellerHomePage.MyShop.ProductPhotos
import com.shopping.shoppingapp.SellerHomePage.SellerHomePage
import com.shopping.shoppingapp.ui.theme.MyApplicationTheme
import kotlinx.coroutines.*
import java.io.File
import java.util.*

class AuthFragment : Fragment() {
    private val auth= FirebaseAuth.getInstance()
    private var oldPath=""
    companion object {
         const val MY_PERMISSION_CODE = 124

    }


private  var picturePath: String=""
private lateinit var imageLocation: File
private lateinit var userType:String
    private lateinit var shop_id:String
    private lateinit var updateUser:User



@ExperimentalMaterialApi
override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return ComposeView(requireActivity()).apply {
            setContent {
                MyApplicationTheme {
                   Navigation()
               }
            }
        }
    }

    @ExperimentalMaterialApi
    @DelicateCoroutinesApi
    @ExperimentalCoilApi
    @Composable
    private fun Navigation() {
       var myShopViewModel:MyShopViewModel = viewModel()

        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = Screen.Login.route) {
            composable(
                route = Screen.Login.route
            ) {
                Login(navController)
            }
            composable(
                route = Screen.Register.route
            ) {
                Register(navController)
            }
            composable(
                route = Screen.CreateShop.route
            ) {
                CreateShop(navController)
            }
            composable(
                route = Screen.SellerHomePage.route
            ) {
                SellerHomePage(navController)
            }
            composable(
                route = Screen.MyShop.route
            ) {

                MyShop(navController)
            }
            composable(
                route = Screen.PrivateChats.route+"/{shopid}",
                arguments = listOf(
                    navArgument("shopid"){
                    }
                )
            ) {
                    entry->
                PrivateChats(navController = navController,shopid = entry.arguments?.getString("shopid")!!)

            }
            composable(
                route = Screen.ProductPhotos.route+"/{index}",
                arguments = listOf(
                    navArgument("index"){
                    }
                )
            ) {
                    entry->
                ProductPhotos(navController = navController,index = entry.arguments?.getString("index")!!)

            }
            composable(
                route = Screen.DisplayShops.route
            ) {

                DisplayShops(navController)
            }
            composable(
                route = Screen.MyProducts.route
            ) {

                MyProducts(navController)
            }
            composable(
                route = Screen.AdminHomePage.route
            ) {

                AdminHomePage(navController)
            }
        }
    }


    @DelicateCoroutinesApi
    @Composable
    fun Login(navController: NavController) {
        var email by remember { mutableStateOf(TextFieldValue("")) }
        var password by remember { mutableStateOf(TextFieldValue("")) }
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
            ,horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Welcome!")
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(value =email,
                onValueChange= {email=it},
                label ={Text("Plz enter your email")
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(value=password,
                onValueChange= {password=it},
                label ={Text("Enter your password")},
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                          handleLogin(email.text,password.text,navController)




             //     handleAuth(name.text,password.text,email.text)
             //       val intent= Intent(requireActivity(),AddShopActivity::class.java)
              //      startActivity(intent)
                },
                modifier = Modifier
                    .fillMaxWidth(),
                border = BorderStroke(width = 1.dp, brush = SolidColor(Color.Green)),
            ) {
                Text(text = "Login",  textAlign = TextAlign.Center,
                    color = Color.White,
                    fontWeight= FontWeight.Bold
                )
                }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {navController.navigate(Screen.Register.route)},
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Transparent,
                    contentColor = Color.Red,
                )

            ) {

                Text(text = "Register now!",  textAlign = TextAlign.Center,
                    color = Color.Black,
                    fontWeight= FontWeight.Bold,

                    textDecoration = TextDecoration.combine(
                        listOf(
                            TextDecoration.Underline,
                        )
                    )
                )

            }
        }
    }

    private fun getDbData(navController: NavController) {
        val lol =   FirebaseDatabase.getInstance().getReference("User").child(auth.currentUser!!.uid).get().addOnSuccessListener {
             shop_id=it.child("shop_id").value.toString()
            val type:String=it.child("type").value.toString()
            val name:String=it.child("name").value.toString()
            val email:String=it.child("email").value.toString()

            userType=type
            updateUser= User(name,email,type,auth.currentUser!!.uid,shop_id)
            Log.d("kuso","haha $shop_id $name")
            if(userType=="Seller"&&TextUtils.isEmpty(shop_id)) {
                navController.navigate(Screen.CreateShop.route)
            }
            else if(userType=="Buyer"){
                navController.navigate(Screen.DisplayShops.route)
            }
            else if(userType=="Seller"){
                navController.navigate(Screen.SellerHomePage.route)
            }
            else{
                navController.navigate(Screen.AdminHomePage.route)
            }
        }
        }

    private fun handleLogin(email: String, password: String, navController: NavController) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(requireActivity(), OnCompleteListener { task ->
            if(task.isSuccessful) {
                getDbData(navController)
            }else {

            }
        })
    }

    @Composable
    fun Register(navController: NavController) {
        var email by remember { mutableStateOf(TextFieldValue("")) }
        var name by remember { mutableStateOf(TextFieldValue("")) }
        var password by remember { mutableStateOf(TextFieldValue("")) }
        var confirmpassword by remember { mutableStateOf(TextFieldValue("")) }
        var expanded by remember { mutableStateOf(false) }
        var choice by remember{ mutableStateOf("Select User Type")}
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top


        ) {
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = {
                    Text("Plz enter your email")
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))


            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Enter your name", color = Color.DarkGray) },
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Enter your password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = confirmpassword,
                onValueChange = { confirmpassword = it },
                label = { Text("Confirm your password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            )
            Spacer(modifier = Modifier.height(16.dp))


            Button(onClick = { expanded = !expanded }


            ) {
                Text(choice)
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = null,
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .background(Color.White)

            ) {
                DropdownMenuItem(onClick = {choice="Buyer"
                    expanded = false},) {
                    Text("Buyer")
                }
                DropdownMenuItem(onClick = {choice="Seller"
                    expanded = false}) {
                    Text("Seller")
                }
                DropdownMenuItem(onClick = {choice="Admin"
                    expanded = false}) {
                    Text("Admin")
                }
        }
            Spacer(modifier = Modifier.height(16.dp))

            Button(
            onClick = {
                //       handleAuth(name.text,password.text,email.text)
                if (TextUtils.isEmpty(name.text) || TextUtils.isEmpty(password.text) || TextUtils.isEmpty(
                        confirmpassword.text
                    ) || TextUtils.isEmpty(email.text)
                ) {
                    Toast.makeText(requireActivity(), "Please fill all data", Toast.LENGTH_LONG)
                        .show()

                } else if (password.text != confirmpassword.text) {
                    Toast.makeText(
                        requireActivity(),
                        "Passwords aren't identical",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    handleAuth(name.text, password.text, email.text,choice,navController)




                }

            },
            modifier = Modifier
                .fillMaxWidth(),
            border = BorderStroke(width = 1.dp, brush = SolidColor(Color.Green)),
        ) {
            Text(
                text = "Register", textAlign = TextAlign.Center,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
    }



    @DelicateCoroutinesApi
    @ExperimentalCoilApi
    @Composable
    fun CreateShop(navController: NavController) {
        var pickedImage: MutableState<String?> =remember { mutableStateOf(null)}
        var description by remember { mutableStateOf(TextFieldValue("")) }
        var name by remember { mutableStateOf(TextFieldValue("")) }

        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,

            ) {
            Spacer(modifier = Modifier.padding(16.dp))
            Image(
                painter = pickedImage.value?.let {
                    rememberImagePainter(it)
                } ?: painterResource(id = R.drawable.ic_plus),
                "...",
                modifier = Modifier
                    .clickable(onClick = {
                        if (checkAndRequestPermissions()) {
                            val value = GlobalScope.async { // creates worker thread
                                val res = withContext(Dispatchers.Default) {
                                    onGalleryClick()
                                    while (picturePath == oldPath) {
                                        delay(1000)
                                    }
                                    pickedImage.value = picturePath
                                    oldPath = picturePath

                                }
                            }
                        }
                    })
                    .height(100.dp)
                    .width(100.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Enter your name", color = Color.DarkGray) },
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {addShopToDb(name.text,pickedImage.value,navController)}

                 ,  modifier = Modifier
                    .fillMaxWidth(),
                border = BorderStroke(width = 1.dp, brush = SolidColor(Color.Green)),
                ) {
                Text(text = "Create Shop")
            }

        }
    }

    private fun addShopToDb(name: String, pickedImage: String?, navController: NavController) {
        val lol =   FirebaseDatabase.getInstance().getReference("User").child(auth.currentUser!!.uid).get().addOnSuccessListener {
            shop_id=it.child("shop_id").value.toString()
            val type:String=it.child("type").value.toString()
            val name:String=it.child("name").value.toString()
            val email:String=it.child("email").value.toString()
            userType=type
            updateUser= User(name,email,type,auth.currentUser!!.uid,shop_id)
            val firebaseDatabase= FirebaseDatabase.getInstance()
            var  dbReference=firebaseDatabase.getReference("Shop")
            val id=auth.currentUser!!.uid
            val storeId = dbReference.push().key.toString()
            val shop= Shop(name, pickedImage!!,storeId,id)
            dbReference.child(storeId).setValue(shop)
              dbReference=firebaseDatabase.getReference("User")

            updateUser.shop_id=storeId
            dbReference.child(auth.currentUser!!.uid).setValue(updateUser)
            navController.navigate(Screen.SellerHomePage.route)

        }

    }



    private fun handleAuth(
        name: String,
        password: String,
        email: String,
        choice: String,
        navController: NavController
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity(), OnCompleteListener { task ->
                if(task.isSuccessful){

                    val firebaseDatabase= FirebaseDatabase.getInstance()
                    val  dbReference=firebaseDatabase.getReference("User")
                    val id=auth.currentUser!!.uid
                    userType=choice
                    val user= User(name,email,choice,id)
                    dbReference.child(id).setValue(user)
                    if(userType=="Seller"&&TextUtils.isEmpty(shop_id)) {
                        Log.d("kuso",userType)
                        navController.navigate(Screen.CreateShop.route)
                    }
                    else if(userType=="Buyer"){
                        navController.navigate(Screen.DisplayShops.route)
                    }
                    else if(userType=="Seller"){
                        navController.navigate(Screen.SellerHomePage.route)
                    }
                    else if(userType=="Admin"){
                        navController.navigate(Screen.AdminHomePage.route)
                    }
                }
                else{

                }

            })

    }

    private fun checkAndRequestPermissions(): Boolean {
        val permissionCAMERA =
            ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA)
        val storagePermission = ContextCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        val listPermissionsNeeded = ArrayList<String>()
        if (storagePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (permissionCAMERA != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA)
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                listPermissionsNeeded.toTypedArray<String>(),
                MY_PERMISSION_CODE
            )
            return false
        }
        return true
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val permReqLuncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                if (it) {
                    when (requestCode) {
                        MY_PERMISSION_CODE -> {
                            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                            onGalleryClick()
                            }
                        }
                    }
                }
            }
    }


    fun  onGalleryClick() {
        val intent =
            Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        resultLauncher.launch(intent)
    }

    var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {

                    val selectedImage = result.data!!.data

                uploadImageToFirebase(selectedImage!!)
            }
        }

    private fun uploadImageToFirebase(fileUri: Uri) {

        if (fileUri != null) {
            val fileName = UUID.randomUUID().toString() +".jpg"

            val database = FirebaseDatabase.getInstance()
            val refStorage = FirebaseStorage.getInstance().reference.child("images/$fileName")

            refStorage.putFile(fileUri)
                .addOnSuccessListener(
                    OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->
                        taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                            val imageUrl = it
                            picturePath=imageUrl.toString()
                        }
                    })

                ?.addOnFailureListener(OnFailureListener { e ->
                    print(e.message)
                })
        }
    }





}