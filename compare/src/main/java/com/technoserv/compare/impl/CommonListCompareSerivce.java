package com.technoserv.compare.impl;

import com.technoserv.compare.api.CompareService;
import com.technoserv.db.service.configuration.impl.SystemSettingsBean;
import com.technoserv.db.service.objectmodel.api.DocumentService;
import com.technoserv.rest.comparator.CompareServiceStopListElement;
import com.technoserv.rest.model.CompareResponseBlackListObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mlevitin on 17.01.17.
 */

@Component
public class CommonListCompareSerivce extends CompareServiceImpl {




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
