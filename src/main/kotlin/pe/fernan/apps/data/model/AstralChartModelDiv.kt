package pe.fernan.apps.data.model

import pl.droidsonroids.jspoon.annotation.Selector

internal data class CountryDiv(
    @Selector("#pais") var selectorCss: CountrySelectorCss? = null
) {
    data class CountrySelectorCss(
        @Selector("option") var cssList: List<CountryCss>? = null
    ) {
        data class CountryCss(
            @Selector(value = "option", attr = "value") var value: String? = null,
            @Selector(value = "option") var text: String? = null
        ) {
            override fun toString(): String {
                return "Option(value=$value, text=$text)"
            }
        }
    }
}

internal data class StateDiv(
    @Selector("#ciudad") var selectorCss: StateSelectorCss? = null
) {
    data class StateSelectorCss(
        @Selector("option") var cssList: List<StateCss>? = null
    ) {
        data class StateCss(
            @Selector(value = "option", attr = "value") var value: String? = null,
            @Selector(value = "option") var text: String? = null
        ) {
            override fun toString(): String {
                return "Option(value=$value, text=$text)"
            }
        }
    }
}


internal data class AstralCharDiv(
    @Selector("img[class=responsive-img][src^=/astral/grafico-astral2.php]", attr = "src")
    var image: String? = null,
    @Selector("div.collection>ul:contains(Nacimiento)>li")
    private var _cssList: List<PairCss>? = null
){
    val dataList by lazy {
        _cssList?.filter { it.key != null && it.value != null }?.map { it.key!!.trim() to  it.value!!.trim() }?.toMap().orEmpty()
    }
    data class PairCss(
        @Selector(value = "li", regex = "^(.+?):") var key: String? = null,
        @Selector(value = "li", regex = ":(.+?)$") var value: String? = null
    ) {
        override fun toString(): String {
            return "PairCss(value=$value, text=$value)"
        }
    }
}











