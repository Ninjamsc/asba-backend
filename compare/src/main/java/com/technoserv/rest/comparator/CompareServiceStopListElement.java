package com.technoserv.rest.comparator;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.technoserv.db.model.objectmodel.Document;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class CompareServiceStopListElement {

    private static final Logger log = LoggerFactory.getLogger(CompareServiceStopListElement.class);

    private String listName;
    private Long id;
    private Double similarity;

    @JsonInclude(Include.NON_EMPTY)
    List<CompareServiceStopListVector> vectors;

    public CompareServiceStopListElement(String listName, Long id, Double similarity) {
        this.vectors = new ArrayList<>();
        this.setId(id);
        this.setListName(listName);
        this.setSimilarity(similarity);
    }

    public boolean addVector(Document doc) {
        if (vectors == null) this.vectors = new ArrayList<>();
        CompareServiceStopListVector v = new CompareServiceStopListVector();
        ObjectMapper mapper = new ObjectMapper();
        try {
            double[] array = mapper.readValue(doc.getBioTemplates().get(0).getTemplateVector(), double[].class);
            v.setVector(new ArrayRealVector(array));
            v.setDocId(doc.getId());
            vectors.add(v);
        } catch (Exception e) {
            log.error(String.format("Can't add vector doc: %s", doc), e);
            return false;
        }
        return true;
    }

    public void setVectors(ArrayList<CompareServiceStopListVector> vector) {
        this.vectors = vector;
    }

    public List<CompareServiceStopListVector> getVectors() {
        return vectors;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(Double similarity) {
        this.similarity = similarity;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

}

