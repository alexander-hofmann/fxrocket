cd /home/pi/fxrocket
javac src/main/java/fxrocket/demo/MCP3008GpioTest.java -d target/classes/
sudo java -Dprism.verbose=true -cp /home/pi/fxrocket/libsfxrt.jar:/home/pi/fxrocket/target/classes fxrocket.demo.MCP3008GpioTest
