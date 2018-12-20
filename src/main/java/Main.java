import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    static final String FILE_PATH = "/home/pedro/Documentos/semanticscholar/src/main/resources/sample-S2-records";

    public static void main(String[] argv) throws IOException {
        HashMap<String, String> article = new HashMap<String, String>();

        InputStream inputstream = new FileInputStream(FILE_PATH);
        JsonReader reader = new JsonReader(new InputStreamReader(inputstream, "UTF-8"));

        String name = "";

        reader.setLenient(true);

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
                    System.out.println(reader.getPath().split("\\.").length);
                    if(reader.getPath().split("\\.").length<3) {
                        processMap(article);
                        article = new HashMap<String, String>();
                    }
                    reader.endObject();
                    break;
                case NAME:
                    name = reader.nextName();
                    break;
                case STRING:
                    s = reader.nextString();
                    if(!name.equals(""))
                        article.put(reader.getPath().replace("$.",""), s);
                    break;
                case NUMBER:
                    s = reader.nextString();
                    if(!name.equals(""))
                        article.put(reader.getPath().replace("$.",""), s);
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

    private static void processMap(HashMap<String, String> article) {
        Node subject = NodeFactory.createURI(article.get("s2Url"));
        for(Map.Entry<String, String> entry : article.entrySet()) {
            List<Mapping> mappings = Article.getMapping(entry.getKey());
            for (Mapping map : mappings) {
                Node predicate = NodeFactory.createURI(map.getPropertyUri());
                Node object = null;
                switch (map.getType()){
                    case STRING:
                        object = NodeFactory.createLiteral(entry.getValue());
                        break;
                    case URI:
                        object = NodeFactory.createURI(entry.getValue());
                        break;
                    case CUSTOM:
                        object = customObject(entry);
                        break;
                }
                Triple t = new Triple(subject, predicate, object);
            }
        }
    }

    private static Node customObject(Map.Entry<String, String> map) {
        Node object = null;

        return object;
    }
}
