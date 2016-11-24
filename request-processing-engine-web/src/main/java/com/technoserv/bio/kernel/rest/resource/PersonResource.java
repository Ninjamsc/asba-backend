package com.technoserv.bio.kernel.rest.resource;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.annotation.JacksonFeatures;
import com.technoserv.db.model.objectmodel.Person;
import com.technoserv.db.model.objectmodel.StopList;
import com.technoserv.db.service.objectmodel.api.PersonService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by sergey on 23.11.2016.
 */
@Component
@Path("/db/rest/person")
@Api(value = "Person")
public class PersonResource {

    @Autowired
    private PersonService personService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @JacksonFeatures( serializationEnable =  { SerializationFeature.INDENT_OUTPUT })
    @Path("/{inn}")
    public Person history(@PathParam("iin")Long id) {
        return personService.history(id);
    }

}
