package com.example.jean.inventarioApp.services;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

public class Firebase {
    private static FirebaseAuth mAuth;
    private static FirebaseFirestore db;
    private static FirebaseStorage storage;

    public static FirebaseAuth getFirebaseAutenticacao() {
        if (mAuth == null) {
            mAuth = FirebaseAuth.getInstance();
        }
        return mAuth;
    }

    public static FirebaseFirestore getFirebaseDatabase() {
        if (db == null) {
            db = FirebaseFirestore.getInstance();
        }
        return db;
    }

    public static FirebaseStorage getFirebaseStorage() {
        if (storage == null) {
            storage = FirebaseStorage.getInstance();
        }
        return storage;
    }

}
