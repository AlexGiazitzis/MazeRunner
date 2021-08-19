package maze.api;

import java.util.*;

/**
 * @author Alex Giazitzis
 */
public class Menu implements Runnable {
	private static final Map<String, Runnable> menuList   = new HashMap<>();
	private static final List<String>          menuText   = new ArrayList<>();
	private static final Scanner               scanner    = new Scanner(System.in);
	private static       boolean               exit       = false;
	private static       boolean               hasRunOnce = false;

	public static boolean hasRunOnce() {
		return hasRunOnce;
	}

	public static void show() {
		exit = false;


		while (true) {
			printMenu();
			int choice = getInput();
			if (runChoice(choice)) {
				hasRunOnce = true;
				break;
			}
		}
	}

	public static void addMenuOption(String text, Runnable method) {
		menuText.add(text);
		menuList.put(text, method);
	}

	private static boolean runChoice(int choice) {
		Optional<String> key = menuText.stream()
		                               .filter(s -> s.startsWith(String.valueOf(choice)))
		                               .findFirst();
		if (key.isPresent()) {
			menuList.get(key.get())
			        .run();
			return true;
		} else {
			System.out.println("Incorrect option. Please try again");
			return false;
		}
	}

	private static void printMenu() {
		menuText.forEach(System.out::println);
	}

	public static void clear() {
		menuList.clear();
		menuText.clear();
	}

	private static int getInput() {
		int input = -1;
		try {
			input = scanner.nextInt();
		} catch (InputMismatchException e) {
			System.out.println("Incorrect option. Please try again");
		}
		return input;
	}

	public static boolean exit() {
		new Menu().run();
		return exit;
	}

	@Override
	public void run() {
		exit = true;
	}
}