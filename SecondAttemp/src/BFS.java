import java.util.*;

public class BFS {
    private Map<String, Map<String, Integer>> adjacencyList;

    public BFS(Map<String, Map<String, Integer>> adjacencyList) {
        this.adjacencyList = adjacencyList;
    }

    public class find_short {
        private List<String> short_path;
        private int distance;

        public find_short(List<String> short_path, int distance) {
            this.short_path = short_path;
            this.distance = distance;
        }

        public List<String> getShort_path() {
            return short_path;
        }

        public int getDistance() {
            return distance;
        }
    }

    public find_short shortestPathBFS(String start, String end) {
        Queue<BFSNode> queue = new LinkedList<>();
        Map<String, String> parentMap = new HashMap<>();
        Map<String, Integer> distanceMap = new HashMap<>();

        queue.offer(new BFSNode(start, null, 0));
        parentMap.put(start, null);
        distanceMap.put(start, 0);

        while (!queue.isEmpty()) {
            BFSNode currentNode = queue.poll();
            String currentCity = currentNode.getCurrentCity();
            int currentDistance = currentNode.getCurrentDistance();

            Map<String, Integer> neighbors = adjacencyList.getOrDefault(currentCity, new HashMap<>());
            for (String neighbor : neighbors.keySet()) {
                int distanceToNeighbor = neighbors.get(neighbor);
                int totalDistance = currentDistance + distanceToNeighbor;

                if (distanceToNeighbor != 99999 && (!distanceMap.containsKey(neighbor) || totalDistance < distanceMap.get(neighbor))) {
                    queue.offer(new BFSNode(neighbor, currentCity, totalDistance));
                    parentMap.put(neighbor, currentCity);
                    distanceMap.put(neighbor, totalDistance);
                }
            }
        }

        List<String> shortestPath = constructPath(parentMap, start, end);
        int distance = distanceMap.getOrDefault(end, -1);

        return new find_short(shortestPath, distance);
    }

    private List<String> constructPath(Map<String, String> parentMap, String start, String end) {
        List<String> shortestPath = new ArrayList<>();
        String current = end;
        while (current != null) {
            shortestPath.add(current);
            current = parentMap.get(current);
        }
        Collections.reverse(shortestPath);
        return shortestPath;
    }

    private static class BFSNode {
        private String currentCity;
        private String previousCity;
        private int currentDistance;

        public BFSNode(String currentCity, String previousCity, int currentDistance) {
            this.currentCity = currentCity;
            this.previousCity = previousCity;
            this.currentDistance = currentDistance;
        }

        public String getCurrentCity() {
            return currentCity;
        }

        public int getCurrentDistance() {
            return currentDistance;
        }
    }
}
