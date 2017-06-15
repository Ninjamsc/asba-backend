package com.technoserv.compare.impl;

import com.technoserv.compare.api.CompareService;
import com.technoserv.db.model.objectmodel.Document;
import com.technoserv.db.model.objectmodel.StopList;
import com.technoserv.db.service.configuration.impl.SystemSettingsBean;
import com.technoserv.db.service.objectmodel.api.DocumentService;
import com.technoserv.db.service.objectmodel.api.StopListService;
import com.technoserv.rest.comparator.CompareServiceStopListElement;
import com.technoserv.rest.comparator.CompareServiceStopListVector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by mlevitin on 17.01.17.
 */
@Component
public abstract class CompareServiceImpl implements CompareService {

    private static final Logger log = LoggerFactory.getLogger(CompareServiceImpl.class);

    @Autowired
    private SystemSettingsBean systemSettingsBean;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private StopListService stopListService;

    // map of stoplists which this service controls
    private HashMap<Long, CompareServiceStopListElement> managedStopLists;

    private String listType;

    public String getListType() {
        return listType;
    }

    @Override
    public void setListType(String listType) {
        this.listType = listType;
    }

    @Override
    public void initialize() {
        List<StopList> allLists = stopListService.getAll("owner", "owner.bioTemplates");
    }

    public void delStopList(Long listId) {
        log.debug("delStopList: (removing) listId: {}", listId);
        if (managedStopLists.get(listId) == null) {
            log.warn("Stop list: {} is absent.", listId);
            return;
        }
        managedStopLists.remove(listId);
    }

    public boolean addList(StopList list) {
        if (list == null) return false;
        CompareServiceStopListElement e = new CompareServiceStopListElement(list.getStopListName(), list.getId(),
                list.getSimilarity());

        if (list.getOwner() != null) {
            for (Document d : list.getOwner()) {
                if (!e.addVector(d)) return false;
            }
        }
        managedStopLists.put(list.getId(), e);
        return true;
    }

    public void delStopListElement(Long listId, Long listElementId) {
        log.info("delStopListElement: (removing) elementId: {} (from) listId: {}", listElementId, listId);
        CompareServiceStopListElement list = this.managedStopLists.get(listId);
        if (list != null) {
            List<CompareServiceStopListVector> elements = list.getVectors();
            List<CompareServiceStopListVector> toDelete = new ArrayList<>();

            for (CompareServiceStopListVector el : elements) {
                if (el.getDocId().equals(listElementId)) {
                    log.debug("delStopListElement document: {} is deleted from list: {}", listElementId, listId);
                    toDelete.add(el);
                }
            }

            elements.removeAll(toDelete);
        }
    }

    public void addElement(Long listId, Document vector) throws Exception {
        log.info("addElement: (adding) vector: {} to listId: {}", vector.getId(), listId);
        if (vector.getId() == null) {
            log.error("addElement - null document id. Ignoring for the listId: {}", listId);
        }
        CompareServiceStopListElement sl = managedStopLists.get(listId);
        if (sl != null) sl.addVector(vector);
    }

    public void modify(StopList list) {

    }
}
