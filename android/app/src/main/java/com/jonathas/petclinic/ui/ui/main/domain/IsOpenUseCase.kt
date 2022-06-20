package com.jonathas.petclinic.ui.ui.main.domain

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class IsOpenUseCase {

    private lateinit var openTime: LocalTime
    private lateinit var closeTime: LocalTime
    private lateinit var currentWorkRange: IntRange

    private val workDays: HashMap<Char, DayOfWeek> = hashMapOf(
        'M' to DayOfWeek.MONDAY,
        'T' to DayOfWeek.TUESDAY,
        'W' to DayOfWeek.WEDNESDAY,
        'R' to DayOfWeek.THURSDAY,
        'F' to DayOfWeek.FRIDAY,
        'S' to DayOfWeek.SATURDAY,
        'U' to DayOfWeek.SUNDAY,
    )

    operator fun invoke(currentDate: LocalDate, currentTime: LocalTime) =
        currentWorkRange.contains(currentDate.dayOfWeek.value) &&
                (currentTime.isAfter(openTime) &&
                        currentTime.isBefore(closeTime))

    fun parseWorkHours(workTimeString: String) {
        Regex(DAYS_REGEX).find(workTimeString)?.value?.let { workDays ->
            Regex(WEEKDAY_REGEX).findAll(workDays)
                .map { it.value.first() }
                .map { this.workDays[it]?.value ?: throw RuntimeException("WRONG VALUE FOR DATE FORMAT") }
                .let {
                    currentWorkRange = IntRange(it.first(), it.last())
                }
        } ?: throw RuntimeException("WRONG DAY FORMAT")

        Regex(HOUR_REGEX).findAll(workTimeString)
            .map { it.value.padStart(5, '0') }
            .map { LocalTime.parse(it, DateTimeFormatter.ISO_LOCAL_TIME) }
            .let {
                it.ifEmpty { throw RuntimeException("WRONG TIME FORMAT") }
                openTime = it.first()
                closeTime = it.last()
            }
    }

    companion object {
        private const val WEEKDAY_REGEX = "[A-Z]"
        private const val DAYS_REGEX = "($WEEKDAY_REGEX-$WEEKDAY_REGEX)"
        private const val HOUR_REGEX = "\\d{1,2}:\\d{2}"
    }

}