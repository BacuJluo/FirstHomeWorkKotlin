package com.home.firsthomeworkkotlin.lesson6

import android.app.IntentService
import android.content.Intent
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.home.firsthomeworkkotlin.utlis.*
import java.lang.Thread.sleep

class MainService(val name:String = "") : IntentService(name) {

    override fun onHandleIntent(intent: Intent?) {
        Log.d("@@@", "work MainService")//после создания сервис получает привет от активити
        intent?.let {
            val extra = it.getStringExtra(KEY_BUNDLE_ACTIVITY_MESSAGE)
            Log.d("@@@", "work MainService $extra")
            sleep(1000L)
            val message = Intent(KEY_WAVE)//создает ответ (Сервис) на волне myAction
            message.putExtra(KEY_BUNDLE_SERVICE_MESSAGE, "привет активити, и тебе всего хорошего")
            sendBroadcast(message)//отправляет сообщение на глобальный приемник
             //LocalBroadcastManager.getInstance(this).sendBroadcast(message)//это если мы передаем Локально сообщение
        }
    }
}