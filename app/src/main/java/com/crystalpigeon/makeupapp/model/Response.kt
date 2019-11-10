package com.crystalpigeon.makeupapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product(
    val id: Int,
    val brand: String?,
    var name: String,
    val price: String?,
    val price_sign: String?,
    val currency: String?,
    val image_link: String,
    val description: String,
    val rating: Float?,
    val category: String?,
    val product_type: String,
    val tag_list: List<String>?,
    val product_colors: List<ProductColor>?) : Parcelable

@Parcelize
data class ProductColor (
    val hex_value: String,
    val colour_name: String) : Parcelable