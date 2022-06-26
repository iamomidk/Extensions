package com.aperfectpolygon.extentions

/**
 * Created by iamomidk on 11/4/2020.
 * @author geniusTheFirst (candle)
 * @constructor receives a Gregorian date and initializes the other private members of the class accordingly.
 * @param date String
 */
class PersianCalendar(date: String) {

	/**
	 * getPersianDate:
	 * Returns a string version of Persian date
	 *
	 * @return String
	 */
	val persianDate: String
		get() = "$irYear/$irMonth/$irDay"

	/**
	 * getGregorianDate:
	 * Returns a string version of Gregorian date
	 *
	 * @return String
	 */
	val gregorianDate: String
		get() = "$gYear/$gMonth/$gDay"

	/**
	 * getJulianDate:
	 * Returns a string version of Julian date
	 *
	 * @return String
	 */
	private val julianDate: String
		get() = "$juYear/$juMonth/$juDay"


	/**
	 * getWeekDayStr:
	 *
	 * @return  week day by name in String
	 */
	private val weekDayStr: String
		get() {
			arrayOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday").apply { return get(dayOfWeek) }
		}

	/**
	 * toString:
	 *
	 * @return all dates in String
	 */
	override fun toString(): String =
		"$weekDayStr, Gregorian:[$gregorianDate], Julian:[$julianDate], Persian:[$persianDate]"


	/**
	 * getDayOfWeek:
	 * Returns the week day number. Monday=0..Sunday=6
	 *
	 * @return int
	 */
	private val dayOfWeek: Int
		get() = julianDayNumber % 7

	/**
	 * setGregorianDate:
	 * Sets the date according to the Gregorian calendar and adjusts the other dates.
	 *
	 * @param year  int
	 * @param month int
	 * @param day   int
	 */
	private fun setGregorianDate(year: Int, month: Int, day: Int) {
		gYear = year
		gMonth = month
		gDay = day
		if (month < 1) gMonth = 1
		if (day < 1) gDay = 1

		julianDayNumber = gregorianDateToJulianDayNumber(year, month, day)
		julianDayNumberToPersian()
		julianDayNumberToJulian()
		julianDayNumberToGregorian()
	}

	/**
	 * PersianCalendar:
	 * This method determines if the Persian (Jalali) year is leap (366-day long)
	 * or is the common year (365 days), and finds the day in March (Gregorian
	 * Calendar)of the first day of the Persian year ('irYear').Persian year (irYear)
	 * ranges from (-61 to 3177).This method will set the following private data
	 * members as follows:
	 * leap: Number of years since the last leap year (0 to 4)
	 * Gy: Gregorian year of the beginning of Persian year
	 * march: The March day of Farvardin the 1st (first day of jaYear)
	 */
	private fun persianCalendar() {
		// Persian years starting the 33-year rule
		val breaks = intArrayOf(
			-61, 9, 38, 199, 426, 686, 756, 818, 1111, 1181,
			1210, 1635, 2060, 2097, 2192, 2262, 2324, 2394, 2456, 3178
		)
		var jm: Int
		var n: Int
		var jp: Int
		var jump: Int
		gYear = irYear + 621
		var leapJ: Int = -14
		jp = breaks[0]
		// Find the limiting years for the Persian year 'irYear'
		var j = 1
		do {
			jm = breaks[j]
			jump = jm - jp
			if (irYear >= jm) {
				leapJ += jump / 33 * 8 + jump % 33 / 4
				jp = jm
			}
			j++
		} while (j < 20 && irYear >= jm)
		n = irYear - jp
		// Find the number of leap years from AD 621 to the begining of the current
		// Persian year in the Persian (Jalali) calendar
		leapJ += n / 33 * 8 + (n % 33 + 3) / 4
		if (jump % 33 == 4 && jump - n == 4) leapJ++
		// And the same in the Gregorian date of Farvardin the first
		val leapG: Int = gYear / 4 - (gYear / 100 + 1) * 3 / 4 - 150
		march = 20 + leapJ - leapG
		// Find how many years have passed since the last leap year
		if (jump - n < 6) n = n - jump + (jump + 4) / 33 * 33
		leap = ((n + 1) % 33 - 1) % 4
		if (leap == -1) leap = 4
	}


