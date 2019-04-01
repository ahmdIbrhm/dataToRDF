package eu.qanswer.mapping;

import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.vocabulary.RDF;

public class Mapping {
    private String tag;
    private String object;
    private String propertyUri;
    private String baseurl;
    private Type type;

    private RDFDatatype datatype;

    public Mapping(String tag, String propertyUri, String objectUri){
        this.tag = tag;
        this.propertyUri = propertyUri;
        this.object = objectUri;
    }

    public Mapping(String tag, String propertyUri, String baseurl, Type type){
        this.tag = tag;

        this.propertyUri = propertyUri;
        this.baseurl = baseurl;
        this.type = type;
    }

    public Mapping(String tag, String propertyUri, Type type){
        this.tag = tag;
        this.propertyUri = propertyUri;
        this.type = type;
    }

    public Mapping(String tag, String propertyUri, Type type, RDFDatatype datatype){
        this.tag = tag;
        this.propertyUri = propertyUri;
        this.type = type;
        this.datatype = datatype;
    }

    public String getBaseurl() {
        return baseurl;
    }


    public String getTag() {
        return tag;
    }

    public String getPropertyUri() {
        return propertyUri;
    }

    public Type getType() {
        return type;
    }

    public RDFDatatype getDatatype() {
        return datatype;
    }

    public void setDatatype(RDFDatatype datatype) {
        this.datatype = datatype;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }
}
