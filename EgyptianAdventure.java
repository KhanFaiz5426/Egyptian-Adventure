import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class EgyptianAdventure extends JPanel implements ActionListener, KeyListener {
    int boardWidth = 360;
    int boardHeight = 640;

    Image backgroundImg;
    Image falconImg;
    Image topObeliskImg;
    Image bottomObeliskImg;

    int falconX = boardWidth/8;
    int falconY = boardWidth/2;
    int falconWidth = 34;
    int falconHeight = 24;

    class Falcon {
        int x = falconX;
        int y = falconY;
        int width = falconWidth;
        int height = falconHeight;
        Image img;

        Falcon(Image img) {
            this.img = img;
        }
    }

    int obeliskX = boardWidth;
    int obeliskY = 0;
    int obeliskWidth = 64;
    int obeliskHeight = 512;
    
    class Obelisk {
        int x = obeliskX;
        int y = obeliskY;
        int width = obeliskWidth;
        int height = obeliskHeight;
        Image img;
        boolean passed = false;

        Obelisk(Image img) {
            this.img = img;
        }
    }

    Falcon falcon;
    int velocityX = -4;
    int velocityY = 0;
    int gravity = 1;

    ArrayList<Obelisk> obelisks;
    Random random = new Random();

    Timer gameLoop;
    Timer placeObeliskTimer;
    boolean gameOver = false;
    double score = 0;

    EgyptianAdventure() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setFocusable(true);
        addKeyListener(this);

        backgroundImg = new ImageIcon(getClass().getResource("./background.png")).getImage();
        falconImg = new ImageIcon(getClass().getResource("./falcon.png")).getImage();
        topObeliskImg = new ImageIcon(getClass().getResource("./upsideDownObelisk.png")).getImage();
        bottomObeliskImg = new ImageIcon(getClass().getResource("./obelisk.png")).getImage();

        falcon = new Falcon(falconImg);
        obelisks = new ArrayList<Obelisk>();

        placeObeliskTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              placeObelisks();
            }
        });
        placeObeliskTimer.start();
        
		gameLoop = new Timer(1000/60, this); 
        gameLoop.start();
	}
    
    void placeObelisks() {
        int randomObeliskY = (int) (obeliskY - obeliskHeight/4 - Math.random()*(obeliskHeight/2));
        int openingSpace = boardHeight/4;
    
        Obelisk topObelisk = new Obelisk(topObeliskImg);
        topObelisk.y = randomObeliskY;
        obelisks.add(topObelisk);
    
        Obelisk bottomObelisk = new Obelisk(bottomObeliskImg);
        bottomObelisk.y = topObelisk.y  + obeliskHeight + openingSpace;
        obelisks.add(bottomObelisk);
    }
    
    
    public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}

	public void draw(Graphics g) {
        g.drawImage(backgroundImg, 0, 0, this.boardWidth, this.boardHeight, null);

        g.drawImage(falconImg, falcon.x, falcon.y, falcon.width, falcon.height, null);

        for (int i = 0; i < obelisks.size(); i++) {
            Obelisk obelisk = obelisks.get(i);
            g.drawImage(obelisk.img, obelisk.x, obelisk.y, obelisk.width, obelisk.height, null);
        }

        g.setColor(Color.white);

        g.setFont(new Font("Arial", Font.PLAIN, 32));
        if (gameOver) {
            g.drawString("Game Over: " + String.valueOf((int) score), 10, 35);
        }
        else {
            g.drawString(String.valueOf((int) score), 10, 35);
        }
        
	}

    public void move() {
        velocityY += gravity;
        falcon.y += velocityY;
        falcon.y = Math.max(falcon.y, 0);

        for (int i = 0; i < obelisks.size(); i++) {
            Obelisk obelisk = obelisks.get(i);
            obelisk.x += velocityX;

            if (!obelisk.passed && falcon.x > obelisk.x + obelisk.width) {
                score += 0.5;
                obelisk.passed = true;
            }

            if (collision(falcon, obelisk)) {
                gameOver = true;
            }
        }

        if (falcon.y > boardHeight) {
            gameOver = true;
        }
    }

    boolean collision(Falcon a, Obelisk b) {
        return a.x < b.x + b.width &&
               a.x + a.width > b.x &&
               a.y < b.y + b.height &&
               a.y + a.height > b.y;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver) {
            placeObeliskTimer.stop();
            gameLoop.stop();
        }
    }  

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            velocityY = -9;

            if (gameOver) {
                falcon.y = falconY;
                velocityY = 0;
                obelisks.clear();
                gameOver = false;
                score = 0;
                gameLoop.start();
                placeObeliskTimer.start();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}
