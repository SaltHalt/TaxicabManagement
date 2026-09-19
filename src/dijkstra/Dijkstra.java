package dijkstra;

import hashtable.Hashtable;
import main.Database;
import priority_queue.PriorityQueue;
import stack.Stack;

/**
 * Finds the paths between customers and taxicabs
 */
public class Dijkstra {

    private Database database;
    /**
     * @param database - allows the Dijkstra object to access the distances between places
     */
    public Dijkstra(Database database) {
        this.database = database;
    }
    /**
     * Generates a two-dimensional array indicating the distance between each customer and taxicab.<br>
     * Ensure that all places are connected to each other before calling this method.<br>
     * The ith row refers to the ith taxicab, when the taxicabs are ordered according to ID.<br>
     * The jth column refers to the jth customer, when the customers are ordered according to ID.
     */
    public float[][] generateDistanceArray() {
        int[] taxicabPositions = database.getListOfPositions("tblTaxicab");
        int[] customerPositions = database.getListOfPositions("tblCustomer");
        int numberOfTaxicabs = database.getSizeOfTable("tblTaxicab");
        float[][] distances = new float[numberOfTaxicabs][numberOfTaxicabs];
        for (int i = 0; i < numberOfTaxicabs; i++) {
            int taxicabPosition = taxicabPositions[i];
            Hashtable<Integer, Float> distancesFromTaxicab = getDistancesFromPlace(taxicabPosition);
            for (int j = 0; j < numberOfTaxicabs; j++) {
                int customerPosition = customerPositions[j];
                distances[i][j] = distancesFromTaxicab.item(customerPosition);
            }
        }
        return distances;
    }
    /**
     * Returns a hashtable which relates each place to the distance it is away from a specified start place.
     * Ensure all places are connected together before calling this method.
     * @param start - the ID of the start place
     */
    Hashtable<Integer, Float> getDistancesFromPlace(int start) {
        int graphSize = database.getSizeOfTable("tblPlace");
        Hashtable<Integer, Float> costs = new Hashtable(graphSize);
        PriorityQueue<Integer, Float> queue = new PriorityQueue();
        queue.add(start, (float) 0);
        while (!queue.isEmpty()) { //Dijkstra's algorithm
            int firstnode = queue.first();
            float cost = queue.priorityOf(firstnode); //gets distance of firstnode from start
            costs.add(firstnode, cost);//visits node
            int[] neighbours = database.listOfNeighbours(firstnode);
            for (int neighbour : neighbours) {
                boolean isNeighbourNotVisited = !costs.contains(neighbour);
                if (isNeighbourNotVisited) {
                    float neighbourCost = cost + database.getDistanceBetweenPlaces(firstnode, neighbour);//gets total distance from neighbour to start
                    if (queue.contains(neighbour)) {
                        if (queue.priorityOf(neighbour) > neighbourCost) {
                            queue.remove(neighbour);
                            queue.add(neighbour, neighbourCost);
                        }
                    } else {
                        queue.add(neighbour, neighbourCost);
                    }
                }
            }
            queue.pop();//move to next place to be traversed
        }
        return costs;
    }
    /**
     * Returns a list of the places that a taxicab must go through to get to a certain customer.
     * Ensure all places are connected together before calling this method.
     * @param taxicabID - the ID of the chosen taxicab
     * @param customerID - the ID of the chosen customer
     * @return An array of the IDs of each place that the taxicab goes through to reach the customer
     */
    public int[] getPathFromTaxicabToCustomer(int taxicabID, int customerID) {
        final int START = -1;
        int start = database.getPositionIDFromID("tblTaxicab", taxicabID);
        int end = database.getPositionIDFromID("tblCustomer", customerID);
        int graphSize = database.getSizeOfTable("tblPlace");
        Hashtable<Integer, Integer> previousNode = new Hashtable(graphSize); //relates each place to the place before it, in the optimal path from start to this place
        Hashtable<Integer, Float> costs = new Hashtable(graphSize);
        int[] nodes = database.getListOfIDs("tblPlace");
        for (int node : nodes) {
            previousNode.add(node, null); //all places are put into the hashtable, so only setValue needs to be used later on
        }
        previousNode.setValue(start, START);
        PriorityQueue<Integer, Float> queue = new PriorityQueue();
        queue.add(start, (float) 0);
        while (!queue.isEmpty() && !costs.contains(end)) {//stops if end is reached
            int firstnode = queue.first();
            float cost = queue.priorityOf(firstnode); //gets distance of firstnode from start
            costs.add(firstnode, cost);//visits node
            int[] neighbours = database.listOfNeighbours(firstnode);;
            for (int neighbour : neighbours) {
                boolean isNeighbourNotVisited = !costs.contains(neighbour);
                if (isNeighbourNotVisited) {
                    float neighbourCost = cost + database.getDistanceBetweenPlaces(firstnode, neighbour);//gets total distance from neighbour to start
                    if (queue.contains(neighbour)) {
                        if (queue.priorityOf(neighbour) > neighbourCost) {
                            queue.remove(neighbour);
                            previousNode.setValue(neighbour, firstnode);
                            queue.add(neighbour, neighbourCost);
                        }
                    } else {
                        previousNode.setValue(neighbour, firstnode);
                        queue.add(neighbour, neighbourCost);
                    }
                }
            }
            queue.pop(); //move to next place to be traversed
        }
        int node = end;
        //previousNode stores the path in reverse order, so a LIFO stack can be used to reverse the order
        Stack pathStack = new Stack(graphSize);
        while (node != START) {
            pathStack.push(node);
            node = previousNode.item(node);
        }
        int pathSize = pathStack.size();
        int[] path = new int[pathSize];
        for (int i = 0; i < pathSize; i++){
            path[i] = pathStack.pop();
        }
        return path;
    }
}
