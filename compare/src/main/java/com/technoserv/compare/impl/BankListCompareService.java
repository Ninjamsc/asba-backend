package com.technoserv.compare.impl;

import com.technoserv.compare.api.CompareService;
import com.technoserv.db.model.objectmodel.Document;
import com.technoserv.db.model.objectmodel.StopList;
import com.technoserv.rest.model.CompareResponseBlackListObject;

import java.util.ArrayList;

/**
 * Created by mlevitin on 17.01.17.
 */
public class BankListCompareService implements CompareService {

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

    @Override
    public void delStopListElement(Long listId, Long listElementId) {

    }

    @Override
    public void addElement(Long listId, Document vector) throws Exception {

    }

    @Override
    public void delStopList(Long listId) {

    }

    @Override
    public boolean addList(StopList list) {
        return false;
    }

    @Override
    public void modify(StopList list) {

    }

    @Override
    public void setListType(String listType) {

    }
}
