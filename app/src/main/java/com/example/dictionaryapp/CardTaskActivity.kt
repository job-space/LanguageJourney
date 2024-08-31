package com.example.dictionaryapp

import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.*

class CardTaskActivity : AppCompatActivity() {

    private lateinit var mainLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_card_task)

        val db = DbHelper(this, null)

        var numTest = 0 // number of tests passed
        var numTestTrue = 0 // number of correct answers

        mainLayout = findViewById(R.id.layoutContent)
        val itemLayout = layoutInflater.inflate(R.layout.list_item_with_button, mainLayout, false) as LinearLayout



        val navigationView: ImageButton = findViewById(R.id.practice_button)

        navigationView.setOnClickListener {
            val intent = Intent(this, PracticeActivity::class.java)
            startActivity(intent)
        }


        // Receiving data from Intent
        var level = (intent.getStringExtra("EXTRA_TEXT")).toString()

        var wordsList: List<Words> = emptyList()

        if (level.equals("favorites")) {
            wordsList = db.getFavoriteWords()
        } else if (level.equals("all_words")) {
            wordsList = db.getAllWords()
        } else {
            wordsList = db.getWordsForTest(level)
        }

        var numQuestion = 0

        fun wordsTasks() {
            val randomValues = wordsList.shuffled().take(3)

            val answerText: TextView = findViewById(R.id.textAnswer)

            val wordButton1: Button = findViewById(R.id.buttonWord1)
            val wordButton2: Button = findViewById(R.id.buttonWord2)
            val wordButton3: Button = findViewById(R.id.buttonWord3)

            val resultAnswer: LinearLayout = findViewById(R.id.itemResult)
            val resultButton: Button = findViewById(R.id.buttonResult)
            val resultText: TextView = findViewById(R.id.textResult)

            // List of indexes to mix
            val randomNums = listOf(0, 1, 2).shuffled()

            // Assign text values from Words objects
            answerText.text = randomValues[0].translation
            wordButton1.text = randomValues[randomNums[0]].word
            wordButton2.text = randomValues[randomNums[1]].word
            wordButton3.text = randomValues[randomNums[2]].word

            // Resetting the status of results
            resultAnswer.visibility = View.GONE
            resultAnswer.setBackgroundColor(Color.TRANSPARENT)
            resultText.text = ""

            // Checkbox to track the selection
            var isAnswered = false

            // Function for processing button presses
            fun checkAnswer(selectedButton: Button) {
                if (!isAnswered) {
                    isAnswered = true
                    numTest++
                    if (selectedButton.text.toString() == randomValues[0].word) {
                        resultAnswer.visibility = View.VISIBLE
                        resultAnswer.setBackgroundColor(Color.parseColor("#1C9D22"))
                        resultText.text = "Правильно"

                        numTestTrue++

                        // Audio playback
                        MediaPlayer.create(this, R.raw.correct).start()

                    } else {
                        resultAnswer.visibility = View.VISIBLE
                        resultAnswer.setBackgroundColor(Color.parseColor("#E60D0D"))
                        resultText.text = "Невірно: ${randomValues[0].word}"

                        // Audio playback
                        MediaPlayer.create(this, R.raw.error).start()
                    }

                    // Voice the correct answer
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(1000) // Delay by 1 second
                        setupSoundButton(itemLayout, randomValues[0])
                    }


                    resultButton.visibility = View.VISIBLE
                    resultButton.setOnClickListener {
                        numQuestion += 1
                        if (numQuestion < 15) {
                            // Reset the state of the buttons before the next task
                            wordButton1.isEnabled = true
                            wordButton2.isEnabled = true
                            wordButton3.isEnabled = true

                            isAnswered = false
                            resultButton.visibility = View.GONE
                            wordsTasks()
                        } else {
                            val textToSendResult = "$numTestTrue/$numTest"
                            val textToSendLevel = "$level"
                            val intent = Intent(this, ResultActivity::class.java).apply {
                                putExtra("EXTRA_TEXT_RESULT", textToSendResult)
                                putExtra("EXTRA_TEXT_LEVEL", textToSendLevel)
                            }
                            startActivity(intent)
                        }
                    }
                }
            }

            wordButton1.setOnClickListener { checkAnswer(wordButton1) }
            wordButton2.setOnClickListener { checkAnswer(wordButton2) }
            wordButton3.setOnClickListener { checkAnswer(wordButton3) }
        }


        // Launching the first task
        wordsTasks()



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // Play the sound of words
    private fun setupSoundButton(itemLayout: View, todo: Words) {
        val word = todo.word.toString().lowercase().replace(" ", "")
        val resId = resources.getIdentifier(word, "raw", packageName)

        // Audio playback
        playSound(resId, word)

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
