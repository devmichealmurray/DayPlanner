package com.devmmurray.dayplanner.util

object ExtensionFunctions {

    fun String.pubDate(): String {
        return substring(0, 10) + " " + substring(11, 19) + " (GMT) "
    }
}