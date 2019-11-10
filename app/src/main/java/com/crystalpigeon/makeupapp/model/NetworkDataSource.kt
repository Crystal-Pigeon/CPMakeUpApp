package com.crystalpigeon.makeupapp.model

import com.crystalpigeon.makeupapp.MakeUpApp
import io.reactivex.Observable
import javax.inject.Inject

class NetworkDataSource {

    @Inject
    lateinit var api: MakeUpService

    private var products: ArrayList<Product> = ArrayList()
    private var productsList: ArrayList<Product> = ArrayList()
    private var map: HashMap<String, ArrayList<Product>> = HashMap()

    init {
        MakeUpApp.app.component.inject(this)
    }


    fun getProducts(): Observable<ArrayList<Product>> {
        return api.getProducts()
            .flatMap { response ->
                when (response.code()) {
                    in 500..511 -> {
                        throw Exception("Server error")
                    }
                    200 -> {
                        response.body()?.let {
                            products.addAll(it)
                            return@flatMap Observable.just(products)
                        }
                    }
                    else -> {
                        throw Exception()
                    }
                }
            }
    }

    fun getProductsByType(type: String): Observable<HashMap<String,ArrayList<Product>>>{

        return api.getProductDetails(type)
            .flatMap { response ->
                when(response.code()) {
                    in 500..511 -> {
                        throw Exception("Server error")
                    }
                    200 -> {
                        response.body()?.let {
                            productsList.clear()
                            productsList.addAll(it)
                            map = hashMapOf(type to productsList)
                            return@flatMap Observable.just(map)
                        }
                    }
                    else -> {
                        throw Exception()
                    }
                }
            }
    }
}