package com.home.firsthomeworkkotlin.view.details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.home.firsthomeworkkotlin.databinding.FragmentDetailsBinding
import com.home.firsthomeworkkotlin.repository.Weather
import com.home.firsthomeworkkotlin.repository.yandexdto.Part
import com.home.firsthomeworkkotlin.repository.yandexdto.WeatherDTO
import com.home.firsthomeworkkotlin.utlis.KEY_BUNDLE_WEATHER
import com.home.firsthomeworkkotlin.viewmodel.DetailsState
import com.home.firsthomeworkkotlin.viewmodel.DetailsViewModel

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding: FragmentDetailsBinding
        get() {
            return _binding!!
        }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        /*LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiver)*/ //Снятие регистрации броадкастресивера (локального)
    }

    //Приемник
    /*private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                intent.getParcelableExtra<WeatherDTO>(KEY_BUNDLE_SERVICE_BROADCAST_WEATHER)?.let{
                    Log.d("@@@", "MyBroadcastReceiver onReceive $it")
                    onResponse(it)
                }
            }
        }
    }*/

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        //return inflater.inflate(R.layout.fragment_main, container, false)
        return binding.root
    }

    private val viewModel: DetailsViewModel by lazy{
        ViewModelProvider(this).get(DetailsViewModel::class.java) //ПОТОКОБЕЗОПАСНЫЙ
    }

    //private lateinit var currentCityName: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getLiveData().observe(viewLifecycleOwner) { t -> renderData(t) }


        /*LocalBroadcastManager.getInstance(requireContext()).registerReceiver(receiver,
            IntentFilter(KEY_WAVE_SERVICE_BROADCAST))*/ //Регистрация сервиса БроадКаст Ресивер (Локальный)

        //если агрументы не null то мы их передаем в renderData
        arguments?.getParcelable<Weather>(KEY_BUNDLE_WEATHER)?.let {
//            currentCityName = it.city.name

            viewModel.getWeather(it.city)

            /*Thread{
            WeatherLoader(this@DetailsFragment,view).loadWeather(it.city.lat, it.city.lon)
            }.start()
            requireActivity().startService(Intent(requireContext(),DetailsService::class.java).apply {
                putExtra(KEY_BUNDLE_LAT,it.city.lat)
                putExtra(KEY_BUNDLE_LON,it.city.lon)
                putExtra(KEY_BUNDLE_CITY,it.city.name)
            })*/
            //getWeather(it.city.lat,it.city.lon)


        }


    }

    /*private fun getWeather(lat:Double,lon:Double){
        binding.loadingLayout.visibility = View.VISIBLE //renderData(Loading) - сделать состояние загрузки

        val client = OkHttpClient()
        val builder = Request.Builder()
        builder.addHeader(YANDEX_API_KEY, BuildConfig.WEATHER_API_KEY_FIRST)
        builder.url("$YANDEX_DOMAIN${YANDEX_ENDPOINT}lat=$lat&lon=$lon")
        val request = builder.build()
        val callback: Callback = object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                TODO("renderData() Если ошибка")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful){
                    val weatherDTO:WeatherDTO = Gson().fromJson(response.body()!!.string(),WeatherDTO::class.java)
                    requireActivity().runOnUiThread {
                        renderData(weatherDTO)
                    }
                }else{
                    //TODO HW response.isRedirect
                }
            }
        }

        val call = client.newCall(request)
        call.enqueue(callback) //enqueue Выполнить в очереди ассинхронный способ запроса для него нужен Callback
        Thread{
            val response = call.execute() //execute Выполнить здесь и сейчас, синхронный способ запроса
            if (response.isSuccessful){

                val weatherDTO:WeatherDTO = Gson().fromJson(response.body()!!.string(),WeatherDTO::class.java)
                requireActivity().runOnUiThread {
                    renderData(weatherDTO)
                }
            }else{
                //TODO HW response.isRedirect
            }
        }.start()
    }*/


    private fun renderData(detailsState: DetailsState) {
        when(detailsState){
            is DetailsState.Error -> {

            }
            DetailsState.Loading -> {

            }
            is DetailsState.Success -> {
                val weather = detailsState.weather

                with(binding) {
                    loadingLayout.visibility = View.GONE
                    cityName.text = weather.city.name
//                    dayTimeValue(weather.parts[0])
//                    seasonValue(weather)
//                    conditionValue(weather)
                    temperatureValue.text = weather.temperature.toString()
                    feelsLikeValue.text = weather.feelsLike.toString()
                    Log.d("@@@","${weather.city.lat} ${weather.city.lon}")
                    cityCoordinates.text = "${weather.city.lat} ${weather.city.lon}"

                    //"${weather.city.lat} ${weather.city.lon}".apply { cityCoordinates.text = this }
                    /*//Toast.makeText(requireContext(),"РАБОТАЕТ",Toast.LENGTH_SHORT).show()
                    //mainView.withOutAction(getString(R.string.success))
                    mainView.snackBarWithAction(
                        getString(R.string.error), getString(R.string.try_again), { sentRequest() },
                        Snackbar.LENGTH_LONG
                    )*/
                }
            }
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

//    override fun onResponse(weatherDTO: WeatherDTO) {
//        renderData(weatherDTO)
//    }

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


