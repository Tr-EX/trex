package main;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.vocabulary.RDFS;

/**
 * director
 *
 */
public class SubGraphExtractor {

	public final static String DBPEDIA_ENDPOINT = "http://dbpedia.org/sparql";
	public final static String LMDB_ENDPOINT = "http://www.linkedmdb.org/sparql";

	public static void main(String[] args) throws Exception {
		
		//FIXME: subject yerine objectler çekilecek

		Model movieModel = buildMovieModel();

		Model actorModel = buildActorModel(movieModel);
		// actorModel.write(System.out);

		Model directorModel = buildDirectorModel(movieModel);
		// directorModel.write(System.out);

		movieModel.add(actorModel);
		movieModel.add(directorModel);
		movieModel.write(new FileWriter(new File("src/main/resources/movieModel.ttl")), "TTL");

	}

	private static Model buildDirectorModel(Model movieModel) {
		List<Resource> directorRscList = movieModel
				.listResourcesWithProperty(ResourceFactory.createProperty("http://dbpedia.org/ontology/director"))
				.toList();
		List<String> directorUriList = turnResourcesIntoUris(directorRscList);
		String directorQuery = buildQuery(directorUriList,
				FileReader.readLines("src/main/resources/director-properties.txt"));
		Model directorModel = QueryManager.runQuery(directorQuery, DBPEDIA_ENDPOINT);
		return directorModel;
	}

	private static Model buildActorModel(Model movieModel) {
		List<Resource> movieRscList = movieModel.listResourcesWithProperty(RDFS.label).toList();
		List<String> actorUriList = turnResourcesIntoUris(movieRscList);
		String actorQuery = buildQuery(actorUriList, FileReader.readLines("src/main/resources/actor-properties.txt"));
		Model actorModel = QueryManager.runQuery(actorQuery, DBPEDIA_ENDPOINT);
		return actorModel;
	}

	private static Model buildMovieModel() {
		String filmQuery = buildQuery(FileReader.readLines("src/main/resources/movie-resources.txt"),
				FileReader.readLines("src/main/resources/movie-properties.txt"));
		Model movieModel = QueryManager.runQuery(filmQuery, DBPEDIA_ENDPOINT);
		return movieModel;
	}

	private static List<String> turnResourcesIntoUris(List<Resource> rscList) {
		List<String> uriList = new ArrayList<String>();
		for (Resource resource : rscList) {
			uriList.add(resource.getURI());
		}
		return uriList;
	}

	private static String buildQuery(List<String> rscUris, List<String> prpUris) {
		String valuesQuery = "CONSTRUCT {?s ?p ?o.}"
				// String valuesQuery = "SELECT *"
				+ " WHERE {{?s ?p ?o. ";
		valuesQuery = addFilterWithProps(prpUris, valuesQuery);
		valuesQuery += "} ";
		valuesQuery = addValuesWithObjects(rscUris, valuesQuery);
		valuesQuery += "}}";
		return valuesQuery;
	}

	private static String buildDirectorQuery() {
		List<String> directorRscUris = FileReader.readLines("src/main/resources/director-resources.txt");
		List<String> directorPrpUris = FileReader.readLines("src/main/resources/director-properties.txt");
		// List<String> actorPrpUris =
		// FileReader.readLines("src/main/resources/actor-properties.txt");
		// List<String> directorPrpUris =
		// FileReader.readLines("src/main/resources/director-properties.txt");
		String valuesQuery = "CONSTRUCT {?s ?p ?o.}"
				// String valuesQuery = "SELECT *"
				+ " WHERE {{?s ?p ?o. ";
		valuesQuery = addFilterWithProps(directorPrpUris, valuesQuery);
		valuesQuery += "} ";
		valuesQuery = addValuesWithObjects(directorRscUris, valuesQuery);
		valuesQuery += "}}";
		return valuesQuery;
	}

	private static String addValuesWithObjects(List<String> movieRscUris, String valuesQuery) {
		valuesQuery += "VALUES (?s) {";
		String compositeLine = "";
		for (String movieRscUri : movieRscUris) {
			compositeLine += "(" + " <" + movieRscUri + "> " + ")";
		}
		valuesQuery += compositeLine;
		return valuesQuery;
	}

	private static String addFilterWithProps(List<String> entityPrpUris, String valuesQuery) {
		valuesQuery += "FILTER (?p IN(";
		for (int i = 0; i < entityPrpUris.size(); i++) {
			String moviePrpUri = entityPrpUris.get(i);
			valuesQuery += "<" + moviePrpUri + ">";
			if (i < entityPrpUris.size() - 1) {
				valuesQuery += ",";
			}
		}
		valuesQuery += "))";
		return valuesQuery;
	}

}
