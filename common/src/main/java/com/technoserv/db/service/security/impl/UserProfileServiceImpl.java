package com.technoserv.db.service.security.impl;

import java.util.List;

import com.technoserv.db.dao.security.api.UserProfileDao;
import com.technoserv.db.model.security.UserProfile;
import com.technoserv.db.service.AbstractService;
import com.technoserv.db.service.security.api.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userProfileService")
@Transactional
public class UserProfileServiceImpl extends AbstractService<Integer,UserProfile,UserProfileDao> implements UserProfileService {

    @Autowired
    @Qualifier("userProfileDao")
    public void setDao(UserProfileDao dao) {
        this.dao = dao;
    }

    public UserProfile findById(int id) {
        return dao.findById(id);
    }

    public UserProfile findByType(String type){
        return dao.findByType(type);
    }

    public List<UserProfile> findAll() {
        return dao.findAll();
    }
}