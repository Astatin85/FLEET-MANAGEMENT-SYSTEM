package app;

import fleet.Highway;
import fleet.VehicleThread;
import vehicles.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class fleetSimGUI extends JFrame {
    private Highway highway;
    private List<VehicleThread> threads;
    private JPanel vehiclesPanel;
    private JLabel totalDistanceLabel;
    private JCheckBox syncCheckBox;


    public fleetSimGUI() {

        highway = new Highway();
        threads = new ArrayList<>();

  
        setTitle("Fleet Highway Simulator (Assignment 3)");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel controlPanel = new JPanel();

    
        JButton startButton = new JButton("Start Simulation");
        JButton stopButton = new JButton("Stop All");


        
        syncCheckBox = new JCheckBox("Enable Synchronization (Fix Race Condition)");

        startButton.addActionListener(e -> startSimulation());

        stopButton.addActionListener(e -> stopSimulation());
        syncCheckBox.addActionListener(e -> highway.setSynchronizedMode(syncCheckBox.isSelected()));

        controlPanel.add(startButton);
        controlPanel.add(stopButton);

        controlPanel.add(syncCheckBox);

        add(controlPanel, BorderLayout.NORTH);

        

        vehiclesPanel = new JPanel(new GridLayout(0, 1)); 
        JScrollPane scrollPane = new JScrollPane(vehiclesPanel);
        add(scrollPane, BorderLayout.CENTER);

        JPanel statusPanel = new JPanel();
        totalDistanceLabel = new JLabel("Shared Highway Distance: 0 km");
        totalDistanceLabel.setFont(new Font("Arial", Font.BOLD, 20));

        statusPanel.add(totalDistanceLabel);
        add(statusPanel, BorderLayout.SOUTH);

        initVehicles();
    }

    private void initVehicles() {
        addVehicleToSim(new Car("C1", "Toyota", 120));
        addVehicleToSim(new Truck("T1", "Ford", 100));
        addVehicleToSim(new Bus("B1", "Volvo", 90));
    }



    private void addVehicleToSim(Vehicle v) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        
        JLabel infoLabel = new JLabel(formatVehicleInfo(v));
        infoLabel.setPreferredSize(new Dimension(500, 30));
        
        JButton refuelBtn = new JButton("Refuel");
        refuelBtn.setEnabled(false); 

        row.add(infoLabel);

        row.add(refuelBtn);
        vehiclesPanel.add(row);

        VehicleThread vt = new VehicleThread(v, highway, () -> {
            infoLabel.setText(formatVehicleInfo(v));
            totalDistanceLabel.setText("Shared Highway Distance: " + highway.getTotalDistance() + " km");
            

            refuelBtn.setEnabled(true);
        });


        refuelBtn.addActionListener(e -> {
            vt.refuelAndResume();
        });

        threads.add(vt);
    }

    private String formatVehicleInfo(Vehicle v) {
        return String.format("ID: %s | Model: %s | Mileage: %.2f km", 
            v.getId(), v.getModel(), v.getCurrentMileage());

    }

    private void startSimulation() {
        highway.reset();
        highway.setSynchronizedMode(syncCheckBox.isSelected());
        for (VehicleThread vt : threads) {
            vt.start();

        }
    }

    private void stopSimulation() {
        for (VehicleThread vt : threads) {
            vt.stop();
        }

    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new fleetSimGUI().setVisible(true);

        });
    }

    
}
