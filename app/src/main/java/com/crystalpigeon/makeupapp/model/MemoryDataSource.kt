package com.crystalpigeon.makeupapp.model

import io.reactivex.Observable

class MemoryDataSource {

    private var products: ArrayList<Product> = ArrayList()
    private var map: HashMap<String, ArrayList<Product>> = HashMap()

    fun getProducts(): Observable<ArrayList<Product>> {
        return Observable.create<ArrayList<Product>> { emitter ->
            if (products.isNotEmpty()) {
                emitter.onNext(products)
            }
            emitter.onComplete()
        }
    }

    fun cacheProductsInMemory(products: ArrayList<Product>) {
        this.products = products
    }

    fun getProductsByType(productType: String): Observable<HashMap<String, ArrayList<Product>>> {
        return Observable.create<HashMap<String, ArrayList<Product>>> { emitter ->
            if (map[productType] != null) {
                emitter.onNext(map)
            }
            emitter.onComplete()
        }
    }

    fun cacheProductsByType(map: HashMap<String, ArrayList<Product>>) {
        this.map = map
    }
}