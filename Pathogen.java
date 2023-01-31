// Tyler Beach, NID ty517136
// COP 3503, Fall 2022

// Modified version of Dr. Szumlanski's provided Pathogen.java; used with permission

// =============================================================================
// POSTING THIS FILE ONLINE OR DISTRIBUTING IT IN ANY WAY, IN PART OR IN WHOLE,
// IS AN ACT OF ACADEMIC MISCONDUCT AND ALSO CONSTITUTES COPYRIGHT INFRINGEMENT.
// =============================================================================


// =============================================================================
// Overview:
// =============================================================================
//
// You should modify the methods in this file to implement your backtracking
// solution for this assignment. You'll want to transform the findPaths()
// methods into the findPaths() methods required for this assignment.
//
// =============================================================================
// Disclaimer:
// =============================================================================
//
// As usual, the comments in this file are way overkill. They're intended to be
// educational (and to make this code easier for you to work with), and are not
// indicative of the kind of comments we'd use in the real world.
//
// =============================================================================
// Maze Format (2D char array):
// =============================================================================
//
// This program assumes there is exactly one person ('@') and one exit ('e') per
// maze. The initial positions of those characters may vary from maze to maze.
//
// This program assumes all mazes are rectangular (all rows have the same
// length). There are no guarantees regarding the number of walls in the maze
// or the locations of those walls. It's possible that the outer edges of the
// maze might not be made up entirely of walls (i.e., the outer edge might
// contain spaces).
//
// While there is guaranteed to be a single person ('@') and a single exit ('e')
// in a well-formed maze, there is no guarantee that there exists a path from
// the starting position of the '@' character to the exit.
//
// =============================================================================
// Example:
// =============================================================================
//
// #############
// #@# #   #   #
// #   # # # # #
// # ### # # # #
// #     #   # #
// # ##### #####
// #          e#
// #############
//
// =============================================================================
// Legend:
// =============================================================================
//
// '#' - wall (not walkable)
// '@' - person
// 'e' - exit
// ' ' - space (walkable)


import java.io.*;
import java.util.*;

public class Pathogen
{
	// Used to toggle "animated" output on and off (for debugging purposes).
	private static boolean animationEnabled = false;

	private static StringBuilder path = new StringBuilder();
	// "Animation" frame rate (frames per second).
	private static double frameRate = 4.0;

	// Setters. Note that for testing purposes you can call enableAnimation()
	// from your backtracking method's wrapper method (i.e., the first line of
	// your public findPaths() method) if you want to override the fact that the
	// test cases are disabling animation. Just don't forget to remove that
	// method call before submitting!
	public static void enableAnimation() { Pathogen.animationEnabled = true; }
	public static void disableAnimation() { Pathogen.animationEnabled = false; }
	public static void setFrameRate(double fps) { Pathogen.frameRate = fps; }

	// Maze constants.
	private static final char WALL       = '#';
	private static final char PERSON     = '@';
	private static final char CORONA     = '*';
	private static final char EXIT       = 'e';
	private static final char BREADCRUMB = '.';  // visited
	private static final char SPACE      = ' ';  // unvisited

