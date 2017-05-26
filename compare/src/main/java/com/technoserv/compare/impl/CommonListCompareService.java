package com.technoserv.compare.impl;

import com.technoserv.db.service.configuration.impl.SystemSettingsBean;
import com.technoserv.db.service.objectmodel.api.DocumentService;
import com.technoserv.db.service.objectmodel.api.StopListService;
import com.technoserv.rest.model.CompareResponseBlackListObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * Created by mlevitin on 17.01.17.
 */

@Component
public class CommonListCompareService extends CompareServiceImpl {

    @Autowired
    public CommonListCompareService(SystemSettingsBean systemSettingsBean, DocumentService documentService, StopListService stopListService) {
        super(systemSettingsBean, documentService, stopListService);
    }

    @Override
    public void initialize() {

    }

    @Override
    public ArrayList<CompareResponseBlackListObject> compare(double[] vector) {
        return null;
    }

    @Override
    public ArrayList<CompareResponseBlackListObject> compare(double[] vector, Long listId) {
        return null;
    }
}
