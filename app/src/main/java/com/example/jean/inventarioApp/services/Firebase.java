package com.example.jean.inventarioApp.services;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

public class Firebase {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseStorage storage;

    public FirebaseAuth getFirebaseAutenticacao() {
        if (mAuth == null) {
            mAuth = FirebaseAuth.getInstance();
        }
        return mAuth;
    }

    public FirebaseFirestore getFirebaseDatabase() {
        if (db == null) {
            db = FirebaseFirestore.getInstance();
        }
        return db;
    }

    public FirebaseStorage getFirebaseStorage() {
        if (storage == null) {
            storage = FirebaseStorage.getInstance();
        }
        return storage;
    }

}
