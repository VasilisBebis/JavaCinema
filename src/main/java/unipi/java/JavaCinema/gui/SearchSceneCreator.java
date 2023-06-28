package unipi.java.JavaCinema.gui;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import unipi.java.JavaCinema.core.BirthdayParty;
import unipi.java.JavaCinema.core.Ticket;

public class SearchSceneCreator extends SceneCreator implements EventHandler<ActionEvent>{
	
	//FlowPane of the search button
	FlowPane buttonFlowPane;
	//The root GridPane of the party handling scene and the secondary GridPane used to search an event
	GridPane rootGridPane, searchGridPane;
	//The buttons used in the scene
	Button searchButton, backButton;
	//The labels used in the search grid pane
	Label eventTypeLabel, movieLabel;
	//ComboBoxes used for selections 
	ComboBox<String> eventTypeSelectionBox, movieSelectionBox;
	//TableViews for the two event types
	TableView<Ticket> ticketTableView;
	TableView<BirthdayParty> partyTableView;
	//TicketHandlingSceneCreator and PartyHandlingSceneCreator objects to get the array lists
	TicketHandlingSceneCreator ticketHandlingSceneCreator;
	PartyHandlingSceneCreator partyHandlingSceneCreator;
	//Selected movie and event type
	private String selectedEventType, selectedMovie;
	
