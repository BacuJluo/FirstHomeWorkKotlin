package com.home.firsthomeworkkotlin.lesson9

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.LinearLayout.VERTICAL
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.fragment.app.Fragment
import com.home.firsthomeworkkotlin.databinding.FragmentWorkWithContentProviderBinding

class WorkWithContentProviderFragment() : Fragment() {

    /*Зануление Binding, для того что бы не посылался запрос в пустоту и не создавались
    из-за этого Зомби*/
    private var _binding: FragmentWorkWithContentProviderBinding? = null
    private val binding get() = _binding!!

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentWorkWithContentProviderBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        retainInstance = true
        checkPermission()

    }

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permitted ->
        val read = permitted.getValue(Manifest.permission.READ_CONTACTS)
        val call = permitted.getValue(Manifest.permission.CALL_PHONE)

        when {
            read && call -> {
                getContacts()
            }
            !shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) -> {
                showGoSettings()
            }
            !shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE) -> {
                showGoSettings()
            }
            else -> {
                explain()
            }
        }

    }

    private val settingsLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        checkPermission()
    }

    private fun showGoSettings() {
        AlertDialog.Builder(requireContext())
            .setTitle("Дайте")
            .setMessage("Потому что " + "Ну а теперь идите в настройки и выдавайте разрешение вручную, раз такие умные")
            .setPositiveButton(getString(android.R.string.ok)) { _, _ ->
                openApplicationSettings()
            }
            .setNegativeButton(getString(android.R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
                requireActivity().finish()
            }
            .create()
            .show()
    }

    private fun openApplicationSettings() {
        val appSettingsIntent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.parse("package:${requireActivity().packageName}")
        )

        settingsLauncher.launch(appSettingsIntent)
    }

    //Открывает список контактов
    private fun showPhones(
        numberHome: String?,
        numberMobile: String?,
        numberWork: String?,
        numberOther: String?,
    ) {
        val linear = LinearLayout(requireContext())
        linear.orientation = VERTICAL
        numberHome?.let { number ->
            linear.addView(createButton(number, "Домашний"))
        }
        numberMobile?.let { number ->
            linear.addView(createButton(number, "Мобильный"))
        }
        numberWork?.let { number ->
            linear.addView(createButton(number, "Рабочий"))
        }
        numberOther?.let { number ->
            linear.addView(createButton(number, "Другой"))
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Позвонить")
            .setView(linear)
            .create()
            .show()
    }

    private fun createButton(number: String, text1: String): Button {
        val button = Button(requireContext())
        button.text = text1
        button.setOnClickListener {
            makeCall(number)
        }

        /*val button = (Button(requireContext()).apply {
            textSize = 20f
            text = text1
            setOnClickListener { makeCall(number) }
        })*/
        return button

    }

    private fun makeCall(number: String) {
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$number"))
        startActivity(intent)
    }

    private fun checkPermission() {
        //а есть ли разрешение?
        val resultRead =
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS)
        val resultCall =
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE)

        if (resultRead == PERMISSION_GRANTED && resultCall == PERMISSION_GRANTED) {
            getContacts()
        } else {
            mRequestPermission()
        }
    }

    private fun explain() {
        AlertDialog.Builder(requireContext())
            .setTitle("Дайте")
            .setMessage("Потому что")
            .setPositiveButton("Да") { _, _ ->
                mRequestPermission()
            }
            .setNegativeButton("Нет") { dialog, _ ->
                dialog.dismiss()
                requireActivity().finish()
            }
            .create()
            .show()
    }

    private fun explainCall() {
        AlertDialog.Builder(requireContext())
            .setTitle("Доступ к контактам")
            .setMessage("Для корректной работы требуется доступ к вызову номера")
            .setPositiveButton("Предоставить доступ") { _, _ ->
                mRequestPermission()
            }
            .setNegativeButton("Не предоставлять") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    private fun mRequestPermission() {
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.CALL_PHONE
            )
        )
    }

    /*@Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {

    }*/

    @SuppressLint("Range")
    private fun getContacts() {

        context?.let {context: Context ->
            val contentResolver = context.contentResolver

            val cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                ContactsContract.Contacts.DISPLAY_NAME + " ASC"
            )

            cursor?.let {
                for (i in 0 until cursor.count) {
                    if (cursor.moveToPosition(i)) {
                        val name =
                            cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)) //Вариант 2 (правильный)

                        val contactId =
                            cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))

                        val view = TextView(context).apply {
                            text = name
                            textSize = 25f
                        }

                        view.setOnClickListener {
                            getNumberForId(contentResolver, contactId)
                        }
                        binding.containerForContacts.addView(view)
                    }
                }
                cursor.close()
            }
        }
    }

    @SuppressLint("Range")
    private fun getNumberForId(contentResolver: ContentResolver, contactId: String) {
        val phones = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId,null,null
        )
        var number : String
        var home : String? = null
        var mobile : String? = null
        var work : String? = null
        var other : String? = null

        phones?.let {cursor ->
            while (cursor.moveToNext()){
                number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                when (cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE))){
                    ContactsContract.CommonDataKinds.Phone.TYPE_HOME -> home = number
                    ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE -> mobile = number
                    ContactsContract.CommonDataKinds.Phone.TYPE_WORK -> work = number
                    else -> other = number
                }
            }
            phones.close()
        }
        showPhones(home,mobile,work,other)
    }

    // ВАРИАНТ 2
    /*private fun callPhoneNumber(number: String) {
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.CALL_PHONE) == PERMISSION_GRANTED
        ){
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:$number")
            startActivity(callIntent)
        }else if (shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)){
            explainCall()
        } else {
            mRequestPermission()
        }
    }*/


    companion object {
        @JvmStatic
        fun newInstance() = WorkWithContentProviderFragment()

    }

}



