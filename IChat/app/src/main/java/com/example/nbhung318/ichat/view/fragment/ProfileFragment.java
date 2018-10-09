package com.example.nbhung318.ichat.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.nbhung318.ichat.R;
import com.example.nbhung318.ichat.entity.User;
import com.example.nbhung318.ichat.view.activity.MainActivity;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    private View rootView;
    private CircleImageView imgUserImage;
    private TextView txtUserName;
    private TextView txtUserStatus;
    private Button btnChangeUserImage;
    private Button btnChangeUserStatus;
    private Button btnLogout;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imgUserImage = rootView.findViewById(R.id.imgUserImage);
        txtUserName = rootView.findViewById(R.id.txtUserName);
        txtUserStatus = rootView.findViewById(R.id.txtUserStatus);
        btnChangeUserImage = rootView.findViewById(R.id.btnChangeUserImage);
        btnChangeUserStatus = rootView.findViewById(R.id.btnChangeUserStatus);
        btnLogout = rootView.findViewById(R.id.btnLogout);

        ((MainActivity) getActivity()).getProfileUser();

        btnChangeUserStatus.setOnClickListener(this);
        btnChangeUserImage.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnChangeUserImage:
                ((MainActivity) getActivity()).changeUserImage();
                break;
            case R.id.btnChangeUserStatus:
                ((MainActivity) getActivity()).changeUserStatus(txtUserStatus.getText().toString());
                break;
            case R.id.btnLogout:
                ((MainActivity) getActivity()).logOut();
        }
    }

    public void settingProfileUser(String userImage, String userName, String userStatus) {
        Picasso.get().load(userImage).into(imgUserImage);
        txtUserName.setText(userName);
        txtUserStatus.setText(userStatus);
    }


}
