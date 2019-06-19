package eu.qanswer.mapping.mappings.semanticscholar;

import eu.qanswer.mapping.Main;
import eu.qanswer.mapping.configuration.AbstractConfiguration;
import eu.qanswer.mapping.configuration.Mapping;
import eu.qanswer.mapping.configuration.Type;

import eu.qanswer.mapping.mappings.orcId.CustomMapping;
import eu.qanswer.mapping.mappings.orcId.Publication;
import eu.qanswer.mapping.utility.Utility;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Article extends AbstractConfiguration {
    Article art=this;
    public Article()
    {
        format = "json";
        file="sample-S2-records";
        baseUrl = "http://semanticscholar.org/paper/";;
        key = "publications.id";
        iterator = "publications";
        mappings = new ArrayList<Mapping>(Arrays.asList(
                new Mapping("publications.id", "http://www.wikidata.org/prop/direct/P4011", Type.LITERAL),


                new Mapping("title", "http://www.wikidata.org/prop/direct/P496", Type.URI),
                new Mapping("title", "http://www.w3.org/2000/01/rdf-schema#label", Type.LITERAL),

                new Mapping("paperAbstract", "http://dbpedia.org/ontology/abstract", Type.LITERAL),

//            new Mapping("entities", "http://rdfs.org/ns/void#entities", Type.LITERAL),

                new Mapping("s2Url",    "http://www.wikidata.org/prop/direct/P845", Type.URI),

                new Mapping("pdfUrls", "http://www.wikidata.org/prop/direct/P4945", Type.URI),
                new Mapping("authors", "http://www.wikidata.org/prop/direct/P50",baseUrl,new ClassForAuthor(), Type.CUSTOM),



                new Mapping("year", "http://www.wikidata.org/prop/direct/P577", Type.LITERAL),

                new Mapping("venue", "http://www.wikidata.org/prop/direct/P1433", Type.LITERAL),

//                new Mapping("journalName", "http://data.europa.eu/eli/ontology#published_in", Type.LITERAL),

//                new Mapping("journalVolume", "http://swrc.ontoware.org/ontology#volume", Type.LITERAL),

//                new Mapping("journalPages", "http://swrc.ontoware.org/ontology#pages", Type.LITERAL),

//                new Mapping("sources", "http://purl.org/dc/terms/source", Type.LITERAL),

                new Mapping("doi", "http://www.wikidata.org/prop/direct/P356", Type.LITERAL),

//                new Mapping("doiUrl", "http://ns.nature.com/terms/doi", Type.URI),

                new Mapping("pmid", "http://www.wikidata.org/prop/direct/P698", Type.LITERAL),

                new Mapping("inCitations", "http://www.wikidata.org/prop/direct/P2860", Type.URI),

                new Mapping("outCitations", "http://www.wikidata.org/prop/direct/P2860", Type.URI)

        ));
    }
    private class ClassForAuthor extends CustomMapping {


        @Override
        public ArrayList<Triple> function(HashMap<String, String> article, String key) {
            Node object;
            Utility utility = new Utility();
            ArrayList<Triple> triples=new ArrayList<>();
            Node predicate = utility.createURI(getMapping().getPropertyUri());
            Node subject = Main.getSubject(article,art,key);
            if(getMapping().getTag().contains("authors.name"))
            {
                String name=article.get(key);
                String id = article.get(key.replace("name", "ids"));
                String url="https://www.semanticscholar.org/author/"+name.replace(" ","-")+"/"+id;
                object = NodeFactory.createLiteral(url);
                triples.add(new Triple(subject, predicate, object));
            }
            return triples;
        }
    }
}
