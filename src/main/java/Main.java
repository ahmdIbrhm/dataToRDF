
import com.hp.hpl.jena.sparql.lib.org.json.JSONArray;
import com.hp.hpl.jena.sparql.lib.org.json.JSONException;
import com.hp.hpl.jena.sparql.lib.org.json.JSONObject;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;

public class Main {

    static final String FILE_PATH = "/home/pedro/Documentos/semanticscholar/src/main/resources/sample-S2-records";

    public static void main(String[] argv) throws JSONException, IOException, ParseException {
        String file = readFile(FILE_PATH);

        JSONArray jarr = new JSONArray(file);

        String subject, predicate, object;

        for(int i = 0; i<jarr.length(); i++){
            JSONObject obj = jarr.getJSONObject(i);


        }
    }

    public static String readFile(String filename) {
        String result = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line + ",");
                line = br.readLine();
            }
            result = sb.toString();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return  "[" + result + "]";
    }
}
