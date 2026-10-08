# Rumah Kemas — Android

Aplikasi checklist rumah teres 1 tingkat (3 bilik, 2 bilik air, 2 anak kecil).

## Bina APK melalui GitHub
1. Create repository baru di GitHub (contoh `rumah-kemas-android`).
2. Upload **semua kandungan folder RumahKemas-Android** ke root repository (bukan folder induk sahaja).
3. Buka tab **Actions** > **Build Rumah Kemas APK** > **Run workflow**. Jika Actions jalan automatik selepas push, pilih run terkini.
4. Bila build berjaya, buka run > **Artifacts** > **RumahKemas-APK**. Download ZIP artifact dan extract `app-debug.apk`.
5. Pasang APK tersebut di telefon Android (benarkan install daripada sumber itu jika diminta).

## Bina dengan Android Studio
Buka folder projek, sync Gradle, pilih Build > Build Bundle(s)/APK(s) > Build APK(s).

## Features
- 8 tugasan harian pagi/malam, tugasan mingguan 7 hari, tips zon dan rumah wangi
- Offline: aset dimuatkan terus dari pakej aplikasi
- WebView localStorage untuk simpan checklist
- Eksport backup JSON melalui Android Save As
- Tiada permission Internet diminta

Nota: Ini projek Android untuk dibina menjadi APK. Fail `app-debug.apk` akan wujud selepas GitHub Actions / Android Studio berjaya menjalankan build.
