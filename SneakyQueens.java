// Tyler Beach, NID ty517136
// COP 3503, Fall 2022

import java.util.*;

public class SneakyQueens
{
	public static boolean allTheQueensAreSafe(ArrayList<String> coordinateStrings, int boardSize)
	{
		HashMap<Integer, Integer> dangerRows = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> dangerColumns = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> dangerPositiveDiagonals = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> dangerNegativeDiagonals = new HashMap<Integer, Integer>();
		for (int queenIndex = 0; queenIndex < coordinateStrings.size(); queenIndex++)
		{
			int spaceNumber = stringToSpaceNumber(coordinateStrings.get(queenIndex), boardSize);
			int row = ((spaceNumber - 1) / boardSize) + 1; // 1 through boardSize
			int column = ((spaceNumber - 1) % boardSize) + 1; // 1 through boardSize

			// This goes to the "bottom left" space of the diagonal to find where it begins
			int boardTraveler = spaceNumber;
			int positiveDiagonal = 0;
			while (boardTraveler > boardSize && (((boardTraveler - 1) % boardSize) + 1) != 1)
				boardTraveler -= boardSize + 1;
			positiveDiagonal = boardTraveler;

			// This goes to the "bottom right" space of the diagonal to find where it begins
			boardTraveler = spaceNumber;
			int negativeDiagonal = 0;
			while (boardTraveler > boardSize && (((boardTraveler - 1) % boardSize) + 1) != boardSize)
				boardTraveler -= boardSize - 1;
			negativeDiagonal = boardTraveler;

			// The value stored at each row/column/diagonal in the above HashMaps will be
			// null if the queen's space is safe and 1 if it's in danger.
			if (dangerRows.containsKey(row))
				return false;
			else
				dangerRows.put(row, 1);

			if (dangerColumns.containsKey(column))
				return false;
			else
				dangerColumns.put(column, 1);

			if (dangerPositiveDiagonals.containsKey(positiveDiagonal))
				return false;
			else
				dangerPositiveDiagonals.put(positiveDiagonal, 1);

			if (dangerNegativeDiagonals.containsKey(negativeDiagonal))
				return false;
			else
				dangerNegativeDiagonals.put(negativeDiagonal, 1);     
		}
		return true;
	}

	public static int stringToSpaceNumber(String input, int boardSize)
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
				baseConvPower /= 26;
			}
		}
		// This gives the "number" of the space represented by the string, where a1 = 1, b1 = 2, c1 = 3, etc.
		return (row * boardSize) - (boardSize - column);
	}

	public static double difficultyRating()
	{
		return 3.5;
	}
	public static double hoursSpent()
	{
		return 15;
	}
}