	/**
	 * JulianDayNumberToPersian:
	 * Converts the current value of 'JulianDayNumber' Julian Day Number to a date in the
	 * Persian calendar. The caller should make sure that the current value of
	 * 'JulianDayNumber' is set correctly. This method first converts the JulianDayNumber to Gregorian
	 * calendar and then to Persian calendar.
	 */
	private fun julianDayNumberToPersian() {
		julianDayNumberToGregorian()
		irYear = gYear - 621
		persianCalendar() // This invocation will updateCode 'leap' and 'march'
		val julianDayNumber1F = gregorianDateToJulianDayNumber(gYear, 3, march)
		var k = julianDayNumber - julianDayNumber1F
		if (k >= 0) {
			if (k <= 185) {
				irMonth = 1 + k / 31
				irDay = k % 31 + 1
				return
			} else k -= 186
		} else {
			irYear--
			k += 179
			if (leap == 1) k++
		}
		irMonth = 7 + k / 30
		irDay = k % 30 + 1
	}


	/**
	 * JulianDayNumberToJulian:
	 * Calculates Julian calendar dates from the julian day number (JulianDayNumber) for the
	 * period since JulianDayNumber=-34839655 (i.e. the year -100100 of both calendars) to
	 * some millions (10^6) years ahead of the present. The algorithm is based on
	 * D.A. Hatcher, Q.Jl.R.Astron.Soc. 25(1984), 53-55 slightly modified by K.M.
	 * Borkowski, Post.Astron. 25(1987), 275-279).
	 */
	private fun julianDayNumberToJulian() {
		val j = 4 * julianDayNumber + 139361631
		val i = j % 1461 / 4 * 5 + 308
		juDay = i % 153 / 5 + 1
		juMonth = i / 153 % 12 + 1
		juYear = j / 1461 - 100100 + (8 - juMonth) / 6
	}

	/**
	 * gergorianDateToJulianDayNumber:
	 * Calculates the julian day number (JulianDayNumber) from Gregorian calendar dates. This
	 * integer number corresponds to the noon of the date (i.e. 12 hours of
	 * Universal Time). This method was tested to be good (valid) since 1 March,
	 * -100100 (of both calendars) up to a few millions (10^6) years into the
	 * future. The algorithm is based on D.A.Hatcher, Q.Jl.R.Astron.Soc. 25(1984),
	 * 53-55 slightly modified by K.M. Borkowski, Post.Astron. 25(1987), 275-279.
	 *
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	private fun gregorianDateToJulianDayNumber(year: Int, month: Int, day: Int): Int {
		var julianDayNumber =
			(year + (month - 8) / 6 + 100100) * 1461 / 4 + (153 * ((month + 9) % 12) + 2) / 5 + day - 34840408
		julianDayNumber = julianDayNumber - (year + 100100 + (month - 8) / 6) / 100 * 3 / 4 + 752
		return julianDayNumber
	}

	/**
	 * JulianDayNumberToGregorian:
	 * Calculates Gregorian calendar dates from the julian day number (JulianDayNumber) for
	 * the period since JulianDayNumber=-34839655 (i.e. the year -100100 of both calendars) to
	 * some millions (10^6) years ahead of the present. The algorithm is based on
	 * D.A. Hatcher, Q.Jl.R.Astron.Soc. 25(1984), 53-55 slightly modified by K.M.
	 * Borkowski, Post.Astron. 25(1987), 275-279).
	 */
	private fun julianDayNumberToGregorian() {
		var j = 4 * julianDayNumber + 139361631
		j += ((4 * julianDayNumber + 183187720) / 146097 * 3 / 4 * 4 - 3908)
		val i = j % 1461 / 4 * 5 + 308
		gDay = i % 153 / 5 + 1
		gMonth = i / 153 % 12 + 1
		gYear = j / 1461 - 100100 + (8 - gMonth) / 6
	}

	/** Year part of a Persian date */
	private var irYear = 0

	/** Month part of a Persian date */
	private var irMonth = 0

	/** Day part of a Persian date */
	private var irDay = 0

	/** Year part of a Gregorian date */
	private var gYear = 0

	/** Month part of a Gregorian date */
	private var gMonth = 0

	/** Day part of a Gregorian date*/
	private var gDay = 0

	/** Year part of a Julian date */
	private var juYear = 0

	/** Month part of a Julian date */
	private var juMonth = 0

	/** Day part of a Julian date */
	private var juDay = 0

	/** Number of years since the last leap year (0 to 4) */
	private var leap = 0

	/** Julian Day Number */
	private var julianDayNumber = 0

	/** The march day of Farvardin the first (First day of jaYear) */
	private var march = 0

	init {
		val strY = date.substring(0, date.indexOf("/"))
		val strM = date.substring(date.indexOf("/") + 1, date.lastIndexOf("/"))
		val strD = date.substring(date.lastIndexOf("/") + 1, date.length)
		setGregorianDate(strY.toInt(), strM.toInt(), strD.toInt())
	}
}