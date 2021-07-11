package com.chrispetersnz.triviaisfun.util

import android.text.Html

class HtmlProvider : IHtmlProvider {
    override fun decodeHtml(encodedHtml: String): String {
        return Html.fromHtml(encodedHtml, 0).toString()
    }
}