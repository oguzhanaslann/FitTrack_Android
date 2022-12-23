package com.oguzhanaslann.common

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.time.DurationUnit
import kotlin.time.toDuration

object DateHelper {

    const val DAY_MONTH_WITH_NAME_YEAR_FORMAT = "dd MMMM yyyy"
    const val BIRTH_DATE_FORMAT = "dd/MM/yyyy"

    private fun getDateFormatter(format: String) = SimpleDateFormat(format)

    private fun getDateFormatter(format: String, locale: Locale) = SimpleDateFormat(format, locale)

    @kotlin.jvm.Throws(ParseException::class)
    fun parse(dateString: String, format: String): Date? {
        return getDateFormatter(format).parse(dateString)
    }

    // localised parse
    @kotlin.jvm.Throws(ParseException::class)
    fun parse(dateString: String, format: String, locale: Locale): Date? {
        return getDateFormatter(format, locale).parse(dateString)
    }

    // auto localised parse
    @kotlin.jvm.Throws(ParseException::class)
    fun parse(dateString: String, format: String, autoLocale: Boolean): Date? {
        return if (autoLocale) {
            parse(dateString, format, Locale.getDefault())
        } else {
            parse(dateString, format)
        }
    }

    fun format(date: Date, format: String): String {
        return getDateFormatter(format).format(date)
    }

    fun format(date: Date, format: String, locale: Locale): String {
        return getDateFormatter(format, locale).format(date)
    }

    // auto localised format
    fun format(date: Date, format: String, autoLocale: Boolean): String {
        return if (autoLocale) {
            format(date, format, Locale.getDefault())
        } else {
            format(date, format)
        }
    }

    fun tryFormat(date: Date?, format: String): String {
        return try {
            date?.let { format(it, format) } ?: ""
        } catch (e: ParseException) {
            ""
        }
    }

    fun tryFormat(date: Date?, format: String, locale: Locale): String {
        return try {
            date?.let { format(it, format, locale) } ?: ""
        } catch (e: ParseException) {
            ""
        }
    }

    fun tryFormat(date: Date?, format: String, autoLocale: Boolean): String {
        return try {
            date?.let { format(it, format, autoLocale) } ?: ""
        } catch (e: ParseException) {
            ""
        }
    }

    fun getCurrentYear(): Int {
        return Calendar.getInstance().get(Calendar.YEAR)
    }

    fun getYearOfOrNull(birthdate: Date?): Int? {
        return birthdate?.let {
            val calendar = Calendar.getInstance()
            calendar.time = it
            calendar.get(Calendar.YEAR)
        }
    }

    fun getYearOf(birthdate: Date?): Int {
        return getYearOfOrNull(birthdate) ?: 0
    }

    fun calculateAgeOf(birthdate: Date?): Int {
        return getCurrentYear() - getYearOf(birthdate)
    }

    fun nextDay(selectionDate: Date): Date {
        val oneDay = 1.toDuration(DurationUnit.DAYS).inWholeMilliseconds
        return Date(selectionDate.time + oneDay)
    }

    fun previousDay(selectionDate: Date): Date {
        val oneDay = 1.toDuration(DurationUnit.DAYS).inWholeMilliseconds
        return Date(selectionDate.time - oneDay)
    }

    fun now(): Date  {
        return Calendar.getInstance().time
    }

    fun nowAsLong() = now().time

    /**
     *  returns the date as 00:00:00.000 clock. Simply forget about the clock time
     * @param date Date
     * @return Date
     */
    fun getStartDateOf(date: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time
    }
}
