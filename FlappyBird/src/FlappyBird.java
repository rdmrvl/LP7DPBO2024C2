import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {
    JButton startButton;
    JButton restartButton;
    int frameWidht = 360;
    int frameHeight = 640;
    int playerStartPosX = frameWidht / 8;
    int playerStartPosY = frameHeight / 2;

    int playerWidht = 34;
    int playerHeight = 24;
    Player player;

    //images attributes
    Image backgroundImage;
    Image birdImage;
    Image lowerPipeImage;
    Image upperPipeImage;

    //game logic
    Timer gameloop;
    Timer pipesCooldown;

    int gravity = 1;
    boolean gameOver = false;
    int score = 0;
    int highScore = 0;

    //pipes
    int pipeStartPosX = frameWidht;
    int pipeStartPosY = 0;
    int pipeWidht = 64;
    int pipeHeight = 512;
    ArrayList<Pipe> pipes;


    public FlappyBird(){
        setPreferredSize(new Dimension(frameWidht, frameHeight));
        setFocusable(true);
        addKeyListener(this);

        startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });
        add(startButton);

        restartButton = new JButton("Restart");
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restartGame();
                startButton.setVisible(true);
                restartButton.setVisible(false);
            }
        });
        restartButton.setVisible(false);
        add(restartButton);

        //load images
        backgroundImage = new ImageIcon(getClass().getResource("assets/background.png")).getImage();
        birdImage = new ImageIcon(getClass().getResource("assets/bird.png")).getImage();
        lowerPipeImage = new ImageIcon(getClass().getResource("assets/lowerPipe.png")).getImage();
        upperPipeImage = new ImageIcon(getClass().getResource("assets/upperPipe.png")).getImage();

        player = new Player(playerStartPosX, playerStartPosY, playerWidht, playerHeight, birdImage);
        pipes = new ArrayList<Pipe>();

        pipesCooldown = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!gameOver) {
                    placePipes();
                }
            }
        });

        gameloop = new Timer(1000/60, this);
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {
        g.drawImage(backgroundImage, 0, 0, frameWidht, frameHeight, null);

        g.drawImage(player.getImage(), player.getPosX(), player.getPosY(), player.getWidth(), player.getHeight(), null);

        for(int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.getImage(), pipe.getPosX(), pipe.getPosY(), pipe.getWidht(), pipe.getHeight(), null);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("Score: " + score / 2, 10, 30);
        g.drawString("High Score: " + highScore / 2, 10, 60);

        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("Game Over", frameWidht / 2 - 100, frameHeight / 2);
        }

    }

    public void move(){
        if (!gameOver) {
            player.setVelocityY(player.getVelocityY() + gravity);
            player.setPosY(player.getPosY() + player.getVelocityY());
            player.setPosY(Math.max(player.getPosY(), 0));

            for (int i = 0; i < pipes.size(); i++) {
                Pipe pipe = pipes.get(i);
                pipe.setPosX(pipe.getPosX() + pipe.getVelocityX());

                if (player.getPosX() + player.getWidth() > pipe.getPosX() && player.getPosX() < pipe.getPosX() + pipe.getWidht()) {
                    if (player.getPosY() < pipe.getPosY() + pipe.getHeight() && player.getPosY() + player.getHeight() > pipe.getPosY()) {
                        gameOver = true;
                    }
                }

                if (pipe.getPosX() + pipe.getWidht() < player.getPosX()) {
                    if (!pipe.isPassed()) {
                        pipe.setPassed(true);
                        score++;
                        highScore = Math.max(highScore, score); // Update high score
                    }
                }
            }

            if (player.getPosY() + player.getHeight() > frameHeight || player.getPosY() < 0) {
                gameOver = true;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();

    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE){
            player.setVelocityY(-10);
        }

        if (gameOver && e.getKeyCode() == KeyEvent.VK_ENTER) {
            restartGame();
            startButton.setVisible(true);
            restartButton.setVisible(false);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public void placePipes(){
        int randomPosY = (int) (pipeStartPosY - pipeHeight/4 - Math.random() * (pipeHeight/2));
        int openingSpace = frameHeight/4;

        Pipe upperPipe = new Pipe(pipeStartPosX, randomPosY, pipeWidht, pipeHeight, upperPipeImage);
        pipes.add(upperPipe);

        Pipe lowerPipe = new Pipe(pipeStartPosX, (randomPosY + openingSpace + pipeHeight), pipeWidht, pipeHeight, lowerPipeImage);
        pipes.add(lowerPipe);
    }

    public void restartGame() {
        player.setPosY(playerStartPosY);
        pipes.clear();
        score = 0;
        gameOver = false;
    }

    public void startGame() {
        startButton.setVisible(false);
        restartButton.setVisible(true);
        player.setPosY(playerStartPosY);
        pipes.clear();
        score = 0;
        gameOver = false;
        gameloop.start();
        pipesCooldown.start();
        requestFocus();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Flappy Bird");
        FlappyBird game = new FlappyBird();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
