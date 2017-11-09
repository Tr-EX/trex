package main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.apache.jena.riot.Lang;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFactory;
import com.hp.hpl.jena.query.ResultSetRewindable;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class QueryManager {

	private static final String DBPEDIA_RSC_URISPACE = "http://dbpedia.org/resource/";

	public static void writeModel(String queryString, String endpoint, String outputFilePath) {
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.sparqlService(endpoint, query);
		Model resultModel = qexec.execConstruct();
		try {
			resultModel.write(new FileWriter(new File(outputFilePath)), Lang.RDFXML.getName());
		} catch (IOException e) {
			e.printStackTrace();
		}
		qexec.close();
	}

	public static Model runConstructQuery(String queryString, String endpoint) {
		Model model = ModelFactory.createDefaultModel();
		Query query = QueryFactory.create(queryString);
		try {			
			QueryExecution qe = QueryExecutionFactory.sparqlService(endpoint, query);
			model = qe.execConstruct();
			qe.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(query);
		}
		return model;
	}

	public static ResultSet runSelectQuery(String queryString, String endpoint) {
		Query query = QueryFactory.create(queryString);
		System.out.println(query);
		QueryExecution qe = QueryExecutionFactory.sparqlService(endpoint, query);
		ResultSet resultSet = qe.execSelect();
		ResultSetRewindable setRewindable = ResultSetFactory.makeRewindable(resultSet);
		qe.close();
		return setRewindable;
	}

	public static ResultSet runSelectQueryOnModel(String queryString, Model model) {
		Query query = QueryFactory.create(queryString);
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet resultSet = qe.execSelect();
		ResultSetRewindable setRewindable = ResultSetFactory.makeRewindable(resultSet);
		qe.close();
		return setRewindable;
	}

	static String buildQuery(List<String> rscUris, List<String> prpUris, String queryInitial, String queryBody,
			String varName, int startIndex, int endIndex) {
		String valuesQuery = queryInitial + " WHERE {{" + queryBody;
		if (prpUris != null && !prpUris.isEmpty()) {
			valuesQuery = QueryManager.addFilterWithProps(prpUris, valuesQuery);
		}
		valuesQuery += "} ";
		valuesQuery = QueryManager.addValuesWithObjects(rscUris, valuesQuery, varName, startIndex, endIndex);
		valuesQuery += "}}";
		return valuesQuery;
	}

	static String addValuesWithObjects(List<String> rscUris, String valuesQuery, String varName, int startIndex,
			int endIndex) {
		valuesQuery += "VALUES (?" + varName + ") {";
		String compositeLine = "";
		for (int i = startIndex; i < endIndex; i++) {
			String rscUri = rscUris.get(i);
			if(rscUri.startsWith(DBPEDIA_RSC_URISPACE)) {
				String identifier = rscUri.split("resource/")[1];//remove namespace part to encod second part to assci mode
				try {
					identifier = URLEncoder.encode(identifier,"UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				compositeLine += "(" + " <" + DBPEDIA_RSC_URISPACE+identifier + "> " + ")";
			}
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
