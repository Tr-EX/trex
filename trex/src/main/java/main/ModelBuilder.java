package main;

import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;

public class ModelBuilder {

	static Model buildModel(List<String> rscUriList, List<String> prpUriList) {
		String filmQuery = QueryManager.buildQuery(rscUriList, prpUriList, Constants.CONSTRUCT_S_P_O, Constants.S_P_O, Constants.SUBJECT_VAR_NAME);
		Model movieModel = QueryManager.runConstructQuery(filmQuery, Constants.DBPEDIA_ENDPOINT);
		return movieModel;
	}

	static List<String> buildUriList(Model model, String query, String varName) {
		List<String> rscUris = new ArrayList<String>();
		ResultSet resultSet = QueryManager.runSelectQueryOnModel(query, model);
		while (resultSet.hasNext()) {
			QuerySolution querySolution = (QuerySolution) resultSet.next();
			RDFNode rdfNode = querySolution.get(varName);
			if (rdfNode.isResource()) {
				rscUris.add(rdfNode.asResource().getURI());
			}
		}
		return rscUris;
	}

}
