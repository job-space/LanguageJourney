package com.example.dictionaryapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationReceiver : BroadcastReceiver() {
    private lateinit var db: DbHelper
    override fun onReceive(context: Context, intent: Intent?) {
        db = DbHelper(context, null)
        if (db.statusCheck() == true) {
            // Creating an instance of NotificationManager
            val notificationManager = NotificationManager(context)
            // Sending a notification
            notificationManager.sendNotification("LanguageJourney", "Hello, Час Англійської")
        }
    }
}
