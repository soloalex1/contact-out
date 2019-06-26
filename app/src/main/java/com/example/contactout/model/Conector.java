package com.example.contactout.model;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Conector {

    private static FirebaseAuth auth;
    private static FirebaseUser usr;
    private static FirebaseAuth.AuthStateListener authListener;

    private Conector(){}

    public static FirebaseAuth getFirebaseAuth(){
        if(auth == null){
            startAuth();
        } return auth;
    }

    private static void startAuth(){
        auth = FirebaseAuth.getInstance();
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null) usr = user;
            }
        };

        auth.addAuthStateListener(authListener);
    }

    public static FirebaseUser getFirebaseUser(){
        return usr;
    }

    public static void logOut(){
        auth.signOut();
    }
}
