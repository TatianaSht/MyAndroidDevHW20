package com.example.myandroiddevhw20

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Room
import com.example.myandroiddevhw20.data.db.AppDatabase
import com.example.myandroiddevhw20.data.db.MIGRATION_1_2
import com.yandex.mapkit.MapKitFactory
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    lateinit var db: AppDatabase

    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey(YANDEX_MAPKIT_API_KEY)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "db"
        )
            .addMigrations(MIGRATION_1_2)
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            createNotificationChannel()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChannel() {

        val name = "Test notification channel"
        val descriptionText = "This is a simple description"
        val importance = NotificationManager.IMPORTANCE_DEFAULT

        val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        private const val YANDEX_MAPKIT_API_KEY = "d566e6f6-c4dc-4a39-a641-0a7bca490858"
        const val NOTIFICATION_CHANNEL_ID = "test_channel_id"
    }
}

