package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class FirstPageActivity : AppCompatActivity() {
    private var first_tv: TextView? = null
    private var second_tv: TextView? = null
    private var main_bt: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firstpage)
        initializeViews()
        setOnClickListeners()
    }

    fun initializeViews() {
        first_tv = findViewById(R.id.tv_first_text)
        second_tv = findViewById(R.id.tv_second_text)
        main_bt = findViewById(R.id.btn_main_button)
    }

    private fun setOnClickListeners() {
        main_bt!!.setOnClickListener { toLoginActivity() }
    }

    fun toLoginActivity() {
        val intent = Intent(this, FirebaseLoginActivity::class.java)
        startActivity(intent)
    }
}