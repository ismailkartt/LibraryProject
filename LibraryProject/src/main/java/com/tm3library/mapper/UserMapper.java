package com.tm3library.mapper;

import com.tm3library.domain.User;
import com.tm3library.dto.request.UserRequest;
import com.tm3library.dto.response.UserResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse userToUserResponse(User user);


    //User userRequestToUser(UserRequest userRequest);

    List<UserResponse> map(List<User> userList);

}