	//Constructor
	public SearchSceneCreator(int width, int height, SceneCreator ticketHandlingSceneCreator, SceneCreator partyHandlingSceneCreator) {
		//Using the constructor of the superclass SceneCreator
		super(width, height);
		
		this.ticketHandlingSceneCreator = (TicketHandlingSceneCreator) ticketHandlingSceneCreator;
		this.partyHandlingSceneCreator = (PartyHandlingSceneCreator) partyHandlingSceneCreator;
		
		//Creates the different pane objects
		buttonFlowPane = new FlowPane();
		rootGridPane = new GridPane();
		searchGridPane = new GridPane();
		
		//Initialize the buttons
		searchButton = new Button("Αναζήτηση");
		backButton = new Button("Επιστροφή στην Αρχική Οθόνη");
		
		//Attaches events to the buttons
		searchButton.setOnAction(this);
		backButton.setOnAction(this);
		
		//Initializes the labels
		eventTypeLabel = new Label("Τύπος Event:");
		movieLabel = new Label("Τίτλος Ταινίας:");
		
		//Initializes the ComboBoxes
		eventTypeSelectionBox = new ComboBox<>();
		movieSelectionBox = new ComboBox<>();
		
		//Fills the event type selection box
		eventTypeSelectionBox.getItems().addAll("Κανονική Προβολή", "Παιδικό Πάρτι");
		
		//Attaches events to the ComboBoxes
		eventTypeSelectionBox.setOnAction(this);
		movieSelectionBox.setOnAction(this);
		
		//Initializes the TableViews
		ticketTableView = new TableView<>();
		partyTableView = new TableView<>();
		
		//Customizes the button FlowPane 
		buttonFlowPane.setHgap(10);
		buttonFlowPane.setAlignment(Pos.CENTER);
		
		//Adds the button to the FlowPane
		buttonFlowPane.getChildren().add(searchButton);
		
		//Customizes the search GridPane
		searchGridPane.setAlignment(Pos.TOP_RIGHT);
		searchGridPane.setHgap(10);
		searchGridPane.setVgap(10);
		
		//Adds the Labels and ComboBoxes to the search GridPane
		searchGridPane.add(eventTypeLabel, 0, 0);
		searchGridPane.add(eventTypeSelectionBox, 1, 0);
		searchGridPane.add(movieLabel, 0, 1);
		searchGridPane.add(movieSelectionBox, 1, 1);
		
		//Customizes the root GridPane
		rootGridPane.setHgap(10);		
		rootGridPane.setVgap(10);
		
		//Adds the secondary GridPane, the TableView, the button FlowPane and the back button in the root GridPane
		rootGridPane.add(searchGridPane, 1, 0);
		rootGridPane.add(ticketTableView, 0, 0);
		rootGridPane.add(buttonFlowPane, 0, 1);
		rootGridPane.add(backButton, 1, 1);
		
		//Adds Columns to the ticket TableView
		TableColumn<Ticket, String> ticketIdColumn = new TableColumn<>("Κωδικός Κράτησης");
		ticketIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
		ticketTableView.getColumns().add(ticketIdColumn);
		
		TableColumn<Ticket, String> ticketDateTimeColumn = new TableColumn<>("Ημερομηνία Κράτησης");
		ticketDateTimeColumn.setCellValueFactory(cellData -> {
			Ticket ticket = cellData.getValue();
			//Gets the date of the event
			LocalDateTime dateTime = ticket.getScreening().getScreeningDate();
			//Creates a dd/MM HH:mm formatter
			DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM HH:mm");
			//Formats the date and stores it into a string variable
			String formattedDate = dateTime.format(format);
			return new SimpleStringProperty(formattedDate);
		});
		ticketTableView.getColumns().add(ticketDateTimeColumn);
		
		TableColumn<Ticket, String> ticketEventTypeColumn = new TableColumn<>("Τύπος Event");
		ticketEventTypeColumn.setCellValueFactory(cellData -> {
		return new SimpleStringProperty("Κανονική Προβολή");
		});
		ticketTableView.getColumns().add(ticketEventTypeColumn);
		
		TableColumn<Ticket, String> ticketScreeningColumn = new TableColumn<>("Προβολή");
		ticketScreeningColumn.setCellValueFactory(cellData -> {
			Ticket ticket = cellData.getValue();
			//Gets the movie's name
			String movieName = ticket.getScreening().getMovie().getName();
			return new SimpleStringProperty(movieName);
		});
		ticketTableView.getColumns().add(ticketScreeningColumn);
		
		//Adds Columns to the party TableView
		TableColumn<BirthdayParty, String> partyIdColumn = new TableColumn<>("Κωδικός Κράτησης");
		partyIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
		partyTableView.getColumns().add(partyIdColumn);
		
		TableColumn<BirthdayParty, String> partyDateTimeColumn = new TableColumn<>("Ημερομηνία Κράτησης");
		partyDateTimeColumn.setCellValueFactory(cellData -> {
			BirthdayParty party = cellData.getValue();
			//Gets the date of the event
			LocalDateTime dateTime = party.getScreeningDate();
			//Creates a dd/MM HH:mm formatter
			DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM HH:mm");
			//Formats the date and stores it into a string variable
			String formattedDate = dateTime.format(format);
			return new SimpleStringProperty(formattedDate);
		});
		partyTableView.getColumns().add(partyDateTimeColumn);
		
		TableColumn<BirthdayParty, String> partyEventTypeColumn = new TableColumn<>("Τύπος Event");
		partyEventTypeColumn.setCellValueFactory(cellData -> {
		return new SimpleStringProperty("Παιδικό Πάρτι");
		});
		partyTableView.getColumns().add(partyEventTypeColumn);
		
		TableColumn<BirthdayParty, String> partyScreeningColumn = new TableColumn<>("Προβολή");
		partyScreeningColumn.setCellValueFactory(cellData -> {
			BirthdayParty party = cellData.getValue();
			//Gets the movie's name
			String movieName = party.getMovie().getName();
			return new SimpleStringProperty(movieName);
		});
		partyTableView.getColumns().add(partyScreeningColumn);
		
	}
	
	//Setters and Getters
	public void setSelectedEventType(String selectedEventType) {
		this.selectedEventType = selectedEventType;
	}
	public String getSelectedEventType() {
		return this.selectedEventType;
	}
	
	public void setSelectedMovie(String selectedMovie) {
		this.selectedMovie = selectedMovie;
	}
	public String getSelectedMovie() {
		return this.selectedMovie;
	}
	
	@Override
	public Scene createScene() {
		return new Scene(rootGridPane, width, height);
	}
	
