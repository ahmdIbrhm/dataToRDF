import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.riot.RDFFormat;
import org.apache.jena.riot.system.StreamRDF;
import org.apache.jena.riot.system.StreamRDFWriter;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
//
//    static final String FILE_PATH = "/home/pedro/Documentos/semanticscholar/src/main/resources/sample-S2-records";
    static final String FILE_PATH = "/home/migliatti/semanticscholar/s2-corpus";
    static final String OUTPUT_PATH = "/home/migliatti/semanticscholar/semanticscholar/src/main/resources/output.rdf";

    public static void main(String[] argv) throws IOException {
        StreamRDF writer = StreamRDFWriter.getWriterStream(new FileOutputStream(OUTPUT_PATH), RDFFormat.NTRIPLES);

        HashMap<String, String> article = new HashMap<String, String>();

        InputStream inputstream = new FileInputStream(FILE_PATH);
        JsonReader reader = new JsonReader(new InputStreamReader(inputstream, "UTF-8"));

        String name = "";

        reader.setLenient(true);

        writer.start();
        while (true) {
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
                    if (reader.getPath().split("\\.").length < 3) {
                        processMap(article, writer);
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
                    return;
            }
        }
    }

    private static void processMap(HashMap<String, String> article, StreamRDF writer) {
        Node subject = NodeFactory.createURI(article.get("s2Url"));
        for (Map.Entry<String, String> entry : article.entrySet()) {
            String key = entry.getKey().replaceAll("[0-9]", "").replace("[", "").replace("]", "");
            List<Mapping> mappings = Article.getMapping(key);
            for (Mapping map : mappings) {
                Node predicate = NodeFactory.createURI(map.getPropertyUri());
                Node object = null;
                Triple t;
                switch (map.getType()) {
                    case STRING:
                        object = NodeFactory.createLiteral(entry.getValue());
                        break;
                    case URI:
                        object = NodeFactory.createURI(entry.getValue());
                        break;
                }
                if (object != null && !object.isBlank()) {
                    t = new Triple(subject, predicate, object);
                    writer.triple(t);
                }
            }
        }
    }
}
