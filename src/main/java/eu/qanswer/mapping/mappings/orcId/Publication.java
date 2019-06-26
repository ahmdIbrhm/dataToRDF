package eu.qanswer.mapping.mappings.orcId;

import eu.qanswer.mapping.*;
import eu.qanswer.mapping.configuration.AbstractConfiguration;
import eu.qanswer.mapping.configuration.Mapping;
import eu.qanswer.mapping.configuration.Type;
import eu.qanswer.mapping.utility.Utility;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static org.apache.jena.datatypes.xsd.XSDDatatype.*;

public class Publication extends AbstractConfiguration {
    Publication publication=this;
    public Publication(){
        format = "xml";
        baseUrl = "http://orcid.org/";
        file="orcid.xml";
        key="profiles.record.activities-summary.works.group.work-summary.put-code";
        iterator="profiles.record.activities-summary.works";
        mappings = new ArrayList<>(Arrays.asList(
                new Mapping("activities-summary.(.*).work-summary.put-code","http://www.wikidata.org/prop/direct/P31","http://www.wikidata.org/entity/Q13442814"),
                new Mapping("(.?)work-summary(.*).external-id-value","http://www.wikidata.org/prop/direct/P356",new ClassForDoi(),Type.CUSTOM),
                new Mapping("(.?).title.title","http://www.w3.org/2000/01/rdf-schema#label", Type.LITERAL),
                new Mapping("(.?)work-summary.type","http://www.wikidata.org/prop/direct/P735", Type.LITERAL),
                new Mapping("(.?)work-summary(.*)title","http://www.wikidata.org/prop/direct/P1476", Type.LITERAL),
                new Mapping("(.?)publication-date.year","http://www.wikidata.org/prop/direct/P577", Type.LITERAL,XSDtime)
                ));
    }
    private class ClassForDoi extends CustomMapping{

        @Override
        public ArrayList<Triple> function(HashMap<String, String> article, String key) {
            Node object;
            Utility utility = new Utility();
            ArrayList<Triple> triples=new ArrayList<>();
            Node predicate = utility.createURI(getMapping().getPropertyUri());
            Node subject = Main.getSubject(article,publication,key);
            if(getMapping().getTag().contains("external-id-value"))
            {
                String type=article.get(key.replace("value","type"));
                if(type!=null && type.equals("doi"))
                {
                    String value=article.get(key);
                    object = NodeFactory.createLiteral(value);
                    triples.add(new Triple(subject,predicate,object));
                }
            }
            return triples;
        }
    }
}
