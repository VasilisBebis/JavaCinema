package unipi.java.JavaCinema.gui;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import javafx.scene.input.MouseButton;
import unipi.java.JavaCinema.core.*;

//This class is used to create the ticket handling scene of the program
public class TicketHandlingSceneCreator extends SceneCreator implements EventHandler<ActionEvent>{

	//List of tickets
	ArrayList<Ticket> ticketList;
	//FlowPane of the buttons
	FlowPane buttonFlowPane;
	//The root GridPane of the ticket handling scene and the secondary GridPane used to arrange the reservation selections
	GridPane rootGridPane, reservationGridPane;
	//All the buttons used in the scene
	Button ticketIssuanceButton, ticketModificationButton, ticketCancellationButton, backButton, seatSelectionButton;
	//The various labels used in the reservation GridPane
	Label screeningLabel, dateLabel, timeLabel, roomLabel, seatLabel, ticketTypeLabel;
	//ComboBoxes used for selections in the issuance of the ticket
	ComboBox<String> movieSelectionBox, dateSelectionBox, timeSelectionBox, roomSelectionBox, ticketTypeBox;
	//TableView of the tickets 
	TableView<Ticket> ticketTableView;
	//The row and column of the selected seat
	private int selectedSeatRow, selectedSeatColumn;
	//The selected movie, date, time, room and ticket type
	private String selectedMovie, selectedDate, selectedTime, selectedRoom, selectedTicketType;
	//seatSelectionSceneCreator used to link the seat selection scene creator with the ticket handling scene creator
	SeatSelectionSceneCreator seatSelectionSceneCreator;
	//The ticket selected from the tableView
	private Ticket ticketSelectedFromTableView;
	//Counts the tickets that have been issued
	private int ticketCounter;
	
