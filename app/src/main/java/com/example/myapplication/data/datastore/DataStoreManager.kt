package com.example.myapplication.data.datastore

import android.content.Context
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

class DataStoreManager(private val context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user")

    companion object {
        val SiZE_STOPWATCH = "size_stopwatch_"
        val SiZE_TEXT = "size_text_"
        val SiZE_COUNT = "size_count_"
        val SHOULD_DRAW_STOPWATCH = "should_draw_stopwatch_"
        val SHOULD_DRAW_COUNT = "should_draw_count_"
        val SHOULD_DRAW_TEXT = "should_draw_text_"
        val START_TIME = "start_time"
        val STOP_TIME = "stop_time"
        val IS_RUNNING = "is_running"
    }

    suspend fun saveDataTextStopwatch(textStopwatch: TextStopwatch) {
        context.dataStore.edit {
            it[floatPreferencesKey(SiZE_STOPWATCH + textStopwatch.text_id)] =
                textStopwatch.size_stopwatch.value
            it[floatPreferencesKey(SiZE_COUNT + textStopwatch.text_id)] =
                textStopwatch.size_count.value
            it[booleanPreferencesKey(SHOULD_DRAW_STOPWATCH + textStopwatch.text_id)] =
                textStopwatch.should_draw_stopwatch
            it[booleanPreferencesKey(SHOULD_DRAW_COUNT + textStopwatch.text_id)] =
                textStopwatch.should_draw_count
        }
    }

    suspend fun saveDataTimeStopwatch(timeStopwatch: TimeStopwatch) {
        context.dataStore.edit {
            it[longPreferencesKey(START_TIME + timeStopwatch.stopwatch_id)] = timeStopwatch.startTime
            it[longPreferencesKey(STOP_TIME + timeStopwatch.stopwatch_id)] = timeStopwatch.stopTime
            it[booleanPreferencesKey(IS_RUNNING + timeStopwatch.stopwatch_id)] = timeStopwatch.isRunning
        }
    }

    suspend fun saveDataAutoTextByWidth(autoTextByWidth: AutoTextByWidth) {
        context.dataStore.edit {
            it[floatPreferencesKey(SiZE_TEXT + autoTextByWidth.text_id)] =
                autoTextByWidth.size_text.value
            it[booleanPreferencesKey(SHOULD_DRAW_TEXT + autoTextByWidth.text_id)] =
                autoTextByWidth.should_draw_text
        }
    }

    fun getDataTextStopwatchFromDataStore(text_id: String) = context.dataStore.data.map {
        TextStopwatch(
            text_id = text_id,
            size_stopwatch = it[floatPreferencesKey(SiZE_STOPWATCH + text_id)]?.sp ?: 0.sp,
            size_count = it[floatPreferencesKey(SiZE_COUNT + text_id)]?.sp ?: 0.sp,
            should_draw_stopwatch = it[booleanPreferencesKey(SHOULD_DRAW_STOPWATCH + text_id)]
                ?: false,
            should_draw_count = it[booleanPreferencesKey(SHOULD_DRAW_COUNT + text_id)] ?: false,
        )
    }

    fun getDataTextAutoByWidth(text_id: String) = context.dataStore.data.map {
        AutoTextByWidth(
            text_id = text_id,
            size_text = it[floatPreferencesKey(SiZE_TEXT + text_id)]?.sp ?: 0.sp,
            should_draw_text = it[booleanPreferencesKey(SHOULD_DRAW_TEXT + text_id)] ?: false,
        )
    }

    fun getTimeStopwatch(lap_id: Int) = context.dataStore.data.map {
        TimeStopwatch(
            stopwatch_id = ""+lap_id,
            startTime = it[longPreferencesKey(START_TIME + lap_id)] ?: System.currentTimeMillis(),
            stopTime = it[longPreferencesKey(STOP_TIME + lap_id)] ?: System.currentTimeMillis(),
            isRunning  = it[booleanPreferencesKey(IS_RUNNING + lap_id)] ?: false,
        )
    }

    suspend fun deleteSession() = context.dataStore.edit {
        it.clear()
    }
}

data class TextStopwatch(
    val text_id: String = "def",
    val size_stopwatch: TextUnit = 20.sp,
    val size_count: TextUnit = 20.sp,
    val should_draw_stopwatch: Boolean = false,
    val should_draw_count: Boolean = false,
)

data class TimeStopwatch(
    val stopwatch_id: String = "",
    val startTime: Long = 0L,
    val stopTime: Long = 0L,
    val isRunning: Boolean = false,
)

data class AutoTextByWidth(
    val text_id: String = "def",
    val size_text: TextUnit = 20.sp,
    val should_draw_text: Boolean = false,
)
