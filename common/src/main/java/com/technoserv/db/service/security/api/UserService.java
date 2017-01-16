package com.technoserv.db.service.security.api;

import com.technoserv.db.model.security.User;
import com.technoserv.db.service.Service;

import java.util.List;




public interface UserService extends Service<Integer,User>{

    User findById(int id);

    User findBySSO(String sso);

    void saveUser(User user);

    void updateUser(User user);

    void deleteUserBySSO(String sso);

    List<User> findAllUsers();

    boolean isUserSSOUnique(Integer id, String sso);

}