	//Constructor
	public TicketHandlingSceneCreator(int width, int height) {
		//Using the constructor of the superclass SceneCreator
		super(width, height);
		
		//Creating an arrayList object
		ticketList = new ArrayList<>();
		//Initializes the ticket counter with 0
		ticketCounter = 0;
		
		//Creating the different pane objects
		buttonFlowPane = new FlowPane();
		rootGridPane = new GridPane();
		reservationGridPane = new GridPane();
		
		//Initializing the buttons
		ticketIssuanceButton = new Button("Έκδοση Εισιτηρίου");
		ticketModificationButton = new Button("Τροποποίηση Εισιτηρίου");
		ticketCancellationButton = new Button("Ακύρωση Εισιτηρίου");
		backButton = new Button("Επιστροφή στην Αρχική Οθόνη");
		seatSelectionButton = new Button("Επιλέξτε Θέση");
		
		//Attaching events to the buttons
		ticketIssuanceButton.setOnAction(this);
		ticketModificationButton.setOnAction(this);
		ticketCancellationButton.setOnAction(this);
		backButton.setOnAction(this);
		seatSelectionButton.setOnAction(this);
		
		//Disables the ticket modification and cancellation buttons
		ticketModificationButton.setDisable(true);
		ticketCancellationButton.setDisable(true);
		
		
		//Adds a tooltip to the ticket modification button
		Tooltip ticketModificationTooltip = new Tooltip("Η τροποποίηση εισιτηρίων είναι εφικτή μέχρι 2 ώρες πρίν την έναρξη της προβολής");
		ticketModificationTooltip.setShowDelay(Duration.millis(500)); //Sets the time it takes for the tooltip to appear to 500 milliseconds
		Tooltip.install(ticketModificationButton, ticketModificationTooltip);
		
		//Adds a tooltip to the ticket cancellation button
		Tooltip ticketCancellationTooltip = new Tooltip("Η ακύρωση εισιτηρίων είναι εφικτή μέχρι 2 ώρες πρίν την έναρξη της προβολής");
		ticketCancellationTooltip.setShowDelay(Duration.millis(500)); //Sets the time it takes for the tooltip to appear to 500 milliseconds
		Tooltip.install(ticketCancellationButton, ticketCancellationTooltip);
		
		//Initializing the labels
		screeningLabel = new Label("Ταινία:");
		dateLabel = new Label("Μέρα Προβολής:");
		timeLabel = new Label("Ώρα Προβολής:");
		roomLabel = new Label("Αίθουσα Προβολής:");
		seatLabel = new Label("Θέση:");
		ticketTypeLabel = new Label("Τύπος Εισιτηρίου:");
		
		//Initializing the ComboBoxes
		movieSelectionBox = new ComboBox<>();
		dateSelectionBox = new ComboBox<>();
		timeSelectionBox = new ComboBox<>();
		roomSelectionBox = new ComboBox<>();
		ticketTypeBox = new ComboBox<>();
		

		//Filling the movie selection ComboBox with the different movies names 
		for (int i = 0; i < App.movies.length; i++) {
			movieSelectionBox.getItems().add(App.movies[i].getName());
		}
		
		//Fills the ticket type ComboBox
		setAvailableTicketTypes();
		
		//Attaching events to the ComboBoxes
		movieSelectionBox.setOnAction(this);
		dateSelectionBox.setOnAction(this);
		timeSelectionBox.setOnAction(this);
		roomSelectionBox.setOnAction(this);
		ticketTypeBox.setOnAction(this);
	
		//Initializing the TableView
		ticketTableView = new TableView<>();
	
		//Customizing the button FlowPane 
		buttonFlowPane.setHgap(10);
		buttonFlowPane.setAlignment(Pos.CENTER);
		
		//Adding the buttons to the FlowPane
		buttonFlowPane.getChildren().add(ticketIssuanceButton);
		buttonFlowPane.getChildren().add(ticketModificationButton);
		buttonFlowPane.getChildren().add(ticketCancellationButton);
		
		//Customizing the reservation GridPane
		reservationGridPane.setAlignment(Pos.TOP_RIGHT);
		reservationGridPane.setHgap(10);
		reservationGridPane.setVgap(10);
		
		//Adding the labels and ComboBoxes to the GridPane
		reservationGridPane.add(screeningLabel, 0, 0);
		reservationGridPane.add(movieSelectionBox, 1, 0);
		reservationGridPane.add(dateLabel, 0, 1);
		reservationGridPane.add(dateSelectionBox, 1, 1);
		reservationGridPane.add(timeLabel, 0, 2);
		reservationGridPane.add(timeSelectionBox, 1, 2);
		reservationGridPane.add(roomLabel, 0, 3);
		reservationGridPane.add(roomSelectionBox, 1, 3);
		reservationGridPane.add(seatLabel, 0, 4);
		reservationGridPane.add(seatSelectionButton, 1, 4);
		reservationGridPane.add(ticketTypeLabel, 0, 5);
		reservationGridPane.add(ticketTypeBox, 1, 5);
		
		//Customizing the root GridPane
		rootGridPane.setHgap(10);		
		rootGridPane.setVgap(10);
		
		//Adding the secondary GridPane, the TableView, the button FlowPane and the back button in the root GridPane
		rootGridPane.add(reservationGridPane, 1, 0);
		rootGridPane.add(ticketTableView, 0, 0);
		rootGridPane.add(buttonFlowPane, 0, 1);
		rootGridPane.add(backButton, 1, 1);
		
		//Συγγνώμη που χρησιμοποίησα lambda απλά δεν μπορούσα να βρω αλλο τρόπο να το κάνω να δουλέψει
		
		//Adding Columns to the TableView
		TableColumn<Ticket, String> idColumn = new TableColumn<>("Κωδικός");
		idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
		ticketTableView.getColumns().add(idColumn);
		
		TableColumn<Ticket, String> movieColumn = new TableColumn<>("Ταινία");
		movieColumn.setCellValueFactory(cellData -> {
			Ticket ticket = cellData.getValue();
			//Gets the movie's name
			String movieName = ticket.getScreening().getMovie().getName();
			return new SimpleStringProperty(movieName);
		});
		ticketTableView.getColumns().add(movieColumn);
		
		TableColumn<Ticket, String> dateColumn = new TableColumn<>("Μέρα Προβολής");
		dateColumn.setCellValueFactory(cellData -> {
			Ticket ticket = cellData.getValue();
			//Gets the date of the screening
			LocalDateTime dateTime = ticket.getScreening().getScreeningDate();
			//Creates a dd/MM formatter
			DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM");
			//Formats the date in dd/MM and stores it into a string variable
			String formattedDate = dateTime.format(format);
			return new SimpleStringProperty(formattedDate);
		});
		ticketTableView.getColumns().add(dateColumn);
		
		TableColumn<Ticket, String> timeColumn = new TableColumn<>("Ώρα Προβολής");
		timeColumn.setCellValueFactory(cellData -> {
			Ticket ticket = cellData.getValue();
			//Gets the date of the screening
			LocalDateTime dateTime = ticket.getScreening().getScreeningDate();
			//Creates a HH:ss formatter
			DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:ss");
			//Formats the time in HH:ss and stores in into a string variable
			String formattedTime = dateTime.format(format);
			return new SimpleStringProperty(formattedTime);
		});
		ticketTableView.getColumns().add(timeColumn);
		
		TableColumn<Ticket, String> seatColumn = new TableColumn<>("Θέση");
		seatColumn.setCellValueFactory(cellData -> {
			Ticket ticket = cellData.getValue();
			//Gets the seatOccupancy array
			String[][] seatOccupacy = ticket.getScreening().getSeatOccupancy();
			int rowIndex = 0, columnIndex = 0;
			//Searches the array to find the row and column of the seat that matches the ticket id
			for (int rows = 0; rows < ticket.getScreening().getRoom().getNoOfRows(); rows++) {
				for (int columns = 0; columns < ticket.getScreening().getRoom().getNoOfColumns(); columns++) {
					//Checks if the element of the array equals the id of the ticket (the element of the array has the id of the ticket related to that seat)
					if (seatOccupacy[rows][columns].equals(ticket.getId())) {
						rowIndex = rows;
						columnIndex = columns;
						break;
					}
				}
			}
			//Finds the letter of the seat's row and stores it to a char variable
			char rowLetter = (char) ('A' + rowIndex);
			//Finds the number of the seat's column and stores it to a string variable
			String columnNumber = Integer.toString(columnIndex + 1);
			//Concatenates the letter and the number to one string (e.g. A1)
			String seat = rowLetter + columnNumber;
			return new SimpleStringProperty(seat);
		});
		ticketTableView.getColumns().add(seatColumn);
		
		TableColumn<Ticket, String> roomColumn = new TableColumn<>("Αίθουσα");
		roomColumn.setCellValueFactory(cellData -> {
			Ticket ticket = cellData.getValue();
			//Gets the id of the room in which the movie is screened
			String room = ticket.getScreening().getRoom().getId();
			return new SimpleStringProperty(room);
		});
		ticketTableView.getColumns().add(roomColumn);
		
		TableColumn<Ticket, String> ticketTypeColumn = new TableColumn<>("Τύπος Εισιτηρίου");
		ticketTypeColumn.setCellValueFactory(cellData -> {
			Ticket ticket = cellData.getValue();
			String ticketType;
			//Checks if the ticket type is "Regular", "Kid" or "Student" and gives to the ticketType variable the proper type in Greek
			if (ticket.getTicketType().equals("Regular")) {
				ticketType = "Κανονικό";
			}
			else if (ticket.getTicketType().equals("Kid")) {
				ticketType = "Παιδικό";
			}
			else {
				ticketType = "Φοιτητικό";
			}
			return new SimpleStringProperty(ticketType);
		});
		ticketTableView.getColumns().add(ticketTypeColumn);
		
		//Attaches an event to the TableView
		ticketTableView.setOnMouseClicked(event -> {
			//If the TableView is clicked with the left mouse button
			if (event.getButton() == MouseButton.PRIMARY) {
				//Gets the ticket that has been selected
				Ticket selectedTicket = ticketTableView.getSelectionModel().getSelectedItem();
				//Checks if the selected ticket is null (which means an empty row has been selected)
				if (selectedTicket != null) {
					//If not it calls the method that handles the TableView actions
					handleTableView(selectedTicket);
				}
			}
		});
		
		//Sets ComboBoxes, Labels and buttons invisible
		setInvisibility();
		
		Random randomNumber = new Random();
		int randomEvent;
		//Creates 20 Ticket instances 
		for (int i = 0; i < 20; i++) {
			//Searches for a random event that is an instance of RegularShow
			while (true) {
				randomEvent = randomNumber.nextInt(App.events.length); //Gets a random number between 0 and n-1 (n = the length of the event array)
				//Checks if the randomly selected event is an instance of RegularShow
				if (App.events[randomEvent] instanceof RegularShow) {
					break; //Exists the while loop
				}
			}
			//Gets the random event's info
			String movie = App.events[randomEvent].getMovie().getName();
			
			DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM");
			DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
			
			String date = App.events[randomEvent].getScreeningDate().format(dateFormat);
			String time = App.events[randomEvent].getScreeningDate().format(timeFormat);
			
			String room = App.events[randomEvent].getRoom().getId() + " (" + App.events[randomEvent].getRoom().getDescription() + ")";
			
			//Gets a random ticket type
			String ticketType;
			int randomTicketType = randomNumber.nextInt(3);
			
			if (randomTicketType == 0) {
				ticketType = "Κανονικό (8€)";
			}
			else if (randomTicketType == 1) {
				ticketType = "Παιδικό (6€)";
			}
			else {
				ticketType = "Φοιτητικό (6€)";
			}

			//Gets the seatOccupancy array of the event
			String[][] seatOccupancy = ((RegularShow) App.events[randomEvent]).getSeatOccupancy();
			//Loops until it finds a seat that has not been occupied
			while (true) {
				int randomRow = randomNumber.nextInt(App.events[randomEvent].getRoom().getNoOfRows()); //Gets a random row number
				int randomColumn = randomNumber.nextInt(App.events[randomEvent].getRoom().getNoOfColumns()); //Gets a random column number
				//Checks if the seat is free by checking if the string the seatOccupancy array contains is "0"
				if (seatOccupancy[randomRow][randomColumn].equals("0")) {
					setSelectedSeatRow(randomRow); //Sets the seat row
					setSelectedSeatColumn(randomColumn); //Sets the seat column
					break;
				}
			}
			//Creates a ticket
			createTicket(movie, date, time, room, ticketType);
		}	
	}

