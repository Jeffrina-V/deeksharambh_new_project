package com.example.data

import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class Converters {
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    
    private val listStringAdapter = moshi.adapter<List<String>>(
        Types.newParameterizedType(List::class.java, String::class.java)
    )
    
    private val listSyllabusUnitAdapter = moshi.adapter<List<SyllabusUnit>>(
        Types.newParameterizedType(List::class.java, SyllabusUnit::class.java)
    )

    @TypeConverter
    fun fromStringList(value: List<String>?): String? {
        return value?.let { listStringAdapter.toJson(it) }
    }

    @TypeConverter
    fun toStringList(value: String?): List<String>? {
        return value?.let { listStringAdapter.fromJson(it) }
    }

    @TypeConverter
    fun fromSyllabusUnitList(value: List<SyllabusUnit>?): String? {
        return value?.let { listSyllabusUnitAdapter.toJson(it) }
    }

    @TypeConverter
    fun toSyllabusUnitList(value: String?): List<SyllabusUnit>? {
        return value?.let { listSyllabusUnitAdapter.fromJson(it) }
    }
}
