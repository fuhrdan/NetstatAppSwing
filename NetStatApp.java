import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class NetstatApp {

    public static void main(String[] args) {
        // Initialize the command
        String command = "netstat -a";

        try {
            // Create a process to execute the command
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", command);
            Process process = processBuilder.start();

            // Read the output from the command
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            // Print header
            System.out.println("Active Connections:");
            System.out.println("Protocol\tLocal Address\t\tForeign Address\t\tState");

            // Process each line of the command output
            while ((line = reader.readLine()) != null) {
                // Filter and format output to display only active connections
                if (line.contains("TCP") || line.contains("UDP")) {
                    System.out.println(line.trim());
                }
            }

            // Wait for the process to complete
            process.waitFor();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}