	/*This method is used to make the labels, button and ComboBoxes used for the issuance of the ticket, invisible. It is called after issuing a ticket*/
	public void setInvisibility() {
		seatSelectionButton.setVisible(false);
		dateLabel.setVisible(false);
		timeLabel.setVisible(false);
		roomLabel.setVisible(false);
		seatLabel.setVisible(false);
		ticketTypeLabel.setVisible(false);
		dateSelectionBox.setVisible(false);
		timeSelectionBox.setVisible(false);
		roomSelectionBox.setVisible(false);
		ticketTypeBox.setVisible(false);
	}

	//Setters & Getters
	public void setSelectedSeatRow(int selectedSeatRow) {
		this.selectedSeatRow= selectedSeatRow;
	}
	public int getSelectedSeatRow() {
		return this.selectedSeatRow;
	}
	
	public void setSelectedSeatColumn(int selectedSeatColumn) {
		this.selectedSeatColumn = selectedSeatColumn;
	}
	public int getSelectedSeatColumn() {
		return this.selectedSeatColumn;
	}
	
	public void setSelectedMovie(String selectedMovie) {
		this.selectedMovie = selectedMovie;
	}
	public String getSelectedMovie() {
		return this.selectedMovie;
	}
	
	public void setSelectedDate(String selectedDate) {
		this.selectedDate= selectedDate;
	}
	public String getSelectedDate() {
		return this.selectedDate;
	}
	
