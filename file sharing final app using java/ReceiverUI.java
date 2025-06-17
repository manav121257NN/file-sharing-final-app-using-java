import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class ReceiverUI extends JFrame {
    private JProgressBar progressBar;
    private JLabel statusLabel;
    private JButton startReceivingButton;
    private JButton stopReceivingButton;
    private volatile boolean receiving; // Flag to control the receiving process

    public ReceiverUI() {
        setTitle("Receiver");
        setSize(400, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());
        getContentPane().setBackground(new Color(224, 255, 224));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Components
        startReceivingButton = new JButton("Start Receiving");
        stopReceivingButton = new JButton("Stop Receiving");
        progressBar = new JProgressBar(0, 100);
        statusLabel = new JLabel("Status: Waiting for file...");

        // Button Styles
        startReceivingButton.setBackground(new Color(100, 150, 255)); // Light Blue
        startReceivingButton.setForeground(Color.WHITE);
        stopReceivingButton.setBackground(new Color(255, 100, 100)); // Light Red
        stopReceivingButton.setForeground(Color.WHITE);
        stopReceivingButton.setEnabled(false); // Initially disabled

        startReceivingButton.addActionListener(e -> {
            receiving = true; 
            stopReceivingButton.setEnabled(true);
            startReceivingButton.setEnabled(false);
            new Thread(this::receiveFile).start();
        });

        stopReceivingButton.addActionListener(e -> {
            receiving = false; 
            stopReceivingButton.setEnabled(false);
            startReceivingButton.setEnabled(true);
            statusLabel.setText("Status: Stopping file reception...");
        });

        // Add components to the frame
        gbc.gridx = 0; gbc.gridy = 0; add(startReceivingButton, gbc);
        gbc.gridy = 1; add(stopReceivingButton, gbc);
        gbc.gridy = 2; add(statusLabel, gbc);
        gbc.gridy = 3; add(progressBar, gbc);

        setVisible(true);
    }

    private void receiveFile() {
        try (ServerSocket serverSocket = new ServerSocket(8080);
             Socket socket = serverSocket.accept();
             DataInputStream dis = new DataInputStream(socket.getInputStream())) {

            String fileName = dis.readUTF();
            long fileSize = dis.readLong();

            File receivedFile = new File(fileName);
            try (FileOutputStream fos = new FileOutputStream(receivedFile)) {
                byte[] buffer = new byte[4096];
                long totalBytesReceived = 0;
                int bytesRead;

                statusLabel.setText("Status: Receiving " + fileName);

                while (receiving && (bytesRead = dis.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                    totalBytesReceived += bytesRead;
                    int progress = (int) ((totalBytesReceived * 100) / fileSize);
                    progressBar.setValue(progress);
                }

                if (receiving) {
                    statusLabel.setText("Status: File Received!");
                } else {
                    statusLabel.setText("Status: Reception Stopped!");
                }

            } catch (IOException e) {
                statusLabel.setText("Status: Error Receiving File");
                e.printStackTrace();
            }
        } catch (IOException e) {
            statusLabel.setText("Status: Connection Error");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ReceiverUI();
    }
}
