package dam.a47736.safedose.data

import dam.a47736.safedose.R

data class CommunityCenter(
    val name: String,
    val id: Int,
    val address: String,
    val services: List<ServiceType>,
    val schedule: String,
    val latitude: Double,
    val longitude: Double,
    val drawableName: String,
    val isFavorite : Boolean = false
)

enum class ServiceType(val id:Int) {
    DRUG_TESTING(R.string.drug_testing_center),
    CONSUMMING(R.string.conssuming_center),
    IST_TESTING(R.string.ist_center),
    NOT_SELECTED(R.string.no_center)
}
