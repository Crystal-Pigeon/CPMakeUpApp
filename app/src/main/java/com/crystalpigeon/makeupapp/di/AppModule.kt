package com.crystalpigeon.makeupapp.di

import android.app.Application
import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val application: Application) {
    @Provides @Singleton
    fun provideContext(): Context = application

    @Provides
    fun provideFirebase() : FirebaseAuth = FirebaseAuth.getInstance()
}