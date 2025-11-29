package com.example.listadetarefas

import android.content.Context
import android.content.Intent
import android.media.AudioDeviceCallback
import android.media.AudioDeviceInfo
import android.media.AudioManager
import android.os.Bundle
import android.provider.Settings
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.listadetarefas.AudioHelper
import java.util.Locale

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var audioManager: AudioManager
    private lateinit var audioHelper: AudioHelper
    private lateinit var tts: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioHelper = AudioHelper(this)
        tts = TextToSpeech(this, this)

        findViewById<Button>(R.id.main_action_button).setOnClickListener {
            ensureAudioOutputAndSpeak("Alerta de segurança. Existe uma notificação crítica para você.")
        }

        audioManager.registerAudioDeviceCallback(object : AudioDeviceCallback() {

            override fun onAudioDevicesAdded(addedDevices: Array<out AudioDeviceInfo>?) {
                super.onAudioDevicesAdded(addedDevices)
                if (audioHelper.audioOutputAvailable(AudioDeviceInfo.TYPE_BLUETOOTH_A2DP)) {
                    Log.d("AudioHelper", "Fone de ouvido Bluetooth conectado! Pronto para áudio.")
                    ensureAudioOutputAndSpeak("Fones de ouvido Bluetooth conectados. Pronta para assistência.")
                }
            }

            override fun onAudioDevicesRemoved(removedDevices: Array<out AudioDeviceInfo>?) {
                super.onAudioDevicesRemoved(removedDevices)
                if (!audioHelper.audioOutputAvailable(AudioDeviceInfo.TYPE_BLUETOOTH_A2DP)) {
                    Log.d("AudioHelper", "Fone de ouvido Bluetooth desconectado! Áudio desativado.")
                }
            }
        }, null)

        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById<View>(R.id.main)
        ) { v: View, insets: WindowInsetsCompat ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts.setLanguage(Locale("pt", "BR"))

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "O idioma especificado (Português/Brasil) não é suportado.")
            } else {
                Log.i("TTS", "Text-to-Speech pronto e configurado.")
            }
        } else {
            Log.e("TTS", "Falha na inicialização do Text-to-Speech.")
        }
    }

    fun speakMessage(text: String) {
        if (::tts.isInitialized && tts.defaultEngine != null) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "TTS_ID")
        } else {
            Log.e("TTS", "TTS não inicializado. Não foi possível falar.")
        }
    }

    fun ensureAudioOutputAndSpeak(text: String) {
        if (audioHelper.audioOutputAvailable(AudioDeviceInfo.TYPE_BLUETOOTH_A2DP)) {
            Log.i("AudioOutput", "Usando Bluetooth para reprodução.")
            speakMessage(text)
        } else if (audioHelper.audioOutputAvailable(AudioDeviceInfo.TYPE_BUILTIN_SPEAKER)) {
            Log.i("AudioOutput", "Usando Alto-falante embutido para reprodução.")
            speakMessage(text)
        } else {
            Log.w("AudioOutput", "Nenhuma saída de áudio disponível para reproduzir a mensagem.")
        }
    }

    fun openBluetoothSettings() {
        val intent = Intent(Settings.ACTION_BLUETOOTH_SETTINGS).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            putExtra("EXTRA_CONNECTION_ONLY", true)
            putExtra("EXTRA_CLOSE_ON_CONNECT", true)
            putExtra("android.bluetooth.devicepicker.extra.FILTER_TYPE", 1)
        }
        startActivity(intent)
    }

    override fun onDestroy() {
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        super.onDestroy()
    }
}