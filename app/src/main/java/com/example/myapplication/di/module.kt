package com.example.myapplication.di

import androidx.compose.foundation.interaction.DragInteraction
import androidx.room.Room
import com.example.myapplication.data.database.AppDatabase
import com.example.myapplication.data.datastore.DataStoreManager
import com.example.myapplication.data.datastore.DataStoreManagerHolder
import com.example.myapplication.data.repository.GroupRepository
import com.example.myapplication.data.repository.GroupRepositoryImpl
import com.example.myapplication.data.repository.LapRepository
import com.example.myapplication.data.repository.LapRepositoryImpl
import com.example.myapplication.data.repository.PersonRepository
import com.example.myapplication.data.repository.PersonRepositoryImpl
import com.example.myapplication.data.repository.RecordRepository
import com.example.myapplication.data.repository.RecordRepositoryImpl
import com.example.myapplication.util.Stopwatch
import com.example.myapplication.viewmodel.GroupPersonScreenViewModel
import com.example.myapplication.viewmodel.GroupScreenViewModel
import com.example.myapplication.viewmodel.LapScreenViewModel
import com.example.myapplication.viewmodel.PersonScreenViewModel
import com.example.myapplication.viewmodel.StopwatchScreenViewModel
import com.example.myapplication.views.screens.StopWatchScreen
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<AppDatabase> {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java, "database-name"
        ).addMigrations()
            .build()
    }
    single<PersonRepository> { PersonRepositoryImpl(get()) }
    single<GroupRepository> { GroupRepositoryImpl(get()) }
    single<LapRepository> { LapRepositoryImpl(get()) }
    single<RecordRepository> { RecordRepositoryImpl(get()) }
    single<DataStoreManager> { DataStoreManager(androidContext()) }
    single<DataStoreManagerHolder> { DataStoreManagerHolder(get()) }
    single<Stopwatch> { Stopwatch(get()) }

    viewModel { PersonScreenViewModel(get()) }
    viewModel { GroupScreenViewModel(get()) }
    viewModel { LapScreenViewModel(get(),get()) }
    viewModel { GroupPersonScreenViewModel(get(), get()) }
    viewModel { StopwatchScreenViewModel(get(),get(),get(),get(),get()) }
}