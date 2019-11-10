package com.crystalpigeon.makeupapp.model

import android.content.Context
import io.reactivex.Observable
import com.google.gson.Gson
import com.crystalpigeon.makeupapp.MakeUpApp
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class DiskDataSource {
    @Inject
    lateinit var context: Context

    init {
        MakeUpApp.app.component.inject(this)
    }

    private var products: ArrayList<Product> = ArrayList()
    private var map: HashMap<String, ArrayList<Product>> = HashMap()


    fun getProducts(): Observable<ArrayList<Product>> {
        val serializedObjects: String =
            context.getSharedPreferences("DiskDataSource", Context.MODE_PRIVATE)
                .getString("product", "")
        val gson = Gson()
        val type = object : TypeToken<ArrayList<Product>>() {}.type
        if (serializedObjects != "") {
            products = gson.fromJson(serializedObjects, type)
        }

        return Observable.create<ArrayList<Product>> { emitter ->
            if (products.isNotEmpty()) {
                emitter.onNext(products)
            }
            emitter.onComplete()
        }
    }

    fun saveProductsToDisk(products: ArrayList<Product>) {
        val editor = context.getSharedPreferences("DiskDataSource", Context.MODE_PRIVATE).edit()
        val gson = Gson()
        val json = gson.toJson(products)
        editor.putString("product", json)
        editor.apply()

    }

    fun getProductsByType(productType : String): Observable<HashMap<String, ArrayList<Product>>> {
        val serializedObject: String =
            context.getSharedPreferences("DiskDataSource", Context.MODE_PRIVATE)
                .getString("productsByType", "")
        val gson = Gson()
        val type = object : TypeToken<HashMap<String, ArrayList<Product>>>() {}.type
        if (serializedObject != "") {
            map = gson.fromJson(serializedObject,type)
        }
        return Observable.create<HashMap<String, ArrayList<Product>>> { emitter ->
            if (map[productType]!=null) {
                emitter.onNext(map)
            }
            emitter.onComplete()
        }
    }

    fun saveProductsByTypeToDisk(map: HashMap<String, ArrayList<Product>>) {
        val editor = context.getSharedPreferences("DiskDataSource", Context.MODE_PRIVATE).edit()
        val gson = Gson()
        val json = gson.toJson(map)
        editor.putString("productsByType", json)
        editor.apply()
    }
}