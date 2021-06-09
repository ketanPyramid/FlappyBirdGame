import flappyBird.Renderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class FlappyBird implements ActionListener, MouseListener, KeyListener {
    public static flappyBird.FlappyBird flappyBird;
    public final int WIDTH = 1200, HEIGHT = 800;
    public Rectangle bird;
    public Renderer renderer;
    public ArrayList<Rectangle> columns;
    public Random rand;
    public boolean gameOver, started;
    public int ticks, yMotion, score;

    public FlappyBird() {
        JFrame jFrame = new JFrame();
        Timer timer = new Timer(20, this);
        renderer = new Renderer();
        jFrame.add(renderer);
        jFrame.setTitle("Flappy Bird");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setSize(WIDTH, HEIGHT);
        jFrame.setResizable(false);
        jFrame.setVisible(true);
        jFrame.addMouseListener(this);
        jFrame.addKeyListener(this);

        bird = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20);
        columns = new ArrayList<Rectangle>();
        rand = new Random();
        addColumn(true);
        addColumn(true);
        addColumn(true);
        addColumn(true);
        timer.start();

    }

    public void addColumn(boolean start) {
        int space = 300;
        int width = 100;
        int height = 50 + rand.nextInt(300);
        if (start) {
            columns.add(new Rectangle(WIDTH + width + columns.size() * 300, HEIGHT - height - 120, width, height));
            columns.add(new Rectangle(WIDTH + width + (columns.size() - 1) * 300, 0, width, HEIGHT - height - space));
        } else {
            columns.add(new Rectangle(columns.get(columns.size() - 1).x + 600, HEIGHT - height - 120, width, height));
            columns.add(new Rectangle(columns.get(columns.size() - 1).x, 0, width, HEIGHT - height - space));
        }
    }

    public void paintColumn(Graphics g, Rectangle column) {
        g.setColor(Color.green.darker());
        g.fillRect(column.x, column.y, column.width, column.height);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int speed = 10;
        ticks++;
        if (started) {
            for (int i = 0; i < columns.size(); i++) {
                Rectangle column = columns.get(i);
                column.x -= speed;
            }
            if (ticks % 2 == 0 && yMotion < 15) {
                yMotion += 2;
            }
            for (int i = 0; i < columns.size(); i++) {
                Rectangle column = columns.get(i);
                if (column.x + column.width < 0) {
                    columns.remove(column);
                    if (column.y == 0) {
                        addColumn(false);
                    }
                }
            }
            bird.y += yMotion;
            for (Rectangle column : columns) {
                if (column.y == 0 && bird.x == column.x + column.width) {
                    score++;
                }

                if (column.intersects(bird)) {
                    gameOver = true;
                    if (bird.x <= column.x) {
                        bird.x = column.x - bird.width;
                    } else {
                        if (column.y != 0) {
                            bird.y = column.y - bird.height;
                        } else if (bird.y < column.height) {
                            bird.y = column.height;
                        }
                    }
                }
            }
            if (bird.y > HEIGHT - 120 || bird.y <= 0) {
                gameOver = true;
            }
            if (bird.y + yMotion >= HEIGHT - 120) {
                bird.y = HEIGHT - 120 - bird.height;
                gameOver = true;
            }
        }
        renderer.repaint();

    }

    public void repaint(Graphics g) {
        g.setColor(Color.cyan.brighter());
        g.fillRect(0, 0, WIDTH, HEIGHT);

        g.setColor(Color.orange.darker());
        g.fillRect(0, HEIGHT - 120, WIDTH, 120);

        g.setColor(Color.green);
        g.fillRect(0, HEIGHT - 140, WIDTH, 20);

        g.setColor(Color.red);
        g.fillRect(bird.x, bird.y, bird.width, bird.height);

        for (Rectangle column : columns) {
            paintColumn(g, column);
        }

        g.setColor(Color.white);
        g.setFont(new Font("Arial", 1, 100));

        if (!started) {
            g.drawString("Click to Start!", 275, HEIGHT / 2 - 50);
        }
        if (gameOver) {
            g.drawString("Game Over!", 300, HEIGHT / 2 - 50);
        }
        if (!gameOver && started) {
            g.drawString(String.valueOf(score), WIDTH / 2, 100);
        }
    }

    public static void main(String[] args) {

        flappyBird = new flappyBird.FlappyBird();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        jump();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            jump();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }
}

