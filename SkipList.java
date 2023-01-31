// Tyler Beach, NID ty517136
// COP 3503, Fall 2022

import java.util.ArrayList;

class Node<AnyType>
{
	int height;
	AnyType data;
	ArrayList<Node<AnyType>> next = new ArrayList<>();

	Node(int height)
	{
		this.height = height;
		for (int levelCounter = 0; levelCounter <= height; levelCounter++)
			this.next.add(levelCounter, null);
		// Create new node with specified height, all next references should be initialized to null
	}

	Node(AnyType data, int height)
	{
		this.height = height;
		this.data = data;
		for (int levelCounter = 0; levelCounter <= height; levelCounter++)
			this.next.add(levelCounter, null);
		// Create new node with specified height, all next references should be initialized to null
		// Initializes node's value to data
	}

	public void setNext(int level, Node<AnyType> node)
	{
		this.next.add(level, node);
		// Set next at the given level within this node to the given node
	}

	public AnyType value()
	{
		return this.data;
		// O(1), returns the value stored at this node
	}

	public int height()
	{
		return this.height;
		// O(1), returns the height of this node
	}

	public void grow()
	{
		this.height++;
		this.next.add(height, null);
		// Grow this node by one level (add a null reference to the top of its next references)
	}

	public void shrink()
	{
		this.next.remove(this.height);
		this.height--;
		// Shrink this node by one level
	}

	public Node<AnyType> next(int level)
	{
		if (level < 0 || level > this.height)
			return null;
		return this.next.get(level);
		// Returns a reference to the next node in the SL at this particular level
		// Levels are numbered 0 through height from bottom to top
		// If level < 0 or level > height, return null
	}

	public void trim(int height)
	{
		int countdown = this.height;
		while (countdown != height)
		{
			this.next.set(countdown, null);
			countdown--;
		}
		this.height = height;
		// Trim a node down to the desired height
	}

	public void printNode()
	{
		System.out.printf("Node %s with value %d has height %d\n", this, this.data, this.height);
		for (int nextIndex = 0; nextIndex <= this.height(); nextIndex++)
		{
			System.out.printf("Next %s exists at level %d", this.next(nextIndex), nextIndex);
			if (this.next(nextIndex) != null)
				System.out.printf(" with value %d", this.next(nextIndex).value());
			System.out.println();
		}

		System.out.println();
		// Used while writing the code for testing
	}
}

class SkipList<AnyType extends Comparable<AnyType>>
{
	int height;
	int size;
	Node<AnyType> head;

	SkipList()
	{
		this.height = 0;
		this.size = 0;
		this.head = new Node<AnyType>(null, 0);
		// Create a SkipList
	}

	SkipList(int height)
	{
		if (height < 0)
		{
			this.head = new Node<AnyType>(null, 0);
			this.height = 0;
		}
		else
		{
			this.head = new Node<AnyType>(null, height);
			this.height = height;
		}

		this.size = 0;
		// Create a SkipList with a defined height
	}

	public int size()
	{
		return this.size;
		// Amount of nodes in the SL
	}

	public int height()
	{
		return this.height;
		// Height of the SL
	}

	public Node<AnyType> head()
	{
		return this.head;
		// The head node of the SL
	}

	public void insert(AnyType data)
	{
		// Insert a given data piece into the SL.
		// Since no height was given, it is assigned a random one.

		Node<AnyType> insNode = new Node<AnyType>(data, generateRandomHeight(getMaxHeight(this.size)) - 1);
		int originalHeight = this.height;
		int originalMaxHeight = getMaxHeight(this.size);
		if (insNode.height > this.height)
		{
			this.height++;
			this.head.grow();
		}

		// This is the fun part, where references get updated
		Node<AnyType> t = this.head;
		ArrayList<Node<AnyType>> update = new ArrayList<>();
		for (int updateIndex = 0; updateIndex <= insNode.height(); updateIndex++)
			update.add(updateIndex, null);

		for (int levelDescender = insNode.height(); levelDescender >= 0; levelDescender--)
		{
			while (t.next(levelDescender) != null && data.compareTo(t.next(levelDescender).value()) == 1)
				t = t.next(levelDescender);
			update.set(levelDescender, t);
		}

		for (int levelClimber = 0; levelClimber <= insNode.height(); levelClimber++)
		{
			insNode.next.set(levelClimber, update.get(levelClimber).next(levelClimber));
			if (levelClimber >= update.get(levelClimber).next.size())
				update.get(levelClimber).next.add(levelClimber, insNode);
			else
				update.get(levelClimber).next.set(levelClimber, insNode);
		}

		// SL size management
		this.size++;
		int newMaxHeight = getMaxHeight(this.size);
		while (this.height < getMaxHeight(this.size))
		{
			this.height = getMaxHeight(this.size);
			this.head.grow();
		}

		if (newMaxHeight > this.height)
		{
			growSkipList(newMaxHeight, newMaxHeight + 1);
			insNode.grow();
		}

		if (this.size <= 1 && this.height == 0)
			this.height = 1;
	}

