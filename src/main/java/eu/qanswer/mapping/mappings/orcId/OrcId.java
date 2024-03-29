package eu.qanswer.mapping.mappings.orcId;

import eu.qanswer.mapping.Main;
import eu.qanswer.mapping.configuration.AbstractConfigurationFile;
import eu.qanswer.mapping.configuration.Mapping;
import eu.qanswer.mapping.configuration.Type;
import eu.qanswer.mapping.utility.Utility;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class OrcId extends AbstractConfigurationFile {
    OrcId orcId=this;
    public OrcId(){
        format = "xml";
        file="orcid.xml";
        baseUrl = "http://orcid.org/";
        key = "profiles.record.orcid-identifier.path";
        iterator = "profiles";
        mappings = new ArrayList<>(Arrays.asList(
                new Mapping("orcid-identifier.uri","http://www.wikidata.org/prop/direct/P31","http://www.wikidata.org/entity/Q1650915"),
                new Mapping("(.?)person.name.given-names","http://www.w3.org/2000/01/rdf-schema#label",new ClassForFnLn(),Type.CUSTOM),
                new Mapping("(.?)orcid-identifier.path","http://www.wikidata.org/prop/direct/P496", Type.URI),
                new Mapping("(.?).work-summary(.*).put-code","http://www.wikidata.org/prop/direct/P50" ,baseUrl,Type.URI),
//                new Mapping("(.?).education-summary(.*).put-code","http://www.wikidata.org/prop/direct/P2578" ,baseUrl,Type.URI),
                new Mapping("(.?)employment-summary(.*)organization.name","http://www.wikidata.org/prop/direct/P108" ,baseUrl,Type.URI),
                new Mapping("(.?)education-summary(.*)organization.name","http://www.wikidata.org/prop/direct/P69", Type.URI)
        ));
    }
    private class ClassForFnLn extends CustomMapping{

        @Override
        public ArrayList<Triple> function(HashMap<String, String> article, String key) {
            Node object;
            Utility utility = new Utility();
            ArrayList<Triple> triples=new ArrayList<>();
            Node predicate = utility.createURI(getMapping().getPropertyUri());
            Node subject = Main.getSubject(article,orcId,key);
            if(getMapping().getTag().contains("person.name.given-names"))
            {
                String fn=article.get(key);
                String ln=article.get(key.replace("given-names","family-name"));
                object = NodeFactory.createLiteral(fn+" "+ln);
                triples.add(new Triple(subject,predicate,object));
            }
            return triples;
        }
    }
}
