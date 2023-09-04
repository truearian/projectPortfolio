import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

class WeightedGraph {
    int dist[];
    int lastVisited[];
    Set<Integer> visited;
    PriorityQueue<Node> pQueue;
    int vertices; // # of vertices
    List<List<Node>> adjList; // adjacent list

    int matrix[][]; // floyd warshall
    int INFINITY = Integer.MAX_VALUE;

    public WeightedGraph(int vertices) {
        this.vertices = vertices;
        lastVisited = new int[vertices];
        visited = new HashSet<Integer>();
        dist = new int[vertices];
        pQueue = new PriorityQueue<Node>(vertices, new Node());
    }

    // floyd warshall constructor
    public WeightedGraph(int vertices, String floydWarshall) {
        this.vertices = vertices - 1;
        matrix = new int[vertices - 1][vertices - 1];
    }

    public void bellmanFordAlgorithm(List<List<Node>> adjList, int sourceVertex) {
        this.adjList = adjList;
        for (int i = 0; i < vertices; i++) {
            dist[i] = Integer.MAX_VALUE; // set to infinity
        }
        // add the source vertex to priority queue
        pQueue.add(new Node(sourceVertex, 0));
        // distance from source vertex to itself is 0
        dist[sourceVertex] = 0;
        while (visited.size() != vertices - 1) {
            // set variable u to the node we remove
            int v = pQueue.remove().node;
            // add node to visited
            visited.add(v);
            adjacentNodes(v);
        }
    }

    public void generateStartMatrix(List<List<Node>> adjList) {
        // make default matrix before we input our adjacency list (with my way of
        // retaining data from adjacency list and reading, I suppose I could
        // do this since we aren't being tested for runtime, there is surely a better
        // way to do this, like filling empty cells in the matrix)
        for (int i = 0; i < vertices; i++) {
            for (int j = 0; j < vertices; j++) {
                if (i == j)
                    matrix[i][j] = 0;
                else {
                    matrix[i][j] = INFINITY;
                }
            }
        }

        // input data from adjacency list
        for (int i = 1; i <= vertices; i++) {
            for (int j = 0; j < adjList.get(i).size(); j++) {
                matrix[i - 1][adjList.get(i).get(j).node - 1] = adjList.get(i).get(j).cost; // find node value in adj
                                                                                            // list and set to matrix
                                                                                            // node value
            }
        }
    }

    public void floydWarshallAlgorithm(List<List<Node>> adjList) {
        generateStartMatrix(adjList); //make our adjacency cost matrix
        int i, j, k;
        for (k = 0; k < vertices; k++) {
            for (i = 0; i < vertices; i++) {
                for (j = 0; j < vertices; j++) {
                    if(matrix[i][k] == INFINITY || matrix[k][j] == INFINITY) continue; //edge case for infinity overflow
                    if (matrix[i][j] > matrix[i][k] + matrix[k][j])
                        matrix[i][j] = matrix[i][k] + matrix[k][j]; //update min distance value
                }
            }
        }
    }

    // process the neighbours (adjacent) of the just visited node
    private void adjacentNodes(int v) {
        int edgeDistance = -1;
        int newDistance = -1;
        // process all adjacent nodes
        for (int i = 0; i < adjList.get(v).size(); i++) {
            Node u = adjList.get(v).get(i);
            // proceed only if current node is not visited
            if (!visited.contains(u.node)) {
                edgeDistance = u.cost;
                newDistance = dist[v] + edgeDistance;
                // compare cost (distance)
                if (newDistance < dist[u.node]) {
                    dist[u.node] = newDistance;
                    lastVisited[u.node] = v;
                }
                pQueue.add(new Node(u.node, dist[u.node]));
            }
        }
    }
}

class Node implements Comparator<Node> {
    public int node;
    public int cost;

    public Node() {
        // empty constructor
    }

    public Node(int node, int cost) {
        this.node = node;
        this.cost = cost;
    }

    public int compare(Node node1, Node node2) {
        if (node1.cost < node2.cost)
            return -1;
        if (node1.cost > node2.cost)
            return 1;
        return 0;
    }
}

public class Assignment3 {
    public static void main(String[] args) throws IOException {
        Scanner in = null;
        FileWriter fileBF = new FileWriter("cop3503-asn3-output-tashakkor-arian-bf.txt");
        FileWriter fileFW = new FileWriter("cop3503-asn3-output-tashakkor-arian-fw.txt");
        File fileName = new File("cop3503-asn2-input.txt"); //update input path
        try {
            // read file and set values to variables needed
            String line = null;
            String[] arr = null;
            int vertices = 0;
            int sourceVertex = 0;
            int numberOfEdges = 0;
            in = new Scanner(fileName);
            // read first 3 lines of the input -> vertices -> sourceVertex -> numberOfEdges
            vertices = in.nextInt() + 1;
            sourceVertex = in.nextInt();
            numberOfEdges = in.nextInt();
            // adjacency list representation of graph
            List<List<Node>> adjList = new ArrayList<List<Node>>();
            // Initialize adjacency list for all nodes in the graph
            for (int i = 0; i < vertices; i++) {
                List<Node> item = new ArrayList<Node>();
                adjList.add(item);
            }
            int node = 0;
            int toNode = 0;
            int cost = 0;
            line = in.nextLine();
            while (in.hasNextLine()) {
                // read line and split
                line = in.nextLine();
                arr = line.split(" ");
                // set integers to values
                node = Integer.valueOf(arr[0]);
                toNode = Integer.valueOf(arr[1]);
                cost = Integer.valueOf(arr[2]);
                // call function to make vertex with edge (nondirectional)
                adjList.get(node).add(new Node(toNode, cost));
                adjList.get(toNode).add(new Node(node, cost));
            }
            WeightedGraph graphBF = new WeightedGraph(vertices);
            graphBF.bellmanFordAlgorithm(adjList, sourceVertex);
            // print for bellman ford output (BF)
            try {
                PrintWriter outputBF = new PrintWriter(fileBF);
                outputBF.println(vertices - 1);
                for (int i = 1; i < graphBF.dist.length; i++) {
                    outputBF.println(i + " " + graphBF.dist[i] + " " +
                            graphBF.lastVisited[i]);
                }
                outputBF.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            WeightedGraph graphFW = new WeightedGraph(vertices, "FloydWarshall");
            graphFW.floydWarshallAlgorithm(adjList);

            try{
                PrintWriter outputFW = new PrintWriter(fileFW);
                outputFW.println(graphFW.vertices);
                for(int i = 0; i < graphFW.vertices; i++){
                    for(int j= 0; j < graphFW.vertices; j++){
                        outputFW.print(graphFW.matrix[i][j] + " "); // print V * V for distance each
                    }
                    outputFW.println();
                }
                outputFW.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}