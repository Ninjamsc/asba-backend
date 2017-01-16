package com.technoserv.db.dao.security.api;

import com.technoserv.db.dao.Dao;
import com.technoserv.db.model.security.User;

import java.util.List;




public interface UserDao extends Dao<Integer,User> {

    User findById(int id);

    User findBySSO(String sso);

    void deleteBySSO(String sso);

    List<User> findAllUsers();

}