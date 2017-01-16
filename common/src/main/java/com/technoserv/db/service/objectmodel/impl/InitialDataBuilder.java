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

        if(documentTypeService.countAll()==0) {
            for (DocumentType.Type type : DocumentType.Type.values()) {
                documentTypeService.save(new DocumentType(type, type.toString()));
            }
        }

        if(bioTemplateTypeService.countAll()==0) {
            for (BioTemplateType.Type type : BioTemplateType.Type.values()) {
                BioTemplateType bioTemplateType = new BioTemplateType();
                bioTemplateType.setDescription(type.getDescription());
                bioTemplateType.setId(type.getValue());
                bioTemplateTypeService.save(bioTemplateType);
            }
        }

        if(systemSettingService.countAll() != SystemSettingsType.values().length) {
            for (SystemSettingsType systemSettingsType : SystemSettingsType.values()) {
                if(systemSettingService.findById(systemSettingsType)==null) {
                    SystemSettings systemSettings = new SystemSettings();
                    systemSettings.setId(systemSettingsType);
                    systemSettings.setValue(systemSettingsType.getDefauldValue());
                    systemSettingService.saveOrUpdate(systemSettings);
                }
            }
        }

        if(userService.countAll() == 0) {
            User user = new User();
            Set<UserProfile> userProfileSet = new HashSet<UserProfile>();
            user.setSsoId("admin");
            user.setPassword("admin");
            UserProfile userProfile = new UserProfile();
            userProfile.setType(UserProfileType.ADMIN.name());
            userProfileSet.add(userProfile);
            user.setUserProfiles(userProfileSet);
            user.setFirstName("Админ");
            user.setLastName("Админов");
            user.setEmail("admin@ru.ru");
            userService.saveOrUpdate(user);
        }
    }
}