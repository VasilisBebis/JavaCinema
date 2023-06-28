package unipi.java.JavaCinema.gui;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import unipi.java.JavaCinema.core.*;

public class PartyHandlingSceneCreator extends SceneCreator implements EventHandler<ActionEvent> {

	//List of parties
	ArrayList<BirthdayParty> partyList;
	//FlowPane of the buttons
	FlowPane buttonFlowPane;
	//The root GridPane of the party handling scene and the secondary GridPane used to arrange the reservation selections
	GridPane rootGridPane, reservationGridPane;
	//All the buttons used in the scene
	Button partyEntryButton, partyModificationButton, partyCancellationButton, backButton;
	//The various labels used in the reservation GridPane
	Label screeningLabel, dateLabel, timeLabel, roomLabel, guestsLabel, menuLabel, birthdayCakeLabel, priceLabel;
	//Text fields that shows the number of guests and the total price of the party 
	TextField guestsField, priceField;
	//ComboBoxes used for selections 
	ComboBox<String> movieSelectionBox, dateSelectionBox, timeSelectionBox, roomSelectionBox, menuSelectionBox, cakeSelectionBox;
	//TableView of the parties 
	TableView<BirthdayParty> partyTableView;
	//The selected movie, date, time, room, number of guests, menu and cake
	private String selectedMovie, selectedDate, selectedTime, selectedRoom, guests, selectedMenu, hasCake;
	//The party selected from the tableView
	private BirthdayParty partySelectedFromTableView;
	//Counts the parties that have been scheduled
	private int partyCounter;
	
