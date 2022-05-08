
import java.util.ArrayList;


/**
 *
 * @author Albin Hjalmas
 */
public class NetGraphNode {
    
    private String name;
    private ArrayList<NetGraphNode> neighbours;
    private boolean visited = false;
    private NetGraphNode previousVisitedNode;
    
    /**
     * Contructor.
     * @param name the desired name of this node.
     */
    public NetGraphNode(String name) {
        this.name = name;
        neighbours = new ArrayList<NetGraphNode>();
    }
    
    /**
     * Returns the name of this node.
     * @return name.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Adds node as a neighbouring node to this. 
     * @param node to add to neighbours.
     * @return true if node is not already a neighbour else false.
     */
    public boolean addNeighbour(NetGraphNode node) {
        //Return false if node is already a neighbour.
        for(NetGraphNode n : neighbours) {
            if(n.getName().equals(this.getName())) {
                return false;
            }
        }
        //Add neighbouring node, this will also be a neighbour to node.
        neighbours.add(node);
        return true;
    }
    
    /**
     * Removes a neighbour to this node by class.
     * @param node the node to remove from neighbours.
     * @return true if specified neighbour was successfully removed else false.
     */
    public boolean removeNeighbour(NetGraphNode node) {
        //Try to remove the specified neighbour from this node
        for(NetGraphNode n : neighbours) {
            if(n.getName().equals(node.getName())) {
                neighbours.remove(n);
                return true;
            }
        }
        return false;
    }
    
    /**
     * Removes a neighbour to this node by name.
     * @param name the name of the neighbour to remove
     * @return true if specified neighbour was successfully removed else false.
     */
    public boolean removeNeighbour(String name) {
        //Try to remove the specified neighbour from this node
        for(NetGraphNode n : neighbours) {
            if(n.getName().equals(name)) {
                neighbours.remove(n);
                return true;
            }
        }
        return false;
    }
    
    /**
     * Check if specified node is a neighbour to this node.
     * @param node the node to check for.
     * @return true if specified node is a neighbour else false.
     */
    public boolean isNeighbour(NetGraphNode node) {
        //Check if specified node is a neighbour
        return isNeighbour(node.getName());
    }
    
    /**
     * Check if specified node is a neighbour to this node.
     * @param name name of the node tocheck for.
     * @return true if specified node is a neighbour else false.
     */
    public boolean isNeighbour(String name) {
        //Check if specified node is a neighbour to this
        for(NetGraphNode n : neighbours) {
            if(n.getName().equals(name)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * @return true if this node has been visited else false.
     */
    public boolean isVisited() {
        return visited;
    }
    
    /**
     * Mark this node as visited.
     */
    public void visit() {
        visited = true;
    }
    
    /**
     * Mark this node as unvisited.
     */
    public void unvisit() {
        visited = false;
    }
    
    /**
     * Set the node visited prior to this.
     * @param node the node previously visited.
     */
    public void setPreviousVisitedNode(NetGraphNode node) {
        if(!node.isVisited()) {
            node.visit();
        }
        
        previousVisitedNode = node;
    }
    
    /**
     * Returns the previous visited node. if there is no previous visited node
     * a node named "DUMMY"will be returned.
     * @return the previous visited node.
     */
    public NetGraphNode getPreviousVisitedNode() {
        if(previousVisitedNode == null) {
            return new NetGraphNode("DUMMY");
        } else {
            return previousVisitedNode;
        }
    }

    /**
     * 
     * @return the list of neighbours to this node.
     */
    public ArrayList<NetGraphNode> getNeighbours() {
        return neighbours;
    }
    
    /**
     * 
     * @return The next unvisited neighbour to this node.
     */
    public NetGraphNode getUnvisitedNeighbour() {
        NetGraphNode retNode = new NetGraphNode("DUMMY");
        retNode.visit();
        if(!neighbours.isEmpty()) {
            for(NetGraphNode n : neighbours) {
                if(!n.isVisited()) {
                    retNode = n;
                    break;
                }
            }
        }
        return retNode;
    }
    
    /**
     * Checks if this node is singular i.e. doesnt have any neighbours.
     * @return true if singular else false.
     */
    public boolean isSingular() {
        return neighbours.size() == 0;
    }
    
    /**
     * Check if this node is a leaf.
     * @return true if this node is a leaf.
     */
    public boolean isLeaf() {
        return neighbours.size() == 1;
    }
    
    /**
     * Check if this node equals node.
     * @param node
     * @return true if equal else false.
     */
    public boolean equals(NetGraphNode node) {
        return name.equals(node.name);
    }
}
