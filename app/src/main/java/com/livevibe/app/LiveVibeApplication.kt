package com.livevibe.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Uygulamanın giriş noktası. Hilt burada devreye girer,
 * tüm @Inject constructor'lar ve @Module'ler bu sınıf üzerinden bağlanır.
 */
@HiltAndroidApp
class LiveVibeApplication : Application()
