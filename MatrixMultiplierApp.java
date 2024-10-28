import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class MatrixMultiplierApp extends JFrame {
    private JTextField sizeInput;
    private JTable tableA, tableB, tableX;
    private JButton loadButton, calculateButton;
    private DefaultTableModel modelA, modelB, modelX;

    public MatrixMultiplierApp() {
        setTitle("Matrix Multiplier");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Панель введення розміру матриці
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(new JLabel("Введіть розмір матриці (n <= 15):"));
        sizeInput = new JTextField(5);
        inputPanel.add(sizeInput);

        // Кнопки для завантаження та обчислення
        loadButton = new JButton("Завантажити з файлу");
        calculateButton = new JButton("Обчислити X");
        inputPanel.add(loadButton);
        inputPanel.add(calculateButton);
        add(inputPanel, BorderLayout.NORTH);

        // Таблиці для матриць
        modelA = new DefaultTableModel();
        modelB = new DefaultTableModel();
        modelX = new DefaultTableModel();
        tableA = new JTable(modelA);
        tableB = new JTable(modelB);
        tableX = new JTable(modelX);

        JPanel tablePanel = new JPanel(new GridLayout(1, 3));
        tablePanel.add(new JScrollPane(tableA));
        tablePanel.add(new JScrollPane(tableB));
        tablePanel.add(new JScrollPane(tableX));
        add(tablePanel, BorderLayout.CENTER);

        // Обробка кнопки завантаження файлу
        loadButton.addActionListener(e -> loadMatrices());

        // Обробка кнопки обчислення
        calculateButton.addActionListener(e -> calculateMatrix());

        setVisible(true);
    }

    // Завантаження матриць з файлу
    private void loadMatrices() {
        try {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                Scanner scanner = new Scanner(file);

                int n = Integer.parseInt(sizeInput.getText());
                if (n <= 0 || n > 15) throw new InvalidMatrixSizeException("Розмір матриці має бути від 1 до 15!");

                modelA.setRowCount(n);
                modelA.setColumnCount(n);
                modelB.setRowCount(n);
                modelB.setColumnCount(n);
                modelX.setRowCount(n);
                modelX.setColumnCount(n);

                // Заповнення матриць A та B
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        if (!scanner.hasNextDouble()) throw new IllegalArgumentException("Невірний формат вхідних даних");
                        modelA.setValueAt(scanner.nextDouble(), i, j);
                    }
                }
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        if (!scanner.hasNextDouble()) throw new IllegalArgumentException("Невірний формат вхідних даних");
                        modelB.setValueAt(scanner.nextDouble(), i, j);
                    }
                }
                scanner.close();
            }
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Файл не знайдено!", "Помилка", JOptionPane.ERROR_MESSAGE);
        } catch (InvalidMatrixSizeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Помилка розміру матриці", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Помилка формату вхідних даних!", "Помилка", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Обчислення матриці X
    private void calculateMatrix() {
        try {
            int n = Integer.parseInt(sizeInput.getText());
            if (n <= 0 || n > 15) throw new InvalidMatrixSizeException("Розмір матриці має бути від 1 до 15!");

            double[][] A = new double[n][n];
            double[][] B = new double[n][n];
            double[][] X = new double[n][n];

            // Заповнення матриць A і B
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    A[i][j] = (double) modelA.getValueAt(i, j);
                    B[i][j] = (double) modelB.getValueAt(i, j);
                }
            }

            // Обчислення матриці X
            for (int i = 0; i < n; i++) {
                double maxInRowB = findMaxInRow(B[i]);
                for (int j = 0; j < n; j++) {
                    X[i][j] = A[i][j] * maxInRowB;
                    modelX.setValueAt(X[i][j], i, j);
                }
            }

        } catch (InvalidMatrixSizeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Помилка розміру матриці", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Невірне числове значення!", "Помилка", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Пошук максимального значення у рядку матриці
    private double findMaxInRow(double[] row) {
        double max = row[0];
        for (double v : row) {
            if (v > max) max = v;
        }
        return max;
    }

    // Власне виключення для розміру матриці
    private static class InvalidMatrixSizeException extends ArithmeticException {
        public InvalidMatrixSizeException(String message) {
            super(message);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MatrixMultiplierApp::new);
    }
}
