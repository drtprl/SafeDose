package dam.a47736.safedose.data

/*
UserDate will be stored in userData collection in firestore.
Every single user will have a unique id, later we are going
to be able to find the user by its id using queries.
 */

data class UserData(
    var name: String = "",
    var email: String = "",
    var userId: String = "",
    var favoriteCommunityCenter : CommunityCenter? = null,
    var dailyMedications: List<Medication> = emptyList()
)

data class Medication(
    var name: String = "",
    var dosage: Int = 0
)
