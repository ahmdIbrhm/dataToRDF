package eu.qanswer.mapping.mappings.eu.h2020;

import eu.qanswer.mapping.configuration.CSVConfiguration;
import eu.qanswer.mapping.configuration.Mapping;
import eu.qanswer.mapping.configuration.Type;

import java.util.ArrayList;
import java.util.Arrays;

public class H2020 extends CSVConfiguration {

    public H2020(){
        format = "csv";
        separator = ';';
        file="/Users/Dennis/Downloads/h2020_topics_with_budget_and_links.csv";
        baseUrl = "http://europa.eu/";
        key = "Topic";
        mappings = new ArrayList<>(Arrays.asList(
                new Mapping("Topic","http://www.w3.org/2000/01/rdf-schema#label",Type.LITERAL),
                new Mapping("Topic Desc","http://schema.org/description", Type.LITERAL),
                new Mapping("H2020 Signed Grants","http://www.wikidata.org/prop/direct/P800" ,Type.LITERAL),
                new Mapping("H2020 Net EU Contribution","http://www.wikidata.org/prop/direct/P2769", Type.LITERAL),
                new Mapping("Topic description","http://www.wikidata.org/prop/direct/P856", Type.LITERAL)
        ));
    }
}
