import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

public class TSPGraph implements IApproximateTSP {
    int[] parent;

    @Override
    public void MST(TSPMap map) {
        // PriorityQueue
        TreeMapPriorityQueue<Double, Integer> pq = new TreeMapPriorityQueue<>();
        // parent array
        parent = new int[map.getCount()];
        // initialise pq
        for (int i = 0; i < map.getCount(); i++) {
            pq.add(i, map.pointDistance(0, i));
        }
        pq.decreasePriority(0, 0.0);

        // Set to check if city is visited
        HashSet<Integer> visited = new HashSet<>();
        visited.add(0);

        while (!pq.isEmpty()) {
            Integer v = pq.extractMin();
            visited.add(v);
            // set the parent link
            map.setLink(v, parent[v], false);
            for (int i = 0; i < map.getCount(); i++) {
                if (! visited.contains(i)) {
                    if (map.pointDistance(i, v) < pq.lookup(i)) {
                        pq.decreasePriority(i, map.pointDistance(i, v));
                        parent[i] = v;
                    }
                }
            }
        }
        map.redraw();
    }

    @Override
    public void TSP(TSPMap map) {
        MST(map);
        // create an adjList based on parent array from MST(map)
        ArrayList<LinkedList<Integer>> adjList = new ArrayList<>();
        // initialise adjList
        for (int i = 0; i < map.getCount(); i++) {
            LinkedList<Integer> nodeList = new LinkedList<>();
            for (int j = 0; j < map.getCount(); j++) {
                if (parent[j] == i || parent[i] == j) {
                    nodeList.add(j);
                }
            }
            adjList.add(nodeList);
        }

        // initialise stack and visited set of nodes
        LinkedList<Integer> stack = new LinkedList<>();
        stack.addLast(0);
        HashSet<Integer> visited = new HashSet<>();

        // DFS
        while (!stack.isEmpty()) {
            Integer curr = stack.poll();
            visited.add(curr);
            for (Integer next : adjList.get(curr)) {
                if (! visited.contains(next)) {
                    stack.push(next);
                }
            }
            if (stack.isEmpty()) {
                map.setLink(curr, 0, false); // if last item, link to 0
            } else {
                map.setLink(curr, stack.peek(), false); // else link to prev item
            }
        }
        map.redraw();
    }

    @Override
    public boolean isValidTour(TSPMap map) {
        // Note: this function should with with *any* map, and not just results from TSP().
        // to be a valid tour,
        // (1) all cities must been visited exactly once
        // (2) all cities must have an outgoing link
        // (3) map must have exactly the same number of links as cities as a result of (1) and (2)
        int count = 0;
        int start = 0;
        HashSet<Integer> visited = new HashSet<>();
        while (true) {
            int next = map.getLink(start);
            start = next;
            if (next == -1 || visited.contains(next)) { // cond (1) and (2)
                break;
            } else {
                count++;
                visited.add(next);
            }
        }
        return count == map.getCount(); // cond (3)
    }

    @Override
    public double tourDistance(TSPMap map) {
        // Note: this function should with with *any* map, and not just results from TSP().
        // TODO: implement this method
        // if its valid, just count the weight of each link in map
        if (isValidTour(map)) {
            double count = 0;
            for (int i = 0; i < map.getCount(); i++) {
                int v = map.getLink(i);
                if (v != -1) {
                    count += map.pointDistance(i, v);
                }
            }
            return count;
        } else { // return -1 if is not valid
            return -1;
        }
    }

    public static void main(String[] args) {
        TSPMap map = new TSPMap(args.length > 0 ? args[0] : "C:/Users/ngjia/Desktop/ps8/hundredpoints.txt");
        TSPGraph graph = new TSPGraph();

        // graph.MST(map);
        graph.TSP(map);
        System.out.println(graph.isValidTour(map));
        System.out.println(graph.tourDistance(map));
    }
}
