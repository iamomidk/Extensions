package com.aperfectpolygon.extentions

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.abs

val Number.withDigits: String
	get() = DecimalFormat().apply {
		maximumFractionDigits = 8
		roundingMode = RoundingMode.HALF_UP
	}.format(this)

private val persianNumbers = arrayOf('۰', '۱', '۲', '۳', '۴', '۵', '۶', '۷', '۸', '۹')

val String.toEnglish: String
	get() = try {
		when {
			isEmpty() -> "0"
			else -> BigDecimal(
				replace('−', '-')
					.replace('٫', '.')
					.replace('٫', '.')
					.replace("٬", "")
					.replace(',', '.')
					.replace(".", ".").trim()
			).toString()
		}
	} catch (e: Exception) {
		"0"
	}

enum class Lang(locale: String) {
	EN("en"), FA("fa")
}

/**
 * @param language "en", "fa", "fa-ir"
 */
fun Number.toLocaleLang(language: Lang): String = buildString {
	when (language) {
		Lang.EN -> append(this@toLocaleLang)
		Lang.FA -> for (element in this@toLocaleLang.toString()) when (element) {
			in '0'..'9' -> (element.toString().toInt()).apply { append(persianNumbers[this]) }
			else -> append(element)
		}
	}
}.trim()

fun String.toLocaleLang(language: Lang = Lang.EN): String = when (language) {
	Lang.EN -> this.trim()
	Lang.FA -> if (isEmpty()) "" else buildString {
		for (element in this@toLocaleLang) when (element) {
			in '0'..'9' -> (element.toString().toInt()).apply { append(persianNumbers[this]) }
			else -> append(element)
		}
	}.trim()
}

fun Double.ensureDigits(fractionDigits: Int): String {
	val minus = this < 0
	var intPart: String
	"%.${fractionDigits}f".format(abs(this)).toEnglish.also { formattedNum ->
		formattedNum.split('.').also {
			intPart = it[0]
			return when {
				intPart.length >= 8 || fractionDigits == 0 -> if (minus) "-$intPart" else intPart
				else -> {
					var res = formattedNum.take(9)
					if (formattedNum.endsWith(".0") or formattedNum.endsWith(".00") or formattedNum.endsWith(".000")) res.trimEnd(
						'0'
					)
					if (res.endsWith(".")) res = res.take(res.length - 1)
					if (minus) "-$res" else res
				}
			}.toDouble().withDigits
		}
	}
}

fun Number?.convertNumbersOnly(
	digits: Int,
): String = when (this) {
	is Double -> ensureDigits(digits).format("%,d", this).toLocaleLang()
	null -> "0".toLocaleLang()
	else -> "%,d".format(this).toLocaleLang()
}
