package com.technoserv.compare.api;

import com.technoserv.db.model.objectmodel.Document;
import com.technoserv.db.model.objectmodel.StopList;
import com.technoserv.rest.model.CompareResponseBlackListObject;
import com.technoserv.rest.model.SelfCompareResult;

import java.util.ArrayList;

/**
 * Created by mlevitin on 17.01.17.
 */
public interface CompareService {

    public void initialize();
    public ArrayList<CompareResponseBlackListObject> compare(double[] vector);
    public ArrayList<CompareResponseBlackListObject> compare(double[] vector,Long listId);
    public void delStopListElement(Long listId, Long listElementId);
    public void addElement (Long listId, Document vector) throws Exception;
    public void delStopList(Long listId);
    public boolean addList(StopList list);
    public void modify(StopList list);
    public void setListType(String listType);
}
