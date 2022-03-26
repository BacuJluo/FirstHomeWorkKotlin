package com.home.firsthomeworkkotlin.view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.home.firsthomeworkkotlin.databinding.FragmentMainBinding
import com.home.firsthomeworkkotlin.viewmodel.MainViewModel

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding //утечка памяти

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //надуваем макет через xml
        //return inflater.inflate(R.layout.fragment_main, container, false)
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnOne.setOnClickListener { }
        /*одинаковые методы, но есть отличия. Если делать через binding то отсеиваются
        NullPointerExeption
        view.findViewById<Button>(R.id.btnOne).setOnClickListener {  }*/

        /*создание ссылки на ViewModel
        Даже если фрагмент умрет то ViewModel будет жить, и не будет посылать фрагменту запросы
        И если Фрагмент заново пересоздастся, то ViewModel нет, а просто вернет ее по прошлому запросу от ранее созданного фрагмента*/
        val viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        //Callback лайвдэйты
        val observer = object : Observer<Any> {
            override fun onChanged(data: Any) {
                renderData(data)
            }
        }
        /*Обращение к LiveData, чтоб подписала фрагмент как слушателя на LiveData, ориентируясь на его жизненный цикл
        Фрагмент будет слушателем LiveData до тех пор пока не умрет, в этом случае LiveData
        уже не будет отправлять в пустоту свои данные. В Callback observer будут рассылаться обновления LiveData*/
        viewModel.getData().observe(viewLifecycleOwner, observer)

        viewModel.getWeather()
    }

    private fun renderData(data: Any) {
        //отрисовка Data
        Toast.makeText(requireContext(), "Работает", Toast.LENGTH_SHORT).show()
    }

    companion object {

        fun newInstance() = MainFragment()
    }
}