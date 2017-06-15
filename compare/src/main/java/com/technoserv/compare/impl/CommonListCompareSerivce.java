package com.technoserv.compare.impl;

import com.technoserv.rest.model.CompareResponseBlackListObject;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by mlevitin on 17.01.17.
 */

@Component
public class CommonListCompareSerivce extends CompareServiceImpl {

    @Override
    public void initialize() {

    }

    @Override
    public List<CompareResponseBlackListObject> compare(double[] vector) {
        return null;
    }

    @Override
    public List<CompareResponseBlackListObject> compare(double[] vector, Long listId) {
        return null;
    }
}
