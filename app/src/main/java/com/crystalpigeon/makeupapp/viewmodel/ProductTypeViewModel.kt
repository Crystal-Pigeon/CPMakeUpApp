package com.crystalpigeon.makeupapp.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import com.crystalpigeon.makeupapp.MakeUpApp
import com.crystalpigeon.makeupapp.model.*
import io.reactivex.Observable
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Retrofit
import javax.inject.Inject

class ProductTypeViewModel : ViewModel() {

    @Inject
    lateinit var api: MakeUpService
    @Inject
    lateinit var retrofit: Retrofit
    @Inject
    lateinit var mFirebaseAuth: FirebaseAuth
    @Inject
    lateinit var dataSource: DataSource
    private var productsTypes: HashSet<String> = HashSet()

    init {
        MakeUpApp.app.component.inject(this)
    }

    @SuppressLint("CheckResult")
    fun getProductTypes(): Observable<HashSet<String>> {
        return dataSource.getData()
            .flatMap {
                it.forEach { product ->
                    val type: String = if (product.product_type.contains("_")) {
                        product.product_type.replace("_", " ")
                    } else product.product_type
                    productsTypes.add(type)
                }
                return@flatMap Observable.just(productsTypes)
            }
    }
}