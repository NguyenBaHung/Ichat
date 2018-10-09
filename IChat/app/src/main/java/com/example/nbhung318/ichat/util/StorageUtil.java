package com.example.nbhung318.ichat.util;

import android.net.Uri;
import android.util.Log;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class StorageUtil {
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private static StorageUtil instance;

    public static StorageUtil getInstance() {
        if (instance == null) {
            instance = new StorageUtil();
        }
        return instance;
    }

    private StorageUtil() {
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
    }

    public UploadTask putImageProfile(String userId, Uri resultUri, String uuid){
        StorageReference filePath = storageReference.child("profileImage").child(userId + uuid + ".jpg");
        return filePath.putFile(resultUri);
    }


}
