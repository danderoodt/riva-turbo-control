# 🔊 RIVA Turbo X Volume Control

Eine moderne Android-App zur Steuerung des RIVA Turbo X Lautsprechers mit **erweiterten Lautstärke-Funktionen** (bis zu 120%!).

## 🎯 Warum diese App?

Die offizielle RIVA Turbo X Ground Control App wird nicht mehr unterstützt. Diese App ersetzt sie und stellt die wichtigste Funktion wieder her:

**🚀 Turbo-Modus mit 120% maximaler Lautstärke**

Anders als die Tasten am Lautsprecher selbst (max 100%), ermöglicht diese App die volle Lautstärke von 120%!

## ✨ Features

- ✅ **Turbo-Modus**: Lautstärke bis zu 120% (20% lauter als normal!)
- ✅ Bluetooth-Verbindung zum RIVA Turbo X
- ✅ Einfache Bedienung mit Schieberegler und +/- Tasten
- ✅ Visuelles Feedback über aktuelle Lautstärke
- ✅ Kompatibel mit Android 5.0+

## 📸 Screenshots

```
┌─────────────────────────┐
│  RIVA Turbo X Lautstärke │
├─────────────────────────┤
│                         │
│  [Mit Lautsprecher     │
│   verbinden]           │
│                         │
│  Turbo-Modus (120%)  ⚡ │
│  [━━━━━━━━○━━━]        │
│                         │
│  Lautstärke: 105/120   │
│  (105%)                │
│                         │
│  [  −  ] [  +  ]       │
│                         │
└─────────────────────────┘
```

## 🚀 Installation

### Voraussetzungen

- Android 5.0 (Lollipop) oder höher
- RIVA Turbo X Lautsprecher
- Android Studio (zum Kompilieren)

### Schritte

1. **Klonen Sie das Repository**:
   ```bash
   git clone https://github.com/IhrUsername/riva-turbo-volume-control.git
   cd riva-turbo-volume-control
   ```

2. **Öffnen Sie in Android Studio**:
   - File → Open → Wählen Sie den Projektordner

3. **App kompilieren und installieren**:
   - Verbinden Sie Ihr Android-Gerät per USB
   - Klicken Sie auf "Run" (▶️)
   - Oder erstellen Sie eine APK: Build → Build APK(s)

## 📱 Verwendung

1. **Bluetooth aktivieren** auf Ihrem Smartphone
2. **RIVA Turbo X pairen** in den Android-Bluetooth-Einstellungen
3. **App öffnen** und auf "Mit Lautsprecher verbinden" tippen
4. **Turbo-Modus aktivieren** für 120% Lautstärke
5. **Lautstärke einstellen** mit Regler oder +/- Tasten

## ⚠️ Wichtige Hinweise

### Lautstärke

- Der **Turbo-Modus (120%)** ist deutlich lauter als die normale Maximallautstärke
- Beginnen Sie immer mit niedriger Lautstärke und erhöhen Sie schrittweise
- Bei zu hoher Lautstärke kann es zu Verzerrungen kommen
- **Schützen Sie Ihr Gehör** - vermeiden Sie dauerhafte Nutzung bei Maximallautstärke

### Technisch

- Die Bluetooth-Befehle basieren auf Reverse Engineering der Original-App
- Falls die Befehle nicht funktionieren, können Sie sie in `MainActivity.java` anpassen
- Ein Bluetooth HCI Snoop Log kann helfen, die exakten Befehle zu ermitteln

## 🔧 Anpassen der Befehle

Falls die Lautstärke-Steuerung nicht funktioniert, können Sie die Bluetooth-Befehle anpassen:

1. Öffnen Sie `app/src/main/java/com/rivaturbox/volumecontrol/MainActivity.java`
2. Finden Sie die Methode `sendVolumeCommand()`
3. Passen Sie das Befehlsformat an:

```java
byte[] command = new byte[] {
    (byte) 0xAA,  // Header - anpassen
    (byte) 0x55,  // Sync - anpassen
    (byte) 0x03,  // Command - anpassen
    (byte) volume // Payload
};
```

## 🛠️ Entwicklung

### Projektstruktur

```
RivaTurboVolumeControl/
├── app/
│   ├── src/main/
│   │   ├── java/com/rivaturbox/volumecontrol/
│   │   │   └── MainActivity.java
│   │   ├── res/layout/
│   │   │   └── activity_main.xml
│   │   └── AndroidManifest.xml
│   └── build.gradle
├── build.gradle
└── settings.gradle
```

### Technologie-Stack

- **Sprache**: Java
- **Min SDK**: 21 (Android 5.0)
- **Target SDK**: 34 (Android 14)
- **UI**: Material Design Components
- **Bluetooth**: Classic Bluetooth (SPP)

## 🔍 Reverse Engineering Details

Aus der Original-APK gefundene Informationen:

- **Konstanten**:
  - `MAX_PROGRESS_NORMAL`: 100
  - `MAX_PROGRESS_IN_TURBO_MODE`: 120
  - `MAX_REMOTE_DEVICE_VOLUME`: Geräte-Maximum

- **Befehle** (gefunden in classes.dex):
  - `COMMAND_VOLUME_UP`
  - `COMMAND_VOLUME_DOWN`
  - `COMMAND_TURBO_ON`
  - `COMMAND_TURBO_OFF`
  - `COMMAND_SET_VOLUME_LEVEL`

- **Bluetooth**: Serial Port Profile (SPP)
  - UUID: `00001101-0000-1000-8000-00805F9B34FB`

## 🤝 Beitragen

Contributions sind willkommen! Besonders hilfreich wären:

1. **Exakte Bluetooth-Befehle**: Wenn Sie die Originalbefehle per Sniffing ermitteln
2. **Zusätzliche Features**: Bass Boost, Equalizer, TWS-Modus
3. **Bugfixes und Verbesserungen**
4. **Tests auf verschiedenen Android-Versionen**

## 📄 Lizenz

Dieses Projekt ist für persönliche und Bildungszwecke erstellt.

**Disclaimer**: Dies ist ein inoffizielles Projekt und steht in keiner Verbindung zu RIVA Audio oder Audio Design Experts Inc.

## 🙏 Danksagungen

- Original RIVA Turbo X Ground Control App für die Inspiration
- Android Bluetooth SPP Dokumentation
- Alle, die bei der Ermittlung der Bluetooth-Befehle geholfen haben

## 📞 Support & Probleme

Falls Sie Probleme haben:

1. Lesen Sie die [ANLEITUNG.md](ANLEITUNG.md)
2. Öffnen Sie ein Issue auf GitHub
3. Teilen Sie Bluetooth HCI Logs (falls verfügbar)

---

**Genießen Sie die volle Lautstärke Ihres RIVA Turbo X! 🎵🔊**