	public void setSelectedTime(String selectedTime) {
		this.selectedTime = selectedTime;
	}
	public String getSelectedTime() {
		return this.selectedTime;
	}
	
	public void setSelectedRoom(String selectedRoom) {
		this.selectedRoom = selectedRoom;
	}
	public String getSelectedRoom() {
		return this.selectedRoom;
	}
	
	public void setSelectedTicketType(String selectedTicketType) {
		this.selectedTicketType = selectedTicketType;
	}
	public String getSelectedTicketType() {
		return this.selectedTicketType;
	}
	
	public void setSeatSelectionSceneCreator(SceneCreator seatSelectionSceneCreator) {
		this.seatSelectionSceneCreator = (SeatSelectionSceneCreator) seatSelectionSceneCreator;
	}
	
	//This method is used to create a new Scene of the current class
	public Scene createScene() {
		return new Scene(rootGridPane, width, height);
	}

	/*This method is used to set the available dates of the screening according to the movie the user has selected*/
	public void setAvailableDates() {
		
		//The date selection ComboBox is cleared
		dateSelectionBox.getItems().clear();
		
		//Iterating over the events array of the App class
		for (int i = 0; i < App.events.length; i++) {
			//Checks if the element of the array is an instance of RegularShow
			if (App.events[i] instanceof RegularShow) {
				//Checks if the name of the selected movie matches the name of the movie of the event
				if (App.events[i].getMovie().getName().equals(selectedMovie)) {
					//Checks if all the seats have been occupied
					boolean eventFull = true;
					String seatOccupancy[][] = ((RegularShow)App.events[i]).getSeatOccupancy();
					//Iterates over the seatOccupancy array and finds if at least one element is zero (meaning there is at least one free seat)
					for (int row = 0; row < App.events[i].getRoom().getNoOfRows(); row++) {
						for (int column = 0; column < App.events[i].getRoom().getNoOfColumns(); column++) {
							if (seatOccupancy[row][column].equals("0")) {
								eventFull = false;
							}
						}
					}
					//If there is not a free seat it skips the loop
					if (eventFull) {
						continue;
					}
					
					//Creates a dd/MM formatter
					DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM");
					//Uses the formatter to get the screening date corresponding to the event in a dd/MM format
					String formattedDate = App.events[i].getScreeningDate().format(format);
				
					//Flag for the existence of the date
					boolean dateExists = false;
					//Iterates over the date selection ComboBox items
					for (String date : dateSelectionBox.getItems()) {
						//Checks if the date already exists in the ComboBox
						if (date.equals(formattedDate)) {
							//Sets the flag to true
							dateExists = true;
						}
					}
					//Uses the flag to check if the date already exists. If it doesn't, it adds it to the ComboBox
					if (dateExists == false) {
						dateSelectionBox.getItems().add(formattedDate);
					}
				}
			}
		}
	}
	
