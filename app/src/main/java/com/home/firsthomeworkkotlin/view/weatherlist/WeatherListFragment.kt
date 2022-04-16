package com.home.firsthomeworkkotlin.view.weatherlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.home.firsthomeworkkotlin.R
import com.home.firsthomeworkkotlin.databinding.FragmentWeatherListBinding
import com.home.firsthomeworkkotlin.repository.Weather
import com.home.firsthomeworkkotlin.utlis.KEY_BUNDLE_WEATHER
import com.home.firsthomeworkkotlin.view.MainActivity
import com.home.firsthomeworkkotlin.view.details.DetailsFragment
import com.home.firsthomeworkkotlin.viewmodel.AppState
import com.home.firsthomeworkkotlin.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar

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

    private var isRussian = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initRecycler()

        /*//binding.btnOne.setOnClickListener { }
        одинаковые методы, но есть отличия. Если делать через binding то отсеиваются
        NullPointerExeption
        view.findViewById<Button>(R.id.btnOne).setOnClickListener {  }*/


    }

    private fun initViewModel() {
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
        binding.floatingActionButton.setOnClickListener {
            isRussian = !isRussian
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

    private fun initRecycler() {
        binding.recyclerView.adapter = adapter
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
                binding.cityCoordinates.text =
                    "${data.weatherData.city.lat} ${data.weatherData.city.lon}"
                Snackbar.make(binding.mainView, "Получилось", Snackbar.LENGTH_LONG).show()*/
            }
        }



    override fun onItemClick(weather: Weather) {
        val bundle = Bundle()
        bundle.putParcelable(KEY_BUNDLE_WEATHER, weather)
        requireActivity().supportFragmentManager.beginTransaction().add(
            R.id.container,
            DetailsFragment.newInstance(bundle)
        ).addToBackStack("").commit()
    }

    companion object {
        @JvmStatic
        fun newInstance() = WeatherListFragment()
    }

}
