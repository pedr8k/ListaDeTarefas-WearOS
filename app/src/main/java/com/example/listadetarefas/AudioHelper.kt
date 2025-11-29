package com.example.listadetarefas

import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioDeviceInfo
import android.media.AudioManager

class AudioHelper(private val context: Context) {

    private val audioManager: AudioManager =
        context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    /**
     * Verifica se o tipo de saída de áudio especificado (como Bluetooth ou alto-falante) está disponível.
     * @param type O tipo de dispositivo de áudio (ex: AudioDeviceInfo.TYPE_BLUETOOTH_A2DP).
     * @return True se o dispositivo de áudio estiver disponível.
     */
    fun audioOutputAvailable(type: Int): Boolean {
        // Verifica se o dispositivo possui capacidade de saída de áudio (geralmente sempre true em Wear OS)
        if (!context.packageManager.hasSystemFeature(PackageManager.FEATURE_AUDIO_OUTPUT)) {
            return false
        }

        // Enumera todas as saídas de áudio e verifica se alguma corresponde ao 'type'
        return audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS).any { it.type == type }
    }
}