	/*This method sets the available times of the screening according to the movie and date the user has selected*/
	public void setAvailableTimes() {
		//Clears the time selection ComboBox
		timeSelectionBox.getItems().clear();
		
		//Iterates over the events array of the App class
		for (int i = 0; i < App.events.length; i++) {
			//Checks if the element of the array is an instance of RegularShow
			if (App.events[i] instanceof RegularShow) {
				//Creates a dd/MM formatter
				DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM");
				//Uses the formatter to get the screening date corresponding to the event in a dd/MM format	
				String formattedDate = App.events[i].getScreeningDate().format(dateFormat);
			
				/*Checks if the selected movie matches the one of the event and if the selected date matches the one of the event*/
				if ((App.events[i].getMovie().getName().equals(selectedMovie)) && (formattedDate.equals(selectedDate))) {
					//Checks if all the seats have been occupied
					boolean eventFull = true;
					String seatOccupancy[][] = ((RegularShow)App.events[i]).getSeatOccupancy();
					//Iterates over the seatOccupancy array and finds if at least one element is zero (meaning there is at least one free seat)
					for (int row = 0; row < App.events[i].getRoom().getNoOfRows(); row++) {
						for (int column = 0; column < App.events[i].getRoom().getNoOfColumns(); column++) {
							if (seatOccupancy[row][column].equals("0")) {
								eventFull = false;
							}
						}
					}
					//If there is not a free seat it skips the loop
					if (eventFull) {
						continue;
					}
					
					//Creates a HH:mm formatter
					DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
					//Uses the formatter to get the screening time corresponding to the event in a HH:mm format
					String formattedTime = App.events[i].getScreeningDate().toLocalTime().format(timeFormat);
				
					//Flag for the existence of the time
					boolean timeExists = false;
					//Iterates over the time selection ComboBox items
					for (String time : timeSelectionBox.getItems()) {
						//Checks if the time already exists in the ComboBox
						if (time.equals(formattedTime)) {
							//Sets the flag to true
							timeExists = true;
						}
					}
					//Uses the flag to check if the time already exists. If it doesn't, it adds it to the ComboBox
					if (timeExists == false) {
						timeSelectionBox.getItems().add(formattedTime);
					}
				}
			}
		}
	}

	/*This method sets the available rooms of the screening according to the movie, date and time the user has selected*/
	public void setAvailableRooms() {
		//Clears the room selection ComboBox
		roomSelectionBox.getItems().clear();
	
		//Iterates over the events array of the App class
		for (int i = 0; i < App.events.length; i++) {
			//Checks if the element of the array is an instance of RegularShow
			if (App.events[i] instanceof RegularShow) {
				//Creates a dd/MM formatter
				DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM");
				//Uses the formatter to get the screening date corresponding to the event in a dd/MM format	
				String formattedDate = App.events[i].getScreeningDate().format(dateFormat);
			
				//Creates a HH:mm formatter
				DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
				//Uses the formatter to get the screening time corresponding to the event in a HH:mm format
				String formattedTime = App.events[i].getScreeningDate().toLocalTime().format(timeFormat);
			
				/*Checks if the selected movie, date and time match the ones of the event*/
				if ((App.events[i].getMovie().getName().equals(selectedMovie)) && (formattedDate.equals(selectedDate)) && (formattedTime.equals(selectedTime))) {
					//Checks if all the seats have been occupied
					boolean eventFull = true;
					String seatOccupancy[][] = ((RegularShow)App.events[i]).getSeatOccupancy();
					//Iterates over the seatOccupancy array and finds if at least one element is zero (meaning there is at least one free seat)
					for (int row = 0; row < App.events[i].getRoom().getNoOfRows(); row++) {
						for (int column = 0; column < App.events[i].getRoom().getNoOfColumns(); column++) {
							if (seatOccupancy[row][column].equals("0")) {
								eventFull = false;
							}
						}
					}
					//If there is not a free seat it skips the loop
					if (eventFull) {
						continue;
					}
					
					//Gets the id of the room
					String roomId = App.events[i].getRoom().getId();
				
					//Flag for the existence of the room
					boolean roomExists = false;
					//Iterates over the room selection ComboBox items
					for (String room : roomSelectionBox.getItems()) {
						//Checks if the room already exists in the ComboBox
						if (room.equals(roomId)) {
							//Sets the flag to true
							roomExists = true;
						}
					}
				
					//Uses the flag to check if the room already exists. If it doesn't, it adds it to the ComboBox
					if (roomExists == false) {
						roomSelectionBox.getItems().add(roomId + " (" + App.events[i].getRoom().getDescription() + ")");
					}
				}
			}
		}
	}
	
	/*This method sets the available ticket types*/
	public void setAvailableTicketTypes() {
		//Clears the ticket type ComboBox
		ticketTypeBox.getItems().clear();
		
		//Fills the ticket type selection ComboBox with the ticket types 
		ticketTypeBox.getItems().addAll("Κανονικό (8€)", "Παιδικό (6€)", "Φοιτητικό (6€)");
		
	}
	
