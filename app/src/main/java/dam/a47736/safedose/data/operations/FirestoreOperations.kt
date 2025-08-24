package dam.a47736.safedose.data.operations

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import dam.a47736.safedose.data.CommunityCenter
import dam.a47736.safedose.data.Dose
import dam.a47736.safedose.data.Medication
import dam.a47736.safedose.data.UserData

class FirestoreOperations(
    private val db: FirebaseFirestore
) {
    private var userData : UserData? = null

    fun addUserData(userId: String, userData: UserData, onResult: (Boolean, String?) -> Unit) {

        db.collection("users").document(userId)
            .set(userData)
            .addOnCompleteListener { dbTask ->
                if (dbTask.isSuccessful) {
                    this.userData = userData
                    onResult(true, null)
                } else {
                    onResult(false, "Something went wrong")
                }
            }
    }

    fun getUserData(userId: String,
                    onSuccess: (UserData) -> Unit,
                    onFailure: () -> Unit
    ) {
        db.collection("users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                val data = document.toObject(UserData::class.java)
                if (data != null) {
                    onSuccess(data)
                } else {
                    onFailure()
                }
            }
            .addOnFailureListener {
                onFailure()
            }
    }

    fun addDailyMedication(userId: String,medication: Medication) {
        try{
            val userRef = db.collection("users").document(userId)
            userRef.update("dailyMedications", FieldValue.arrayUnion(medication))
        }catch (e: Exception){
            println("ERROR ADDING MED: "+e.printStackTrace())
        }
    }

    fun addDose(userId: String, dose: Dose){
        try{

            if(dose.userId.isBlank()){
                dose.userId = userId
            }

            db.collection("doses")
                .add(dose)
                .addOnFailureListener { e ->
                    println("Error on firestore addDose: ${e.message}")
                }

        }catch (e : Exception){
            println("Exception on addDose: ${e.message}")
        }
    }

    fun getDoses(
        userId: String,
        onSuccess: (List<Dose>) -> Unit,
        onFailure: (Exception) -> Unit
    ){
        db.collection("doses")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val doses = querySnapshot.documents.mapNotNull{ doc ->
                    doc.toObject(Dose::class.java)
                }
                onSuccess(doses)
            }
            .addOnFailureListener { onFailure(it) }
    }
}
