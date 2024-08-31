package com.example.dictionaryapp


import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.EditText
import android.widget.TextView
import android.text.Editable
import android.text.TextWatcher
import androidx.core.view.GravityCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.view.View
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatDelegate
import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import android.util.TypedValue
import androidx.core.content.ContextCompat
import java.util.Calendar


val buttonIds = listOf(
    R.id.searchAButton, R.id.searchBButton, R.id.searchCButton, R.id.searchDButton, R.id.searchEButton,
    R.id.searchFButton, R.id.searchGButton, R.id.searchHButton, R.id.searchIButton, R.id.searchJButton,
    R.id.searchKButton, R.id.searchLButton, R.id.searchMButton, R.id.searchNButton, R.id.searchOButton,
    R.id.searchPButton, R.id.searchQButton, R.id.searchRButton, R.id.searchSButton, R.id.searchTButton,
    R.id.searchUButton, R.id.searchVButton, R.id.searchWButton, R.id.searchXButton, R.id.searchYButton,
    R.id.searchZButton
)


class MainActivity : AppCompatActivity() {


    private lateinit var db: DbHelper
    private lateinit var mainLayout: LinearLayout
    private lateinit var favoriteButtonUp: ImageButton
    private var currentLetter: String? = null

    private lateinit var notificationManager: NotificationManager

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {

        // Applying a saved theme before calling super.onCreate
        if (ThemePreference.isNightMode(this)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Notifications
        setDailyAlarm()

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val menuButton = findViewById<ImageButton>(R.id.menu_button)


        // Menu
        menuButton.setOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }





        val navigationView: NavigationView = findViewById(R.id.navigation_view)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_main -> {
                    // Handling clicks on the dictionary menu
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_practice -> {
                    // Handling clicks on the practice menu

                    val intent = Intent(this, PracticeActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_settings -> {
                    // Handling clicks on the setup menu
                    val intent = Intent(this, SettingsActivity::class.java)
                    startActivity(intent)
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }










        db = DbHelper(this, null)
        mainLayout = findViewById(R.id.list_words)
        favoriteButtonUp = findViewById(R.id.favorite_button_up)

        setupFavoriteButton()
        dictionaryMain()
        setupSearchButtons()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawer_layout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        // Search for a word in real time
        val editTextInput: EditText = findViewById(R.id.edit_text_input)
        editTextInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Required actions after changing the text
                val wordsList = db.getSearchWords(s.toString())
                mainLayout.removeAllViews()
                for (word in wordsList) {
                    val itemLayout = layoutInflater.inflate(R.layout.list_item_with_button, mainLayout, false) as LinearLayout

                    val titleTextView: TextView = itemLayout.findViewById(R.id.text_word)
                    val subtitleTextView: TextView = itemLayout.findViewById(R.id.text_translation)
                    val starButton: ImageButton = itemLayout.findViewById(R.id.button_star)

                    titleTextView.text = word.word
                    subtitleTextView.text = word.translation
                    starButton.setImageResource(if (word.favorite) android.R.drawable.btn_star_big_on else android.R.drawable.btn_star_big_off)

                    setupSoundButton(itemLayout, word)

                    starButton.setOnClickListener {
                        val newFavorite = db.changeFavorite(word.id, starButton)
                        starButton.setImageResource(if (newFavorite) android.R.drawable.btn_star_big_on else android.R.drawable.btn_star_big_off)
                    }

                    mainLayout.addView(itemLayout)
                }
            }


            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Steps to take before changing the text
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Steps to take when changing text
            }
        })

    }













    // search by button
    private fun setupSearchButtons() {

        for (buttonId in buttonIds) {
            val button = findViewById<Button>(buttonId)

            button.setOnClickListener {
                val letter = button.text.toString()


                // Get colours to match the current theme
                val typedValue = TypedValue()
                val theme = theme
                theme.resolveAttribute(R.attr.colorSecondary, typedValue, true)
                val colorPrimary = ContextCompat.getColor(this, typedValue.resourceId)
                theme.resolveAttribute(R.attr.colorPrimary, typedValue, true)
                val colorSecondary = ContextCompat.getColor(this, typedValue.resourceId)




                // Before setting a new button colour, restore all buttons to their default state
                for (id in buttonIds) {
                    val otherButton = findViewById<Button>(id)
                    otherButton.backgroundTintList = ColorStateList.valueOf(colorPrimary)
                    otherButton.setTextColor(colorSecondary)
                }

                if (currentLetter == letter) {
                    button.backgroundTintList = ColorStateList.valueOf(colorPrimary)
                    button.setTextColor(colorSecondary)

                    dictionaryMain()
                } else {
                    button.backgroundTintList = ColorStateList.valueOf(colorSecondary)
                    button.setTextColor(colorPrimary)

                    displayWordsStartingWith(letter)
                }
            }
        }
    }



    private fun displayWordsStartingWith(letter: String) {
        currentLetter = letter
        mainLayout.removeAllViews()

        val wordsList = db.getSearchWordsLetter(letter)

            //    val itemLayout = layoutInflater.inflate(R.layout.list_item_with_button, mainLayout, false) as LinearLayout
        val starButton: ImageButton = findViewById(R.id.favorite_button_up)
        starButton.setImageResource(android.R.drawable.btn_star_big_off)

        for (word in wordsList) {
                val itemLayout = layoutInflater.inflate(R.layout.list_item_with_button, mainLayout, false) as LinearLayout

                val titleTextView: TextView = itemLayout.findViewById(R.id.text_word)
                val subtitleTextView: TextView = itemLayout.findViewById(R.id.text_translation)
                val starButton: ImageButton = itemLayout.findViewById(R.id.button_star)

                titleTextView.text = word.word
                subtitleTextView.text = word.translation
                starButton.setImageResource(if (word.favorite) android.R.drawable.btn_star_big_on else android.R.drawable.btn_star_big_off)

                setupSoundButton(itemLayout, word)

                starButton.setOnClickListener {
                    val newFavorite = db.changeFavorite(word.id, starButton)
                    starButton.setImageResource(if (newFavorite) android.R.drawable.btn_star_big_on else android.R.drawable.btn_star_big_off)
                }

                mainLayout.addView(itemLayout)

        }
    }



    private fun setupFavoriteButton() {
        favoriteButtonUp.setOnClickListener {
            displayFavoriteWords()
        }
    }

    // Display the main dictionary on the main page
    private fun dictionaryMain() {
        currentLetter = null
        mainLayout.removeAllViews()
        favoriteButtonUp.setImageResource(android.R.drawable.btn_star_big_off)


        var todos: List<Words> = db.getAllWords()

        if(todos.isEmpty()) {
            // Filling in the database
            db.dictionaryFull()
            todos = db.getAllWords()
        }

        for (todo in todos) {
            val itemLayout = layoutInflater.inflate(R.layout.list_item_with_button, mainLayout, false) as LinearLayout

            val titleTextView: TextView = itemLayout.findViewById(R.id.text_word)
            val subtitleTextView: TextView = itemLayout.findViewById(R.id.text_translation)
            val starButtonItem: ImageButton = itemLayout.findViewById(R.id.button_star)


            setupSoundButton(itemLayout, todo)




            titleTextView.text = todo.word
            subtitleTextView.text = todo.translation

            if (todo.favorite) {
                starButtonItem.setImageResource(android.R.drawable.btn_star_big_on)
            }





            starButtonItem.setOnClickListener {
                val newFavorite = db.changeFavorite(todo.id, starButtonItem)
                if (newFavorite) {
                    starButtonItem.setImageResource(android.R.drawable.btn_star_big_on)
                } else {
                    starButtonItem.setImageResource(android.R.drawable.btn_star_big_off)
                }
            }

            mainLayout.addView(itemLayout)
        }
    }

    // Transition page with selected words
    private fun displayFavoriteWords() {
        mainLayout.removeAllViews()

        val buttonFavoriteUp: ImageButton = findViewById(R.id.favorite_button_up)
        buttonFavoriteUp.setImageResource(android.R.drawable.btn_star_big_on)

        val favoriteWords = db.getFavoriteWords()

        for (word in favoriteWords) {
            val itemLayout = layoutInflater.inflate(R.layout.list_item_with_button, mainLayout, false) as LinearLayout

            val titleTextView: TextView = itemLayout.findViewById(R.id.text_word)
            val subtitleTextView: TextView = itemLayout.findViewById(R.id.text_translation)
            val starButton: ImageButton = itemLayout.findViewById(R.id.button_star)

            titleTextView.text = word.word
            subtitleTextView.text = word.translation
            starButton.setImageResource(android.R.drawable.btn_star_big_on)

            setupSoundButton(itemLayout, word)

            starButton.setOnClickListener {
                val newFavorite = db.changeFavorite(word.id, starButton)
                starButton.setImageResource(if (newFavorite) android.R.drawable.btn_star_big_on else android.R.drawable.btn_star_big_off)
            }

            mainLayout.addView(itemLayout)
        }

        // Add a handler to return to the main page after clicking on the button
        favoriteButtonUp.setOnClickListener {
            dictionaryMain()
            setupFavoriteButton()
        }



    }


    private fun setDailyAlarm() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, NotificationReceiver::class.java)
        // Create a PendingIntent to receive a broadcast
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            intent,
            PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )



        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 10)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        // If the alarm time has already expired for today, set it for tomorrow
        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }



        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    // Play the sound of words
    private fun setupSoundButton(itemLayout: View, todo: Words) {
        val soundWordButton: ImageButton = itemLayout.findViewById(R.id.button_sound)
        val word = todo.word.toString().lowercase().replace(" ", "")
        val resId = resources.getIdentifier(word, "raw", packageName)

        // Audio playback
        soundWordButton.setOnClickListener {
            playSound(resId, word)
        }
    }

    private fun playSound(resId: Int, word: String) {
        if (resId != 0) { // Check if the resource exists
            val mediaPlayer = MediaPlayer.create(this, resId)
            mediaPlayer.start()
        } else {
            // Additional processing if the resource is not found
            Log.e("Audio", "Audio resource not found for word: $word")
        }
    }
}
