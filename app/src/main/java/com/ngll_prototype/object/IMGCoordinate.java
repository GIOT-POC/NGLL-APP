package com.ngll_prototype.object;

/**
 * Created by Chris on 2016/10/25.
 */

public class IMGCoordinate {
    private int number;
    private int x;
    private  int y;

    public void setNumber(int num){
        this.number = num;
    }
    public void setX(int X){
        this.x = X;
    }
    public void setY(int Y){
        this.y = Y;
    }

    public int getNumber(){
        return number;
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }

    public IMGCoordinate(int num, int X, int Y) {
        super();
        this.number = num;
        this.x = X;
        this.y = Y;
    }
}
