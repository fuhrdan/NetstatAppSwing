import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class NetstatAppSwing extends JFrame {

    // Table model to dynamically hold the netstat data
    private DefaultTableModel tableModel;

    public NetstatAppSwing() {
        setTitle("Active Network Connections");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialize table model with columns
        tableModel = new DefaultTableModel(new String[]{"Protocol", "Local Address", "Foreign Address", "State"}, 0);
        JTable table = new JTable(tableModel);
        
        // Set layout and add the table inside a scroll pane
        setLayout(new BorderLayout());
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Fetch and display connections
        fetchNetstatData();
    }

    private void fetchNetstatData() {
        String command = "netstat -a";

        try {
            // Execute the netstat command
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", command);
            Process process = processBuilder.start();

            // Read the command output
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            // Process each line of netstat output
            while ((line = reader.readLine()) != null) {
                if (line.contains("TCP") || line.contains("UDP")) {
                    // Split the line into columns
                    String[] columns = line.trim().split("\\s+");
                    
                    // Only add rows with expected number of columns (4 in this case)
                    if (columns.length >= 4) {
                        String protocol = columns[0];
                        String localAddress = columns[1];
                        String foreignAddress = columns[2];
                        String state = columns.length > 3 ? columns[3] : "N/A";
                        
                        // Add the row to the table model
                        tableModel.addRow(new Object[]{protocol, localAddress, foreignAddress, state});
                    }
                }
            }

            // Wait for the process to complete
            process.waitFor();

        } catch (IOException | InterruptedException e) {
            JOptionPane.showMessageDialog(this, "Error fetching network data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        // Run the Swing application
        SwingUtilities.invokeLater(() -> {
            NetstatAppSwing app = new NetstatAppSwing();
            app.setVisible(true);
        });
    }
}
