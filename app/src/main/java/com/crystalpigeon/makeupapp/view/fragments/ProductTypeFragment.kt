package com.crystalpigeon.makeupapp.view.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import com.crystalpigeon.makeupapp.view.adapters.ProductTypeAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_product_type.*
import com.crystalpigeon.makeupapp.view.MainActivity
import com.crystalpigeon.makeupapp.viewmodel.ProductTypeViewModel
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject
import kotlin.system.exitProcess
import com.crystalpigeon.makeupapp.*
import java.net.UnknownHostException


class ProductTypeFragment : Fragment() {

    private lateinit var navController: NavController
    @Inject
    lateinit var productTypeViewModel: ProductTypeViewModel

    private var productTypeAdapter: ProductTypeAdapter? = null
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MakeUpApp.app.component.inject(this)
        val callback = object : OnBackPressedCallback(true /* enabled by default */) {
            override fun handleOnBackPressed() {
                AlertDialog.Builder(activity as MainActivity, R.style.AlertDialogStyle)
                    .setMessage(getString(R.string.exit_app))
                    .setPositiveButton(getString(R.string.yes)) { _, _ ->
                        activity?.moveTaskToBack(true)
                        android.os.Process.killProcess(android.os.Process.myPid())
                        exitProcess(1)
                    }
                    .setNegativeButton(getString(R.string.no)) { d, _ -> d.cancel() }
                    .show()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_product_type, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).showToolbar()
        (activity as MainActivity).setActionBarTitle(R.string.products_types)

        navController =
            Navigation.findNavController(activity as MainActivity, R.id.nav_host_fragment)

        getProductsApi(view)
    }

    private fun getProductsApi(view: View) {
        context?.showLoader()
        errorLayout.visibility = View.GONE
        compositeDisposable.add(productTypeViewModel.getProductTypes()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    context?.hideLoader()
                    errorLayout.visibility = View.GONE
                    flProductsTypes.visibility = View.VISIBLE

                    rvProductType.layoutManager = GridLayoutManager(context, 2)
                    productTypeAdapter = ProductTypeAdapter(it, context!!, navController)
                    rvProductType.adapter = productTypeAdapter
                },
                onError = {
                    context?.hideLoader()
                    if (it is UnknownHostException){
                        Snackbar.make(view, R.string.no_internet_connection, Snackbar.LENGTH_SHORT).show()
                    } else {
                        Snackbar.make(view, it.message.toString(), Snackbar.LENGTH_SHORT).show()
                    }

                    flProductsTypes.visibility = View.GONE
                    errorLayout.visibility = View.VISIBLE
                    btnTryAgain.setOnClickListener{ getProductsApi(view) }
                }
            )
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}
