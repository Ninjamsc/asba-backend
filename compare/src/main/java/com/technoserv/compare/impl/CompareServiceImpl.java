package com.technoserv.compare.impl;

import com.technoserv.compare.api.CompareService;
import com.technoserv.db.model.objectmodel.Document;
import com.technoserv.db.model.objectmodel.StopList;
import com.technoserv.db.service.configuration.impl.SystemSettingsBean;
import com.technoserv.db.service.objectmodel.api.DocumentService;
import com.technoserv.db.service.objectmodel.api.StopListService;
import com.technoserv.rest.comparator.CompareServiceStopListElement;
import com.technoserv.rest.comparator.CompareServiceStopListVector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

    private static final Log log = LogFactory.getLog(CompareServiceImpl.class);

    private SystemSettingsBean systemSettingsBean;

    private DocumentService documentService;

    private StopListService stopListService;


    @Autowired
    public CompareServiceImpl(SystemSettingsBean systemSettingsBean,
                              DocumentService documentService,
                              StopListService stopListService) {

        this.systemSettingsBean = systemSettingsBean;
        this.documentService = documentService;
        this.stopListService = stopListService;
    }

    // map of stopLists which this service controls
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
        log.debug(String.format("initialize number of lists: %d", allLists.size()));
    }

    public void delStopList(Long listId) {
        log.info(String.format("delStopList listId: %d", listId));
        if (managedStopLists.get(listId) == null) {
            log.warn("list id=" + listId + " is absent");
            return;
        }
        managedStopLists.remove(listId);
    }

    public boolean addList(StopList list) {
        log.debug(String.format("addList list: %s", list));

        if (list == null) return false;
        CompareServiceStopListElement stopListElement = new CompareServiceStopListElement(
                list.getStopListName(),
                list.getId(),
                list.getSimilarity()
        );

        if (list.getOwner() != null) {
            for (Document d : list.getOwner()) {
                if (!stopListElement.addVector(d)) {
                    return false;
                }
            }
        }
        managedStopLists.put(list.getId(), stopListElement);
        return true;
    }

    public void delStopListElement(Long listId, Long listElementId) {
        log.info(String.format("delStopListElement removing element: %d from list: %d", listElementId, listId));
        CompareServiceStopListElement list = this.managedStopLists.get(listId);

        if (list != null) {
            List<CompareServiceStopListVector> elements = list.getVectors();
            List<CompareServiceStopListVector> toDelete = new ArrayList<>();

            for (CompareServiceStopListVector el : elements) {
                if (el.getDocId().longValue() == listElementId.longValue()) {
                    log.debug(String.format("delStopListElement(): Document: %d is deleted from list: %d", listElementId, listId));
                    toDelete.add(el);
                }
            }
            elements.removeAll(toDelete);
        }
    }

    public void addElement(Long listId, Document vector) throws Exception {
        log.info(String.format("addElement vector: %d to list: %d", vector.getId(), listId));

        if (vector.getId() == null) {
            log.error("Vector Id is null.");
        }

        CompareServiceStopListElement sl = managedStopLists.get(listId);
        if (sl != null) {
            sl.addVector(vector);
        }
    }

    public void modify(StopList list) {

    }
}
