package com.home.firsthomeworkkotlin.view.weatherlist

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.home.firsthomeworkkotlin.R
import com.home.firsthomeworkkotlin.databinding.FragmentWeatherListBinding
import com.home.firsthomeworkkotlin.utlis.KEY_BUNDLE_WEATHER
import com.home.firsthomeworkkotlin.view.details.DetailsFragment
import com.home.firsthomeworkkotlin.viewmodel.AppState
import com.home.firsthomeworkkotlin.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import com.home.firsthomeworkkotlin.repository.City
import com.home.firsthomeworkkotlin.repository.Weather

class WeatherListFragment : Fragment(),OnItemListClickListener {

    /*Зануление Binding, для того что бы не посылался запрос в пустоту и не создавались
    из-за этого Зомби*/
    private var _binding: FragmentWeatherListBinding? = null
    private val binding: FragmentWeatherListBinding get() = _binding!!

    private val adapter = WeatherListAdapter(this)

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        /*надуваем макет через xml
        return inflater.inflate(R.layout.fragment_main, container, false)
        но этот метод не очень эффективен, так что используем binding*/
        _binding = FragmentWeatherListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initRecycler()
        setupFabLocation()
        //initRecyclerView() Не работает

        /*//binding.btnOne.setOnClickListener { }
        одинаковые методы, но есть отличия. Если делать через binding то отсеиваются
        NullPointerExeption
        view.findViewById<Button>(R.id.btnOne).setOnClickListener {  }*/
    }

    private fun setupFabLocation(){
        binding.mainFragmentFABLocation.setOnClickListener {
            checkPermission()
        }
    }

    private fun checkPermission(){
        //Проверка разрешения на получение ГеоЛокации
        if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            getLocation()
        }else if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)){
            explain()
        } else {
            mRequestPermission()
        }
    }

    private fun explain() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.dialog_rationale_title))
            .setMessage(getString(R.string.dialog_rationale_message))
            .setPositiveButton(getString(R.string.dialog_rationale_give_access)) { _, _ ->
                mRequestPermission()
            }
            .setNegativeButton(getString(R.string.dialog_rationale_decline)) { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE){
            for (i in permissions.indices){
                if (permissions[i] == Manifest.permission.ACCESS_FINE_LOCATION && grantResults[i] == PackageManager.PERMISSION_GRANTED){
                    getLocation()
                } else {
                    explain()
                }
            }
        }else{
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private val REQUEST_CODE = 998
    private fun mRequestPermission() {
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),REQUEST_CODE)
    }

    //Получение адреса по местоположению
    fun getAddressByLocation(location: Location){
        val geocoder = Geocoder(requireContext())
        val timeStump = System.currentTimeMillis()
        Thread{
            val textAddress = geocoder.getFromLocation(location.latitude,location.latitude,1000000)[1].getAddressLine(0)
            requireActivity().runOnUiThread { //Передача из вспомогательного потока в Главный поток
                showAddressDialog(textAddress,location)
                Log.d("@@@",textAddress + location)
            }

        }.start()

        Log.d("@@@", "Прошло ${System.currentTimeMillis() - timeStump}")
    }

    private val locationListenerTime = object : LocationListener{
        override fun onLocationChanged(location: Location) {
            Log.d("@@@",location.toString())
            getAddressByLocation(location)
        }

        override fun onProviderDisabled(provider: String) {
            super.onProviderDisabled(provider)
        }
    }

    private val locationListenerDistance = object : LocationListener{
        override fun onLocationChanged(location: Location) {
            Log.d("@@@",location.toString())
            getAddressByLocation(location)
        }

        override fun onProviderDisabled(provider: String) {
            super.onProviderDisabled(provider)
        }

    }

    //ПОЛУЧЕНИЕ ГЕОПОЛОЖЕНИЯ (КООРДИНАТЫ)
    @SuppressLint("MissingPermission")
    private fun getLocation() {
        context?.let {
            //Явное получение LocationManager через as LocationManager
            val locationManager = it.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            //Проверяем включен ли GPS_PROVIDER
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                val providerGPS = locationManager.getProvider(LocationManager.GPS_PROVIDER) //Можно использовать getBestProvider
                /*providerGPS?.let {
                    locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        10000L,
                        0f,
                        locationListenerTime
                    //Обновляется по времени (10Сек) Друг друга не перезаписывают
                    )
                }*/
                //Можно вынести в отдельные методы
                providerGPS?.let {
                    locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        0L,
                        100f,
                        locationListenerDistance
                    //Обновляется по прохождению дистанции (100м) Друг друга не перезаписывают
                    )
                }
            }
        }

    }

    private var isRussian = true

    private fun initViewModel() {
        /*создание ссылки на ViewModel
        Даже если фрагмент умрет то ViewModel будет жить, и не будет посылать фрагменту запросы
        И если Фрагмент заново пересоздастся, то ViewModel нет, а просто вернет ее по прошлому запросу от ранее созданного фрагмента*/
        val viewModel:MainViewModel by lazy{
            ViewModelProvider(this).get(MainViewModel::class.java) //ПОТОКОБЕЗОПАСНЫЙ
        }
        //Callback лайвдэйты
        //меняем Any на собственный AppState.class
        val observer =  { data:AppState -> renderData(data) }
        /*Обращение к LiveData, чтоб подписала фрагмент как слушателя на LiveData, ориентируясь на его жизненный цикл
        Фрагмент будет слушателем LiveData до тех пор пока не умрет, в этом случае LiveData
        уже не будет отправлять в пустоту свои данные. В Callback observer будут рассылаться обновления LiveData*/
        viewModel.getData().observe(viewLifecycleOwner, observer)
        binding.floatingActionButton.setOnClickListener {
            isRussian = !isRussian
            //TODO HW Сделать через SharedPreferences состояние кнопки Россия или Мировые города
            if (isRussian) {
                viewModel.getWeatherRussian()
                binding.floatingActionButton.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_russia
                    ))
            } else {
                binding.floatingActionButton.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_earth
                    ))
                viewModel.getWeatherWorld()
            }
        }
        viewModel.getWeatherRussian()
    }

    //Так работает
    private fun initRecycler() {
        binding.recyclerView.adapter = adapter
    }

    //Так не работает приходит savedInstanceState = null
    private fun initRecyclerView() {
        binding.recyclerView.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(requireContext())
        }

    }

    private fun renderData(data: AppState) = //отрисовка data
        when (data) {
            is AppState.Error -> {
                binding.loadingLayout.visibility = View.GONE
                Snackbar.make(binding.root, "Не получилось ${data.error}", Snackbar.LENGTH_LONG)
                    .show()
            }
            is AppState.Loading -> {
                binding.loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Success -> {
                binding.loadingLayout.visibility = View.GONE
                adapter.setData(data.weatherListData)

                /*binding.cityName.text = data.weatherData.city.name
                binding.temperatureValue.text = data.weatherData.temperature.toString()
                binding.feelsLikeValue.text = data.weatherData.feelsLike.toString()
                binding.cityCoordinates.text = "${data.weatherData.city.lat} ${data.weatherData.city.lon}"
                Snackbar.make(binding.mainView, "Получилось", Snackbar.LENGTH_LONG).show()*/
            }
        }



    override fun onItemClick(weather: Weather) {
        requireActivity().supportFragmentManager.beginTransaction().add(
            R.id.container,
            DetailsFragment.newInstance(Bundle().apply {
                putParcelable(KEY_BUNDLE_WEATHER, weather)
            })
        ).addToBackStack("").commit()
    }

    companion object {
        @JvmStatic
        fun newInstance() = WeatherListFragment()
    }

    fun showAddressDialog(address: String, location:Location){
        activity?.let {
            AlertDialog.Builder(it)
                .setTitle(getString(R.string.dialog_address_title))
                .setMessage(address)
                .setPositiveButton(getString(R.string.dialog_address_get_weather)){_,_ ->
                    onItemClick(Weather(City(address,location.latitude,location.longitude)))
                }
                .setNegativeButton(getString(R.string.dialog_button_close)){
                    dialog,_ -> dialog.dismiss()
                }.create().show()
        }

    }

}

