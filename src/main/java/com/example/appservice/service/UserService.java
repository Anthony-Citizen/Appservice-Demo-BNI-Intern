package com.example.appservice.service;

import com.example.appservice.dto.input.UserInput;
import com.example.appservice.dto.output.UserOutput;
import com.example.appservice.model.User;

import java.util.List;

public interface UserService {
    List<UserOutput> getListUsers();
    UserOutput getUserInfo(String username);
    User update(Long userId, UserInput userInput);
    void addOne(UserInput userInput);
    void delete(Long userId);
}
