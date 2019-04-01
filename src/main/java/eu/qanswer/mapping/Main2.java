package eu.qanswer.mapping;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.riot.RDFFormat;
import org.apache.jena.riot.system.StreamRDF;
import org.apache.jena.riot.system.StreamRDFWriter;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import informa.Organization;
import informa.Trial;

import static org.apache.jena.datatypes.xsd.XSDDatatype.XSDinteger;

public class Main2 {

    static final String endpoint = "http://qanswer-core1.univ-st-etienne.fr/api/endpoint/wikidata/sparql";
    //static final String endpoint = "https://query.wikidata.org/sparql";
    static final String OUTPUT_PATH = "/Users/Dennis/IdeaProjects/semanticscholar/newclin.ttl";

    public static void main(String[] argv) throws IOException {

        StreamRDF writer = StreamRDFWriter.getWriterStream(new FileOutputStream(OUTPUT_PATH), RDFFormat.NTRIPLES);

        //uses links to extract important information from wikidata
        List<AbstractClassMapping> mappingsList = new ArrayList<>();
        mappingsList.add(new Trial());
        //mappingsList.add(new Investigator());
        mappingsList.add(new Organization());
        for (AbstractClassMapping mappings : mappingsList) {
            for (Mapping m : mappings.mappings){
                if (m.getPropertyUri().contains("http://www.wikidata.org/prop/direct/")) {
                    String construct = "CONSTRUCT {?s <http://wikiba.se/ontology#directClaim> <" + m.getPropertyUri() + "> . ?s ?p ?o} where { " +
                            "?s <http://wikiba.se/ontology#directClaim> <" + m.getPropertyUri() + "> . " +
                            "?s ?p ?o " +
                            "}";
                    //System.out.println(construct);
                    Query query = QueryFactory.create(construct);
                    QueryExecution qexec = QueryExecutionFactory.sparqlService(endpoint, query);

                    Model constructModel = qexec.execConstruct();

                    StmtIterator a = constructModel.listStatements();
                    while (a.hasNext()) {
                        Statement statement = a.next();
                        writer.triple(statement.asTriple());
                    }
                    qexec.close();
                    constructModel.close();
                }
            }
        }




        List<String> qids = Arrays.asList("Q8441","Q467","Q6581097","Q6581072","Q42824069","Q42824440","Q42824827","Q42825046","Q15","Q18","Q46","Q48","Q49","Q27468","Q7204","Q828","Q538","Q408","Q7204", "Q30612");
        for (String qid : qids){
            String construct = "CONSTRUCT {?s ?p ?o} where { VALUES ?s { <http://www.wikidata.org/entity/"+qid+"> } . ?s ?p ?o }";
            Query query = QueryFactory.create(construct);
            QueryExecution qexec = QueryExecutionFactory.sparqlService(endpoint, query);

            Model constructModel = qexec.execConstruct();

            StmtIterator a = constructModel.listStatements();
            while (a.hasNext()) {
                Statement statement = a.next();
                writer.triple(statement.asTriple());
            }
            qexec.close();
            constructModel.close();
            //retrive wikipedia links
            construct = "CONSTRUCT {" +
                    "?s <http://schema.org/about> ?s ." +
                    " ?s ?p ?o2} " +
                    "where {" +
                    " VALUES ?o { <http://www.wikidata.org/entity/"+qid+"> } ." +
                    " ?s <http://schema.org/about> ?o ." +
                    " ?s ?p ?o2}";
            query = QueryFactory.create(construct);
            qexec = QueryExecutionFactory.sparqlService(endpoint, query);

            constructModel = qexec.execConstruct();

            a = constructModel.listStatements();
            while (a.hasNext()) {
                Statement statement = a.next();
                writer.triple(statement.asTriple());
            }
            qexec.close();
            constructModel.close();
        }

        List<String> qidsTypes = Arrays.asList("Q6256", "Q12136");
        for (String qid : qidsTypes){
            String construct = "CONSTRUCT {?s ?p ?o} where {?s <http://www.wikidata.org/prop/direct/P31> <http://www.wikidata.org/entity/"+qid+"> . ?s ?p ?o}";
            Query query = QueryFactory.create(construct);
            QueryExecution qexec = QueryExecutionFactory.sparqlService(endpoint, query);

            Model constructModel = qexec.execConstruct();

            StmtIterator a = constructModel.listStatements();
            while (a.hasNext()) {
                Statement statement = a.next();
                writer.triple(statement.asTriple());
            }
            qexec.close();
            constructModel.close();
            //retrive wikipedia links
            construct = "CONSTRUCT {" +
                    "?s <http://schema.org/about> ?s ." +
                    " ?s ?p ?o2} " +
                    "where {" +
                    " VALUES ?o { <http://www.wikidata.org/entity/"+qid+"> } ." +
                    " ?s <http://schema.org/about> ?o ." +
                    " ?s ?p ?o2}";
            query = QueryFactory.create(construct);
            qexec = QueryExecutionFactory.sparqlService(endpoint, query);

            constructModel = qexec.execConstruct();

            a = constructModel.listStatements();
            while (a.hasNext()) {
                Statement statement = a.next();
                writer.triple(statement.asTriple());
            }
            qexec.close();
            constructModel.close();


            construct = "CONSTRUCT {" +
                    "?s <http://schema.org/about> ?s ." +
                    " ?s ?p ?o2} " +
                    "where {" +
                    " ?o <http://www.wikidata.org/prop/direct/P31> <http://www.wikidata.org/entity/"+qid+"> ." +
                    " ?s <http://schema.org/about> ?o ." +
                    " ?s ?p ?o2}";
            query = QueryFactory.create(construct);
            qexec = QueryExecutionFactory.sparqlService(endpoint, query);

            constructModel = qexec.execConstruct();

            a = constructModel.listStatements();
            while (a.hasNext()) {
                Statement statement = a.next();
                writer.triple(statement.asTriple());
            }
            qexec.close();
            constructModel.close();
        }


        //extracts the information
        for (AbstractClassMapping mappings : mappingsList) {

            HashMap<String, String> article = new HashMap<String, String>();

            InputStream inputstream = new FileInputStream(mappings.file);
            JsonReader reader = new JsonReader(new InputStreamReader(inputstream, "UTF-8"));

            String name = "";

            reader.setLenient(true);


            HashMap <String,Pattern> fast = new HashMap <>();
            int count = 0;
            boolean continuing = true;
            while (continuing) {
                if (count%1000 == 0){
                    System.out.println(count);
                }
                String s = "";

                JsonToken token = reader.peek();
                switch (token) {
                    case BEGIN_ARRAY:
                        reader.beginArray();
                        break;
                    case END_ARRAY:
                        reader.endArray();
                        break;
                    case BEGIN_OBJECT:
                        reader.beginObject();
                        break;
                    case END_OBJECT:
                        count++;
                        //System.out.println(count);
                        if (reader.getPath().split("\\.").length < 3) {
                            processMap(article, writer, mappings, fast);
                            article = new HashMap<String, String>();
                        }
                        reader.endObject();
                        break;
                    case NAME:
                        name = reader.nextName();
                        break;
                    case STRING:
                        s = reader.nextString();
                        if (!name.equals(""))
                            article.put(reader.getPath().replace("$.", ""), s);
                        break;
                    case NUMBER:
                        s = reader.nextString();
                        if (!name.equals(""))
                            article.put(reader.getPath().replace("$.", ""), s);
                        break;
                    case BOOLEAN:
                        boolean b = reader.nextBoolean();
                        break;
                    case NULL:
                        reader.nextNull();
                        break;
                    case END_DOCUMENT:
                        continuing = false;
                        break;
                }
            }


    }
    writer.finish();
}

