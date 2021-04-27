import javax.swing.JPanel;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener{
    //Instead of making an object, inherit from JPanel class.
    //Implementing ActionListener to define what to do when user performs certain action.
    static final int width = 600; //Screen size.
    static final int height = 600;
    static final int object_size = 25; //Size for objects in the game.
    static final int units = (width * height) / object_size;
    static final int delay = 90; //Delay for timer (high number = slow game).
    final int x[] = new int[units]; //Snake bodypart coordinates.
    final int y[] = new int[units];
    int bodyParts = 5; //Initial snake size.
    int applesEaten;
    int appleX; //Coordinates for the apple (random).
    int appleY;
    char direction = 'R'; //Snake starts by going right.
    boolean running = false;
    Timer timer;
    Random random;

    GamePanel(){ //Constructor.
        random = new Random();
        this.setPreferredSize(new Dimension(width, height));
        this.setBackground(Color.black); //Set background color.
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter()); //Adapter for receiving keyboard events.
        startGame();
    }
    public void startGame(){
        newApple(); //Create a new apple.
        running = true;
        timer = new Timer(delay, this); //Using the ActionListener interface.
        timer.start();
    }
    public void paintComponent(Graphics g){ //paintComponent() instead of paint() because we have moving shapes.
        super.paintComponent(g); //super is needed because we're getting the paintComponent() from JPanel.
        draw(g);
    }
    public void draw(Graphics g){
        if (running){
            for (int i=0; i < (height/object_size); i++){
                g.drawLine(i*object_size, 0, i*object_size, height); //Drawing a grid (optional).
                g.drawLine(0, i*object_size, width, i*object_size);
            }
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, object_size, object_size); //Drawing the apple.

            //Drawing the snake.
            for (int i=0; i < bodyParts; i++){
                if (i == 0) { //Head of the snake.
                    g.setColor(Color.gray);
                    g.fillRect(x[i], y[i], object_size, object_size);
                }
                else { //Body of the snake.
                    g.setColor(Color.white);
                    g.fillRect(x[i], y[i], object_size, object_size);
                }
            }
            //Displaying score on the screen.
            g.setColor(Color.green);
            g.setFont(new Font("Verdana", Font.BOLD, 15));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + applesEaten, (width - metrics.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize()); //Score at the top.
        }
        else {
            gameOver(g);
        }
    }
    public void newApple(){ //Generate coordinates for a new apple.
        appleX = random.nextInt((int)(width / object_size)) * object_size; //Multiply by object_size to place apple evenly within the grid.
        appleY = random.nextInt((int)(height / object_size)) * object_size;
    }
    public void move(){
        for (int i=bodyParts; i>0; i--){ //Iterate through snake bodyparts.
            x[i] = x[i-1]; //Shifting the coordinates in the array with 1 spot.
            y[i] = y[i-1];
        }
        
        switch (direction){ //Examine the direction variable (U-up D-down L-left R-right).
            case 'U':
                y[0] = y[0] - object_size; //Y-coordinate for the head of the snake.
                break;
            case 'D':
                y[0] = y[0] + object_size;
                break;
            case 'L':
                x[0] = x[0] - object_size; //X-coordinate for the head of the snake.
                break;
            case 'R':
                x[0] = x[0] + object_size;
                break;
        }
    }
    public void checkApple(){
        if ((x[0] == appleX) && (y[0] == appleY)){ //Check if head coordinate is the same as of the apple.
            bodyParts++; //Add one body part.
            applesEaten++; //Increase score.
            newApple();
        }
    }
    public void checkCollision(){
        //If head has collided with the body.
        for (int i=bodyParts; i > 0; i--){
            if ((x[0] == x[i]) && (y[0] == y[i])){
                running = false; //Game over.
            }
        }
        //If head touches left border.
        if (x[0] < 0){
            running = false;
        }
        //If head touches right border.
        if (x[0] > width){
            running = false;
        }
        //If head touches top border.
        if (y[0] < 0){
            running = false;
        }
        //If head touches bottom border.
        if (y[0] > height){
            running = false;
        }
        if (!running){ //If running is false, stop the timer.
            timer.stop();
        }
    }
    public void gameOver(Graphics g){
        //Setting up the "game over" text.
        g.setColor(Color.red);
        g.setFont(new Font("Verdana", Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (width - metrics1.stringWidth("Game Over")) / 2, height / 2); //Centering the text.

        //Displaying score on the "game over" screen.
        g.setColor(Color.green);
        g.setFont(new Font("Verdana", Font.BOLD, 15));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten, (width - metrics2.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());
    }
    @Override
    public void actionPerformed(ActionEvent e){
        if (running){ //Check if the game is running.
            move();
            checkApple();
            checkCollision();
        }
        repaint(); //When the game stops running, repaint it.
    }
    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch(e.getKeyCode()){ //Limiting the user to do only 90 degree turns.
                case KeyEvent.VK_LEFT:
                    if (direction != 'R'){ 
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L'){
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D'){
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U'){
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}