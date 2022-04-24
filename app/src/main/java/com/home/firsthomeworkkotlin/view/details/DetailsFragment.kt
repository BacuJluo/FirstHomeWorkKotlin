package com.home.firsthomeworkkotlin.view.details
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.home.firsthomeworkkotlin.databinding.FragmentDetailsBinding
import com.home.firsthomeworkkotlin.repository.Weather
import com.home.firsthomeworkkotlin.utlis.KEY_BUNDLE_WEATHER
import com.google.android.material.snackbar.Snackbar
import com.home.firsthomeworkkotlin.R

class DetailsFragment : Fragment() {

    private var _binding:FragmentDetailsBinding? = null
    private val binding:FragmentDetailsBinding
        get() {
            return _binding!!
        }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =FragmentDetailsBinding.inflate(inflater,container, false)
        //return inflater.inflate(R.layout.fragment_main, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //если агрументы не null то мы их передаем в renderData
        arguments?.getParcelable<Weather>(KEY_BUNDLE_WEATHER)?.let {
                renderData(it)
        }
    }

    private fun renderData(weather:Weather){
        with(binding){
            loadingLayout.visibility = View.GONE
                cityName.text = weather.city.name
                temperatureValue.text = weather.temperature.toString()
                feelsLikeValue.text = weather.feelsLike.toString()
                "${weather.city.lat} ${weather.city.lon}".apply { cityCoordinates.text = this }
            mainView.showSnackBar(cityName.text as String, weather.temperature,binding)
            //mainView.withAction(getString(R.string.error), getString(R.string.try_again), {sentRequest()}, Snackbar.LENGTH_LONG)
        }
    }

    private fun sentRequest() {

    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle):DetailsFragment{
            val fragment = DetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}

//extension функция от Андрея
private fun View.withAction(text: String, actionText: String, action: (View) -> Unit,
    length: Int = Snackbar.LENGTH_INDEFINITE) {
    Snackbar.make(this, text, length).setAction(actionText, action).show()
}

//extension function (Функция расширения)
private fun View.showSnackBar(text: String, weather: Int, binding: FragmentDetailsBinding) {
    when {
        weather>10 -> {
            Snackbar.make(binding.mainView, "$text: Тепло", Snackbar.LENGTH_LONG).show()
        }
        weather>0 -> {
            Snackbar.make(binding.mainView, "$text: Прохладно", Snackbar.LENGTH_LONG).show()
        }
        else -> {
            Snackbar.make(binding.mainView, "$text: Холодно", Snackbar.LENGTH_LONG).show()
        }
    }
}


