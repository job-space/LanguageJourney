package com.example.dictionaryapp

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.view.animation.AnimationUtils


class ResultActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_result)


        val nextButton: Button = findViewById(R.id.buttonNext)
        val finishButton: Button = findViewById(R.id.buttonFinish)
        val resultScoreText: TextView = findViewById(R.id.textResult)
        val yourResultText: TextView = findViewById(R.id.textYourResult)
        val resultItemNum = (intent.getStringExtra("EXTRA_TEXT_RESULT")).toString()
        val levelItemNum = (intent.getStringExtra("EXTRA_TEXT_LEVEL"))

        // Audio playback
        MediaPlayer.create(this, R.raw.finalsound).start()

        // Appearance animation
        val animation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        yourResultText.startAnimation(animation)
        resultScoreText.startAnimation(animation)
        nextButton.startAnimation(animation)
        finishButton.startAnimation(animation)


        resultScoreText.text = resultItemNum


        nextButton.setOnClickListener {
            val intent = Intent(this, CardTaskActivity::class.java).apply {
                putExtra("EXTRA_TEXT", levelItemNum)
            }
            startActivity(intent)
        }




        finishButton.setOnClickListener {
            val intent = Intent(this, PracticeActivity::class.java)
            startActivity(intent)
        }



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}