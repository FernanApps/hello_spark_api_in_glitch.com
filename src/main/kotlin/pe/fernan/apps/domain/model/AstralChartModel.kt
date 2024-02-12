package pe.fernan.apps.domain.model

data class Country(
    val id: Int,
    val name: String
)
data class State(
    val id: Int,
    val name: String
)

data class Location(
    val id: Int,
    val name: String
)

data class AstralChart(val image: String, val params: Map<String, String>){
    override fun toString(): String {
        return "AstralChart(image='$image')"
    }
}