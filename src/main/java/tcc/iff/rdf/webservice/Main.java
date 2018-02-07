package tcc.iff.rdf.webservice;


public class Main {

	public static void main(String[] args) {
		String str = "Definition BEGIN foo; Definition bar; END Definition baz";
	    
	    String replaceString = str.replace("BEGIN","{");
	    String last = replaceString.replace("END", "}");
	    System.out.println(last);
	    

	}

}
