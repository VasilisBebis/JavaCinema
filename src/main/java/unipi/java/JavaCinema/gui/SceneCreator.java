package unipi.java.JavaCinema.gui;

import javafx.scene.Scene;

/*This abstract class is used to create sub classes for creating specific scenes*/
public abstract class SceneCreator {

	//Fields
	int width;
	int height;
	
	//Constructor
	public SceneCreator(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	//Abstract method for creating a scene
	public abstract Scene createScene();
}
