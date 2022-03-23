package com.home.firsthomeworkkotlin

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button

import android.widget.TextView
import com.home.firsthomeworkkotlin.datasource.MyDataClass
import com.home.firsthomeworkkotlin.datasource.MyTestObject

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init ()

    }

    fun init (){
        val myData = MyDataClass(1, "Москва")
        val mySecondData = myData.copy()
        mySecondData.town="Москва"
        mySecondData.weather=0

        val textView:TextView = findViewById(R.id.textView)
        val button:Button = findViewById(R.id.btnPush)
        button.setOnClickListener{
            textView.text = "Город: "+mySecondData.town+"\nТемпература: "+mySecondData.weather+" градусов!"
            textView.append("\n"+MyTestObject.getMyTest())
            MyTestObject.getTestForEach()
        }


    }


}






