import java.util.*;

public class DFS {
    private Map<String, Map<String, Integer>> adjacencyList;
    private List<String> shortestPath;
    private int short_dis;
    private int depth_limit; // Derinlik sınırlaması

    public DFS(Map<String, Map<String, Integer>> adjacencyList, int depth_limit) {
        this.adjacencyList = adjacencyList;
        this.depth_limit = depth_limit;
        shortestPath = new ArrayList<>();
        short_dis = Integer.MAX_VALUE;
    }

    public List<String> find_short(String startCity, String endCity) {
        stackDFS(startCity, endCity);
        return shortestPath;
    }

    private void stackDFS(String startCity, String endCity) {
        Stack<DFSNode> stack = new Stack<>();
        stack.push(new DFSNode(startCity, new ArrayList<>(Collections.singletonList(startCity)), 0));

        while (!stack.isEmpty()) {
            DFSNode currentNode = stack.pop();
            String currentCity = currentNode.getCurrentCity();
            List<String> currentPath = currentNode.getCurrentPath();
            int currentDepth = currentNode.getCurrentDepth();

            if (currentCity.equals(endCity)) {
                int distance = cal_dis(currentPath);
                if (distance < short_dis) {
                    short_dis = distance;
                    shortestPath = new ArrayList<>(currentPath);
                }
            }

            if (currentDepth < depth_limit) {
                Map<String, Integer> neighbors = adjacencyList.getOrDefault(currentCity, new HashMap<>());
                for (Map.Entry<String, Integer> neighbor : neighbors.entrySet()) {
                    String nextCity = neighbor.getKey();
                    int distance = neighbor.getValue();

                    if (distance != 99999 && !currentPath.contains(nextCity)) {
                        List<String> newPath = new ArrayList<>(currentPath);
                        newPath.add(nextCity);
                        stack.push(new DFSNode(nextCity, newPath, currentDepth + 1));
                    }
                }
            }
        }
    }

    private int cal_dis(List<String> path) {
        int total_dis = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            String currentCity = path.get(i);
            String nextCity = path.get(i + 1);
            total_dis = total_dis + adjacencyList.get(currentCity).get(nextCity);
        }
        return total_dis;
    }

    public int getShort_dis() {
        return short_dis;
    }

    private static class DFSNode {
        private String currentCity;
        private List<String> currentPath;
        private int currentDepth;

        public DFSNode(String currentCity, List<String> currentPath, int currentDepth) {
            this.currentCity = currentCity;
            this.currentPath = currentPath;
            this.currentDepth = currentDepth;
        }

        public String getCurrentCity() {
            return currentCity;
        }

        public List<String> getCurrentPath() {
            return currentPath;
        }

        public int getCurrentDepth() {
            return currentDepth;
        }
    }
}
