package fxrocket.hardware;

import com.pi4j.gpio.extension.base.AdcGpioProvider;
import com.pi4j.gpio.extension.mcp.MCP3008GpioProvider;
import com.pi4j.gpio.extension.mcp.MCP3008Pin;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinAnalogInput;
import com.pi4j.io.gpio.event.GpioPinAnalogValueChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerAnalog;
import com.pi4j.io.spi.SpiChannel;

import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Felix Müllner on 24.03.2017.
 */
public class DefaultGamepad implements Gamepad {
    private static final int EVENT_TRESHOLD = 0;
    private static final int MONITOR_INTERVAL = 50;

    private static int xMax = 1023;
    private static int xMin = 0;
    private static int yMax = 1023;
    private static int yMin = 0;

    private int gamepadNumber;
    private volatile Lock lock; //does this have to be volatile?
    private double xDeadzone;
    private double yDeadzone;
    private int xCenter;
    private int yCenter;
    private volatile int xPos;
    private volatile int yPos;
    private volatile boolean stickPressed;

    public DefaultGamepad(int gamepadNumber) throws IOException {
        this.gamepadNumber = gamepadNumber;
        if (this.gamepadNumber < 1 || this.gamepadNumber > 2) {
            throw new IllegalArgumentException("Invalid Gamepad Number");
        }

        this.lock = new ReentrantLock();

        //set default values
        this.xDeadzone = 0.1;
        this.yDeadzone = 0.1;
        this.xCenter = 512;
        this.yCenter = 512;

        //set up GPIO connection
        final GpioController gpio = GpioFactory.getInstance();
        final AdcGpioProvider provider = new MCP3008GpioProvider(SpiChannel.CS0); //throws IOException
        final GpioPinAnalogInput inputs[] = new GpioPinAnalogInput[3];
        if (this.gamepadNumber == 1) {
            inputs[0] = gpio.provisionAnalogInputPin(provider, MCP3008Pin.CH0, "MyAnalogInput-CH0");
            inputs[1] = gpio.provisionAnalogInputPin(provider, MCP3008Pin.CH1, "MyAnalogInput-CH1");
            inputs[2] = gpio.provisionAnalogInputPin(provider, MCP3008Pin.CH2, "MyAnalogInput-CH2");
        } else if (this.gamepadNumber == 2) {
            inputs[0] = gpio.provisionAnalogInputPin(provider, MCP3008Pin.CH3, "MyAnalogInput-CH3");
            inputs[1] = gpio.provisionAnalogInputPin(provider, MCP3008Pin.CH4, "MyAnalogInput-CH4");
            inputs[2] = gpio.provisionAnalogInputPin(provider, MCP3008Pin.CH5, "MyAnalogInput-CH5");
        }
        provider.setEventThreshold(DefaultGamepad.EVENT_TRESHOLD, inputs);
        provider.setMonitorInterval(DefaultGamepad.MONITOR_INTERVAL);

        this.stickPressed = (inputs[0].getValue() <= 512);
        this.xPos = (int) inputs[1].getValue();
        this.yPos = (int) inputs[2].getValue();

        GpioPinListenerAnalog listener = new GpioPinListenerAnalog()
        {
            public void handleGpioPinAnalogValueChangeEvent(GpioPinAnalogValueChangeEvent event)
            {
                lock.lock();

                if (event.getPin().getName().equals(inputs[0].getName())) {
                    stickPressed = (event.getValue() <= 512);
                } else if (event.getPin().getName().equals(inputs[1].getName())) {
                    xPos = (int) event.getValue();
                } else if (event.getPin().getName().equals(inputs[2].getName())) {
                    yPos = (int) event.getValue();
                }

                lock.unlock();
            }
        };

        gpio.addListener(listener, inputs);
    }

    public void calibrateCenter() {
        this.xCenter = this.xPos;
        this.yCenter = this.yPos;
    }

    public void calibrateDeadzoneX(double deadzoneX) {
        this.xDeadzone = deadzoneX;
    }

    public void calibrateDeadzoneY(double deadzoneY) {
        this.yDeadzone = deadzoneY;
    }

    public double getX() {
        double xOut;
        if (this.xPos >= this.xCenter) {
            xOut = (double) (this.xPos - this.xCenter) / (double) (DefaultGamepad.xMax - this.xCenter);
            if (xOut < this.xDeadzone) {
                return 0;
            } else if (xOut > 1) {
                return 1;
            } else {
                return xOut;
            }
        } else {
            xOut = (double) (this.xCenter - this.xPos) / (double) (this.xCenter - DefaultGamepad.xMin) * -1;
            if (xOut * -1 < this.xDeadzone) {
                return 0;
            } else if (xOut < -1) {
                return -1;
            } else {
                return xOut;
            }
        }
    }

    public double getY() {
        double yOut;
        if (this.yPos >= this.yCenter) {
            yOut = (double) (this.yPos - this.yCenter) / (double) (DefaultGamepad.yMax - this.yCenter);
            if (yOut < this.yDeadzone) {
                return 0;
            } else if (yOut > 1) {
                return 1;
            } else {
                return yOut;
            }
        } else {
            yOut = (double) (this.yCenter - this.yPos) / (double) (this.yCenter - DefaultGamepad.yMin) * -1;
            if (yOut * -1 < this.yDeadzone) {
                return 0;
            } else if (yOut < -1) {
                return -1;
            } else {
                return yOut;
            }
        }
    }

    public boolean isStickPressed() {
        return this.stickPressed;
    }
}