	// Takes a 2D char maze and returns true if it can find a path from the
	// starting position to the exit. Assumes the maze is well-formed according
	// to the restrictions above.
	public static HashSet<String> findPaths(char [][] maze)
	{
		HashSet<String> rawSolutions = new HashSet<String>();
		HashSet<String> solutions = new HashSet<String>();
		int height = maze.length;
		int width = maze[0].length;

		// The visited array keeps track of visited positions. It also keeps
		// track of the exit, since the exit will be overwritten when the '@'
		// symbol covers it up in the maze[][] variable. Each cell contains one
		// of three values:
		//
		//   '.' -- visited
		//   ' ' -- unvisited
		//   'e' -- exit
		char [][] visited = new char[height][width];
		for (int i = 0; i < height; i++)
			Arrays.fill(visited[i], SPACE);

		// Find starting position (location of the '@' character).
		int startRow = -1;
		int startCol = -1;

		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				if (maze[i][j] == PERSON)
				{
					startRow = i;
					startCol = j;
				}
			}
		}

		// Let's goooooooooo!!
		rawSolutions = findPaths(maze, visited, startRow, startCol, height, width, rawSolutions);
		for (String route : rawSolutions)
			solutions.add(route.substring(0, route.length() - 1)); // Trim spaces

		return solutions;
	}

	private static HashSet<String> findPaths(char [][] maze, char [][] visited,
			int currentRow, int currentCol,
			int height, int width, HashSet<String> solutions)
	{

		// This conditional block prints the maze when a new move is made.
		if (Pathogen.animationEnabled)
			printAndWait(maze, height, width, "Searching...", Pathogen.frameRate);

		// Hoorayyyy!
		if (visited[currentRow][currentCol] == 'e')
		{
			if (Pathogen.animationEnabled)
			{
				char [] widgets = {'|', '/', '-', '\\', '|', '/', '-', '\\',
						'|', '/', '-', '\\', '|', '/', '-', '\\', '|'};

				for (int i = 0; i < widgets.length; i++)
				{
					maze[currentRow][currentCol] = widgets[i];
					printAndWait(maze, height, width, "Hooray!", 12.0);
				}

				maze[currentRow][currentCol] = PERSON;
				printAndWait(maze, height, width, "Hooray!", Pathogen.frameRate);
			}
			return null;
		}

		// Moves: left, right, up, down
		int [][] moves = new int[][] {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};

		for (int i = 0; i < moves.length; i++)
		{
			int newRow = currentRow + moves[i][0];
			int newCol = currentCol + moves[i][1];

			// Check move is in bounds, not a wall, and not marked as visited.
			if (!isLegalMove(maze, visited, newRow, newCol, height, width))
				continue;

			// Change state. Before moving the person forward in the maze, we
			// need to check whether we're overwriting the exit. If so, save the
			// exit in the visited[][] array so we can actually detect that
			// we've gotten there.
			//
			// NOTE: THIS IS OVERKILL. We could just track the exit position's
			// row and column in two int variables. However, this approach makes
			// it easier to extend our code in the event that we want to be able
			// to handle multiple exits per maze.

			if (maze[newRow][newCol] == EXIT)
				visited[newRow][newCol] = EXIT;

			maze[currentRow][currentCol] = BREADCRUMB;
			visited[currentRow][currentCol] = BREADCRUMB;
			maze[newRow][newCol] = PERSON;

			if (i == 0)
				path.append('l');
			if (i == 1)
				path.append('r');
			if (i == 2)
				path.append('u');
			if (i == 3)
				path.append('d');
			path.append(' ');

			// Perform recursive descent.
			if (findPaths(maze, visited, newRow, newCol, height, width, solutions) == null)
				solutions.add(path.toString());

			// Undo state change. Note that if we return from the previous call,
			// we know visited[newRow][newCol] did not contain the exit, and
			// therefore already contains a breadcrumb, so I haven't updated
			// that here.
			maze[currentRow][currentCol] = PERSON;
			visited[currentRow][currentCol] = SPACE;

			// Remove failed move (and its space) when backtracking
			if (path.length() >= 1)
			{
				path.deleteCharAt(path.length() - 1);
				path.deleteCharAt(path.length() - 1);
			}

			// This conditional block prints the maze when a move gets undone
			// (which is effectively another kind of move).
			if (Pathogen.animationEnabled)
				printAndWait(maze, height, width, "Backtracking...", frameRate);
		}

		return solutions;
	}

	// Returns true if moving to row and col is legal (i.e., we have not visited
	// that position before, and it's not a wall).
	private static boolean isLegalMove(char [][] maze, char [][] visited,
			int row, int col, int height, int width)
	{
		if (row < 0 || row >= height || col < 0 || col >= width)
			return false;
		if (maze[row][col] == WALL || maze[row][col] == CORONA || visited[row][col] == BREADCRUMB)
			return false;

		return true;
	}

	// This effectively pauses the program for waitTimeInSeconds seconds.
	private static void wait(double waitTimeInSeconds)
	{
		long startTime = System.nanoTime();
		long endTime = startTime + (long)(waitTimeInSeconds * 1e9);

		while (System.nanoTime() < endTime)
			;
	}

	// Prints maze and waits. frameRate is given in frames per second.
	private static void printAndWait(char [][] maze, int height, int width,
			String header, double frameRate)
	{
		if (header != null && !header.equals(""))
			System.out.println(header);

		if (height < 1 || width < 1)
			return;

		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				System.out.print(maze[i][j]);
			}

			System.out.println();
		}

		System.out.println();
		wait(1.0 / frameRate);
	}

	// Read maze from file. This function dangerously assumes the input file
	// exists and is well formatted according to the specification above.
	private static char [][] readMaze(String filename) throws IOException
	{
		Scanner in = new Scanner(new File(filename));

		int height = in.nextInt();
		int width = in.nextInt();

		char [][] maze = new char[height][];

		// After reading the integers, there's still a new line character we
		// need to do away with before we can continue.

		in.nextLine();

		for (int i = 0; i < height; i++)
		{
			// Explode out each line from the input file into a char array.
			maze[i] = in.nextLine().toCharArray();
		}

		return maze;
	}

	public static double difficultyRating()
	{
		return 3.5;
	}

	public static double hoursSpent()
	{
		return 7;
	}
}
