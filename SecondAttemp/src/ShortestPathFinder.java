import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class ShortestPathFinder {
    private JComboBox<String> startCityComboBox;
    private JComboBox<String> targetCityComboBox;
    private JButton dfsButton;
    private JButton bfsButton;
    private JTextArea resultTextArea;
    private JTextField depthLimitField;
    private JPanel mainPanel;

    private Map<String, Map<String, Integer>> adjacencyList;
    private Map<String, City> cities;

    public ShortestPathFinder(String filename) {
        mainPanel = new JPanel();
        mainPanel.setLayout(new FlowLayout());

        startCityComboBox = new JComboBox<>();
        targetCityComboBox = new JComboBox<>();
        dfsButton = new JButton("DFS");
        bfsButton = new JButton("BFS");
        resultTextArea = new JTextArea(10, 30);
        depthLimitField = new JTextField(5);

        mainPanel.add(new JLabel("From: "));
        mainPanel.add(startCityComboBox);
        mainPanel.add(new JLabel("To: "));
        mainPanel.add(targetCityComboBox);
        mainPanel.add(new JLabel("Depth Limit: "));
        mainPanel.add(depthLimitField);
        mainPanel.add(dfsButton);
        mainPanel.add(bfsButton);
        mainPanel.add(resultTextArea);

        CSVReader csvReader = new CSVReader(filename);
        adjacencyList = csvReader.getAdjacencyList();
        cities = new HashMap<>();

        List<String> sortedCityNames = new ArrayList<>(adjacencyList.keySet());
        Collections.sort(sortedCityNames); // Şehir adlarını alfabetik sıraya göre sırala

        startCityComboBox.addItem("Select Start City");
        targetCityComboBox.addItem("Select Target City");

        for (String cityName : sortedCityNames) {
            City city = new City(cityName);
            cities.put(cityName, city);
            startCityComboBox.addItem(cityName);
            targetCityComboBox.addItem(cityName);
        }

        dfsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleAlgorithmButtonClick('D');
            }
        });

        bfsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleAlgorithmButtonClick('B');
            }
        });
    }

    private void handleAlgorithmButtonClick(char algorithmChoice) {
        String startCity = (String) startCityComboBox.getSelectedItem();
        String targetCity = (String) targetCityComboBox.getSelectedItem();

        if (!startCity.equals("Select Start City") && !targetCity.equals("Select Target City")) {
            if (algorithmChoice == 'D') {
                handleDFS(startCity, targetCity);
            } else if (algorithmChoice == 'B') {
                handleBFS(startCity, targetCity);
            } else {
                resultTextArea.setText("Invalid algorithm choice.");
            }
        } else {
            JOptionPane.showMessageDialog(
                    null,
                    "Please select both start and target cities.",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void handleDFS(String startCity, String targetCity) {
        String depthLimitStr = depthLimitField.getText();
        if (!depthLimitStr.isEmpty()) {
            int depthLimit = Integer.parseInt(depthLimitStr);
            String result = calculateShortestPath(startCity, targetCity, 'D', depthLimit);
            resultTextArea.setText(result);
        } else {
            JOptionPane.showMessageDialog(
                    null,
                    "Please provide a depth limit for DFS.",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void handleBFS(String startCity, String targetCity) {
        String result = calculateShortestPath(startCity, targetCity, 'B', 0); // 0 as depth limit for BFS
        resultTextArea.setText(result);
    }


    private String calculateShortestPath(String startCity, String targetCity, char algorithmChoice, int depthLimit) {
        String result = "";
        if (algorithmChoice == 'D') {
            DFS dfs = new DFS(adjacencyList, depthLimit);
            List<String> shortestPath = dfs.find_short(startCity, targetCity);
            int totalDistance = dfs.getShort_dis();
            result = formatResult(shortestPath, totalDistance);
        } else if (algorithmChoice == 'B') {
            BFS bfs = new BFS(adjacencyList);
            BFS.find_short findshort = bfs.shortestPathBFS(startCity, targetCity);

            if (findshort != null) {
                List<String> shortestPath = findshort.getShort_path();
                int totalDistance = findshort.getDistance();
                result = formatResult(shortestPath, totalDistance);
            } else {
                result = "No path found.";
            }
        } else {
            result = "Invalid algorithm choice.";
        }

        return result;
    }

    private String formatResult(List<String> shortestPath, int totalDistance) {
        StringBuilder resultBuilder = new StringBuilder();
        for (int i = 0; i < shortestPath.size() - 1; i++) {
            String city1 = shortestPath.get(i);
            String city2 = shortestPath.get(i + 1);
            int distance = adjacencyList.get(city1).get(city2);

            resultBuilder.append(city1).append(" -> ").append(city2).append(": ").append(distance).append(" km\n");
        }

        resultBuilder.append("Total Distance: ").append(totalDistance).append(" km");
        return resultBuilder.toString();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Shortest Path Finder");
        ShortestPathFinder shortestPathFinder = new ShortestPathFinder("src/cities.csv");
        frame.setContentPane(shortestPathFinder.mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public class City {
        private String city_name;
        private int row;
        private int col;

        public City(String city_name) {
            this.city_name = city_name;
        }

        public String getCity_name() {
            return city_name;
        }

        public int getRow() {
            return row;
        }

        public int getCol() {
            return col;
        }

        public void setRow(int row) {
            this.row = row;
        }

        public void setCol(int col) {
            this.col = col;
        }
    }
}
