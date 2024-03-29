package com.home.firsthomeworkkotlin.lesson9

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.home.firsthomeworkkotlin.MyApp
import com.home.firsthomeworkkotlin.R
import com.home.firsthomeworkkotlin.domain.room.HistoryWeatherEntity
import com.home.firsthomeworkkotlin.domain.room.*
import java.util.*

private const val URI_ALL = 1 //URI для всех записей
private const val URI_ID = 2 //URI для конкретной записи
private const val ENTITY_PATH = "HistoryEntity" //Часть пути (будем опредлелять путь до historyEntity)


class HistoryContentProvider : ContentProvider() {

    private var authorities:String? = null //Адрес URI
    private lateinit var uriMatcher:UriMatcher //Помогает определиит тип адреса URI

    private var entityContentType:String? = null //Набор строк
    private var entityContentItemType:String? = null // Одна строка

    private lateinit var contentUri:Uri //Адрес URI Provider

    override fun onCreate(): Boolean {
        authorities = context?.resources?.getString(R.string.authorities)
        uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        uriMatcher.addURI(authorities, ENTITY_PATH, URI_ALL)
        uriMatcher.addURI(authorities, "$ENTITY_PATH/#", URI_ID)
        entityContentType = "vnd.android.cursor.dir/vnd.$authorities.$ENTITY_PATH"
        entityContentItemType = "vnd.android.cursor.item/vnd.$authorities.$ENTITY_PATH"

        contentUri = Uri.parse("content://$authorities/$ENTITY_PATH")
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?,
    ): Cursor {

        val historyDao = MyApp.getHistory()
        val cursor = when(uriMatcher.match(uri)) {
            URI_ALL -> {
                historyDao.getHistoryCursor()
            }
            URI_ID -> {
                val id = ContentUris.parseId(uri)
                historyDao.getHistoryCursor(id)
            }
            else -> { throw IllformedLocaleException("плохой URI")}
        }
        context.let {
            cursor.setNotificationUri(context!!.contentResolver,contentUri) //фиксирование изменений внутри этой базы данных
        }

        return cursor
    }

    override fun getType(uri: Uri): String? {
        return when (uriMatcher.match(uri)) {
            URI_ALL -> {
                entityContentType
            }
            URI_ID -> {
                entityContentItemType
            }
            else -> {
                throw IllformedLocaleException("плохой URI")
            }
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        require(uriMatcher.match(uri) == URI_ALL){
            throw IllformedLocaleException("плохой URI")
        }
        val historyDao = MyApp.getHistory()
        return mapper(values)?.let {
            historyDao.insert(it)
            val loggerUri = ContentUris.withAppendedId(contentUri,it.id)
            context?.contentResolver?.notifyChange(loggerUri,null)
            loggerUri
        }
    }

    override fun delete(uri: Uri, selection: String?, p2: Array<out String>?): Int {
        require(uriMatcher.match(uri) == URI_ID){
            throw IllformedLocaleException("плохой URI")
        }
        val id = ContentUris.parseId(uri)
        val historyDao = MyApp.getHistory()
        historyDao.deleteById(id)
        context?.contentResolver?.notifyChange(uri,null)
        return 1 //

    }

    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        require(uriMatcher.match(p0) == URI_ID){
            throw IllformedLocaleException("плохой URI")
        }
        val historyDao = MyApp.getHistory()
        mapper(p1)?.let {
            historyDao.update(it)
        }

        context?.contentResolver?.notifyChange(p0,null)
        return 1 // FIXME
    }

    //Переводим ContentValues в HistoryWeatherEntity
    private fun mapper(values: ContentValues?): HistoryWeatherEntity?{
        return if (values == null){
            null//HistoryWeatherEntity()
        } else{
            val id = if (values.containsKey(ID)) values[ID] as Long else 0
            val city = values[NAME] as String
            val temperature = values[TEMPERATURE] as Int
            HistoryWeatherEntity(id,city,temperature)
        }
    }
}