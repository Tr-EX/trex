package main;

public class GraphWalkerApp {

	public final static String DBPEDIA_ENDPOINT = "http://dbpedia.org/sparql";
	public final static String LMDB_ENDPOINT = "http://www.linkedmdb.org/sparql";

	public static void main(String[] args) {

		// String querySTR = new
		// QueryManager().generateQuery("http://dbpedia.org/resource/Wicker_Park_(film)",
		// 4, 10);

		String query = "select distinct ?p ?o where  { " + "<http://data.linkedmdb.org/resource/film/2079> ?p ?o . "
				+ "}";

		String query2 = "select distinct ?p ?o where  { " + " { "
				+ " <http://data.linkedmdb.org/resource/film/2014> ?p ?o . " + " } " + " union " + " { "
				+ " <http://data.linkedmdb.org/resource/film/2079> ?p ?o . " + " } " + "}";

		String queryConstr = " construct { " + " <http://data.linkedmdb.org/resource/film/2014> ?p ?o . "
				+ " <http://data.linkedmdb.org/resource/film/2079> ?p ?o . " + " } where  {" + " { "
				+ " <http://data.linkedmdb.org/resource/film/2014> ?p ?o . " + "}" + "union " + "{"
				+ "<http://data.linkedmdb.org/resource/film/2079> ?p ?o ." + " } " + "}";

		String queryConstrDBpediaDepth1 = " construct { " + " <http://dbpedia.org/resource/The_Shining_(film)> ?p ?o . "
				+ " <http://dbpedia.org/resource/Tommy_(film)> ?p ?o . " + " } where  {" + " { "
				+ " <http://dbpedia.org/resource/The_Shining_(film)> ?p ?o . " + "}" + "union " + "{"
				+ "<http://dbpedia.org/resource/Tommy_(film)> ?p ?o ." + " } " + "}";

		String queryConstrDBpediaDepth2 = " construct { " + " <http://dbpedia.org/resource/The_Shining_(film)> ?p ?o . "
				+ " ?o ?p2 ?o2 . " + " <http://dbpedia.org/resource/Tommy_(film)> ?p ?o . " + " ?o ?p2 ?o2 . "
				+ " } where  {" + " { " + " <http://dbpedia.org/resource/The_Shining_(film)> ?p ?o . "
				+ " ?o ?p2 ?o2 . " + "}" + "union " + "{" + "<http://dbpedia.org/resource/Tommy_(film)> ?p ?o ."
				+ " ?o ?p2 ?o2 . " + " } " + "}";

		new QueryManager().writeModel(queryConstrDBpediaDepth2, DBPEDIA_ENDPOINT, "output3.rdf");
		new QueryManager().runQuery(query2, LMDB_ENDPOINT);

	}

}
