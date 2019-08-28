package com.example.audiorecorder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class musicactivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_musicactivity)

        val intent = intent
        intent.getIntExtra("position",0)

    }
}
