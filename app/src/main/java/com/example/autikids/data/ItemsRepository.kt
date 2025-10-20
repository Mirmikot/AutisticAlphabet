package com.example.autikids.data

import android.content.Context
import com.example.autikids.R

data class Item(val word: String, val imageRes: Int)

object ItemsRepository {
	private val fallback: List<Item> = listOf(
		Item(word = "СОБАКА", imageRes = R.drawable.ic_placeholder),
		Item(word = "КОШКА", imageRes = R.drawable.ic_placeholder),
		Item(word = "ЯБЛОКО", imageRes = R.drawable.ic_placeholder),
		Item(word = "МАШИНА", imageRes = R.drawable.ic_placeholder),
		Item(word = "ДОМ", imageRes = R.drawable.ic_placeholder),
	)

	fun load(context: Context): List<Item> {
		val fromCsv = CsvLoader.loadItems(context)
		return if (fromCsv.isNotEmpty()) fromCsv else fallback
	}
}
