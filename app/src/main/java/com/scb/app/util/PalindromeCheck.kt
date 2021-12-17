package com.scb.app.util

import androidx.compose.ui.text.toLowerCase
import java.util.*

class PalindromeCheck {


    fun palindromeHelper(input: String): Boolean {
        if (input.isEmpty()) {
            return false
        }
        var left: Int = 0
        var right: Int = input.length - 1
        val stringArray = input.lowercase(Locale.getDefault()).toCharArray()
        while (left < right) {
            if (stringArray[left] != stringArray[right]) {
                return false
            }
            else {
                left++
                right--
            }
        }
        return true
    }

}