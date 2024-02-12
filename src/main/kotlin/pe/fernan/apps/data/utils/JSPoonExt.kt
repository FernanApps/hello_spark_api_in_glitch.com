package pe.fernan.apps.data.utils

import pl.droidsonroids.jspoon.Jspoon

object JSPoonExt {
    inline fun <reified T : Any> String.toHtmlAdapter(): T? {
        val htmlDocument = this
        return try {
            val jspoon = Jspoon.create()
            val htmlAdapter = jspoon.adapter(T::class.java)
            return htmlAdapter.fromHtml(htmlDocument)

        } catch (e: Exception){
            e.printStackTrace()
            null
        }
    }
}