package com.livevibe.app.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Firebase servislerinin tek (singleton) instance olarak uygulama genelinde
 * paylaşılmasını sağlıyoruz. Repository'ler bu servisleri constructor injection
 * ile alacak (bkz. AuthRepository).
 */
@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideStorage(): FirebaseStorage = FirebaseStorage.getInstance()

    @Provides
    @Singleton
    fun provideFunctions(): FirebaseFunctions = FirebaseFunctions.getInstance("europe-west1")
    // NOT: Cloud Functions'ı deploy ederken de aynı region'ı (europe-west1 -> Belçika,
    // Türkiye'ye en yakın bölgelerden biri) kullanmalısın, aksi halde bu satır hata verir.
}
