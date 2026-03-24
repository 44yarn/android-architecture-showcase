package io.github.yarn44.showcase.core.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * Type-safe wrapper around DataStore Preferences.
 * Provides suspend functions for CRUD operations and Flow-based observation.
 */
@Singleton
class PreferenceStorage @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {
    suspend fun <T> getOrNull(key: PreferenceKey<T>): T? {
        val preferences = dataStore.data.first()
        return preferences[key.dataStoreKey()]
    }

    suspend fun <T> getOrDefault(key: PreferenceKey<T>, default: T): T =
        getOrNull(key) ?: default

    fun <T> observe(key: PreferenceKey<T>): Flow<T?> =
        dataStore.data
            .map { it[key.dataStoreKey()] }
            .distinctUntilChanged()

    suspend fun <T> put(key: PreferenceKey<T>, value: T) {
        dataStore.edit { it[key.dataStoreKey()] = value }
    }

    suspend fun <T> remove(key: PreferenceKey<T>) {
        dataStore.edit { it.remove(key.dataStoreKey()) }
    }

    suspend fun removeAll() {
        dataStore.edit { it.clear() }
    }
}
