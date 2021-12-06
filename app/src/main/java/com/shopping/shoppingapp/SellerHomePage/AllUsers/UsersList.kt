import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.shopping.shoppingapp.DB.Shop
import com.shopping.shoppingapp.DB.User
import com.shopping.shoppingapp.DisplayShops.UsersPrivateChatViewModel
import com.shopping.shoppingapp.Screen
import com.shopping.shoppingapp.SellerHomePage.AllUsers.UserChatListViewModel

@Composable
fun UsersList(navController: NavController, userChatListViewModel: UserChatListViewModel = viewModel()) {
    var userStatus = remember { mutableStateOf("") }

    var shopnames = remember { mutableStateListOf<String>() }
    var shoplogos = remember { mutableStateListOf<String>() }
    var users = remember { mutableStateListOf<User>() }

    BackHandler() {
        navController.popBackStack()
    }
    LaunchedEffect(key1 = Unit){
        userChatListViewModel.getFromDb()

    }
    Log.d("mikasa","Sda")
    users.clear()
    users.addAll(userChatListViewModel.users)







    LazyColumn(
            modifier = Modifier.fillMaxSize()

    ) {


            itemsIndexed(users) { index, item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            all = 4.dp
                        )

                    ,


                ) {
                    Spacer(modifier = Modifier.height(8.dp))



                    var isClicked by remember { mutableStateOf(false) }

                        Spacer(modifier = Modifier.width(16.dp))
                        Column(
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = users[index].name,
                                color = Color.Black,
                                style = MaterialTheme.typography.h6,
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold
                            )
//            Log.d("tag",shop.toString())

                            if (isClicked) {
                                /*  navController.navigate(Screen.PrivateChats.withArgs(item.shop_id.toString())) {
                                popUpTo(Screen.DisplayShops.route) {
                                    inclusive = true
                                }
                            }*/
                                isClicked = false
                            }



                    }

                }
            }

        }


}

