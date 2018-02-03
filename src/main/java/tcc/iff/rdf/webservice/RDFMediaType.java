package tcc.iff.rdf.webservice;

import javax.ws.rs.core.MediaType;

public class RDFMediaType {

	public final static String TEXT_N3 = "text/rdf+n3";
    public final static MediaType TEXT_N3_TYPE = new MediaType("text","rdf+n3");

	public final static String APPLICATION_N3 = "application/n3";
    public final static MediaType APPLICATION_N3_TYPE = new MediaType("application","n3");

    public final static String APPLICATION_TURTLE_CURRENT = "application/x-turtle";
    public final static MediaType APPLICATION_TURTLE_TYPE_CURRENT = new MediaType("application","x-turtle");
    
    public final static String APPLICATION_TURTLE_IDEAL = "application/turtle";
    public final static MediaType APPLICATION_TURTLE_TYPE_IDEAL = new MediaType("application","turtle");

    public final static String APPLICATION_RDFXML = "application/rdf+xml";
    public final static MediaType APPLICATION_RDFXML_TYPE = new MediaType("application","rdf+xml");
    
    public final static String TEXT_NTRIPLES = "text/plain";
    public final static MediaType TEXT_NTRIPLES_TYPE = new MediaType("text","plain");

    public final static String APPLICATION_NTRIPLES = "application/n-triples";
    public final static MediaType APPLICATION_NTRIPLES_TYPE = new MediaType("application","n-triples");

    public final static String APPLICATION_SPARQL_RESULTS_XML = "application/sparql-result+xml";
    public final static MediaType APPLICATION_SPARQL_RESULTS_XML_TYPE = new MediaType("application","sparql-result+xml");

    public final static String APPLICATION_SPARQL_RESULTS_JSON = "application/sparql-result+json";
    public final static MediaType APPLICATION_SPARQL_RESULTS_JSON_TYPE = new MediaType("application","sparql-result+json");

    public final static String APPLICATION_SPARQL_QUERY_X = "application/x-sparql-query";
    public final static MediaType APPLICATION_SPARQL_QUERY_X_TYPE = new MediaType("application","x-sparql-query");

    public final static String APPLICATION_SPARQL_QUERY = "application/sparql-query";
    public final static MediaType APPLICATION_SPARQL_QUERY_TYPE = new MediaType("application","sparql-query");

    public final static String APPLICATION_SPARQL_UPDATE_X = "application/x-sparql-update";
    public final static MediaType APPLICATION_SPARQL_UPDATE_X_TYPE = new MediaType("application","x-sparql-update");

    public final static String APPLICATION_SPARQL_UPDATE = "application/sparql-update";
    public final static MediaType APPLICATION_SPARQL_UPDATE_TYPE = new MediaType("application","sparql-update");
    
    public final static String APPLICATION_JSON_LD = "application/ld+json";
    public final static MediaType APPLICATION_JSON_LD_TYPE = new MediaType("application","ld+json");
    
}