package com.home.firsthomeworkkotlin.view.weatherlist

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
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
        //initRecyclerView() Не работает

        /*//binding.btnOne.setOnClickListener { }
        одинаковые методы, но есть отличия. Если делать через binding то отсеиваются
        NullPointerExeption
        view.findViewById<Button>(R.id.btnOne).setOnClickListener {  }*/
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
                binding.cityCoordinates.text =
                    "${data.weatherData.city.lat} ${data.weatherData.city.lon}"
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

}

