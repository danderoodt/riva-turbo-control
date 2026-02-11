# RIVA Turbo X Lautstärke-App - Anleitung

## 📱 Was macht diese App?

Diese App ersetzt die nicht mehr unterstützte RIVA Ground Control App und ermöglicht Ihnen:

✅ **Turbo-Modus**: Lautstärke bis zu **120%** (statt nur 100% am Lautsprecher)
✅ Einfache Bluetooth-Verbindung
✅ Intuitive Lautstärke-Steuerung
✅ Kompatibel mit Android 5.0 und höher

---

## 🔧 Installation

### Methode 1: APK direkt installieren (Empfohlen)

1. Kopieren Sie das gesamte Projekt-Verzeichnis `RivaTurboVolumeControl` auf Ihren Computer
2. Öffnen Sie das Projekt in **Android Studio**
3. Schließen Sie Ihr Android-Telefon per USB an (mit aktiviertem USB-Debugging)
4. Klicken Sie auf "Run" (▶️) in Android Studio
5. Die App wird auf Ihrem Telefon installiert

### Methode 2: APK-Datei erstellen

1. Öffnen Sie das Projekt in Android Studio
2. Menü: `Build` → `Build Bundle(s) / APK(s)` → `Build APK(s)`
3. Warten Sie auf die Fertigstellung
4. Die APK finden Sie unter: `app/build/outputs/apk/debug/app-debug.apk`
5. Übertragen Sie die APK auf Ihr Telefon
6. Installieren Sie die APK (evtl. müssen Sie "Installation aus unbekannten Quellen" erlauben)

---

## 📖 Verwendung

### Erste Schritte

1. **Bluetooth aktivieren**: Stellen Sie sicher, dass Bluetooth auf Ihrem Telefon eingeschaltet ist
2. **Lautsprecher pairen**: Pairen Sie Ihren RIVA Turbo X zuerst normal über die Android-Bluetooth-Einstellungen
3. **App öffnen**: Starten Sie die RIVA Turbo Control App
4. **Verbinden**: Tippen Sie auf "Mit Lautsprecher verbinden"

### Turbo-Modus aktivieren

1. Schalten Sie den **Turbo-Modus** Schalter auf "AN"
2. Der maximale Lautstärke-Bereich erhöht sich von 100% auf **120%**
3. Sie können nun die Lautstärke über den normalen Maximalwert hinaus erhöhen!

### Lautstärke einstellen

- **Schieberegler**: Ziehen Sie den Regler für präzise Einstellung
- **+ / - Tasten**: Für schnelle Anpassungen in kleinen Schritten

---

## ⚙️ Technische Details

### Wie funktioniert das?

Die Original-App von RIVA nutzte Bluetooth-Befehle, um die Lautstärke zu steuern:

- **Normal-Modus**: Lautstärke 0-100 (wie am Lautsprecher selbst)
- **Turbo-Modus**: Lautstärke 0-120 (nur über die App möglich!)

Die neue App sendet folgende Bluetooth-Befehle an den Lautsprecher:

```
COMMAND_VOLUME_UP    → Lautstärke erhöhen
COMMAND_VOLUME_DOWN  → Lautstärke verringern
COMMAND_TURBO_ON     → Turbo-Modus aktivieren
COMMAND_TURBO_OFF    → Turbo-Modus deaktivieren
```

### Bluetooth-Protokoll

Die App verwendet das **Serial Port Profile (SPP)** über Bluetooth:
- UUID: `00001101-0000-1000-8000-00805F9B34FB`
- Befehlsformat: `[Header][Sync][Command][Payload]`

---

## ⚠️ Wichtige Hinweise

### Lautstärke und Lautsprecherschutz

⚠️ **VORSICHT**: Der Turbo-Modus (120%) ist lauter als die normale Maximallautstärke!

- Beginnen Sie immer mit **niedrigeren Lautstärken**
- Erhöhen Sie die Lautstärke **schrittweise**
- Achten Sie auf Verzerrungen bei sehr hoher Lautstärke
- Schützen Sie Ihr Gehör - vermeiden Sie dauerhafte Nutzung bei maximaler Lautstärke

