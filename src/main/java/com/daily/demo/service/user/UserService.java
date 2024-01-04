package com.daily.demo.service.user;

import com.daily.demo.controller.ifs.crudInterface;
import com.daily.demo.dto.request.UserRequest;
import com.daily.demo.dto.response.UserResponse;
import com.daily.demo.service.ifs.UserCrudIfs;

public interface UserService extends UserCrudIfs<UserRequest, UserResponse> {

}
