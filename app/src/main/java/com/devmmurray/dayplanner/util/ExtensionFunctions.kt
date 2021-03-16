package com.devmmurray.dayplanner.util

object ExtensionFunctions {

    // Edits the publication date sent from Guardian News
    fun String.pubDate(): String {
        return substring(0, 10) + " " + substring(11, 19) + " (GMT) "
    }
}