package com.home.firsthomeworkkotlin.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.home.firsthomeworkkotlin.R
import com.home.firsthomeworkkotlin.view.main.MainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(savedInstanceState==null){ //если первый запуск
            supportFragmentManager.beginTransaction().replace(R.id.container, MainFragment.newInstance()).commit()
        }


    }


}






