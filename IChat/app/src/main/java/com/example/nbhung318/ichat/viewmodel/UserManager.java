package com.example.nbhung318.ichat.viewmodel;

import com.example.nbhung318.ichat.entity.User;

import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private List<User> userList;

    public UserManager(){
        userList = new ArrayList<>();
    }

    public void addUser(User user){
        userList.add(user);
    }

    public List<User> getUserList() {
        return userList;
    }
}
