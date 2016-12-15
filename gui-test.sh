cd /home/pi/fxrocket
javac src/main/java/fxrocket/demo/GuiTest.java src/main/java/fxrocket/hardware/DefaultGamepad.java -d target/classes/
sudo java -Dprism.verbose=true -cp /home/pi/fxrocket/libsfxrt.jar:/home/pi/fxrocket/target/classes fxrocket.demo.GuiTest
