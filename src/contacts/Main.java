
package contacts;



import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;


import java.util.Random;
import java.util.Scanner;

import java.lang.Thread;

public class Main extends JFrame{
    public static void main(String[] args) {

        Main gameoflife = new Main();
        gameoflife.setVisible(true);

        //set constants, number of generations and size of Universe
        int sizeUniverse = gridsize;
        int numOfGen = 10;

        //create and fill model (first data)
        Universe uniCurr = new Universe(sizeUniverse);

        uniCurr.fill();

        Universe uniNew = new Universe(sizeUniverse);
        uniNew.copyData(uniCurr);
        Thread mainThread = Thread.currentThread();

        for (int k = 0; k < numOfGen; k++) {
            setGeneration(k+1);
            setAliveSum(uniCurr.getAllAlive());
            setGrid(uniCurr.getWholeUniverse());

            for (int i = 0; i < sizeUniverse; i++) {
                for (int j = 0; j < sizeUniverse; j++) {

                    //if died
                    if (uniCurr.get(i,j)!=1){
                        //if 3 neighbours make alive
                        if (uniCurr.getAliveNeibSum(i,j)==3) uniNew.set(i,j,1);
                            //else set the same status - died
                        else uniNew.set(i,j,0);
                    }
                    else
                    {
                        //died if neighbours more then 3 or less then 2
                        if(uniCurr.getAliveNeibSum(i,j)<2 || uniCurr.getAliveNeibSum(i,j)>3) uniNew.set(i,j,0);
                            //else make the same status - alive
                        else uniNew.set(i,j,1);
                    }
                }
            }

            try{mainThread.sleep(5000);
            }
            catch(InterruptedException e){
                System.out.println(e);
            }

            //copy data to temp array
            uniCurr.copyData(uniNew);

        }
        //uniNew.print();
    }


    private static int gridsize = 20;
    private static Grid grid = new Grid(gridsize,300);

    private static JLabel genLabel = new JLabel();
    private static JLabel aliveLabel = new JLabel();


    public Main(){//int gridsize){

        super("Game of Life");

        int gridsize=20;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300,350);
        setLocationRelativeTo(null);
        setLayout(null);

        JPanel topPanel = new JPanel();
        topPanel.setBounds(0,0,300,50);
        topPanel.setLayout(new BorderLayout());


        genLabel.setBounds(80,0,100,25);
        genLabel.setHorizontalAlignment(SwingConstants.LEFT);
        genLabel.setVerticalAlignment(SwingConstants.CENTER);

        JLabel genLabelText = new JLabel();
        genLabelText.setBounds(0,0,80,25);
        genLabelText.setHorizontalAlignment(SwingConstants.LEFT);
        genLabelText.setVerticalAlignment(SwingConstants.CENTER);
        genLabelText.setText("Generation #");


        aliveLabel.setBounds(60,25,80,25);
        aliveLabel.setHorizontalAlignment(SwingConstants.LEFT);
        aliveLabel.setVerticalAlignment(SwingConstants.CENTER);

        JLabel aliveLabelText = new JLabel();
        aliveLabelText.setBounds(0,25,60,25);
        aliveLabelText.setHorizontalAlignment(SwingConstants.LEFT);
        aliveLabelText.setVerticalAlignment(SwingConstants.CENTER);
        aliveLabelText.setText("Alive : ");

        topPanel.add(genLabel);
        topPanel.add(aliveLabel);
        topPanel.add(genLabelText);
        topPanel.add(aliveLabelText);

        add(topPanel);


        grid.setBounds(0,50,300,300);
        grid.setLayout(new BorderLayout());

