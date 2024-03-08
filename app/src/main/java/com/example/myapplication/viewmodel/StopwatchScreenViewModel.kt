package com.example.myapplication.viewmodel

import android.util.Log
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.datastore.AutoTextByWidth
import com.example.myapplication.data.datastore.DataStoreManager
import com.example.myapplication.data.datastore.TextStopwatch
import com.example.myapplication.data.dto.LapDto
import com.example.myapplication.data.dto.PersonDto
import com.example.myapplication.data.dto.RecordDto
import com.example.myapplication.data.dto.RecordWithPersonDto
import com.example.myapplication.data.repository.LapRepository
import com.example.myapplication.data.repository.PersonRepository
import com.example.myapplication.data.repository.RecordRepository
import com.example.myapplication.data.repository.ValidationState
import com.example.myapplication.ui.theme.AplicationState
import com.example.myapplication.util.Stopwatch
import com.example.myapplication.views.components.CardState
import com.example.myapplication.views.components.DragPlayBoxStatus
import com.example.myapplication.views.components.StateAutoSizeByWidth
import com.example.myapplication.views.components.StateDragPlayBox
import com.example.myapplication.views.components.StateRecordList
import com.example.myapplication.views.components.StateTimeStopwatch
import com.example.myapplication.views.components.TitleState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch

