/**
 * ScapeGoat Tree class
 *
 * This class contains some of the basic code for implementing a ScapeGoat tree.
 * This version does not include any of the functionality for choosing which node
 * to scapegoat.  It includes only code for inserting a node, and the code for rebuilding
 * a subtree.
 */

public class SGTree {

    // Designates which child in a binary tree
    enum Child {LEFT, RIGHT}

    /**
     * TreeNode class.
     *
     * This class holds the data for a node in a binary tree.
     *
     * Note: we have made things public here to facilitate problem set grading/testing.
     * In general, making everything public like this is a bad idea!
     *
     */
    public static class TreeNode {
        int key;
        int weight;
        public TreeNode left = null;
        public TreeNode right = null;

        TreeNode(int k) {
            key = k;
            // initialise weight as 1
            weight = 1;
        }
    }

    // Root of the binary tree
    public TreeNode root = null;

    /**
     * Count the number of nodes in the specified subtree
     *
     * @param node  the parent node, not to be counted
     * @param child the specified subtree
     * @return number of nodes
     */
    public int countNodes(TreeNode node, Child child) {
        // TODO: Implement this
        return (child == Child.LEFT)
                ? count(node.left)
                : count(node.right);
    }

    /**
     * Recursively count all the nodes in a tree
     *
     * @param tree the input tree
     * @return number of all the nodes in the input tree
     */
    public int count(TreeNode tree) {
        return (tree == null)
                ? 0
                : 1 + count(tree.left) + count(tree.right);
    }

    /**
     * Build an array of nodes in the specified subtree.
     *
     * @param node  the parent node, not to be included in returned array
     * @param child the specified subtree
     * @return array of nodes
     */
    TreeNode[] enumerateNodes(TreeNode node, Child child) {
        // TODO: Implement this
        result = new TreeNode[countNodes(node, child)];
        pointer = 0;
        if (child == Child.LEFT) {
            inOrder(node.left);
        } else {
            inOrder(node.right);
        }
        return result;

    }

    /**
     * Traverse a tree inorder
     *
     * @param tree the input tree
     * @return an array of nodes of the inorder traversal of the input tree
     */
    public TreeNode[] result;
    private int pointer;
    private void inOrder(TreeNode tree) {
        if (tree != null) {
            inOrder(tree.left);
            result[pointer] = tree;
            pointer++;
            inOrder(tree.right);
        }
    }

    /**
     * Builds a tree from the list of nodes Returns the node that is the new root of the subtree
     *
     * @param nodeList ordered array of nodes
     * @return the new root node
     */
    TreeNode buildTree(TreeNode[] nodeList) {
        // TODO: Implement this
        return helper(nodeList, 0, nodeList.length - 1);
    }

    /**
     * Idea is to use begin and end pointers similar to binary search
     * instead of copying the array.
     * @param nodeList takes in nodeList from buildTree
     * @param begin array begin pointer
     * @param end array end pointer
     * @return new balanced tree about the root node
     */
    TreeNode helper(TreeNode[] nodeList, int begin, int end) {
        if (begin > end) {
            return null;
        }
        int mid = begin + (end-begin)/2;
        TreeNode root = nodeList[mid];
        root.left = helper(nodeList, begin, mid - 1);
        root.right = helper(nodeList, mid + 1, end);
        return root;
    }

    /**
     * Determines if a node is balanced.  If the node is balanced, this should return true.  Otherwise, it should return false.
     * A node is unbalanced if either of its children has weight greather than 2/3 of its weight.
     *
     * @param node a node to check balance on
     * @return true if the node is balanced, false otherwise
     */
    public boolean checkBalance(TreeNode node) {
        // Empty tree
        if (node == null) {
            return true;
        // first node without children.
        // will be initialised as weight 1
        } else if (node.weight == 1) {
            return true;
        // if left tree has no children
        } else if (node.left == null) {
            return node.right.weight <= node.weight * (2.0 / 3);
        // if right tree has no children
        } else if (node.right == null) {
            return node.left.weight <= node.weight * (2.0 / 3);
        } else {
            return node.left.weight <= node.weight * (2.0 / 3) &&
                    node.right.weight <= node.weight * (2.0 / 3);
        }
    }

    /**
    * Rebuild the specified subtree of a node.
    * 
    * @param node the part of the subtree to rebuild
    * @param child specifies which child is the root of the subtree to rebuild
    */
    public void rebuild(TreeNode node, Child child) {
        // Error checking: cannot rebuild null tree
        if (node == null) return;
        // First, retrieve a list of all the nodes of the subtree rooted at child
        TreeNode[] nodeList = enumerateNodes(node, child);
        // Then, build a new subtree from that list
        TreeNode newChild = buildTree(nodeList);
        // Finally, replace the specified child with the new subtree
        if (child == Child.LEFT) {
            node.left = newChild;
            fixWeights(node, Child.LEFT);
        } else if (child == Child.RIGHT) {
            node.right = newChild;
            fixWeights(node, Child.RIGHT);
        }
    }

    /**
    * Insert a key into the tree
    *
    * @param key the key to insert
    */
    public void insert(int key) {
        // if root is null, become root
        if (root == null) {
            root = new TreeNode(key);
            return;
        }

        TreeNode node = root;

        // if root is not null, recurse till leaf node
        // every node that is traversed past, add one to its weight
        while (true) {
            if (key <= node.key) {
                if (node.left == null) break;
                node.weight++;
                // after updating height, check if balanced
                // if not balanced, rebuild subtree
                if (! checkBalance(node)) {
                    rebuild(node, Child.LEFT);
                }
                node = node.left;
            } else {
                if (node.right == null) break;
                node.weight++;
                // after updating height, check if balanced
                // if not balanced, rebuild subtree
                if (! checkBalance(node)) {
                    rebuild(node, Child.RIGHT);
                }
                node = node.right;
            }
        }

        // insert new node to leaf node
        if (key <= node.key) {
            node.left = new TreeNode(key);
        } else {
            node.right = new TreeNode(key);
        }
    }

    /**
     * update the weight of all nodes in the specified subtree
     * @param u the parent node of the subtree that needs its weight updated
     * @param child the specified subtree
     */
    public void fixWeights(TreeNode u, Child child) {
        if (child == Child.LEFT) {
            fixWeightsHelper(u.left);
        } else {
            fixWeightsHelper(u.right);
        }
    }

    public void fixWeightsHelper(TreeNode u) {
        if (u != null) {
            u.weight = 1 + countNodes(u, Child.LEFT) + countNodes(u, Child.RIGHT);
            fixWeightsHelper(u.left);
            fixWeightsHelper(u.right);
        }
    }

    // Simple main function for debugging purposes
    public static void main(String[] args) {
        SGTree tree = new SGTree();
        for (int i = 0; i < 100; i++) {
            tree.insert(i);
        }
        tree.rebuild(tree.root, Child.RIGHT);
    }
}
