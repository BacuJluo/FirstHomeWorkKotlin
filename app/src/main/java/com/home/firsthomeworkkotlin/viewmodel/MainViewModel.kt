package com.home.firsthomeworkkotlin.viewmodel

import android.view.View
import android.widget.RadioButton
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.home.firsthomeworkkotlin.R
import com.home.firsthomeworkkotlin.repository.Repository
import com.home.firsthomeworkkotlin.repository.RepositoryImplementation

class MainViewModel(
    private val liveData: MutableLiveData<AppState> = MutableLiveData(),
    private val repository: Repository = RepositoryImplementation()
) : ViewModel() {

    private val SOURCE_LOCAL:Int = 1
    private val SOURCE_SERVER:Int = 2

    fun getData(): LiveData<AppState> {
        return liveData
    }

    fun getWeather() {
        Thread {
            liveData.postValue(AppState.Loading)
            if ((0..10).random() > 5) {
                //после обновления liveData автоматически рассылает всем своим слушателям ее данные
                val answerLocale = repository.getWeatherFromLocalStorage()

                liveData.postValue(AppState.Success(answerLocale))
            }
            else {
                liveData.postValue(AppState.Error(IllegalAccessError()))

            }
        }.start()
    }

    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton){
            val checked = view.isChecked

            when(view.getId()){
                R.id.sourceServer ->
                    if (checked){

                    }
                R.id.sourceLocal ->
                    if (checked){

                    }
            }
        }
    }

}