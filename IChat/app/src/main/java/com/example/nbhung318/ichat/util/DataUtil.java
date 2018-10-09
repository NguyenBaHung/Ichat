package com.example.nbhung318.ichat.util;


import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DataUtil {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference reference;

    private static DataUtil instance;

    public static DataUtil getInstance() {
        if (instance == null) {
            instance = new DataUtil();
        }
        return instance;
    }

    private DataUtil() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();
    }

    public Task<Void> saveUser(String userId, String userName) {
        reference.child("User").child(userId)
                .child("userImage").setValue("https://st2.depositphotos.com/3369547/11981/v/950/depositphotos_119816484-stockillustratie-panda-bear-kawaii-schattige-dieren.jpg");
        reference.child("User").child(userId)
                .child("userName").setValue(userName);
        Task<Void> task = reference.child("User").child(userId)
                .child("userStatus").setValue("Default Status");

        return task;
    }

    public DatabaseReference getUser(String userId) {
        return reference.child("User").child(userId);

    }


    public Task<Void> setUserImage(String userId, String urlImage) {
        return reference.child("User").child(userId).child("userImage").setValue(urlImage);
    }

    public Task<Void> changeUserStatus(String userId, String newStatus) {
        return reference.child("User").child(userId).child("userStatus").setValue(newStatus);
    }

    public DatabaseReference getAllUser(){
        return reference.child("User");
    }

    public DatabaseReference getFriendRequest(){
        return reference.child("FriendRequest");
    }

    public DatabaseReference getFriend(){
        return reference.child("friend");
    }

}
