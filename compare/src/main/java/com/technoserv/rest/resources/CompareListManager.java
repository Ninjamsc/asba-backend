package com.technoserv.rest.resources;

import com.technoserv.db.model.configuration.SystemSettingsType;
import com.technoserv.db.model.objectmodel.BioTemplate;
import com.technoserv.db.model.objectmodel.Document;
import com.technoserv.db.model.objectmodel.StopList;
import com.technoserv.db.service.configuration.impl.SystemSettingsBean;
import com.technoserv.db.service.objectmodel.api.BioTemplateService;
import com.technoserv.db.service.objectmodel.api.DocumentService;
import com.technoserv.rest.comparator.CompareServiceStopListElement;
import com.technoserv.rest.comparator.CompareServiceStopListVector;
import com.technoserv.rest.model.CompareResponseBlackListObject;
import com.technoserv.rest.model.CompareResponsePhotoObject;
import com.technoserv.rest.model.SelfCompareResult;
import com.technoserv.utils.TevianVectorComparator;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

import static org.springframework.security.crypto.codec.Base64.encode;

@Component
public class CompareListManager implements InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(CompareListManager.class);

    @Autowired
    private SystemSettingsBean systemSettingsBean;

    @Autowired
    private DocumentService documentService;


    @Autowired
    private BioTemplateService bioTemplateService;

    private HashMap<Long, CompareServiceStopListElement> managedStopLists;

    public HashMap<Long, CompareServiceStopListElement> getManagedStopLists(){
        return this.managedStopLists;
    }

    public CompareServiceStopListElement getList(Long id) {
        return this.managedStopLists.get(id);
    }

    /**
     * Добавление элемента в существующий список по его ID
     */
    public void addElement(Long listId, Document vector) throws Exception {
        log.info("addElement: adding element id: {} to listId: {}", vector.getId(), listId);
        if (vector.getId() == null) {
            log.error("addElement: null document id. Ignoring for the listId: {}", listId);
        }
        CompareServiceStopListElement sl = managedStopLists.get(listId);
        if (sl != null) {
            sl.addVector(vector);
        }
    }

    /**
     * удаление элемента списка ID по его ID
     */
    public void delStopListElement(Long listId, Long listElementId) {
        log.info("delStopListElement: (removing) element: {} from list: {}", listElementId, listId);
        CompareServiceStopListElement list = this.managedStopLists.get(listId);
        if (list != null) {
            List<CompareServiceStopListVector> elements = list.getVectors();
            List<CompareServiceStopListVector> toDelete = new ArrayList<>();
            for (CompareServiceStopListVector el : elements) {
                if (el.getDocId().equals(listElementId)) {
                    log.debug("delStopListElement element: {} is deleted from list: {}", listElementId, listId);
                    toDelete.add(el);
                }
            }
            elements.removeAll(toDelete);
        }
    }

    /**
     * удаление списка по его ID
     */
    public void delStopList(Long listId) {
        log.info("delStopList(): Removing list id=" + listId);
        if (managedStopLists.get(listId) == null) {
            log.debug("list id=" + listId + " is absent");
            return;
        }
        managedStopLists.remove(listId);
    }

    /**
     * Добавление нового стоплиста
     */
    public boolean addList(StopList list) {
        log.info("addStopList: (removing) list: {}", list.getId());

        if (list == null) return false;

        CompareServiceStopListElement e = new CompareServiceStopListElement(list.getStopListName(), list.getId(),
                list.getSimilarity());



        if (list.getOwner() != null) {
            for (Document d : list.getOwner()) {
                if (d.getBioTemplates().isEmpty()){
                    List<BioTemplate> bts = bioTemplateService.getAllByDocument(d);
                    log.info("bioTemplatesIsEmpty! find {} templates",bts.size());
                    d.setBioTemplates(bts);
                }
                if (!e.addVector(d)) return false;
            }
        }
        managedStopLists.put(list.getId(), e);
        return true;
    }

    /**
     * Compare with exact list
     */
    public CompareResponseBlackListObject compare1(double[] vector, Long listId) throws Exception {
        //TODO: specify exception
        log.info("compare(double, Long) list size: {}", managedStopLists.size());
        double mult = new Double(systemSettingsBean.get(SystemSettingsType.COMPARATOR_MULTIPLIER));
        int power = new Integer(systemSettingsBean.get(SystemSettingsType.COMPARATOR_POWER));

        ArrayRealVector arv = new ArrayRealVector(vector);
        CompareServiceStopListElement list = managedStopLists.get(listId);
        CompareResponseBlackListObject report = new CompareResponseBlackListObject();
        if (list == null) {
            log.error("ListId: {} is null or unavailable.", listId);
            return null;
        }
        report.setListId(list.getId());
        report.setListName(list.getListName());
        report.setSimilarity(list.getSimilarity());

        List<CompareServiceStopListVector> vectors = list.getVectors();
        for (CompareServiceStopListVector vect : vectors) {
            /*ArrayRealVector diff = arv.subtract(vect.getVector());
            double dot = diff.dotProduct(diff);
			double norm = 1 / new Exp().value(new Pow().value(mult*dot, power));*/
            // double norm = TevianVectorComparator.calculateSimilarityWrapper(arv.getDataRef(),vect.getVector().getDataRef());

            double norm = TevianVectorComparator.calculateSimilarityCliWrapper(
                    new String(encode(TevianVectorComparator.getByteArrayFromVector(arv.getDataRef()))),
                    new String(encode(TevianVectorComparator.getByteArrayFromVector(vect.getVector().getDataRef()))), "1");

            if (norm > list.getSimilarity()) {
                log.debug("compare1 HIT: {} norm: {} similarity: {} doc: {}",
                        list.getListName(), norm, list.getSimilarity(), vect.getDocId());

                Long doc = vect.getDocId();
                Document d = this.documentService.findById(doc);
                CompareResponsePhotoObject po = new CompareResponsePhotoObject();
                po.setUrl(d.getFaceSquare());
                po.setSimilarity(norm);
                po.setIdentity(d.getId());
                report.addPhoto(po);
            } else
                log.debug("compare1 MISS: {} norm: {} similarity: {} doc: {}",
                        list.getListName(), norm, list.getSimilarity(), vect.getDocId());
        }
        if (report.getPhoto().size() > 0) {
            return report;
        }
        return null;
    }

    /**
     * compare with all list except specified
     */
    public List<CompareResponseBlackListObject> compare2(double[] vector, Long listId) throws Exception {
        //TODO: specify exception
        log.info("compare (double) list size: {}", managedStopLists.size());
        double mult = new Double(systemSettingsBean.get(SystemSettingsType.COMPARATOR_MULTIPLIER));
        int power = new Integer(systemSettingsBean.get(SystemSettingsType.COMPARATOR_POWER));
        List<CompareResponseBlackListObject> bl = new ArrayList<>();
        // Сериализуем строковое представление вектора в ArrayRealVector
        ArrayRealVector arv = new ArrayRealVector(vector);

        for (Map.Entry<Long, CompareServiceStopListElement> longCompareServiceStopListElementEntry : managedStopLists.entrySet()) {

            CompareServiceStopListElement list = longCompareServiceStopListElementEntry.getValue();
            if (list.getId().equals(listId)) {
                log.debug("skipping common list id: {}", listId);
                continue;
            }

            Double similarity = list.getSimilarity();
            CompareResponseBlackListObject report = new CompareResponseBlackListObject();
            report.setListId(list.getId());
            report.setListName(list.getListName());
            report.setSimilarity(list.getSimilarity());

            List<CompareServiceStopListVector> vectors = list.getVectors();
            for (CompareServiceStopListVector vect : vectors) {
                /*ArrayRealVector diff =arv.subtract(vect.getVector());
                double dot = diff.dotProduct(diff);
				double norm = 1 / new Exp().value(new Pow().value(mult*dot, power));*/
                // double norm = TevianVectorComparator.calculateSimilarityWrapper(arv.getDataRef(),vect.getVector().getDataRef());
                double norm = TevianVectorComparator.calculateSimilarityCliWrapper(
                        new String(encode(TevianVectorComparator.getByteArrayFromVector(arv.getDataRef()))),
                        new String(encode(TevianVectorComparator.getByteArrayFromVector(vect.getVector().getDataRef()))), "1");

                if (norm > similarity) {
                    // HIT
                    log.debug("compare2 HIT list: {} norm: {} similarity: {} listId: {} docId: {}",
                            list.getListName(), norm, similarity, list.getId(), vect.getDocId());

                    Long doc = vect.getDocId();

                    Document d = this.documentService.findById(doc);
                    CompareResponsePhotoObject po = new CompareResponsePhotoObject();
                    po.setUrl(d.getFaceSquare());
                    po.setSimilarity(norm);
                    report.addPhoto(po);
                    if (!bl.contains(report)) bl.add(report);
                } else {
                    log.debug("compare2 MISS list: {} norm: {} similarity: {} list id: {} docId: {}",
                            list.getListName(), norm, similarity, list.getId(), vect.getDocId());
                }
            }
        }
        return bl;
    }

    public SelfCompareResult isSimilar(double[] a1, double[] a2) {
        double similarity = new Double(systemSettingsBean.get(SystemSettingsType.RULE_SELF_SIMILARITY));
        double mult = new Double(systemSettingsBean.get(SystemSettingsType.COMPARATOR_MULTIPLIER));
        int power = new Integer(systemSettingsBean.get(SystemSettingsType.COMPARATOR_POWER));
        /*ArrayRealVector v1 = new ArrayRealVector(a1);
        ArrayRealVector v2 = new ArrayRealVector(a2);
		ArrayRealVector diff =v1.subtract(v2);
		double dot = diff.dotProduct(diff);
		double norm = 1 / new Exp().value(new Pow().value(mult*dot, power));*/
        //double norm = TevianVectorComparator.calculateSimilarityWrapper(a1,a2);
        double norm = TevianVectorComparator.calculateSimilarityCliWrapper(
                new String(encode(TevianVectorComparator.getByteArrayFromVector(a1))),
                new String(encode(TevianVectorComparator.getByteArrayFromVector(a2))), "1");


        log.debug("SIMILARITY: {} THRESHOLD: {}", norm, similarity);

        SelfCompareResult result = new SelfCompareResult();
        if (norm < similarity) {
            result.setSimilar(false);
        } else {
            result.setSimilar(true);
        }
        result.setThreshold(similarity);
        result.setSimilarity(norm);
        return result;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        managedStopLists = new HashMap<>();
    }
}
