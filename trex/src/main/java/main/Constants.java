package main;

public class Constants {

	public static final String DBO_PRODUCER = "http://dbpedia.org/ontology/producer";
	public static final String DBO_WRITER = "http://dbpedia.org/ontology/writer";
	public static final String DBO_COMPOSER = "http://dbpedia.org/ontology/musicComposer";
	public static final String DBO_DISTRIBUTOR = "http://dbpedia.org/ontology/distributor";
	public static final String SUBJECT_VAR_NAME = "s";
	public static final String OBJECT_VAR_NAME = "o";
	public static final String S_P_O = "?s ?p ?o. ";
	public static final String CONSTRUCT_S_P_O = "CONSTRUCT {?s ?p ?o.}";
	public static final String CONSTRUCT_S_PRODUCER_O = "CONSTRUCT {" + "?s <" + DBO_PRODUCER + "> ?o." + "}";
	public static final String SELECT_DISTINCT_O = "SELECT DISTINCT ?o";
	public static final String SELECT_DISTINCT_S = "SELECT DISTINCT ?s";
	public final static String DBPEDIA_ENDPOINT = "http://dbpedia.org/sparql";
	public final static String LMDB_ENDPOINT = "http://www.linkedmdb.org/sparql";
	public static final String MODEL_FILE_PATH = "src/main/resources/mainModel.ttl";
	public static final String MODEL_FILE_PATH2 = "src/main/resources/mainModel2.ttl";
	public static final String DIRECTOR_QUERY_BODY = "?s <http://dbpedia.org/ontology/director> ?o";
	public static final String ACTOR_QUERY_BODY = "?s <http://dbpedia.org/ontology/starring> ?o";
	public static final String MUSIC_COMPOSER_QUERY_BODY = "?s <http://dbpedia.org/ontology/musicComposer> ?o";
	public static final String PRODUCER_QUERY_BODY = "?s <" + DBO_PRODUCER + "> ?o."
			+ "?s <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://dbpedia.org/ontology/Film>.";
	public static final String COMPOSER_QUERY_BODY = "?s <" + DBO_COMPOSER + "> ?o."
			+ "?s <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://dbpedia.org/ontology/Film>.";
	public static final String WRITER_QUERY_BODY = "?s <" + DBO_WRITER + "> ?o."
			+ "?s <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://dbpedia.org/ontology/Film>.";
	public static final String DISTRIBUTOR_QUERY_BODY = "?s <" + DBO_DISTRIBUTOR + "> ?o."
			+ "?s <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://dbpedia.org/ontology/Film>.";
	public static final String BIRTHPLACE_QUERY_BODY = "?s <http://dbpedia.org/ontology/birthPlace> ?o";
	public static final String COUNTRY_QUERY_BODY = "?s <http://dbpedia.org/property/country> ?o";

}
