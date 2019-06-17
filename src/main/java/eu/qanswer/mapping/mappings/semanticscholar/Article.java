package eu.qanswer.mapping.mappings.semanticscholar;

import eu.qanswer.mapping.configuration.Mapping;
import eu.qanswer.mapping.configuration.Type;

import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Article {
    public static final ArrayList<Mapping> mappings = new ArrayList<Mapping>(Arrays.asList(
            new Mapping("id", "http://ns.nature.com/terms/id", Type.LITERAL),

            new Mapping("title", "http://purl.org/dc/elements/1.1/title", Type.URI),
            new Mapping("title", "http://www.w3.org/2000/01/rdf-schema#label", Type.LITERAL),

            new Mapping("paperAbstract","http://dbpedia.org/ontology/abstract",Type.LITERAL),

            new Mapping("entities", "http://rdfs.org/ns/void#entities", Type.LITERAL),

            new Mapping("s2Url", "http://www.w3.org/2002/07/owl#sameAs", Type.URI),

            new Mapping("s2PdfUrl", "http://www.w3.org/2002/07/owl#sameAs", Type.URI),


            new Mapping("year", "http://purl.org/dc/terms/issued", Type.LITERAL),

            new Mapping("venue","http://data.europa.eu/eli/ontology#published_in", Type.LITERAL),

            new Mapping ("journalName", "http://data.europa.eu/eli/ontology#published_in", Type.LITERAL),

            new Mapping("journalVolume", "http://swrc.ontoware.org/ontology#volume", Type.LITERAL),

            new Mapping("journalPages", "http://swrc.ontoware.org/ontology#pages", Type.LITERAL),

            new Mapping("sources", "http://purl.org/dc/terms/source", Type.LITERAL),

            new Mapping("doi", "http://ns.nature.com/terms/doi", Type.LITERAL),

            new Mapping( "doiUrl", "http://ns.nature.com/terms/doi", Type.URI),

            new Mapping("pmid", "http://purl.org/ontology/bibo/pmid", Type.LITERAL),

            new Mapping("authors.name", "http://dbpedia.org/ontology/author", Type.LITERAL),

            //TODO authors id

            new Mapping("pdfUrls", "http://www.w3.org/2002/07/owl#sameAs", Type.URI),

            new Mapping("inCitations", "http://www.w3.org/1999/xhtml/vocab#cite", Type.URI),

            new Mapping("outCitations", "http://www.w3.org/1999/xhtml/vocab#cite", Type.URI)

    ));

    public static  List<Mapping> getMappings(){
        return mappings;
    }

    public static Map<String,ObjectUtils.Null> getMappedTags(){
        Map<String, ObjectUtils.Null> map = new HashMap<String,ObjectUtils.Null>();
        for (Mapping m : mappings){
            map.put(m.getTag(),null);
        }
        return map;
    }

    public static List<Mapping> getMapping(String tag){
        List<Mapping> properties = new ArrayList<Mapping>();
        for (Mapping m : mappings){
            if (m.getTag().equals(tag)){
                properties.add(m);
            }
        }

        return properties;
    }
}
