
import java.util.ArrayList;


/**
 *
 * @author Albin Hjalmas
 */
public class NetGraph {
    
    private ArrayList<NetGraphNode> nodes = new ArrayList<NetGraphNode>();
    
    /**
     * Adds a new node to this graph. don't forget to call makeLink to
     * define a connection between nodes.
     * @param node
     * @return true if node has unique name else false
     */
    public boolean addNode(String name) {
        NetGraphNode newNode = new NetGraphNode(name);
        
        //Check if node.name() is unique.
        for(NetGraphNode n : nodes) {
            if(n.getName().equals(name)) {
                return false;
            }
        }
        
        //node.getName() was unique so add to list of nodes.
        this.nodes.add(newNode);
        
        return true;
    }
    
    public boolean addNode(NetGraphNode node) {
        if(!nodes.contains(node)) {
            nodes.add(node);
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Creates a link between 2 nodes in the NetGraph.
     * @param a node a
     * @param b node b
     * @return true if connection is possible else false.
     */
    public boolean makeLink(String a, String b) {
        NetGraphNode nodeA = null;
        NetGraphNode nodeB = null;
        
        //Find nodes from given names
        for(NetGraphNode n : nodes) {
            if(n.getName().equals(a)) {
                nodeA = n;
            } else if(n.getName().equals(b)) {
                nodeB = n;
            }
        }
        
        //Find errors
        if(nodeA == null || nodeB == null || a.equals(b)) {
            return false;
        }
        
        //Check if nodeA and nodeB already is connected
        for(NetGraphNode n : nodeA.getNeighbours()) {
            if(n.getName().equals(nodeB.getName())) {
                return false;
            }
        }
        
        //Connect node A and B.
        nodeA.addNeighbour(nodeB);
        nodeB.addNeighbour(nodeA);
        
        return true;
    }
    
    /**
     * Creates a link between 2 nodes in the NetGraph.
     * @param node1
     * @param node2
     * @return true if connection is possible else false.
     */
    public boolean makeLink(NetGraphNode node1, NetGraphNode node2) {
        return makeLink(node1.getName(), node2.getName());
    }
    
    /**
     * Breaks a link between nodes
     * @param a name of first node
     * @param b name of second node
     * @return true if link is successfully broken else false.
     */
    public boolean breakLink(String a, String b) {
        NetGraphNode nodeA = null;
        NetGraphNode nodeB = null;
        for(NetGraphNode n : nodes) {
            if(n.getName().equals(a)) {
                nodeA = n;
            } else if(n.getName().equals(b)) {
                nodeB = n;
            }
        }
        
        if(nodeA == null || nodeB == null || a.equals(b)) {
            return false;
        }
        
        //Remove link between nodes
        nodeA.removeNeighbour(nodeB);
        nodeB.removeNeighbour(nodeA);
        
        return true;
    }
    
    /**
     * Breaks a link between nodes n1 & n2.
     * @param n1
     * @param n2
     * @return true if link is broken else false.
     */
    public boolean breakLink(NetGraphNode n1, NetGraphNode n2) {
        return breakLink(n1.getName(), n2.getName());
    }
    
    @Override
    public String toString() {
        String str = new String();
        
        for(NetGraphNode n : nodes) {
            str += "Node: " + n.getName() + "\tNeighbours: ";
            for(NetGraphNode neighbour : n.getNeighbours()) {
                str += neighbour.getName() + " ";
            }
            str += "\n";
        }
        
        return str;
    }
    
    /**
     * Recursively prints the names of the nodes and acompanying neighbours.
     * @return list of the nodes contained in this graph and their neighbours.
     */
    public String recToString() {
        if(nodes.size() == 0) {
            return "";
        } else {
            return recToString2(nodes.get(0));
        }
    }
    
    /**
     * Static helper function to "recToString"
     * @param node current node.
     * @return list of the nodes contained in this graph and their neighbours.
     */
    private static String recToString2(NetGraphNode node) {
        if(node.isVisited()) {
            return "";
        } else {
            node.visit();
            String neighbours = " Neighbours = ";
            for(NetGraphNode n : node.getNeighbours()) {
                neighbours += n.getName() + " ";
            }
            return "Node = " + node.getName() + "\t" + neighbours + 
                    "\n" + recToString2(node.getUnvisitedNeighbour());
        }
    }
    
    public boolean containsNode(String name) {
        for(NetGraphNode node : nodes) {
            if(node.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean containsNode(NetGraphNode node) {
        return nodes.contains(node);
    }
    
    /**
     * 
     * @param name
     * @return 
     */
    public NetGraphNode getNodeFromName(String name) {
        for(NetGraphNode node : nodes) {
            if(node.getName().equals(name)) {
                return node;
            }
        }
        
        return new NetGraphNode("DUMMY");
    }
    
    public NetGraph copy() {
        NetGraph newG = new NetGraph();
        for(NetGraphNode node : nodes) {
            NetGraphNode tmp = new NetGraphNode(node.getName());
            if(node.isVisited()) {
                tmp.visit();
            }
            newG.addNode(tmp);
        }
        
        //Rebuild links in new graph
        for(int i=0; i<nodes.size(); i++) {
            ArrayList<NetGraphNode> srcList = nodes.get(i).getNeighbours();
            NetGraphNode currNode = newG.nodes.get(i);

            for(NetGraphNode srcNeighbour : srcList) {
                for(NetGraphNode tmp : newG.nodes) {
                    if(tmp.getName().equals(srcNeighbour.getName())) {
                        newG.makeLink(currNode, tmp);
                        break;
                    }
                }
            }
        }
        
        for(NetGraphNode node : nodes) {
            if(!node.getPreviousVisitedNode().getName().equals("DUMMY")) {
                String prev = node.getPreviousVisitedNode().getName();
                for(NetGraphNode n1 : newG.nodes) {
                    if(n1.getName().equals(node.getName())) {
                        for(NetGraphNode n2 : newG.nodes) {
                            if(n2.getName().equals(prev)) {
                                n1.setPreviousVisitedNode(n2);
                            }
                        }
                    }
                }
            }
        }
        
        return newG;
    }
}
