package main;

import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;

public class UriBuilder {

	public static List<String> buildUriList(Model model, String query, String varName) {
		ResultSet resultSet = QueryManager.runSelectQueryOnModel(query, model);
		List<String> rscUris = UriBuilder.buildUriList(resultSet, varName);
		return rscUris;
	}

	public static List<String> buildUriList(ResultSet resultSet, String varName) {
		List<String> rscUris = new ArrayList<String>();
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
