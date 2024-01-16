package dataaaaaaaaaaaaaa;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class CityDistanceGUI {
    private static List<String[]> data;
    private static double totalDistance;

    public static void main(String[] args) {
        String csvFilePath = "C://Users//Lenovo//Downloads//onem.csv";
        data = readCSV(csvFilePath);
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("City Distance Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel(new GridLayout(4, 2));

        JLabel fromLabel = new JLabel("From:");
        panel.add(fromLabel);

        JComboBox<String> cityComboBox1 = new JComboBox<>(getCitiesArray());
        panel.add(cityComboBox1);

        JLabel toLabel = new JLabel("To:");
        panel.add(toLabel);

        JComboBox<String> cityComboBox2 = new JComboBox<>(getCitiesArray());
        panel.add(cityComboBox2);

        JButton calculateButton = new JButton("Calculate Distance");
        calculateButton.addActionListener(e -> calculateShortestPath((String) cityComboBox1.getSelectedItem(), (String) cityComboBox2.getSelectedItem()));

        panel.add(new JLabel());
        panel.add(calculateButton);

        frame.getContentPane().add(BorderLayout.CENTER, panel);
        frame.setSize(400, 150);
        frame.setVisible(true);
    }

    private static String[] getCitiesArray() {
        String[] cities = new String[data.size()];
        for (int i = 0; i < data.size(); i++) {
            cities[i] = data.get(i)[0];
        }
        return cities;
    }

    private static void calculateShortestPath(String startCity, String endCity) {
        totalDistance = 0;
        List<String> path = dijkstra(data, startCity, endCity);

        if (!path.isEmpty()) {
            StringBuilder resultString = new StringBuilder("Selected cities: " + startCity + " and " + endCity +
                    "\nPath: " + String.join(" -> ", path) +
                    "\nTotal distance traveled: " + totalDistance);
            JOptionPane.showMessageDialog(null, resultString.toString());
        } else {
            JOptionPane.showMessageDialog(null, "No valid path found between " + startCity + " and " + endCity);
        }
    }

    private static List<String> dijkstra(List<String[]> data, String startCity, String endCity) {
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>(Comparator.comparingDouble(node -> node.distance));
        Map<String, Double> distances = new HashMap<>();
        Map<String, String> parentMap = new HashMap<>();
        Set<String> visited = new HashSet<>();

        priorityQueue.add(new Node(startCity, 0));
        distances.put(startCity, 0.0);

        while (!priorityQueue.isEmpty()) {
            Node currentNode = priorityQueue.poll();
            String currentCity = currentNode.city;

            if (visited.contains(currentCity)) {
                continue;
            }

            visited.add(currentCity);

            for (String successor : getSuccessors(data, currentCity)) {
                if (!visited.contains(successor)) {
                    double distance = getDistanceBetweenCities(currentCity, successor);

                    double newDistance = distances.get(currentCity) + distance;

                    if (!distances.containsKey(successor) || newDistance < distances.get(successor)) {
                        distances.put(successor, newDistance);
                        parentMap.put(successor, currentCity);
                        priorityQueue.add(new Node(successor, newDistance));
                    }
                }
            }
        }

        return constructPath(parentMap, endCity);
    }

    private static List<String> constructPath(Map<String, String> parentMap, String endCity) {
        List<String> path = new ArrayList<>();
        String current = endCity;

        while (current != null) {
            path.add(current);
            totalDistance += getDistanceBetweenCities(parentMap.get(current), current);
            current = parentMap.get(current);
        }

        Collections.reverse(path);
        return path;
    }

    private static class Node {
        String city;
        double distance;

        Node(String city, double distance) {
            this.city = city;
            this.distance = distance;
        }
    }

    // Modify the getDistanceBetweenCities method
    private static double getDistanceBetweenCities(String city1, String city2) {
        int index1 = findIndex(data, city1);
        int index2 = findIndex(data, city2);

        if (index1 != -1 && index2 != -1) {
            String distanceStr = data.get(index1)[index2].trim();

            if (!distanceStr.isEmpty()) {
                String numericPart = distanceStr.replaceAll("[^0-9.]", "");

                try {
                    return Double.parseDouble(numericPart);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }

        // Return a large positive value for unreachable cities
        return Double.POSITIVE_INFINITY;
    }


    private static int findIndex(List<String[]> data, String city) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i)[0].equals(city)) {
                return i;
            }
        }
        return -1;
    }

    private static List<String[]> readCSV(String filePath) {
        List<String[]> data = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                data.add(values);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    private static List<String> getSuccessors(List<String[]> data, String city) {
        List<String> successors = new ArrayList<>();
        int index = findIndex(data, city);

        if (index != -1) {
            for (int i = 1; i < data.get(index).length; i++) {
                String distance = data.get(index)[i].trim();
                if (!distance.isEmpty() && !distance.equals("99999") && !distance.equals("0")) {
                    successors.add(data.get(i)[0]);
                }
            }
        }

        return successors;
    }
}