	/*This method creates a Ticket object according to the selections of the user*/
	public void createTicket(String movie, String date, String time, String room, String ticketType) {
		//Increases the ticket counter by one
		ticketCounter++;
		
		LocalDateTime currentDate = LocalDateTime.now(); //Stores the current date in a LocalDateTime object
		DateTimeFormatter format = DateTimeFormatter.ofPattern("ddMMyyyy"); //Creates a ddMMyyyy formatter
		String formattedDate = currentDate.format(format); //Uses the formatter to get the current date in a ddMMyyyy format
		
		//Creates the ticket id using the formatted date and the ticket number 
		String ticketId = String.format("<%s>-<%02d>-Ticket", formattedDate, ticketCounter);
		
		int eventNumber = 0;
		//Iterates over the events array of the App class
		for (int i = 0; i < App.events.length; i++) {
			
			//Checking if the Event is a RegularShow
			if (App.events[i] instanceof RegularShow) {
				
				//Event's info
				String eventMovie = App.events[i].getMovie().getName();
			
				DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM");
				String eventDate = App.events[i].getScreeningDate().format(dateFormat);
			
				DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
				String eventTime = App.events[i].getScreeningDate().toLocalTime().format(timeFormat);
			
				String eventRoom = App.events[i].getRoom().getId() + " (" + App.events[i].getRoom().getDescription() + ")";
				
				//Checks if the user's selections are equal to the event's info
				if ((movie.equals(eventMovie)) && (date.equals(eventDate)) && (time.equals(eventTime)) && (room.equals(eventRoom))) {
					
					//When the event is found we get the number of the event
					eventNumber = i;
					//Uses the RegularShow's method to occupy the seat 
					((RegularShow) App.events[i]).occupySeat(ticketId, getSelectedSeatRow(), getSelectedSeatColumn());
					break;
					}
				}
			}
		String type;
		//Gets the ticket type according to the user's selection
		if (ticketType.equals("Κανονικό (8€)")) {
			type = "Regular";
		}
		else if (ticketType.equals("Παιδικό (6€)")) {
			type = "Kid";
		}
		else {
			type = "Student";
		}
		
		//Creates a Ticket object
		Ticket ticket = new Ticket(ticketId, (RegularShow) App.events[eventNumber], currentDate, type);
		//Sets the price of the ticket using the calculateTotalPrice method
		ticket.setPrice(ticket.calculateTotalPrice());

		ticketList.add(ticket); //Adds the ticket to the ticket ArrayList
		
		tableSync(); //Syncs the TableView
		
	}
	
	/*This method modifies a selected ticket's info*/
	public void modifyTicket(String movie, String date, String time, String room, String ticketType) {
		
		//Frees the occupied seat before changing the ticket's info
		ticketSelectedFromTableView.getScreening().freeSeat(ticketSelectedFromTableView.getId());
		
		int eventNumber = 0;
		//Iterates over the events array of the App class
		for (int i = 0; i < App.events.length; i++) {
			
			//Checking if the Event is a RegularShow
			if (App.events[i] instanceof RegularShow) {
				
				//Event's info
				String eventMovie = App.events[i].getMovie().getName();
			
				DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM");
				String eventDate = App.events[i].getScreeningDate().format(dateFormat);
			
				DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
				String eventTime = App.events[i].getScreeningDate().toLocalTime().format(timeFormat);
			
				String eventRoom = App.events[i].getRoom().getId() + " (" + App.events[i].getRoom().getDescription() + ")";
				
				//Checks if the user's selections are equal to the event's info
				if ((movie.equals(eventMovie)) && (date.equals(eventDate)) && (time.equals(eventTime)) && (room.equals(eventRoom))) {
					
					//When the event is found we get the number of the event
					eventNumber = i;
					//Uses the RegularShow's method to occupy the seat 
					((RegularShow) App.events[i]).occupySeat(ticketSelectedFromTableView.getId(), getSelectedSeatRow(), getSelectedSeatColumn());
					break;
					}
				}
			}
		String type;
		//Gets the ticket type according to the user's selection
		if (ticketType.equals("Κανονικό (8€)")) {
			type = "Regular";
		}
		else if (ticketType.equals("Παιδικό (6€)")) {
			type = "Kid";
		}
		else {
			type = "Student";
		}
		
		//Changes the event and the ticket type
		ticketSelectedFromTableView.setScreening((RegularShow) App.events[eventNumber]);
		ticketSelectedFromTableView.setTicketType(type);
		
		tableSync(); //Syncs the TableView
		
	}
	
