package gamestate;

import resourcemanage.ImageResource;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

public class PuzzleGame extends GameState{
    PuzzleBoard board;

    protected PuzzleGame(GameStateManager gsm) {
        super(gsm);
        init();
    }

    class PuzzleBoard extends Canvas{
        public PuzzleBoard(){
            JFrame test = new JFrame("test");
            test.setPreferredSize(new Dimension(300, 300));
            test.setMaximumSize(new Dimension(300, 300));
            test.setMinimumSize(new Dimension(300, 300));
            test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            test.setResizable(false);
            test.setLocationRelativeTo(null);
            test.setVisible(true);
        }
    }

    @Override
    public GameState getInstance() {
        return null;
    }

    @Override
    public void init() {
        board = new PuzzleBoard();
    }

    @Override
    public void tick() {

    }

    @Override
    public void event() {

    }

    @Override
    public void render(Graphics g) {

    }

    @Override
    public void keyPressed(int k) {
        if(k == KeyEvent.VK_ESCAPE){
            gsm.setState(GameStateManager.LEVEL1_STATE);
        }

    }

    @Override
    public void keyReleased(int k) {

    }

    @Override
    public void mousePressed(int x, int y) {

    }

    @Override
    public void mouseDragged(int x, int y) {

    }
}
