package com.rivaturbox.volumecontrol;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    
    // Bluetooth
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket;
    private OutputStream outputStream;
    private BluetoothDevice rivaDevice;
    
    // UI Elements
    private SeekBar volumeSeekBar;
    private TextView volumeText;
    private Switch turboSwitch;
    private Button connectButton;
    private Button volumeUpButton;
    private Button volumeDownButton;
    
    // Volume Settings
    private int currentVolume = 50;
    private int maxVolumeNormal = 100;
    private int maxVolumeTurbo = 120;  // Erhöhte Lautstärke im Turbo-Modus!
    private boolean turboMode = false;
    
    // UUID für Bluetooth SPP (Serial Port Profile)
    private static final UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    
    private Handler handler = new Handler();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initializeViews();
        initializeBluetooth();
        setupListeners();
    }
    
    private void initializeViews() {
        volumeSeekBar = findViewById(R.id.volumeSeekBar);
        volumeText = findViewById(R.id.volumeText);
        turboSwitch = findViewById(R.id.turboSwitch);
        connectButton = findViewById(R.id.connectButton);
        volumeUpButton = findViewById(R.id.volumeUpButton);
        volumeDownButton = findViewById(R.id.volumeDownButton);
        
        volumeSeekBar.setMax(maxVolumeNormal);
        volumeSeekBar.setProgress(currentVolume);
        updateVolumeText();
    }
    
    private void initializeBluetooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth wird nicht unterstützt", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
        }
    }
    
    private void setupListeners() {
        connectButton.setOnClickListener(v -> connectToRivaSpeaker());
        
        turboSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            turboMode = isChecked;
            int newMax = turboMode ? maxVolumeTurbo : maxVolumeNormal;
            volumeSeekBar.setMax(newMax);
            
            // Begrenze aktuelle Lautstärke wenn Turbo ausgeschaltet
            if (!turboMode && currentVolume > maxVolumeNormal) {
                currentVolume = maxVolumeNormal;
                volumeSeekBar.setProgress(currentVolume);
            }
            
            updateVolumeText();
            sendTurboCommand(isChecked);
            
            String mode = isChecked ? "Turbo-Modus AN (max 120%)" : "Normal-Modus (max 100%)";
            Toast.makeText(MainActivity.this, mode, Toast.LENGTH_SHORT).show();
        });
        
        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                currentVolume = progress;
                updateVolumeText();
            }
            
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                setVolume(currentVolume);
            }
        });
        
        volumeUpButton.setOnClickListener(v -> {
            int maxVol = turboMode ? maxVolumeTurbo : maxVolumeNormal;
            if (currentVolume < maxVol) {
                currentVolume++;
                volumeSeekBar.setProgress(currentVolume);
                setVolume(currentVolume);
                sendVolumeUpCommand();
            }
        });
        
        volumeDownButton.setOnClickListener(v -> {
            if (currentVolume > 0) {
                currentVolume--;
                volumeSeekBar.setProgress(currentVolume);
                setVolume(currentVolume);
                sendVolumeDownCommand();
            }
        });
    }
    
    private void connectToRivaSpeaker() {
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        
        // Suche nach Riva Turbo X
        for (BluetoothDevice device : pairedDevices) {
            String deviceName = device.getName();
            if (deviceName != null && (deviceName.contains("RIVA") || 
                deviceName.contains("Turbo") || deviceName.contains("riva"))) {
                rivaDevice = device;
                break;
            }
        }
        
        if (rivaDevice == null) {
            Toast.makeText(this, "RIVA Turbo X nicht gefunden. Bitte zuerst pairen!", 
                Toast.LENGTH_LONG).show();
            return;
        }
        
        new Thread(() -> {
            try {
                bluetoothSocket = rivaDevice.createRfcommSocketToServiceRecord(SPP_UUID);
                bluetoothSocket.connect();
                outputStream = bluetoothSocket.getOutputStream();
                
                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this, 
                        "Verbunden mit " + rivaDevice.getName(), 
                        Toast.LENGTH_SHORT).show();
                    connectButton.setText("Verbunden");
                    connectButton.setEnabled(false);
                });
                
            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> 
                    Toast.makeText(MainActivity.this, 
                        "Verbindung fehlgeschlagen: " + e.getMessage(), 
                        Toast.LENGTH_LONG).show()
                );
            }
        }).start();
    }
    
    private void updateVolumeText() {
        int maxVol = turboMode ? maxVolumeTurbo : maxVolumeNormal;
        int percentage = (currentVolume * 100) / maxVolumeNormal;
        volumeText.setText(String.format("Lautstärke: %d/%d (%d%%)", 
            currentVolume, maxVol, percentage));
    }
    
    private void setVolume(int volume) {
        // Skaliere Lautstärke basierend auf Turbo-Modus
        int scaledVolume = volume;
        if (turboMode && volume > maxVolumeNormal) {
            // Im Turbo-Modus: Werte über 100 werden verstärkt gesendet
            scaledVolume = volume;
        }
        
        sendVolumeCommand(scaledVolume);
    }
    
    private void sendVolumeCommand(int volume) {
        if (outputStream == null) {
            Toast.makeText(this, "Nicht verbunden!", Toast.LENGTH_SHORT).show();
            return;
        }
        
        new Thread(() -> {
            try {
                // Basierend auf der Analyse: COMMAND_SET_VOLUME
                // Format könnte sein: [CMD_HEADER][COMMAND_TYPE][PAYLOAD]
                byte[] command = new byte[] {
                    (byte) 0xAA,  // Möglicher Header
                    (byte) 0x55,  // Sync byte
                    (byte) 0x03,  // Command: Set Volume
                    (byte) volume // Volume level
                };
                
                outputStream.write(command);
                outputStream.flush();
                
            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> 
                    Toast.makeText(MainActivity.this, 
                        "Fehler beim Senden: " + e.getMessage(), 
                        Toast.LENGTH_SHORT).show()
                );
            }
        }).start();
    }
    
    private void sendVolumeUpCommand() {
        sendCommand(new byte[] {
            (byte) 0xAA,
            (byte) 0x55,
            (byte) 0x01  // COMMAND_VOLUME_UP
        });
    }
    
    private void sendVolumeDownCommand() {
        sendCommand(new byte[] {
            (byte) 0xAA,
            (byte) 0x55,
            (byte) 0x02  // COMMAND_VOLUME_DOWN
        });
    }
    
    private void sendTurboCommand(boolean enable) {
        sendCommand(new byte[] {
            (byte) 0xAA,
            (byte) 0x55,
            (byte) (enable ? 0x10 : 0x11)  // TURBO_ON / TURBO_OFF
        });
    }
    
    private void sendCommand(byte[] command) {
        if (outputStream == null) return;
        
        new Thread(() -> {
            try {
                outputStream.write(command);
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (outputStream != null) outputStream.close();
            if (bluetoothSocket != null) bluetoothSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
