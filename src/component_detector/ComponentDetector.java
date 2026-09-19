package component_detector;

import java.util.ArrayList;
import main.Database;
import stack.Stack;
/**
 * Generates a list of disconnected components in the graph
 */
public class ComponentDetector {
    private ArrayList<Integer> unvisitedNodes;
    private String[][] components;
    private Database graph;
    private final int ITEM_NOT_FOUND = -1;
    /**
     * Allows JUnit test to instantiate ComponentDetector without calling the generateListOfDisconnectedComponets()
     */
    ComponentDetector(){
    }
    /**
     * This constructor will also generate a list of disconnected components which can be retrieved via methods.
     * @param database - Allows object to find out which places are neighbours
     */
    public ComponentDetector(Database database) {
        graph = database;
        generateListOfDisconnectedComponents();
    }
    /**
     * Initialises a private member variable which stores a list of components
     */
    private void generateListOfDisconnectedComponents() {
        int[] nodes = graph.getListOfIDs("tblPlace");
        unvisitedNodes = new ArrayList<>();
        for (int node : nodes) {
            unvisitedNodes.add(node);
        }
        ArrayList<String[]> listOfComponents = new ArrayList<>();
        while (!unvisitedNodes.isEmpty()) {//find all components
            int startNode = unvisitedNodes.get(0);
            String[] component = traverseComponent(startNode);
            listOfComponents.add(component);
        }
        components = new String[listOfComponents.size()][];
        for(int i = 0; i < listOfComponents.size(); i++){
            components[i] = listOfComponents.get(i);
        }
    }
    /**
     * Fully traverses a component, whlist updating unviistedNodes
     * @param startNode the first node inside the component to be traversed
     * @return an array with the postcodes of each in place in the component
     */
    private String[] traverseComponent(int startNode){
        ArrayList<String> component = new ArrayList<>();
        Stack nodesToTraverse = new Stack(unvisitedNodes.size());
        nodesToTraverse.push(startNode);
        while (!nodesToTraverse.isEmpty()) {//traverse a component completely
            int currentNode = nodesToTraverse.pop();
            int index = binarySearch(unvisitedNodes, currentNode);
            if (index != ITEM_NOT_FOUND) {
                unvisitedNodes.remove(index);
                component.add(graph.getStringValueFromID("Postcode", "tblPlace", currentNode));
                int[] neighbours = graph.listOfNeighbours(currentNode);
                for (int neighbour : neighbours) {
                    boolean isUnvisited = (binarySearch(unvisitedNodes, neighbour) != ITEM_NOT_FOUND);
                    if (isUnvisited) {
                        nodesToTraverse.push(neighbour);
                    }
                }
            }
        }
        String[] componentArray = new String[component.size()];
        for(int i = 0; i < component.size(); i++){
            componentArray[i] = component.get(i);
        }
        return componentArray;
    }
    /**
     * @return A list of components
     */
    public String[][] getComponents() {
        return components;
    }
    /**
     * Returns whether or not the graph is connected together
     */
    public boolean isConnected() {
        return components.length == 1;
    }
    /**
     * Finds the position of a certain item in an ordered array.
     * If the item isn't found, ITEM_NOT_FOUND is returned.
     */
    int binarySearch(ArrayList<Integer> array, int item) {
        int indexItem = ITEM_NOT_FOUND;
        int middleIndex = array.size() / 2;
        if (!array.isEmpty()) {
            int middleItem = (int) array.get(middleIndex);
            if (item == middleItem) {
                indexItem = middleIndex;
            } else if (item < middleItem) {
                ArrayList<Integer> firstHalfList = new ArrayList<>(array.subList(0, middleIndex));
                indexItem = binarySearch(firstHalfList, item);//if ITEM_NOT_FOUND, then indexItem is assigned ITEM_NOT_FOUND
            } else {
                ArrayList<Integer> secondHalfList = new ArrayList<>(array.subList(middleIndex + 1, array.size()));
                int indexOfItemInSubList = binarySearch(secondHalfList, item);
                indexItem = (indexOfItemInSubList == ITEM_NOT_FOUND)? ITEM_NOT_FOUND : middleIndex + 1 + indexOfItemInSubList;
            }
        }
        return indexItem;
    }
}
