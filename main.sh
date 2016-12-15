cd /home/pi/fxrocket
javac src/main/java/fxrocket/Main.java src/main/java/fxrocket/hardware/DefaultGamepad.java src/main/java/fxrocket/scenes/GameScene.java src/main/java/fxrocket/scenes/SceneManager.java src/main/java/fxrocket/scenes/gamescenes/MainMenuScene.java src/main/java/fxrocket/scenes/gamescenes/GamepadDebugScene.java src/main/java/fxrocket/scenes/gamescenes/ExitScene.java -d target/classes/
sudo java -Dprism.verbose=true -cp /home/pi/fxrocket/libsfxrt.jar:/home/pi/fxrocket/target/classes fxrocket.Main
