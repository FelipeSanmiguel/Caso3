import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        HashMap<String, Integer> config = new HashMap<>();

        Scanner sc = new Scanner(new File("config.txt"));

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] partes = line.split("=");
            config.put(partes[0], Integer.parseInt(partes[1]));
        }
        sc.close(); 

        int ni = config.get("ni");
        int nc = config.get("nc");

        System.out.println(ni);
    }
}
