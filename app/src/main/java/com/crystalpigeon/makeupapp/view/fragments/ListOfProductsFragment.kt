package com.crystalpigeon.makeupapp.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.crystalpigeon.makeupapp.MakeUpApp
import com.crystalpigeon.makeupapp.R
import com.crystalpigeon.makeupapp.hideLoader
import com.crystalpigeon.makeupapp.showLoader
import com.crystalpigeon.makeupapp.view.MainActivity
import com.crystalpigeon.makeupapp.view.adapters.ListOfProductsAdapter
import com.crystalpigeon.makeupapp.viewmodel.ListOfProductsViewModel
import com.google.android.material.snackbar.Snackbar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_list_of_products.*
import kotlinx.android.synthetic.main.fragment_list_of_products.errorLayout
import java.net.UnknownHostException
import javax.inject.Inject


class ListOfProductsFragment : Fragment() {

    private lateinit var navController: NavController
    @Inject
    lateinit var listOfProductsViewModel: ListOfProductsViewModel
    private val compositeDisposable = CompositeDisposable()
    private var productTypeDetailsAdapter: ListOfProductsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list_of_products, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MakeUpApp.app.component.inject(this)
        navController =
            Navigation.findNavController(activity as MainActivity, R.id.nav_host_fragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).showToolbar()
        (activity as MainActivity).setActionBarTitle(R.string.list_of_products)

        val type = arguments?.getString("type")

        getListOfProductsApi(view, type)
    }

    private fun getListOfProductsApi(view: View, type: String?) {
        context?.showLoader()
        errorLayout.visibility = View.GONE
        compositeDisposable.add(listOfProductsViewModel.getProductsByType(type!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    context?.hideLoader()
                    errorLayout.visibility = View.GONE
                    rvProductTypeDetails.visibility = View.VISIBLE

                    rvProductTypeDetails.layoutManager = GridLayoutManager(context, 2)
                    productTypeDetailsAdapter = ListOfProductsAdapter(it, context!!, navController)
                    rvProductTypeDetails.adapter = productTypeDetailsAdapter
                },
                onError = {
                    context?.hideLoader()
                    if (it is UnknownHostException)
                        Snackbar.make(view, R.string.no_internet_connection, Snackbar.LENGTH_SHORT).show()
                    else
                        Snackbar.make(view, it.message.toString(), Snackbar.LENGTH_SHORT).show()

                    errorLayout.visibility = View.VISIBLE
                    rvProductTypeDetails.visibility = View.GONE
                    tryAgain.setOnClickListener { getListOfProductsApi(view, type) }
                }
            )
        )
    }

    override fun onPause() {
        super.onPause()
        compositeDisposable.clear()
    }
}
