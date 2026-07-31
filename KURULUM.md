# LiveVibe — Kurulum Talimatları (Faz 1)

Bu paket, **Google Sign-In + temel profil** akışının çalışan kaynak kodunu içerir.
Android Studio'da açtıktan sonra çalıştırabilmen için şu adımları sırayla yapman gerekiyor.

## 1. Firebase Projesi Oluştur
1. https://console.firebase.google.com adresinden yeni proje oluştur (örn. "LiveVibe").
2. Android uygulaması ekle: **package name olarak `com.livevibe.app` gir** (build.gradle.kts'teki applicationId ile birebir aynı olmalı).
3. İndirilen `google-services.json` dosyasını `app/` klasörünün içine koy (bu repo'da örnek/placeholder yok, kendi dosyanı eklemen lazım).

## 2. Google Sign-In için Web Client ID al
1. Firebase Console > Authentication > Sign-in method > Google'ı etkinleştir.
2. Aynı ekranda "Web SDK configuration" altında bir **Web client ID** göreceksin (uzun, `....apps.googleusercontent.com` ile biten bir değer).
3. `app/src/main/res/values/strings.xml` içindeki `default_web_client_id` değerini bununla değiştir.

## 3. SHA-1 / SHA-256 sertifika parmak izini ekle
Google Sign-In'in çalışması için debug ve (sonrasında) release keystore'unun SHA-1'ini Firebase Console > Proje Ayarları > Uygulamalarım kısmına eklemen gerekiyor:
```
./gradlew signingReport
```
komutuyla debug SHA-1'ini alabilirsin.

## 4. Firestore ve Security Rules
1. Firebase Console > Firestore Database > "Veritabanı oluştur" (production mode).
2. Bu repodaki `firestore.rules` dosyasının içeriğini Firebase Console > Firestore > Rules sekmesine yapıştır ve yayınla.
   **Bu adımı atlama** — bu kural, coin bakiyesinin client tarafından değiştirilmesini engelleyen güvenlik katmanı.

## 5. Projeyi aç ve çalıştır
1. Android Studio (en güncel sürüm) ile `LiveVibe/` klasörünü aç.
2. Gradle sync'in bitmesini bekle.
3. Bir emülatör veya fiziksel cihazda çalıştır.

## Sırada ne var?
Bu paket sadece Faz 1'in temelini (auth + profil) içeriyor. Sıradaki adımlar:
- 1-1 mesajlaşma ekranı + ChatRepository
- Cloud Functions kurulumu (coin satın alma doğrulaması, hediye gönderme)
- Google Play Billing entegrasyonu
- Sesli oda (Agora/ZegoCloud SDK entegrasyonu)

Her birini ayrı ayrı, aynı şekilde kod olarak yazmaya devam edebiliriz.
