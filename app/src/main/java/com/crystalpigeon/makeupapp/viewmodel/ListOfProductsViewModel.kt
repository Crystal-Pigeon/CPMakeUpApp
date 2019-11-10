package com.crystalpigeon.makeupapp.viewmodel

import androidx.lifecycle.ViewModel
import com.crystalpigeon.makeupapp.MakeUpApp
import com.crystalpigeon.makeupapp.model.DataSource
import com.crystalpigeon.makeupapp.model.MakeUpService
import com.crystalpigeon.makeupapp.model.Product
import io.reactivex.Observable
import retrofit2.Retrofit
import javax.inject.Inject

class ListOfProductsViewModel : ViewModel() {

    @Inject
    lateinit var api: MakeUpService
    @Inject
    lateinit var retrofit: Retrofit
    @Inject
    lateinit var dataSource: DataSource
    private var productsList: ArrayList<Product> = ArrayList()

    init {
        MakeUpApp.app.component.inject(this)
    }

    fun getProductsByType(type: String): Observable<ArrayList<Product>> {
        return dataSource.getProductsByType(type)
            .flatMap {
                if (it[type] != null) {
                    productsList = it[type]!!
                }
                return@flatMap Observable.just(productsList)
            }
    }
}