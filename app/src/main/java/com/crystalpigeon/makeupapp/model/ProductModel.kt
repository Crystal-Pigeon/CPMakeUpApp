package com.crystalpigeon.makeupapp.model

class ProductModel {
    var id : String = ""
    var productName: String = ""
    var productBrand: String = ""
    var productPrice: Float = 0F
    var color: String ?= ""
    var productImg: String = ""

    constructor()

    constructor(id: String, productName: String, productBrand: String, productPrice: Float, color: String?, productImg: String){
        this.id = id
        this.productName = productName
        this.productBrand = productBrand
        this.productPrice = productPrice
        this.productImg = productImg
        this.color = color
    }
}

