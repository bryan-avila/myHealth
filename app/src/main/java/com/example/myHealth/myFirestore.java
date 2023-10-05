package com.example.myHealth;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class myFirestore {
    private static FirebaseFirestore DBinstance;
    private static FirebaseAuth mAuthinstance;

    private myFirestore() {
        // Private constructor to prevent instantiation
    }


    //singleton db instance
    //call using FirebaseFirestore db = myFirestore.getDBInstance();
    public static synchronized FirebaseFirestore getDBInstance() {
        if (DBinstance == null) {
            DBinstance = FirebaseFirestore.getInstance();
        }
        return DBinstance;
    }

    //singleton db instance
    //call using FirebaseAuth mAuth = myFirestore.getmAuthInstance();
    public static synchronized FirebaseAuth getmAuthInstance() {
        if (mAuthinstance == null) {
            mAuthinstance = FirebaseAuth.getInstance();
        }
        return mAuthinstance;
    }
}

