package main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileReader {
	public static List<String> readLines(String fileName) {
		List<String> lines = new ArrayList<String>();

		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {

			lines = stream.collect(Collectors.toList());

		} catch (IOException e) {
			e.printStackTrace();
		}

		return lines;
	}
}
