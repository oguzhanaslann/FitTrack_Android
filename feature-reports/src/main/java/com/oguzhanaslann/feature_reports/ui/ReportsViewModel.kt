package com.oguzhanaslann.feature_reports.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.oguzhanaslann.common.DateHelper
import com.oguzhanaslann.common.State
import com.oguzhanaslann.common.isLoading
import com.oguzhanaslann.common.reduceToNotNull
import com.oguzhanaslann.common.toStateNullable
import com.oguzhanaslann.commonui.withAtLeastDurationOf
import com.oguzhanaslann.feature_reports.data.ReportsRepository
import com.oguzhanaslann.feature_reports.domain.Report
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ReportsViewModel @Inject constructor(
    private val reportsRepository: ReportsRepository,
) : ViewModel() {
    private val _selectedDate = MutableStateFlow(Date())

    val selectedDateText = _selectedDate.map {
        DateHelper.tryFormat(it, DateHelper.DAY_MONTH_WITH_NAME_YEAR_FORMAT)
    }.asLiveData()

    private val _reports = MutableLiveData<State<Report>>()
    val reports: LiveData<State<Report>> = _reports

    val isLoading = _reports.map { it.isLoading }
    val isError = _reports.map { it is State.Error }
    val isSuccess = _reports.map { it is State.Success }

    init {
        viewModelScope.launch {
            _selectedDate.collectLatest {
                _reports.postValue(State.Loading)
                val resultState = withAtLeastDurationOf(ONE_AND_HALF_SECONDS) {
                    val result = reportsRepository.getReportOnDate(it)
                    result.toStateNullable()
                        .reduceToNotNull(NOT_FOUND_EXCEPTION)
                }

                _reports.postValue(resultState)
            }
        }
    }



    fun setSelectedDate(date: Long) {
        _selectedDate.value = Date(date)
    }

    fun getSelectedDateOrNow(): Date {
        return _selectedDate.value
    }

    fun moveNextDayIfPossible() {
        val selectionDate = getSelectedDateOrNow()
        val nextDay = DateHelper.nextDay(selectionDate)
        val now = Date()
        if (nextDay.before(now)) {
            setSelectedDate(nextDay)
        }
    }

    private fun setSelectedDate(date: Date) {
        _selectedDate.value = date
    }

    fun movePreviousDay() {
        val selectionDate = getSelectedDateOrNow()
        val previousDay = DateHelper.previousDay(selectionDate)
        setSelectedDate(previousDay)
    }

    companion object {
        const val NOT_FOUND_EXCEPTION = "Not found exception"
        private const val ONE_AND_HALF_SECONDS = 1500L
    }
}
