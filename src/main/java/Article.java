import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Article {
    public static final ArrayList<Mapping> mappings = new ArrayList<Mapping>(Arrays.asList(
            new Mapping("id", "", Type.STRING),

            new Mapping("title", "http://purl.org/dc/elements/1.1/title", Type.URI),
            new Mapping("title", "http://www.w3.org/2000/01/rdf-schema#label", Type.STRING),

            new Mapping("paperAbstract","",Type.STRING),

            new Mapping("entities", "", Type.STRING),

            new Mapping("s2Url", "", Type.URI),

            new Mapping("s2PdfUrl", "", Type.URI),

            new Mapping("pdfUrls", "", Type.CUSTOM),

            new Mapping("authors", "http://dbpedia.org/ontology/author", Type.CUSTOM),

            new Mapping("inCitations", "http://www.w3.org/1999/xhtml/vocab#cite", Type.CUSTOM),

            new Mapping("outCitations", "http://www.w3.org/1999/xhtml/vocab#cite", Type.CUSTOM),

            new Mapping("year", "http://purl.org/dc/terms/issued", Type.STRING),

            new Mapping("venue","http://data.europa.eu/eli/ontology#published_in", Type.STRING),

            new Mapping ("journalName", "http://data.europa.eu/eli/ontology#published_in", Type.STRING),

            new Mapping("journalVolume", "http://swrc.ontoware.org/ontology#volume", Type.STRING),

            new Mapping("journalPages", "http://swrc.ontoware.org/ontology#pages", Type.STRING),

            new Mapping("sources", "", Type.STRING),

            new Mapping("doi", "", Type.STRING),

            new Mapping( "doiUrl", "", Type.URI),

            new Mapping("pmid", "", Type.STRING)

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
