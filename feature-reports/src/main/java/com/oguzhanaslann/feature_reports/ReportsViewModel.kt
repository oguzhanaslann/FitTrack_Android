package com.oguzhanaslann.feature_reports

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.oguzhanaslann.common.DateHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.Duration
import java.util.Date
import javax.inject.Inject
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@HiltViewModel
class ReportsViewModel @Inject constructor(

) : ViewModel() {
    private val _selectedDate = MutableLiveData(Date())
    val selectedDate : LiveData<Date>
        get() = _selectedDate


    val selectedDateText = _selectedDate.map {
        DateHelper.tryFormat(it, DateHelper.DAY_MONTH_WITH_NAME_YEAR_FORMAT)
    }

    fun setSelectedDate(date: Date) {
        _selectedDate.value = date
    }

    fun setSelectedDate(date: Long) {
        _selectedDate.value = Date(date)
    }

    fun getSelectedDateOrNow(): Date {
        return _selectedDate.value ?: Date()
    }

    fun moveNextDayIfPossible() {
        val now = Date()
        val oneDay = 1.toDuration(DurationUnit.DAYS).inWholeMilliseconds
        val selectionDate = getSelectedDateOrNow()
        val nextDay = Date(selectionDate.time + oneDay)
        if (nextDay.before(now)) {
            setSelectedDate(nextDay)
        }
    }

    fun movePreviousDay() {
        val oneDay = 1.toDuration(DurationUnit.DAYS).inWholeMilliseconds
        val selectionDate = getSelectedDateOrNow()
        val previousDay = Date(selectionDate.time - oneDay)
        setSelectedDate(previousDay)
    }
}