	/*This method cancels a selected ticket*/
	public void cancelTicket() {
		//Frees the occupied seat
		ticketSelectedFromTableView.getScreening().freeSeat(ticketSelectedFromTableView.getId());
		
		//Iterates over the ticket ArrayList
		for (int i = 0; i < ticketList.size(); i++) {
			//Finds the selected ticket in the array list
			if (ticketList.get(i) == ticketSelectedFromTableView) {
				//Removes the ticket from the list
				ticketList.remove(i);
				break;
			}
		}
		
		tableSync(); //Syncs the TableView
	}
	
	/*This method syncs the TableView with the items of the ticket List*/
	public void tableSync() {
		//Gets the list of the TableView
		List<Ticket> items = ticketTableView.getItems();
		//Clears the List
		items.clear();
		//Adds the ticket items of the ArrayList to the TableView
		for (Ticket t : ticketList) {
			items.add(t);
		}
	}
	
	/*This method clears the ComboBoxes selections*/
	public void clearFields() {
		//Clears the selections
		movieSelectionBox.getSelectionModel().clearSelection();
		dateSelectionBox.getSelectionModel().clearSelection();
		timeSelectionBox.getSelectionModel().clearSelection();
		roomSelectionBox.getSelectionModel().clearSelection();
		ticketTypeBox.getSelectionModel().clearSelection();
		
		//Sets the value of the ComboBoxes to null
		movieSelectionBox.setValue(null);
	    dateSelectionBox.setValue(null);
	    timeSelectionBox.setValue(null);
	    roomSelectionBox.setValue(null);
	    ticketTypeBox.setValue(null);
	}
	
	//Overridden method of the EventHandler interface that is used to handle the various actions
	@Override
	public void handle(ActionEvent event) {

		//Gets the values of the user's selections
		selectedMovie = movieSelectionBox.getValue();
		selectedDate = dateSelectionBox.getValue();
		selectedTime = timeSelectionBox.getValue();
		selectedRoom = roomSelectionBox.getValue();
		selectedTicketType = ticketTypeBox.getValue();
		
		//Checks if the user has interacted with the movie selection ComboBox
		if (event.getSource() == movieSelectionBox) {
			//Sets the available dates
			setAvailableDates();
			//Makes the date Label and ComboBox visible
			dateLabel.setVisible(true);
			dateSelectionBox.setVisible(true);
		}
	
		//Checks if the user has interacted with the date selection ComboBox
		if (event.getSource() == dateSelectionBox) {
			//Sets the available times
			setAvailableTimes();
			//Makes the time Label and ComboBox visible
			timeLabel.setVisible(true);
			timeSelectionBox.setVisible(true);
		}

		//Checks if the user has interacted with the time Selection ComboBox
		if (event.getSource() == timeSelectionBox) {
			//Sets the available rooms
			setAvailableRooms();
			//Makes the room Label and ComboBox visible
			roomLabel.setVisible(true);
			roomSelectionBox.setVisible(true);
		}
		
		//Checks if the user has interacted with the room selection ComboBox
		if (event.getSource() == roomSelectionBox) {
			//Makes the seat Label and Button visible
			seatLabel.setVisible(true);
			seatSelectionButton.setVisible(true);
		}
		
		//Checks if the user has interacted with the seat selection Button
		if (event.getSource() == seatSelectionButton) {
			//Sets the available seats
			seatSelectionSceneCreator.setAvailableSeats();
			//Changes the scene to the seat selection scene and the title 
			App.stage.setScene(App.seatSelectionScene);
			App.stage.setTitle("Επιλογή Θέσης");
			//Makes the ticket type Label and ComboBox visible
			ticketTypeLabel.setVisible(true);
			ticketTypeBox.setVisible(true);
		}
		
		//Checks if the user has interacted with the ticket issuance Button
		if (event.getSource() == ticketIssuanceButton) {
			//Checks if any of the selections is null
			if ((selectedMovie == null) || (selectedDate == null) || (selectedTime == null) || (selectedRoom == null) || (selectedTicketType == null)) {
				//Shows an error message asking to fill all the selections
				Alert issuanceFailed = new Alert(Alert.AlertType.ERROR);
				issuanceFailed.setTitle("Αδυναμία Έκδοσης Εισιτηρίου");
				issuanceFailed.setContentText("Δεν ήταν δυνατή η έκδοση εισιτηρίου καθώς δεν έχουν συμπληρωθεί όλα τα πεδία. Παρακαλώ συμπληρώστε τα και προσπαθήστε ξανά");
				issuanceFailed.show();
			}
			else {
				//Creates a ticket 
				createTicket(selectedMovie, selectedDate, selectedTime, selectedRoom, selectedTicketType);
				clearFields();
				setInvisibility();
			}
		}
		
		//Checks if the user has interacted with the ticket modification button
		if (event.getSource() == ticketModificationButton) {
			//Gets the current date and time
			LocalDateTime currentDateTime = LocalDateTime.now();
			
			//Finds the difference between the time of the movie screening and the current time
			long minutesBetween = ChronoUnit.MINUTES.between(currentDateTime, ticketSelectedFromTableView.getScreening().getScreeningDate());
			
			//Checks if the difference is under 2 hours
			if (minutesBetween < 120) {
				//Shows an error message saying that its not possible to modify the ticket because the screening is in less than 2 hours
				Alert modificationFailed = new Alert(Alert.AlertType.ERROR);
				modificationFailed.setTitle("Αδυναμία Τροποποίησης Εισιτηρίου");
				modificationFailed.setContentText("Δεν ήταν δυνατή η τροποποίηση του εισιτηρίου καθώς η έναρξη της προβολής είναι σε λιγότερο απο 2 ώρες");
				modificationFailed.show();
				
				clearFields();
				setInvisibility();
			}
			else {
				//Modifies the selected ticket
				modifyTicket(selectedMovie, selectedDate, selectedTime, selectedRoom, selectedTicketType);
				clearFields();
				setInvisibility();
			}
			//Disables the modification button and cancellation button
			ticketModificationButton.setDisable(true);
			ticketCancellationButton.setDisable(true);
			//Enables the ticket issuance button
			ticketIssuanceButton.setDisable(false);
		}
		//Checks if the user has interacted with the ticket cancellation button
		if (event.getSource() == ticketCancellationButton) {
			//Gets the current date and time
			LocalDateTime currentDateTime = LocalDateTime.now();
			
			//Finds the difference between the time of the movie screening and the current time
			long minutesBetween = ChronoUnit.MINUTES.between(currentDateTime, ticketSelectedFromTableView.getScreening().getScreeningDate());
			
			//Checks if the difference is under 2 hours
			if (minutesBetween < 120) {
				//Shows an error message saying that its not possible to cancel the ticket because the screening is in less than 2 hours
				Alert cancellationFailed = new Alert(Alert.AlertType.ERROR);
				cancellationFailed.setTitle("Αδυναμία Ακύρωσης Εισιτηρίου");
				cancellationFailed.setContentText("Δεν ήταν δυνατή η ακύρωση του εισιτηρίου καθώς η έναρξη της προβολής είναι σε λιγότερο απο 2 ώρες");
				cancellationFailed.show();
				
				clearFields();
				setInvisibility();
			}
			else {
				//Cancels the selected ticket
				cancelTicket();
				clearFields();
				setInvisibility();
			}
			//Disables the modification button and cancellation button
			ticketModificationButton.setDisable(true);
			ticketCancellationButton.setDisable(true);
			//Enables the ticket issuance button
			ticketIssuanceButton.setDisable(false);
		}
		//Checks if the user has interacted with the back button
		if (event.getSource() == backButton) {
			App.stage.setTitle("Java Cinema");
			App.stage.setScene(App.mainScene);
		}
	}
	
