package informa;

import eu.qanswer.mapping.AbstractClassMapping;
import eu.qanswer.mapping.Mapping;
import eu.qanswer.mapping.Type;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.jena.datatypes.xsd.XSDDatatype.XSDdateTime;
import static org.apache.jena.datatypes.xsd.XSDDatatype.XSDinteger;
import static org.apache.jena.datatypes.xsd.XSDDatatype.XSDstring;

public class Trial extends AbstractClassMapping {
    //the url used for all instances

    public Trial(){
        format = "json";
        file = "/Users/Dennis/PycharmProjects/TrialTrove/crawl_trials";
        baseUrl = "https://citeline.informa.com/trials/details/";
        key = "trialId";
        mappings = new ArrayList<Mapping>(Arrays.asList(
                new Mapping("trialId", "http://www.w3.org/2000/01/rdf-schema#type", "http://www.wikidata.org/entity/Q30612"),
                new Mapping("trialId", "http://purl.org/dc/terms/identifier", Type.LITERAL),
                new Mapping("trialTitle", "http://www.w3.org/2000/01/rdf-schema#label", Type.LITERAL),
                new Mapping("trialStatus","http://www.newclean.eu/status", Type.URI_WITH_LABEL),
                new Mapping("(.?).meshId","http://www.newclean.eu/meshId", "https://meshb.nlm.nih.gov/#/record/ui?ui=", Type.URI),
                new Mapping("(.?).meshId","http://www.wikidata.org/prop/direct/P486", Type.LITERAL),
                new Mapping("trialCountries(.?)","http://www.wikidata.org/prop/direct/P17", Type.URI),
                new Mapping("trialRegions(.?)","http://www.newclean.eu/region", Type.CUSTOM),
                new Mapping("(.?).trialDiseases\\[[0-9]\\].name","http://www.newclean.eu/disease", Type.LITERAL),
                new Mapping("(.?).icd9Id","http://www.wikidata.org/prop/direct/P493", Type.URI),
                new Mapping("trialObjective","http://www.w3.org/2000/01/rdf-schema#description", Type.LITERAL),
                new Mapping("(.?).actualTrialStartDate","http://www.wikidata.org/prop/direct/P580", Type.DATE),
                new Mapping("(.?).minAge($)","http://www.newclean.eu/age", Type.CUSTOM),
                new Mapping("(.?).maxAge($)","http://www.newclean.eu/age", Type.CUSTOM),
                new Mapping("(.?).gender","http://www.newclean.eu/gender","http://www.newclean.eu/gender/", Type.CUSTOM),
                new Mapping("trialPhase","http://www.wikidata.org/prop/direct/P6099","http://www.newclean.eu/trialPhase/", Type.CUSTOM),
                new Mapping("(.?).Sponsors\\[[0-9]\\].name","http://www.wikidata.org/prop/direct/P859","http://www.newclean.eu/sponsor/", Type.URI_WITH_LABEL),
                new Mapping("(.?).Sponsors\\[[0-9]\\].type","http://www.newclean.eu/sponsorType","http://www.newclean.eu/sponsor/", Type.URI_WITH_LABEL),
                new Mapping("trialExclusionCriteria","http://www.wikidata.org/prop/direct/P3712", Type.LITERAL,XSDstring),
                new Mapping("trialInclusionCriteria","http://www.wikidata.org/prop/direct/P3712", Type.LITERAL,XSDstring),
                new Mapping("trialStartDate","http://www.wikidata.org/prop/direct/P580", Type.LITERAL,XSDdateTime),
                new Mapping("trialPrimaryCompletionDate","http://www.wikidata.org/prop/direct/P582", Type.LITERAL,XSDdateTime),
                new Mapping("trialObjective","http://purl.org/dc/terms/description", Type.LITERAL,XSDstring),
                new Mapping("trialPrimaryEndPoint","http://www.wikidata.org/prop/direct/P3712", Type.LITERAL,XSDstring),
                new Mapping("trialOtherEndPoint","http://www.wikidata.org/prop/direct/P3712", Type.LITERAL,XSDstring),
                new Mapping("trialStudyDesign","http://www.newclean.eu/trialStudyDesign", Type.LITERAL,XSDstring),
                new Mapping("trialStudyKeywords(.?)","http://www.wikidata.org/prop/direct/P921", Type.LITERAL,XSDstring),
                new Mapping("trialPatientPopulation","http://www.newclean.eu/patientPopulation", Type.LITERAL,XSDstring),
                new Mapping("trialTreatmentPlan","http://www.newclean.eu/patientPopulation", Type.LITERAL,XSDstring),
                new Mapping("trialTargetAccrual","http://www.newclean.eu/tragetNumberPatients", Type.LITERAL, XSDinteger),
                new Mapping("trialActualAccrual","http://www.newclean.eu/currentNumberPatients", Type.LITERAL, XSDinteger),
                new Mapping("trialInvestigators(.?)","http://www.wikidata.org/prop/direct/P1840", "https://citeline.informa.com/investigators/details/", Type.URI),
                //new Mapping("(.*)trialSupportingUrls","http://www.wikidata.org/prop/direct/P1840", "" ,Type.URI),
                new Mapping("trialSource","http://www.newclean.eu/source", Type.LITERAL)
    ));
    }
}
