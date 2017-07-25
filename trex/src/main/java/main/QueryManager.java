package main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.query.Syntax;
import com.hp.hpl.jena.rdf.model.Model;

public class QueryManager {

	public String generateQuery(String entityStr, int depth, int numberWalks) {
		String selectPart = "SELECT ?p ?o1";
		String mainPart = "{ <" + entityStr + "> ?p ?o1  ";
		String query = "";
		int lastO = 1;
		for (int i = 1; i < depth; i++) {
			mainPart += ". ?o" + i + " ?p" + i + "?o" + (i + 1);
			selectPart += " ?p" + i + "?o" + (i + 1);
			lastO = i + 1;
		}
		String lastOS = "?o" + lastO;
		query = selectPart + " WHERE " + mainPart + " . FILTER(!isLiteral(" + lastOS
				+ ")). BIND(RAND() AS ?sortKey) } ORDER BY ?sortKey LIMIT " + numberWalks;
		return query;
	}

	public void writeModel(String queryString, String endpoint, String outputFilePath) {
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

	public void runQuery(String queryString, String endpoint) {
		System.out.println("Query:" + queryString);

		Query query = QueryFactory.create(queryString, Syntax.syntaxARQ);
		QueryExecution qe = QueryExecutionFactory.sparqlService(endpoint, query);
		ResultSet results = qe.execSelect();

		ResultSetFormatter.out(System.out, results, query);
		// iterate all municipal districts
		while (results.hasNext()) {

			QuerySolution result = results.next();
			if (result.get("?o").isLiteral())
				System.out.println(result.get("?o").asLiteral().getLanguage());
			result.toString();
		}

		// Important - free up resources used running the query
		qe.close();
	}

}
