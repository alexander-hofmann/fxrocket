package fxrocket.demo;
/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  MCP3008GpioExample.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2016 Pi4J
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 *
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */
import com.pi4j.gpio.extension.base.AdcGpioProvider;
import com.pi4j.gpio.extension.mcp.MCP3008GpioProvider;
import com.pi4j.gpio.extension.mcp.MCP3008Pin;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinAnalogInput;
import com.pi4j.io.gpio.event.GpioPinAnalogValueChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerAnalog;
import com.pi4j.io.spi.SpiChannel;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * <p>
 * This example code demonstrates how to setup a custom GpioProvider
 * for analog output pin using the MCP3008 ADC chip.
 * </p>
 *
 * <p>
 * This GPIO provider implements the MCP3008 10-Bit Analog-to-Digital Converter (ADC) as native Pi4J GPIO pins.
 * </p>
 *
 * <p>
 * The MCP3008 is connected via SPI connection to the Raspberry Pi and provides 8 GPIO analog input pins.
 * </p>
 *
 * @author Christian Wehrli, Robert Savage
 */
public class MCP3008GpioTest {

    private static final int EVENT_TRESHOLD = 0;
    private static final int MONITOR_INTERVAL = 50;

    private static String[] pinNames = new String[6];
    private static double[] pinValues = new double[6];

    public static void main(String args[]) throws Exception {
        System.out.println("<--Pi4J--> MCP3008 ADC Test ... started.");
        // Create gpio controller
        final GpioController gpio = GpioFactory.getInstance();
        // Create custom MCP3008 analog gpio provider
        // we must specify which chip select (CS) that that ADC chip is physically connected to.
        final AdcGpioProvider provider = new MCP3008GpioProvider(SpiChannel.CS0);

        // Provision gpio analog input pins for all channels of the MCP3008.
        // (you don't have to define them all if you only use a subset in your project)
        final GpioPinAnalogInput inputs[] = {
                gpio.provisionAnalogInputPin(provider, MCP3008Pin.CH0, "MyAnalogInput-CH0"),
                gpio.provisionAnalogInputPin(provider, MCP3008Pin.CH1, "MyAnalogInput-CH1"),
                gpio.provisionAnalogInputPin(provider, MCP3008Pin.CH2, "MyAnalogInput-CH2"),
                gpio.provisionAnalogInputPin(provider, MCP3008Pin.CH3, "MyAnalogInput-CH3"),
                gpio.provisionAnalogInputPin(provider, MCP3008Pin.CH4, "MyAnalogInput-CH4"),
                gpio.provisionAnalogInputPin(provider, MCP3008Pin.CH5, "MyAnalogInput-CH5")
        };


        // Define the amount that the ADC input conversion value must change before
        // a 'GpioPinAnalogValueChangeEvent' is raised.  This is used to prevent unnecessary
        // event dispatching for an analog input that may have an acceptable or expected
        // range of value drift.
        provider.setEventThreshold(MCP3008GpioTest.EVENT_TRESHOLD, inputs); // all inputs; alternatively you can set thresholds on each input discretely

        // Set the background monitoring interval timer for the underlying framework to
        // interrogate the ADC chip for input conversion values.  The acceptable monitoring
        // interval will be highly dependant on your specific project.  The lower this value
        // is set, the more CPU time will be spend collecting analog input conversion values
        // on a regular basis.  The higher this value the slower your application will get
        // analog input value change events/notifications.  Try to find a reasonable balance
        // for your project needs.
        provider.setMonitorInterval(MCP3008GpioTest.MONITOR_INTERVAL); // milliseconds

        // Print current analog input conversion values from each input channel
        for(int i = 0; i < inputs.length; ++i){
            pinNames[i] = inputs[i].getName();
            pinValues[i] = inputs[i].getValue();
            System.out.println("<INITIAL VALUE> [" + inputs[i].getName() + "] : RAW VALUE = " + inputs[i].getValue());
        }

        // Create an analog pin value change listener
        GpioPinListenerAnalog listener = new GpioPinListenerAnalog()
        {
            public void handleGpioPinAnalogValueChangeEvent(GpioPinAnalogValueChangeEvent event)
            {
                String update = "\n\n";

                for (int i = 0; i < pinNames.length; ++i) {
                    if (event.getPin().getName().equals(pinNames[i])) {
                        pinValues[i] = event.getValue();
                        update += "<CHANGED VALUE> [" + pinNames[i] + "] : RAW VALUE = " + pinValues[i] + "\n";
                    } else {
                        update += "<             > [" + pinNames[i] + "] : RAW VALUE = " + pinValues[i] + "\n";
                    }
                }

                System.out.println(update);
            }
        };

        // Register the gpio analog input listener for all input pins
        gpio.addListener(listener, inputs);

        // Keep this sample program running for 10 minutes
        for (int count = 0; count < 600; count++) {
            Thread.sleep(1000);
        }

        // When your program is finished, make sure to stop all GPIO activity/threads by shutting
        // down the GPIO controller (this method will forcefully shutdown all GPIO monitoring threads
        // and background scheduled tasks)
        gpio.shutdown();

        System.out.println("Exiting MCP3008GpioTest");
    }
}
