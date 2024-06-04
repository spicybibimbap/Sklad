import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SkladApp {
    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;
    private List<Polozka> polozky;

    public SkladApp() {
        polozky = new ArrayList<>();

        frame = new JFrame("Skladová aplikace");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tableModel = new DefaultTableModel(new String[]{"Název zboží", "Cena za kus", "Počet kusů"}, 0);
        table = new JTable(tableModel);

        JButton addButton = new JButton("Přidat zboží");
        addButton.addActionListener(new AddButtonListener());

        JButton deleteButton = new JButton("Smazat zboží");
        deleteButton.addActionListener(new DeleteButtonListener());

        JButton calculateButton = new JButton("Výpočet celkové ceny");
        calculateButton.addActionListener(new CalculateButtonListener());

        JButton exportButton = new JButton("Export do CSV");
        exportButton.addActionListener(new ExportButtonListener());

        JPanel panel = new JPanel();
        panel.add(addButton);
        panel.add(deleteButton);
        panel.add(calculateButton);
        panel.add(exportButton);

        frame.getContentPane().add(new JScrollPane(table), BorderLayout.CENTER);
        frame.getContentPane().add(panel, BorderLayout.SOUTH);

        frame.pack();
        frame.setVisible(true);
    }

    private class AddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String nazev = JOptionPane.showInputDialog("Zadejte název zboží:");
            double cena = Double.parseDouble(JOptionPane.showInputDialog("Zadejte cenu za kus:"));
            int pocet = Integer.parseInt(JOptionPane.showInputDialog("Zadejte počet kusů:"));

            Polozka polozka = new Polozka(nazev, cena, pocet);
            polozky.add(polozka);
            tableModel.addRow(new Object[]{polozka.getNazev(), polozka.getCena(), polozka.getPocet()});
        }
    }

    private class DeleteButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow!= -1) {
                polozky.remove(selectedRow);
                tableModel.removeRow(selectedRow);
            }
        }
    }

    private class CalculateButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            double celkem = 0;
            for (Polozka polozka : polozky) {
                celkem += polozka.getCena() * polozka.getPocet();
            }
            JOptionPane.showMessageDialog(frame, "Celková cena: " + celkem);
        }
    }

    private class ExportButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try (FileWriter writer = new FileWriter("sklad.csv")) {
                writer.write("Název zboží;Cena za kus;Počet kusů\n");
                for (Polozka polozka : polozky) {
                    writer.write(polozka.getNazev() + ";" + polozka.getCena() + ";" + polozka.getPocet() + "\n");
                }
                JOptionPane.showMessageDialog(frame, "Export úspěšný!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Chyba při exportu: " + ex.getMessage());
            }
        }
    }

    private static class Polozka {
        private String nazev;
        private double cena;
        private int pocet;

        public Polozka(String nazev, double cena, int pocet) {
            this.nazev = nazev;
            this.cena = cena;
            this.pocet = pocet;
        }

        public String getNazev() {
            return nazev;
        }

        public double getCena() {
            return cena;
        }

        public int getPocet() {
            return pocet;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SkladApp();
            }
        });
    }
}