	public void insert(AnyType data, int height)
	{
		// Insert a given data piece into the SL, using the given height.

		Node<AnyType> insNode = new Node<AnyType>(data, height - 1);
		int originalHeight = this.height;
		if (insNode.height > this.height)
		{
			this.height++;
			this.head.grow();
		}

		// This is the fun part, where references get updated
		Node<AnyType> t = this.head;
		ArrayList<Node<AnyType>> update = new ArrayList<>();
		for (int updateIndex = 0; updateIndex <= insNode.height(); updateIndex++)
			update.add(updateIndex, null);

		for (int levelDescender = insNode.height(); levelDescender >= 0; levelDescender--)
		{
			while (t.next(levelDescender) != null && data.compareTo(t.next(levelDescender).value()) == 1)
				t = t.next(levelDescender);
			update.set(levelDescender, t);
		}

		for (int levelClimber = 0; levelClimber <= insNode.height(); levelClimber++)
		{
			insNode.next.set(levelClimber, update.get(levelClimber).next(levelClimber));
			if (levelClimber >= update.get(levelClimber).next.size())
				update.get(levelClimber).next.add(levelClimber, insNode);
			else
				update.get(levelClimber).next.set(levelClimber, insNode);
		}

		// SL size management
		this.size++;
		int newMaxHeight = getMaxHeight(this.size);
		while (this.height < getMaxHeight(this.size))
		{
			this.height = getMaxHeight(this.size);
			this.head.grow();
		}

		if (newMaxHeight > this.height)
			growSkipList(newMaxHeight, newMaxHeight + 1);

		if (this.size <= 1 && this.height == 0)
			this.height = 1;
	}

	public void delete(AnyType data)
	{
		// Delete the earliest occurrence of data, IFF it is in the SL already.

		Node<AnyType> delNode = this.get(data);
		if (delNode != null)
		{
			System.out.printf("Deleting %d with height %d\n", delNode.value(), delNode.height());
			int originalHeight = this.height;
			int originalMaxHeight = getMaxHeight(this.size);

			Node<AnyType> t = this.head;
			ArrayList<Node<AnyType>> update = new ArrayList<>();
			for (int updateIndex = 0; updateIndex <= delNode.height(); updateIndex++)
				update.add(updateIndex, null);

			for (int levelDescender = delNode.height(); levelDescender >= 0; levelDescender--)
			{
				while (t.next(levelDescender) != null && data.compareTo(t.next(levelDescender).value()) == 1)
					t = t.next(levelDescender);
				update.set(levelDescender, t);
			}

			for (int levelClimber = 0; levelClimber <= delNode.height(); levelClimber++)
			{
				if (levelClimber >= update.get(levelClimber).next.size())
					update.get(levelClimber).next.add(levelClimber, delNode.next(levelClimber));
				else
					update.get(levelClimber).next.set(levelClimber, delNode.next(levelClimber));
			}

			this.size--;
			if (this.height > getMaxHeight(this.size))
				this.height = getMaxHeight(this.size);

			int newMaxHeight = getMaxHeight(this.size);
			if (newMaxHeight < this.height)
			{
				trimSkipList(originalMaxHeight, newMaxHeight);
				this.height = newMaxHeight;
			}
			if (this.size <= 1)
				this.height = 1;
		}
	}

	public boolean contains(AnyType data)
	{
		// Check whether data is in our SL.
		Node<AnyType> t = this.head;
		for (int levelDescender = this.height(); levelDescender >= 0; levelDescender--)
		{
			while (t.next(levelDescender) != null && data.compareTo(t.next(levelDescender).value()) <= 0)
				t = t.next(levelDescender);
		}
		return (t.value() == data);
	}

	public Node<AnyType> get(AnyType data)
	{
		// Get the reference to data in our SL, if it's there.
		Node<AnyType> t = this.head;
		for (int levelDescender = this.height(); levelDescender >= 0; levelDescender--)
		{
			while (t.next(levelDescender) != null && data.compareTo(t.next(levelDescender).value()) >= 0)
				t = t.next(levelDescender);
		}
		if (t != null)
		{
			if (data.compareTo(t.value()) == 0)
				return t;
		}
		return null;
	}

	public static int getMaxHeight(int n)
	{
		// Get the max height for an SL containing n nodes.
		if (n == 0)
			return 0;
		return (int)Math.ceil(Math.log(n) / Math.log(2));
	}

	public static int generateRandomHeight(int maxHeight)
	{
		// Generate a random height according to the given probabilities.
		int height = 1;
		int rng = (int)(Math.random() * 2);
		while (rng == 1 && height < maxHeight)
		{
			height++;
			rng = (int)(Math.random() * 2);
		}
		return height;
	}

	private void growSkipList(int oldMax, int newMax)
	{
		// Possibly grow each node of the old max height to the new.
		Node<AnyType> t = this.head.next(oldMax);
		Node<AnyType> traversal2 = this.head;
		while (t != null)
		{
			int rng = (int)(Math.random() * 2);
			if (rng == 1)
			{
				t.grow();
				if (this.head.height < newMax)
					this.head.grow();
				this.height = this.head.height;
				traversal2.next.set(newMax, t);
				traversal2 = t;
			}
			t = t.next(oldMax);
		}
	}

	private void trimSkipList(int oldMax, int newMax)
	{
		Node<AnyType> t = this.head;
		Node<AnyType> traversal2 = this.head;
		while (t != null)
		{
			traversal2 = t.next(oldMax);
			t.shrink();
			t = traversal2;
		}
		if (this.head.height > newMax)
			this.head.shrink();
		// Go through every node exceeding the max height and trim it down to the max height.
	}

	public void printSkipList()
	{
		System.out.println();
		for (int levelDescender = this.height(); levelDescender >= 0; levelDescender--)
		{
			System.out.printf("\nLEVEL %d: ", levelDescender);
			Node t = this.head.next(levelDescender);
			while (t != null)
			{
				System.out.printf("%d -> ", t.data);
				t = t.next(levelDescender);
			}
		}
		System.out.printf("\nSize is %d, height is %d\n", this.size, this.height);
		System.out.println();
		System.out.println();
		// Used for testing while creating the program.
	}
	public static double difficultyRating()
	{
		return 5;
		// This was a humbling experience.
	}

	public static double hoursSpent()
	{
		return 25;
		// I'd take the over here
	}    

}