	//Constructor
	public PartyHandlingSceneCreator(int width, int height) {
		//Using the constructor of the superclass SceneCreator
		super(width, height);
		
		partyList = new ArrayList<>(); //Creates ArrayList object
		
		partyCounter = 0; //Initializes the counter to 0
		
		//Creates the different pane objects
		buttonFlowPane = new FlowPane();
		rootGridPane = new GridPane();
		reservationGridPane = new GridPane();
		
		//Initializes the Buttons
		partyEntryButton = new Button("Καταχώριση Νέου Πάρτι");
		partyModificationButton = new Button("Τροποποίηση Στοιχείων Πάρτι");
		partyCancellationButton = new Button("Ακύρωση Πάρτι");
		backButton = new Button("Επιστροφή στην Αρχική Οθόνη");
		
		//Attaches events to the buttons
		partyEntryButton.setOnAction(this);
		partyModificationButton.setOnAction(this);
		partyCancellationButton.setOnAction(this);
		backButton.setOnAction(this);
		
		//Disables the party modification and cancellation buttons
		partyModificationButton.setDisable(true);
		partyCancellationButton.setDisable(true);
		
		//Adds a tooltip to the party modification button
		Tooltip partyModificationTooltip = new Tooltip("Η τροποποίηση των στοιχείων του πάρτι είναι εφικτή μέχρι 2 μέρες πρίν την προγραμματισμένη ημερομηνία του πάρτι");
		partyModificationTooltip.setShowDelay(Duration.millis(500)); //Sets the time it takes for the tooltip to appear to 500 milliseconds
		Tooltip.install(partyModificationButton, partyModificationTooltip);
		
		//Adds a tooltip to the party cancellation button
		Tooltip partyCancellationTooltip = new Tooltip("Η ακύρωση πάρτι είναι εφικτή μέχρι 2 μέρες πριν την προγραμματισμένη ημερομηνία του πάρτι");
		partyCancellationTooltip.setShowDelay(Duration.millis(500)); //Sets the time it takes for the tooltip to appear to 500 milliseconds
		Tooltip.install(partyCancellationButton, partyCancellationTooltip);
		
		//Initializes the labels
		screeningLabel = new Label("Ταινία:");
		dateLabel = new Label("Μέρα Προβολής:");
		timeLabel = new Label("Ώρα Προβολής:");
		roomLabel = new Label("Αίθουσα Προβολής:");
		guestsLabel = new Label("Αριθμός Ατόμων:");
		menuLabel = new Label("Μενού:");
		birthdayCakeLabel = new Label("Τούρτα:");
		priceLabel = new Label("Τελικό Κόστος:");
		
		//Initializes the text fields
		guestsField = new TextField();
		priceField = new TextField();
		
		//Creates a text formatter
		TextFormatter<Integer> textFormatter = new TextFormatter<>(change -> {
			//Retrieves the text entered in the text field
			String text = change.getControlNewText();
			//Checks if the text matches the regular expression and is not over 3 characters
			if (text.matches("\\d*") && text.length() <= 3) {
				//Returns the change
				return change;
			}
			else {
				//Returns null
				return null;
			}
		});
		//Applies the text formatter to the guests text field so it only accepts integer numbers
		guestsField.setTextFormatter(textFormatter);
		//Attaches an event to the guests field
		guestsField.setOnAction(this);
		
		//Makes the price field not editable
		priceField.setDisable(true);
		
		//Initializes ComboBoxes
		movieSelectionBox = new ComboBox<>();
		dateSelectionBox = new ComboBox<>();
		timeSelectionBox = new ComboBox<>();
		roomSelectionBox = new ComboBox<>();
		menuSelectionBox = new ComboBox<>();
		cakeSelectionBox = new ComboBox<>();
		
		//Fills the movie selection ComboBox with the different kids movies names 
		for (int i = 0; i < 4; i++) {
			movieSelectionBox.getItems().add(App.movies[i].getName());
		}
		
		//Fills the menu selection ComboBox with the different menu options
		menuSelectionBox.getItems().addAll("Παιδικό (6€/άτομο)", "Ποπ Κορν & Αναψυκτικό (3€/άτομο)");
		
		//Fills the cake selection ComboBox with the different options
		cakeSelectionBox.getItems().addAll("Ναι", "Όχι");
		
		//Attaches events to the ComboBoxes
		movieSelectionBox.setOnAction(this);
		dateSelectionBox.setOnAction(this);
		timeSelectionBox.setOnAction(this);
		roomSelectionBox.setOnAction(this);
		menuSelectionBox.setOnAction(this);
		cakeSelectionBox.setOnAction(this);
		
		//Initializes the TableView
		partyTableView = new TableView<>();
		
		//Customizes the button FlowPane 
		buttonFlowPane.setHgap(10);
		buttonFlowPane.setAlignment(Pos.CENTER);
		
		//Adds the buttons to the FlowPane
		buttonFlowPane.getChildren().add(partyEntryButton);
		buttonFlowPane.getChildren().add(partyModificationButton);
		buttonFlowPane.getChildren().add(partyCancellationButton);
		
		//Customizes the reservation GridPane
		reservationGridPane.setAlignment(Pos.TOP_RIGHT);
		reservationGridPane.setHgap(10);
		reservationGridPane.setVgap(10);
		
		//Adds the labels, ComboBoxes and text fields to the GridPane
		reservationGridPane.add(screeningLabel, 0, 0);
		reservationGridPane.add(movieSelectionBox, 1, 0);
		reservationGridPane.add(dateLabel, 0, 1);
		reservationGridPane.add(dateSelectionBox, 1, 1);
		reservationGridPane.add(timeLabel, 0, 2);
		reservationGridPane.add(timeSelectionBox, 1, 2);
		reservationGridPane.add(roomLabel, 0, 3);
		reservationGridPane.add(roomSelectionBox, 1, 3);
		reservationGridPane.add(guestsLabel, 0, 4);
		reservationGridPane.add(guestsField, 1, 4);
		reservationGridPane.add(menuLabel, 0, 5);
		reservationGridPane.add(menuSelectionBox, 1, 5);
		reservationGridPane.add(birthdayCakeLabel, 0, 6);
		reservationGridPane.add(cakeSelectionBox, 1, 6);
		reservationGridPane.add(priceLabel, 0, 7);
		reservationGridPane.add(priceField, 1, 7);
		
		//Customizes the root GridPane
		rootGridPane.setHgap(10);		
		rootGridPane.setVgap(10);	
		
		//Adds the secondary GridPane, the TableView, the button FlowPane and the back button in the root GridPane
		rootGridPane.add(reservationGridPane, 1, 0);
		rootGridPane.add(partyTableView, 0, 0);
		rootGridPane.add(buttonFlowPane, 0, 1);
		rootGridPane.add(backButton, 1, 1);

		//Adds Columns to the TableView
		TableColumn<BirthdayParty, String> idColumn = new TableColumn<>("Κωδικός");
		idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
		partyTableView.getColumns().add(idColumn);
		
		TableColumn<BirthdayParty, String> movieColumn = new TableColumn<>("Ταινία");
		movieColumn.setCellValueFactory(cellData -> {
			BirthdayParty party = cellData.getValue();
			//Gets the movies name
			String movieName = party.getMovie().getName();
			return new SimpleStringProperty(movieName);
		});
		partyTableView.getColumns().add(movieColumn);
		
		TableColumn<BirthdayParty, String> dateColumn = new TableColumn<>("Μέρα Πάρτι");
		dateColumn.setCellValueFactory(cellData -> {
			BirthdayParty party = cellData.getValue();
			//Gets the date of the party
			LocalDateTime dateTime = party.getScreeningDate();
			//Creates a dd/MM formatter
			DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM");
			//Formats the date in dd/MM and stores it into a string variable
			String formattedDate = dateTime.format(format);
			return new SimpleStringProperty(formattedDate);
		});
		partyTableView.getColumns().add(dateColumn);
		
		TableColumn<BirthdayParty, String> timeColumn = new TableColumn<>("Ώρα Πάρτι");
		timeColumn.setCellValueFactory(cellData -> {
			BirthdayParty party = cellData.getValue();
			//Gets the date of the screening
			LocalDateTime dateTime = party.getScreeningDate();
			//Creates a HH:ss formatter
			DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:ss");
			//Formats the time in HH:ss and stores in into a string variable
			String formattedTime = dateTime.format(format);
			return new SimpleStringProperty(formattedTime);
		});
		partyTableView.getColumns().add(timeColumn);

		TableColumn<BirthdayParty, String> roomColumn = new TableColumn<>("Αίθουσα");
		roomColumn.setCellValueFactory(cellData -> {
			BirthdayParty party = cellData.getValue();
			//Gets the id of the room in which the party is taking place
			String room = party.getRoom().getId();
			return new SimpleStringProperty(room);
		});
		partyTableView.getColumns().add(roomColumn);
		
		TableColumn<BirthdayParty, String> guestsColumn = new TableColumn<>("Άτομα");
		guestsColumn.setCellValueFactory(new PropertyValueFactory<>("noOfGuests"));
		partyTableView.getColumns().add(guestsColumn);
		
		TableColumn<BirthdayParty, String> menuColumn = new TableColumn<>("Μενού");
		menuColumn.setCellValueFactory(cellData -> {
			BirthdayParty party = cellData.getValue();
			//Finds the menu type
			String menuType;
			//Checks which menu has been selected and sets the menu type
			if (party.getMenuType().equals("Full Menu")) {
				menuType = "Παιδικό";
			}
			else {
				menuType = "Ποπ Κορν & Αναψυκτικό";
			}
			return new SimpleStringProperty(menuType);
		});
		partyTableView.getColumns().add(menuColumn);
		
		TableColumn<BirthdayParty, String> cakeColumn = new TableColumn<>("Τούρτα");
		cakeColumn.setCellValueFactory(cellData -> {
			BirthdayParty party = cellData.getValue();
			String hasBirthdayCake;
			//Checks if the party has birthday cake
			if (party.getHasBirthdayCake()) {
				hasBirthdayCake = "Ναι";
			}
			else {
				hasBirthdayCake = "Όχι";
			}
			return new SimpleStringProperty(hasBirthdayCake);
		});
		partyTableView.getColumns().add(cakeColumn);
		
		TableColumn<BirthdayParty, String> priceColumn = new TableColumn<>("Συνολικό Ποσό");
		priceColumn.setCellValueFactory(cellData -> {
			BirthdayParty party = cellData.getValue();
			//Gets the price of the party
			String price = String.format("%.2f", party.getPrice());
			return new SimpleStringProperty(price);
		});
		partyTableView.getColumns().add(priceColumn);
		
		//Attaches an event to the TableView
		partyTableView.setOnMouseClicked(event -> {
			//If the TableView is clicked with the left mouse button
			if (event.getButton() == MouseButton.PRIMARY) {
				//Gets the ticket that has been selected
				BirthdayParty selectedParty = partyTableView.getSelectionModel().getSelectedItem();
				//Checks if the selected ticket is null (which means an empty row has been selected)
				if (selectedParty != null) {
					//If not it calls the method that handles the TableView actions
					handleTableView(selectedParty);
				}
			}
		});

		//Sets ComboBoxes, Labels and textFields invisible
		setInvisibility();
		
		Random randomNumber = new Random();
		int randomEvent;
		//Creates 5 BirthdayParty instances 
		for (int i = 0; i < 5; i++) {
			//Searches for a random event that is not an instance of RegularShow
			while (true) {
				randomEvent = randomNumber.nextInt(App.events.length); //Gets a random number between 0 and n-1 (n = the length of the event array)
				//Checks if the randomly selected event is not an instance of RegularShow
				if ((App.events[randomEvent] instanceof RegularShow) == false) {
					//Checks if the event has been reserved
					boolean partyReserved = false;
					//Iterates over the party ArrayList
					for (BirthdayParty party : partyList) {
						//If the event exists in the ArrayList it sets the partyReserved flag to true
						if ((party.getMovie().equals(App.events[i].getMovie())) && 
							(party.getScreeningDate().equals(App.events[i].getScreeningDate())) && 
							(party.getRoom().equals(App.events[i].getRoom()))) {
								partyReserved = true;
						}
					}
					//Checks if the party has been reserved
					if (partyReserved == false) {
						break; //Exists the while loop
					}
				}
			}
			//Gets the random event's info
			String movie = App.events[randomEvent].getMovie().getName();
			
			DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM");
			DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
			
			String date = App.events[randomEvent].getScreeningDate().format(dateFormat);
			String time = App.events[randomEvent].getScreeningDate().format(timeFormat);
			
			String room = App.events[randomEvent].getRoom().getId() + " (" + App.events[randomEvent].getRoom().getDescription() + ")";
			
			//Gets a random number of guests between 50 and 150
			String guests;
			int randomGuests = 51 + randomNumber.nextInt(100);
			guests = Integer.toString(randomGuests);
			
			//Selects a random menu
			String menu;
			int randomMenu = randomNumber.nextInt(2);
			
			if (randomMenu == 0) {
				menu = "Παιδικό (6€/άτομο)";
			}
			else {
				menu = "Ποπ Κορν & Αναψυκτικό (3€/άτομο)";
			}
			
			//Selects randomly if there is a cake or not
			String cake;
			int randomCake = randomNumber.nextInt(2);
			
			if (randomCake == 0) {
				cake = "Ναι";
			}
			else {
				cake = "Όχι";
			}
			
			//Creates a party 
			createParty(movie, date, time, room, guests, menu, cake);
			
		}
		
	}
	
