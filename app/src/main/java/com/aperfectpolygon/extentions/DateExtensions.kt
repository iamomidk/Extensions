package com.aperfectpolygon.extentions

import android.annotation.SuppressLint
import com.aperfectpolygon.extentions.Lang.EN
import com.aperfectpolygon.extentions.Lang.FA
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

fun Long.getDateTime(language: Lang): String {
	var jDate = ""
	var time = ""
	try {
		SimpleDateFormat(
			"HH:mm:ss", Locale(
				when (language) {
					EN -> "en"
					FA -> "fa"
				}
			)
		).apply {
			TimeZone.getDefault()
			time = format(Date(this@getDateTime.times(1000)))
			jDate = this@getDateTime.getDayOfWeek(language)
		}
	} catch (e: Exception) {
		e.printStackTrace()
	}
	return "${jDate.toLocaleLang(language)} - ${time.toLocaleLang(language)}"
}

fun Long.getDayOfWeek(language: Lang): String {
	val today = Calendar.getInstance(TimeZone.getDefault())
		.apply { time = Date(System.currentTimeMillis()) }.get(Calendar.DAY_OF_YEAR)
	val tsDay = Calendar.getInstance(TimeZone.getDefault())
		.apply { time = Date(this@getDayOfWeek * 1000) }.get(Calendar.DAY_OF_YEAR)
	return when {
		today - tsDay == 0 -> if (language == EN) "today" else "امروز"
		today - tsDay == 1 -> if (language == EN) "yesterday" else "دیروز"
		else -> toDate(language)
	}
}

fun Long.getDate(pattern: String, tz: String? = null, language: Lang): String {
	SimpleDateFormat(
		pattern, Locale(
			when (language) {
				EN -> "en"
				FA -> "fa"
			}
		)
	).apply {
		timeZone = if (tz != null)
			TimeZone.getTimeZone(tz)
		else
			TimeZone.getDefault()
		return when (language) {
			EN -> PersianCalendar(format(Date(this@getDate.times(1000)))).gregorianDate
			FA -> PersianCalendar(format(Date(this@getDate.times(1000)))).persianDate
		}
	}
}

@SuppressLint("SimpleDateFormat")
fun Long.toHour(tz: String, language: Lang): String {
	var time = ""
	try {
		SimpleDateFormat("HH").apply {
			timeZone = TimeZone.getTimeZone(tz)
			time = format(Date(this@toHour.times(1000)))
		}
	} catch (e: Exception) {
		e.toString()
	}
	return time.toLocaleLang(language)
}

@SuppressLint("SimpleDateFormat")
fun Long.toMinutes(tz: String, language: Lang): String {
	var time = ""
	try {
		SimpleDateFormat("mm").apply {
			timeZone = TimeZone.getTimeZone(tz)
			time = format(Date(this@toMinutes.times(1000)))
		}
	} catch (e: Exception) {
		e.toString()
	}
	return time.toLocaleLang(language)
}

val Long.toTime: String
	@SuppressLint("SimpleDateFormat")
	get() {
		var time = ""
		try {
			SimpleDateFormat("HH:mm").apply {
				timeZone = TimeZone.getDefault()
				time = format(Date(this@toTime.times(1000)))
			}
		} catch (e: Exception) {
			e.toString()
		}
		return time.toLocaleLang()
	}

fun Long.getDate(language: Lang, date: (year: Int, month: Int, day: Int) -> Unit) {
	var jDate = ""
	try {
		jDate = this.getDate(pattern = "yyyy/MM/dd", language = language, tz = null)
	} catch (e: Exception) {
		e.toString()
	}
	jDate.split('/').apply { date.invoke(get(0).toInt(), get(1).toInt(), get(2).toInt()) }
}

fun Long.toDate(language: Lang): String = try {
	getDate(pattern = "yyyy/MM/dd", language = language)
} catch (e: Exception) {
	e.printStackTrace()
	"bad timestamp"
}.toLocaleLang(language)

fun Long.toDateWithMonthName(language: Lang): String {
	var jDate: String
	val year: String
	val month: String
	val day: String
	val todayYear: String
	try {
		jDate = getDate(pattern = "yyyy/MM/dd", language = language)
		jDate.split("/").let {
			year = it[0].takeLast(2)
			month = it[1]
			day = it[2]
		}
		jDate = when (language) {
			FA -> when (month) {
				"1" -> "$day فروردین"
				"2" -> "$day اردیبهشت"
				"3" -> "$day خرداد"
				"4" -> "$day تیر"
				"5" -> "$day مرداد"
				"6" -> "$day شهریور"
				"7" -> "$day مهر"
				"8" -> "$day آبان"
				"9" -> "$day آذر"
				"10" -> "$day دی"
				"11" -> "$day بهمن"
				"12" -> "$day اسفند"
				else -> ""
			}
			EN -> when (month) {
				"1" -> "$day Jan"
				"2" -> "$day Feb"
				"3" -> "$day March"
				"4" -> "$day Apr"
				"5" -> "$day May"
				"6" -> "$day June"
				"7" -> "$day July"
				"8" -> "$day Aug"
				"9" -> "$day Sept"
				"10" -> "$day Oct"
				"11" -> "$day Nov"
				"12" -> "$day Dec"
				else -> ""
			}
		}
		todayYear =
			System.currentTimeMillis().div(1000).getDate("yyyy/MM/dd", language = language)
				.split("/")[0].takeLast(2)
		if (year != todayYear) jDate += " - $year"
	} catch (e: Exception) {
		e.printStackTrace()
		jDate = "bad timestamp"
	}
	return jDate.toLocaleLang(language)
}

fun Long.toDateWithMonth(language: Lang): String {
	var jDate: String
	try {
		jDate = getDate("yyyy/MM/dd", language = language)
		val year: String = jDate.split("/")[0].takeLast(2)
		val month: String = jDate.split("/")[1]
		val day: String = jDate.split("/")[2]
		jDate = "$day/$month"
		val todayYear: String =
			System.currentTimeMillis().div(1000).getDate("yyyy/MM/dd", language = language)
				.split("/")[0].takeLast(2)
		if (year != todayYear) jDate += "/$year"
	} catch (e: Exception) {
		e.printStackTrace()
		jDate = "bad timestamp"
	}
	return jDate.toLocaleLang(language)
}

fun Long.setDate(language: Lang, date: (suffix: String, mainText: String) -> Unit) {
	TimeUnit.DAYS.convert(this, TimeUnit.SECONDS).also {
		val months = it / 30
		val years = it / 365
		when {
			months < 1 -> date(
				it.toLocaleLang(language), when (language) {
					EN -> "day"
					FA -> "روز"
				}
			)
			years < 1 -> date(
				months.toLocaleLang(language),
				when (language) {
					EN -> "month"
					FA -> "ماه"
				}
			)
			else -> date(
				years.toLocaleLang(language),
				when (language) {
					EN -> "year"
					FA -> "سال"
				}
			)
		}
	}
}

val Calendar.timeInDays: Long
	get() = Calendar.getInstance().apply {
		time = Date(this@timeInDays.timeInMillis)
		timeZone = TimeZone.getTimeZone("GMT")
		set(Calendar.HOUR_OF_DAY, 0)
		set(Calendar.MINUTE, 0)
		set(Calendar.SECOND, 0)
		set(Calendar.MILLISECOND, 0)
	}.timeInSeconds

val Calendar.timeInSeconds: Long
	get() = timeInMillis.div(1000)