	/*This method handles the TableView events*/
	public void handleTableView(Ticket selectedTicket) {
		//Disables the ticket issuance button
		ticketIssuanceButton.setDisable(true);
		
		//Makes the ticket modification button and ticket cancellation button clickable
		ticketModificationButton.setDisable(false);
		ticketCancellationButton.setDisable(false);
		
		ticketSelectedFromTableView = selectedTicket;
		
		//Gets the info of the selected ticket
		String movie = selectedTicket.getScreening().getMovie().getName();
		
		DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM");
		String date = selectedTicket.getScreening().getScreeningDate().format(dateFormat);
		
		DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
		String time = selectedTicket.getScreening().getScreeningDate().format(timeFormat);
		
		String room = selectedTicket.getScreening().getRoom().getId() + " (" + selectedTicket.getScreening().getRoom().getDescription() + ")";
		
		String ticketType;
		if (selectedTicket.getTicketType().equals("Regular")) {
			ticketType = "Κανονικό (8€)";
		}
		else if (selectedTicket.getTicketType().equals("Kid")) {
			ticketType = "Παιδικό (6€)";
		}
		else {
			ticketType = "Φοιτητικό (6€)";
		}
		
		//Makes the Labels, ComboBoxes and button visible
		dateLabel.setVisible(true);
		dateSelectionBox.setVisible(true);
		timeLabel.setVisible(true);
		timeSelectionBox.setVisible(true);
		seatLabel.setVisible(true);
		seatSelectionButton.setVisible(true);
		roomLabel.setVisible(true);
		roomSelectionBox.setVisible(true);
		ticketTypeLabel.setVisible(true);
		ticketTypeBox.setVisible(true);
		
		//Sets the value of the ComboBoxes to the info of the selected ticket
		movieSelectionBox.setValue(movie);
		dateSelectionBox.setValue(date);
		timeSelectionBox.setValue(time);
		roomSelectionBox.setValue(room);
		ticketTypeBox.setValue(ticketType);
	}
}
