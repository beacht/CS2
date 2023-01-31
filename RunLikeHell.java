// Tyler Beach, NID: ty517136
// COP 3503, Fall 2022

import java.util.*;
import java.io.*;

public class RunLikeHell
{
	public static final int UNINITIALIZED = -1;

	public static int maxGain(int[] blocks)
	{
		// Using an (n x 2) array. I was alternating true/false in the
		// original recursive solution to track whether I could jump.
		// maxGain(blocks, indexYoureAt, true) -> matrix[idx][0]
		// and maxGain(blocks, indexYoureAt, true) -> matrix[idx][1].
		// Should I have made true correspond to 1 and false to 0?
		// Probably. Does it matter? Nah.
		int[][] matrix = new int[blocks.length][2];
		for (int i = 0; i < blocks.length; i++)
		{
			matrix[i][0] = UNINITIALIZED;
			matrix[i][1] = UNINITIALIZED;
		}

		// The bottom left corner of our matrix from which we build out.
		// You can picture n rows (indexed 0 through n-1) with 2 columns.
		// True was the left column and false was the right.
		matrix[blocks.length - 1][0] = 0;
		// Bottom right is just last block value
		matrix[blocks.length - 1][1] = blocks[blocks.length - 1];

		// Minus 2 so we start at row n-2. We already know what the values
		// in row n-1 are, so we can start at the next row to save a little time.
		// In case it isn't clear by now, I am doing rows 0-(n-1) rather than 1-n.
		for (int i = blocks.length - 2; i >= 0; i--)
		{
			matrix[i][0] = Math.max(matrix[i + 1][0], matrix[i + 1][1]);
			matrix[i][1] = matrix[i + 1][0] + blocks[i];
		}

		// Ultimately, we return the best possible value from either hitting or not
		// hitting the very first block.
		return Math.max(matrix[0][0], matrix[0][1]);
	}

	public static double difficultyRating()
	{
		return 1.5;
	}

	public static double hoursSpent()
	{
		return 1.5;
	}
}
