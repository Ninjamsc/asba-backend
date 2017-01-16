package com.technoserv.db.dao.security.api;

/**
 * Created by mlevitin on 16.01.17.
 */
import com.technoserv.db.model.security.UserProfile;

import java.util.List;



public interface UserProfileDao {

    List<UserProfile> findAll();

    UserProfile findByType(String type);

    UserProfile findById(int id);
}