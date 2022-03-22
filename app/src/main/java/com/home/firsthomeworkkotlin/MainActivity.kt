package com.home.firsthomeworkkotlin

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

import android.widget.TextView
import com.home.firsthomeworkkotlin.datasource.MyDataClass

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val myData = MyDataClass(1, "Москва")
        val mySecondData = myData.copy()
        mySecondData.town="Москва"
        mySecondData.weather=9
        val textView:TextView = findViewById(R.id.textView)
        val button:Button = findViewById(R.id.btnPush)
        button.setOnClickListener{
            textView.text = "Город: "+mySecondData.town+"\nТемпература: "+mySecondData.weather+" градусов!"

        }
    }




}


