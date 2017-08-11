package main;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

import org.apache.jena.riot.Lang;

import com.hp.hpl.jena.rdf.model.Model;

/**
 * director
 *
 */
public class GraphBuilder {

	private static List<String> personPrpUris;
	private static List<String> moviePrpUris;
	private static List<String> movieRscUris;

	public static void main(String[] args) throws Exception {

		initialize();

		// construct movie model
		Model movieModel = ModelBuilder.buildModel(movieRscUris, moviePrpUris);

		// construct actor model
		String actorObjectQuery = QueryManager.buildQuery(movieRscUris, null, Constants.SELECT_DISTINCT_O,
				Constants.ACTOR_QUERY_BODY);
		List<String> actorRscUris = ModelBuilder.buildUriList(movieModel, actorObjectQuery);
		Model actorModel = ModelBuilder.buildModel(actorRscUris, personPrpUris);

		// construct director model
		String directorObjectQuery = QueryManager.buildQuery(movieRscUris, null, Constants.SELECT_DISTINCT_O,
				Constants.DIRECTOR_QUERY_BODY);
		List<String> directorRscUris = ModelBuilder.buildUriList(movieModel, directorObjectQuery);
		Model directorModel = ModelBuilder.buildModel(directorRscUris, personPrpUris);

		// construct distributor model
		String distributorObjectQuery = QueryManager.buildQuery(movieRscUris, null, Constants.SELECT_DISTINCT_O,
				Constants.DISTRIBUTOR_QUERY_BODY);
		List<String> distributorRscUris = ModelBuilder.buildUriList(movieModel, distributorObjectQuery);
		Model distributorModel = ModelBuilder.buildModel(distributorRscUris, personPrpUris);

		// construct music composer model
		String musicComposerObjectQuery = QueryManager.buildQuery(movieRscUris, null, Constants.SELECT_DISTINCT_O,
				Constants.MUSIC_COMPOSER_QUERY_BODY);
		List<String> musicComposerRscUris = ModelBuilder.buildUriList(movieModel, musicComposerObjectQuery);
		Model musicComposerModel = ModelBuilder.buildModel(musicComposerRscUris, personPrpUris);

		// construct producer model
		String producerObjectQuery = QueryManager.buildQuery(movieRscUris, null, Constants.SELECT_DISTINCT_O,
				Constants.PRODUCER_QUERY_BODY);
		List<String> producerRscUris = ModelBuilder.buildUriList(movieModel, producerObjectQuery);
		Model producerModel = ModelBuilder.buildModel(producerRscUris, personPrpUris);

		// construct writer model
		String writerObjectQuery = QueryManager.buildQuery(movieRscUris, null, Constants.SELECT_DISTINCT_O,
				Constants.WRITER_QUERY_BODY);
		List<String> writerRscUris = ModelBuilder.buildUriList(movieModel, writerObjectQuery);
		Model writerModel = ModelBuilder.buildModel(writerRscUris, personPrpUris);

		movieModel.add(actorModel);
		movieModel.add(directorModel);
		movieModel.add(distributorModel);
		movieModel.add(musicComposerModel);
		movieModel.add(producerModel);
		movieModel.add(writerModel);
		movieModel.write(new FileWriter(new File(Constants.MODEL_FILE_PATH)), Lang.TTL.getName());

	}

	private static void initialize() {
		personPrpUris = FileReader.readLines("src/main/resources/person-properties.txt");
		moviePrpUris = FileReader.readLines("src/main/resources/movie-properties.txt");
		movieRscUris = FileReader.readLines("src/main/resources/movie-resources.txt");
	}

}
