package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

//import model.Dictionary;
//import model.Entity;
//import model.Movie;

;

/*
 * <?xml version="1.0" encoding="UTF-8"?>

 <dictionary xmlns="http://www.sap.com/ta/4.0" >
 <entity_category name="Outlets">

 <entity_name standard_form="#dubaimall"></entity_name>
 */

public class FileOperator {
	public static void main(String[] args) {
		new FileOperator().qutoCleaner();
	}
	// write tagged Sentences to the text file
	public void qutoCleaner() {

		try {

			InputStreamReader reader = new InputStreamReader(new FileInputStream("src/main/resources/movie-resources130k.txt"), "UTF-8");
			BufferedReader fbr = new BufferedReader(reader);			
			fbr.read();
			while (fbr.readLine() != null) {
				String uri=fbr.readLine().toString().replaceAll("\"","");
			    System.out.println(uri);
			    new FileOperator().writeToFile(uri);
			}
			fbr.close();
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
	// write tagged Sentences to the text file
	public void writeToFile(String uri) {

		try {

			OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream("src/main/resources/movie-resources.txt", true), "UTF-8");
			BufferedWriter fbw = new BufferedWriter(writer);
			fbw.write(uri);
			fbw.newLine();
			fbw.close();
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	// FILEID TA_COUNTER TA_PARAGRAPH TA_SENTENCE TA_OFFSET TA_TYPE TA_TOKEN


	// ID TITLE TYPE URL
	// TODO LANGUAGE eklenecek
	// CONTENT
}
