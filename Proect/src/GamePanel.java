import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    public static final int SCREEN_WEIGTH = 600;
    public static final int SCREEN_HEIGHT = 600;
    public static final int UNIT_SIZE = 20 * 2;
    public static final int GAME_UNITS = (SCREEN_WEIGTH * SCREEN_HEIGHT) / UNIT_SIZE;
    public static final int DELAY = 75;
    public static final int x[] = new int[GAME_UNITS];
    public final int y[] = new int[GAME_UNITS];
    public static int bodyParts = 3;
    public static int applesEaten;
    public static int appleX;
    public static int appleY;
    public static char direction = 'R';
    public static boolean running = false;
    public static Timer timer;
    public static Random random;

    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WEIGTH, SCREEN_HEIGHT + 100));// Размер панели
        this.setBackground(new Color(245, 204, 91)); // Цвет фона
        this.setFocusable(true); //Фокусировка
        this.addKeyListener(new MyKeyAdapter()); // Вызов метода управления
        startGame();// Вызов метода старта игры
    }

    public void startGame() {
        newApple(); //Вызов метода отрисовки нового яблока
        running = true;
        timer = new Timer(DELAY, this);
        timer.start(); //Запуск таймера
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {

        if (running) {
            //Вывод игровой сетки(
            for (int i = 0; i < (SCREEN_HEIGHT) / UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WEIGTH, i * UNIT_SIZE);
            }
            g.drawLine(0, SCREEN_HEIGHT, SCREEN_WEIGTH, SCREEN_HEIGHT);
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
            //)
            //Вывод змейки
            for (int i = 0; i < bodyParts; i++) {
                //Вывод головы змейки
                if (i == 0) {
                    g.setColor(new Color(35, 255, 0));
                    g.fillOval(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                    if (x[0] > x[1]) {
                        g.fillRect(x[i], y[i], UNIT_SIZE - 20, UNIT_SIZE);
                    }
                    if (x[1] > x[0]) {
                        g.fillRect(x[0] + 20, y[0], UNIT_SIZE / 2, UNIT_SIZE);
                    }
                    if (y[0] > y[1]) {
                        g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE - 20);
                    }
                    if (y[1] > y[0]) {
                        g.fillRect(x[0], y[0] + 20, UNIT_SIZE, UNIT_SIZE / 2);
                    }
                } else {
                    //Вывод тела змейки
                    g.setColor(new Color(130, 255, 121));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            //Вывод счетчика
            g.setColor(Color.blue);
            g.setFont(new Font("Segoe print", Font.ITALIC + Font.BOLD, SCREEN_WEIGTH / 10));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Счет" + applesEaten, (SCREEN_WEIGTH - metrics.stringWidth("Счет" + applesEaten)) / 2, SCREEN_HEIGHT + 60);
        } else {
            gameOver(g);
        }
    }

    public void newApple() {
        appleX = random.nextInt((int) (SCREEN_WEIGTH / UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    public void move() { //Метод задания положения змейки
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        switch (direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }

    public void checkApple() { //Метод поедания яблока
        if ((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }

    public void checkCollisions() {
        // проверка сталкивается ли голова с телом
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }
        // проверка сталкиваетс ли голова с левой границей
        if (x[0] < 0) {
            running = false;
        }
        // проверка сталкиваетс ли голова с правой границей
        if (x[0] > SCREEN_WEIGTH - 1) {
            running = false;
        }
        // проверка сталкиваетс ли голова с нижней границей
        if (y[0] > SCREEN_HEIGHT - 1) {
            running = false;
        }
        // проверка сталкиваетс ли голова с верхней границей
        if (y[0] < 0) {
            running = false;
        }
        if (!running) {
            timer.stop();
        }
    }

    public void gameOver(Graphics g) {
        g.setColor(Color.blue);
        g.setFont(new Font("Segoe print", Font.ITALIC + Font.BOLD, SCREEN_WEIGTH / 10));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Игра закончена", (SCREEN_WEIGTH - metrics.stringWidth("Игра закончена")) / 2, SCREEN_HEIGHT / 2);
        g.setColor(Color.blue);
        g.setFont(new Font("Segoe print", Font.ITALIC + Font.BOLD, SCREEN_WEIGTH / 10));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Счет" + applesEaten, (SCREEN_WEIGTH - metrics1.stringWidth("Счет" + applesEaten)) / 2, SCREEN_HEIGHT - 200);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter { //Класс упрвления змейкой
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                //Движение влево
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                    //Движение вправо
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                    //Движение вверх
                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                    //Движение вниз
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}

