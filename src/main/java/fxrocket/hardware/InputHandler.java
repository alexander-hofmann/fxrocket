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
import javafx.application.Platform;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Alexander Hofmann on 31.01.2017.
 */
public class InputHandler {
    private GpioController gpio;
    private AdcGpioProvider provider;
    private GpioPinAnalogInput[] inputs;
    private Joystick j1;
    private Joystick j2;

    public InputHandler() {
        j1 = new Joystick();
        j2 = new Joystick();

    }
    public void init() {
        gpio = GpioFactory.getInstance();
        try {
            provider = new MCP3008GpioProvider(SpiChannel.CS0);
        } catch (java.io.IOException exception) {
            Logger.getLogger("fxrocket").log(Level.SEVERE, "couldn't initialize SPI channel 0 with MCP3008 ADC.");
            Platform.exit();
        }
        GpioPinAnalogInput[] is = {
            gpio.provisionAnalogInputPin(provider, MCP3008Pin.CH0, "MyAnalogInput-CH0"),
            gpio.provisionAnalogInputPin(provider, MCP3008Pin.CH1, "MyAnalogInput-CH1"),
            gpio.provisionAnalogInputPin(provider, MCP3008Pin.CH2, "MyAnalogInput-CH2"),
            gpio.provisionAnalogInputPin(provider, MCP3008Pin.CH3, "MyAnalogInput-CH3"),
            gpio.provisionAnalogInputPin(provider, MCP3008Pin.CH4, "MyAnalogInput-CH4"),
            gpio.provisionAnalogInputPin(provider, MCP3008Pin.CH5, "MyAnalogInput-CH5"),
            gpio.provisionAnalogInputPin(provider, MCP3008Pin.CH6, "MyAnalogInput-CH6"),
            gpio.provisionAnalogInputPin(provider, MCP3008Pin.CH7, "MyAnalogInput-CH7")
        };
        inputs = is;
        provider.setMonitorInterval(250); // milliseconds
        GpioPinListenerAnalog listener = new GpioPinListenerAnalog()
        {
            public void handleGpioPinAnalogValueChangeEvent(GpioPinAnalogValueChangeEvent event)
            {
                double value = event.getValue();
                // display output
                System.out.println("<CHANGED VALUE> [" + event.getPin().getName() + "] : RAW VALUE = " + value);
            }
        };
    }

}
