import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class RandomMovingStrings extends JPanel {
    private static final String[] STRINGS = {
        "Hello", "Java", "Random", "Moving", "Strings", "Animation"
    };
    private static final int FRAME_WIDTH = 800;
    private static final int FRAME_HEIGHT = 600;
    private static final int DELAY = 30; // Затримка анімації у мілісекундах

    private int x, y; // Координати рядка
    private int xSpeed, ySpeed; // Швидкість руху
    private String currentString; // Поточний рядок

    public RandomMovingStrings() {
        Random random = new Random();
        x = random.nextInt(FRAME_WIDTH);
        y = random.nextInt(FRAME_HEIGHT);
        xSpeed = random.nextInt(11) - 5; // Швидкість між -5 і 5
        ySpeed = random.nextInt(11) - 5; // Швидкість між -5 і 5
        currentString = STRINGS[random.nextInt(STRINGS.length)]; // Випадковий рядок

        Timer timer = new Timer(DELAY, e -> moveAndRepaint());
        timer.start();
    }

    private void moveAndRepaint() {
        x += xSpeed;
        y += ySpeed;

        // Змінюємо напрямок руху, якщо рядок виходить за межі фрейму
        if (x < 0 || x > FRAME_WIDTH - 50) xSpeed = -xSpeed;
        if (y < 0 || y > FRAME_HEIGHT - 50) ySpeed = -ySpeed;

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString(currentString, x, y);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Random Moving Strings");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);

        RandomMovingStrings movingStrings = new RandomMovingStrings();
        frame.add(movingStrings);
        frame.setVisible(true);
    }
}
