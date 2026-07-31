package com.livevibe.app.di

// AuthRepository zaten @Inject constructor + @Singleton kullandığı için
// Hilt otomatik olarak nasıl oluşturacağını biliyor - burada ekstra bir
// @Provides tanımlamaya gerek yok. Bu dosya, ileride interface/implementation
// ayrımı yapmak istediğimizde (örn. AuthRepository bir interface olduğunda)
// @Binds tanımları için ayrılmış bir yer tutucu.
