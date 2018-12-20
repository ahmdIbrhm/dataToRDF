import java.util.ArrayList;
import java.util.Arrays;

public class Article {
    public static final ArrayList<Mapping> mappings = new ArrayList<Mapping>(Arrays.asList(
            new Mapping("id", "", Type.STRING),

            new Mapping("title", "http://purl.org/dc/elements/1.1/title", Type.URI),
            new Mapping("title", "http://www.w3.org/2000/01/rdf-schema#label", Type.STRING),

            new Mapping("paperAbstract","",Type.STRING),

            new Mapping("entities", "", Type.LIST),

            new Mapping("s2Url", "", Type.URI),

            new Mapping("s2PdfUrl", "", Type.URI),

            

    ));
}
