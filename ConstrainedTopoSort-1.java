// Tyler Beach, NID: ty517136
// COP 3503, Fall 2022
// (DFS and Topo Sort code modified from that in the 10/12 notes)

import java.util.*;
import java.io.*;

class ConstrainedTopoSort
{
	int vertexes;
	// xFlag is for tracking in DFS whether X is encountered *after* Y
	// If X is encountered *after* Y in DFS, there's no way it can come before Y in topo sort
	boolean xFlag = false;

	ArrayList<ArrayList<Integer>> adjacencyList;

	public ConstrainedTopoSort(String filename) throws IOException
	{
		Scanner in = new Scanner(new File(filename));
		this.vertexes = in.nextInt();
		adjacencyList = new ArrayList<ArrayList<Integer>>(vertexes);
		// Setting 0th list to empty so we can use the same indexes for everything later
		adjacencyList.add(new ArrayList<Integer>());

		// For every line 1 through (vertexes):
		// Take in the first integer of the line to count how many nodes it points out to
		// Then, for every following integer on the line, create an edge from (line #) to that integer
		for (int lineCounter = 1; lineCounter <= this.vertexes; lineCounter++)
		{
			adjacencyList.add(new ArrayList<Integer>());
			int edgeCount = in.nextInt();
			for (int nodeCounter = 0; nodeCounter < edgeCount; nodeCounter++)
				addEdge(lineCounter, in.nextInt());
		}
	}

	void addEdge(int origin, int destination)
	{
		// Helper method for adding so I can throw print statements in here for debugging
		adjacencyList.get(origin).add(destination);
	}

	// The DFS code was modified from that which Dr. Szumlanski had in the 10/12 notes.
	public void depthFirstSearch(int x, int y)
	{
		this.xFlag = false;
		// + 1 because vertex #'s start at 1, this lets me use the node # as the index in the array
		boolean[] visited = new boolean[this.vertexes + 1];
		depthFirstSearch(y, visited, x);
	}

	private void depthFirstSearch(int y, boolean[] visited, int x)
	{
		// If Y does not find X in DFS, then X either leads to Y or at least can come first in topo sort
		// That's why I have flipped X and Y here. If Y doesn't find X in the search, we're good to go
		visited[y] = true;
		if (visited[x] == true)
			// This means Y leads to X and X can't possibly lead to Y then
			// If it does, then we have a cycle. If not, we still return false anyway
			xFlag = true;

		for (int vertexIndex = 1; vertexIndex <= this.vertexes; vertexIndex++)
			if (adjacencyList.get(y).contains(vertexIndex) && !visited[vertexIndex])
				depthFirstSearch(vertexIndex, visited, x);
	}

	// The topo sort code was modified from that which Dr. Szumlanski had in the 10/12 notes.
	public boolean hasConstrainedTopoSort(int x, int y)
	{
		// First we DFS to see if a path Y to X does NOT exist
		// If that path does NOT exist, then X is allowed to come before Y
		// We then check if the graph has a valid topological sort
		// If both are true, the graph has a valid topological sort where X comes before Y

		depthFirstSearch(x, y);
		if (this.xFlag == true)
			// If X was found in the Y DFS, that means Y leads to X and we're out of luck
			// We can't have a topological sort where X comes before Y if Y leads to X
			return false;

		// + 1 because vertex #'s start at 1, this lets me use the node # as the index in the array
		int[] incoming = new int[this.vertexes + 1];
		int count = 0;

		// Need to get the in-degree of each node.
		for (int originalNode = 1; originalNode <= this.vertexes; originalNode++)
		{
			int inDegree = 0;
			for (int checkingNode = 1; checkingNode <= this.vertexes; checkingNode++)
			{
				if (adjacencyList.get(checkingNode).contains(originalNode))
					inDegree++;
			}
			incoming[originalNode] = inDegree;
		}

		Queue<Integer> nodeQueue = new ArrayDeque<Integer>();
		for (int nodeCounter = 1; nodeCounter <= this.vertexes; nodeCounter++)
		{
			if (incoming[nodeCounter] == 0)
				nodeQueue.add(nodeCounter);
		}

		while (!nodeQueue.isEmpty())
		{
			int node = nodeQueue.remove();
			++count;

			for (int vertexCounter = 1; vertexCounter <= this.vertexes; vertexCounter++)
			{
				if (adjacencyList.get(node).contains(vertexCounter) && --incoming[vertexCounter] == 0)
					nodeQueue.add(vertexCounter);
			}
		}

		// If the graph is a cycle, no valid topological sort exists
		// Therefore, we can't have one where X comes before Y
		// Doing - 1 because count goes 0 to (n-1) and size is 1 to n
		if (count != adjacencyList.size() - 1)
			return false;

		// If we ever reach this point then the graph has a valid topo sort where X comes before Y
		return true;
	}

	public static double difficultyRating()
	{
		return 2;
	}

	public static double hoursSpent()
	{
		return 6;
	}
}