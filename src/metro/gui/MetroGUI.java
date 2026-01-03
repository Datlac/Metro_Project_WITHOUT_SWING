package metro.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import metro.models.finance.Ticket;
import metro.models.person.Customer;
import metro.services.MetroSystem;

public class MetroGUI extends JFrame {
    private MetroSystem metroSystem;
    private List<Customer> customers;
    private JTable ticketTable;
    private JTable customerTable;
    private DefaultTableModel ticketModel;
    private DefaultTableModel customerModel;

    public MetroGUI(MetroSystem metroSystem, List<Customer> customers) {
        this.metroSystem = metroSystem;
        this.customers = customers;

        setTitle("Metro Management System - Viewer");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        refreshData();
    }

    private void initComponents() {
        JTabbedPane tabbedPane = new JTabbedPane();

        // Panel for Tickets
        JPanel ticketPanel = new JPanel(new BorderLayout());
        String[] ticketColumns = {"Ticket ID", "Customer Name", "Type", "Price"};
        ticketModel = new DefaultTableModel(ticketColumns, 0);
        ticketTable = new JTable(ticketModel);
        ticketPanel.add(new JScrollPane(ticketTable), BorderLayout.CENTER);
        tabbedPane.addTab("Quản lý Vé", ticketPanel);

        // Panel for Customers
        JPanel customerPanel = new JPanel(new BorderLayout());
        String[] customerColumns = {"Customer ID", "Name", "Balance", "Ticket Count"};
        customerModel = new DefaultTableModel(customerColumns, 0);
        customerTable = new JTable(customerModel);
        customerPanel.add(new JScrollPane(customerTable), BorderLayout.CENTER);
        tabbedPane.addTab("Quản lý Khách Hàng", customerPanel);

        // Refresh Button
        JButton refreshBtn = new JButton("Refresh Data");
        refreshBtn.addActionListener(e -> refreshData());
        
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(refreshBtn);

        add(tabbedPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void refreshData() {
        // Update Ticket Table
        ticketModel.setRowCount(0);
        for (Ticket t : metroSystem.getSoldTickets()) {
            Object[] row = {
                t.getTicketId(),
                t.getCustomer().getFullName(),
                t.getTicketType(),
                t.getPrice()
            };
            ticketModel.addRow(row);
        }

        // Update Customer Table
        customerModel.setRowCount(0);
        for (Customer c : customers) {
            Object[] row = {
                c.getCustomerId(),
                c.getFullName(),
                String.format("%,.0f", c.getWalletBalance()),
                c.getTicketHistory().size()
            };
            customerModel.addRow(row);
        }
    }
}
