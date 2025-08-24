import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

// Flight class
class Flight {
    private String flightNumber;
    private String origin;
    private String destination;
    private String departureTime;
    private int seatsAvailable;
    private double price;

    public Flight(String flightNumber, String origin, String destination, String departureTime, int seatsAvailable,
                  double price) {
        this.flightNumber = flightNumber;
        this.origin = origin;
        this.destination = destination;
        this.departureTime = departureTime;
        this.seatsAvailable = seatsAvailable;
        this.price = price;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public int getSeatsAvailable() {
        return seatsAvailable;
    }

    public double getPrice() {
        return price;
    }

    public void bookSeat() {
        seatsAvailable--;
    }

    public void cancelSeat() {
        seatsAvailable++;
    }

    @Override
    public String toString() {
        return "Flight Number: " + flightNumber +
                "\nOrigin: " + origin +
                "\nDestination: " + destination +
                "\nDeparture Time: " + departureTime +
                "\nSeats Available: " + seatsAvailable +
                "\nPrice (in Rs): " + price;
    }
}

// Reservation class
class Reservation {
    private String passengerName;
    private String flightNumber;
    private double price;

    public Reservation(String passengerName, String flightNumber, double price) {
        this.passengerName = passengerName;
        this.flightNumber = flightNumber;
        this.price = price;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Passenger Name: " + passengerName +
                "\nFlight Number: " + flightNumber +
                "\nPrice Paid (in Rs): " + price;
    }
}

// Airline Reservation System
public class AirlineReservationSystem extends JFrame {
    private List<Flight> flights = new ArrayList<>();
    private List<Reservation> reservations = new ArrayList<>();
    private JTextArea outputArea;
    private JTextField nameField, flightNumberField;

    public AirlineReservationSystem() {
        setTitle("Airline Reservation System");
        setSize(900, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(true);

        initializeFlights();

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(outputArea);
        add(scrollPane, BorderLayout.CENTER);

        // Header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(30, 144, 255));
        JLabel headerLabel = new JLabel("Welcome to Udaan Airport");
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(headerLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.setBackground(new Color(230, 240, 250));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Booking Details"));

        nameField = new JTextField();
        flightNumberField = new JTextField();

        inputPanel.add(new JLabel("Passenger Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Flight Number:"));
        inputPanel.add(flightNumberField);

        // Button Panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 5, 10, 10));
        buttonPanel.setBackground(new Color(230, 240, 250));

        JButton viewFlightsButton = new JButton("View Flights");
        JButton bookButton = new JButton("Book Flight");
        JButton cancelBookingButton = new JButton("Cancel Booking");
        JButton viewReservationButton = new JButton("View Reservation");
        JButton logoutButton = new JButton("Logout");

        viewFlightsButton.addActionListener(new ViewFlightsListener());
        bookButton.addActionListener(new BookFlightListener());
        cancelBookingButton.addActionListener(new CancelBookingListener());
        viewReservationButton.addActionListener(new ViewReservationListener());
        logoutButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to logout?",
                    "Logout Confirmation",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                this.dispose();
                new LoginScreen(); // Return to login screen after logout.
            }
        });

        buttonPanel.add(viewFlightsButton);
        buttonPanel.add(bookButton);
        buttonPanel.add(cancelBookingButton);
        buttonPanel.add(viewReservationButton);
        buttonPanel.add(logoutButton);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(inputPanel, BorderLayout.NORTH);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(southPanel, BorderLayout.SOUTH);
    }

    private void initializeFlights() {
        flights.add(new Flight("UA101", "Delhi", "Mumbai", "10:00 AM", 50, 4500));
        flights.add(new Flight("AA202", "Delhi", "Bangalore", "1:00 PM", 30, 3800));
        flights.add(new Flight("BA303", "Delhi", "Kolkata", "5:00 PM", 120, 12500));
    }

    private void displayMessage(String message) {
        outputArea.setText(message);
    }

    private class ViewFlightsListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            StringBuilder sb = new StringBuilder("Available Flights:\n");
            for (Flight flight : flights) {
                sb.append(flight).append("\n\n");
            }
            displayMessage(sb.toString());
        }
    }

    private class BookFlightListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = nameField.getText().trim();
            String flightNumber = flightNumberField.getText().trim();

            if (name.isEmpty() || flightNumber.isEmpty()) {
                JOptionPane.showMessageDialog(AirlineReservationSystem.this, "Please fill in all fields.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            for (Flight flight : flights) {
                if (flight.getFlightNumber().equals(flightNumber)) {
                    if (flight.getSeatsAvailable() > 0) {
                        flight.bookSeat();
                        reservations.add(new Reservation(name, flightNumber, flight.getPrice()));
                        displayMessage("Booking confirmed for " + name +
                                " on flight " + flightNumber +
                                ". Price: Rs " + flight.getPrice());
                        return;
                    } else {
                        displayMessage("Sorry, no seats available on flight " + flightNumber);
                        return;
                    }
                }
            }
            displayMessage("Flight " + flightNumber + " not found.");
        }
    }

    private class CancelBookingListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = nameField.getText().trim();
            String flightNumber = flightNumberField.getText().trim();

            if (name.isEmpty() || flightNumber.isEmpty()) {
                JOptionPane.showMessageDialog(AirlineReservationSystem.this, "Please fill in all fields.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            for (Reservation reservation : reservations) {
                if (reservation.getPassengerName().equals(name) && reservation.getFlightNumber().equals(flightNumber)) {
                    reservations.remove(reservation);
                    for (Flight flight : flights) {
                        if (flight.getFlightNumber().equals(flightNumber)) {
                            flight.cancelSeat();
                            displayMessage("Booking cancelled for " + name +
                                    " on flight " + flightNumber);
                            return;
                        }
                    }
                }
            }
            displayMessage("No booking found for " + name + " on flight " + flightNumber);
        }
    }

    private class ViewReservationListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            StringBuilder sb = new StringBuilder("Reservations:\n");
            for (Reservation reservation : reservations) {
                sb.append(reservation).append("\n\n");
            }
            displayMessage(sb.toString());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginScreen::new);
    }
}

// Login Screen
class LoginScreen extends JFrame {
    public LoginScreen() {
        setTitle("Login");
        setSize(400, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel loginPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();

        JButton loginButton = new JButton("Login");
        JButton exitButton = new JButton("Exit");
        JButton forgotPasswordButton = new JButton("Forgot Password");

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (username.equals("admin") && password.equals("password")) {
                JOptionPane.showMessageDialog(this, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
                new AirlineReservationSystem().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials. Try again.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        exitButton.addActionListener(e -> System.exit(0));

        forgotPasswordButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "Password Hint: The default password is 'password'.",
                    "Forgot Password",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        loginPanel.add(usernameLabel);
        loginPanel.add(usernameField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);
        loginPanel.add(loginButton);
        loginPanel.add(exitButton);
        loginPanel.add(forgotPasswordButton);

        add(loginPanel, BorderLayout.CENTER);
        setVisible(true);
    }
}