### Bekannte Einschränkungen

1. **Bluetooth-Verbindung erforderlich**: Die App funktioniert nur, wenn der Lautsprecher über Bluetooth verbunden ist
2. **Befehlsformat**: Die Befehle basieren auf Reverse Engineering - es kann sein, dass nicht alle Funktionen sofort perfekt funktionieren
3. **Erste Tests**: Bitte testen Sie die App zuerst bei niedriger Lautstärke

---

## 🔄 Alternative Lösungen

Falls die Bluetooth-Befehle nicht exakt passen, gibt es folgende Alternativen:

### Option 1: Befehlsformat anpassen

Die Befehlsformate in `MainActivity.java` können angepasst werden:

```java
// Aktuelles Format
byte[] command = new byte[] {
    (byte) 0xAA,  // Header
    (byte) 0x55,  // Sync
    (byte) 0x03,  // Command
    (byte) volume // Payload
};
```

Sie können diese Werte experimentell anpassen.

### Option 2: Bluetooth-Sniffer verwenden

Mit Apps wie **Bluetooth HCI Snoop Log** können Sie die Original-Befehle der alten App aufzeichnen:

1. Aktivieren Sie HCI Snoop Log in den Entwickleroptionen
2. Verwenden Sie die alte App
3. Analysieren Sie die aufgezeichneten Bluetooth-Pakete mit Wireshark
4. Passen Sie die Befehle in der neuen App entsprechend an

---

## 📂 Projektstruktur

```
RivaTurboVolumeControl/
├── app/
│   ├── src/main/
│   │   ├── java/com/rivaturbox/volumecontrol/
│   │   │   └── MainActivity.java      # Haupt-Code
│   │   ├── res/layout/
│   │   │   └── activity_main.xml      # UI-Layout
│   │   └── AndroidManifest.xml        # App-Konfiguration
│   └── build.gradle                   # Build-Konfiguration
├── build.gradle                       # Root-Build-Datei
└── settings.gradle                    # Projekt-Einstellungen
```

---

## 🛠️ Fehlersuche

### App verbindet sich nicht

- ✅ Ist Bluetooth eingeschaltet?
- ✅ Ist der Lautsprecher in den Android-Bluetooth-Einstellungen gepairt?
- ✅ Ist der Lautsprecher eingeschaltet und im Kopplungsmodus?
- ✅ Sind die Bluetooth-Berechtigungen in den App-Einstellungen erlaubt?

### Lautstärke ändert sich nicht

- Die Bluetooth-Befehle müssen eventuell angepasst werden
- Versuchen Sie, die alte RIVA App zu verwenden und die Befehle mit einem Bluetooth-Sniffer aufzuzeichnen
- Passen Sie die Befehlsformate in `MainActivity.java` entsprechend an

### Turbo-Modus funktioniert nicht

- Der Turbo-Modus könnte ein Hardware-Feature sein, das zusätzliche Befehle erfordert
- Überprüfen Sie, ob der Lautsprecher den Turbo-Modus unterstützt
- Die Lautstärke-Werte können trotzdem manuell auf 120% gesetzt werden

---

## 💡 Verbesserungsvorschläge

Mögliche Erweiterungen für die App:

1. **Bass Boost Steuerung**: Die Original-App hatte Bass-Boost-Funktionen
2. **Equalizer**: Frequenzband-Einstellungen
3. **TWS-Modus**: True Wireless Stereo mit zwei Lautsprechern
4. **Firmware-Updates**: (falls Protokoll bekannt)
5. **Presets**: Speichern von Lautstärke-Profilen

---

## 📞 Support

Bei Fragen oder Problemen:

1. Überprüfen Sie diese Anleitung
2. Testen Sie mit der Original-APK und einem Bluetooth-Sniffer
3. Passen Sie die Befehle in `MainActivity.java` an

---

## 📜 Lizenz

Dieses Projekt wurde für persönliche Nutzung erstellt. RIVA und alle Marken gehören ihren jeweiligen Eigentümern.

---

**Viel Erfolg mit Ihrer wiederhergestellten Turbo-Lautstärke! 🔊🎵**
