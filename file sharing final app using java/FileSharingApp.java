import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class FileSharingApp extends JFrame {

    public FileSharingApp() {
        setTitle("File Sharing App");
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(230, 240, 250)); // Light blue background

        // App Title
        JLabel titleLabel = new JLabel("File Sharing App", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(0, 51, 102)); // Dark blue title
        add(titleLabel, BorderLayout.NORTH); // Add title at the top

        // Panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20)); // Centered buttons with spacing
        buttonPanel.setBackground(new Color(230, 240, 250)); // Match background

        // Main buttons for Sender and Receiver
        JButton senderButton = new JButton("SENDER");
        JButton receiverButton = new JButton("RECEIVER");

        // Styling the buttons
        senderButton.setFont(new Font("Arial", Font.BOLD, 18));
        senderButton.setBackground(new Color(0, 153, 76)); // Green background
        senderButton.setForeground(Color.WHITE);
        senderButton.setFocusPainted(false);
        senderButton.setPreferredSize(new Dimension(150, 50)); // Set button size

        receiverButton.setFont(new Font("Arial", Font.BOLD, 18));
        receiverButton.setBackground(new Color(204, 0, 0)); // Red background
        receiverButton.setForeground(Color.WHITE);
        receiverButton.setFocusPainted(false);
        receiverButton.setPreferredSize(new Dimension(150, 50)); // Set button size

        // Button actions
        senderButton.addActionListener((ActionEvent e) -> openSenderUI());
        receiverButton.addActionListener((ActionEvent e) -> openReceiverUI());

        // Add buttons to the panel
        buttonPanel.add(senderButton);
        buttonPanel.add(receiverButton);

        // Add button panel to the center of the frame
        add(buttonPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    // Sender UI
    private void openSenderUI() {
        new SenderUI();
    }

    // Receiver UI
    private void openReceiverUI() {
        new ReceiverUI();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FileSharingApp::new);
    }
}
