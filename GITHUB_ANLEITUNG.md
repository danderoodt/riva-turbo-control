# 📖 APK mit GitHub automatisch erstellen - Schritt für Schritt

## ✅ Was Sie brauchen:
- Einen kostenlosen GitHub Account (falls noch nicht vorhanden)
- Das ZIP-File `RivaTurboVolumeControl.zip` (bereits heruntergeladen)

---

## 🚀 Anleitung (ca. 10 Minuten)

### Schritt 1: GitHub Account erstellen (falls nötig)

1. Gehen Sie zu: https://github.com/signup
2. Erstellen Sie einen kostenlosen Account
3. Bestätigen Sie Ihre E-Mail-Adresse

### Schritt 2: Neues Repository erstellen

1. Klicken Sie auf das **+** Symbol oben rechts
2. Wählen Sie **"New repository"**
3. Geben Sie einen Namen ein: `riva-turbo-control`
4. Wählen Sie **Public** (oder Private, beide funktionieren)
5. ✅ **WICHTIG**: Aktivieren Sie **"Add a README file"**
6. Klicken Sie auf **"Create repository"**

### Schritt 3: Projekt hochladen

**Option A: Via Web-Interface (einfacher)**

1. In Ihrem neuen Repository, klicken Sie auf **"Add file"** → **"Upload files"**
2. **Entpacken** Sie zuerst `RivaTurboVolumeControl.zip` auf Ihrem Computer
3. Ziehen Sie **ALLE Dateien und Ordner** aus dem entpackten Ordner in das Upload-Fenster
   - ⚠️ Wichtig: Nicht den Ordner selbst, sondern den INHALT hochladen!
   - Sie sollten sehen: `app/`, `.github/`, `build.gradle`, `settings.gradle`, `gradlew`, etc.
4. Scrollen Sie nach unten und klicken Sie auf **"Commit changes"**

**Option B: Mit Git (für Fortgeschrittene)**

```bash
# Entpacken Sie das ZIP
unzip RivaTurboVolumeControl.zip
cd RivaTurboVolumeControl

# Git initialisieren
git init
git add .
git commit -m "Initial commit"

# Mit GitHub verbinden (ersetzen Sie USERNAME)
git remote add origin https://github.com/USERNAME/riva-turbo-control.git
git branch -M main
git push -u origin main
```

### Schritt 4: APK Build starten

Die APK wird **automatisch** gebaut, sobald Sie die Dateien hochgeladen haben!

1. Gehen Sie im Repository zum Tab **"Actions"**
2. Sie sollten einen laufenden Build sehen: **"Android APK Build"**
3. Warten Sie 2-5 Minuten bis der Build fertig ist (grüner Haken ✅)

### Schritt 5: APK herunterladen

1. Klicken Sie auf den **fertigen Build** (grüner Haken)
2. Scrollen Sie nach unten zu **"Artifacts"**
3. Laden Sie herunter: **`riva-turbo-control-debug`**
4. Entpacken Sie die heruntergeladene ZIP-Datei
5. Sie finden die APK: **`app-debug.apk`**

### Schritt 6: APK auf Ihr Android-Telefon übertragen

1. Verbinden Sie Ihr Telefon per USB mit dem Computer, ODER
2. Senden Sie die APK per E-Mail/WhatsApp an sich selbst, ODER
3. Laden Sie die APK auf Google Drive/Dropbox hoch

### Schritt 7: APK installieren

1. Öffnen Sie die APK-Datei auf Ihrem Android-Telefon
2. Falls gefragt: Erlauben Sie **"Installation aus unbekannten Quellen"**
3. Tippen Sie auf **"Installieren"**
4. Fertig! 🎉

---

## 🔄 APK neu bauen (nach Änderungen)

Wenn Sie den Code anpassen möchten:

1. Bearbeiten Sie die Dateien auf GitHub direkt (z.B. `MainActivity.java`)
2. Klicken Sie auf **"Commit changes"**
3. GitHub baut automatisch eine neue APK!
4. Laden Sie die neue APK unter **"Actions"** herunter

---

## 🐛 Fehlersuche

### Build schlägt fehl?

1. Gehen Sie zu **"Actions"** → Klicken Sie auf den fehlgeschlagenen Build
2. Klicken Sie auf **"build"** um die Logs zu sehen
3. Suchen Sie nach Fehlermeldungen (meist ganz unten)

### Häufige Probleme:

**Problem**: "gradlew: Permission denied"
- **Lösung**: Die gradlew-Datei wurde richtig hochgeladen, Build sollte funktionieren

**Problem**: "Could not find gradle wrapper"
- **Lösung**: Stellen Sie sicher, dass der `gradle/` Ordner hochgeladen wurde

**Problem**: Build dauert sehr lange
- **Normal**: Erster Build kann 5-10 Minuten dauern

---

## 💡 Vorteile dieser Methode

✅ **Kostenlos** - GitHub Actions ist gratis  
✅ **Automatisch** - Build startet bei jeder Änderung  
✅ **Kein Android Studio** nötig auf Ihrem Computer  
✅ **Immer verfügbar** - APK wird als Artifact gespeichert  

---

## 📌 Zusammenfassung

```
1. GitHub Account erstellen
2. Repository erstellen
3. Projekt hochladen
4. 2-5 Minuten warten
5. APK unter "Actions" → "Artifacts" herunterladen
6. Auf Telefon installieren
7. Fertig! 🔊
```

---

## 🆘 Brauchen Sie Hilfe?

Falls es nicht klappt, können Sie:
1. Ein "Issue" auf GitHub öffnen
2. Die Build-Logs kopieren und mir zeigen
3. Screenshots vom Fehler machen

**Viel Erfolg!** 🚀