	/*This method is used to make the labels, ComboBoxes and text fields used for the party reservation, invisible*/
	public void setInvisibility() {
		dateLabel.setVisible(false);
		timeLabel.setVisible(false);
		roomLabel.setVisible(false);
		guestsLabel.setVisible(false);
		menuLabel.setVisible(false);
		birthdayCakeLabel.setVisible(false);
		priceLabel.setVisible(false);
		guestsField.setVisible(false);
		priceField.setVisible(false);
		dateSelectionBox.setVisible(false);
		timeSelectionBox.setVisible(false);
		roomSelectionBox.setVisible(false);
		menuSelectionBox.setVisible(false);
		cakeSelectionBox.setVisible(false);
	}
	
	//Setters & Getters
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
	
	public void setGuests(String guests) {
		this.guests = guests;
	}
	public String getGuests() {
		return this.guests;
	}
	
	public void setSelectedMenu(String selectedMenu) {
		this.selectedMenu = selectedMenu;
	}
	public String getSelectedMenu() {
		return this.selectedMenu;
	}
	
	public void setHasCake(String hasCake) {
		this.hasCake = hasCake;
	}
	public String getHasCake() {
		return this.hasCake;
	}
	
	//This method is used to create a new Scene of the current class
	public Scene createScene() {
		return new Scene(rootGridPane, width, height);
	}
	
