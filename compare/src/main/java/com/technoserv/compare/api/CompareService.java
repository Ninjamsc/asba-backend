package com.technoserv.compare.api;

import com.technoserv.db.model.objectmodel.Document;
import com.technoserv.db.model.objectmodel.StopList;
import com.technoserv.rest.model.CompareResponseBlackListObject;

import java.util.ArrayList;

/**
 * Created by mlevitin on 17.01.17.
 */
public interface CompareService {

    void initialize();

    ArrayList<CompareResponseBlackListObject> compare(double[] vector);

    ArrayList<CompareResponseBlackListObject> compare(double[] vector, Long listId);

    void delStopListElement(Long listId, Long listElementId);

    void addElement(Long listId, Document vector) throws Exception;

    void delStopList(Long listId);

    boolean addList(StopList list);

    void modify(StopList list);

    void setListType(String listType);
}
