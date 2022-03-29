package com.home.firsthomeworkkotlin.view.main

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.home.firsthomeworkkotlin.databinding.FragmentMainBinding
import com.home.firsthomeworkkotlin.view.MainActivity
import com.home.firsthomeworkkotlin.viewmodel.AppState
import com.home.firsthomeworkkotlin.viewmodel.MainViewModel

class MainFragment : Fragment() {

    private val NOTIFICATION_ID = 0
    private val REQUEST_CODE = 0
    private val FLAGS = 0

    /*Зануление Binding, для того что бы не посылался запрос в пустоту и не создавались
    из-за этого Зомби*/
    private  var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        /*надуваем макет через xml
        return inflater.inflate(R.layout.fragment_main, container, false)
        но этот метод не очень эффективен, так что используем binding*/
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*//binding.btnOne.setOnClickListener { }
        одинаковые методы, но есть отличия. Если делать через binding то отсеиваются
        NullPointerExeption
        view.findViewById<Button>(R.id.btnOne).setOnClickListener {  }*/

        /*создание ссылки на ViewModel
        Даже если фрагмент умрет то ViewModel будет жить, и не будет посылать фрагменту запросы
        И если Фрагмент заново пересоздастся, то ViewModel нет, а просто вернет ее по прошлому запросу от ранее созданного фрагмента*/
        val viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        //Callback лайвдэйты
        //меняем Any на собственный AppState.class
        val observer = object : Observer<AppState> {
            override fun onChanged(data: AppState) {
                renderData(data)
            }
        }
        /*Обращение к LiveData, чтоб подписала фрагмент как слушателя на LiveData, ориентируясь на его жизненный цикл
        Фрагмент будет слушателем LiveData до тех пор пока не умрет, в этом случае LiveData
        уже не будет отправлять в пустоту свои данные. В Callback observer будут рассылаться обновления LiveData*/
        viewModel.getData().observe(viewLifecycleOwner, observer)

        viewModel.getWeather()
    }

    private fun renderData(data: AppState) = //отрисовка data
        when(data){
            is AppState.Error -> {
                binding.loadingLayout.visibility = View.GONE
                //binding.cityName.text = "Не получилось ${data.error}"
                showSnackBar()
            }
            is AppState.Loading -> {
                binding.loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Success -> {
                binding.loadingLayout.visibility = View.GONE
                binding.cityName.text = data.weatherData.city.name
                binding.temperatureValue.text = data.weatherData.temperature.toString()
                binding.feelsLikeValue.text = data.weatherData.feelsLike.toString()
                binding.cityCoordinates.text = "${ data.weatherData.city.lat } ${data.weatherData.city.lon}"
                Snackbar.make(binding.mainView, "Получилось", Snackbar.LENGTH_LONG).show()
            }
        }

    private fun showSnackBar(){
        val snackbar = Snackbar.make(binding.mainView, "Не получилось", Snackbar.LENGTH_INDEFINITE)
        snackbar.setAction("Попробовать еще раз?",View.OnClickListener {
            val viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
            viewModel.getWeather()
        })
        snackbar.show()
    }



        companion object {
            fun newInstance() = MainFragment()
            const val NOTIFICATION_ID = 10
            const val CHANNEL_ID = "channelID"
        }
    
}

