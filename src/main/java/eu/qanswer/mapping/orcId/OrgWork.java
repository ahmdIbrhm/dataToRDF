package eu.qanswer.mapping.orcId;

import eu.qanswer.mapping.AbstractClassMapping;
import eu.qanswer.mapping.Mapping;
import eu.qanswer.mapping.Type;

import java.util.ArrayList;
import java.util.Arrays;

public class OrgWork extends AbstractClassMapping {
    public OrgWork()
    {
        format = "xml";
//        file = "C:\\Users\\My pc\\Desktop\\orcid.json";
//        file = "D:\\Orcid2018Json\\public_profiles_API-2.0_2017_10_json\\0\\0000-0001-5006-5580.json";
        file = "C:\\Users\\My pc\\Desktop\\cleared.xml";
//        file = "C:\\Users\\My pc\\Desktop\\test\\0000-0001-5105-9000.xml";
//        file="D:\\summaries\\001\\0000-0001-5068-2001.xml";
//        file="C:\\Users\\My pc\\Desktop\\xmlFiles\\0000-0001-5013-9543.xml";
        baseUrl = "http://orcid.org/";
        key = "profiles.record.activities-summary.employments.employment-summary.organization.name";
        iterator="profiles.record.activities-summary.employments";
        mappings = new ArrayList<>(Arrays.asList(
                new Mapping("(.?)employment-summary(.*)organization.name","http://www.w3.org/2000/01/rdf-schema#label", Type.LITERAL),
                new Mapping("(.?)employment-summary(.*)disambiguated-organization-identifier","http://www.wikidata.org/prop/direct/P2427", Type.LITERAL)
                ));
    }
}
