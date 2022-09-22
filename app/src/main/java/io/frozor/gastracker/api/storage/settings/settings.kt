package io.frozor.gastracker.api.storage.settings

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

const val DATA_STORE_NAME = "settings"
val KEY_DEVICE_ID = stringPreferencesKey("btDeviceUuid")

val Context.settingsDataStore by preferencesDataStore(DATA_STORE_NAME)

fun getDeviceId(context: Context) = context.settingsDataStore.data.map { it[KEY_DEVICE_ID] }
suspend fun setDeviceId(context: Context, deviceId: String) =
    context.settingsDataStore.edit { it[KEY_DEVICE_ID] = deviceId }
