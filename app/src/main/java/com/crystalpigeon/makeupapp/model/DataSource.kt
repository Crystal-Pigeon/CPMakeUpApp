package com.crystalpigeon.makeupapp.model

import com.crystalpigeon.makeupapp.MakeUpApp
import io.reactivex.Observable
import javax.inject.Inject

class DataSource {

    @Inject
    lateinit var memoryDataSource: MemoryDataSource
    @Inject
    lateinit var diskDataSource: DiskDataSource
    @Inject
    lateinit var networkDataSource: NetworkDataSource

    init {
        MakeUpApp.app.component.inject(this)
    }

    private fun getDataFromMemory(): Observable<ArrayList<Product>> {
        return memoryDataSource.getProducts()
    }

    private fun getDataFromDisk(): Observable<ArrayList<Product>> {
        return diskDataSource.getProducts().doOnNext { products ->
            memoryDataSource.cacheProductsInMemory(products)
        }
    }

    private fun getDataFromNetwork(): Observable<ArrayList<Product>> {
        return networkDataSource.getProducts().doOnNext { products ->
            diskDataSource.saveProductsToDisk(products)
            memoryDataSource.cacheProductsInMemory(products)
        }
    }

    private fun getProductsByTypeFromMemory(type:String): Observable<HashMap<String, ArrayList<Product>>> {
        return memoryDataSource.getProductsByType(type)
    }

    private fun getProductsByTypeFromDisk(type:String): Observable<HashMap<String, ArrayList<Product>>> {
        return diskDataSource.getProductsByType(type).doOnNext { products ->
            memoryDataSource.cacheProductsByType(products)
        }
    }

    private fun getProductsByTypeFromNetwork(type: String): Observable<HashMap<String, ArrayList<Product>>> {
        return networkDataSource.getProductsByType(type).doOnNext { products ->
            diskDataSource.saveProductsByTypeToDisk(products)
            memoryDataSource.cacheProductsByType(products)
        }
    }

    fun getData(): Observable<ArrayList<Product>> {
        return Observable.concat(getDataFromMemory(), getDataFromDisk(), getDataFromNetwork())
            .firstElement()
            .toObservable()
    }

    fun getProductsByType(type: String): Observable<HashMap<String, ArrayList<Product>>> {
        return Observable.concat(
            getProductsByTypeFromMemory(type),
            getProductsByTypeFromDisk(type),
            getProductsByTypeFromNetwork(type)
        )
            .firstElement()
            .toObservable()
    }


}