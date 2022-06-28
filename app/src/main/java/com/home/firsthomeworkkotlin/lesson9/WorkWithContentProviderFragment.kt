package com.home.firsthomeworkkotlin.lesson9

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.home.firsthomeworkkotlin.databinding.FragmentWorkWithContentProviderBinding

data class PickedContact(val number: String, val name: String?)
class WorkWithContentProviderFragment constructor(private val requestCode: Int = 23) : Fragment() {


    private lateinit var onContactPicked: (PickedContact) -> Unit

    /*Зануление Binding, для того что бы не посылался запрос в пустоту и не создавались
    из-за этого Зомби*/
    private var _binding: FragmentWorkWithContentProviderBinding? = null
    private val binding: FragmentWorkWithContentProviderBinding get() = _binding!!

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

    //Открывает список контактов
    private fun pick() {
        try {
            Intent().apply {
                data = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                action = Intent.ACTION_PICK
                startActivityForResult(this, requestCode)
            }
        } catch (e: Exception) {
        }
    }

    private fun checkPermission() {
        //а есть ли разрешение?
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
        ) {
            getContacts()
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
            explain()
        } else {
            mRequestPermission()
        }
    }

    private fun explain() {
        AlertDialog.Builder(requireContext())
            .setTitle("Доступ к контактам")
            .setMessage("Объяснение бла бла бла")
            .setPositiveButton("Предоставить доступ") { _, _ ->
                mRequestPermission()
            }
            .setNegativeButton("Не надо") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    private val REQUEST_CODE = 999
    private fun mRequestPermission() {
        requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), REQUEST_CODE)
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        if (requestCode == REQUEST_CODE) {
            for (i in permissions.indices) {
                if (permissions[i] == Manifest.permission.READ_CONTACTS && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    getContacts()

                } else {
                    explain()
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private lateinit var number: String

    @SuppressLint("Range")
    private fun getContacts() {
        val contentResolver: ContentResolver = requireContext().contentResolver

        val cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.Contacts.DISPLAY_NAME + " ASC"
        )
        val phone = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
        ContactsContract.CommonDataKinds.Phone.NUMBER)



        cursor?.let {
            phone.let {
                for (i in 0 until cursor.count) {
                    if (phone != null) {
                        if (cursor.moveToPosition(i)&&phone.moveToPosition(i)) {
                            val columnNameIndex =
                                cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                            val name: String = cursor.getString(columnNameIndex)
                            val columnPhoneIndex =
                                phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                            val phoneNumber: String = phone.getString(columnPhoneIndex)


                            binding.containerForContacts.addView(TextView(requireContext()).apply {
                                textSize = 30f
                                text = "$name: $phoneNumber"

                            })
                            binding.containerForContacts.addView(Button(requireContext()).apply {
                                textSize = 20f
                                text = "Позвонить"
                                setOnClickListener { phoneCall(phoneNumber) }
                            })
                        }
                    }
                }
            }
        }

    }

    private fun phoneCall(number: String) {
        if(ActivityCompat.checkSelfPermission(requireContext(),Manifest.permission.CALL_PHONE) === PackageManager.PERMISSION_GRANTED){
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:$number")
            startActivity(callIntent)
        }else{
            Toast.makeText(requireContext(),"У вас нету разрешения", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = WorkWithContentProviderFragment()

    }

}



