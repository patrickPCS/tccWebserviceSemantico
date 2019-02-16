package tcc.iff.rdf.webservice.services;

public class Methods {
	
    public final static String URIBASE = "http://localhost:8080/webservice/webapi";
    public final static String SPARQL_ENDPOINT = "http://localhost:10035/catalogs/CatalogoGR/repositories/RepositorioGR/sparql";


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
	
	public String getProducttypeSparqlDescribe(String productTypeID) {
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
	
	public String getProducttypeSparqlDescribeExp(String id) {
		String queryDescribe = "PREFIX exp: <http://localhost:8080/webservice/webapi/producttypes/> \r\n" + 

							"\r\n" + 
							"DESCRIBE exp:"+id+"\r\n" + 
							"WHERE\r\n" + 
							"{\r\n" + 
							"exp:"+id+" ?p ?o \r\n"+
							"}";
		
		return queryDescribe;
	}
	
	public String getProducttypeSparqlDescribePto(String id) {
		String queryDescribe = "PREFIX pto: <http://www.productontology.org/id/>\r\n" + 
				"\r\n" + 
				"DESCRIBE pto:"+id+"\r\n" + 
				"WHERE\r\n" + 
				"{\r\n" + 
				"pto:"+id+" ?p ?o .\r\n" + 
				"}";
		
		return queryDescribe;
	}
	
	public String getOfferSparqlDescribe(String companyID, String productID) {
		String queryDescribe = "DESCRIBE <http://localhost:8080/webservice/webapi/companies/"+companyID+"/offers/"+productID+">\r\n";
		
		
		
		return queryDescribe;
	}
	
	public String getCompanySparqlDescribe(String companyID) {
		String query = "PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
				"PREFIX schema: <http://schema.org/>\r\n" + 
				"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" +
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n" +
				"PREFIX exco: <http://localhost:8080/webservice/webapi/companies/>\r\n"+
				"\r\n" + 
				"DESCRIBE exco:"+companyID+"\r\n" + 
				"WHERE\r\n" + 
				"{\r\n" + 
				"  exco:"+companyID+"	rdf:type			schema:identifier,\r\n" + 
				"									gr:BusinessEntity;\r\n"+
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

	public String getCompanySparqlSelect(String companyID) {
		String query = "PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
				"PREFIX schema: <http://schema.org/>\r\n"+ 
				"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n"+ 
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n" + 
				"PREFIX exco: <http://localhost:8080/webservice/webapi/companies/>\r\n" + 
				"\r\n" + 
				"SELECT ?companyURL ?legalName ?grLegalName ?email ?name ?label\r\n" + 
				"\r\n" + 
				"WHERE{\r\n" + 
				"\r\n" + 
				"  exco:"+companyID+"	rdf:type			schema:identifier,\r\n"+ 
				"									gr:BusinessEntity;\r\n"+ 
				//"						schema:name			?name;			\r\n"+ 
				"						rdfs:label   		?label; 		\r\n"+
				"						schema:legalName	?legalName;	\r\n"+
				"						gr:legalName		?grLegalName;	\r\n"+
				"						schema:url			?companyURL;	\r\n"+ 
				"						schema:email		?email.		\r\n"+
				"  \r\n" + 
				"}\r\n" + 
				"";
		return query;
	}
	
	public String insertCompanySparql(String companyID, String name, String legalName, String grLegalName, String companyURL, String email) {
		String queryInsert = "PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
				"PREFIX schema: <http://schema.org/>\r\n" + 
				"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" +
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n" +
				"PREFIX exco: <http://localhost:8080/webservice/webapi/companies/>\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"INSERT DATA\r\n" + 
				"{ \r\n" + 
				"  exco:"+companyID+" 	rdf:type			schema:identifier,\r\n"+
				"									gr:BusinessEntity;\r\n"+
				//"						schema:name			'"+name+"';\r\n"+ 
				"						rdfs:label   		'"+name+"';\r\n"+
				"						schema:legalName	'"+legalName+"';\r\n"+
				"						gr:legalName		'"+grLegalName+"';\r\n"+
				"						schema:url			"+companyURL+";\r\n"+ 
				"						schema:email		'"+email+"'.\r\n"+  
				"}";
		
		return queryInsert;
	}
	
	public String updateCompanySparql(String oldCompanyID, String companyID, String companyURL, String email, String name, String legalName) {
		String queryUpdate = "PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
				"PREFIX schema: <http://schema.org/>\r\n" + 
				"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" +
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n" +
				"PREFIX exco: <http://localhost:8080/webservice/webapi/companies/>\r\n" + 
				"\r\n" + 
				"DELETE\r\n" + 
				"{ exco:"+oldCompanyID+" ?p ?s }\r\n" + 
				"WHERE\r\n" +
				"{\r\n" + 
				"exco:"+oldCompanyID+" ?p ?s;\r\n" + 
				"rdf:type  gr:BusinessEntity .\r\n" + 
				"};"+
				"\r\n" + 
				"INSERT DATA\r\n" + 
				"{ \r\n" + 
				"  exco:"+companyID+" 	rdf:type	schema:identifier,\r\n"+ 
				"								gr:BusinessEntity;\r\n" +
				"						schema:name			<"+name+">;			\r\n"+ 
				"						rdfs:label   		<"+name+">; 		\r\n"+
				"						schema:legalName	<"+legalName+">;	\r\n"+
				"						gr:legalName		<"+legalName+">;	\r\n"+
				"						schema:url			<"+companyURL+">;	\r\n"+ 
				"						schema:email		<"+email+">.		\r\n"+
				"}";
		return queryUpdate;
				
	}
	
	public String deleteAllOffers() {
		String deleteQuery = "PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
				"PREFIX exco:<http://localhost:8080/webservice/webapi/companies/> \r\n" + 
				"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
				"\r\n" + 
				"DELETE {?company	gr:offers     ?Offers }\r\n" + 
				"WHERE\r\n" + 
				"{\r\n" + 
				"?company	gr:offers     ?Offers    .\r\n" +
				"};\r\n" + 
				
				"\r\n" + 
				"DELETE {?Offer	?p     ?o }\r\n" + 
				"WHERE\r\n" + 
				"{\r\n" + 
				"?Offer	?p     ?o;"
				+ "		gr:BusinessEntity	?company .\r\n" +
				"}\r\n" + 
				"";
		
		return deleteQuery;
	}
	
	public String deleteCompanySparql(String companyID) {
		String queryDelete = "PREFIX exco: <http://localhost:8080/webservice/webapi/companies/>\r\n" + 
				"PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
				"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
				"\r\n" + 
				"DELETE \r\n" + 
				"	{ exco:"+companyID+" ?p ?s }\r\n" + 
				"WHERE\r\n" + 
				"{ \r\n" + 
				"  exco:"+companyID+" ?p ?s; .\r\n" + 
				"}\r\n" + 
				"";
		
		return queryDelete;
	}
	
	public String deleteProducttypeSparql(String id) {
		String queryDelete = "PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
				"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n" + 
				"PREFIX lang: <http://www.w3.org/XML/1998/>\r\n" + 
				"PREFIX exp: <http://localhost:8080/webservice/webapi/producttypes/>\r\n" +
				"PREFIX foaf: <http://xmlns.com/foaf/0.1/> \r\n" + 
				"PREFIX owl: <http://www.w3.org/2002/07/owl#>\r\n" + 
				"\r\n" + 
				"DELETE \r\n" + 
				"	{ exp:"+id+" ?p ?s }\r\n" + 
				"WHERE\r\n" + 
				"{ \r\n" + 
				"  exp:"+id+"	  	?p ?s;" +
				" rdf:type			owl:Class;\r\n" + 
				"			 rdfs:subClassOf		<http://schema.org/Product> .\r\n" + 
				"}";
		
		return queryDelete;
	}
	
	public String deleteAllProducttypes() {
		String queryDelete = "PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
				"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n" + 
				"PREFIX lang: <http://www.w3.org/XML/1998/>\r\n" + 
				"PREFIX exp: <http://localhost:8080/webservice/webapi/producttypes/>\r\n" +
				"PREFIX foaf: <http://xmlns.com/foaf/0.1/> \r\n" + 
				"PREFIX owl: <http://www.w3.org/2002/07/owl#>\r\n" + 
				"\r\n" + 
				"DELETE \r\n" + 
				"	{ ?productType ?p ?s }\r\n" + 
				"WHERE\r\n" + 
				"{ \r\n" + 
				"  ?productType	  	 ?p			?s;" + 
				"			 rdf:type			owl:Class;\r\n" + 
				"			 rdfs:subClassOf		<http://schema.org/Product> .\r\n" + 
				"}";
		
		return queryDelete;
	}
	
	public String deleteAllCompaniesSparql() {
		String queryDeleteAll = "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
				"PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
				"\r\n" + 
				"DELETE\r\n" + 
				"{ ?companyURL ?p ?s }\r\n" + 
				"WHERE\r\n" + 
				"{\r\n" + 
				"?companyURL ?p ?s;\r\n" + 
				"rdf:type  gr:BusinessEntity .\r\n" + 
				"}";
		
		return queryDeleteAll;
	}
	
	public String getAllCompaniesSparqlSelect() {
		String querySelect = "PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
				"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
				"\r\n" + 
				"SELECT ?company\r\n" + 
				"WHERE\r\n" + 
				"{\r\n" + 
				"?company rdf:type gr:BusinessEntity .\r\n" + 
				"}\r\n" + 
				"";
		
		return querySelect;
	}
	
	public String getAllOffersSparqlSelect(String companyID) {
		String querySelect = "PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
				"PREFIX exco:<http://localhost:8080/webservice/webapi/companies/> \r\n" + 
				"\r\n" + 
				"SELECT ?Offers\r\n" + 
				"WHERE\r\n" + 
				"{\r\n" + 
				"exco:"+companyID+"	gr:offers     ?Offers    .\r\n" + 
				"}\r\n" + 
				"";
		return querySelect;
	}
	
	public String insertOfferSparql(String companyID, String productID, String offerURI, String validFrom, String validThrough, String hasCurrency, String price) {
		String queryInsert = "PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
				"PREFIX exo: <http://localhost:8080/webservice/webapi/companies/"+companyID+"/offers/>\r\n" + 
				"PREFIX exco: <http://localhost:8080/webservice/webapi/companies/>\r\n" + 
				"PREFIX exp: <http://localhost:8080/webservice/webapi/producttypes/>\r\n" + 
				"PREFIX foaf: <http://xmlns.com/foaf/0.1/> \r\n" + 
				"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>  \r\n" + 
				"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
				"\r\n" + 
				"INSERT DATA\r\n" + 
				"{ \r\n" + 
				"  exo:"+productID+"	rdf:type		gr:Offering;\r\n" + 
				"                        gr:includes      exp:"+productID+";\r\n" + 
				"	gr:BusinessEntity     exco:"+companyID+";\r\n" + 
				"	foaf:page     <"+offerURI+">;\r\n" + 
				"	gr:hasBusinessFunction gr:Sell ;\r\n" + 
				"	gr:validFrom '"+validFrom+"'^^xsd:dateTime ;\r\n" + 
				"	gr:validThrough '"+validThrough+"'^^xsd:dateTime ;\r\n" + 
				"	gr:hasPriceSpecification\r\n" + 
				"         [ a gr:UnitPriceSpecification ;\r\n" + 
				"           gr:hasCurrency '"+hasCurrency+"'^^xsd:string ;\r\n" + 
				"           gr:hasCurrencyValue '"+price+"'^^xsd:float;\r\n" + 
				"           gr:validThrough '"+validThrough+"'^^xsd:dateTime ] .   \r\n" +
				"  exco:"+companyID+"	gr:offers		exo:"+productID+" .\r\n" + 
				"}";
		return queryInsert;
	}
		
	public String updateOfferSparql(String oldOfferID, String companyID, String productID, String offerURI, String validFrom, String validThrough, String hasCurrency, String price) {
		String queryUpdate = "PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
				"PREFIX exo: <http://localhost:8080/webservice/webapi/companies/"+companyID+"/offers/>\r\n" + 
				"PREFIX exp: <http://localhost:8080/webservice/webapi/products/>\r\n" + 
				"PREFIX foaf: <http://xmlns.com/foaf/0.1/> \r\n" + 
				"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>  \r\n" + 
				"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
				"PREFIX exco: <http://localhost:8080/webservice/webapi/companies/>\r\n" + 
				"\r\n" + 
				"DELETE \r\n" + 
				"	{ exo:"+oldOfferID+" ?p ?s }\r\n" + 
				"WHERE\r\n" + 
				"{ \r\n" + 
				"  exo:"+oldOfferID+" ?p ?s;\r\n" + 
				" 		rdf:type gr:Offering .\r\n" + 
				"};\r\n" + 
				"\r\n" + 
				"INSERT DATA\r\n" + 
				"{ \r\n" + 
				"  exo:"+productID+"	rdf:type		gr:Offering;\r\n" + 
				"                        gr:includes      exp:"+productID+";\r\n" + 
				"	gr:BusinessEntity     exco:"+companyID+";\r\n" + 
				"	foaf:page     <"+offerURI+">;\r\n" + 
				"	gr:hasBusinessFunction gr:Sell ;\r\n" + 
				"	gr:validFrom '"+validFrom+"'^^xsd:dateTime ;\r\n" + 
				"	gr:validThrough '"+validThrough+"'^^xsd:dateTime ;\r\n" + 
				"	gr:hasPriceSpecification\r\n" + 
				"         [ rdf:type gr:UnitPriceSpecification ;\r\n" + 
				"           gr:hasCurrency '"+hasCurrency+"'^^xsd:string ;\r\n" + 
				"           gr:hasCurrencyValue '"+price+"'^^xsd:float ;\r\n" + 
				"           gr:validThrough '"+validThrough+"'^^xsd:dateTime ] .   \r\n" +
				"  exco:"+companyID+"	gr:offers		exo:"+productID+" .\r\n" + 
				"}";
		
		return queryUpdate;
	}
	
	public String deleteOfferSparql(String companyID, String offerID) {
		String queryDelete = "PREFIX exo: <http://localhost:8080/webservice/webapi/companies/"+companyID+"/offers/>\r\n" + 
				"PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
				"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
				"PREFIX exco:<http://localhost:8080/webservice/webapi/companies/> \r\n" + 
				"\r\n" + 
				"DELETE \r\n" + 
				"	{ exo:"+offerID+" ?p ?s }\r\n" + 
				"WHERE\r\n" + 
				"{ \r\n" + 
				"  exo:"+offerID+" ?p ?s;\r\n" +
				" 		rdf:type gr:Offering .\r\n" + 
				"};\r\n" + 
				"\r\n" + 
				"DELETE {exco:"+companyID+"	gr:offers     exo:"+offerID+" }\r\n" + 
				"WHERE\r\n" + 
				"{\r\n" + 
				"exco:"+companyID+"	gr:offers     exo:"+offerID+"    .\r\n" +
				"}";
		
		return queryDelete;
	}
	
	public String deleteAllOffersSparql(String companyID) {
		String queryDelete = "PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
				"PREFIX exco:<http://localhost:8080/webservice/webapi/companies/> \r\n" + 
				"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
				"PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
				"\r\n" + 
				"DELETE {exco:"+companyID+"	gr:offers     ?Offers }\r\n" + 
				"WHERE\r\n" + 
				"{\r\n" + 
				"exco:"+companyID+"	gr:offers     ?Offers    .\r\n" +
				"};\r\n" + 
				"DELETE \r\n" + 
				"	{ ?offers ?p ?o }\r\n" + 
				"WHERE\r\n" + 
				"{ \r\n" + 
				"?offers ?p ?o;\r\n" +
				"rdf:type gr:Offering;\r\n" +
				"		gr:BusinessEntity		exco:"+companyID+".\r\n"+
				"}";
		
		return queryDelete;
	}
	
	public String insertProducttypeSparql(String id, String superClassURI, String label, String homepage, String description, String language) {
		String queryInsert = "PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
				"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n" + 
				"PREFIX pto: <http://www.productontology.org/id/>\r\n" + 
				"PREFIX lang: <http://www.w3.org/XML/1998/>\r\n" + 
				"PREFIX exp: <http://localhost:8080/webservice/webapi/producttypes/>\r\n" +
				"PREFIX foaf: <http://xmlns.com/foaf/0.1/> \r\n" + 
				"PREFIX owl: <http://www.w3.org/2002/07/owl#>\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"INSERT DATA\r\n" + 
				"{ \r\n" + 
				"  exp:"+id+"	  	 rdf:type			owl:Class;\r\n" + 
				"			 rdfs:subClassOf		"+superClassURI+";\r\n" + 
				"			 rdfs:subClassOf		<http://schema.org/Product>;\r\n" + 
				"			 rdf:type			<http://www.productontology.org/>;\r\n" + 
				"			 rdfs:label			'"+label+"';\r\n" + 
				"			 foaf:homepage			<"+homepage+">;\r\n" + 
				"			 gr:description			'"+description+"';\r\n" + 
				"			 lang:namespacelang		'"+language+"' .\r\n" + 
				"			\r\n" + 
				"}		\r\n" + 
				"";
		
		return queryInsert;
	}
	
	public String getOffersToProductsSparql(String productID) {
	/*String queryConstruct = "PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
				"PREFIX exp: <http://localhost:8080/webservice/webapi/producttypes/>\r\n" +
				"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" +
				"PREFIX owl: <http://www.w3.org/2002/07/owl#>\r\n" + 
				"\r\n" + 
				/*"SELECT ?Companies	?Prices \r\n" + 
				"WHERE { ?Companies 			gr:includes		exp:"+productID+";\r\n" + 
				"      						gr:hasPriceSpecification\r\n" + 
				"				        [rdf:type gr:UnitPriceSpecification ;\r\n" + 
				"				           gr:hasCurrencyValue		?Prices ]}\r\n" + 
				"ORDER BY(?Prices)";
					
				"CONSTRUCT WHERE{ ?Companies 	gr:includes		exp:"+productID+";\r\n"+
				"								gr:hasPriceSpecification [rdf:type gr:UnitPriceSpecification;\r\n"+
				"														gr:hasCurrencyValue ?Prices]}\r\n"+
				"ORDER BY(?Prices)";
		return queryConstruct;*/
		String queryDescribe = 
				"PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
				"PREFIX exp: <http://localhost:8080/webservice/webapi/producttypes/>\r\n" +
				"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" +
				"PREFIX owl: <http://www.w3.org/2002/07/owl#>\r\n" + 
						"\r\n" + 
				"DESCRIBE exp:"+productID+" WHERE { ?Companies gr:includes exp:"+productID+";\n" + 
				"                                         gr:hasPriceSpecification [rdf:type gr:UnitPriceSpecification;\n" + 
				"                                                                  gr:hasCurrencyValue ?price]}\n" + 
				"\n" + 
				"ORDER BY ?prices";
		
		return queryDescribe;
	}
	
	public String getAllOffersSparqlSelect() {
		String querySelect = "PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
				"PREFIX exco:<http://localhost:8080/webservice/webapi/companies/> \r\n" + 
				"\r\n" + 
				"SELECT ?Offers\r\n" + 
				"WHERE\r\n" + 
				"{\r\n" + 
				"?company	gr:offers     ?Offers    .\r\n" + 
				"}\r\n" + 
				"";
		
		return querySelect;
	}

	public String getOfferSparqlSelect(String companyID, String offerID) {
		String querySelect = "PREFIX exo: <http://localhost:8080/webservice/webapi/companies/"+companyID+"/offers/>\r\n" + 
				"PREFIX exco: <http://localhost:8080/webservice/webapi/companies/>\r\n" + 
				"PREFIX exp: <http://localhost:8080/webservice/webapi/producttypes/>\r\n" + 
				"PREFIX foaf: <http://xmlns.com/foaf/0.1/>\r\n" + 
				"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\r\n" + 
				"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
				"PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
				"\r\n" + 
				"SELECT ?offerURI ?validFrom ?validThrough ?hasCurrency ?price ?companyID\r\n" + 
				"\r\n" + 
				"WHERE\r\n" + 
				"{ \r\n" + 
				"  exo:"+offerID+" rdf:type gr:Offering;\r\n" + 
				"         foaf:page	?offerURI;\r\n" + 
		//		"         gr:validFrom		?validFrom;\r\n" + 
				"         gr:BusinessEntity	?companyID .\r\n" + 
			//	"         gr:validThrough		?validThrough .\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"  \r\n" + 
				"  exo:"+offerID+"	gr:hasPriceSpecification	[] .\r\n" + 
				"  []	gr:hasCurrency	?hasCurrency .\r\n" + 
		//		"        gr:hasCurrencyValue	?price .\r\n" + 
				" \r\n" + 
				"}\r\n" + 
				"";

		return querySelect;
	}

	
}

