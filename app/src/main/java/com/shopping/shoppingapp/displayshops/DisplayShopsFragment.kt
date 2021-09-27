package com.shopping.shoppingapp.displayshops

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import com.shopping.shoppingapp.R
import com.shopping.shoppingapp.ui.theme.MyApplicationTheme

class DisplayShopsFragment : Fragment() {

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
@Composable
    private fun displayShops() {

    }


}