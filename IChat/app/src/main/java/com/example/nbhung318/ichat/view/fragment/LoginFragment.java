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

public class LoginFragment extends Fragment {
    private static final String TAG = LoginFragment.class.getName();

    private EditText edtEmailLogin;
    private EditText edtPasswordLogin;
    private Button btnLogin;
    private TextView txtRedirectRegister;
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_login, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        edtEmailLogin = rootView.findViewById(R.id.edtEmailLogin);
        edtPasswordLogin = rootView.findViewById(R.id.edtPasswordLogin);
        btnLogin = rootView.findViewById(R.id.btnLogin);
        txtRedirectRegister = rootView.findViewById(R.id.txtRedirectRegister);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmailLogin.getText().toString();
                String password = edtPasswordLogin.getText().toString();

                ((MainActivity) getActivity()).loginAccount(email, password);
            }
        });

        txtRedirectRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).openRegisterFragment();
            }
        });

    }

}

