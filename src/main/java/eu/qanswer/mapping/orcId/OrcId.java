package eu.qanswer.mapping.orcId;

import eu.qanswer.mapping.AbstractClassMapping;
import eu.qanswer.mapping.Mapping;
import eu.qanswer.mapping.Type;

import java.util.ArrayList;
import java.util.Arrays;

public class OrcId extends AbstractClassMapping {
    //the url used for all instances

    public OrcId(){
        format = "xml";
//        file = "C:\\Users\\My pc\\Desktop\\orcid.json";
//        file = "D:\\Orcid2018Json\\public_profiles_API-2.0_2017_10_json\\0\\0000-0001-5006-5580.json";
//        file = "C:\\Users\\My pc\\Desktop\\orcid2018.json";
//        file = "C:\\Users\\My pc\\Desktop\\ahmad.xml";
//        file = "C:\\Users\\My pc\\Desktop\\test\\0000-0001-5105-9000.xml";
//        file="D:\\summaries\\001\\0000-0001-5068-2001.xml";
//        file="C:\\Users\\My pc\\Desktop\\xmlFiles\\0000-0001-5013-9543.xml";
        file="C:\\Users\\My pc\\Desktop\\cleared.xml";
//        file = "C:\\Users\\My pc\\Desktop\\xmlfiles\\0000-0001-5058-5543.xml";
        baseUrl = "http://orcid.org/";
//        key = "profiles.record.orcid-identifier.path";
        key = "profiles.record.orcid-identifier.path";
        iterator = "profiles";
        mappings = new ArrayList<>(Arrays.asList(
                new Mapping("(.?)name.family-name","http://www.w3.org/2000/01/rdf-schema#label",Type.LITERAL),
                new Mapping("(.?)orcid-identifier.path","http://www.wikidata.org/prop/direct/P496", Type.URI),
                new Mapping("(.?).work-summary(.*).put-code","http://www.wikidata.org/prop/direct/P800" ,baseUrl,Type.URI),
//                new Mapping("(.?).education-summary(.*).put-code","http://www.wikidata.org/prop/direct/P2578" ,baseUrl,Type.URI),
                new Mapping("(.?)employment-summary(.*)organization.name","http://www.wikidata.org/prop/direct/P108" ,baseUrl,Type.URI),
                new Mapping("(.?)education-summary(.*)organization.name","http://www.wikidata.org/prop/direct/P69", Type.URI)
        ));
    }
}
//link keys
