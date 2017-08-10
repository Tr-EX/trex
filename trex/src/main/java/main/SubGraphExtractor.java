package main;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.vocabulary.RDFS;

/**
 * director
 *
 */
public class SubGraphExtractor {

	private static final String S_P_O = "?s ?p ?o. ";
	private static final String CONSTRUCT_S_P_O = "CONSTRUCT {?s ?p ?o.}";
	private static final String SELECT_DISTINCT_O = "SELECT DISTINCT ?o";
	private static final List<String> directorPrpUris = FileReader
			.readLines("src/main/resources/director-properties.txt");
	private static final List<String> actorPrpUris = FileReader.readLines("src/main/resources/actor-properties.txt");
	private static final List<String> moviePrpUris = FileReader.readLines("src/main/resources/movie-properties.txt");
	private static final List<String> movieRscUris = FileReader.readLines("src/main/resources/movie-resources.txt");
	public final static String DBPEDIA_ENDPOINT = "http://dbpedia.org/sparql";
	public final static String LMDB_ENDPOINT = "http://www.linkedmdb.org/sparql";

	public static void main(String[] args) throws Exception {


		// TODO: other resources will be added

		Model movieModel = buildModel(movieRscUris, moviePrpUris);

		String actorObjectQuery = buildQuery(movieRscUris, null, SELECT_DISTINCT_O,
				"?s <http://dbpedia.org/ontology/starring> ?o");
		List<String> actorRscUris = buildObjectList(movieModel, actorObjectQuery);

		Model actorModel = buildModel(actorRscUris, actorPrpUris);
		// actorModel.write(System.out);
		
		String directorObjectQuery = buildQuery(movieRscUris, null, SELECT_DISTINCT_O,
				"?s <http://dbpedia.org/ontology/director> ?o");
		List<String> directorRscUris = buildObjectList(movieModel, directorObjectQuery);

		Model directorModel = buildModel(directorRscUris, directorPrpUris);
		// directorModel.write(System.out);

		movieModel.add(actorModel);
		movieModel.add(directorModel);
		movieModel.write(new FileWriter(new File("src/main/resources/movieModel.ttl")), "TTL");

	}

	private static List<String> buildObjectList(Model model, String query) {
		List<String> actorRscUris = new ArrayList<String>();
		ResultSet resultSet = QueryManager.runSelectQueryOnModel(query,model);
		while (resultSet.hasNext()) {
			QuerySolution querySolution = (QuerySolution) resultSet.next();
			actorRscUris.add(querySolution.getResource("o").getURI());
		}
		return actorRscUris;
	}

	private static Model buildDirectorModel(Model movieModel) {
		List<Resource> directorRscList = movieModel
				.listResourcesWithProperty(ResourceFactory.createProperty("http://dbpedia.org/ontology/director"))
				.toList();
		List<String> directorUriList = turnResourcesIntoUris(directorRscList);
		String directorQuery = buildQuery(directorUriList, directorPrpUris, CONSTRUCT_S_P_O, S_P_O);
		Model directorModel = QueryManager.runConstructQuery(directorQuery, DBPEDIA_ENDPOINT);
		return directorModel;
	}

	private static Model buildActorModel(Model movieModel) {
		List<Resource> movieRscList = movieModel.listResourcesWithProperty(RDFS.label).toList();
		List<String> actorUriList = turnResourcesIntoUris(movieRscList);
		String actorQuery = buildQuery(actorUriList, actorPrpUris, CONSTRUCT_S_P_O, S_P_O);
		Model actorModel = QueryManager.runConstructQuery(actorQuery, DBPEDIA_ENDPOINT);
		return actorModel;
	}

	private static Model buildModel(List<String> rscUriList, List<String> prpUriList) {
		String filmQuery = buildQuery(rscUriList, prpUriList, CONSTRUCT_S_P_O, S_P_O);
		Model movieModel = QueryManager.runConstructQuery(filmQuery, DBPEDIA_ENDPOINT);
		return movieModel;
	}

	private static List<String> turnResourcesIntoUris(List<Resource> rscList) {
		List<String> uriList = new ArrayList<String>();
		for (Resource resource : rscList) {
			uriList.add(resource.getURI());
		}
		return uriList;
	}

	private static String buildQuery(List<String> rscUris, List<String> prpUris, String queryInitial,
			String queryBody) {
		String valuesQuery = queryInitial
				// String valuesQuery = "SELECT *"
				+ " WHERE {{" + queryBody;
		if (prpUris != null && !prpUris.isEmpty()) {
			valuesQuery = addFilterWithProps(prpUris, valuesQuery);
		}
		valuesQuery += "} ";
		valuesQuery = addValuesWithObjects(rscUris, valuesQuery);
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
