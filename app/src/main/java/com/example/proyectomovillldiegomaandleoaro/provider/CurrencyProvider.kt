package com.example.proyectomovillldiegomaandleoaro.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.example.proyectomovillldiegomaandleoaro.data.AppDatabase
import com.example.proyectomovillldiegomaandleoaro.data.CurrencyDao
import java.text.SimpleDateFormat
import java.util.Locale

class CurrencyProvider : ContentProvider() {

    companion object {
        private const val AUTHORITY = "com.example.exchangerate.provider"
        private const val TABLE_NAME = "currency_table"
        val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/$TABLE_NAME")

        private const val CODE_CURRENCY = 1
        private const val CODE_CURRENCY_WITH_DATES = 2

        private val MATCHER = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, TABLE_NAME, CODE_CURRENCY)
            addURI(AUTHORITY, "$TABLE_NAME/*/*", CODE_CURRENCY_WITH_DATES) // Dos segmentos: startDate y endDate
        }
    }

    private lateinit var currencyDao: CurrencyDao

    override fun onCreate(): Boolean {
        currencyDao = AppDatabase.getDatabase(context!!).currencyDao()
        Log.d("CurrencyProvider", "ContentProvider creado")
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        Log.d("CurrencyProvider", "URI recibida: $uri")

        val db = AppDatabase.getDatabase(context!!).openHelper.readableDatabase
        return when (MATCHER.match(uri)) {
            CODE_CURRENCY -> {
                Log.d("CurrencyProvider", "Consultando todas las tasas de cambio")
                db.query("SELECT * FROM currency_table ORDER BY timestamp DESC LIMIT 1")
            }
            CODE_CURRENCY_WITH_DATES -> {
                val startDate = uri.pathSegments[1] // Primer segmento: startDate
                val endDate = uri.pathSegments[2]   // Segundo segmento: endDate
                Log.d("CurrencyProvider", "Consultando tasas de cambio entre $startDate y $endDate")

                // Convertir las fechas de String a Long (timestamp) si es necesario
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val startTimestamp = dateFormat.parse(startDate)?.time ?: 0L
                val endTimestamp = dateFormat.parse(endDate)?.time ?: 0L

                db.query(
                    "SELECT * FROM currency_table WHERE timestamp BETWEEN ? AND ?",
                    arrayOf(startTimestamp.toString(), endTimestamp.toString())
                )
            }
            else -> {
                Log.e("CurrencyProvider", "URI desconocida: $uri")
                throw IllegalArgumentException("Unknown URI: $uri")
            }
        }
    }

    override fun getType(uri: Uri): String? = when (MATCHER.match(uri)) {
        CODE_CURRENCY -> "vnd.android.cursor.dir/$AUTHORITY.$TABLE_NAME"
        CODE_CURRENCY_WITH_DATES -> "vnd.android.cursor.item/$AUTHORITY.$TABLE_NAME"
        else -> throw IllegalArgumentException("Unknown URI: $uri")
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        Log.d("CurrencyProvider", "Insert no implementado")
        throw UnsupportedOperationException("Insert not supported")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        Log.d("CurrencyProvider", "Delete no implementado")
        throw UnsupportedOperationException("Delete not supported")
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        Log.d("CurrencyProvider", "Update no implementado")
        throw UnsupportedOperationException("Update not supported")
    }
}