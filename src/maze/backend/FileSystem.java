package maze.backend;

import maze.api.Maze;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

/**
 * @author Alex Giazitzis
 */
public class FileSystem {
	public static void saveToFile(Maze maze) {
		String path    = new Scanner(System.in).next();
		String mazeStr = maze.toString();
		Path   file    = Path.of(path);
		try {
			Files.write(file,mazeStr.getBytes(StandardCharsets.UTF_8));
		} catch (IOException e) {
			System.out.println("Couldn't save file.");
			e.printStackTrace();
		}
	}


	public static Maze loadFromFile() {
		String path = new Scanner(System.in).next();
		Path file = Path.of(path);
		return new Maze(file);
	}
}