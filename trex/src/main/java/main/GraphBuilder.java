package main;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.riot.Lang;

import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

/**
 * director
 *
 */
public class GraphBuilder {

	private static List<String> personPrpUris;
	private static List<String> moviePrpUris;
	private static List<String> movieRscUris;
	private static List<String> locationPrpUris;

	public static void main(String[] args) throws Exception {

		List<String> personRscUris = new ArrayList<String>();

		Model mainModel = ModelFactory.createDefaultModel();

		initialize();

		// construct movie model
		Model movieModel = ModelBuilder.buildModel(movieRscUris, moviePrpUris);

		// construct actor model
		String actorObjectQuery = QueryManager.buildQuery(movieRscUris, null, Constants.SELECT_DISTINCT_O,
				Constants.ACTOR_QUERY_BODY, Constants.SUBJECT_VAR_NAME, 0, movieRscUris.size());
		List<String> actorRscUris = UriBuilder.buildUriList(movieModel, actorObjectQuery, Constants.OBJECT_VAR_NAME);
		Model actorModel = ModelBuilder.buildModel(actorRscUris, personPrpUris);
		personRscUris.addAll(actorRscUris);
		mainModel.add(actorModel);

		// construct director model
		String directorObjectQuery = QueryManager.buildQuery(movieRscUris, null, Constants.SELECT_DISTINCT_O,
				Constants.DIRECTOR_QUERY_BODY, Constants.SUBJECT_VAR_NAME, 0, movieRscUris.size());
		List<String> directorRscUris = UriBuilder.buildUriList(movieModel, directorObjectQuery,
				Constants.OBJECT_VAR_NAME);
		Model directorModel = ModelBuilder.buildModel(directorRscUris, personPrpUris);
		personRscUris.addAll(directorRscUris);
		mainModel.add(directorModel);

		// construct distributor model
		String distributorObjectQuery = QueryManager.buildQuery(movieRscUris, null, Constants.SELECT_DISTINCT_O,
				Constants.DISTRIBUTOR_QUERY_BODY, Constants.SUBJECT_VAR_NAME, 0, movieRscUris.size());
		List<String> distributorRscUris = UriBuilder.buildUriList(movieModel, distributorObjectQuery,
				Constants.OBJECT_VAR_NAME);
		Model distributorModel = ModelBuilder.buildModel(distributorRscUris, personPrpUris);
		personRscUris.addAll(distributorRscUris);
		mainModel.add(distributorModel);

		// construct music composer model
		String musicComposerObjectQuery = QueryManager.buildQuery(movieRscUris, null, Constants.SELECT_DISTINCT_O,
				Constants.MUSIC_COMPOSER_QUERY_BODY, Constants.SUBJECT_VAR_NAME, 0, movieRscUris.size());
		List<String> musicComposerRscUris = UriBuilder.buildUriList(movieModel, musicComposerObjectQuery,
				Constants.OBJECT_VAR_NAME);
		Model musicComposerModel = ModelBuilder.buildModel(musicComposerRscUris, personPrpUris);
		personRscUris.addAll(musicComposerRscUris);
		mainModel.add(musicComposerModel);

		// construct producer model
		String producerObjectQuery = QueryManager.buildQuery(movieRscUris, null, Constants.SELECT_DISTINCT_O,
				Constants.PRODUCER_QUERY_BODY, Constants.SUBJECT_VAR_NAME, 0, movieRscUris.size());
		List<String> producerRscUris = UriBuilder.buildUriList(movieModel, producerObjectQuery,
				Constants.OBJECT_VAR_NAME);
		Model producerModel = ModelBuilder.buildModel(producerRscUris, personPrpUris);
		personRscUris.addAll(producerRscUris);
		mainModel.add(producerModel);

		// construct writer model
		String writerObjectQuery = QueryManager.buildQuery(movieRscUris, null, Constants.SELECT_DISTINCT_O,
				Constants.WRITER_QUERY_BODY, Constants.SUBJECT_VAR_NAME, 0, movieRscUris.size());
		List<String> writerRscUris = UriBuilder.buildUriList(movieModel, writerObjectQuery, Constants.OBJECT_VAR_NAME);
		Model writerModel = ModelBuilder.buildModel(writerRscUris, personPrpUris);
		personRscUris.addAll(writerRscUris);
		mainModel.add(writerModel);

		// construct writer model
		String countryObjectQuery = QueryManager.buildQuery(movieRscUris, null, Constants.SELECT_DISTINCT_O,
				Constants.COUNTRY_QUERY_BODY, Constants.SUBJECT_VAR_NAME, 0, movieRscUris.size());
		List<String> countryRscUris = UriBuilder.buildUriList(movieModel, countryObjectQuery,
				Constants.OBJECT_VAR_NAME);
		Model countryModel = ModelBuilder.buildModel(countryRscUris, locationPrpUris);

		// construct birthPlace model
		String birthPlaceObjectQuery = QueryManager.buildQuery(personRscUris, null, Constants.SELECT_DISTINCT_O,
				Constants.BIRTHPLACE_QUERY_BODY, Constants.SUBJECT_VAR_NAME, 0, personRscUris.size());
		List<String> birthPlaceRscUris = UriBuilder.buildUriList(mainModel, birthPlaceObjectQuery,
				Constants.OBJECT_VAR_NAME);
		Model birthPlaceModel = ModelBuilder.buildModel(birthPlaceRscUris, locationPrpUris);

		String producedFilmQuery = QueryManager.buildQuery(producerRscUris, null, Constants.SELECT_DISTINCT_S,
				Constants.PRODUCER_QUERY_BODY, Constants.OBJECT_VAR_NAME, 0, producerRscUris.size());
		ResultSet resultSetPM = QueryManager.runSelectQuery(producedFilmQuery, Constants.DBPEDIA_ENDPOINT);
		List<String> producedMovies = UriBuilder.buildUriList(resultSetPM, Constants.SUBJECT_VAR_NAME);
		Model producedMovieModel = ModelBuilder.buildModel(producedMovies, moviePrpUris);

		String writtenFilmQuery = QueryManager.buildQuery(writerRscUris, null, Constants.SELECT_DISTINCT_S,
				Constants.WRITER_QUERY_BODY, Constants.OBJECT_VAR_NAME, 0, writerRscUris.size());
		ResultSet resultSetWM = QueryManager.runSelectQuery(writtenFilmQuery, Constants.DBPEDIA_ENDPOINT);
		List<String> writtenMovies = UriBuilder.buildUriList(resultSetWM, Constants.SUBJECT_VAR_NAME);
		Model writtenMovieModel = ModelBuilder.buildModel(writtenMovies, moviePrpUris);

		String distributedFilmQuery = QueryManager.buildQuery(distributorRscUris, null, Constants.SELECT_DISTINCT_S,
				Constants.DISTRIBUTOR_QUERY_BODY, Constants.OBJECT_VAR_NAME, 0, distributorRscUris.size());
		ResultSet resultSetDM = QueryManager.runSelectQuery(distributedFilmQuery, Constants.DBPEDIA_ENDPOINT);
		List<String> distributedMovies = UriBuilder.buildUriList(resultSetDM, Constants.SUBJECT_VAR_NAME);
		Model distributedMovieModel = ModelBuilder.buildModel(distributedMovies, moviePrpUris);

		String composedFilmQuery = QueryManager.buildQuery(musicComposerRscUris, null, Constants.SELECT_DISTINCT_S,
				Constants.COMPOSER_QUERY_BODY, Constants.OBJECT_VAR_NAME, 0, musicComposerRscUris.size());
		ResultSet resultSetCM = QueryManager.runSelectQuery(composedFilmQuery, Constants.DBPEDIA_ENDPOINT);
		List<String> composedMovies = UriBuilder.buildUriList(resultSetCM, Constants.SUBJECT_VAR_NAME);
		Model composedMovieModel = ModelBuilder.buildModel(composedMovies, moviePrpUris);

		mainModel.add(movieModel);
		mainModel.add(countryModel);
		mainModel.add(birthPlaceModel);
		mainModel.add(producedMovieModel);
		mainModel.add(writtenMovieModel);
		mainModel.add(distributedMovieModel);
		mainModel.add(composedMovieModel);
		mainModel.write(new FileWriter(new File(Constants.MODEL_FILE_PATH)), Lang.TTL.getName());

	}

	private static void initialize() {
		personPrpUris = FileReader.readLines("src/main/resources/person-properties.txt");
		locationPrpUris = FileReader.readLines("src/main/resources/location-properties.txt");
		moviePrpUris = FileReader.readLines("src/main/resources/movie-properties.txt");
		movieRscUris = FileReader.readLines("src/main/resources/movie-resources.txt");
	}

}
