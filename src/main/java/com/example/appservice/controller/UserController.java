package com.example.appservice.controller;

import com.example.appservice.dto.input.UserInput;
import com.example.appservice.dto.output.UserOutput;
import com.example.appservice.dto.response.BaseResponse;
import com.example.appservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getListUsers(){
        try {
            List<UserOutput> usersList = userService.getListUsers();
            return ResponseEntity.ok(new BaseResponse<>(usersList));
        }catch (Exception e){
            if(e.getMessage().equalsIgnoreCase("Not Found")){
                return new ResponseEntity(new BaseResponse(Boolean.FALSE, "Username Not Found"), HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity(new BaseResponse(Boolean.FALSE, "Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/info/{username}")
    public ResponseEntity<BaseResponse<?>> getUserInfo(@PathVariable String username){
        try {
            UserOutput userInfo = userService.getUserInfo(username);
            return ResponseEntity.ok(new BaseResponse<>(userInfo));
        }catch (Exception e){
            if(e.getMessage().equalsIgnoreCase("Bad Credential")){
                return new ResponseEntity(new BaseResponse(Boolean.FALSE, "Bad Credential"), HttpStatus.UNAUTHORIZED);
            }
            return new ResponseEntity(new BaseResponse(Boolean.FALSE, "Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity addOne(@Valid @RequestBody UserInput userInput){
        try{
            userService.addOne(userInput);
            return ResponseEntity.ok(new BaseResponse<>(userInput));
        }catch (Exception e){
            if(e.getMessage().equalsIgnoreCase("Duplicated")){
                return new ResponseEntity(new BaseResponse(Boolean.FALSE, "Already Exist"), HttpStatus.CONFLICT);
            }
            return new ResponseEntity(new BaseResponse(Boolean.FALSE, "Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{userId}")
    public ResponseEntity update(@PathVariable Long userId, @Valid @RequestBody UserInput userInput) {
        try{
            if (userInput.getUsername() == null && userInput.getEmail() == null && userInput.getPhone() == null){
                return new ResponseEntity(new BaseResponse(Boolean.FALSE, "Bad Request"), HttpStatus.BAD_REQUEST);
            }

            return ResponseEntity.ok(userService.update(userId, userInput));
        }catch (Exception e){
            if(e.getMessage().equalsIgnoreCase("Not Found")){
                return new ResponseEntity(new BaseResponse(Boolean.FALSE, "Not Found"), HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity(new BaseResponse(Boolean.FALSE, "Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity delete(@PathVariable Long userId) {
        try{
            userService.delete(userId);
            return new ResponseEntity(new BaseResponse(Boolean.TRUE, "Deleted Successfully"), HttpStatus.OK);
        }catch (Exception e){
            if(e.getMessage().equalsIgnoreCase("Not Found")){
                return new ResponseEntity(new BaseResponse(Boolean.FALSE, "Not Found"), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity(new BaseResponse(Boolean.FALSE, "Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
