package com.technoserv.rest.resources;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.annotation.JacksonFeatures;
import com.technoserv.db.model.objectmodel.*;
import com.technoserv.db.service.Service;
import com.technoserv.db.service.configuration.impl.SystemSettingsBean;
import com.technoserv.db.service.objectmodel.api.*;
import com.technoserv.rest.client.PhotoPersistServiceRestClient;
import com.technoserv.rest.client.TemplateBuilderServiceRestClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.*;

//import javax.ws.rs.core.Response;


@Component
@Path("")
//@Api(value = "Compare")
public class SkudResource implements InitializingBean  {

    private static final Log log = LogFactory.getLog(SkudResource.class);

    @Autowired(required = true)
    @Qualifier("skudResultService")
    private SkudResultService skudResultService;



    @Override
    public void afterPropertiesSet() throws Exception {

        log.debug("---------------------\n");
        //listManager = new CompareListManager(documentService);


        //Todo тут логика при старте приложенния
        //Фактически данный бин singleton и создаётся при старте приложения
        //this.listManager.compare("[0.020753301680088043, 0.04044751450419426, 0.13084986805915833, 0.059689026325941086, 0.15199658274650574, -0.15477296710014343, 0.10174626111984253, 0.09708714485168457, 0.05313556268811226, -0.003060600021854043, -0.027737755328416824, -0.09192277491092682, 0.024010395631194115, -0.061058711260557175, 0.046029217541217804, -0.1775650829076767, 0.07849898934364319, -0.04662546142935753, 0.07057669758796692, 0.08652392029762268, 0.0662936121225357, 0.03756343200802803, -0.04922910034656525, 0.03141292557120323, 0.08356206119060516, -0.1350899189710617, -0.20401452481746674, 0.10807696729898453, 0.04847602918744087, 0.030618079006671906, 0.1429709941148758, -0.08152655512094498, -0.05040675401687622, -0.0020790710113942623, 0.055735066533088684, -0.13398271799087524, -0.10305635631084442, 0.07367397099733353, -0.08056174218654633, 0.011274375021457672, 0.07565123587846756, 0.0006628360715694726, -0.14597639441490173, 0.06119852513074875, 0.10004027187824249, 0.023146357387304306, 0.11026181280612946, -0.0040884907357394695, -0.037800535559654236, 0.10853380709886551, -0.10188018530607224, -0.007757482118904591, -0.010982991196215153, 0.02395358681678772, 0.09733343869447708, -0.07628369331359863, 0.02649574540555477, 0.039745479822158813, -0.17949996888637543, 0.007255507633090019, 0.016987621784210205, 0.18098284304141998, -0.1025320366024971, -0.018973298370838165, 0.10926719754934311, 0.046763718128204346, 0.06041368842124939, 0.06826133280992508, 0.012208324857056141, -0.08431797474622726, 0.0037080312613397837, 0.20286907255649567, 0.03940027579665184, 0.03899642452597618, 0.014941767789423466, 0.17148782312870026, -0.0380895659327507, -0.019260890781879425, -0.19221830368041992, -0.03016120381653309, 0.08850887417793274, -0.012414127588272095, -0.004460429307073355, 0.04568830505013466, -0.12407730519771576, -0.1516706794500351, 0.0789976567029953, 0.04784555733203888, 0.0027379500679671764, -0.014989141374826431, -0.056807562708854675, -0.06459169834852219, -0.014003312215209007, 0.07949569821357727, -0.015189931727945805, 0.03384886682033539, 0.17762857675552368, -0.06609157472848892, -0.2093888819217682, -0.03780129551887512, 0.007640279829502106, -0.03648004308342934, 0.04399833083152771, 0.0316741056740284, 0.04010686278343201, 0.08813131600618362, -0.06487372517585754, -0.039993055164813995, 0.23097702860832214, -0.017569255083799362, -0.15538936853408813, 0.06488358229398727, 0.02634734846651554, -0.04101783409714699, -0.036427076905965805, 0.05655577406287193, -0.002288134302943945, -0.046763014048337936, -0.1639958918094635, 0.19583673775196075, 0.14950740337371826, -0.00714136241003871, -0.055665522813797, 0.043675411492586136, -0.018850823864340782, -0.01104491576552391, -0.04339916259050369, -0.03134331479668617]");
        //String s = compareRules.get("scanAndWeb")
        //Class cc = Class.forName(s);
        //CompareRule r = (CompareRule)cc.newInstance();
        //Collection<Request> coll = requestService.findByIin(new Long(123456789012l));
        //log.debug("Coll size is"+coll.size()); ///
        System.out.println("\n-------------------------");
    }

    /**
     * Список всех стоп листов
     * @return Список всех стоп листов
     */
    @Path("/results")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @JacksonFeatures( serializationEnable =  { SerializationFeature.INDENT_OUTPUT })
    //@Override
    public Collection<SkudResult> list() {
        if(skudResultService == null)  {System.out.println("skudResultService is null"); return null;}
        return skudResultService.getAll();
    }


}