package com.example.nbhung318.ichat.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.nbhung318.ichat.R;
import com.example.nbhung318.ichat.view.adapter.ProgramAdapter;
import com.example.nbhung318.ichat.view.activity.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProgramFragment extends Fragment {
    private View rootView;
    private ViewPager vpProgram;
    private TabLayout tloProgram;
    private ProgramAdapter programAdapter;

    private ChatFragment chatFragment;
    private ProfileFragment profileFragment;
    private CheckFriendFragment checkFriendFragment;
    private FriendFragment friendFragment;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_program, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        vpProgram = rootView.findViewById(R.id.vpProgram);
        tloProgram = rootView.findViewById(R.id.tloProgram);
        if (chatFragment == null){
            chatFragment = new ChatFragment();
        }
        if (checkFriendFragment == null){
            checkFriendFragment = new CheckFriendFragment();
        }
        if (friendFragment == null){
            friendFragment = new FriendFragment();
        }
        if (profileFragment == null){
            profileFragment = new ProfileFragment();
        }

        programAdapter = new ProgramAdapter(getFragmentManager());
        programAdapter.addFragment(chatFragment, "Chat");
        programAdapter.addFragment(friendFragment, "Friend");
        programAdapter.addFragment(checkFriendFragment, "Check");
        programAdapter.addFragment(profileFragment, "Profile");

        vpProgram.setAdapter(programAdapter);
        tloProgram.setupWithViewPager(vpProgram);
        tloProgram.getTabAt(0).setIcon(R.drawable.ic_chat_24dp);
        tloProgram.getTabAt(1).setIcon(R.drawable.ic_format_list_24dp);
        tloProgram.getTabAt(2).setIcon(R.drawable.ic_spellcheck_24dp);
        tloProgram.getTabAt(3).setIcon(R.drawable.ic_account_circle_24dp);

    }

    public ProfileFragment getProfileFragment(){
        return profileFragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

    private void logoutUser() {
        ((MainActivity) getActivity()).openLoginFragment();
    }
}
