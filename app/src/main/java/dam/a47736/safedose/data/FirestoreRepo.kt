package dam.a47736.safedose.data

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import dam.a47736.safedose.data.operations.FirestoreOperations
import dam.a47736.safedose.data.operations.GeminiOperations

object FirestoreRepo {
    val firestore = Firebase.firestore
    val firestoreOperations = FirestoreOperations(firestore)
    val geminiOperations = GeminiOperations()
}