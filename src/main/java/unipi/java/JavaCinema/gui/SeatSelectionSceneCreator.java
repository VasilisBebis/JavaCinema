package unipi.java.JavaCinema.gui;

import java.time.format.DateTimeFormatter;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import unipi.java.JavaCinema.core.RegularShow;

public class SeatSelectionSceneCreator extends SceneCreator implements EventHandler<MouseEvent> {

	//Root GridPane
	GridPane rootGridPane;
	//Array of buttons
	Button[] seatButtons = new Button[150];
	//an object of the TicketHandlingSceneCreator class (This is used to link the ticketHandlingSceneCreator with the seatSelectionSceneCreator)
	TicketHandlingSceneCreator ticketHandlingSceneCreator;
	
	//Constructor
	public SeatSelectionSceneCreator(int width, int height, SceneCreator ticketHandlingSceneCreator) {
		//Using the constructor of the superclass SceneCreator
		super(width, height);
		
		this.ticketHandlingSceneCreator = (TicketHandlingSceneCreator) ticketHandlingSceneCreator;
		
		//Creating a GridPane object
		rootGridPane = new GridPane();
		
		//Setting the gap between the rows and columns of the GridPane and aligning it to the center of the window 
		rootGridPane.setHgap(15);
		rootGridPane.setVgap(10);
		rootGridPane.setAlignment(Pos.CENTER);
		
		/*To initialize the rows and columns variables we access the rooms[0] (all the rooms have the same amount of rows and columns, so it 
		 * doesn't matter which one we use) object from the App class and then use the getNoOfRows() and getNoOfColumns() methods of the Room class
		 * to get the number of rows and number of columns
		 */
		int rows = App.rooms[0].getNoOfRows();
		int columns = App.rooms[0].getNoOfColumns();
		
		//An array of Strings that represent the id of each seat
		String[] seatLayout = new String[rows*columns];
		
		int seatCounter = 0;
		
		for (int i = 0; i < rows; i++) {
			//We assign a letter to every row starting from A (A-J)
			char rowLetter = (char) ('A' + i);
			
			for (int j = 0; j < columns; j++) {
				//We assign a number to each column starting from 1 (1-15)
				int columnNumber = j + 1;
				
				//Using the seatCounter to access every seat of the seatLayout array and set its id using string concatenation
				seatLayout[seatCounter] = rowLetter + Integer.toString(columnNumber);
				
				//Naming the button using the IDs stored in the seatLayout array
				seatButtons[seatCounter] = new Button(seatLayout[seatCounter]);
				//Setting the preferable size of the button to 40x40 pixels
				seatButtons[seatCounter].setPrefSize(40, 40);
				//Adding the buttons to the root GridPane
				rootGridPane.add(seatButtons[seatCounter], j, i);
				
				//Attaching events to the buttons
				seatButtons[seatCounter].setOnMouseClicked(this);
				
				//Increasing the seatCounter by 1 after successfully setting the id of a seat
				seatCounter++;
			}
		}
	}

	//This method is used to create a new Scene of the current class
	@Override
	public Scene createScene() {
		return new Scene(rootGridPane, width, height);
	}
	
	/*This method sets which seats are available based on the selections of the user in the previous scene. It uses the getters from the 
	 * ticketHandlingSceneCreator object to get the selected movie, date, time and room. Then it searches the events array from the App, which 
	 * contains all the events of the movie theater, and finds the event that matches all the selections. Then using the seatOccupancy array from 
	 * the RegularShow class, it finds which seats have already been occupied*/
	public void setAvailableSeats() {
		for (int i = 0; i < App.events.length; i++) {
			
			//Checking if the Event is a RegularShow
			if (App.events[i] instanceof RegularShow) {
				
				//User's selections
				String selectedMovie = ticketHandlingSceneCreator.getSelectedMovie();
				String selectedDate = ticketHandlingSceneCreator.getSelectedDate();
				String selectedTime = ticketHandlingSceneCreator.getSelectedTime();
				String selectedRoom = ticketHandlingSceneCreator.getSelectedRoom();
			
				//Event's info
				String eventMovie = App.events[i].getMovie().getName();
			
				//Uses DateTimeFormatter class to format the date to a dd/MM pattern then creates a String with the date in that format
				DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM");
				String eventDate = App.events[i].getScreeningDate().format(dateFormat);
			
				//Uses DateTimeFormatter class to format the time to a HH:mm pattern the creates a String with the time in that format
				DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
				String eventTime = App.events[i].getScreeningDate().toLocalTime().format(timeFormat);
			
				String eventRoom = App.events[i].getRoom().getId() + " (" + App.events[i].getRoom().getDescription() + ")";
				
				//Checks if the user's selections are equal to the event's info
				if ((selectedMovie.equals(eventMovie)) && (selectedDate.equals(eventDate)) && (selectedTime.equals(eventTime)) && (selectedRoom.equals(eventRoom))) {
					
					//String array that contains the ticket id if the seat has been occupied (null if its not occupied)
					String[][] seatOccupancy = ((RegularShow) App.events[i]).getSeatOccupancy();
					
					//Iterating over the seatOccupancy array
					for (int row = 0; row < App.events[i].getRoom().getNoOfRows(); row++) {
						for (int column = 0; column < App.events[i].getRoom().getNoOfColumns(); column++) {
							
							//The index used to access the elements of the seatButtons array
							int index = row * App.events[i].getRoom().getNoOfColumns() + column;
							
							//Checks if the seat has not been occupied by checking if the String matching the seat is 0 
							if (seatOccupancy[row][column].equals("0")) {
								//Enables the button and changes the text color to green (available seat)
								seatButtons[index].setDisable(false);
								seatButtons[index].setTextFill(Color.GREEN);
							}
							else {
								//Disables the button and changes the text color to red (unavailable seat)
								seatButtons[index].setDisable(true);
								seatButtons[index].setTextFill(Color.RED);
							}
						}
					}
				}
			}
		}
	}

	//Overridden method of the EventHandler interface that is used to handle the clicking of buttons
	@Override
	public void handle(MouseEvent event) {
		//Finding the button that has been clicked from the buttons array
		for (int i = 0; i < seatButtons.length; i++) {
			if (event.getSource() == seatButtons[i]) {
				//Sets the scene to the ticket handling scene and changes the scene title
				App.stage.setScene(App.ticketHandlingScene);
				App.stage.setTitle("Διαχείριση Εισιτηρίων Προβολών");
				/*Sets the row and column of the selected seat in the ticketHandlingSceneCreator using the setters. To find the row of the seat
				 * we do i (which is the index of the button selected) / the number of total columns. To find the column of the seat we do 
				 * i modulo the number of total columns*/ 
				this.ticketHandlingSceneCreator.setSelectedSeatRow(i / App.rooms[0].getNoOfColumns());
				this.ticketHandlingSceneCreator.setSelectedSeatColumn(i % App.rooms[0].getNoOfColumns());
			}
		}
	}
}
