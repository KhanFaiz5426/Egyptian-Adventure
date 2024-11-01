import javax.swing.*;

public class App {
    public static void main(String[] args) {
        int boardWidth = 360;
        int boardHeight = 640;

        JFrame frame = new JFrame("Egyptian Adventure");
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        EgyptianAdventure egyptianAdventure = new EgyptianAdventure();
        frame.add(egyptianAdventure);
        frame.pack();
        egyptianAdventure.requestFocus();
        frame.setVisible(true);
    }
}
