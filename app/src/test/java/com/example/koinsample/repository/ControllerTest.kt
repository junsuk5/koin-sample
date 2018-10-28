package com.example.koinsample.repository

import com.example.koinsample.di.appModules
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.koin.standalone.StandAloneContext.startKoin
import org.koin.standalone.inject
import org.koin.test.AutoCloseKoinTest

class ControllerTest: AutoCloseKoinTest() {
    val service: BusinessService by inject()

    @Before
    fun setUp() {
        startKoin(appModules)
    }

    @Test
    fun testController() {
        assertEquals("서비스", service.name)
    }
}