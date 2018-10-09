package com.example.nbhung318.ichat.view.activity;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nbhung318.ichat.R;
import com.example.nbhung318.ichat.entity.User;
import com.example.nbhung318.ichat.key.RequestCode;
import com.example.nbhung318.ichat.util.AuthUtil;
import com.example.nbhung318.ichat.util.DataUtil;
import com.example.nbhung318.ichat.util.StorageUtil;
import com.example.nbhung318.ichat.view.fragment.AllUserFragment;
import com.example.nbhung318.ichat.view.fragment.LoginFragment;
import com.example.nbhung318.ichat.view.fragment.ProfileFragment;
import com.example.nbhung318.ichat.view.fragment.ProfileOwnerFragment;
import com.example.nbhung318.ichat.view.fragment.ProgramFragment;
import com.example.nbhung318.ichat.view.fragment.RegisterFragment;
import com.example.nbhung318.ichat.view.fragment.WaitFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.UUID;


public class MainActivity extends AppCompatActivity {
    private AuthUtil authUtil;
    private DataUtil dataUtil;
    private StorageUtil storageUtil;
    private String currentUserId;
    private User user;
    private String userId;

    private WaitFragment waitFragment;
    private LoginFragment loginFragment;
    private RegisterFragment registerFragment;
    private ProgramFragment programFragment;
    private AllUserFragment allUserFragment;
    private ProfileFragment profileFragment;
    private ProfileOwnerFragment profileOwnerFragment;

