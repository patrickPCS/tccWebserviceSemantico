package tcc.iff.rdf.webservice.connection;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClients;
import org.apache.jena.riot.web.HttpOp;

public class Authentication {
	
	public Authentication() {}
	
	public void getAuthentication() {
	CredentialsProvider credsProvider = new BasicCredentialsProvider();
	Credentials credentials = new UsernamePasswordCredentials("super","super");
	credsProvider.setCredentials(AuthScope.ANY, credentials);
	HttpClient httpclient = HttpClients.custom()
			.setDefaultCredentialsProvider(credsProvider)
			.build();
	HttpOp.setDefaultHttpClient(httpclient);
	
	}
	
	

}
