package com.technoserv.db.service.objectmodel.impl;

import com.technoserv.db.model.configuration.SystemSettings;
import com.technoserv.db.model.configuration.SystemSettingsType;
import com.technoserv.db.model.objectmodel.BioTemplateType;
import com.technoserv.db.model.objectmodel.DocumentType;
import com.technoserv.db.model.security.User;
import com.technoserv.db.model.security.UserProfile;
import com.technoserv.db.model.security.UserProfileType;
import com.technoserv.db.service.configuration.api.SystemSettingService;
import com.technoserv.db.service.objectmodel.api.BioTemplateTypeService;
import com.technoserv.db.service.objectmodel.api.DocumentTypeService;
import com.technoserv.db.service.security.api.UserService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by sergey on 23.11.2016.
 */
@Service("initialDataBuilder") //TODO ...
public class InitialDataBuilder implements InitializingBean {

    @Autowired
    private DocumentTypeService documentTypeService;

    @Autowired
    private BioTemplateTypeService bioTemplateTypeService;

    @Autowired
    private SystemSettingService systemSettingService;

    @Autowired
    private UserService userService;

    public void afterPropertiesSet() throws Exception {

        if (documentTypeService.countAll() == 0) {
            for (DocumentType.Type type : DocumentType.Type.values()) {
                documentTypeService.save(new DocumentType(type, type.toString()));
            }
        }

        if (bioTemplateTypeService.countAll() == 0) {
            for (BioTemplateType.Type type : BioTemplateType.Type.values()) {
                BioTemplateType bioTemplateType = new BioTemplateType();
                bioTemplateType.setDescription(type.getDescription());
                bioTemplateType.setId(type.getValue());
                bioTemplateTypeService.save(bioTemplateType);
            }
        }

        if (systemSettingService.countAll() != SystemSettingsType.values().length) {
            for (SystemSettingsType systemSettingsType : SystemSettingsType.values()) {
                if (systemSettingService.findById(systemSettingsType) == null) {
                    SystemSettings systemSettings = new SystemSettings();
                    systemSettings.setId(systemSettingsType);
                    systemSettings.setValue(systemSettingsType.getDefaultValue());
                    systemSettingService.saveOrUpdate(systemSettings);
                }
            }
        }

        if (userService.countAll() == 0) {
            User user = new User();
            Set<UserProfile> userProfileSet = new HashSet<UserProfile>();
            user.setSsoId("read");
            user.setPassword("read");
            UserProfile userProfile = new UserProfile();
            userProfile.setType(UserProfileType.READ.name());
            userProfileSet.add(userProfile);
            user.setUserProfiles(userProfileSet);
            user.setFirstName("Read-Only user");
            user.setLastName("Read-only user");
            user.setEmail("read@nowhere.com");
            userService.saveOrUpdate(user);


            User admin = new User();
            Set<UserProfile> adminProfileSet = new HashSet<UserProfile>();
            admin.setSsoId("admin");
            admin.setPassword("admin");
            UserProfile adminProfile = new UserProfile();
            adminProfile.setType(UserProfileType.ADMIN.name());
            adminProfileSet.add(adminProfile);
            admin.setUserProfiles(adminProfileSet);
            admin.setFirstName("Administrator");
            admin.setLastName("Administrator");
            admin.setEmail("admin@nowhere.com");
            userService.saveOrUpdate(admin);
        }
    }
}