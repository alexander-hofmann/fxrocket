package fxrocket.hardware;

/**
 * Created by Alexander Hofmann on 31.01.2017.
 */
public class Joystick {
    /** Joystick analog stick x direction 0..1023, 512 is neutral */
    private int x;
    /** Joystick analog stick y direction 0..1023, 512 is neutral */
    private int y;
    /** Joystick digital stick a, b, c, d direction a = x_max, b = y_min, c = y_max, d = y_min */
    private int a;
    private int b;
    private int c;
    private int d;
    /** Buttons select, escape, rbu (right button up), rbd (right button down), lbu (left button up), lbd (left button down) */
    private int select;
    private int escape;
    private int rbu;
    private int rbd;
    private int lbu;
    private int lbd;

    public Joystick() {
    }



}