    private static void processMap(HashMap<String, String> article, StreamRDF writer, AbstractClassMapping mappings, HashMap <String,Pattern> fast) {

        Utility utility = new Utility();
            Node subject = NodeFactory.createURI(mappings.baseUrl + article.get(mappings.getKey()));
            for (String key : article.keySet()) {
                for (Mapping mapping : mappings.mappings) {
                    //System.out.println(key);
                    Pattern p;

                    if (fast.containsKey(mapping.getTag().toString())){
                        p = fast.get(mapping.getTag());
                    } else {
                        p = Pattern.compile(mapping.getTag());
                        fast.put(mapping.getTag(),p);
                    }

                    Matcher m = p.matcher(key);
                    //System.out.println(key);
                    if (m.find()) {
                        //System.out.println("Found "+mapping.getTag());
                        Node predicate = utility.createURI(mapping.getPropertyUri());
                        Node object = null;
                        if (mapping.getType() == null) {
                            object = NodeFactory.createURI(mapping.getObject());
                            Triple t = new Triple(subject, predicate, object);
                            writer.triple(t);
                        } else {

                            switch (mapping.getType()) {
                                case LITERAL:
                                    if (mapping.getDatatype() == null) {
                                        object = Utility.createLiteral(article.get(key));
                                        Triple t = new Triple(subject, predicate, object);
                                        writer.triple(t);
                                    } else {
                                        object = Utility.createLiteral(article.get(key), mapping.getDatatype());
                                        Triple t = new Triple(subject, predicate, object);
                                        writer.triple(t);
                                    }
                                    break;
                                case URI:
                                    if (mapping.getBaseurl() != null) {
                                        object = utility.createURI(mapping.getBaseurl() + article.get(key));
                                    } else {
                                        if (article.get(key).startsWith("http://")) {
                                            object = utility.createURI(article.get(key));
                                        } else {
                                            object = utility.createURI(mappings.getBaseUrl() + article.get(key));
                                        }
                                    }
                                    Triple t = new Triple(subject, predicate, object);
                                    writer.triple(t);
                                    break;
                                case URI_WITH_LABEL:
                                    if (mapping.getBaseurl() != null) {
                                        object = utility.createURI(mapping.getBaseurl() + article.get(key));
                                        t = new Triple(subject, predicate, object);
                                        writer.triple(t);
                                        t = new Triple(object, utility.createURI("http://www.w3.org/2000/01/rdf-schema#label"), NodeFactory.createLiteral(article.get(key)));
                                        writer.triple(t);
                                    } else {
                                        if (article.get(key).startsWith("http://")) {
                                            object = utility.createURI(article.get(key));
                                            t = new Triple(subject, predicate, object);
                                            writer.triple(t);
                                            t = new Triple(object, utility.createURI("http://www.w3.org/2000/01/rdf-schema#label"), NodeFactory.createLiteral(article.get(key)));
                                            writer.triple(t);
                                        } else {
                                            object = utility.createURI(mappings.getBaseUrl() + article.get(key));
                                            t = new Triple(subject, predicate, object);
                                            writer.triple(t);
                                        }
                                    }

                                    break;
                                case DATE:
                                    object = NodeFactory.createLiteral(article.get(key), XSDDatatype.XSDdateTime);
                                    break;
                                case CUSTOM:
                                    //custom age
                                    if (mapping.getTag().contains("minAge")) {
                                        //System.out.println("Hello");
                                        int minAge = Integer.parseInt(article.get(key).replace(".0", ""));
                                        int maxAge = 100;
                                        if (article.containsKey(key.replace("minAge", "maxAge").replace(".0", ""))) {
                                            maxAge = Integer.parseInt(article.get(key.replace("minAge", "maxAge")).replace(".0", ""));

                                        }
                                        for (Integer k = minAge; k <= maxAge; k++) {
                                            object = NodeFactory.createLiteral(k.toString(), XSDinteger);
                                            t = new Triple(subject, predicate, object);
                                            writer.triple(t);
                                        }
                                    } else if (mapping.getTag().contains("maxAge")) {
                                        int maxAge = Integer.parseInt(article.get(key).replace(".0", ""));
                                        int minAge = 0;
                                        if (article.containsKey(key.replace("maxAge", "minAge"))) {
                                            minAge = Integer.parseInt(article.get(key.replace("maxAge", "minAge")).replace(".0", ""));

                                        }
                                        for (Integer k = minAge; k <= maxAge; k++) {
                                            object = NodeFactory.createLiteral(k.toString(), XSDinteger);
                                            t = new Triple(subject, predicate, object);
                                            writer.triple(t);
                                        }
                                    }
                                    //custom for gender
                                    if (mapping.getTag().contains("gender")) {
                                        if (article.get(key).equals("Both")) {
                                            object = NodeFactory.createURI("http://www.wikidata.org/entity/Q467");
                                            t = new Triple(subject, predicate, object);
                                            writer.triple(t);
                                            object = NodeFactory.createURI("http://www.wikidata.org/entity/Q8441");
                                            t = new Triple(subject, predicate, object);
                                            writer.triple(t);
                                        } else if (article.get(key).equals("Male")) {
                                            object = NodeFactory.createURI("http://www.wikidata.org/entity/Q8441");
                                            t = new Triple(subject, predicate, object);
                                            writer.triple(t);
                                        } else if (article.get(key).equals("Female")) {
                                            object = NodeFactory.createURI("http://www.wikidata.org/entity/Q467");
                                            t = new Triple(subject, predicate, object);
                                            writer.triple(t);
                                        }
                                    }
                                    //custum for clinical phase
                                    if (mapping.getTag().contains("trialPhase")) {
                                        if (article.get(key).equals("I") || article.get(key).contains("I/")) {
                                            object = NodeFactory.createURI("http://www.wikidata.org/entity/Q42824069");
                                            t = new Triple(subject, predicate, object);
                                            writer.triple(t);
                                        }
                                        if (article.get(key).equals("II") || article.get(key).contains("II/") || article.get(key).contains("/II")) {
                                            object = NodeFactory.createURI("http://www.wikidata.org/entity/Q42824440");
                                            t = new Triple(subject, predicate, object);
                                            writer.triple(t);
                                        }
                                        if (article.get(key).contains("III") || article.get(key).contains("III/") || article.get(key).contains("/III")) {
                                            object = NodeFactory.createURI("http://www.wikidata.org/entity/Q42824827");
                                            t = new Triple(subject, predicate, object);
                                            writer.triple(t);
                                        }
                                        if (article.get(key).contains("IV") || article.get(key).contains("/IV")) {
                                            object = NodeFactory.createURI("http://www.wikidata.org/entity/Q42825046");
                                            t = new Triple(subject, predicate, object);
                                            writer.triple(t);
                                        }
                                    }











                                    //custom for region
                                    if (mapping.getTag().contains("trialRegions")) {
                                        if (article.get(key).equals("Africa")) {
                                            object = NodeFactory.createURI("http://www.wikidata.org/entity/Q15");
                                            t = new Triple(subject, predicate, object);
                                            writer.triple(t);
                                        }
                                        if (article.get(key).equals("South America")) {
                                            object = NodeFactory.createURI("http://www.wikidata.org/entity/Q18");
                                            t = new Triple(subject, predicate, object);
                                            writer.triple(t);
                                        }
                                        if (article.get(key).equals("Europe")) {
                                            object = NodeFactory.createURI("http://www.wikidata.org/entity/Q46");
                                            t = new Triple(subject, predicate, object);
                                            writer.triple(t);
                                        }
                                        if (article.get(key).equals("Asia")) {
                                            object = NodeFactory.createURI("http://www.wikidata.org/entity/Q48");
                                            t = new Triple(subject, predicate, object);
                                            writer.triple(t);
                                        }
                                        if (article.get(key).equals("North America")) {
                                            object = NodeFactory.createURI("http://www.wikidata.org/entity/Q49");
                                            t = new Triple(subject, predicate, object);
                                            writer.triple(t);
                                        }
                                        if (article.get(key).equals("Eastern Europe")) {
                                            object = NodeFactory.createURI("http://www.wikidata.org/entity/Q27468");
                                            t = new Triple(subject, predicate, object);
                                            writer.triple(t);
                                        }
                                        if (article.get(key).equals("Middle East")) {
                                            object = NodeFactory.createURI("http://www.wikidata.org/entity/Q7204");
                                            t = new Triple(subject, predicate, object);
                                            writer.triple(t);
                                        }
                                        if (article.get(key).equals("Americas")) {
                                            object = NodeFactory.createURI("http://www.wikidata.org/entity/Q828");
                                            t = new Triple(subject, predicate, object);
                                            writer.triple(t);
                                        }
                                        if (article.get(key).equals("Australia/Oceania")) {
                                            object = NodeFactory.createURI("http://www.wikidata.org/entity/Q538");
                                            t = new Triple(subject, predicate, object);
                                            writer.triple(t);
                                            object = NodeFactory.createURI("http://www.wikidata.org/entity/Q408");
                                            t = new Triple(subject, predicate, object);
                                            writer.triple(t);
                                        }
                                        if (article.get(key).equals("Western Asia/Middle East")) {
                                            object = NodeFactory.createURI("http://www.wikidata.org/entity/Q7204");
                                            t = new Triple(subject, predicate, object);
                                            writer.triple(t);
                                        }
                                    }
                                    // custom name
                                    if (mapping.getTag().contains("investigatorFirstName")) {
                                        object = NodeFactory.createLiteral(article.get(key) + " " + article.get(key.replace("investigatorFirstName", "investigatorLastName")));
                                        t = new Triple(subject, predicate, object);
                                        writer.triple(t);
                                    }
                                    if (key.contains("investigatorGeoLocation.lat") && article.get(key)!=null) {
                                        //"Point(12.482777777778 41.893055555556)"^^<http://www.opengis.net/ont/geosparql#wktLiteral>
                                        object = NodeFactory.createLiteral("Point(" + article.get(key) + " " + article.get(key.replace("investigatorGeoLocation.lon", "investigatorGeoLocation.lat")) + ")^^<http://www.opengis.net/ont/geosparql#wktLiteral>");
                                        t = new Triple(subject, predicate, object);
                                        writer.triple(t);
                                    }
                                    if (key.contains("organizationGeoLocation.latitude") && article.get(key)!= null) {
                                        //System.out.println(key);
                                        //"Point(12.482777777778 41.893055555556)"^^<http://www.opengis.net/ont/geosparql#wktLiteral>
                                        object = NodeFactory.createLiteral("Point(" + article.get(key) + " " + article.get(key.replace("longitude", "latitude")) + ")^^<http://www.opengis.net/ont/geosparql#wktLiteral>");
                                        t = new Triple(subject, predicate, object);
                                        writer.triple(t);
                                    }
                                    if (key.contains("organizationTrials")){
                                        object = NodeFactory.createURI(mapping.getBaseurl()+article.get(key));
                                        t = new Triple(subject, predicate, object);
                                        writer.triple(t);
                                        //System.out.println(t.getSubject().toString()+"--"+t.getPredicate().toString()+"--"+t.getObject().toString());
                                        t = new Triple(object,NodeFactory.createURI("http://www.wikidata.org/prop/direct/P625"),NodeFactory.createLiteral("Point(" + article.get("organizationGeoLocation.longitude") + " " + article.get("organizationGeoLocation.latitude") + ")^^<http://www.opengis.net/ont/geosparql#wktLiteral>"));
                                        writer.triple(t);
                                    }
                            }
                        }
                    }
                }
            }
        }
    }

