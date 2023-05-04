
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


import java.util.ArrayList;


public class TestMain{

    public static void main(String[] args){
        ArrayList<String[]> data = readFile("tree.txt");
        TreeExample tree = new TreeExample(data);

        Scanner scanner = new Scanner(System.in);
        System.out.println("\nEnter the node to search for BFS");
        String node = scanner.nextLine();
        tree.BFS(node);
        System.out.println("\nEnter the node to search for DFS");
        String node1 = scanner.nextLine();
        tree.DFS(node1);
        System.out.println("\nEnter the node to search for Post-Order Traversal");
        String node2 = scanner.nextLine();
        tree.postOrderTraversal(node2);
   
        while(true){
        System.out.println("\nEnter x to exit(or close the window).\nEnter the source path (comma-separated): ");
        String sourcePathStr = scanner.nextLine();
        if(sourcePathStr.equals("x"))
        {
            
            scanner.close();
            System.exit(0);
        }
        String[] sourcePath = sourcePathStr.split(",");
        System.out.println("\nEnter the destination year: ");
        String destYear = scanner.nextLine();
        
       
        if(destYear.equals("x"))
        {
            
            scanner.close();
            System.exit(0);
        }
        tree.moveYear(sourcePath, destYear);
        
        }
    }

    


    public static ArrayList<String[]> readFile(String filename) {
        File file = new File(filename);
        ArrayList<String[]> lines = new ArrayList<>();
    
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(";");
                lines.add(parts);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    
        return lines;
    }

}

