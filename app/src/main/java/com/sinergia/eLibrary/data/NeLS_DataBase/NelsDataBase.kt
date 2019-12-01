package com.sinergia.eLibrary.data.NeLS_DataBase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class NelsDataBase {

    val nelsDB= FirebaseFirestore.getInstance()

    fun addUser(name: String, lastName: String, email: String, password: String){

        val newUser: HashMap<String, Any> = hashMapOf(
            "name" to name,
            "lastName" to lastName,
            "email" to email,
            "password" to password
        )

        nelsDB
            .collection("users")
            .add(newUser)
            .addOnCompleteListener{ adduser ->
                if(adduser.isSuccessful){

                } else {

                }
            }

    }

    fun setUser(field: String, newValue: String){

        val userAccount = FirebaseAuth.getInstance().currentUser?.email.toString().trim()

        when(field){
        }

    }

    fun addResource(){

    }

    fun setResource(){

    }



}