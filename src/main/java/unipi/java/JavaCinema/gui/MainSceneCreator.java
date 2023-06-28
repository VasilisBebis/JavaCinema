package unipi.java.JavaCinema.gui;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;

/*This class is used to create the main scene of the program*/
public class MainSceneCreator extends SceneCreator implements EventHandler<MouseEvent> {

	//Root FlowPane
	FlowPane rootFlowPane;
	//Buttons used
	Button ticketHandlingButton, partyHandlingButton, searchButton;
	
	//Constructor
	public MainSceneCreator(int width, int height) {
		//Using the constructor of the superclass SceneCreator
		super(width, height);
		
		//Creating a FlowPane object
		rootFlowPane = new FlowPane();
		
		//Initializing the buttons 
		ticketHandlingButton = new Button("Διαχείριση Εισιτηρίων Προβολών");
        partyHandlingButton = new Button("Διαχείριση Παιδικών Πάρτι");
        searchButton = new Button("Αναζήτηση Προβολών/Παιδικών Πάρτι");
        
        //Attaching events to the buttons
        ticketHandlingButton.setOnMouseClicked(this);
        partyHandlingButton.setOnMouseClicked(this);
        searchButton.setOnMouseClicked(this);
        
        //Customizing the root FlowPane
        rootFlowPane.setHgap(10);
        rootFlowPane.setAlignment(Pos.CENTER);

        //Adding the buttons to the root FlowPane
        rootFlowPane.getChildren().add(ticketHandlingButton);
        rootFlowPane.getChildren().add(partyHandlingButton);
        rootFlowPane.getChildren().add(searchButton);
        
	}
	
	//This method is used to create a new Scene of the current class
	public Scene createScene() {
		return new Scene(rootFlowPane, width, height);
	}

	//Overridden method of the EventHandler interface that is used to handle the clicking of buttons
	@Override
	public void handle(MouseEvent event) {
		//Checks which button has been clicked and changes to the appropriate scene and title
		//If the ticketHandlingButton is clicked it changes to the ticketHandlingScene
		if (event.getSource() == ticketHandlingButton) {
			App.stage.setScene(App.ticketHandlingScene);
			App.stage.setTitle("Διαχείριση Εισιτηρίων Προβολών");
		}
		//If the partyHandlingButton is clicked it changes to the partyHandlingScene
		if (event.getSource() == partyHandlingButton) {
			App.stage.setScene(App.partyHandlingScene);
			App.stage.setTitle("Διαχείριση Παιδικών Πάρτι");
		}
		//If the searchButton is clicked it changes to the searchScene
		if (event.getSource() == searchButton) {
			App.stage.setScene(App.searchScene);
			App.stage.setTitle("Αναζήτηση Κρατήσεων");
		}
	}
}
