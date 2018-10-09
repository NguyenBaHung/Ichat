package com.example.nbhung318.ichat.view.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nbhung318.ichat.R;
import com.example.nbhung318.ichat.entity.User;
import com.example.nbhung318.ichat.view.activity.MainActivity;
import com.example.nbhung318.ichat.view.adapter.AllUserAdapter;
import com.example.nbhung318.ichat.viewmodel.UserManager;

import java.util.ArrayList;
import java.util.List;

public class AllUserFragment extends Fragment implements AllUserAdapter.OnClickedListener{
    private View rootView;
    private RecyclerView rcvAllUser;

    private UserManager userManager;
    private AllUserAdapter allUserAdapter;
    private List<String> userIds;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_all_user, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userManager = new UserManager();
        userIds = new ArrayList<>();
        ((MainActivity) getActivity()).getAllUser();
        allUserAdapter = new AllUserAdapter(getActivity(),userManager.getUserList());

        rcvAllUser = rootView.findViewById(R.id.rcvAllUser);
        rcvAllUser.setHasFixedSize(true);
        rcvAllUser.setLayoutManager(new GridLayoutManager(getActivity(),3));
        rcvAllUser.setAdapter(allUserAdapter);
    }

    public void initComponent(User user, String userId){
        userManager.addUser(user);
        userIds.add(userId);
    }


    @Override
    public void onClicked(int position) {
        ((MainActivity) getActivity()).setUser(userManager.getUserList().get(position));
        ((MainActivity) getActivity()).setUserId(userIds.get(position));
        ((MainActivity) getActivity()).openProfileOwnerFragment();
    }
}
