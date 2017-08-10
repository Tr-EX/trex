package main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.jena.atlas.web.auth.HttpAuthenticator;
import org.apache.jena.riot.RDFLanguages;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.sparql.engine.http.QueryEngineHTTP;

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
//		System.out.println(query);
//		@SuppressWarnings("resource")
//		QueryEngineHTTP engineHTTP = new QueryEngineHTTP(endpoint, query, (HttpAuthenticator) null);
//		engineHTTP.setModelContentType(RDFLanguages.strLangRDFXML);
//		Model model = engineHTTP.execConstruct();
		QueryExecution qe = QueryExecutionFactory.sparqlService(endpoint, query);
		Model model = qe.execConstruct();
//		qe.close();
		return model;
	}

	public static ResultSet runSelectQueryOnModel(String queryString, Model model) {
		Query query = QueryFactory.create(queryString);
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet resultSet = qe.execSelect();
//		qe.close();
		return resultSet;
	}

}