	/*This method sets the available movies depending on the event type*/
	public void setAvailableMovies() {
		//Clears the movie selection ComboBox
		movieSelectionBox.getItems().clear();
		
		//Checks if the event type is null
		if (getSelectedEventType() != null) {
			if (getSelectedEventType().equals("Κανονική Προβολή")) {
				//Fills the movie selection ComboBox
				for (int i = 0; i < App.movies.length; i++) {
					movieSelectionBox.getItems().add(App.movies[i].getName());
				}
			}
			else {
				//Fills the movie selection ComboBox with the different kids movies names 
				for (int i = 0; i < 4; i++) {
					movieSelectionBox.getItems().add(App.movies[i].getName());
				}
			}
		}
	}
	
	/*This method syncs the ticket TableView*/
	public void ticketTableSync() {
		//Gets the list of the TableView
		List<Ticket> items = ticketTableView.getItems();
		//Clears the List
		items.clear();
		//Adds the ticket items of the ArrayList to the TableView 
		for (Ticket t: ticketHandlingSceneCreator.ticketList) {
			//Checks if the movie of the ticket item is the selected one
			if (selectedMovie.equals(t.getScreening().getMovie().getName())) {
				items.add(t);
			}
		}
	}
	
	/*This method syncs the party TableView*/
	public void partyTableSync() {
		//Gets the list of the TableView
		List<BirthdayParty> items = partyTableView.getItems();
		//Clears the List
		items.clear();
		//Adds the party items of the ArrayList to the TableView
		for (BirthdayParty p: partyHandlingSceneCreator.partyList) {
			//Checks if the movie screened at the party is the selected one
			if (selectedMovie.equals(p.getMovie().getName())) {
				items.add(p);
			}
		}
	}
	
	/*This method clears the ComboBox selections*/
	public void clearFields() {
		//Clears the selections
		eventTypeSelectionBox.getSelectionModel().clearSelection();
		movieSelectionBox.getSelectionModel().clearSelection();
	
		//Sets the value of the ComboBoxes to null
		eventTypeSelectionBox.setValue(null);
		movieSelectionBox.setValue(null);

	}
	
	@Override
	public void handle(ActionEvent event) {
		//Gets the values of the selections
		selectedEventType = eventTypeSelectionBox.getValue();
		selectedMovie = movieSelectionBox.getValue();
		
		//Checks if the user has interacted with the event type ComboBox
		if (event.getSource() == eventTypeSelectionBox) {
			//Sets the available movies
			setAvailableMovies();	
		}
		
		//Checks if the user has interacted with the search button
		if (event.getSource() == searchButton) {
			//Checks if all the selections are filled
			if ((selectedEventType != null) && (selectedMovie != null)) {
	
				//Removes the TableViews from the GridPane
				rootGridPane.getChildren().remove(ticketTableView);
				rootGridPane.getChildren().remove(partyTableView);
				
				//Checks which event type has been selected
				if (getSelectedEventType().equals("Κανονική Προβολή")) {
					//Adds the ticketTableView to the GridPane and syncs the ticket TableView
					rootGridPane.add(ticketTableView, 0, 0);
					ticketTableSync();
				}
				else {
					//Adds the partyTableView to the GridPane and syncs the party TableView
					rootGridPane.add(partyTableView, 0, 0);
					partyTableSync();
				}
			}
			else {
				//Shows an error message asking to fill all the selections
				Alert searchFailed = new Alert(Alert.AlertType.ERROR);
				searchFailed.setTitle("Αδυναμία Αναζήτησης");
				searchFailed.setContentText("Δεν ήταν δυνατή η αναζήτηση καθώς δεν έχουν συμπληρωθεί όλα τα πεδία. Παρακαλώ συμπληρώστε τα και προσπαθήστε ξανά");
				searchFailed.show();
			}
		}
		
		//Checks if the user has interacted with the back button
		if (event.getSource() == backButton) {			
			//Clears the TableViews
			ticketTableView.getItems().clear();
			partyTableView.getItems().clear();
			
			//Clears the fields
			clearFields();
			
			App.stage.setTitle("Java Cinema");
			App.stage.setScene(App.mainScene);
		}
	}

}
