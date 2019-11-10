package com.crystalpigeon.makeupapp.di

import com.crystalpigeon.makeupapp.model.DataSource
import com.crystalpigeon.makeupapp.model.DiskDataSource
import com.crystalpigeon.makeupapp.model.NetworkDataSource
import com.crystalpigeon.makeupapp.view.MainActivity
import com.crystalpigeon.makeupapp.view.fragments.*
import com.crystalpigeon.makeupapp.viewmodel.*
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, ViewModelModule::class, AppModule::class])
interface AppComponent {

    fun inject(viewModel: ProductTypeViewModel)
    fun inject(fragment: ProductTypeFragment)
    fun inject(fragment: LoginFragment)
    fun inject(fragment: RegisterFragment)
    fun inject(viewModel: LoginViewModel)
    fun inject(viewModel: RegisterViewModel)
    fun inject(viewModel: SplashViewModel)
    fun inject(fragment: SplashFragment)
    fun inject(viewModel: ListOfProductsViewModel)
    fun inject(fragment: ListOfProductsFragment)
    fun inject(fragment: ProductDetailsFragment)
    fun inject(unit: NetworkDataSource)
    fun inject(unit: DataSource)
    fun inject(unit: DiskDataSource)
    fun inject(viewModel: MainViewModel)
    fun inject(activity: MainActivity)
    fun inject(fragment: CartScreenFragment)
}
