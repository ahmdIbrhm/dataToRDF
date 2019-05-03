package eu.qanswer.mapping.orcId;

import eu.qanswer.mapping.*;
import org.apache.jena.datatypes.xsd.impl.XSDYearType;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static org.apache.jena.datatypes.xsd.XSDDatatype.*;

public class Publication extends AbstractClassMapping {
    //the url used for all instances
    Publication publication=this;
    public Publication(){
        format = "xml";
        baseUrl = "http://orcid.org/";
        file="C:\\Users\\My pc\\Desktop\\cleared.xml";
//        file = "C:\\Users\\My pc\\Desktop\\test\\0000-0001-5105-9000.xml";
        key="profiles.record.activities-summary.works.group.work-summary.put-code";
        iterator="profiles.record.activities-summary.works";
        mappings = new ArrayList<>(Arrays.asList(
                new Mapping("(.?)work-summary(.*).external-id-value","http://www.wikidata.org/prop/direct/P356",baseUrl,new ClassForDoi(),Type.CUSTOM),
                new Mapping("(.?).title.title","http://www.w3.org/2000/01/rdf-schema#label", Type.LITERAL),
                //work type?
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
            Node subject = Main2.getSubject(article,publication,key);
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
