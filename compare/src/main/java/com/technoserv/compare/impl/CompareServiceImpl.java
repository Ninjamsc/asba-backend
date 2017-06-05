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
import java.util.Iterator;
import java.util.List;

/**
 * Created by mlevitin on 17.01.17.
 */
@Component
public abstract class CompareServiceImpl implements CompareService {

    @Autowired
    private SystemSettingsBean systemSettingsBean;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private StopListService stopListService;

    private static final Log log = LogFactory.getLog(CompareServiceImpl.class);

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
        log.info("delStopList(): Removing list id=" + listId);
        if (managedStopLists.get(listId) == null) {
            log.debug("list id=" + listId + " is absent");
            return;
        }
        managedStopLists.remove(listId);
    }

    public boolean addList(StopList list) {

        if (list == null) return false;
        CompareServiceStopListElement e = new CompareServiceStopListElement(list.getStopListName(), list.getId(), list.getSimilarity());
        if (list.getOwner() != null) {
            Iterator<Document> id = list.getOwner().iterator();
            while (id.hasNext()) {
                Document d = id.next();
                if (!e.addVector(d)) return false;
            }
        }
        managedStopLists.put(list.getId(), e);
        return true;
    }

    public void delStopListElement(Long listId, Long listElementId) {
        log.info("delStopListElement(): removing element id=" + listElementId + " from list id=" + listId);
        CompareServiceStopListElement list = this.managedStopLists.get(listId);
        if (list != null) {
            List<CompareServiceStopListVector> elements = list.getVectors();
            List<CompareServiceStopListVector> toDelete = new ArrayList<>();
            for (Iterator<CompareServiceStopListVector> it = elements.iterator(); it.hasNext(); ) {
                CompareServiceStopListVector el = it.next();
                if (el.getDocId().longValue() == listElementId.longValue()) {
                    log.debug("delStopListElement(): Document id=" + listId + " is deleted from list id=" + listId);
                    toDelete.add(el);
                }

            }
            elements.removeAll(toDelete);
        }
    }

    public void addElement(Long listId, Document vector) throws Exception {
        log.info("addElement(): adding element id=" + vector.getId() + " to list id=" + listId);
        if (vector.getId() == null) {
            log.error("addElement(): null document id. ignoring for the list_id=" + listId);

        }
        CompareServiceStopListElement sl = managedStopLists.get(listId);
        if (sl != null) sl.addVector(vector);
    }

    //
    public void modify(StopList list) {

    }
}
