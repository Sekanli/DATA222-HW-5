import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
/**
 * @author Sekanli
 */

public class TreeExample {
    JFrame f;
    DefaultMutableTreeNode root;
    int POTstep;

    /**
     * A constructor to create the tree with the given data
     * Creates the tree,binds it to Jtree.
     * Binds scroll to Jtree then puts into Jframe
     * @param data  parsed data of the tree.txt
     */
    TreeExample(ArrayList<String[]> data) {
        f = new JFrame();
        root = new DefaultMutableTreeNode("Root");
        for (String[] row : data) {
            DefaultMutableTreeNode parent = root;
            for (String val : row) {
                DefaultMutableTreeNode child = null;

                for (int i = 0; i < parent.getChildCount(); i++) {
                    DefaultMutableTreeNode current = (DefaultMutableTreeNode) parent.getChildAt(i);
                    if (current.getUserObject().equals(val)) {
                        child = current; // bind the nodes
                        break;
                    }
                }

                if (child == null) {
                    child = new DefaultMutableTreeNode(val);
                    parent.add(child);
                }
                parent = child;
            }
        }
        JTree jt = new JTree(root);
        JScrollPane scrollPane = new JScrollPane(jt);
        scrollPane.setBorder(null);
        f.add(scrollPane);
        for (int i = 0; i < jt.getRowCount(); i++) {
            jt.expandRow(i);
        }
        f.setSize(300, 450);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocation(680, 200);
    
    }

    /**A public function to move a certain node to a year
     * It has multiple helper functions.
     * It first searches the tree and finds the target node
     * Then cuts the target nodes relation to its parents and removes it
     * After that it finds the destination year node and puts the target node there after multiple checks on various cases.
     * @param path
     * @param destYear
     */
    public void moveYear(String[] path, String destYear) {
        DefaultMutableTreeNode target = searchNode(path);
    
        if (target == null) {
            System.out.println("target node not found.");
            return;
        }
    
        DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) target.getParent();
        parentNode.remove(target);
        
        DefaultMutableTreeNode currentNode = findNode(root, destYear, true);
    
        for (int i = 1; i < path.length - 1; i++) {
            currentNode = findNode(currentNode, path[i], true);
        }
    
        // Check if a node with the same name exists in the destination
        DefaultMutableTreeNode exists = null;
    
