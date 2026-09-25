package com.batchtrace.app.data.repository

import com.batchtrace.app.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AuthRepository {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun login(
        email: String,
        password: String,
        onSuccess: (User) -> Unit,
        onError: (String) -> Unit
    ) {
        firebaseAuth
            .signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->

                val firebaseUser = authResult.user

                if (firebaseUser == null) {
                    onError("Unable to retrieve authenticated user.")
                    return@addOnSuccessListener
                }

                loadUserProfile(
                    uid = firebaseUser.uid,
                    onSuccess = onSuccess,
                    onError = onError
                )
            }
            .addOnFailureListener { exception ->
                onError(
                    exception.localizedMessage
                        ?: "Authentication failed."
                )
            }
    }

    private fun loadUserProfile(
        uid: String,
        onSuccess: (User) -> Unit,
        onError: (String) -> Unit
    ) {
        firestore
            .collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener { document ->

                if (!document.exists()) {
                    firebaseAuth.signOut()
                    onError("User profile was not found.")
                    return@addOnSuccessListener
                }

                val user = document.toObject(User::class.java)

                if (user == null) {
                    firebaseAuth.signOut()
                    onError("Unable to read user profile.")
                    return@addOnSuccessListener
                }

                if (!user.active) {
                    firebaseAuth.signOut()
                    onError("This account is inactive.")
                    return@addOnSuccessListener
                }

                if (user.getUserRole() == null) {
                    firebaseAuth.signOut()
                    onError("This account has an invalid role.")
                    return@addOnSuccessListener
                }

                onSuccess(user)
            }
            .addOnFailureListener { exception ->
                firebaseAuth.signOut()

                onError(
                    exception.localizedMessage
                        ?: "Unable to load user profile."
                )
            }
    }

    fun logout() {
        firebaseAuth.signOut()
    }

    fun isUserLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }
}