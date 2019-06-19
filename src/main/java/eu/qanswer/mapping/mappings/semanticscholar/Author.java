package eu.qanswer.mapping.mappings.semanticscholar;

import eu.qanswer.mapping.configuration.AbstractConfiguration;
import eu.qanswer.mapping.configuration.Mapping;
import eu.qanswer.mapping.configuration.Type;

import java.util.ArrayList;
import java.util.Arrays;

public class Author extends AbstractConfiguration {
    public Author()
    {
        format = "json";
        file="sample-S2-records";
        baseUrl = "http://semanticscholar.org/paper/";;
        key = "publications.authors.id";
        iterator = "publications.authors";
        mappings = new ArrayList<Mapping>(Arrays.asList(
                new Mapping("name","http://www.w3.org/2000/01/rdf-schema#label", Type.LITERAL),
                new Mapping("name","http://www.wikidata.org/prop/direct/P2561", Type.LITERAL),
                new Mapping("name","http://www.wikidata.org/prop/direct/P4012", Type.LITERAL)));

    }
}
