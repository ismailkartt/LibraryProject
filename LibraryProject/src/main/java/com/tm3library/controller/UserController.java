package com.tm3library.controller;

import com.tm3library.dto.request.UserRequest;
import com.tm3library.dto.response.LbResponse;
import com.tm3library.dto.response.PublisherResponse;
import com.tm3library.dto.response.ResponseMessage;
import com.tm3library.dto.response.UserResponse;
import com.tm3library.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //Get User With ID  - Page 50
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
    public ResponseEntity<UserResponse> getUserWithId(@PathVariable Long id){

        UserResponse userResponse= userService.findById(id);

        return ResponseEntity.ok(userResponse);
    }

    // Get Auth User - Page 47
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE') or hasRole('MEMBER')")
    public ResponseEntity<UserResponse> getAuthUser(){

       UserResponse userResponse= userService.getPrincipal();

       return ResponseEntity.ok(userResponse);
    }

    // GetUsersWithPage - Page 49
    @GetMapping("/pages")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
    public ResponseEntity<Page<UserResponse>> getAllUsersWithPage(@RequestParam("page") int page,
                                                                         @RequestParam("size") int size,
                                                                         @RequestParam("sort") String prop,
                                                                         @RequestParam(value = "type",
                                                                                 required = false,
                                                                                 defaultValue = "DESC") Sort.Direction type
    ){

        Pageable pageable = PageRequest.of(page, size, Sort.by(type, prop));

        Page<UserResponse> pageResponse= userService.findAllUsersWithPage(pageable);

        return ResponseEntity.ok(pageResponse);

    }

    // Create User - Page 51
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest userRequest){
        UserResponse userResponse = userService.creatUser(userRequest);
        return  ResponseEntity.ok(userResponse);
    }


    // Update User - Page 52
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id,
                                                   @Valid @RequestBody UserRequest userRequest ){

        UserResponse userResponse = userService.updateUser(id, userRequest);

        return ResponseEntity.ok(userResponse);

    }




    // Delete user - Page 53
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> deleteUser(@PathVariable Long id){

        UserResponse userResponse= userService.deleteUser(id);

        return ResponseEntity.ok(userResponse);

    }

    // User Loans - Page 48

    // !!! YAPILACAK !!!


}
