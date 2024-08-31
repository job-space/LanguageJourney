package com.example.dictionaryapp


data class Words(
    var id: Int = 0,
    var word: String,
    var translation: String,
    var favorite: Boolean,
    var level: Int
)

data class WordEntry(
    val word: String,
    val translation: String,
    val level: Int
)

data class Todo(
    val word: String
)