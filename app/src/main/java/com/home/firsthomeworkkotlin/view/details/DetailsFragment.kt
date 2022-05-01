package com.home.firsthomeworkkotlin.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.home.firsthomeworkkotlin.R
import com.home.firsthomeworkkotlin.databinding.FragmentDetailsBinding
import com.home.firsthomeworkkotlin.datasource.Weather
import com.home.firsthomeworkkotlin.repository.*
import com.home.firsthomeworkkotlin.utlis.KEY_BUNDLE_WEATHER

class DetailsFragment : Fragment(), OnServerResponse {

    private var _binding: FragmentDetailsBinding? = null
    private val binding: FragmentDetailsBinding
        get() {
            return _binding!!
        }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        //return inflater.inflate(R.layout.fragment_main, container, false)
        return binding.root
    }

    lateinit var currentCityName: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //если агрументы не null то мы их передаем в renderData
        arguments?.getParcelable<Weather>(KEY_BUNDLE_WEATHER)?.let {
            currentCityName = it.city.name
            //Thread{
                WeatherLoader(this@DetailsFragment,view).loadWeather(it.city.lat, it.city.lon)
            //}.start()
        }
    }


    private fun renderData(weather: WeatherDTO) {
        with(binding) {
            loadingLayout.visibility = View.GONE
            cityName.text = currentCityName
            dayTimeValue(weather.forecast.parts[0])
            seasonValue(weather)
            conditionValue(weather)
            temperatureValue.text = weather.fact.temperature.toString()
            feelsLikeValue.text = weather.fact.feelsLike.toString()

            "${weather.info.lat} ${weather.info.lon}".apply { cityCoordinates.text = this }
            //Toast.makeText(requireContext(),"РАБОТАЕТ",Toast.LENGTH_SHORT).show()
            //mainView.withOutAction(getString(R.string.success))
            /*mainView.snackBarWithAction(
                getString(R.string.error), getString(R.string.try_again), { sentRequest() },
                Snackbar.LENGTH_LONG
            )*/
        }
    }

    private fun conditionValue(weather: WeatherDTO) {
        when(weather.fact.condition){
            "clear" -> binding.conditionValue.text = "ясно"
            "partly-cloudy" -> binding.conditionValue.text = "малооблачно"
            "cloudy" -> binding.conditionValue.text = "облачно с прояснениями"
            "overcast" -> binding.conditionValue.text = "пасмурно"
            "drizzle " -> binding.conditionValue.text = "морось"
            "light-rain " -> binding.conditionValue.text = "небольшой дождь"
            "rain " -> binding.conditionValue.text = "дождь"
            "moderate-rain" -> binding.conditionValue.text = "умеренно сильный дождь"
            "heavy-rain" -> binding.conditionValue.text = "сильный дождь"
            "continuous-heavy-rain" -> binding.conditionValue.text = " длительный сильный дождь"
            "showers" -> binding.conditionValue.text = "ливень"
            "wet-snow" -> binding.conditionValue.text = "дождь со снегом"
            "light-snow" -> binding.conditionValue.text = "небольшой снег"
            "snow" -> binding.conditionValue.text = "снег"
            "snow-showers" -> binding.conditionValue.text = "снегопад"
            "hail" -> binding.conditionValue.text = "град"
            "thunderstorm" -> binding.conditionValue.text = "гроза"
            "thunderstorm-with-rain" -> binding.conditionValue.text = "дождь с грозой"
            "thunderstorm-with-hail" -> binding.conditionValue.text = "гроза с градом"
        }
        binding.conditionValue.textSize = 20f
    }

    private fun seasonValue(weather: WeatherDTO){
        when (weather.fact.season) {
            "summer" -> binding.seasonValue.text = "Лето"
            "autumn" -> binding.seasonValue.text = "Осень"
            "winter" -> binding.seasonValue.text = "Зима"
            "spring" -> binding.seasonValue.text = "Весна"
        }
        binding.seasonValue.textSize = 20f
    }

    private fun dayTimeValue(part: Part){
        when (part.partName){
            "night" -> binding.dayTimeValue.text = "Ночь"
            "morning" -> binding.dayTimeValue.text = "Утро"
            "day" -> binding.dayTimeValue.text = "День"
            "evening" -> binding.dayTimeValue.text = "Вечер"
        }
        binding.dayTimeValue.textSize = 20f
    }

    private fun sentRequest() {

    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): DetailsFragment {
            val fragment = DetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onResponse(weatherDTO: WeatherDTO) {
        renderData(weatherDTO)

    }
}

//extension функция от Андрея
//функция расширяет Snackbar
/*private fun View.snackBarWithAction(
    text: String,
    actionText: String,
    action: (View) -> Unit,
    length: Int = Snackbar.LENGTH_INDEFINITE
) {
    Snackbar.make(this, text, length).setAction(actionText, action).show()
}*/

private fun View.withOutAction(text: String, length: Int = Snackbar.LENGTH_LONG) {
    Snackbar.make(this, text, length).show()
}


