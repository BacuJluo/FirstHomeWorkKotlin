package com.home.firsthomeworkkotlin.lesson6

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.home.firsthomeworkkotlin.databinding.FragmentThreadsBinding
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
        myThreads.start()
        with(binding){
            val time = editText.text.toString().toLong()
            var counter = 0
            button.setOnClickListener {
                Thread{
                    sleep(time*1000L)
                    requireActivity().runOnUiThread{"Плотно поработали $time сек".also { textView.text = it }}
                    Handler(Looper.getMainLooper()).post{
                        createTextView("${Thread.currentThread().name} ${++counter}")
                    }

                }.start()
            }

            //вечный поток
            buttonSecond.setOnClickListener {
                myThreads.mHandler?.post{
                    sleep(time*1000L)
                    requireActivity().runOnUiThread{"Поток работает $time сек".also { textViewSecond.text = it }}
                    createTextView("${Thread.currentThread().name} ${++counter}")
                }
            }
        }


}

    private fun createTextView(name: String) {
        binding.mainContainer.addView(TextView(requireContext()).apply {
            text = name
            textSize = 14f
        })
    }

    class  MyThreads : Thread(){
        var mHandler: Handler?=null
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


