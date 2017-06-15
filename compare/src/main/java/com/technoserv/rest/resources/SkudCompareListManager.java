package com.technoserv.rest.resources;

import com.technoserv.db.model.configuration.SystemSettingsType;
import com.technoserv.db.model.objectmodel.Document;
import com.technoserv.db.model.objectmodel.StopList;
import com.technoserv.db.service.configuration.impl.SystemSettingsBean;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.security.crypto.codec.Base64.encode;

@Component
public class SkudCompareListManager implements InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(SkudCompareListManager.class);

    @Autowired
    private SystemSettingsBean systemSettingsBean;

    @Autowired
    private DocumentService documentService;

    private HashMap<Long, CompareServiceStopListElement> managedStopLists;

    public boolean addList(StopList list) {
        log.debug("addList list: {}", list);

        if (list == null) return false;

        CompareServiceStopListElement e = new CompareServiceStopListElement(list.getStopListName(), list.getId(), list.getSimilarity());
        if (list.getOwner() != null) {
            for (Document d : list.getOwner()) {
                if (!e.addVector(d)) return false;
            }
        }
        managedStopLists.put(list.getId(), e);
        return true;
    }

    /**
     * Добавление элемента в существующий список по его ID
     */
    public void addElement(Long listId, Document vector) throws Exception {
        log.debug("addElement (to list) element: {} to list: {}", vector, listId);
        if (vector.getId() == null) {
            log.error("addElement - null document Id. Ignoring for the listId: {}", listId);
        }
        CompareServiceStopListElement sl = managedStopLists.get(listId);
        if (sl != null) sl.addVector(vector);
    }

    // compare with exact list
    public CompareResponseBlackListObject compare1(double[] vector, Long listId) throws Exception {
        log.info("compare1 (double, Long) list size: {}", managedStopLists.size());

        double mult = new Double(systemSettingsBean.get(SystemSettingsType.COMPARATOR_MULTIPLIER));
        int power = new Integer(systemSettingsBean.get(SystemSettingsType.COMPARATOR_POWER));

        ArrayRealVector arv = new ArrayRealVector(vector);
        CompareServiceStopListElement list = managedStopLists.get(listId);
        CompareResponseBlackListObject report = new CompareResponseBlackListObject();

        if (list == null) {
            log.error("List: {} is null or unavailable.", listId);
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
            double norm = wrapSimilarityCalculation(arv, vect.getVector());
            if (norm > list.getSimilarity()) {
                // HIT
                log.debug("compare1 HIT list: {} norm: {} similarity: {} doc: {}",
                        list.getListName(), norm, list.getSimilarity(), vect.getDocId());

                Long doc = vect.getDocId();
                Document d = this.documentService.findById(doc);
                CompareResponsePhotoObject po = new CompareResponsePhotoObject();
                po.setUrl(d.getFaceSquare());
                po.setSimilarity(norm);
                po.setIdentity(d.getId());
                report.addPhoto(po);
            } else {
                log.debug("compare1 MISS list: {} norm: {} similarity: {} doc: {}",
                        list.getListName(), norm, list.getSimilarity(), vect.getDocId());
            }
        }
        if (!report.getPhoto().isEmpty()) return report;

        return null;
    }

    // compare with all list except specified
    public List<CompareResponseBlackListObject> compare2(double[] vector, Long listId) throws Exception {
        log.debug("compare2 (double) list size: {}", managedStopLists.size());

        double mult = new Double(systemSettingsBean.get(SystemSettingsType.COMPARATOR_MULTIPLIER));
        int power = new Integer(systemSettingsBean.get(SystemSettingsType.COMPARATOR_POWER));
        List<CompareResponseBlackListObject> bl = new ArrayList<>();

        // Сериализуем строковое представление вектора в ArrayRealVector
        ArrayRealVector arv = new ArrayRealVector(vector);

        for (Map.Entry<Long, CompareServiceStopListElement> longCompareServiceStopListElementEntry : managedStopLists.entrySet()) {

            CompareServiceStopListElement list = longCompareServiceStopListElementEntry.getValue();
            if (list.getId().equals(listId)) {
                log.debug("skipping common list: {}", listId);
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
                double norm = wrapSimilarityCalculation(arv, vect.getVector());

                if (norm > similarity) {
                    // HIT
                    log.debug("compare2 HIT list: {} norm: {} similarity: {} list: {} docId: {}",
                            list.getListName(), norm, similarity, list.getId(), vect.getDocId());

                    Long doc = vect.getDocId();
                    Document document = documentService.findById(doc);

                    CompareResponsePhotoObject po = new CompareResponsePhotoObject();
                    po.setUrl(document.getFaceSquare());
                    po.setSimilarity(norm);
                    report.addPhoto(po);
                    bl.add(report);

                } else {
                    log.debug("compare2 MISS list: {} norm: {} similarity: {} listId: {} docId: {}",
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
        result.setSimilar(norm < similarity);
        result.setThreshold(similarity);
        result.setSimilarity(norm);
        return result;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.managedStopLists = new HashMap<>();
    }

    private double wrapSimilarityCalculation(ArrayRealVector v1, ArrayRealVector v2) {
        double mult = new Double(systemSettingsBean.get(SystemSettingsType.COMPARATOR_MULTIPLIER)), norm;
        int power = new Integer(systemSettingsBean.get(SystemSettingsType.COMPARATOR_POWER));
        if (v1.getDimension() == v2.getDimension()) {
            /*ArrayRealVector diff = v1.subtract(v2);
            double dot = diff.dotProduct(diff);
			norm = 1 / new Exp().value(new Pow().value(mult * dot, power));*/
            //norm = TevianVectorComparator.calculateSimilarityWrapper(v1.getDataRef(),v2.getDataRef());
            norm = TevianVectorComparator.calculateSimilarityCliWrapper(
                    new String(encode(TevianVectorComparator.getByteArrayFromVector(v1.getDataRef()))),
                    new String(encode(TevianVectorComparator.getByteArrayFromVector(v2.getDataRef()))), "1");
        } else {
            norm = 0;
        }
        return norm;
    }

}