class StopwatchScreenViewModel(
    private val personRepository: PersonRepository,
    private val lapRepository: LapRepository,
    private val recordRepository: RecordRepository,
    private val dataStoreManager: DataStoreManager,
    private val stopwatch: Stopwatch
) : ViewModel() {
    private val _aplicationState = MutableStateFlow(AplicationState())
    val aplicationState = _aplicationState.asStateFlow()

    private val _titleState = MutableStateFlow(
        TitleState(title = "Lap", isBack = true)
    )
    val titleState = _titleState.asStateFlow()

    private val _lap = MutableStateFlow(
        LapDto()
    )
    val lap = _lap.asStateFlow()

    private val _statepPersonList = MutableStateFlow<List<CardState>>(
        emptyList()
    )
    val statepPersonList = _statepPersonList.asStateFlow()

    private val _records = MutableStateFlow<List<RecordWithPersonDto>>(
        emptyList()
    )
    val records = _records.asStateFlow()

    private val _record = MutableStateFlow(
        RecordWithPersonDto()
    )

    private val _person = MutableStateFlow<List<PersonDto>>(
        emptyList()
    )

    private val _stateTimeStopwatch = MutableStateFlow(
        StateTimeStopwatch(
            count = "0",
            time = "01:50:00",
            textStopwatch = TextStopwatch(
                text_id = "asd",
                size_stopwatch = 40.sp,
                size_count = 15.sp
            )
        )
    )
    val stateTimeStopwatch = _stateTimeStopwatch.asStateFlow()

    private val _stateDragPlayBox = MutableStateFlow(
        StateDragPlayBox(
            onPlayClick = {
                onPlayClick()
            },
            onRecordClick = {
                onRecordClick()
            },
            onRestartClick = {
                onRestartClick()
            },
            onStopClick = {
                onStopClick()
            },
        )
    )

    private fun onStopClick() {
        stopwatch.stop(lap.value.lap_id)
        _stateDragPlayBox.value = _stateDragPlayBox.value.copy(
            status = DragPlayBoxStatus.Stop
        )
    }

    private fun onRestartClick() {
        stopwatch.restart(lap.value.lap_id)
        _stateTimeStopwatch.value = _stateTimeStopwatch.value.copy(
            count = "0",
            time = "00:00:00"
        )
    }

    private fun onRecordClick() {
        Log.e("Cawi", "onRecordClick: "+_person.value.count(), )
        Log.e("Cawi", "onRecordClick: "+records.value.count(), )
        if(_person.value.count() > records.value.count()){
            CoroutineScope(Dispatchers.IO).launch {
                recordRepository.add(RecordDto(
                    lap_id = lap.value.lap_id,
                    time = stopwatch.getTime(),
                ), ValidationState(
                    onSuccess = {
                        getAllRecord()
                    },
                    onFailure = {

                    }
                ))
            }
        }
    }

    private fun onPlayClick() {
        stopwatch.start(lap.value.lap_id) { stopwatch, time, day ->
            _stateTimeStopwatch.value = _stateTimeStopwatch.value.copy(
                count = day,
                time = time
            )
        }
        _stateDragPlayBox.value = _stateDragPlayBox.value.copy(
            status = DragPlayBoxStatus.Play
        )
    }

    private fun onUP() {
        stopwatch.up(lap.value.lap_id) { stopwatch, time, day ->
            _stateTimeStopwatch.value = _stateTimeStopwatch.value.copy(
                count = day,
                time = time
            )
            if (stopwatch.isRunning) {
                _stateDragPlayBox.value = _stateDragPlayBox.value.copy(
                    status = DragPlayBoxStatus.Play
                )
            }
        }
    }

    val stateDragPlayBox = _stateDragPlayBox.asStateFlow()

    private val _stateRecordList = MutableStateFlow(
        StateRecordList(
            stateTimeStopwatch = StateTimeStopwatch(
                count = "0",
                time = "01:50:00",
                textStopwatch = TextStopwatch(
                    text_id = "hanam",
                    size_stopwatch = 40.sp,
                    size_count = 15.sp
                )
            ),
            stateAutoSizeByWidth = StateAutoSizeByWidth(
                text = "000",
                autoTextByWidth = AutoTextByWidth(
                    text_id = "nopi",
                    size_text = 40.sp
                )
            )
        )
    )
    val stateRecordList = _stateRecordList.asStateFlow()

    private fun getDataTextStopwatchFromDataStore(text_id: String): Flow<TextStopwatch> {
        return dataStoreManager.getDataTextStopwatchFromDataStore(text_id)
    }

    private fun getDataTextAutoByWidth(text_id: String): Flow<AutoTextByWidth> {
        return dataStoreManager.getDataTextAutoByWidth(text_id)
    }

    fun setLap(lapId: Int) {
        CoroutineScope(Dispatchers.IO).launch() {
            _lap.value = lapRepository.findByUid(lapId)!!
            onUP()
            getAllPerson()
            getAllRecord()
            _person.value = personRepository.getPersonsLinkedToLap(lapId)
        }

    }

    fun selectRecord(record: RecordWithPersonDto){
        _record.value = record
        Log.e("Cawi", "selectRecord: "+_record.value, )
        showDrawer()
    }

    fun showDrawer(){
        _aplicationState.value = _aplicationState.value.copy(
            darwerVisible = true
        )
    }

    fun closeDrawer(){
        _aplicationState.value = _aplicationState.value.copy(
            darwerVisible = false
        )
    }

    fun getAllPerson() {
        CoroutineScope(Dispatchers.IO).launch() {
            var count = 0
            _statepPersonList.value =
                personRepository.getPersonsLinkedToLapWithoutRecords(lap.value.lap_id).map {
                    CardState(
                        id = it.person_id,
                        nomer = ++count,
                        name = it.name,
                        onClick = {
                            setPerson(it.person_id)
                        }
                    )
                }
            Log.e("Cawi", "getAllPerson: "+_statepPersonList.value.count(), )
        }
    }

    fun setPerson(personId: Int) {
        Log.e("Cawi", "setPerson: "+_record.value, )
        CoroutineScope(Dispatchers.IO).launch() {
            recordRepository.update(RecordDto(
                lap_id = lap.value.lap_id,
                time = stopwatch.getTime(),
                record_id = _record.value.recordDto.record_id,
                person_id = personId
            ), ValidationState(
                onSuccess = {
                    closeDrawer()
                    getAllRecord()
                },
                onFailure = {

                }
            ))
        }
    }

    fun getAllRecord() {
        CoroutineScope(Dispatchers.IO).launch() {
            _records.value = recordRepository.getAllByLap(lap.value.lap_id)
        }
    }

    fun toTimeString(time: Long): String {
        return stopwatch.formatTime(time)
    }

    fun toCountString(time: Long): String {
        return stopwatch.formatDay(time)
    }

    init {
        CoroutineScope(Dispatchers.IO).launch {
            getDataTextStopwatchFromDataStore(_stateTimeStopwatch.value.textStopwatch.text_id)
                .take(1)
                .collectLatest { data ->
                    if (data.should_draw_stopwatch && data.should_draw_count) {
                        _stateTimeStopwatch.value = _stateTimeStopwatch.value.copy(
                            textStopwatch = data
                        )
                    }
                }
        }

        CoroutineScope(Dispatchers.IO).launch {
            getDataTextStopwatchFromDataStore(_stateRecordList.value.stateTimeStopwatch.textStopwatch.text_id)
                .take(1)
                .collectLatest { data ->
                    if (data.should_draw_stopwatch && data.should_draw_count) {
                        _stateRecordList.value = _stateRecordList.value.copy(
                            stateTimeStopwatch = _stateRecordList.value.stateTimeStopwatch.copy(
                                textStopwatch = data
                            )
                        )
                    }
                }

            getDataTextAutoByWidth(_stateRecordList.value.stateAutoSizeByWidth.autoTextByWidth.text_id)
                .take(1)
                .collectLatest { data ->
                    if (data.should_draw_text) {
                        _stateRecordList.value = _stateRecordList.value.copy(
                            stateAutoSizeByWidth = _stateRecordList.value.stateAutoSizeByWidth.copy(
                                autoTextByWidth = data
                            )
                        )
                    }
                }
        }
    }

}