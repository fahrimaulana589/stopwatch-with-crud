package com.example.myapplication.data.datastore

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class DataStoreManagerHolder(private val dataStoreManager: DataStoreManager) {

    fun saveDataStopwatch(textStopwatch: TextStopwatch) {
        CoroutineScope(Dispatchers.IO).launch {
            dataStoreManager.saveDataTextStopwatch(
                textStopwatch
            )
        }
    }

    fun saveDataAutoText(autoTextByWidth: AutoTextByWidth) {
        CoroutineScope(Dispatchers.IO).launch {
            dataStoreManager.saveDataAutoTextByWidth(
                autoTextByWidth
            )
        }
    }

    fun saveDataStartTime(timeStopwatch: TimeStopwatch){
        CoroutineScope(Dispatchers.IO).launch {
            dataStoreManager.saveDataTimeStopwatch(timeStopwatch)
        }
    }

    fun getStartTime(lap_id: Int): Flow<TimeStopwatch> {
        return  dataStoreManager.getTimeStopwatch(lap_id)
    }
}