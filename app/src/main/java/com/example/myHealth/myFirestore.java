package com.example.myHealth;

import com.google.firebase.firestore.FirebaseFirestore;

public class myFirestore {
    private static FirebaseFirestore instance;

    private myFirestore() {
        // Private constructor to prevent instantiation
    }


    //singleton db instance
    //call using FirebaseFirestore db = myFirestore.getInstance();
    public static synchronized FirebaseFirestore getInstance() {
        if (instance == null) {
            instance = FirebaseFirestore.getInstance();
        }
        return instance;
    }
}

