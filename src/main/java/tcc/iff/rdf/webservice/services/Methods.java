package tcc.iff.rdf.webservice.services;

public class Methods {
	
    public final static String URIBASE = "http://localhost:8080/webservice/webapi";


	public String convertFromAcceptToFormat(String accept) {
		String format = "";
		switch(accept) {
		case "application/ld+json":
			format = "JSON-LD";
			break;
		case "application/n-triples":
			format = "N-Triples";
			break;
		case "application/rdf+xml":
			format = "RDF/XML";
			break;
		case "application/turtle":
			format = "TURTLE";
			break;
		case "application/rdf+json":
			format = "RDF/JSON";
			break;
		}
		return format;
	}
	
	public boolean isValidFormat(String accept) {
		if (accept.equals("application/json") || accept.equals("application/ld+json") || accept.equals("application/n-triples") || accept.equals("application/rdf+xml") || accept.equals("application/turtle") || accept.equals("application/rdf+json") ) 
			return true;
			
		else
			return false;
	}
	
	public String getProductTypeSparqlSelect(String productTypeID) {
		String query = "PREFIX exp: <http://localhost:8080/webservice/webapi/producttypes/>\r\n" + 
				"PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n" + 
				"PREFIX lang: <http://www.w3.org/XML/1998/>\r\n" + 
				"PREFIX foaf: <http://xmlns.com/foaf/0.1/>\r\n" + 
				"PREFIX pto: <http://www.productontology.org/id/>\r\n" + 
				"\r\n" + 
				"SELECT ?label ?homepage ?language ?description ?subClassOf\r\n" + 
				"\r\n" + 
				"WHERE{ \r\n" + 
				"  \r\n" + 
				"  OPTIONAL{\r\n" + 
				"  	exp:"+productTypeID+" 	  rdfs:label	?label;\r\n" + 
				"           		  foaf:homepage		?homepage;\r\n" + 
				"          	   	  lang:namespacelang	  ?language;\r\n" + 
				"          	      gr:description	?description .\r\n" + 
				"    \r\n" + 
				" 			OPTIONAL {  exp:"+productTypeID+" rdfs:subClassOf	?subClassOf . FILTER regex(str(?subClassOf), 'http://www.productontology.org/id/')}\r\n" + 
				" 			OPTIONAL {  exp:"+productTypeID+" rdfs:subClassOf	?subClassOf . FILTER regex(str(?subClassOf), '/producttypes/')}\r\n" + 
				" 			OPTIONAL {  exp:"+productTypeID+" rdfs:subClassOf	?subClassOf . FILTER regex(str(?subClassOf), '/goodrelations/')}\r\n" + 
				"  \r\n" + 
				" }\r\n" + 
				" \r\n" + 
				"   OPTIONAL {\r\n" + 
				"    pto:"+productTypeID+"	  rdfs:label	?label;\r\n" + 
				"           		  foaf:homepage		?homepage;\r\n" + 
				"          	   	  lang:namespacelang	  ?language;\r\n" + 
				"          	      rdfs:label		?description .\r\n" + 
				"          \r\n" + 
				"  		OPTIONAL {  pto:"+productTypeID+" rdfs:subClassOf	?subClassOf . FILTER regex(str(?subClassOf), '/goodrelations/')}\r\n" + 
				"}\r\n" + 
				"   \r\n" + 
				"}";
		return query;
	}
	
	public String getProductTypeSparqlDescribe(String productTypeID) {
		String query = "PREFIX exp: <http://localhost:8080/webservice/webapi/producttypes/> \r\n" + 
				"PREFIX pto: <http://www.productontology.org/id/>\r\n" + 
				"\r\n" + 
				"DESCRIBE exp:"+productTypeID+" pto:"+productTypeID+"\r\n" + 
				"WHERE\r\n" + 
				"{\r\n" + 
				"OPTIONAL { exp:"+productTypeID+" ?p ?o . }\r\n"+
				"OPTIONAL { pto:"+productTypeID+" ?p ?o . }\r\n"+
				"}";
		return query;
	}
	
	public String getAllProductTypesSparqlSelect() {
		String query = "PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
				"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n" + 
				"PREFIX owl: <http://www.w3.org/2002/07/owl#>\r\n" + 
				"\r\n" + 
				"SELECT ?ProductTypes \r\n" + 
				"WHERE {?ProductTypes   	 rdf:type			owl:Class;\r\n" + 
				"			 rdfs:subClassOf		<http://schema.org/Product> .}";
		return query;
	}
}

