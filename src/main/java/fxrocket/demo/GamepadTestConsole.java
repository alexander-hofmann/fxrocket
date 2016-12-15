package fxrocket.demo;

import fxrocket.hardware.DefaultGamepad;

/**
 * Created by Felix Müllner on 27.03.2017.
 */
public class GamepadTestConsole {
    public static void main(String args[]) throws Exception {
        DefaultGamepad gamepad1 = new DefaultGamepad(1);
        DefaultGamepad gamepad2 = new DefaultGamepad(2);

        while (true) {
            System.out.print("Stick 1:\tPressed: " + (gamepad1.isStickPressed() ? "yes" : " no") + "; x-Axis: " +
                    (gamepad1.getX() >= 0 ? " " : "") + String.format("%.5f", gamepad1.getX()) + "; y-Axis: " +
                    (gamepad1.getY() >= 0 ? " " : "") + String.format("%.5f", gamepad1.getY()) + "\n");
            System.out.print("Stick 2:\tPressed: " + (gamepad2.isStickPressed() ? "yes" : " no") + "; x-Axis: " +
                    (gamepad2.getX() >= 0 ? " " : "") + String.format("%.5f", gamepad2.getX()) + "; y-Axis: " +
                    (gamepad2.getY() >= 0 ? " " : "") + String.format("%.5f", gamepad2.getY()) + "\n");
            Thread.sleep(50);
            // ESC[2A - cursor up 2 lines
            // \r - cursor return to begin of line
            // ESC[J - erase to end of screen
            System.out.print("\033[2A\r\033[J");
        }
    }
}
