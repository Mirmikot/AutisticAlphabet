package com.example.autikids.data

import android.content.Context
import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader

object CsvLoader {
	fun loadItems(context: Context, assetName: String = "items.csv"): List<Item> {
		return try {
			val input = context.assets.open(assetName)
			val reader = BufferedReader(InputStreamReader(input, Charsets.UTF_8))
			val lines = reader.readLines()
			if (lines.isEmpty()) return emptyList()
			// Skip header, expect columns: word,image_name
			lines.drop(1).mapNotNull { line ->
				val parts = line.split(",")
				if (parts.size < 2) return@mapNotNull null
				val word = parts[0].trim()
				val imageName = parts[1].trim()
				val resId = context.resources.getIdentifier(imageName, "drawable", context.packageName)
				if (resId == 0) return@mapNotNull null
				Item(word = word.uppercase(), imageRes = resId)
			}
		} catch (e: Exception) {
			Log.e("CsvLoader", "Failed to load items: ${e.message}")
			emptyList()
		}
	}
}
