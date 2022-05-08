
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *
 * @author Albin Hjalmas
 */
public class LinkProbabilityMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        //Build the graph
        NetGraph g = new NetGraph();
        g.addNode("A");
        g.addNode("B");
        g.addNode("C");
        g.addNode("D");
        g.addNode("E");
        
        g.makeLink("A", "B");
        g.makeLink("A", "C");
        g.makeLink("B", "D");
        g.makeLink("C", "D");
        g.makeLink("C", "E");
        g.makeLink("E", "D");
        
        //Print program header
        System.out.println("*********************************************************");
        System.out.println("*                Link Probality                         *");
        System.out.println("*********************************************************");
        
        Scanner scan = new Scanner(System.in);
        
        double p = -1.00;
        int T = -1;
  
        //Get the probability from user input. example input: 0,65
        do {
            System.out.print("Please specify the probability for link failure: ");
            if(!scan.hasNextDouble()) {
                scan.next();
                continue;
            }
            p = scan.nextDouble();
        } while (p <= 0.0 || p > 1.00);
        
        //Get the number of experiments from user input.
        do {
            System.out.print("Please specify the number of experiments: ");
            if(!scan.hasNextInt()) {
                scan.next();
                continue;
            }
            T = scan.nextInt();
        } while (T <= 0);
        
        System.out.println("*********************************************************");
        double p2 = p/(double)T;
        DecimalFormat df = new DecimalFormat("#.###");
        df.setRoundingMode(RoundingMode.CEILING);
        for(int i=1; i<=T; i++, p2+=p/(double)T) {
            System.out.println(
                    String.format("%-15s %-15s %-15s",
                            "T = " + i, "p = " + df.format(p2), "P[B->C] = " + df.format(recLinkProb(g.copy(), p2, "B", "C"))));
        }
        
    }
    
    /**
     * Recursively calculates the linkprobability between two specified 
     * nodes in the graph g.
     * @param g graph containing interconnected nodes.
     * @param p the probability of link failure.
     * @param srcNode source node
     * @param dstNode destination node
     * @return the probability for link between srsNode and dstNode
     */
    public static double recLinkProb(NetGraph g, double p, String srcNode, String dstNode) {
        NetGraphNode src = g.getNodeFromName(srcNode);
        NetGraphNode dst = g.getNodeFromName(dstNode);
        
        if(srcNode.equals(dstNode)) {
            return 1.00;
        } else if(!g.containsNode(src) || !g.containsNode(dst)) {
            return 0.00;
        } else {
            src.visit(); //Visit the current node
            NetGraphNode nextNode = src.getUnvisitedNeighbour(); //Get the next node to move to
            nextNode.setPreviousVisitedNode(src); //Set current node to be nextNodes previously visited node.
            NetGraph nextG = g.copy(); //Copy original graph
            nextG.breakLink(src.getName(), nextNode.getName()); //Break link between current node and next node.
            return (1-p)*next(g, p, nextNode.getName(), dstNode) + p*next(nextG, p, srcNode, dstNode);
        }
    }
    
    private static double next(NetGraph g, double p, String currNode, String dstNode) {
        //Get actual NetGraphNode instances
        NetGraphNode curr = g.getNodeFromName(currNode);
        NetGraphNode dst = g.getNodeFromName(dstNode);
        
        //Visit current node
        curr.visit(); 
        
        //Check if we have reached the destination
        if(curr.equals(dst)) {
            return 1;
        }
        
        //Check if current node is singular, if so, abort
        if(curr.isSingular()) {
            return 0.00;
        }
        
        NetGraphNode nextNode = curr.getUnvisitedNeighbour(); //Get the node to move to
        nextNode.setPreviousVisitedNode(curr); //Set current node to be nextNodes previously visited node.
        NetGraph nextG = g.copy(); //Copy original graph
        nextG.breakLink(curr.getName(), nextNode.getName()); //Break link between current node and next node.
        
        //Check if current node is a leaf & previous node is visited if so, 
        // move back to previous node & delete link.
        NetGraphNode prev = curr.getPreviousVisitedNode();
        if(curr.isLeaf() && prev.isVisited() || nextNode.getName().equals("DUMMY")) {
            NetGraph newG = g.copy();
            newG.breakLink(curr, prev);
            return next(newG, p, prev.getName(), dstNode);
        }
        
        return (1-p)*next(g, p, nextNode.getName(), dstNode) + p*next(nextG, p, currNode, dstNode);
    }
}
