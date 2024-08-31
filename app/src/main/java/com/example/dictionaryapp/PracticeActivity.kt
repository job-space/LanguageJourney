package com.example.dictionaryapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class PracticeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_practice)

        val navigationView: ImageButton = findViewById(R.id.main_menu_button)

        val favoritesItem: Button = findViewById(R.id.item_favorites)
        val beginnerItem: Button = findViewById(R.id.item_beginner)
        val intermediateItem: Button = findViewById(R.id.item_intermediate)
        val advancedItem: Button = findViewById(R.id.item_advanced)
        val allWordsItem: Button = findViewById(R.id.item_all_words)


        navigationView.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val db = DbHelper(this, null)


        favoritesItem.setOnClickListener {
            val wordsList = db.getFavoriteWords()
            if (wordsList.size < 3) {
                /////// temporary solution for favourites
                Toast.makeText(this@PracticeActivity, "Оберіть від трьох обраних слів", Toast.LENGTH_SHORT).show()

            } else {
                val textToSend = "favorites"
                val intent = Intent(this, CardTaskActivity::class.java).apply {
                    putExtra("EXTRA_TEXT", textToSend)
                }
                startActivity(intent)
            }
        }

        beginnerItem.setOnClickListener {
            val textToSend = "1"
            val intent = Intent(this, CardTaskActivity::class.java).apply {
                putExtra("EXTRA_TEXT", textToSend)
            }
            startActivity(intent)
        }

        intermediateItem.setOnClickListener {
            val textToSend = "2"
            val intent = Intent(this, CardTaskActivity::class.java).apply {
                putExtra("EXTRA_TEXT", textToSend)
            }
            startActivity(intent)
        }

        advancedItem.setOnClickListener {
            val textToSend = "3"
            val intent = Intent(this, CardTaskActivity::class.java).apply {
                putExtra("EXTRA_TEXT", textToSend)
            }
            startActivity(intent)
        }

        allWordsItem.setOnClickListener {
            val textToSend = "all_words"
            val intent = Intent(this, CardTaskActivity::class.java).apply {
                putExtra("EXTRA_TEXT", textToSend)
            }
            startActivity(intent)
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.practice_activity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
