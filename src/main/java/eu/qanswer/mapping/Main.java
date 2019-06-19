package eu.qanswer.mapping;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.opencsv.CSVReader;

import eu.qanswer.mapping.configuration.AbstractConfiguration;
import eu.qanswer.mapping.configuration.CSVConfiguration;
import eu.qanswer.mapping.configuration.Mapping;
import eu.qanswer.mapping.utility.Utility;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class Main {
//    "java -jar target/eu.wdaqua.semanticscholar-1.0-SNAPSHOT-jar-with-dependencies.jar -f eu.qanswer.mapping.mappings.orcId.OrcId,eu.qanswer.mapping.mappings.orcId.OrgStudy,eu.qanswer.mapping.mappings.orcId.OrgWork,eu.qanswer.mapping.mappings.orcId.Publication -o orcid.ttl"


    private static final String endpoint = "http://qanswer-core1.univ-st-etienne.fr/api/endpoint/wikidata/sparql";
    //static final String endpoint = "https://query.wikidata.org/sparql";
//    static final String OUTPUT_PATH = "C:\\Users\\My pc\\Desktop\\orcId.ttl";

    @Parameter(names={"--filesArguments", "-f"})
    private String filesArguments;

    @Parameter(names = {"--outputFilePath", "-o"})
    private String outputFilePath="test.out";

    @Parameter(names = "--help", help = true)
    private boolean help = false;


    public void run() throws Exception {
        if (filesArguments == null){
            System.out.println("Specify the --filesArguments argument, or --help if you need more information");
            return;
        }
        if (help) {
            System.out.println("Help Yourself");
        }
        else {
            StreamRDF writer = StreamRDFWriter.getWriterStream(new FileOutputStream(new File(outputFilePath)), RDFFormat.NTRIPLES);
            //uses links to extract important information from wikidata
            List<AbstractConfiguration> mappingsList = new ArrayList<>();
            String[] files = filesArguments.split(",");
            for (String filePath : files) {
                Class<?> aClass;
                try {
                    aClass = Class.forName(filePath.trim());
                    Constructor<?> ctor = aClass.getConstructor();
                    AbstractConfiguration object = (AbstractConfiguration) ctor.newInstance();
                    mappingsList.add(object);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            for (AbstractConfiguration mappings : mappingsList) {
                for (Mapping m : mappings.mappings) {
                    if (m.getPropertyUri().contains("http://www.wikidata.org/prop/direct/"))
                    {
                        String construct = "CONSTRUCT {?s <http://wikiba.se/ontology#directClaim> <" + m.getPropertyUri() + "> . ?s ?p ?o} where { " +
                                "?s <http://wikiba.se/ontology#directClaim> <" + m.getPropertyUri() + "> . " +
                                "?s ?p ?o " +
                                "}";
                        Query query = QueryFactory.create(construct);
                        QueryExecution qexec = QueryExecutionFactory.sparqlService(endpoint, query);
                        Model constructModel = qexec.execConstruct();

                        StmtIterator a = constructModel.listStatements();
                        while (a.hasNext())
                        {
                            Statement statement = a.next();
                            writer.triple(statement.asTriple());
                        }
                        qexec.close();
                        constructModel.close();
                    }
                    if(m.getObject()!=null && m.getObject().contains("http://www.wikidata.org/entity/"))
                    {
                        System.out.println(m.getObject());
                        String construct = "CONSTRUCT { <"+m.getObject()+"> ?p ?o} where { " +
                                "<" + m.getObject() + "> ?p ?o " +
                                "}";
                        Query query = QueryFactory.create(construct);
                        QueryExecution qexec = QueryExecutionFactory.sparqlService(endpoint, query);
                        Model constructModel = qexec.execConstruct();

                        StmtIterator a = constructModel.listStatements();
                        while (a.hasNext())
                        {
                            Statement statement = a.next();
                            writer.triple(statement.asTriple());
                        }
                        qexec.close();
                        constructModel.close();
                    }
                }
            }

            //extracts the information
            for (AbstractConfiguration mappings : mappingsList) {
                if (mappings.getFormat().equals("json")) {
                    parseJson(mappings,writer);
                } else if(mappings.getFormat().equals("xml")) {
                    parseXML(mappings,writer);
                }
                else if (mappings.getFormat().equals("csv")){
                    parseCSV((CSVConfiguration) mappings,writer);
                } else {
                    System.out.println("Error");
                }
                writer.finish();
            }
        }
    }
    private int nbOfTimes=0;
    private void parseXML(AbstractConfiguration mappings, StreamRDF writer) {
        String iterator = mappings.getIterator();
        HashMap<String, String> article = new HashMap<>();
        ArrayList<String> path=new ArrayList<>();
        try
        {
            Stack<String> stack=new Stack<>();
            String lastElementOnStack="";
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLEventReader eventReader = factory.createXMLEventReader(new FileReader(mappings.getFile()));
            while (eventReader.hasNext())
            {
                XMLEvent event = eventReader.nextEvent();
                switch (event.getEventType())
                {
                    case XMLStreamConstants.START_ELEMENT:
                        StartElement startElement = event.asStartElement();
                        String qName = startElement.getName().getLocalPart();
                        stack.push(qName);

                        path.add(qName);
//                        System.out.println(splitByPoints(path));
//                        System.out.println(lastElementOnStack);
//                        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`");
                        if(qName.equals(lastElementOnStack))
                        {
                            String arrayPath=path.get(path.size()-2);
                            if(arrayPath.length()>3)
                            {
                                String number= StringUtils.substringBetween(arrayPath, "[", "]");
                                if(NumberUtils.isNumber(number))
                                {
                                    int newNbOfTimes=Integer.parseInt(number)+1;
                                    String newArrayPath=(arrayPath.split("\\[(.*?)\\]"))[0];
                                    newArrayPath=newArrayPath+"["+newNbOfTimes+"]";
                                    path.remove(path.size()-2);
                                    path.add(path.size()-1,newArrayPath);
                                }
                                else
                                {
                                    arrayPath=arrayPath+"["+nbOfTimes+"]";
                                    path.remove(path.size()-2);
                                    path.add(path.size()-1,arrayPath);
                                    nbOfTimes++;
                                }
                            }
                        }
                        else
                            nbOfTimes=0;
//                        System.out.println(splitByPoints(path));
//                        System.out.println("==============================================");
                        String pathString;
                        Iterator attributes = event.asStartElement().getAttributes();
                        while(attributes.hasNext())
                        {
                            Attribute attribute = (Attribute) attributes.next();
                            path.add(attribute.getName().toString());
                            article.put(splitByPoints(path),attribute.getValue());
                            path.remove(path.size()-1);
                        }
                        break;
                    case XMLStreamConstants.CHARACTERS:
                        Characters characters = event.asCharacters();
                        String data=characters.getData();
                        if(!data.trim().equals(""))
                        {
                            article.put(splitByPoints(path), data.trim());
                        }
                        break;
                    case XMLStreamConstants.END_ELEMENT:
                        event.asEndElement();
                        if(stack.size()!=0)
                            lastElementOnStack=stack.pop();
                        path.remove(path.size()-1);
                        pathString=splitByPoints(path);
                        if(pathString.replaceAll("\\[(.*?)]", "").replace("$.", "").equals(iterator))
                        {
//                            System.out.println(article);
                            processMap(article, writer, mappings);
                            article = new HashMap<>();
                        }
                        break;
                    case XMLStreamConstants.END_DOCUMENT:
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void parseJson(AbstractConfiguration mappings, StreamRDF writer) throws Exception{
        String iterator = mappings.getIterator();
        int counterNew = 0;
        int counterOld = -1;
        HashMap<String, String> article = new HashMap<>();
        InputStream inputstream = new FileInputStream(mappings.file);
        JsonReader reader = new JsonReader(new InputStreamReader(inputstream, "UTF-8"));
        String name = "";
        reader.setLenient(true);

        boolean continuing = true;
        while (continuing) {
            String s;
            JsonToken token = reader.peek();
            switch (token) {
                case BEGIN_ARRAY:
                    reader.beginArray();
                    break;
                case END_ARRAY:
                    reader.endArray();
                    break;
                case BEGIN_OBJECT:
                    if (reader.getPath().replaceAll("\\[(.*?)]", "").replace("$.", "").equals(iterator))
                    {
                        String[] list = iterator.split("\\.");
                        String arrayString = list[list.length - 1];
                        Pattern pattern = Pattern.compile(arrayString + "\\[(.*?)]");
                        Matcher matcher = pattern.matcher(reader.getPath());
                        if (matcher.find())
                        {
                            counterNew = Integer.parseInt(matcher.group(1));
                            counterOld = -1;
                        }
                    }
                    reader.beginObject();
                    break;
                case END_OBJECT:
                    reader.endObject();
                    if (reader.getPath().replaceAll("\\[(.*?)]", "").replace("$.", "").equals(iterator) && counterNew != counterOld) {
                        counterOld = counterNew;
                        processMap(article, writer, mappings);
                        article = new HashMap<>();
                    }
                    break;
                case NAME:
                    name = reader.nextName();
                    break;
                case STRING:
                case NUMBER:
                    s = reader.nextString();
                    if (!name.equals(""))
                        article.put(reader.getPath().replace("$.", ""), s);

                    break;
                case BOOLEAN:
                    reader.nextBoolean();
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
    private void parseCSV(CSVConfiguration mappings, StreamRDF writer) throws Exception{
        CSVReader reader = new CSVReader(new FileReader(mappings.file),mappings.separator);
        String [] nextLine;
        System.out.println("Reading header of the CSV file ...");
        String [] header = reader.readNext();
        for (int i=0; i<header.length; i++) {
            System.out.println(header[i]);
        }
        HashMap<String, String> article = new HashMap<>();
        System.out.println("Reading the CSV file ...");
        while ((nextLine = reader.readNext()) != null) {
            for (int i=0; i<nextLine.length; i++){
                article.put(header[i],nextLine[i]);
            }
            processMap(article, writer, mappings);
        }
    }

    public static void main(String[] argv) throws Exception {
        Main main = new Main();
            JCommander.newBuilder()
                    .addObject(main)
                    .build()
                    .parse(argv);
            main.run();

}

    private static void processMap(HashMap<String, String> article, StreamRDF writer, AbstractConfiguration mappings) {
        System.out.println(article);
        for (Mapping mapping : mappings.mappings)
        {
            ArrayList<Triple> triples = getObjects(mapping, mappings, article);
            for (Triple triple : triples) {
                writer.triple(triple);
            }
        }
    }

    private static String splitByPoints(List<String> array) {
        String result = "";
        if (array.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (String s : array) {
                sb.append(s).append(".");
            }
            result = sb.deleteCharAt(sb.length() - 1).toString();
        }
        return result;
    }
    public static Node getSubject(HashMap<String,String> article, AbstractConfiguration mappings, String key)
    {
        Utility utility=new Utility();
        String keyWithoutBrackets = key.replaceAll("\\[(.*?)]", "");
        List<String> arrayMappingsKey = new ArrayList<>(Arrays.asList(mappings.getKey().split("\\.")));
        List<String> arrayKeyArticle = new ArrayList<>(Arrays.asList(keyWithoutBrackets.split("\\.")));
        String[] keySplitted = key.replace("$.", "").split("\\.");
        ArrayList<String> finalSubject = new ArrayList<>();
        for (int i = 0; i < arrayMappingsKey.size(); i++)
        {
            if (i < arrayKeyArticle.size())
            {
                if (arrayKeyArticle.get(i).equals(arrayMappingsKey.get(i)))
                {
                    finalSubject.add(keySplitted[i]);
                }
                else {
                    finalSubject.add(arrayMappingsKey.get(i));
                }
            }
            else {
                finalSubject.add(arrayMappingsKey.get(i));
            }
        }
        String uri=mappings.baseUrl + article.get(splitByPoints(finalSubject));
        String [] array=uri.split(" ");
        String newUri=String.join("_",array);

        return (Utility.createURI(newUri));
    }
    private static Node getPredicate(Mapping mapping)
    {
        Utility utility = new Utility();
        return utility.createURI(mapping.getPropertyUri());
    }
    private static ArrayList<Triple> getObjects(Mapping mapping, AbstractConfiguration mappings, HashMap<String,String> article) {
        ArrayList<Triple> triples = new ArrayList<>();
        HashMap<String, Pattern> fast=new HashMap<>();
        Node subject, predicate, object;
        Utility utility = new Utility();
        for (String key : article.keySet())
        {
            Pattern p;
            if (mappings.format.equals("csv")) {
                p = Pattern.compile("^"+mapping.getTag()+"$");
                //p = fast.get("^"+mapping.getTag()+"$");
            } else if (fast.containsKey(mapping.getTag()))
            {
                p = fast.get(mapping.getTag());
            }
            else {
                p = Pattern.compile(mapping.getTag());
                fast.put(mapping.getTag(), p);
            }
            Matcher m = p.matcher(key);
            if (m.find())
            {
                if (mapping.getType() == null)
                {
                    subject = getSubject(article, mappings, key);
                    predicate = getPredicate(mapping);
                    object = Utility.createURI(mapping.getObject());
                    triples.add(new Triple(subject, predicate, object));
                }
                else {
                    switch (mapping.getType())
                    {
                        case LITERAL:
                            if (mapping.getDatatype() == null)
                            {
                                subject = getSubject(article, mappings, key);
                                predicate = getPredicate(mapping);
                                object = Utility.createLiteral(article.get(key));
                                triples.add(new Triple(subject, predicate, object));
                            }
                            else {
                                subject = getSubject(article, mappings, key);
                                predicate = getPredicate(mapping);
                                object = Utility.createLiteral(article.get(key), mapping.getDatatype());
                                triples.add(new Triple(subject, predicate, object));
                            }
                            break;
                        case URI:
                            if (mapping.getBaseurl() != null) {
                                subject = getSubject(article, mappings, key);
                                predicate = getPredicate(mapping);
                                object = utility.createURI(mapping.getBaseurl() + article.get(key));
                                triples.add(new Triple(subject, predicate, object));
                            } else {
                                if (article.get(key).startsWith("http://")) {
                                    subject = getSubject(article, mappings, key);
                                    predicate = getPredicate(mapping);
                                    object = utility.createURI(article.get(key));
                                    triples.add(new Triple(subject, predicate, object));
                                } else {
                                    subject = getSubject(article, mappings, key);
                                    predicate = getPredicate(mapping);
                                    object = utility.createURI(mappings.getBaseUrl() + article.get(key));
                                    triples.add(new Triple(subject, predicate, object));
                                }
                            }
                            break;
                        case URI_WITH_LABEL:
                            if (mapping.getBaseurl() != null)
                            {
                                subject = getSubject(article, mappings, key);
                                predicate = getPredicate(mapping);
                                object = utility.createURI(mapping.getBaseurl() + article.get(key));
                                triples.add(new Triple(subject, predicate, object));

                                subject = object;
                                predicate = utility.createURI("http://www.w3.org/2000/01/rdf-schema#label");
                                object = NodeFactory.createLiteral(article.get(key));
                                triples.add(new Triple(subject, predicate, object));
                            }
                            else {
                                if (article.get(key).startsWith("http://")) {
                                    subject = getSubject(article, mappings, key);
                                    predicate = getPredicate(mapping);
                                    object = utility.createURI(article.get(key));
                                    triples.add(new Triple(subject, predicate, object));

                                    subject = object;
                                    predicate = utility.createURI("http://www.w3.org/2000/01/rdf-schema#label");
                                    object = NodeFactory.createLiteral(article.get(key));
                                    triples.add(new Triple(subject, predicate, object));

                                } else {
                                    subject = getSubject(article, mappings, key);
                                    predicate = getPredicate(mapping);
                                    object = utility.createURI(mappings.getBaseUrl() + article.get(key));
                                    triples.add(new Triple(subject, predicate, object));

                                }
                            }
                            break;
                        case DATE:
//                            object = NodeFactory.createLiteral(article.get(key), XSDDatatype.XSDdateTime);
                            break;
                        case CLASS:
                            if(mapping.getObject()!=null)
                            {
                                subject = getSubject(article, mappings, key);
                                predicate = getPredicate(mapping);
                                object = utility.createURI(mapping.getObject());
                                triples.add(new Triple(subject, predicate, object));
                            }
                            break;
                        case CUSTOM:
                            triples.addAll(mapping.getCustomMapping().function(article, key));
                            break;
                    }
                }
            }
        }
        return triples;
    }
    public String printHashmap(HashMap<String, String> map) {
        StringBuilder sb = new StringBuilder();
        Iterator<Map.Entry<String, String>> iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, String> entry = iter.next();
            sb.append(entry.getKey());
            sb.append('=').append('"');
            sb.append(entry.getValue());
            sb.append('"');
            if (iter.hasNext()) {
                sb.append(',').append('\n');
            }
        }
        return sb.toString();

    }
}

