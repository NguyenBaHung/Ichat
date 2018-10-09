package com.example.nbhung318.ichat.util;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthUtil {
    private static AuthUtil instance;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private Task<AuthResult> task;

    public static AuthUtil getInstance() {
        if (instance == null) {
            instance = new AuthUtil();
        }
        return instance;

    }

    private AuthUtil() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public FirebaseUser getCurrentUser(){
        firebaseUser = firebaseAuth.getCurrentUser();
        return firebaseUser;
    }

    public Task<AuthResult> signIn(String email,String password){
        task = firebaseAuth.signInWithEmailAndPassword(email,password);
        return task;
    }

    public Task<AuthResult> register(String email, String password){
        task = firebaseAuth.createUserWithEmailAndPassword(email, password);
        return task;
    }

    public String getCurrentUserId(){
        firebaseUser = firebaseAuth.getCurrentUser();
        return firebaseUser.getUid();
    }

    public void signOut(){
        firebaseAuth.signOut();
    }



}
