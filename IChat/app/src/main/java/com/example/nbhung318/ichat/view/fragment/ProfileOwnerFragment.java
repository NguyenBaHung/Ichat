package com.example.nbhung318.ichat.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.nbhung318.ichat.R;
import com.example.nbhung318.ichat.entity.User;
import com.example.nbhung318.ichat.view.activity.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileOwnerFragment extends Fragment implements View.OnClickListener {
    private View rootView;
    private CircleImageView imgAvatarOwner;
    private TextView txtNameOwner;
    private TextView txtStatusOwner;
    private Button btnSendFriendRequest;
    private Button btnDeclineFriendRequest;

    private String STATE_NOT_FRIEND = "not friend";
    private String STATE_FRIEND_REQUEST = "request friend";
    private String STATE_FRIEND_ACCEPT = "accept friend";
    private String STATE_FRIEND = "friend";

    private String currentState;
    private String receiverUserId;
    private String sendUserID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_owner, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imgAvatarOwner = rootView.findViewById(R.id.imgAvatarOwner);
        txtNameOwner = rootView.findViewById(R.id.txtNameOwner);
        txtStatusOwner = rootView.findViewById(R.id.txtStatusOwner);
        btnSendFriendRequest = rootView.findViewById(R.id.btnSendFriendRequest);
        btnDeclineFriendRequest = rootView.findViewById(R.id.btnDeclineFriendRequest);


        if (!sendUserID.equals(receiverUserId)){
            btnSendFriendRequest.setOnClickListener(this);
        }else {
            btnDeclineFriendRequest.setVisibility(View.INVISIBLE);
            btnSendFriendRequest.setVisibility(View.INVISIBLE);
        }
        btnDeclineFriendRequest.setOnClickListener(this);
        initComponent();
    }

    public void initComponent() {
        User user = ((MainActivity) getActivity()).getUser();
        receiverUserId = ((MainActivity) getActivity()).getUserId();
        sendUserID = ((MainActivity) getActivity()).getCurrentId();
        Picasso.get().load(user.getUserImage()).into(imgAvatarOwner);
        txtNameOwner.setText(user.getUserName());
        txtStatusOwner.setText(user.getUserStatus());

        ((MainActivity) getActivity()).checkRequestFriend();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSendFriendRequest:
                btnSendFriendRequest.setEnabled(false);
                if (currentState.equals(STATE_NOT_FRIEND)) {
                    ((MainActivity) getActivity()).sendFriendRequest();
                }
                if (currentState.equals(STATE_FRIEND_REQUEST)) {
                    ((MainActivity) getActivity()).cancleFriendRequest();
                }
                if (currentState.equals(STATE_FRIEND_ACCEPT)) {
                    ((MainActivity) getActivity()).acceptRequestFriend();
                }
                if (currentState.equals(STATE_FRIEND)){
                    ((MainActivity) getActivity()).unFriend();
                }
                break;
            case R.id.btnDeclineFriendRequest:
                break;
            default:
                break;
        }
    }

    public void checkRequestFriend(DatabaseReference friendRequestReference, final DatabaseReference friendReference) {
        friendRequestReference.child(sendUserID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.hasChild(receiverUserId)) {
                        String requestType = dataSnapshot.child(receiverUserId).child("requestType").getValue().toString();

                        if (requestType.equals("send")) {
                            currentState = STATE_FRIEND_REQUEST;
                            btnSendFriendRequest.setText("Cancle Friend");
                        } else if (requestType.equals("receiver")) {
                            currentState = STATE_FRIEND_ACCEPT;
                            btnSendFriendRequest.setText("Accept Friend Request");
                        }
                    }

                }
                else {
                    friendReference.child(sendUserID)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.hasChild(receiverUserId)){
                                        currentState = STATE_FRIEND;
                                        btnSendFriendRequest.setText("Unfriend");
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void sendFriendRequest(final DatabaseReference friendRequestReference) {
        friendRequestReference.child(sendUserID).child(receiverUserId).child("requestType")
                .setValue("send").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    friendRequestReference.child(receiverUserId).child(sendUserID).child("requestType")
                            .setValue("receiver").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                btnSendFriendRequest.setEnabled(true);
                                currentState = STATE_FRIEND_REQUEST;
                                btnSendFriendRequest.setText("Cancle Friend");
                                btnSendFriendRequest.setCompoundDrawables(null,
                                        getActivity().getResources().getDrawable(R.drawable.ic_sentiment_24dp)
                                        , null, null);

                            }
                        }
                    });
                }
            }
        });
    }

    public void cancleFriendRequest(final DatabaseReference friendRequestReference) {
        friendRequestReference.child(sendUserID).child(receiverUserId)
                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    friendRequestReference.child(receiverUserId).child(sendUserID)
                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                btnSendFriendRequest.setEnabled(true);
                                currentState = STATE_NOT_FRIEND;
                                btnSendFriendRequest.setText("Send Friend Request");
                            }
                        }
                    });
                }
            }
        });
    }

    public void acceptFriendRequest(final DatabaseReference friendReference, final DatabaseReference friendRequestReference) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        final String saveCurrentDate = currentDate.format(calendar);
        friendReference.child(sendUserID).child(receiverUserId).
                setValue(saveCurrentDate).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                friendReference.child(receiverUserId).child(sendUserID)
                        .setValue(saveCurrentDate).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        friendRequestReference.child(sendUserID).child(receiverUserId)
                                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    friendRequestReference.child(receiverUserId).child(sendUserID)
                                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                btnSendFriendRequest.setEnabled(true);
                                                currentState = STATE_FRIEND;
                                                btnSendFriendRequest.setText("Unfriend");
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                });
            }
        });
    }

    public void unFriend(final DatabaseReference friendReference){
        friendReference.child(sendUserID).child(receiverUserId)
                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    friendReference.child(receiverUserId).child(sendUserID).removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        btnSendFriendRequest.setEnabled(true);
                                        currentState = STATE_NOT_FRIEND;
                                        btnSendFriendRequest.setText("Send Friend Request");
                                    }
                                }
                            });
                }
            }
        });
    }
}
