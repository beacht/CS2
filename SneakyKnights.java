// Tyler Beach, NID ty517136
// COP 3503, Fall 2022

import java.util.*;
import java.lang.Math;
import java.awt.Point;

public class SneakyKnights
{
	public static boolean allTheKnightsAreSafe(ArrayList<String> coordinateStrings, int boardSize)
	{
		HashSet<Point> dangerSpaces = new HashSet<Point>();
		for (int knightIndex = 0; knightIndex < coordinateStrings.size(); knightIndex++)
		{
			Point currentKnight = stringToPoint(coordinateStrings.get(knightIndex), boardSize);

			int column = currentKnight.x; // 1 through boardSize
			int row = currentKnight.y; // 1 through boardSize

			// Using these variables to not do the math twice in add statements
			// The if statements are to prevent wrapping around the border of the board
			// Up 1, left 2
			int u1 = currentKnight.y + 1;
			int u2 = currentKnight.y + 2;
			int d1 = currentKnight.y - 1;
			int d2 = currentKnight.y - 2;
			int l1 = currentKnight.x - 1;
			int l2 = currentKnight.x - 2;
			int r1 = currentKnight.x + 1;
			int r2 = currentKnight.x + 2;

			Point u1l2 = new Point(l2, u1);
			Point u1r2 = new Point(r2, u1);
			Point u2l1 = new Point(l1, u2);
			Point u2r1 = new Point(r1, u2);
			Point d1l2 = new Point(l2, d1);
			Point d1r2 = new Point(r2, d1);
			Point d2l1 = new Point(l1, d2);
			Point d2r1 = new Point(r1, d2);        

			// Up 1, left 2
			if (logicallyEndangered(column, row, l2, u1, boardSize))
				if (dangerSpaces.contains(u1l2))
					return false;

			// Up 1, right 2
			if (logicallyEndangered(column, row, r2, u1, boardSize))
				if (dangerSpaces.contains(u1r2))
					return false;

			// Up 2, left 1
			if (logicallyEndangered(column, row, l1, u2, boardSize))
				if (dangerSpaces.contains(u2l1))
					return false;

			// Up 2, right 1
			if (logicallyEndangered(column, row, r1, u2, boardSize))
				if (dangerSpaces.contains(u2r1))
					return false;

			// Down 1, left 2
			if (logicallyEndangered(column, row, l2, d1, boardSize))
				if (dangerSpaces.contains(d1l2))
					return false;

			// Down 1, right 2
			if (logicallyEndangered(column, row, r2, d1, boardSize))
				if (dangerSpaces.contains(d1r2))
					return false;

			// Down 2, left 1
			if (logicallyEndangered(column, row, l1, d2, boardSize))
				if (dangerSpaces.contains(d2l1))
					return false;

			// Down 2, right 1
			if (logicallyEndangered(column, row, r1, d2, boardSize))
				if (dangerSpaces.contains(d2r1))
					return false;

			dangerSpaces.add(currentKnight);
		}
		return true;
	}

	public static boolean logicallyEndangered(int originColumn, int originRow, int spaceColumn, int spaceRow, int boardSize)
	{
		int columnDifference = Math.abs(originColumn - spaceColumn);
		int rowDifference = Math.abs(originRow - spaceRow);

		if (columnDifference == rowDifference)
			return false;

		if (columnDifference == 0 || rowDifference == 0)
			return false;

		if (columnDifference > 2 || rowDifference > 2)
			return false;

		return true;
	}

	public static Point stringToPoint(String input, int boardSize)
	{
		int column = 0;
		int row = 0;

		int letterCount = 0;
		int characterIndex = 0;
		// This counts the letters and is only O(k) at worst for k characters.
		while (!Character.isDigit(input.charAt(characterIndex)))
		{
			letterCount++;
			characterIndex++;
		}

		// What I'm doing here is getting the maximum power of 26 used in the letter conversion.
		// I will later decrement the power after each letter is converted.
		int baseConvPower = 1;

		// Also O(k)
		for (int powers = (letterCount - 1); powers > 0; powers--)
			baseConvPower *= 26;

		// Also O(k)
		for (characterIndex = 0; characterIndex < input.length(); characterIndex++)
		{
			char currentChar = input.charAt(characterIndex);

			// Horner's Method
			if (Character.isDigit(currentChar))
			{
				row *= 10;
				row += Character.getNumericValue(currentChar);
			}
			else
			{
				// There's a lot of math in this one line, so let me break it down.
				// First, the cast gets the ASCII of the letter.
				// Then, subtracting 96 gives us a = 1, b = 2, c = 3, etc
				// Lastly, I multiply it by whatever power of 26.
				column += ((((int)(currentChar)) - 96) * baseConvPower);

				// Decrementing the power of 26 with division.
				// I am using 26 because we are in base 26. No magic number deduction pls :(
				baseConvPower /= 26;
			}
		}
		// This gives the "number" of the space represented by the string, where a1 = 1, b1 = 2, c1 = 3, etc.
		return new Point(column, row);
	}

	public static double difficultyRating()
	{
		return 2;
	}
	public static double hoursSpent()
	{
		return 3;
	}
}
