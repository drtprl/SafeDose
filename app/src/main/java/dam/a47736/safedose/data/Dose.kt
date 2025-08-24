package dam.a47736.safedose.data

data class Dose(
    var userId: String = "",
    var timestamp: Long = System.currentTimeMillis(),
    var drugs :  List<String> = emptyList(),
    var interactions : List<Interaction> = emptyList()
)
data class Interaction(
    var risk : Risk? = null,
    var drugs : List<String> = emptyList(),
    var explanation : String = ""
)

enum class Risk{
    DEATH,MODERATE,SEVERE
}
