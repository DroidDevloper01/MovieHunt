package com.scb.app.util

import org.junit.Assert
import org.junit.Before
import org.junit.Test

class PalindromeCheckTest {
    private lateinit var input: List<String>
    private lateinit  var checker:PalindromeCheck

    @Before
    fun setUp() {
        input = listOf<String>("Ada", "Random", "mom", "2321", "124F21","!@345567%^")
        checker= PalindromeCheck()
    }

    @Test
    fun palindromeHelper() {
       Assert.assertEquals(false, checker.palindromeHelper(""))
    }
    @Test
    fun palindromeHelperProper() {
        Assert.assertEquals(true, checker.palindromeHelper(input[0]))
    }
        @Test
    fun palindromeHelperInvalid() {
        Assert.assertEquals(false, checker.palindromeHelper(input[5]))
    }
    @Test
    fun palindromeHelperFailure() {
        Assert.assertNotEquals(false, checker.palindromeHelper(input[2]))
    }

}