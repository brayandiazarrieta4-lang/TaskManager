package com.example.taskmanager.data.repository

import com.example.taskmanager.domain.model.User
import com.example.taskmanager.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override suspend fun register(
        email: String,
        password: String
    ): Result<User> {

        return try {

            val result = firebaseAuth
                .createUserWithEmailAndPassword(
                    email,
                    password
                )
                .await()

            val firebaseUser = result.user
                ?: return Result.failure(
                    Exception("Usuario no encontrado")
                )

            Result.success(
                User(
                    uid = firebaseUser.uid,
                    email = firebaseUser.email ?: email
                )
            )

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    override suspend fun login(
        email: String,
        password: String
    ): Result<User> {

        return try {

            val result = firebaseAuth
                .signInWithEmailAndPassword(
                    email,
                    password
                )
                .await()

            val firebaseUser = result.user
                ?: return Result.failure(
                    Exception("Usuario no encontrado")
                )

            Result.success(
                User(
                    uid = firebaseUser.uid,
                    email = firebaseUser.email ?: email
                )
            )

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    override fun logout() {
        firebaseAuth.signOut()
    }

    override fun getCurrentUser(): User? {

        val firebaseUser = firebaseAuth.currentUser

        return firebaseUser?.let {
            User(
                uid = it.uid,
                email = it.email ?: ""
            )
        }
    }
}