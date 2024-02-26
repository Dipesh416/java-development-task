 package com.veritech;
 
 import javax.swing.*;
 import java.awt.*;
 import java.awt.event.ActionEvent;
 import java.awt.event.ActionListener;
 import java.awt.event.KeyAdapter;
 import java.awt.event.KeyEvent;
 import java.util.ArrayList;
 import java.util.List;
 import java.util.Random;

 public class SnakeGame extends JPanel implements ActionListener {

     private static final int BOARD_WIDTH = 600;
     private static final int BOARD_HEIGHT = 600;
     private static final int UNIT_SIZE = 25;
     private static final int GAME_UNITS = (BOARD_WIDTH * BOARD_HEIGHT) / UNIT_SIZE;
     private static final int DELAY = 150;

     private final int[] x = new int[GAME_UNITS];
     private final int[] y = new int[GAME_UNITS];

     private int bodyParts = 6;
     private int foodEaten;
     private int foodX;
     private int foodY;

     private char direction = 'R';
     private boolean running = false;

     private Timer timer;

     public SnakeGame() {
         this.setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
         this.setBackground(Color.black);
         this.setFocusable(true);
         this.addKeyListener(new MyKeyAdapter());
         startGame();
     }

     public void startGame() {
         newFood();
         running = true;
         timer = new Timer(DELAY, this);
         timer.start();
     }

     public void paintComponent(Graphics g) {
         super.paintComponent(g);
         draw(g);
     }

     public void draw(Graphics g) {
         if (running) {
             for (int i = 0; i < BOARD_HEIGHT / UNIT_SIZE; i++) {
                 g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, BOARD_HEIGHT);
                 g.drawLine(0, i * UNIT_SIZE, BOARD_WIDTH, i * UNIT_SIZE);
             }
             g.setColor(Color.red);
             g.fillOval(foodX, foodY, UNIT_SIZE, UNIT_SIZE);

             for (int i = 0; i < bodyParts; i++) {
                 if (i == 0) {
                     g.setColor(Color.green);
                     g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                 } else {
                     g.setColor(new Color(45, 180, 0));
                     g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                 }
             }
             g.setColor(Color.blue);
             g.setFont(new Font("Ink Free", Font.BOLD, 40));
             FontMetrics metrics = getFontMetrics(g.getFont());
             g.drawString("Score: " + foodEaten, (BOARD_WIDTH - metrics.stringWidth("Score: " + foodEaten)) / 2,
            		 g.getFont().getSize());
         } else {
             gameOver(g);
         }
     }

     public void newFood() {
         foodX = new Random().nextInt((int) (BOARD_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
         foodY = new Random().nextInt((int) (BOARD_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
     }

     public void move() {
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

     public void checkFood() {
         if ((x[0] == foodX) && (y[0] == foodY)) {
             bodyParts++;
             foodEaten++;
             newFood();
         }
     }

     public void checkCollisions() {
         // Check if head collides with body
         for (int i = bodyParts; i > 0; i--) {
             if ((x[0] == x[i]) && (y[0] == y[i])) {
                 running = false;
             }
         }

         // Check if head touches left border
         if (x[0] < 0) {
             running = false;
         }

         // Check if head touches right border
         if (x[0] >= BOARD_WIDTH) {
             running = false;
         }

         // Check if head touches top border
         if (y[0] < 0) {
             running = false;
         }

         // Check if head touches bottom border
         if (y[0] >= BOARD_HEIGHT) {
             running = false;
         }
         if (!running) {
             timer.stop();
         }
     }

     public void gameOver(Graphics g) {
         // Game Over Text
         g.setColor(Color.blue);
         g.setFont(new Font("Ink Free", Font.BOLD, 75));
         FontMetrics metrics = getFontMetrics(g.getFont());
         g.drawString("Game Over", (BOARD_WIDTH - metrics.stringWidth("Game Over")) / 2, BOARD_HEIGHT / 2);
         // Score
         g.setColor(Color.blue);
         g.setFont(new Font("Ink Free", Font.BOLD, 40));
         g.drawString("Score: " + foodEaten, (BOARD_WIDTH - metrics.stringWidth("Score: " + foodEaten)) / 2,
        		 g.getFont().getSize());
         // Restart Message
         g.setColor(Color.blue);
         g.setFont(new Font("Ink Free", Font.BOLD, 30));
         g.drawString("Press Space to Restart", (500+BOARD_WIDTH - metrics.stringWidth("Press Space to Restart")) /
        		 2, BOARD_HEIGHT / 2 + 100);
         System.out.println("mterics "+metrics.stringWidth("Game Over"));
     }

     @Override
     public void actionPerformed(ActionEvent e) {
         if (running) {
             move();
             checkFood();
             checkCollisions();
         }
         repaint();
     }

     public class MyKeyAdapter extends KeyAdapter {
         @Override
         public void keyPressed(KeyEvent e) {
             switch (e.getKeyCode()) {
                 case KeyEvent.VK_LEFT:
                     if (direction != 'R') {
                         direction = 'L';
                     }
                     break;
                 case KeyEvent.VK_RIGHT:
                     if (direction != 'L') {
                         direction = 'R';
                     }
                     break;
                 case KeyEvent.VK_UP:
                     if (direction != 'D') {
                         direction = 'U';
                     }
                     break;
                 case KeyEvent.VK_DOWN:
                     if (direction != 'U') {
                         direction = 'D';
                     }
                     break;
                 case KeyEvent.VK_SPACE:
                     if (!running) {
                         resetGame();
                     }
                     break;
             }
         }
     }

     public void resetGame() {
         x[0] = BOARD_WIDTH / 2;
         y[0] = BOARD_HEIGHT / 2;
         direction = 'R';
         bodyParts = 6;
         foodEaten = 0;
         running = true;
         timer.start();
         newFood();
         repaint();
     }

     public static void main(String[] args) {
         JFrame frame = new JFrame("Snake Game");
         SnakeGame snakeGame = new SnakeGame();
         frame.add(snakeGame);
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         frame.pack(); 
         frame.setVisible(true); 
         }
     }
