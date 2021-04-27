import javax.swing.JFrame;

public class GameFrame extends JFrame{
    
    GameFrame(){
        this.add(new GamePanel());
        this.setTitle("Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false); //Is the window resizable.
        this.pack(); //Fits all added components into the frame.
        this.setVisible(true);
        this.setLocationRelativeTo(null); //Window appears in the middle of the screen.
    }
}