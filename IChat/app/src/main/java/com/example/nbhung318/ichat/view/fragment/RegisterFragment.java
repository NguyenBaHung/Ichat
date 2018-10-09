package com.example.nbhung318.ichat.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.nbhung318.ichat.R;
import com.example.nbhung318.ichat.view.activity.MainActivity;

public class RegisterFragment extends Fragment {

    private EditText edtEmailRegister;
    private EditText edtPasswordRegister;
    private EditText edtNameRegister;
    private Button btnRegister;
    private TextView txtRedirectLogin;

    private View rootView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_register,container,false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView(){
        edtEmailRegister = rootView.findViewById(R.id.edtEmailRegister);
        edtPasswordRegister = rootView.findViewById(R.id.edtPasswordRegister);
        edtNameRegister = rootView.findViewById(R.id.edtNameRegister);
        btnRegister = rootView.findViewById(R.id.btnRegister);
        txtRedirectLogin = rootView.findViewById(R.id.txtRedirectLogin);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmailRegister.getText().toString();
                String password = edtPasswordRegister.getText().toString();
                String name = edtNameRegister.getText().toString();

                ((MainActivity) getActivity()).registerAccount(email,password,name);
            }
        });

        txtRedirectLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).popBackStack();
            }
        });
    }
}
