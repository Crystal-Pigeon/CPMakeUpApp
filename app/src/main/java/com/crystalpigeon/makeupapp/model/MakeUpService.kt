package com.crystalpigeon.makeupapp.model

import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MakeUpService {

    @GET("v1/products.json/")
    fun getProducts():Observable<Response<List<Product>>>

    @GET("v1/products.json")
    fun getProductDetails(@Query("product_type") type: String):Observable<Response<ArrayList<Product>>>
}