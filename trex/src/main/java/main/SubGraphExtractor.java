package main;

import java.util.List;

import com.hp.hpl.jena.query.QueryFactory;

/**
 * director
 *
 */
public class SubGraphExtractor {

	public final static String DBPEDIA_ENDPOINT = "http://dbpedia.org/sparql";
	public final static String LMDB_ENDPOINT = "http://www.linkedmdb.org/sparql";

	public static void main(String[] args) {

		String valuesQuery = buildQuery();
		QueryManager.runQuery(valuesQuery, DBPEDIA_ENDPOINT);

	}

	private static String buildQuery() {
		List<String> movieRscUris = FileReader.readLines("src/main/resources/movie-resources.txt");
		List<String> moviePrpUris = FileReader.readLines("src/main/resources/movie-properties.txt");
//		List<String> actorPrpUris = FileReader.readLines("src/main/resources/actor-properties.txt");
//		List<String> directorPrpUris = FileReader.readLines("src/main/resources/director-properties.txt");
		String valuesQuery = "CONSTRUCT {?s ?p ?o.}"
//		String valuesQuery = "SELECT *"
				+ " WHERE {{?s ?p ?o. FILTER (?p IN(";
		for (int i = 0; i < moviePrpUris.size(); i++) {
			String moviePrpUri = moviePrpUris.get(i);
			valuesQuery += "<" + moviePrpUri + ">";
			if (i < moviePrpUris.size() - 1) {
				valuesQuery += ",";
			}
		}
		valuesQuery += "))";
		valuesQuery += "} VALUES (?s) {";
		String compositeLine = "";
		for (String movieRscUri : movieRscUris) {
			compositeLine += "(" + " <" + movieRscUri + "> " + ")";
		}
		valuesQuery += compositeLine + "}}";
		return valuesQuery;
	}

}
