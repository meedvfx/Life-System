package com.meedz.lifeos.core.security

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import java.security.SecureRandom
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KeyManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val PREF_NAME = "encrypted_db_prefs"
    private val KEY_PASSPHRASE = "db_passphrase"

    private val sharedPreferences by lazy {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
            
        EncryptedSharedPreferences.create(
            context,
            PREF_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun getDatabasePassphrase(): ByteArray {
        var passphraseHex = sharedPreferences.getString(KEY_PASSPHRASE, null)
        if (passphraseHex == null) {
            val secureRandom = SecureRandom()
            val newPassphrase = ByteArray(32)
            secureRandom.nextBytes(newPassphrase)
            passphraseHex = newPassphrase.joinToString("") { "%02x".format(it) }
            sharedPreferences.edit().putString(KEY_PASSPHRASE, passphraseHex).apply()
        }
        return passphraseHex.chunked(2).map { it.toInt(16).toByte() }.toByteArray()
    }
}