        for (int i = 0; i < currentNode.getChildCount(); i++) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) currentNode.getChildAt(i);
            if (child.getUserObject().equals(target.getUserObject())) {
                exists = child;
                break;
            }
        }
    
        // If a node with the same name exists, remove it before adding the moved node
        if (exists != null) {
            currentNode.remove(exists);
            System.out.println("Overwritten existing node.");
        }
        currentNode.add(target);
    
        System.out.println("Moved node to destination year.Check the window for changes");
        deleteEmptyNodes(parentNode);
        updateJTree();
    }
    
    

    /**
     * Helper function to update the JTree and the Jframe window with the new results.
     */
    public void updateJTree() {
        JTree updatedJt = new JTree(root);
        JScrollPane scrollPane = new JScrollPane(updatedJt);
        f.getContentPane().removeAll();
        f.getContentPane().add(scrollPane);

        for (int i = 0; i < updatedJt.getRowCount(); i++) {
            updatedJt.expandRow(i);
        }

        f.revalidate();
        f.repaint();
    }

    /**A helper function to check the childs of the given parent to see if there is a match for the given nodename
     * If there is a match,bind
     * If not, create new node and bind it
     * @param parentNode The base node to check for childs
     * @param nodeName  the search target node
     * @param notFound  boolean value to keep track if the result is achieved
     * @return the new binded node
     */
    private DefaultMutableTreeNode findNode(DefaultMutableTreeNode parentNode, String nodeName, boolean notFound) {
        for (int i = 0; i < parentNode.getChildCount(); i++) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) parentNode.getChildAt(i);
            if (child.getUserObject().equals(nodeName)) {
                return child;
            }
        }
        if (notFound) {
            DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(nodeName);
            parentNode.add(newNode);
            return newNode;
        }
        return null;
    }
    

    /**Helper function to remove the empty nodes from the tree after the deletion of the child node in the Move operation
     * The parent of the target node is sent to check for any empty nodes
     * @param node
     */
    private void deleteEmptyNodes(DefaultMutableTreeNode node) {
        if (node == root || node.getChildCount() > 0) {
            return;
        }
    
        DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) node.getParent();
        parentNode.remove(node);
        deleteEmptyNodes(parentNode);
    }
    
    

    /** a helper search algorithm to detect the desired node so the move operation could happen
     * it reaches to the last element of the given path and binds it to currentNode
     * @param path a given path array with multiple or single entry
     * @return s the newly set currentNode
     */
    private DefaultMutableTreeNode searchNode(String[] path) {
        DefaultMutableTreeNode currentNode = root;

        for (String p : path) {
            boolean found = false;
            for (int i = 0; i < currentNode.getChildCount(); i++) {
                DefaultMutableTreeNode child = (DefaultMutableTreeNode) currentNode.getChildAt(i);
                if (child.getUserObject().equals(p)) {
                    currentNode = child;
                    found = true;
                    break;
                }
            }
            if (!found) {
                return null;
            }
        }

        return currentNode;
    }

    /**Helper recursive function for post order traversal
     * @param root given node to check for the target string
     * @param test the target string to be found
     * @return returns the boolean found value (default false)
     * 
     */
    private boolean traverse(DefaultMutableTreeNode root, String test) {
        //Base Case
        if (root == null) {
            return false;
        }

        boolean found = false;
        //Check every child in recursive way. If the childs contain it return true 
        for (int i = 0; i < root.getChildCount(); i++) {
            found = traverse((DefaultMutableTreeNode) root.getChildAt(i), test);
            if (found) {
                return true;
            }
        }

        POTstep++;
        System.out.println("Step " + POTstep + " -> " + root.getUserObject());
        //If the root contains it,return true
        if (root.getUserObject().equals(test)) {
            System.out.println("Match found.");
            return true;
        }

        return false;
    }

    /** A public function to search for a target string.Keeps track of the steps.
     * @param test the target string to be found
     */
    public void postOrderTraversal(String test) {
        System.out.println("Using Post-Order Traversal to find " + test + " in the tree...");
        POTstep = 0;
        boolean found = traverse(root, test);
        if (!found) {
            System.out.println("Not found!");
        }
    }

    /** Depth First Search algorithm 
     * it iterates through the tree,checks the root
     * fills the stack from left to right so when it pops its right to left
     * @param test
     */
    public void DFS(String test) {
        System.out.println("Using DFS to find " + test + " in the tree...");
        DefaultMutableTreeNode root = this.root;
        int step = 1;
        Stack<DefaultMutableTreeNode> stack = new Stack<>();
        stack.add(root);
        while (!stack.isEmpty()) {
            DefaultMutableTreeNode next = stack.pop();
            System.out.print("Step " + step + " -> ");
            System.out.print(next.getUserObject() + " \n");
            step++;

            if (next.getUserObject().equals(test)) {
                System.out.print("Match found.");
                return;
            }

            for (int i = 0; i < next.getChildCount(); i++) {
                DefaultMutableTreeNode child = (DefaultMutableTreeNode) next.getChildAt(i);
                stack.add(child);
            }

        }
        System.out.println("Not found!");

    }

    /**A breadth first search algorithm
     * First checks the roots,then its childs and then goes on 
     * checks the root by using poll and
     * adds the children to the queue
     * @param test target string
     */
    public void BFS(String test) {
        System.out.println("Using BFS to find " + test + " in the tree... ");
        DefaultMutableTreeNode root = this.root;
        int step = 1;
        Queue<DefaultMutableTreeNode> que = new LinkedList<>();
        que.add(root);
        while (!que.isEmpty()) {

            for (int i = 0; i < que.size(); i++) {
                DefaultMutableTreeNode next = que.poll();
                System.out.print("Step " + step + " -> ");
                System.out.print(next.getUserObject() + " \n");
                step++;

                if (next.getUserObject().equals(test)) {
                    System.out.print("Match found.");
                    return;
                }

                for (int k = 0; k < next.getChildCount(); k++) {
                    DefaultMutableTreeNode child = (DefaultMutableTreeNode) next.getChildAt(k);
                    que.add(child);
                }

            }

        }
        System.out.println("Not found!");
    }

}
