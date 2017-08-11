package main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;

public class QueryManager {

	public static void writeModel(String queryString, String endpoint, String outputFilePath) {
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.sparqlService(endpoint, query);
		Model resultModel = qexec.execConstruct();
		try {
			resultModel.write(new FileWriter(new File(outputFilePath)), "RDF/XML");
		} catch (IOException e) {
			e.printStackTrace();
		}
		qexec.close();
	}

	public static Model runConstructQuery(String queryString, String endpoint) {
		Query query = QueryFactory.create(queryString);
		// System.out.println(query);
		// @SuppressWarnings("resource")
		// QueryEngineHTTP engineHTTP = new QueryEngineHTTP(endpoint, query,
		// (HttpAuthenticator) null);
		// engineHTTP.setModelContentType(RDFLanguages.strLangRDFXML);
		// Model model = engineHTTP.execConstruct();
		QueryExecution qe = QueryExecutionFactory.sparqlService(endpoint, query);
		Model model = qe.execConstruct();
		// qe.close();
		return model;
	}

	public static ResultSet runSelectQueryOnModel(String queryString, Model model) {
		Query query = QueryFactory.create(queryString);
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet resultSet = qe.execSelect();
		// qe.close();
		return resultSet;
	}

	static String buildQuery(List<String> rscUris, List<String> prpUris, String queryInitial, String queryBody,
			String varName) {
		String valuesQuery = queryInitial
				// String valuesQuery = "SELECT *"
				+ " WHERE {{" + queryBody;
		if (prpUris != null && !prpUris.isEmpty()) {
			valuesQuery = QueryManager.addFilterWithProps(prpUris, valuesQuery);
		}
		valuesQuery += "} ";
		valuesQuery = QueryManager.addValuesWithObjects(rscUris, valuesQuery, varName);
		valuesQuery += "}}";
		return valuesQuery;
	}

	static String addValuesWithObjects(List<String> rscUris, String valuesQuery, String varName) {
		valuesQuery += "VALUES (?" + varName + ") {";
		String compositeLine = "";
		for (String rscUri : rscUris) {
			compositeLine += "(" + " <" + rscUri + "> " + ")";
		}
		valuesQuery += compositeLine;
		return valuesQuery;
	}

	static String addFilterWithProps(List<String> entityPrpUris, String valuesQuery) {
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
