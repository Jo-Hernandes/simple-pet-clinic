package com.jonathas.petclinic

import com.jonathas.petclinic.ui.ui.main.domain.IsOpenUseCase
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before
import java.time.LocalDate
import java.time.LocalTime

class IsOpenUseCaseTest {

    lateinit var useCase: IsOpenUseCase

    @Before
    fun setup() {
        useCase = IsOpenUseCase()
    }

    @Test
    fun `parse days of the week accordingly`() {
        val workTime = "M-F 9:00 - 18:00"
        useCase.parseWorkHours(workTime)
        assertTrue(true)
    }

    @Test
    fun `throw error for wrong input format for days`() {
        val workTime = "MONDAY-WEDNESDAY 9:00 - 18:00"
        assertThrows(Exception::class.java) { useCase.parseWorkHours(workTime) }
    }

    @Test
    fun `throw error for no input format for days`() {
        val workTime = "9:00 - 18:00"
        assertThrows(Exception::class.java) { useCase.parseWorkHours(workTime) }
    }

    @Test
    fun `throw error for wrong input for hours`(){
        val workTime = "M-F 48:00 - 18:00"
        assertThrows(Exception::class.java) { useCase.parseWorkHours(workTime) }
    }

    @Test
    fun `throw error for no input for hours`(){
        val workTime = "M-F"
        assertThrows(Exception::class.java) { useCase.parseWorkHours(workTime) }
    }


    @Test
    fun `returns true for open time accordingly to worktime`() {
        val workTime = "M-F 9:00 - 18:00"
        useCase.parseWorkHours(workTime)
        val currentDate = LocalDate.of(2022, 6, 20)
        val currentTime = LocalTime.of(11, 30)
        assertTrue(useCase(currentDate, currentTime))
    }

    @Test
    fun `return false as is not workday`() {
        val workTime = "T-F 9:00 - 18:00"
        useCase.parseWorkHours(workTime)
        val currentDate = LocalDate.of(2022, 6, 20)
        val currentTime = LocalTime.of(11, 30)
        assertFalse(useCase(currentDate, currentTime))
    }

    @Test
    fun `return false as is past time opening hours`() {
        val workTime = "T-F 9:00 - 18:00"
        useCase.parseWorkHours(workTime)
        val currentDate = LocalDate.of(2022, 6, 20)
        val currentTime = LocalTime.of(22, 0)
        assertFalse(useCase(currentDate, currentTime))
    }
}