	/*This method is used to set the available dates for the party according to the movie the user has selected*/
	public void setAvailableDates() {
		
		//The date selection ComboBox is cleared
		dateSelectionBox.getItems().clear();
		
		//Iterating over the events array of the App class
		for (int i = 0; i < App.events.length; i++) {
			//Checks if the element of the array is not an instance of RegularShow
			if ((App.events[i] instanceof RegularShow) == false) {
				//Checks if the name of the selected movie matches the name of the movie of the event
				if (App.events[i].getMovie().getName().equals(selectedMovie)) {
					//Checks if the event has been reserved
					boolean partyReserved = false;
					//Iterates over the party ArrayList
					for (BirthdayParty party : partyList) {
						//If the event exists in the ArrayList it sets the partyReserved flag to true
						if ((party.getMovie().equals(App.events[i].getMovie())) && 
							(party.getScreeningDate().equals(App.events[i].getScreeningDate())) && 
							(party.getRoom().equals(App.events[i].getRoom()))) {
								partyReserved = true;
						}
					}
					//If the party is reserved it skips this loop and goes to the next one 
					if (partyReserved) {
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
	
	/*This method sets the available times for the party according to the movie and date the user has selected*/
	public void setAvailableTimes() {
		//Clears the time selection ComboBox
		timeSelectionBox.getItems().clear();
		
		//Iterates over the events array of the App class
		for (int i = 0; i < App.events.length; i++) {
			//Checks if the element of the array is not an instance of RegularShow
			if ((App.events[i] instanceof RegularShow) == false) {
				//Creates a dd/MM formatter
				DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM");
				//Uses the formatter to get the screening date corresponding to the event in a dd/MM format	
				String formattedDate = App.events[i].getScreeningDate().format(dateFormat);
			
				/*Checks if the selected movie matches the one of the event and if the selected date matches the one of the event*/
				if ((App.events[i].getMovie().getName().equals(selectedMovie)) && (formattedDate.equals(selectedDate))) {
					//Checks if the event has been reserved
					boolean partyReserved = false;
					//Iterates over the party ArrayList
					for (BirthdayParty party : partyList) {
						//If the event exists in the ArrayList it sets the partyReserved flag to true
						if ((party.getMovie().equals(App.events[i].getMovie())) && 
							(party.getScreeningDate().equals(App.events[i].getScreeningDate())) && 
							(party.getRoom().equals(App.events[i].getRoom()))) {
								partyReserved = true;
						}
					}
					//If the party is reserved it skips this loop and goes to the next one 
					if (partyReserved) {
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
	
	/*This method sets the available rooms for the party according to the movie, date and time the user has selected*/
	public void setAvailableRooms() {
		//Clears the room selection ComboBox
		roomSelectionBox.getItems().clear();
	
		//Iterates over the events array of the App class
		for (int i = 0; i < App.events.length; i++) {
			//Checks if the element of the array is not an instance of RegularShow
			if ((App.events[i] instanceof RegularShow) == false) {				
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
					//Checks if the event has been reserved
					boolean partyReserved = false;
					//Iterates over the party ArrayList
					for (BirthdayParty party : partyList) {
						//If the event exists in the ArrayList it sets the partyReserved flag to true
						if ((party.getMovie().equals(App.events[i].getMovie())) && 
							(party.getScreeningDate().equals(App.events[i].getScreeningDate())) && 
							(party.getRoom().equals(App.events[i].getRoom()))) {
								partyReserved = true;
						}
					}
					//If the party is reserved it skips this loop and goes to the next one 
					if (partyReserved) {
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
	
	/*This method is used to calculate the total price of the party in order to display it before the user clicks the partyEntryButton.
	 * We want the price to change every time the user changes the number of guests, the menu option or the existence of a cake so we call
	 * the method when one of those ComboBoxes or TextField is clicked. When the user makes the selections for the first time some of these
	 * selections are null and it crashes the program. Thats why we firstly check that none of these values are null*/
	public void setTotalPrice() {
		double totalPrice;
		totalPrice = PriceList.occupationPrice; //Initializes the total price with the cost for the occupation of the room

		//Checks if the guests, selectedMenu and hasCake strings are not null
		if ((guests != null) && (selectedMenu != null) && (hasCake != null)) {
			
			//Checks which menu has been selected
			if (selectedMenu.equals("Παιδικό (6€/άτομο)")) {
				totalPrice += Integer.parseInt(guests) * PriceList.menuPricePerPerson; //Adds the product of the number of guests by the cost for the menu to the total price
			}
			else {
				totalPrice += Integer.parseInt(guests) * PriceList.snacksPricePerPerson; //Adds the product of the number of guests by the cost for the menu to the total price
			}
		
			//Checks if the party has a cake
			if (hasCake.equals("Ναι")) {
				totalPrice += PriceList.birthdayCakePrice; //Adds the price of the cake to the total price
			}
		}
		String price = String.format("%.2f", totalPrice);
		priceField.setText(price);
	}

	public void createParty(String movie, String date, String time, String room, String guests, String menu, String cake) {
		
		//Increase the party counter by one
		partyCounter++;
		
		LocalDateTime currentDate = LocalDateTime.now(); //Stores the current date in a LocalDateTime object
		DateTimeFormatter format = DateTimeFormatter.ofPattern("ddMMyyyy"); //Creates a ddMMyyyy formatter
		String formattedDate = currentDate.format(format); //Uses the formatter to get the current date in a ddMMyyyy format
		
		//Creates the party id using the formatted date and the party number 
		String partyId = String.format("<%s>-<%02d>-Birthday", formattedDate, partyCounter);
		
		int eventNumber = 0;
		//Iterates over the events array of the App class
		for (int i = 0; i < App.events.length; i++) {
			
			//Checking if the Event is not a RegularShow
			if ((App.events[i] instanceof RegularShow) == false) {
				
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
					}
			}
		}	
		
		//Gets the number of guests
		int noOfGuests = Integer.parseInt(guests);
		
		//Gets the menu type
		String menuType;
		//Checks which menu has been selected
		if (menu.equals("Παιδικό (6€/άτομο)")) {
			menuType = "Full Menu";
		}
		else {
			menuType = "Snacks";
		}
		
		//Boolean variable for existence of a cake in the party
		boolean hasBirthdayCake;
		//Checks if the party has a cake
		if (cake.equals("Ναι")) {
			hasBirthdayCake = true;
		}
		else {
			hasBirthdayCake = false;
		}
		
		//Creates a Birthday Party object
		BirthdayParty party = new BirthdayParty(partyId, App.events[eventNumber].getMovie(), App.events[eventNumber].getScreeningDate(), App.events[eventNumber].getRoom(), currentDate, noOfGuests, menuType, hasBirthdayCake);
		//Sets the price for the birthday party
		party.setPrice(party.calculateTotalPrice());
		
		partyList.add(party); //Adds the party to the party arrayList
		
		tableSync(); //Syncs the TableView
	}
	
	public void modifyParty(String movie, String date, String time, String room, String guests, String menu, String cake) {
		
		LocalDateTime currentDate = LocalDateTime.now(); //Stores the current date in a LocalDateTime object
		DateTimeFormatter format = DateTimeFormatter.ofPattern("ddMMyyyy"); //Creates a ddMMyyyy formatter
		String formattedDate = currentDate.format(format); //Uses the formatter to get the current date in a ddMMyyyy format
		
		//Creates the party id using the formatted date and the party number 
		String partyId = String.format("<%s>-<%02d>-Ticket", formattedDate, partyCounter);
		
		int eventNumber = 0;
		//Iterates over the events array of the App class
		for (int i = 0; i < App.events.length; i++) {
			
			//Checking if the Event is not a RegularShow
			if ((App.events[i] instanceof RegularShow) == false) {
				
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
					}
			}
		}	
		
		//Gets the number of guests
		int noOfGuests = Integer.parseInt(guests);
		
		//Gets the menu type
		String menuType;
		//Checks which menu has been selected
		if (selectedMenu.equals("Παιδικό (6€/άτομο)")) {
			menuType = "Full Menu";
		}
		else {
			menuType = "Snacks";
		}
		
		//Boolean variable for existence of a cake in the party
		boolean hasBirthdayCake;
		//Checks if the party has a cake
		if (cake.equals("Ναι")) {
			hasBirthdayCake = true;
		}
		else {
			hasBirthdayCake = false;
		}
		
		//Changes the values
		partySelectedFromTableView.setMovie(App.events[eventNumber].getMovie());
		partySelectedFromTableView.setScreeningDate(App.events[eventNumber].getScreeningDate());
		partySelectedFromTableView.setRoom(App.events[eventNumber].getRoom());
		partySelectedFromTableView.setNoOfGuests(noOfGuests);
		partySelectedFromTableView.setMenuType(menuType);
		partySelectedFromTableView.setHasBirthdayCake(hasBirthdayCake);
		
		tableSync(); //Syncs the TableView
	}
	
	public void cancelParty() {
		//Iterates over the party ArrayList
		for (int i = 0; i < partyList.size(); i++) {
			//Finds the selected party in the array list
			if (partyList.get(i) == partySelectedFromTableView) {
				//Removes the party from the list
				partyList.remove(i);
				break;
			}
		}
		
		tableSync(); //Syncs the TableView
	}
	
	/*This method syncs the TableView with the items of the ticket List*/
	public void tableSync() {
		//Gets the list of the TableView
		List<BirthdayParty> items = partyTableView.getItems();
		//Clears the List
		items.clear();
		//Adds the ticket items of the ArrayList to the TableView
		for (BirthdayParty p: partyList) {
			items.add(p);
		}
	}
	
	/*This method clears the selections*/
	public void clearFields() {
		//Clears the selections
		movieSelectionBox.getSelectionModel().clearSelection();
		dateSelectionBox.getSelectionModel().clearSelection();
		timeSelectionBox.getSelectionModel().clearSelection();
		roomSelectionBox.getSelectionModel().clearSelection();
		menuSelectionBox.getSelectionModel().clearSelection();
		cakeSelectionBox.getSelectionModel().clearSelection();
		
		//Sets the value of the ComboBoxes to null and the values of the the text fields to "" 
		movieSelectionBox.setValue(null);
	    dateSelectionBox.setValue(null);
	    timeSelectionBox.setValue(null);
	    roomSelectionBox.setValue(null);
	    menuSelectionBox.setValue(null);
	    cakeSelectionBox.setValue(null);
	    
	    guestsField.setText("");
	    priceField.setText("");
	}
	
	//Overridden method of the EventHandler interface that is used to handle the various actions
	@Override
	public void handle(ActionEvent event) {
		//Gets the values of the user's selections
		selectedMovie = movieSelectionBox.getValue();
		selectedDate = dateSelectionBox.getValue();
		selectedTime = timeSelectionBox.getValue();
		selectedRoom = roomSelectionBox.getValue();
		guests = guestsField.getText();
		selectedMenu = menuSelectionBox.getValue();
		hasCake = cakeSelectionBox.getValue();
		
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
			//Makes the guest number selection Label and field visible
			guestsLabel.setVisible(true);
			guestsField.setVisible(true);
		}
		
		//Checks if the user has interacted with the guests field
		if (event.getSource() == guestsField) {
			//Sets the total price of the party
			setTotalPrice();
			//Makes the menu selection Label and ComboBox visible
			menuLabel.setVisible(true);
			menuSelectionBox.setVisible(true);
		}
		
		//Checks if the user has interacted with the menu selection ComboBox
		if (event.getSource() == menuSelectionBox) {
			//Sets the total price of the party
			setTotalPrice();
			//Makes the cake selection Label and ComboBox visible
			birthdayCakeLabel.setVisible(true);
			cakeSelectionBox.setVisible(true);
		}
		
		//Checks if the user has interacted with the cake selection ComboBox
		if (event.getSource() == cakeSelectionBox) {
			//Sets the total price of the party
			setTotalPrice();
			//Makes the price Label and Field visible
			priceLabel.setVisible(true);
			priceField.setVisible(true);
		}
		
		//Checks if the user has interacted with the party entry button
		if (event.getSource() == partyEntryButton) {
			//Checks if any of the selections is null
			if ((selectedMovie == null) || (selectedDate == null) || (selectedTime == null) || (selectedRoom == null) || (guests.equals("")) || (selectedMenu == null) || (hasCake == null)) {
				//Shows an error message asking to fill all the selections
				Alert entryFailed = new Alert(Alert.AlertType.ERROR);
				entryFailed.setTitle("Αδυναμία Καταχώρισης Νέου Πάρτι");
				entryFailed.setContentText("Δεν ήταν δυνατή η καταχώριση του πάρτι καθώς δεν έχουν συμπληρωθεί όλα τα πεδία. Παρακαλώ συμπληρώστε τα και προσπαθήστε ξανά");
				entryFailed.show();
			}
			else {
				//Creates a party
				createParty(selectedMovie, selectedDate, selectedTime, selectedRoom, guests, selectedMenu, hasCake);
				clearFields();
				setInvisibility();
			}
		}
		
		//Checks if the user has interacted with the party modification button
		if (event.getSource() == partyModificationButton) {
			//Gets the current date and time
			LocalDateTime currentDateTime = LocalDateTime.now();
			
			//Finds the difference between the time of the party and the current time
			long daysBetween = ChronoUnit.DAYS.between(currentDateTime, partySelectedFromTableView.getScreeningDate());
			
			//Checks if the difference is under 2 days
			if (daysBetween < 2) {
				//Shows an error message saying that its not possible to modify the party because it is in less than 2 days
				Alert modificationFailed = new Alert(Alert.AlertType.ERROR);
				modificationFailed.setTitle("Αδυναμία Τροποποίησης Στοιχείων Πάρτι");
				modificationFailed.setContentText("Δεν ήταν δυνατή η τροποποίηση των στοιχείων του πάρτι καθώς η προγραμματισμένη ημερομηνία είναι σε λιγότερο απο 2 μέρες");
				modificationFailed.show();
				
				clearFields();
				setInvisibility();
			}
			else {
				//modifies the selected party
				modifyParty(selectedMovie, selectedDate, selectedTime, selectedRoom, guests, selectedMenu, hasCake);
				clearFields();
				setInvisibility();
			}
			//Disables the modification button and cancellation button
			partyModificationButton.setDisable(true);
			partyCancellationButton.setDisable(true);
			//Enables the party entry button
			partyEntryButton.setDisable(false);
		}
		
		//Checks if the user has interacted with the party cancellation button
		if (event.getSource() == partyCancellationButton) {
			//Gets the current date and time
			LocalDateTime currentDateTime = LocalDateTime.now();
			
			//Finds the difference between the time of the party and the current time
			long daysBetween = ChronoUnit.DAYS.between(currentDateTime, partySelectedFromTableView.getScreeningDate());
			
			//Checks if the difference is under 2 days
			if (daysBetween < 2) {
				//Shows an error message saying that its not possible to modify the party because it is in less than 2 days
				Alert cancellationFailed = new Alert(Alert.AlertType.ERROR);
				cancellationFailed.setTitle("Αδυναμία Ακύρωσης Πάρτι");
				cancellationFailed.setContentText("Δεν ήταν δυνατή η ακύρωση του πάρτι καθώς η προγραμματισμένη ημερομηνία είναι σε λιγότερο απο 2 μέρες");
				cancellationFailed.show();
				
				clearFields();
				setInvisibility();
			}
			else {
				//cancels the selected party
				cancelParty();
				clearFields();
				setInvisibility();
			}
			//Disables the modification button and cancellation button
			partyModificationButton.setDisable(true);
			partyCancellationButton.setDisable(true);
			//Enables the party entry button
			partyEntryButton.setDisable(false);
		}
		
		//Checks if the user has interacted with the back button
		if (event.getSource() == backButton) {
			App.stage.setTitle("Java Cinema");
			App.stage.setScene(App.mainScene);
		}
	}
	
	/*This method handles the TableView events*/
	public void handleTableView(BirthdayParty selectedParty) {
		//Disables the party entry button
		partyEntryButton.setDisable(true);
		
		//Makes the party modification button and party cancellation button clickable
		partyModificationButton.setDisable(false);
		partyCancellationButton.setDisable(false);
		
		partySelectedFromTableView = selectedParty;
		
		//Gets the info of the selected party
		String movie = selectedParty.getMovie().getName();
				
		DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM");
		String date = selectedParty.getScreeningDate().format(dateFormat);
				
		DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
		String time = selectedParty.getScreeningDate().format(timeFormat);
				
		String room = selectedParty.getRoom().getId() + " (" + selectedParty.getRoom().getDescription() + ")";
		
		String noOfGuests = Integer.toString(selectedParty.getNoOfGuests());
		
		String menuType;
		if (selectedParty.getMenuType().equals("Full Menu")) {
			menuType = "Παιδικό (6€/άτομο)";
		}
		else {
			menuType = "Ποπ Κορν & Αναψυκτικό (3€/άτομο)";
		}
		
		String cake;
		if (selectedParty.getHasBirthdayCake()) {
			cake = "Ναι";
		}
		else {
			cake = "Όχι";
		}
		
		String totalPrice = Double.toString(selectedParty.getPrice());
		
		//Makes the Labels, ComboBoxes and TextFields visible
		dateLabel.setVisible(true);
		dateSelectionBox.setVisible(true);
		timeLabel.setVisible(true);
		timeSelectionBox.setVisible(true);
		roomLabel.setVisible(true);
		roomSelectionBox.setVisible(true);
		guestsLabel.setVisible(true);
		guestsField.setVisible(true);
		menuLabel.setVisible(true);
		menuSelectionBox.setVisible(true);
		birthdayCakeLabel.setVisible(true);
		cakeSelectionBox.setVisible(true);
		priceLabel.setVisible(true);
		priceField.setVisible(true);
		
		//Sets the value of the ComboBoxes and TextFields to the info of the selected party
		movieSelectionBox.setValue(movie);
		dateSelectionBox.setValue(date);
		timeSelectionBox.setValue(time);
		roomSelectionBox.setValue(room);
		guestsField.setText(noOfGuests);
		menuSelectionBox.setValue(menuType);
		cakeSelectionBox.setValue(cake);
		priceField.setText(totalPrice);
	}
}