    private Dialog changeStatusDialog;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponent();
    }

    private void initComponent() {
        authUtil = AuthUtil.getInstance();
        dataUtil = DataUtil.getInstance();
        storageUtil = StorageUtil.getInstance();
        requestPermisstion();
        openWaitFragment();
        if (authUtil.getCurrentUser() == null) {
            openLoginFragment();
        } else {
            openProgramFragment();
        }

    }

    public void loginAccount(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(MainActivity.this, "Pleae write email", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(MainActivity.this, "Pleae write password", Toast.LENGTH_SHORT).show();
        } else {
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Login your user");
            progressDialog.setMessage("Please wait util complete");
            progressDialog.setIcon(R.drawable.ic_forward_24dp);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            authUtil.signIn(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        openProgramFragment();
                    } else {
                        Toast.makeText(MainActivity.this, "Please check your emal and password", Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                }
            });

        }

    }

    public void registerAccount(String email, String password, final String name) {
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(MainActivity.this, "Pleae write email", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(MainActivity.this, "Pleae write password", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(MainActivity.this, "Pleae write name", Toast.LENGTH_SHORT).show();
        } else {
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Create new account!");
            progressDialog.setMessage("Please wait util complete");
            progressDialog.setIcon(R.drawable.ic_add_alert_24dp);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            Task<AuthResult> task = authUtil.register(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                currentUserId = authUtil.getCurrentUserId();
                                dataUtil.saveUser(currentUserId, name).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        openProgramFragment();
                                    }
                                });

                            }
                            progressDialog.dismiss();
                        }
                    });
        }
    }


    private void openWaitFragment() {
        if (waitFragment == null) {
            waitFragment = new WaitFragment();
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, waitFragment)
                .commit();
    }

    public void openLoginFragment() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (loginFragment == null) {
                    loginFragment = new LoginFragment();
                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content, loginFragment)
                        .commit();
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    public void openRegisterFragment() {
        if (registerFragment == null) {
            registerFragment = new RegisterFragment();
        }
        getSupportFragmentManager().beginTransaction()
                .add(R.id.content, registerFragment)
                .addToBackStack(null)
                .commit();
    }

    public void openProgramFragment() {
        if (programFragment == null) {
            programFragment = new ProgramFragment();
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, programFragment)
                .commit();

    }

    public void popBackStack() {
        getSupportFragmentManager().popBackStack();
    }

    public void changeUserImage() {
        if (!requestPermisstion()) {
            Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
            galleryIntent.setType("image/*");
            startActivityForResult(galleryIntent, RequestCode.REQUEST_GALLERY);
        }
    }

    public void changeUserStatus(String old_status) {
        changeStatusDialog = new Dialog(this);
        changeStatusDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        changeStatusDialog.setContentView(R.layout.dialog_change_status);
        final EditText edtStatus = changeStatusDialog.findViewById(R.id.edtStatus);
        Button btnSave = changeStatusDialog.findViewById(R.id.btnSave);
        edtStatus.setText(old_status);

        changeStatusDialog.setCanceledOnTouchOutside(true);
        changeStatusDialog.setCancelable(false);
        changeStatusDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        changeStatusDialog.show();


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newStatus = edtStatus.getText().toString();
                addUserStatus(newStatus);
            }
        });
    }

    public void addUserStatus(String newStatus) {
        if (TextUtils.isEmpty(newStatus)) {
            Toast.makeText(this, "Please write your status", Toast.LENGTH_SHORT).show();
        } else {
            progressDialog = new ProgressDialog(this);
            progressDialog.setIcon(R.drawable.ic_text_format_24dp);
            progressDialog.setTitle("Change User Status");
            progressDialog.setMessage("Please wait, while updating your user status");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(true);

            dataUtil.changeUserStatus(currentUserId, newStatus).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        changeStatusDialog.dismiss();
                    }
                }
            });
        }
    }


    public void logOut() {
        authUtil.signOut();
        openLoginFragment();
    }

    public void getProfileUser() {
        currentUserId = authUtil.getCurrentUserId();
        dataUtil.getUser(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String image = dataSnapshot.child("userImage").getValue().toString();
                String name = dataSnapshot.child("userName").getValue().toString();
                String status = dataSnapshot.child("userStatus").getValue().toString();

                programFragment.getProfileFragment().settingProfileUser(image,name,status);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RequestCode.REQUEST_GALLERY:
                if (resultCode == RESULT_OK && data != null) {
                    Uri image = data.getData();
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setAspectRatio(1, 1)
                            .start(this);


                }
            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    Uri resultUri = null;
                    if (result != null) {
                        resultUri = result.getUri();
                    }
                    String userId = authUtil.getCurrentUserId();
                    String uuid = UUID.randomUUID().toString();
                    storageUtil.putImageProfile(userId, resultUri,uuid).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                String urlImage = task.getResult().getDownloadUrl().toString();
                                dataUtil.setUserImage(currentUserId, urlImage).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(MainActivity.this, "Image Uploading Successfully", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Toast.makeText(MainActivity.this, "Error uploading profile image", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }
        }
    }

    public boolean requestPermisstion() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return false;
        } else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, RequestCode.REQUEST_PERMISSTION);
            return true;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case RequestCode.REQUEST_PERMISSTION:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    requestPermisstion();
                }
        }

    }

    public void getAllUser(){
        dataUtil.getAllUser().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                String userId = dataSnapshot.getKey();
                allUserFragment.initComponent(user, userId);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setUser(User user){
        this.user = user;
    }

    public User getUser(){
        return user;
    }

    public void setUserId(String userId){
        this.userId = userId;
    }

    public String getUserId(){
        return userId;
    }

    public String getCurrentId(){
        return authUtil.getCurrentUserId();
    }

    public void openAllUserFragment(){

    }

    public void openProfileOwnerFragment() {

    }

    public void sendFriendRequest(){
        profileOwnerFragment.sendFriendRequest(dataUtil.getFriendRequest());
    }

    public void cancleFriendRequest(){
        profileOwnerFragment.cancleFriendRequest(dataUtil.getFriendRequest());
    }

    public void checkRequestFriend(){
        profileOwnerFragment.checkRequestFriend(dataUtil.getFriendRequest(),dataUtil.getFriend());
    }

    public void acceptRequestFriend(){
        profileOwnerFragment.acceptFriendRequest(dataUtil.getFriend(),dataUtil.getFriendRequest());
    }

    public void unFriend(){
        profileOwnerFragment.unFriend(dataUtil.getFriend());
    }


}

