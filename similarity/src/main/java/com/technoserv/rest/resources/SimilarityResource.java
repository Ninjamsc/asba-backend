package com.technoserv.rest.resources;

import com.technoserv.rest.client.PhotoPersistServiceRestClient;
import com.technoserv.rest.client.TemplateBuilderServiceRestClient;
import com.technoserv.rest.model.CompareRequest;
import com.technoserv.rest.model.CompareResponse;
import com.technoserv.rest.request.PhotoTemplate;
import io.swagger.annotations.Api;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.math3.analysis.function.Exp;
import org.apache.commons.math3.analysis.function.Pow;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Component
@Path("")
@Api(value = "Similarity")
public class SimilarityResource  implements InitializingBean  {

    private static final Log log = LogFactory.getLog(SimilarityResource.class);


    @Autowired
    private TemplateBuilderServiceRestClient templateBuilderServiceRestClient;


    @Autowired
    private PhotoPersistServiceRestClient photoServiceClient;

    @Override
    public void afterPropertiesSet() throws Exception {

        log.debug("---------------------\nИницаиализация сервиса сравнения");
        System.out.println("Конец инициализации сервиса сравнения\n-------------------------");
    }
    /*
     * Сравнить 2 картинки
     */
    @GET
    @Path("/ping")
    @Produces(MediaType.TEXT_PLAIN)
    public String ping() {
        return new String ("Pong");
    }

    @PUT
    @Path("/compare")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response compare(CompareRequest message) {
       System.out.println("!!!!"+message.toString());
        CompareResponse resp = new CompareResponse();
        //vector
        // A
        PhotoTemplate tmplt1,tmplt2;
        ArrayRealVector v1,v2;
        try {
            String base64Image = message.getPictureA().split(",")[1]; // should skip header data:image/png;base64
            byte[] a = Base64.decode(base64Image.getBytes());
            tmplt1 = templateBuilderServiceRestClient.getPhotoTemplate(a);
            //vector B
            base64Image = message.getPictureB().split(",")[1];
            a = Base64.decode(base64Image.getBytes());
            tmplt2 = templateBuilderServiceRestClient.getPhotoTemplate(a);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(resp).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON+"; charset=UTF-8").build();

        }
        v1 = new ArrayRealVector(tmplt1.template);
        v2 = new ArrayRealVector(tmplt2.template);

        //ArrayRealVector diff = v1.subtract(v2);
        //double dot = diff.dotProduct(diff);
        System.out.println("=========VECTOR1 = "+new String(Base64.encode(tmplt1.binTemplate)));
        double norm = calculateSimilarity(new String(Base64.encode(tmplt1.binTemplate)),new String(Base64.encode(tmplt2.binTemplate)),"1");//1 / new Exp().value(new Pow().value(0.7*dot, 4));
        resp.setSimilarity(norm);
        resp.setPictureAURL("none");
        resp.setPictureBURL("none");
        System.out.println("SIMILARITY="+norm);
        return Response.status(Response.Status.OK).entity(resp).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON+"; charset=UTF-8").build();
    }

    public double calculateSimilarity(String base64Vector1, String base64Vector2, String version){
        double result = 0;
        try {
            Process p = Runtime.getRuntime().exec(String.format("/opt/biometrics/tevian-similarity %s %s",base64Vector1,base64Vector2));
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                result = Double.valueOf(line);
            }
        } catch (IOException e) {
            System.out.print("++++++++++++Wrong format++++++++++++++");
        }
        return result;
    };

}