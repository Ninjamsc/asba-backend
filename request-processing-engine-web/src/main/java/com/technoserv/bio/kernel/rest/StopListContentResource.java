package com.technoserv.bio.kernel.rest;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.annotation.JacksonFeatures;
import com.technoserv.db.model.objectmodel.Document;
import com.technoserv.db.model.objectmodel.StopList;
import com.technoserv.db.service.objectmodel.api.StopListService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by sergey on 23.11.2016.
 */
@Component
//@Api(value = "stop-list-content")
public class StopListContentResource {

    @Autowired
    private StopListService stopListService;

    @Path("/lists/rest/stoplist/{ID}/entry")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @JacksonFeatures(serializationEnable =  { SerializationFeature.INDENT_OUTPUT })
    public void add(@PathParam("ID")Long id, Document document) {
        StopList stopList = stopListService.findById(id);
        if(stopList!=null) {
            stopList.getOwner().add(document);
            stopListService.saveOrUpdate(stopList);
        }

    }

    @Path("/partners/rest/stoplist/{listId}/entry/{itemId}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @JacksonFeatures(serializationEnable =  { SerializationFeature.INDENT_OUTPUT })
    public void delete(@PathParam("listId")Long listId,@PathParam("itemId")Long itemId) {

        //todo write native sql

        StopList stopList = stopListService.findById(listId);
        if(stopList!=null) {
            Document delDocument = null;
            for (Document document : stopList.getOwner()) {
                if(document.getId().equals(itemId)) {
                    delDocument = document;
                }
            }
            if(delDocument!=null) {
                stopList.getOwner().remove(delDocument);
                stopListService.saveOrUpdate(stopList);
            }
        }

    }
}
