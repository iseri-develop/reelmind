package com.ripplecode.reelmind.data.store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class UserPreferencesDataStore(private val context: Context) {

    private val GENRES_KEY = stringSetPreferencesKey("favorite_genres")

    val favoriteGenres: Flow<Set<String>> = context.dataStore.data.map { prefs ->
        prefs[GENRES_KEY] ?: emptySet()
    }

    suspend fun saveFavoriteGenres(genres: Set<String>) {
        context.dataStore.edit { prefs ->
            prefs[GENRES_KEY] = genres
        }
    }
}