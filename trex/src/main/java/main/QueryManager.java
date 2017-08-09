package main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.sparql.resultset.RDFOutput;

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

	public static Model runQuery(String queryString, String endpoint) {
		Query query = QueryFactory.create(queryString);
		System.out.println(query);
		QueryExecution qe = QueryExecutionFactory.sparqlService(endpoint, query);
		// ResultSet resultSet = qe.execSelect();
		// ResultSetFormatter.out(resultSet);
		return qe.execConstruct();
	}

}
