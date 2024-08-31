package com.example.dictionaryapp

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class SettingsActivity : AppCompatActivity() {

    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var buttonToggleNotifications: Button

    private lateinit var db: DbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)

        val navigationView: ImageButton = findViewById(R.id.main_menu_button)

        val shareToFriendsItem: Button = findViewById(R.id.item_share_friends)
        val rateUsItem: Button = findViewById(R.id.item_rate_us)
        val FeedbackItem: Button = findViewById(R.id.item_feedback)

        navigationView.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


        // Adding logging for diagnostics
        val themeSwitchButton: Button = findViewById(R.id.themeSwitchButton)
        themeSwitchButton.setOnClickListener {
            val isNightMode = ThemePreference.isNightMode(this)
            ThemePreference.setTheme(this, !isNightMode)
            AppCompatDelegate.setDefaultNightMode(
                if (isNightMode) AppCompatDelegate.MODE_NIGHT_NO
                else AppCompatDelegate.MODE_NIGHT_YES
            )
            recreate()
        }

        // Share with your friends
        shareToFriendsItem.setOnClickListener {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(
                    Intent.EXTRA_TEXT,
                    "Перегляньте цей чудовий додаток: https://play.google.com/store/apps/details?id=${packageName}"
                )
                type = "text/plain"
            }
            startActivity(Intent.createChooser(shareIntent, "Поділитися через"))
        }

        // Rate us
        rateUsItem.setOnClickListener {
            val appPackageName = packageName // This is used to refer to the application
            // try to open the Play market if installed
            try {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=$appPackageName")
                    )
                )
            } catch (e: android.content.ActivityNotFoundException) {
                // if the application is not installed, the site
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                    )
                )
            }
        }

        // Feedback
        FeedbackItem.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SEND).apply {
                type = "message/rfc822"
                putExtra(Intent.EXTRA_EMAIL, arrayOf("v64667121@gmail.com"))
                putExtra(Intent.EXTRA_SUBJECT, "Фідбек щодо додатку")
            }
            try {
                startActivity(Intent.createChooser(emailIntent, "Виберіть поштовий клієнт:"))
            } catch (e: Exception) {
                Toast.makeText(this, "Немає доступного поштового клієнта", Toast.LENGTH_SHORT)
                    .show()
            }
        }







        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.settings_activity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        // Notifications
        db = DbHelper(this, null)


        // Button processing
        buttonToggleNotifications = findViewById(R.id.button_toggle_notifications)

        if (db.statusCheck()==false) {
            buttonToggleNotifications.text = "Увімкнути сповіщення"
        }

        buttonToggleNotifications.setOnClickListener {
            if (buttonToggleNotifications.text == "Вимкнути сповіщення") {
                if (db.statusUpdate(false) == true) {
                buttonToggleNotifications.text = "Увімкнути сповіщення"
                    } else {
                    Toast.makeText(this, "Не вдалося, спробуйте пізніше", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                if (db.statusUpdate(false)) {
                    buttonToggleNotifications.text = "Вимкнути сповіщення"
                } else {
                    Toast.makeText(this, "Не вдалося, спробуйте пізніше", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }


    }


}
