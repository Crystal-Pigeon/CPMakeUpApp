package com.crystalpigeon.makeupapp.di

import com.crystalpigeon.makeupapp.viewmodel.*
import dagger.Module
import dagger.Provides

@Module
class ViewModelModule {

    @Provides fun providesProductTypeViewModel(): ProductTypeViewModel = ProductTypeViewModel()
    @Provides fun providesLoginViewModel(): LoginViewModel = LoginViewModel()
    @Provides fun providesRegisterViewModel(): RegisterViewModel = RegisterViewModel()
    @Provides fun providesSplashViewModel(): SplashViewModel = SplashViewModel()
    @Provides fun providesListOfProductsViewModel(): ListOfProductsViewModel = ListOfProductsViewModel()
    @Provides fun providesMainViewModel(): MainViewModel = MainViewModel()
}