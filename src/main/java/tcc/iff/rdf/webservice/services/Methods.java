package tcc.iff.rdf.webservice.services;

import java.util.Date;

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
	
	public String insertProducttypeSparql(String id, String superClassURI, String label,  String name, String homepage, String description, String comment) {
		String queryInsert = "PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" +
				"PREFIX schema: <http://schema.org/>\r\n" +
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
				"  exp:"+id+"	rdf:type			owl:Class,\r\n" +
				"									schema:identifier,\r\n"+
				"							<http://www.productontology.org/>;\r\n" +
				"				rdfs:subClassOf		gr:ProductOrService, \r\n" +
				" 									schema:Product; \r\n" +
				"				rdfs:subClassOf		"+superClassURI+";\r\n" +  
				"				rdfs:label			'"+label+"';\r\n" +
				//"				schema:name			'"+name+"';\r\n"+
				"				schema:homepage		<"+homepage+">;\r\n" + 
				"				gr:description		'"+description+"';\r\n" +
				"				rdfs:comment		'"+comment+"';\r\n"+				 
				"			\r\n" + 
				"}		\r\n" + 
				"";
		
		return queryInsert;
	}
	
	public String getProductTypeSparqlSelect(String productTypeID) {
		String query = "PREFIX exp: <http://localhost:8080/webservice/webapi/producttypes/>\r\n" + 
				"PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" +
				"PREFIX schema: <http://schema.org/>\r\n" +
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n" + 
				"PREFIX lang: <http://www.w3.org/XML/1998/>\r\n" + 
				"PREFIX foaf: <http://xmlns.com/foaf/0.1/>\r\n" + 
				"PREFIX pto: <http://www.productontology.org/id/>\r\n" + 
				"\r\n" + 
				"SELECT ?subClassOf ?label ?name ?homepage ?description ?comment ?language\r\n" + 
				"\r\n" + 
				"WHERE{ \r\n" + 
				"  \r\n" + 
				"  OPTIONAL{\r\n" + 
				"  	exp:"+productTypeID+"		rdfs:label		?label;	\r\n" +
				//"								schema:name		?name;	\r\n"+
				"								schema:homepage	?homepage;	\r\n" + 
				"								gr:description	?description;	\r\n" +
				"								rdfs:comment	?comment;	\r\n"+ 
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
	public String getProductDescribe(String productID) {
		String queryDescribe = "PREFIX expr: <http://localhost:8080/webservice/webapi/products/>\r\n"+
							"\r\n"+
							"DESCRIBE expr:"+productID+"\r\n"+
							"WHERE\r\n"+
							"	{\r\n"+
							"		expr:"+productID+"	?p	?o .\r\n "+
							"}";
		return queryDescribe;
	}
	public String getProductSparqlSelect(String productID) {
		String query = "PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
				"PREFIX schema: <http://schema.org/>\r\n" + 
				"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" +
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n" +
				"PREFIX lang: <http://www.w3.org/XML/1998/>\r\n" +
				"PREFIX expr: <http://localhost:8080/webservice/webapi/products/>\r\n"+
				"PREFIX exp: <http://localhost:8080/webservice/webapi/producttypes/> \r\n" + 
				"PREFIX pto: <http://www.productontology.org/id/>\r\n" + 
				"SELECT ?productName ?productLabel ?productDescription ?productComment ?productURL ?productLanguage ?productType \r\n"+
				"WHERE\r\n"+
					"{\r\n"+
				"		expr:"+productID+"	rdf:type		schema:identifier, \r\n"+
				"											?productType; \r\n"+
				"							rdf:label	?productLabel; \r\n"+
				"							schema:homepage	?productURL;	\r\n" + 
				"							gr:description	?productDescription;	\r\n" +
				"							rdfs:comment	?productComment;	\r\n"+
				"							lang:namespacelang	?productLanguage.	\r\n" +
				"}							";
		return query;
				
	}
	
	public String getAllProductSparqlSelect() {
		String query = "PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
				"PREFIX schema: <http://schema.org/>\r\n" + 
				"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" +
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n" +
				"PREFIX lang: <http://www.w3.org/XML/1998/>\r\n" +
				"PREFIX expr: <http://localhost:8080/webservice/webapi/products/>\r\n"+
				"PREFIX exp: <http://localhost:8080/webservice/webapi/producttypes/> \r\n" + 
				"PREFIX pto: <http://www.productontology.org/id/>\r\n" + 
				"SELECT ?product  \r\n"+
				"WHERE \r\n"+
				"	{\r\n"+
				"		?product	rdf:type	schema:identifier.\r\n"+
				//"					rdfs:subClassOf		schema:Product,\r\n"+
				//"										gr:ProductOrService.\r\n"+
				"	}";
		
		return query;
	}
	public String insertProductSpaqrl(String productID, String productType, String productName, String productLabel, String productDescription, String productComment, String productPage) {
		String queryInsert = "PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
				"PREFIX schema: <http://schema.org/>\r\n" + 
				"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" +
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n" +
				"PREFIX lang: <http://www.w3.org/XML/1998/>\r\n" +
				"PREFIX expr: <http://localhost:8080/webservice/webapi/products/>\r\n"+
				"PREFIX exp: <http://localhost:8080/webservice/webapi/producttypes/> \r\n" + 
				"PREFIX pto: <http://www.productontology.org/id/>\r\n" +
				"INSERT DATA \r\n"+
				"	{\r\n"+
				"		expr:"+productID+"		rdf:type		schema:identifier,\r\n"+
				"												exp:"+productType+";\r\n"+
				"								rdfs:label		'"+productLabel+"';\r\n"+
				//"								schema:name		'"+productName+"'; \r\n"+
				"								gr:description	'"+productDescription+"'; \r\n"+
				"								rdfs:comment	'"+productComment+"'; \r\n"+				
				"								schema:url		<"+productPage+">.\r\n"+
				"	}";
		
		return queryInsert;
		
	}
	
	public String updateProductSparql(String oldProductID, String productID, String productType, String productLabel,String productDescription, String productComment, String productPage) {
		String queryupdate = "PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
				"PREFIX schema: <http://schema.org/>\r\n" + 
				"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" +
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n" +
				"PREFIX lang: <http://www.w3.org/XML/1998/>\r\n" +
				"PREFIX expr: <http://localhost:8080/webservice/webapi/products/>\r\n"+
				"PREFIX exp: <http://localhost:8080/webservice/webapi/producttypes/> \r\n" + 
				"PREFIX pto: <http://www.productontology.org/id/>\r\n" +
				"PREFIX exp: <http://localhost:8080/webservice/webapi/producttypes/>\r\n" +
				"DELETE\r\n" + 
				"	{ exco:"+oldProductID+" ?p ?s }\r\n" + 
				"WHERE\r\n" +
				"	{\r\n" + 
				"		exco:"+oldProductID+" ?p ?s;\r\n" + 
				"					rdf:type  schema:identifier, \r\n" +
				"								exp:"+productType+".\r\n"+
				"};"+
				"\r\n" + 
				"INSERT DATA\r\n" + 
				"	{\r\n"+
				"		expr:"+productID+"		rdf:type		schema:identifier;\r\n"+
				"												exp:"+productType+";\r\n"+
				"								rdfs:label		'"+productLabel+"';\r\n"+
				//"								schema:name		'"+productName+"'; \r\n"+
				"								gr:description	'"+productDescription+"'; \r\n"+
				"								rdfs:comment	'"+productComment+"'; \r\n"+
				"								schema:url		<"+productPage+">.\r\n"+
				"	}";
		return queryupdate;
	}
	
	public String deleteProductSparql(String productID) {
		String queryDelete =  "PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
				"PREFIX schema: <http://schema.org/>\r\n" + 
				"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" +
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n" +
				"PREFIX expr: <http://localhost:8080/webservice/webapi/products/>\r\n"+
				"PREFIX exp: <http://localhost:8080/webservice/webapi/producttypes/> \r\n" + 
				"DELETE\r\n" + 
				"	{ expr:"+productID+" ?p ?s }\r\n" + 
				"WHERE\r\n" +
				"	{\r\n" + 
				"		expr:"+productID+" ?p ?s;\r\n" + 
				"					rdf:type  schema:identifier. \r\n" +
				"}";
		return queryDelete;
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
				"PREFIX exp: <http://localhost:8080/webservice/webapi/producttypes/> \r\n" + 
				"PREFIX pto: <http://www.productontology.org/id/>\r\n" +
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
	
	public String insertCompanySparql(String companyID, String name, String legalName, String grLegalName, String homePage, String email) {
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
				"						schema:url			"+homePage+";\r\n"+ 
				"						schema:email		'"+email+"'.\r\n"+  
				"}";
		
		return queryInsert;
	}
	
	public String updateCompanySparql(String oldCompanyID, String companyID, String name, String label, String legalName, String grLegalName, String homePage, String email) {
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
				//"						schema:name			'"+name+"';			\r\n"+ 
				"						rdfs:label   		'"+name+"'; 		\r\n"+
				"						schema:legalName	'"+legalName+"';	\r\n"+
				"						gr:legalName		'"+legalName+"';	\r\n"+
				"						schema:url			<"+homePage+">;	\r\n"+ 
				"						schema:email		'"+email+"'.		\r\n"+
				"}";
		return queryUpdate;
				
	}
	
	public String getAllCompaniesSparqlSelect() {
		String querySelect = "PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
				"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
				"PREFIX schema: <http://schema.org/>\r\n" + 
				"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" +
				"\r\n" + 
				"SELECT ?company\r\n" + 
				"WHERE\r\n" + 
				"{\r\n" + 
				"?company		rdf:type 	schema:identifier,"+
				"							gr:BusinessEntity .\r\n" + 
				"}\r\n" + 
				"";
		
		return querySelect;
	}
	
	public String deleteAllOffers() {
		String deleteQuery = "PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
				"PREFIX exco:<http://localhost:8080/webservice/webapi/companies/> \r\n" + 
				"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
				"PREFIX schema: <http://schema.org/>\r\n" +
				"\r\n" + 
				"DELETE {?company	gr:offers     ?Offers }\r\n" + 
				"WHERE\r\n" + 
				"{\r\n" + 
				"?company	gr:offers		?Offers ;"+
				//"?Offers 	rdf:type	schema:identifier.\r\n" +
				"};\r\n" + 
				
				"\r\n" + 
				"DELETE {?Offer	?p     ?o }\r\n" + 
				"WHERE\r\n" + 
				"{\r\n" + 
				"?Offer	?p     ?o;"+
				//"		rdf:type	schema:identifier;"+
				"		gr:BusinessEntity	?company .\r\n" +
				"}\r\n" + 
				"";
		
		return deleteQuery;
	}
	
	public String deleteCompanySparql(String companyID) {
		String queryDelete = "PREFIX exco: <http://localhost:8080/webservice/webapi/companies/>\r\n" + 
				"PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
				"PREFIX exo: <http://localhost:8080/webservice/webapi/companies/"+companyID+"/offers/>\r\n" + 
				"PREFIX exp: <http://localhost:8080/webservice/webapi/products/>\r\n" +
				"PREFIX expr: <http://localhost:8080/webservice/webapi/products/>\r\n"+
				"PREFIX exco: <http://localhost:8080/webservice/webapi/companies/>\r\n" +
				"PREFIX schema: <http://schema.org/>\r\n" +
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n" + 
				"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>  \r\n" + 
				"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" +  
				"\r\n" + 
				"DELETE \r\n" + 
				"	{ exco:"+companyID+" ?p ?s }\r\n" + 
				"WHERE\r\n" + 
				"{ \r\n" + 
				"  exco:"+companyID+" rdf:type schema:identifier; .\r\n" + 
				"}\r\n" + 
				"";
	
		return queryDelete;
	}
	
	public String deleteProducttypeSparql(String id) {
		String queryDelete = "PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" +  
				"PREFIX exp: <http://localhost:8080/webservice/webapi/products/>\r\n" +
				"PREFIX expr: <http://localhost:8080/webservice/webapi/products/>\r\n"+
				"PREFIX exco: <http://localhost:8080/webservice/webapi/companies/>\r\n" +
				"PREFIX schema: <http://schema.org/>\r\n" +
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n" + 
				"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>  \r\n" + 
				"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
				"\r\n" + 
				"DELETE \r\n" + 
				"	{ exp:"+id+" ?p ?s }\r\n" + 
				"WHERE\r\n" + 
				"{ \r\n" + 
				"  exp:"+id+"	  	?p ?s;" +
				" 				rdf:type		schema:identifier,"+
				"								owl:Class;\r\n" + 
				"			 	rdfs:subClassOf		<http://schema.org/Product> .\r\n" + 
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
				"		rdf:type  gr:BusinessEntity .\r\n" + 
				"}";
		
		return queryDeleteAll;
	}
	
	
	public String getAllOffersSparqlSelect(String companyID) {
		String querySelect = "PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
				"PREFIX exo: <http://localhost:8080/webservice/webapi/companies/"+companyID+"/offers/>\r\n" + 
				"PREFIX exp: <http://localhost:8080/webservice/webapi/products/>\r\n" +
				"PREFIX expr: <http://localhost:8080/webservice/webapi/products/>\r\n"+
				"PREFIX exco: <http://localhost:8080/webservice/webapi/companies/>\r\n" +
				"PREFIX schema: <http://schema.org/>\r\n" +
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n" + 
				"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>  \r\n" + 
				"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" +  
				"\r\n" + 
				"SELECT ?Offers\r\n" + 
				"WHERE\r\n" + 
				"{\r\n" + 
				"exco:"+companyID+"	rdf:type schema:identifier;"+ 
				"					gr:offers		?Offers;"+
				//"    	?Offer		gr:includes		?product.\r\n" + 
				"}\r\n" + 
				"";
		return querySelect;
	}
	
	public String insertOfferSparql(String offerID, String productID, String companyID, String offerPage, Date validFrom, Date validThrough, String hasCurrency, float price, String comment, String description) {
		String queryInsert = "PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
				"PREFIX exo: <http://localhost:8080/webservice/webapi/companies/"+companyID+"/offers/>\r\n" + 
				"PREFIX exp: <http://localhost:8080/webservice/webapi/products/>\r\n" +
				"PREFIX expr: <http://localhost:8080/webservice/webapi/products/>\r\n"+
				"PREFIX exco: <http://localhost:8080/webservice/webapi/companies/>\r\n" +
				"PREFIX schema: <http://schema.org/>\r\n" +
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n" +
				"PREFIX foaf: <http://xmlns.com/foaf/0.1/> \r\n" + 
				"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>  \r\n" + 
				"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
				"\r\n" + 
				"INSERT DATA\r\n" + 
				"{ \r\n" + 
				"  exo:"+offerID+"	rdf:type		schema:identifier,\r\n"+
				"									gr:Offering;\r\n" + 
				"                   gr:includes      expr:"+productID+";\r\n" + 
				"					gr:BusinessEntity     exco:"+companyID+";\r\n" + 
				"					foaf:url     <"+offerPage+">;\r\n" + 
				"					gr:hasBusinessFunction gr:Sell ;\r\n" + 
				"					gr:validFrom '"+validFrom+"' ;\r\n" + 
				"					gr:validThrough '"+validThrough+"' ;\r\n" + 
				"					gr:hasPriceSpecification\r\n" + 
				"         				[ a gr:UnitPriceSpecification ;\r\n" + 
				"           				gr:hasCurrency '"+hasCurrency+"'^^xsd:string ;\r\n" + 
				"           			gr:hasCurrencyValue '"+price+"';\r\n" + 
				"           		gr:validThrough '"+validThrough+"' ];\r\n"+
				"					gr:description		'"+description+"';\r\n" +
				"					rdfs:comment		'"+comment+"'.\r\n"+
				"  exco:"+companyID+"	gr:offers		exo:"+offerID+" .\r\n" + 
				"}";
		return queryInsert;
	}
		
	public String updateOfferSparql(String oldOfferID, String offerID, String productID, String companyID, String offerPage, Date validFrom, Date validThrough, String hasCurrency, float price, String comment, String description) {
		String queryUpdate = "PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
				"PREFIX exo: <http://localhost:8080/webservice/webapi/companies/"+companyID+"/offers/>\r\n" + 
				"PREFIX exp: <http://localhost:8080/webservice/webapi/products/>\r\n" +
				"PREFIX expr: <http://localhost:8080/webservice/webapi/products/>\r\n"+
				"PREFIX exco: <http://localhost:8080/webservice/webapi/companies/>\r\n" +
				"PREFIX schema: <http://schema.org/>\r\n" +
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n" +
				"PREFIX foaf: <http://xmlns.com/foaf/0.1/> \r\n" + 
				"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>  \r\n" + 
				"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
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
				"  exo:"+offerID+"	rdf:type			schema:identifier,\r\n"+ 
				"										gr:Offering;\r\n" + 
				"                       gr:includes      expr:"+productID+";\r\n" + 
				"						gr:BusinessEntity     exco:"+companyID+";\r\n" + 
				"						schema:url    <"+offerPage+">;\r\n" + 
				"						gr:hasBusinessFunction gr:Sell ;\r\n" + 
				"						gr:validFrom '"+validFrom+"'^^xsd:dateTime ;\r\n" + 
				"						gr:validThrough '"+validThrough+"'^^xsd:dateTime ;\r\n" + 
				"						gr:hasPriceSpecification\r\n" + 
				"         					[ rdf:type gr:UnitPriceSpecification ;\r\n" + 
				"           					gr:hasCurrency '"+hasCurrency+"'^^xsd:string ;\r\n" + 
				"           					gr:hasCurrencyValue '"+price+"'^^xsd:float ;\r\n" + 
				"           					gr:validThrough '"+validThrough+"'^^xsd:dateTime ];\r\n" +
				"						gr:description		'"+description+"';\r\n" +
				"						rdfs:comment		'"+comment+"'.\r\n"+
				"  exco:"+companyID+"	gr:offers		exo:"+offerID+" .\r\n" + 
				"}";
		
		return queryUpdate;
	}
	
	public String deleteOfferSparql(String companyID, String offerID) {
		String queryDelete ="PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
				"PREFIX exo: <http://localhost:8080/webservice/webapi/companies/"+companyID+"/offers/>\r\n" + 
				"PREFIX exp: <http://localhost:8080/webservice/webapi/products/>\r\n" +
				"PREFIX expr: <http://localhost:8080/webservice/webapi/products/>\r\n"+
				"PREFIX exco: <http://localhost:8080/webservice/webapi/companies/>\r\n" +
				"PREFIX schema: <http://schema.org/>\r\n" +
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n" + 
				"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>  \r\n" + 
				"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
				"\r\n" + 
				"DELETE \r\n" + 
				"	{ exo:"+offerID+" ?p ?s }\r\n" + 
				"WHERE\r\n" + 
				"{ \r\n" + 
				"  exo:"+offerID+" ?p ?s;\r\n" +
				" 		rdf:type 	schema:identifier,\r\n"+
				"					gr:Offering .\r\n" + 
				"};\r\n" + 
				"\r\n" + 
				"DELETE {exco:"+companyID+"	gr:offers     exo:"+offerID+" }\r\n" + 
				"WHERE\r\n" + 
				"{\r\n" + 
				"	exco:"+companyID+"		rdf:type	schema:identifier;\r\n"+
				"	exo:"+offerID+"			rdf:type	schema:identifier;\r\n"+
				"	exco:"+companyID+"		gr:offers	exo:"+offerID+"    .\r\n" +
				"}";
		
		return queryDelete;
	}
	
	public String deleteAllOffersSparql(String companyID) {
		String queryDelete = "PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
				"PREFIX exo: <http://localhost:8080/webservice/webapi/companies/"+companyID+"/offers/>\r\n" + 
				"PREFIX exp: <http://localhost:8080/webservice/webapi/products/>\r\n" +
				"PREFIX expr: <http://localhost:8080/webservice/webapi/products/>\r\n"+
				"PREFIX exco: <http://localhost:8080/webservice/webapi/companies/>\r\n" +
				"PREFIX schema: <http://schema.org/>\r\n" +
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n" + 
				"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>  \r\n" + 
				"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
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
				"?offers	?p					?o;\r\n" +
				"			rdf:type 			schema:identifier,\r\n"+
				"								gr:Offering;\r\n" +
				"			gr:BusinessEntity	exco:"+companyID+".\r\n"+
				"}";
		
		return queryDelete;
	}
	
	public String getOffersToProductsSparql(String productID) {
		String queryDescribe = 
				"PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" +  
				"PREFIX exp: <http://localhost:8080/webservice/webapi/products/>\r\n" +
				"PREFIX expr: <http://localhost:8080/webservice/webapi/products/>\r\n"+
				"PREFIX exco: <http://localhost:8080/webservice/webapi/companies/>\r\n" +
				"PREFIX schema: <http://schema.org/>\r\n" +
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n" + 
				"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>  \r\n" + 
				"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" +  
						"\r\n" + 
				"DESCRIBE expr:"+productID+" WHERE { ?Companies gr:includes expr:"+productID+";\n" + 
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
		String querySelect = "PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
				"PREFIX exo: <http://localhost:8080/webservice/webapi/companies/"+companyID+"/offers/>\r\n" + 
				"PREFIX exp: <http://localhost:8080/webservice/webapi/products/>\r\n" +
				"PREFIX expr: <http://localhost:8080/webservice/webapi/products/>\r\n"+
				"PREFIX exco: <http://localhost:8080/webservice/webapi/companies/>\r\n" +
				"PREFIX schema: <http://schema.org/>\r\n" +
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n" + 
				"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>  \r\n" + 
				"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
				"\r\n" + 
				"SELECT ?offerURI ?validFrom ?validThrough ?hasCurrency ?price ?companyID\r\n" + 
				"\r\n" + 
				"WHERE\r\n" + 
				"{ \r\n" + 
				"  exo:"+offerID+" rdf:type  schema:identifier,\r\n"+
				"								gr:Offering;\r\n" + 
				"         schema:url	?offerURI;\r\n" + 
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

