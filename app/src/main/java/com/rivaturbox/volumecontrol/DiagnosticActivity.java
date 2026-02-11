package com.rivaturbox.volumecontrol;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

/**
 * Diagnose-Activity zum Testen verschiedener Bluetooth-Befehle
 * 
 * Verwendung:
 * 1. Verbinden Sie sich mit dem Lautsprecher
 * 2. Geben Sie Hex-Befehle ein (z.B. "AA 55 03 64")
 * 3. Senden Sie den Befehl
 * 4. Beobachten Sie die Reaktion des Lautsprechers
 * 
 * Dies hilft dabei, die korrekten Befehle zu ermitteln!
 */
public class DiagnosticActivity extends AppCompatActivity {
    
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket;
    private OutputStream outputStream;
    private InputStream inputStream;
    
    private EditText hexCommandInput;
    private TextView responseOutput;
    private TextView logOutput;
    
    private static final UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Setzen Sie hier Ihr Layout (diagnostic_layout.xml)
        
        hexCommandInput = findViewById(R.id.hexCommandInput);
        responseOutput = findViewById(R.id.responseOutput);
        logOutput = findViewById(R.id.logOutput);
        
        Button connectButton = findViewById(R.id.connectButton);
        Button sendButton = findViewById(R.id.sendButton);
        Button clearButton = findViewById(R.id.clearButton);
        
        connectButton.setOnClickListener(v -> connectToDevice());
        sendButton.setOnClickListener(v -> sendHexCommand());
        clearButton.setOnClickListener(v -> {
            logOutput.setText("");
            responseOutput.setText("");
        });
        
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        
        // Vordefinierte Test-Befehle
        Button testVolume = findViewById(R.id.testVolumeButton);
        testVolume.setOnClickListener(v -> {
            hexCommandInput.setText("AA 55 03 50");  // Set Volume 80
        });
        
        Button testTurboOn = findViewById(R.id.testTurboOnButton);
        testTurboOn.setOnClickListener(v -> {
            hexCommandInput.setText("AA 55 10");  // Turbo On
        });
        
        Button testTurboOff = findViewById(R.id.testTurboOffButton);
        testTurboOff.setOnClickListener(v -> {
            hexCommandInput.setText("AA 55 11");  // Turbo Off
        });
    }
    
    private void connectToDevice() {
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        BluetoothDevice rivaDevice = null;
        
        for (BluetoothDevice device : pairedDevices) {
            if (device.getName() != null && device.getName().contains("RIVA")) {
                rivaDevice = device;
                break;
            }
        }
        
        if (rivaDevice == null) {
            log("RIVA Turbo X nicht gefunden!");
            return;
        }
        
        BluetoothDevice finalRivaDevice = rivaDevice;
        new Thread(() -> {
            try {
                bluetoothSocket = finalRivaDevice.createRfcommSocketToServiceRecord(SPP_UUID);
                bluetoothSocket.connect();
                outputStream = bluetoothSocket.getOutputStream();
                inputStream = bluetoothSocket.getInputStream();
                
                runOnUiThread(() -> log("Verbunden mit " + finalRivaDevice.getName()));
                
                // Starte Listener für Antworten
                startResponseListener();
                
            } catch (IOException e) {
                runOnUiThread(() -> log("Verbindungsfehler: " + e.getMessage()));
            }
        }).start();
    }
    
    private void sendHexCommand() {
        String hexString = hexCommandInput.getText().toString().trim();
        
        try {
            String[] hexBytes = hexString.split(" ");
            byte[] command = new byte[hexBytes.length];
            
            for (int i = 0; i < hexBytes.length; i++) {
                command[i] = (byte) Integer.parseInt(hexBytes[i], 16);
            }
            
            log("Sende: " + hexString);
            
            new Thread(() -> {
                try {
                    outputStream.write(command);
                    outputStream.flush();
                    runOnUiThread(() -> log("Befehl gesendet!"));
                } catch (IOException e) {
                    runOnUiThread(() -> log("Sendefehler: " + e.getMessage()));
                }
            }).start();
            
        } catch (Exception e) {
            log("Fehler beim Parsen: " + e.getMessage());
        }
    }
    
    private void startResponseListener() {
        new Thread(() -> {
            byte[] buffer = new byte[1024];
            
            while (bluetoothSocket != null && bluetoothSocket.isConnected()) {
                try {
                    int bytes = inputStream.read(buffer);
                    if (bytes > 0) {
                        StringBuilder hex = new StringBuilder();
                        for (int i = 0; i < bytes; i++) {
                            hex.append(String.format("%02X ", buffer[i]));
                        }
                        
                        String response = hex.toString();
                        runOnUiThread(() -> {
                            responseOutput.setText("Empfangen: " + response);
                            log("← " + response);
                        });
                    }
                } catch (IOException e) {
                    break;
                }
            }
        }).start();
    }
    
    private void log(String message) {
        runOnUiThread(() -> {
            String currentLog = logOutput.getText().toString();
            logOutput.setText(currentLog + "\n" + message);
        });
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (outputStream != null) outputStream.close();
            if (inputStream != null) inputStream.close();
            if (bluetoothSocket != null) bluetoothSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

/*
 * ANLEITUNG ZUM FINDEN DER RICHTIGEN BEFEHLE:
 * 
 * 1. BEKANNTE BEFEHLSMUSTER (aus Original-APK):
 *    - COMMAND_VOLUME_UP: Testwerte AA 55 01, oder AA 01, oder 01
 *    - COMMAND_VOLUME_DOWN: Testwerte AA 55 02, oder AA 02, oder 02
 *    - COMMAND_SET_VOLUME: Testwerte AA 55 03 <vol>, oder 03 <vol>
 *    - COMMAND_TURBO_ON: Testwerte AA 55 10, oder 10
 *    - COMMAND_TURBO_OFF: Testwerte AA 55 11, oder 11
 * 
 * 2. SYSTEMATISCHES TESTEN:
 *    a) Starten Sie mit einfachen Befehlen (1 Byte)
 *    b) Erweitern Sie zu 2-Byte-Befehlen
 *    c) Testen Sie mit Header/Sync-Bytes
 * 
 * 3. BEOBACHTEN SIE:
 *    - Ändert sich die Lautstärke?
 *    - Gibt der Lautsprecher eine Bestätigung zurück?
 *    - Welche Hex-Antwort kommt zurück?
 * 
 * 4. DOKUMENTIEREN SIE:
 *    - Notieren Sie funktionierende Befehle
 *    - Teilen Sie diese mit der Community!
 * 
 * ALTERNATIVE METHODE - HCI Snoop Log:
 * 1. Aktivieren Sie in Android Developer Options: "Bluetooth HCI snoop log"
 * 2. Verwenden Sie die Original RIVA App
 * 3. Ändern Sie die Lautstärke
 * 4. Extrahieren Sie das Log: /sdcard/Android/data/btsnoop_hci.log
 * 5. Analysieren Sie mit Wireshark
 * 6. Suchen Sie nach SPP-Paketen mit der UUID 00001101-...
 */
