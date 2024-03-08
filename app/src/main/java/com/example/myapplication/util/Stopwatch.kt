package com.example.myapplication.util

import android.util.Log
import com.example.myapplication.data.datastore.DataStoreManagerHolder
import com.example.myapplication.data.datastore.TimeStopwatch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeUnit

enum class StopwatchStatus {
    START, RESUME
}

class Stopwatch(
    private val dataStoreManagerHolder: DataStoreManagerHolder
) {
    private var job: Job? = null
    private var timeStopwatch: TimeStopwatch = TimeStopwatch()

    fun start(stopwatchId: Int, tick: (timeStopwatch: TimeStopwatch,time: String, day: String) -> Unit) {
        job?.cancel()
        job = CoroutineScope(Dispatchers.IO).launch {
            timeStopwatch = runBlocking { dataStoreManagerHolder.getStartTime(stopwatchId).take(1).first() }
            while (isActive) {
                if (!timeStopwatch.isRunning) {
                    if (timeStopwatch.startTime == timeStopwatch.stopTime) {
                        timeStopwatch = timeStopwatch.copy(
                            stopwatch_id = "" + stopwatchId,
                            startTime = System.currentTimeMillis(),
                            isRunning = true
                        )
                        Log.e("Stopwatch", "start: "+timeStopwatch, )
                        dataStoreManagerHolder.saveDataStartTime(
                            timeStopwatch
                        )
                    }

                    if (timeStopwatch.startTime < timeStopwatch.stopTime) {
                        timeStopwatch =  timeStopwatch.copy(
                            stopwatch_id = "" + stopwatchId,
                            startTime = timeStopwatch.startTime + (System.currentTimeMillis() - timeStopwatch.stopTime),
                            isRunning = true
                        )
                        Log.e("Stopwatch", "resume: "+timeStopwatch, )
                        dataStoreManagerHolder.saveDataStartTime(
                            timeStopwatch
                        )
                    }
                } else {
                    dataStoreManagerHolder.saveDataStartTime(timeStopwatch)
                }
                if (timeStopwatch.isRunning) {
                    Log.e("Stopwatch", "run: "+timeStopwatch, )
                    callback(timeStopwatch, tick)
                }
                delay(100)
            }
        }
    }

    fun up(stopwatchId: Int, tick: (timeStopwatch: TimeStopwatch,time: String, day: String) -> Unit) {
        job?.cancel()
        job = CoroutineScope(Dispatchers.IO).launch {
            timeStopwatch = runBlocking { dataStoreManagerHolder.getStartTime(stopwatchId).first() }
            callback(timeStopwatch, tick)
            Log.e("Stopwatch", "up: "+timeStopwatch, )
            while (isActive) {
                if (timeStopwatch.isRunning) {
                    callback(timeStopwatch, tick)
                }
                delay(100)
            }
        }
    }

    fun stop(stopwatchId: Int) {
        job?.cancel()
        timeStopwatch =  timeStopwatch.copy(
            stopwatch_id = "" + stopwatchId,
            stopTime = System.currentTimeMillis(),
            isRunning = false
        )
        Log.e("Stopwatch", "stop: "+timeStopwatch, )
        dataStoreManagerHolder.saveDataStartTime(
            timeStopwatch
        )
    }

    fun restart(stopwatchId: Int) {
        job?.cancel()
        timeStopwatch =  timeStopwatch.copy(
            stopwatch_id = "" + stopwatchId,
            stopTime = 0L,
            startTime = 0L,
            isRunning = false
        )
        Log.e("Stopwatch", "restart: "+timeStopwatch, )
        dataStoreManagerHolder.saveDataStartTime(
            timeStopwatch
        )
    }

    private fun callback(timeStopwatch: TimeStopwatch, tick: (timeStopwatch: TimeStopwatch,time: String, day: String) -> Unit) {
        var currentTime = System.currentTimeMillis()
        var elapsedTime = currentTime - timeStopwatch.startTime

        var formattedTime = formatTime(elapsedTime)
        var formattedDay = formatDay(elapsedTime)

        if (!timeStopwatch.isRunning){
            currentTime = timeStopwatch.stopTime
            elapsedTime = currentTime - timeStopwatch.startTime

            formattedTime = formatTime(elapsedTime)
            formattedDay = formatDay(elapsedTime)
        }

        tick(timeStopwatch,formattedTime, formattedDay)
    }

    fun formatTime(milliseconds: Long): String {
        val hours = TimeUnit.MILLISECONDS.toHours(milliseconds) % 24
        val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds) % 60

        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    fun formatDay(milliseconds: Long): String {
        val days = TimeUnit.MILLISECONDS.toDays(milliseconds)
        return String.format("%d", days)
    }

    fun getTime(): Long{
        var currentTime = System.currentTimeMillis()
        var elapsedTime = currentTime - timeStopwatch.startTime

        return elapsedTime
    }
}