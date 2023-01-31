// Tyler Beach, NID: ty517136
// COP 3503, Fall 2022
// ====================
// GenericBST: GenericBST.java
// ====================

import java.io.*;
import java.util.*;

// Node class, modified to take any datatype that extends Comparable instead of only integers.
class Node<AnyType>
{
	AnyType data;
	Node<AnyType> left, right;

	Node(AnyType data)
	{
		this.data = data;
	}
}

// Our GenericBST can work with any datatype passed to it, as long as it extends Comparable.
// This is so we are able to correctly insert, delete, and search following standard BST rules.
public class GenericBST<AnyType extends Comparable<AnyType>>
{
	private Node<AnyType> root;

	public void insert(AnyType data)
	{
		root = insert(root, data);
	}

	// The compareTo if statements take advantage of the fact that AnyType extends Comparable.
	// This is because our data may not be an integer, where we can simply use < or > to compare.
	// The method returns the root node in whatever type we are using, after performing a BST insertion.
	private Node<AnyType> insert(Node<AnyType> root, AnyType data)
	{
		if (root == null)
		{
			return new Node<AnyType>(data);
		}
		else if (data.compareTo(root.data) < 0)
		{
			root.left = insert(root.left, data);
		}
		else if (data.compareTo(root.data) > 0)
		{
			root.right = insert(root.right, data);
		}

		return root;
	}

	public void delete(AnyType data)
	{
		root = delete(root, data);
	}

	// As with the insertion method, we again use compareTo for comparing data of type AnyType.
	// A root node of type AnyType is returned, like with insertion, after performing a BST deletion.
	private Node<AnyType> delete(Node<AnyType> root, AnyType data)
	{
		if (root == null)
		{
			return null;
		}
		else if (data.compareTo(root.data) < 0)
		{
			root.left = delete(root.left, data);
		}
		else if (data.compareTo(root.data) > 0)
		{
			root.right = delete(root.right, data);
		}
		else
		{
			if (root.left == null && root.right == null)
			{
				return null;
			}
			else if (root.left == null)
			{
				return root.right;
			}
			else if (root.right == null)
			{
				return root.left;
			}
			else
			{
				root.data = findMax(root.left);
				root.left = delete(root.left, root.data);
			}
		}

		return root;
	}

	// In our GenericBST version, we return the tree's maximum data of type AnyType instead of int.
	private AnyType findMax(Node<AnyType> root)
	{
		while (root.right != null)
		{
			root = root.right;
		}

		return root.data;
	}

	// We continue to use boolean rather than AnyType.
	// This is because we are simply checking whether "data" is in our BST.
	// We are not looking to return said data, just a "yes" or "no".
	public boolean contains(AnyType data)
	{
		return contains(root, data);
	}

	// Once again, compareTo lets us compare whatever type we are using for our BST.
	// Throughout the program, the original integer parameters and returns have been replaced with AnyType.
	// This is so all methods can uniformly work with whatever type we are given, instead of only integers.
	private boolean contains(Node<AnyType> root, AnyType data)
	{
		if (root == null)
		{
			return false;
		}
		else if (data.compareTo(root.data) < 0)
		{
			return contains(root.left, data);
		}
		else if (data.compareTo(root.data) > 0)
		{
			return contains(root.right, data);
		}
		else
		{
			return true;
		}
	}

	// The following methods are standard BST inorder, preorder, and postorder traversals.
	// They need not be modified, since we are simply printing the given data.
	public void inorder()
	{
		System.out.print("In-order Traversal:");
		inorder(root);
		System.out.println();
	}

	private void inorder(Node root)
	{
		if (root == null)
			return;

		inorder(root.left);
		System.out.print(" " + root.data);
		inorder(root.right);
	}

	public void preorder()
	{
		System.out.print("Pre-order Traversal:");
		preorder(root);
		System.out.println();
	}

	private void preorder(Node root)
	{
		if (root == null)
			return;

		System.out.print(" " + root.data);
		preorder(root.left);
		preorder(root.right);
	}

	public void postorder()
	{
		System.out.print("Post-order Traversal:");
		postorder(root);
		System.out.println();
	}

	private void postorder(Node root)
	{
		if (root == null)
			return;

		postorder(root.left);
		postorder(root.right);
		System.out.print(" " + root.data);
	}

	public static double difficultyRating()
	{
		return 2;
	}

	// (See note in Webcourses submission)
	public static double hoursSpent()
	{
		return 10;
	}
}
