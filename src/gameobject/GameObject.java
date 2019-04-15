/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameobject;

import game.GamePanel;
import java.awt.Graphics;

/**
 *
 * @author Kai
 */
public abstract class GameObject {
    // 長寬
    protected int width;
    protected int hight;
    
    // 座標 
    protected int x;
    protected int y;
    
    // 方向
    protected int dir;    
    
    // 判定屬性
    protected int currentX;
    protected int currentY;
    protected int xDest;
    protected int yDest;
    protected int xTemp;
    protected int yTemp;
    
    // 判定相交
    protected boolean left;
    protected boolean right;
    protected boolean up;
    protected boolean down;
    
    //判定戰鬥
    protected boolean hit;
    protected boolean heal;
    protected boolean atk;
    
    
    public abstract void paint(Graphics g);

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHight() {
        return hight;
    }
    
    public void setPosition(int x, int y){
        this.x = x;
        this.y = y;
    }
    
    public void setDirection(int dir){
        this.dir = dir;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
    }

    public void setHeal(boolean heal) {
        this.heal = heal;
    }

    public void setAtk(boolean atk) {
        this.atk = atk;
    }
    
    public boolean checkInMap() {
        return x + width > 0 || x - width < GamePanel.WIDTH || y + hight > 0 || y - hight < GamePanel.HIGHT ;
    }
    
    
}
