package akh.data.di.module

import akh.core.app.App
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SharedPreferenceModule {

    private val PREF_NAME = "prefs"
    private val ENCRYPT_PREF_NAME = "secret_shared_prefs"

    @Provides
    @Singleton
    fun provideSharedPreferences(app: App): SharedPreferences =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

            EncryptedSharedPreferences.create(
                ENCRYPT_PREF_NAME,
                masterKeyAlias,
                app.getApplicationContext(),
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        } else
            app.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

}