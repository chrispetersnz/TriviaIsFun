package com.chrispetersnz.triviaisfun.util

/**
 * A class that allows the view model to not have to know about how to decode html
 */
interface IHtmlProvider {
    fun decodeHtml(encodedHtml: String): String
}