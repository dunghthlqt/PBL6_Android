package com.demo.pbl6_android.utils

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.demo.pbl6_android.R

fun Fragment.navigateToShop(shopName: String) {
    val bundle = Bundle().apply {
        putString("shopName", shopName)
    }
    try {
        findNavController().navigate(R.id.shopFragment, bundle)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Fragment.navigateToChatWithShop(shopName: String, shopId: String) {
    val bundle = Bundle().apply {
        putString("conversationId", "shop_$shopId")
        putString("shopName", shopName)
    }
    try {
        findNavController().navigate(R.id.chatFragment, bundle)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

