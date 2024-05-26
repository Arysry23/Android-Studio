package com.example.quizapp

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "quizzes")
@TypeConverters(Converters::class)
data class QuizModel(
    @PrimaryKey var id: String,
    val title: String,
    val subtitle: String,
    val time: String,
    val questionList: Map<String, QuestionModel>
) {
    constructor() : this("", "", "", "", emptyMap())
}

data class QuestionModel(
    var id: String = "",
    val question: String,
    val options: Map<String, String>,
    val correct: String
) {
    constructor() : this("", "", emptyMap(), "")
}

class Converters {
    @TypeConverter
    fun fromString(value: String): Map<String, QuestionModel> {
        val mapType = object : TypeToken<Map<String, QuestionModel>>() {}.type
        return Gson().fromJson(value, mapType)
    }

    @TypeConverter
    fun fromMap(map: Map<String, QuestionModel>): String {
        return Gson().toJson(map)
    }
}
