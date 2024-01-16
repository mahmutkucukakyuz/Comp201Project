import java.util.Map;
import java.util.HashMap;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;

public class CSVReader {
    private Map<String, Map<String, Integer>> adjacencyList;

    public CSVReader(String filename) {
        adjacencyList = new HashMap<>();
        csvRead(filename);
    }

    private void csvRead(String csvName) {
        try (BufferedReader br = new BufferedReader(new FileReader(csvName))) {
            String[] cities_Name = br.readLine().split(",");
            br.lines().forEach(line -> {
                String[] values = line.split(",");
                String city = values[0];

                Map<String, Integer> edges = new HashMap<>();
                int i = 1;
                while (i < values.length) {
                    edges.put(cities_Name[i], Integer.parseInt(values[i]));
                    i++;
                }
                adjacencyList.put(city, edges);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Map<String, Integer>> getAdjacencyList() {
        return adjacencyList;
    }
}
