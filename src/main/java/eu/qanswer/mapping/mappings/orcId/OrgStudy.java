package eu.qanswer.mapping.mappings.orcId;

import eu.qanswer.mapping.configuration.AbstractConfiguration;
import eu.qanswer.mapping.configuration.Mapping;
import eu.qanswer.mapping.configuration.Type;

import java.util.ArrayList;
import java.util.Arrays;

public class OrgStudy extends AbstractConfiguration {
    public OrgStudy()
    {
        format = "xml";
//        file = "C:\\Users\\My pc\\Desktop\\orcid.json";
//        file = "D:\\Orcid2018Json\\public_profiles_API-2.0_2017_10_json\\0\\0000-0001-5006-5580.json";
//        file = "C:\\Users\\My pc\\Desktop\\orcid2018.json";
        baseUrl = "http://orcid.org/";
//        file="D:\\summaries\\001\\0000-0001-5068-2001.xml";
//        file="C:\\Users\\My pc\\Desktop\\xmlFiles\\0000-0001-5013-9543.xml";
        file="C:\\Users\\My pc\\Desktop\\cleared.xml";
//        file = "C:\\Users\\My pc\\Desktop\\xmlfiles\\0000-0001-5058-5543.xml";
        key="profiles.record.activities-summary.educations.education-summary.organization.name";
        iterator="profiles.record.activities-summary.educations";
        mappings = new ArrayList<>(Arrays.asList(
                new Mapping("(.?)education-summary(.*)disambiguated-organization-identifier","http://www.wikidata.org/prop/direct/P2427", Type.LITERAL),
//                new Mapping("(.?)education-summary(.*)organization.name","http://www.wikidata.org/prop/direct/P1813", Type.LITERAL),
                new Mapping("(.?)education-summary(.*)organization.name","http://www.w3.org/2000/01/rdf-schema#label", Type.LITERAL)
                ));
    }
}