        add(grid);
        setLocationRelativeTo(null);
        setVisible(true);

    }

    public static void setGeneration(int gen){
        genLabel.setText(Integer.toString(gen));
    }

    public static void setAliveSum(int aliv){
        aliveLabel.setText(Integer.toString(aliv));
    }
    public static void setGrid(Object[][] uni){
        grid.setAlive(uni);
    }


    //Panel with Universe
    public static class Grid extends JPanel{
        //private Rectangle[][] cells;
        private int steps;
        private int size;
        private Object[][] curState;

        Grid(int steps, int size){
            this.steps = steps;
            this.size = size;
            curState = null;
        }

        public void setAlive(Object[][] uni){
            this.curState = uni;
            this.repaint();
        }

        public void paintComponent(Graphics g){

            super.paintComponent(g);

            int step = size/steps;

            for(int i=0;i<steps;i++){
                for(int j=0;j<steps;j++){
                    if(this.curState!=null && (Integer)this.curState[i][j]!=0){
                        g.setColor(Color.black);
                        g.fillRect(i*step,j*step,step,step);
                    }
                    g.setColor(Color.black);
                    g.drawRect(i*step,j*step,step-1,step-1);
                }
            }

        }


    }

}

class Universe {

    private int[][] arrUniv;

    Universe(int n) {
        this.arrUniv = new int[n][n];
    }

    public void copyData(Universe old){

        for (int i = 0; i < this.arrUniv.length; i++) {
            for (int j = 0; j < this.arrUniv[0].length; j++) {
                this.arrUniv[i][j] = old.arrUniv[i][j];
            }
        }
    }

    public void fill() {

        Random rnd = new Random(System.currentTimeMillis());

        for (int i = 0; i < this.arrUniv.length; i++) {
            for (int j = 0; j < this.arrUniv[0].length; j++) {
                this.arrUniv[i][j] = rnd.nextInt(2);
            }
        }
    }

    public void print(int gen) {
        System.out.println("Generation #"+gen);
        System.out.println("Alive: "+ this.getAllAlive());
        for (int i = 0; i < this.arrUniv.length; i++) {
            for (int j = 0; j < this.arrUniv[0].length; j++) {
                System.out.print((this.arrUniv[i][j] == 1) ? "O" : " ");
            }
            System.out.println();
        }
    }

    public int get(int a, int b) {
        return this.arrUniv[a][b];
    }

    public void set(int a, int b, int c) {
        this.arrUniv[a][b] = c;
    }

    public int getAllAlive(){
        int res=0;
        for (int i = 0; i < this.arrUniv.length; i++) {
            for (int j = 0; j < this.arrUniv[0].length; j++) {
                if (arrUniv[i][j] == 1) res++;
            }
        }
        return res;
    }

    public int getAliveNeibSum(int a, int b) {

        return this.arrUniv[(a + 1) % this.arrUniv.length][(b + 1) % this.arrUniv.length] +
                this.arrUniv[((a - 1)+this.arrUniv.length) % this.arrUniv.length][(b + 1) % this.arrUniv.length] +
                this.arrUniv[(a + 1) % this.arrUniv.length][((b - 1)+this.arrUniv.length) % this.arrUniv.length] +
                this.arrUniv[((a - 1)+this.arrUniv.length) % this.arrUniv.length][((b - 1)+this.arrUniv.length) % this.arrUniv.length] +
                this.arrUniv[(a + 1) % this.arrUniv.length][(b) % this.arrUniv.length] +
                this.arrUniv[((a - 1)+this.arrUniv.length) % this.arrUniv.length][(b) % this.arrUniv.length] +
                this.arrUniv[(a) % this.arrUniv.length][(b + 1) % this.arrUniv.length] +
                this.arrUniv[(a) % this.arrUniv.length][((b - 1)+this.arrUniv.length) % this.arrUniv.length];
    }
    public Object[][] getWholeUniverse(){

        Integer[][] resarr = new Integer[arrUniv.length][arrUniv[0].length];
        for (int i = 0; i < this.arrUniv.length; i++) {
            for (int j = 0; j < this.arrUniv[0].length; j++) {
                resarr[i][j] = Integer.valueOf(arrUniv[i][j]);
            }
        }
        return resarr;
    }

}
