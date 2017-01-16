package com.technoserv.db.service.security.api;

import com.technoserv.db.model.security.UserProfile;

import java.util.List;




public interface UserProfileService {

    UserProfile findById(int id);

    UserProfile findByType(String type);

    List<UserProfile> findAll();

}