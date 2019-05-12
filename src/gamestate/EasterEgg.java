package gamestate;

import game.Game;
import game.Updater;
import gameobject.*;
import resourcemanage.ImageResource;
import resourcemanage.SoundResource;
import scene.ParallaxBackGround;
import scene.Texture;

import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class EasterEgg extends GameState {
    private final int BACKGROUND2 = 3;
    private final int FARMOUNTAIN = 4;
    private final int NEARMOUNTAINS = 5;
    private final int ROAD = 6;
    private Keys key;
    private BufferedImage background2, restart;
    private ParallaxBackGround farMountain;
    private ParallaxBackGround nearMountain;
    private Snow[] snow;
    private AudioClip bgm;
    private ArrayList<Floor> floor;
    private ArrayList<NostaligaItem> itemList;
    private ActionPlayer player;
    private Npc doppelganger;
    private int keyPressed;
    private BufferedReader npcScript;
    private ArrayList<String> npcLine;

    public static EasterEgg EasterEgg;


    protected EasterEgg(GameStateManager gsm) {
        super(gsm);
        init();
        bgm.loop();
    }

    @Override
    public EasterEgg getInstance() {
        if(EasterEgg == null){
            EasterEgg = new EasterEgg(GameStateManager.getInstance());
        }
        return EasterEgg;
    }
    private class NostaligaItem extends GameObject{
        private BufferedImage img;
        private boolean falling, collectable;
        private int gravity;

        public NostaligaItem(int x, int y, ObjectID id, BufferedImage img) {
            super(x, y, id);
            this.img = img;
            width = 30;
            height = 30;
            gravity = 1;
            collectable = false;
            falling = true;
        }

        @Override
        public void setFalling(boolean falling) {
            this.falling = falling;
        }

        public boolean isCollectable() {
            return collectable;
        }

        public void setCollectable(boolean collectable) {
            this.collectable = collectable;
        }

        @Override
        public void tick() {
            x += xVel;
            y += yVel;
            if(falling || jumping){
                yVel += gravity;
                if(yVel > 5){
                    yVel = 5;
                }
            }
        }

        @Override
        public void render(Graphics g) {
            g.drawImage(img,x,y,width,height,null);
        }

        public Rectangle getBound(){
            return new Rectangle(x,y,width,height);
        }
    }
    private class Floor extends GameObject {
        private BufferedImage img;
        public Floor(int x, int y, ObjectID id) {
            super(x, y, id);
            img = Texture.getInstance().floor[0];
            width = 128;
            height = 32;
        }
        @Override
        public void tick() {

        }
        @Override
        public void render(Graphics g) {
            g.drawImage(img,x,y,128,32,null);
        }
    }
    private class Snow implements Updater{
        double x = Math.random() * Game.WIDTH;
        double y = (Math.random() *-500) +50;
        double yVel = Math.random() * 2.5 + 0.5 ;
        double xVel = Math.random() * 2 ;

        @Override
        public void tick(){
            y += yVel;
            x -= xVel;
            if( y > Game.HEIGHT){
                y = Math.random() * -400;
            }
            if(x < 0){
                x = Game.WIDTH;
            }
        }

        @Override
        public void render(Graphics g){
            g.setColor(java.awt.Color.white);
            g.drawLine((int)x, (int)y, (int)x, (int)y+3);
        }
    }

    @Override
    public void init() {
        snow  = new Snow[200];
        bgm = SoundResource.getInstance().getClip("/Art/Sound Effect/Level2.wav");
        for (int i = 0; i < snow.length; i++) {
            snow[i] = new Snow();
        }
        //script
        npcLine = new ArrayList<>();
        try {
            npcScript = new BufferedReader(new FileReader(""));
            while(npcScript.ready()){
                npcLine.add(npcScript.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //inputMethod
        key = new Keys();
        //background
        background2 = Texture.getInstance().background[BACKGROUND2];
        restart = ImageResource.getInstance().getImage("/Art/backGround/restart.png");
        farMountain = new ParallaxBackGround(Texture.getInstance().background[FARMOUNTAIN],1);
        nearMountain = new ParallaxBackGround(Texture.getInstance().background[NEARMOUNTAINS],1);
        floor = new ArrayList<>();
        //player
        player = new ActionPlayer(276,450,ObjectID.PLAYER,5);
        player.setHeight(player.getHeight()/2);
        player.setWidth(player.getWidth()/2);
        //enemy
        doppelganger = new Npc(0,50,ObjectID.OBSTACLE,3, player);
        //steps
        floor.add(new Floor(276,450,ObjectID.OBSTACLE));
        floor.add(new Floor(576,300,ObjectID.OBSTACLE));
        floor.add(new Floor(896,450,ObjectID.OBSTACLE));
        for(int i = 0; i < Game.WIDTH + 128; i+=128){ // add 128 because the lower right corner issue// Cannot jump
            floor.add(new Floor(i,666,ObjectID.OBSTACLE));
        }
        //item
        itemList = new ArrayList<>();
        itemList.add(new NostaligaItem((int)(Math.random()*Game.WIDTH/2),0,ObjectID.HEART,ImageResource.getInstance().getImage("/Art/Game Material/heart.png")));
        itemList.add(new NostaligaItem(Game.WIDTH/2 + (int)((Math.random()*(Game.WIDTH - Game.WIDTH/2 - 50))),0,ObjectID.HEART,ImageResource.getInstance().getImage("/Art/Game Material/heart.png")));
    }
    @Override
    public void tick() {
        event();
        key.update();
        player.tick();
        doppelganger.tick();
        for(int i = 0; i < floor.size(); i++){
            floor.get(i).tick();
        }
        for (Snow snow1 : snow) {
            snow1.tick();
        }
        for( NostaligaItem item : itemList){
            item.tick();
        }
    }

    @Override
    public void event() {
        player.checkBorder();
        for(int i = 0 ; i < floor.size(); i ++){
            //TOP COLLISION
            if(player.getTop().intersects(floor.get(i).getBound())){
                player.setY(floor.get(i).getY() +34);
                player.setyVel(0);
            }
            //BOT COLLISION
            if(player.getBot().intersects(floor.get(i).getBound())){
                player.setY(floor.get(i).getY() - player.getHeight());
                player.setyVel(0);
                player.setFalling(false);
                player.setJumping(false);
            }else{
                player.setFalling(true);
            }
            //RIGHT COLLISION
            if(player.getRight().intersects(floor.get(i).getBound())){
                player.setX(floor.get(i).getX() - player.getWidth() + 33);
            }
            //LEFT COLLISION
            if(player.getLeft().intersects(floor.get(i).getBound())){
                player.setX(floor.get(i).getX() + floor.get(i).getWidth() - 23);
            }
            //NPC COLLISION with FLOOR
            if(doppelganger.npcExhausted() &&doppelganger.getBound().intersects(floor.get(i).getBound())){
                doppelganger.setY(floor.get(i).getY() - doppelganger.getHeight()+ 15);
                doppelganger.setyVel(0);
                doppelganger.setFalling(false);
            }

        }
        //Player/Enemy/Item COLLISION
        if(player.getBound().intersects(doppelganger.getBound()) && !doppelganger.getIsCollision()){
           player.setLifeC(player.getLifeC()-1);
        }
        //Item COLLISION
        if(!itemList.isEmpty()){
            for(int i = 0; i < itemList.size(); i ++){
                for(Floor floor: floor){
                    if(itemList.get(i).getBound().intersects(floor.getBound())){
                        itemList.get(i).setyVel(0);
                        itemList.get(i).setFalling(false);
                        itemList.get(i).setCollectable(true);
                    }
                }
                if(player.getBound().intersects(itemList.get(i).getBound())){
                    if(itemList.get(i).isCollectable())
                    itemList.remove(i);
                    break;
                }
            }
        }
        //Item Generation && Enemy heart reduction
        if(itemList.isEmpty() && !doppelganger.npcExhausted()){
            doppelganger.getHeart().remove(doppelganger.getHeart().size() - 1);
            if(!doppelganger.npcExhausted()){ // Will not drop items after npc dead
                int randomLeft = (int)(Math.random()*Game.WIDTH/2);
                int randomRight = Game.WIDTH/2 + (int)((Math.random()*(Game.WIDTH - Game.WIDTH/2 - 50))); // minus 50 so the heart wont spawn at 1280
                System.out.println(randomLeft +" "+ randomRight);
                itemList.add(new NostaligaItem(randomRight,0,ObjectID.HEART,ImageResource.getInstance().getImage("/Art/Game Material/heart.png")));
                itemList.add(new NostaligaItem(randomLeft,0,ObjectID.HEART,ImageResource.getInstance().getImage("/Art/Game Material/heart.png")));
            }
        }

        //KEYLISTENER
        if(!player.playerDead()){
            if(key.keyState[key.LEFT]){
                player.setxVel(-5);
            }else if(key.keyState[key.RIGHT]){
                player.setxVel(5);
            }else{
                player.setxVel(0);
            }
            if((key.keyState[key.SPACE] && !player.isJumping())||(key.keyState[key.UP] && !player.isJumping())){
                if(player.isFalling()){
                    player.setyVel(-24);
                    player.setJumping(true);
                    //player.showMsg("654654654", 1000, Color.red, 0,Fonts.getHorrorFont(30));
                }
            }
        }
        //END
        doppelganger.checkBorder();
        if(!doppelganger.npcExhausted() && !player.playerDead()){
            doppelganger.dive();
        }
    }
    @Override
    public void render(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 1280, 720);
        if(player.isDead() && this.alpha == 0){
            g.setColor(Color.red);
            g.drawImage(restart,0,0,null);
        }
        if(player.isDead()){
            this.fadeOut(g);
        }else{
            this.fadeIn(g);
        }
        g.drawImage(background2, 0, 0, Game.WIDTH, Game.HEIGHT, null);
        farMountain.render(g);
        nearMountain.render(g);
        doppelganger.render(g);
        player.render(g);

        for(Floor floor : floor){
            floor.render(g);
        }
        for (Snow snow1 : snow) {
            snow1.render(g);
        }
        
        for( NostaligaItem item : itemList){
            item.render(g);
        }

        //debug
//        Graphics2D g2d = (Graphics2D) g;
//        g2d.setColor(Color.GREEN);
//        for(int i = 0; i< floor.size(); i++){
//            g2d.draw(floor.get(i).getBound());
//        }
//        for(int i = 0; i< itemList.size(); i++){
//            g2d.draw(itemList.get(i).getBound());
//        }
//        g2d.draw(doppelganger.getBound());
//        g2d.draw(player.getBound());
//        g2d.draw(player.getTop());
//        g2d.draw(player.getBot());
//        g2d.draw(player.getRight());
//        g2d.draw(player.getLeft());
//        player.renderMsg(g);
       
    }

    @Override
    public void keyPressed(int k) {
        keyPressed = k;
        key.keySet(k, true);
        if(keyPressed == KeyEvent.VK_ESCAPE){
            gsm.newState(GameStateManager.OPTION_STATE);
        }
        if(keyPressed == KeyEvent.VK_ENTER && this.alpha == 0){
            gsm.newState(GameStateManager.EASTER_EGG);
        }
    }

    @Override
    public void keyReleased(int k) {
        key.keySet(k,false);
        if(k == keyPressed){
            keyPressed = -1;
        }
    }

    @Override
    public void mousePressed(int x, int y) {

    }

    @Override
    public void mouseDragged(int x, int y) {

    }

    @Override
    public void mouseReleased(int x, int y) {

    }

    @Override
    public void mouseMoved(int x, int y) {

    }

    private class Keys {

        public static final int NUM_KEYS = 16;

        public boolean[] keyState = new boolean[NUM_KEYS];
        public boolean[] prevKeyState = new boolean[NUM_KEYS];


        public  final int LEFT = 0;
        public  final int RIGHT = 1;
        public  final int ENTER = 2;
        public  final int ESCAPE = 3;
        public  final int ONE = 4;
        public  final int TWO = 5;
        public  final int SPACE = 6;
        public  final int UP = 6;


        public  void keySet(int i, boolean b) {
            if(i == KeyEvent.VK_A) keyState[LEFT] = b;
            if(i == KeyEvent.VK_D) keyState[RIGHT] = b;
            if(i == KeyEvent.VK_ENTER) keyState[ENTER] = b;
            if(i == KeyEvent.VK_ESCAPE) keyState[ESCAPE] = b;
            if(i == KeyEvent.VK_1) keyState[ONE] = b;
            if(i == KeyEvent.VK_2) keyState[TWO] = b;
            if(i == KeyEvent.VK_SPACE) keyState[SPACE] = b;
            if(i == KeyEvent.VK_W) keyState[UP] = b;
        }

        public  void update() {
            for(int i = 0; i < NUM_KEYS; i++) {
                prevKeyState[i] = keyState[i];
            }
        }
        public  boolean isPressed(int i) {
            return keyState[i] && !prevKeyState[i];
        }

        public  boolean anyKeyPress() {
            for(int i = 0; i < NUM_KEYS; i++) {
                if(keyState[i]) return true;
            }
            return false;
        }

    }
}
