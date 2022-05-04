package com.home.firsthomeworkkotlin.lesson6

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.home.firsthomeworkkotlin.R
import com.home.firsthomeworkkotlin.databinding.FragmentWeatherListBinding
import com.home.firsthomeworkkotlin.datasource.Weather
import com.home.firsthomeworkkotlin.utlis.KEY_BUNDLE_WEATHER
import com.home.firsthomeworkkotlin.view.details.DetailsFragment
import com.home.firsthomeworkkotlin.viewmodel.AppState
import com.home.firsthomeworkkotlin.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import com.home.firsthomeworkkotlin.databinding.FragmentThreadsBinding
import com.home.firsthomeworkkotlin.view.weatherlist.OnItemListClickListener
import com.home.firsthomeworkkotlin.view.weatherlist.WeatherListAdapter
import kotlinx.android.synthetic.main.fragment_threads.*
import java.lang.Thread.sleep

class ThreadsFragment : Fragment() {

    /*Зануление Binding, для того что бы не посылался запрос в пустоту и не создавались
    из-за этого Зомби*/
    private var _binding: FragmentThreadsBinding? = null
    private val binding: FragmentThreadsBinding get() = _binding!!

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
         _binding = FragmentThreadsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val myThreads = MyThreads()
        with(binding){
            button.setOnClickListener {
                Thread{
                    val time = editText.text.toString().toLong()
                    sleep(time*1000L)
                    requireActivity().runOnUiThread{"Плотно поработали $time сек".also { textView.text = it }}
                }.start()
            }
        }
    }

    class  MyThreads : Thread(){
        lateinit var mHandler: Handler;
        override fun run() {
            Looper.prepare()
            mHandler = Handler(Looper.myLooper()!!)
            Looper.loop()//Запетлить (зациклить)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ThreadsFragment()
    }

}

