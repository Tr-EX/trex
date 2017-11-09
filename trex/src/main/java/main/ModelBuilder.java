package main;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class ModelBuilder {

	public static Model buildModel(List<String> rscUriList, List<String> prpUriList) {
		if (rscUriList.size() > 1000) {
			Model model = ModelFactory.createDefaultModel();
			int startIndex = 0;
			int remainingSize = rscUriList.size();
			while (remainingSize >= 1000) {
				int endIndex = findEndIndex(rscUriList, startIndex, remainingSize);
				String query = QueryManager.buildQuery(rscUriList, prpUriList, Constants.CONSTRUCT_S_P_O,
						Constants.S_P_O, Constants.SUBJECT_VAR_NAME, startIndex, endIndex);
				System.out.println("query from movies main model in ModelBuilder.buildModel() line 19:\n"+query);
				try {
					TimeUnit.SECONDS.sleep(15);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				model.add(QueryManager.runConstructQuery(query, Constants.DBPEDIA_ENDPOINT));
				startIndex += 1000;
				remainingSize -= 1000;
			}
			return model;
		} else {
			String query = QueryManager.buildQuery(rscUriList, prpUriList, Constants.CONSTRUCT_S_P_O, Constants.S_P_O,
					Constants.SUBJECT_VAR_NAME, 0, rscUriList.size());
			return QueryManager.runConstructQuery(query, Constants.DBPEDIA_ENDPOINT);
		}
	}

	private static int findEndIndex(List<String> rscUriList, int startIndex, int remainingSize) {
		int endIndex = 0;
		if (remainingSize >= 1000) {
			endIndex = startIndex + 1000;
		} else {
			endIndex = rscUriList.size();
		}
		return endIndex;
	}

}
