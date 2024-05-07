package org.example.largent.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.largent.API.ApiResponse;
import org.example.largent.Model.User;
import org.example.largent.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/get")
    public ResponseEntity getUsers(){
        return ResponseEntity.status(200).body(userService.getAllUsers());
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid User user, Errors errors){
        if (errors.hasErrors()){
            String message=errors.getFieldError().getDefaultMessage();
            return ResponseEntity.badRequest().body(message);
        }

        userService.register(user);
        return ResponseEntity.ok().body(new ApiResponse("User has registered successfully"));
    }


    @PutMapping("/update/{id}")
    public ResponseEntity updateUser(@PathVariable Integer id, @RequestBody @Valid User user, Errors errors){
        if (errors.hasErrors()){
            String message=errors.getFieldError().getDefaultMessage();
            return ResponseEntity.badRequest().body(message);
        }
        userService.updateUser(id,user);
        return ResponseEntity.ok().body(new ApiResponse("User information has been updated"));

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteUser(@PathVariable Integer id){
        userService.deleteUser(id);
        return ResponseEntity.ok().body(new ApiResponse("User information has been deleted"));

    }

    //                  EXTRA

    @PostMapping("/login/{username}/{password}")
    public ResponseEntity logIn(@PathVariable String username,@PathVariable String password ){
        userService.logIn(username,password);
        return ResponseEntity.ok().body(new ApiResponse("Login successful"));
    }

    @PostMapping("/connect-member/{userId}/{teenId}")
    public ResponseEntity connectMember(@PathVariable Integer userId, @PathVariable Integer teenId){
        userService.connectMember(userId,teenId);
        return ResponseEntity.ok().body(new ApiResponse("Family member has been connected to your account"));
    }

    @PostMapping("/request/{userId}/{amount}")
    public ResponseEntity requestMoney(@PathVariable Integer userId, @PathVariable Double amount){
        userService.requestMoney(userId,amount);
        return ResponseEntity.ok().body(new ApiResponse("Requesting Parent with amount: "+amount));
    }

    @PostMapping("/set-limit/{teenId}/{weeklyLimit}")
    public ResponseEntity setWeeklyLimit(@PathVariable Integer teenId, @PathVariable Double weeklyLimit){
        userService.setWeeklyLimit(teenId,weeklyLimit);
        return ResponseEntity.ok().body(new ApiResponse("Weekly Limit has been set"));
    }

    @GetMapping("/set-budget/{parentId}/{teenId}/{budget}")
    public ResponseEntity setBudget(@PathVariable Integer parentId,@PathVariable Integer teenId,@PathVariable Double budget){
        userService.setBudget(parentId,teenId,budget);

        return ResponseEntity.ok().body(new ApiResponse("Monthly Budget has been set"));
    }

    @GetMapping("/get-family/{parentId}")
    public ResponseEntity getParentFamily(@PathVariable Integer parentId){
        return ResponseEntity.ok().body(userService.getParentFamily(parentId));
    }


}
