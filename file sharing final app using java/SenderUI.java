import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class SenderUI extends JFrame {
    private JProgressBar progressBar;
    private JLabel statusLabel;
    private File selectedFile;
    private JTextField ipAddressField;

    public SenderUI() {
        setTitle("Sender");
        setSize(400, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());
        getContentPane().setBackground(new Color(255, 255, 224));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Components
        ipAddressField = new JTextField(15);
        JButton selectFileButton = new JButton("Select File");
        JButton sendFileButton = new JButton("Send File");
        progressBar = new JProgressBar(0, 100);
        statusLabel = new JLabel("Status: Idle");

        // Button Styles
        selectFileButton.setBackground(new Color(70, 130, 180)); // Steel Blue
        selectFileButton.setForeground(Color.WHITE);
        sendFileButton.setBackground(new Color(60, 179, 113)); // Medium Sea Green
        sendFileButton.setForeground(Color.WHITE);

        // Button Actions
        selectFileButton.addActionListener(e -> selectFile());
        sendFileButton.addActionListener(e -> sendFile());

        // Add components
        gbc.gridx = 0; gbc.gridy = 0; add(new JLabel("Receiver IP Address:"), gbc);
        gbc.gridy = 1; add(ipAddressField, gbc);
        gbc.gridy = 2; add(selectFileButton, gbc);
        gbc.gridy = 3; add(sendFileButton, gbc);
        gbc.gridy = 4; add(statusLabel, gbc);
        gbc.gridy = 5; add(progressBar, gbc);

        ipAddressField.setText(getLocalIPAddress());
        setVisible(true);
    }

    private void selectFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select a file to send");
        fileChooser.setAcceptAllFileFilterUsed(true);
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            statusLabel.setText("Selected: " + selectedFile.getName());
        }
    }

    private void sendFile() {
        if (selectedFile != null) {
            String ipAddress = ipAddressField.getText().trim();
            if (!ipAddress.isEmpty()) {
                new Thread(() -> {
                    try (Socket socket = new Socket(ipAddress, 8080);
                         DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                         FileInputStream fis = new FileInputStream(selectedFile)) {
                        dos.writeUTF(selectedFile.getName());
                        dos.writeLong(selectedFile.length());

                        byte[] buffer = new byte[4096];
                        long totalBytesSent = 0, fileSize = selectedFile.length();
                        statusLabel.setText("Status: Sending " + selectedFile.getName());

                        int bytesRead;
                        while ((bytesRead = fis.read(buffer)) != -1) {
                            dos.write(buffer, 0, bytesRead);
                            totalBytesSent += bytesRead;
                            progressBar.setValue((int) ((totalBytesSent * 100) / fileSize));
                        }
                        statusLabel.setText("Status: File Sent!");
                    } catch (IOException e) {
                        statusLabel.setText("Status: Error Sending File");
                        e.printStackTrace();
                    }
                }).start();
            } else {
                statusLabel.setText("Status: Please enter a valid IP address.");
            }
        } else {
            statusLabel.setText("Status: No file selected.");
        }
    }

    private String getLocalIPAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return "Unknown";
        }
    }

    public static void main(String[] args) {
        new SenderUI();
    }
}
