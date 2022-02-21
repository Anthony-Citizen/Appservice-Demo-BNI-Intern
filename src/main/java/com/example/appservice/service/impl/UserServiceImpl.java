package com.example.appservice.service.impl;

import com.example.appservice.dto.input.UserInput;
import com.example.appservice.dto.output.UserOutput;
import com.example.appservice.model.User;
import com.example.appservice.repository.UserRepository;
import com.example.appservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<UserOutput> getListUsers() {
        Iterable<User> users = userRepository.findAll();
        List<UserOutput> usersInfo = new ArrayList<>();
        for (User user : users){
            UserOutput userInfo = UserOutput.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .phone(user.getPhone())
                    .build();
            usersInfo.add(userInfo);
        }
        return usersInfo;
    }

    @Override
    public UserOutput getUserInfo(String username) {
        User user = userRepository.getDistinctTopByUsername(username);
        if (user==null)
            throw new RuntimeException("Not Found");
        UserOutput userInfo =  new UserOutput();
        userInfo.setId(user.getId());
        userInfo.setUsername(username);
        userInfo.setEmail(user.getEmail());
        userInfo.setPhone(user.getPhone());

        return userInfo;
    }

    @Override
    public void addOne(UserInput userInput) {
        User user = User.builder()
                .username(userInput.getUsername())
                .email(userInput.getEmail())
                .phone(userInput.getPhone())
                .build();
        try {
            userRepository.save(user);
        }catch (Exception e){
            throw new RuntimeException("Duplicated");
        }
    }
}
