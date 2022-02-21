package com.example.appservice.service;

import com.example.appservice.dto.input.UserInput;
import com.example.appservice.dto.output.UserOutput;

import java.util.List;

public interface UserService {
    List<UserOutput> getListUsers();
    UserOutput getUserInfo(String username);
    void addOne(UserInput userInput);
}
