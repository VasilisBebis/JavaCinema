package unipi.java.JavaCinema.gui;

import java.util.Random;
import java.time.LocalDateTime;
import java.time.YearMonth;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import unipi.java.JavaCinema.core.*;

/**
 * JavaFX App
 */
public class App extends Application {

	//Primary stage of the program
	static Stage stage;
	//Scenes of the program
	static Scene mainScene, ticketHandlingScene, seatSelectionScene, partyHandlingScene, searchScene;
	
	//Static Movie array that contains the different movies that are being played in the movie theater
	static Movie[] movies = new Movie[8];
	//Static Room array that contains the different rooms of the movie theater
	static Room[] rooms = new Room[10];
	//Static Event array that contains the different events that take place in the movie theater
	static Event[] events = new Event[210];
	
    @Override
    public void start(Stage stage) {

     	 this.stage = stage;
         
     	 //Initializes a SceneCreator object with the MainSceneCreator constructor and then uses the createScene method to create a scene
         SceneCreator mainSceneCreator = new MainSceneCreator(1100, 620);
         mainScene = mainSceneCreator.createScene();
         
         //Initializes a SceneCreator object with the TicketHandlingSceneCreator constructor and then uses the createScene method to create a scene
         SceneCreator ticketHandlingSceneCreator = new TicketHandlingSceneCreator(1100, 620);
         ticketHandlingScene = ticketHandlingSceneCreator.createScene();
         
         //Initializes a SceneCreator object with the SeatSelectionSceneCreator constructor and then uses the createScene method to create a scene
         SceneCreator seatSelectionSceneCreator = new SeatSelectionSceneCreator(1100, 620, ticketHandlingSceneCreator);
         seatSelectionScene = seatSelectionSceneCreator.createScene();
         
         //Linking the seatSelectionSceneCreator to the ticketHandlingSceneCreator
         ((TicketHandlingSceneCreator) ticketHandlingSceneCreator).setSeatSelectionSceneCreator(seatSelectionSceneCreator);
         
         //Initializes a SceneCreator object with the PartyHandlingSceneCreator constructor and then uses the createScene method to create a scene
         SceneCreator partyHandlingSceneCreator = new PartyHandlingSceneCreator(1100, 620);
         partyHandlingScene = partyHandlingSceneCreator.createScene();
         
         //Initializes a SceneCreator object with the SearchSceneCreator constructor and then uses the createScene method to create a scene
         SceneCreator searchSceneCreator = new SearchSceneCreator(1100, 620, ticketHandlingSceneCreator, partyHandlingSceneCreator);
         searchScene = searchSceneCreator.createScene();
         
         //Setting the scene to the main scene
         stage.setScene(mainScene);
         stage.setTitle("Java Cinema");
         stage.show();
    }

    public static void main(String[] args) {
    	
    	//Initializing the Movie array with different movies
        movies[0] = new Movie("1816266191998952", "The Little Mermaid", 2023, "Melissa McCarthy, Javier Bardem, Halle Bailey, Jonah Hauer-King, Jude Akuwudike, Noma Dumezweni, Kajsa Mohammar, Lorena Andrea", 135);
        movies[1] = new Movie("4266359359080299", "Spider-Man: Across the Spider-Verse", 2023, "Daniel Kaluuya, Shameik Moore, Hailee Steinfeld, Jake Johnson, Issa Rae, Brian Tyree Henry, Rachel Dratch, Oscar Isaac", 141);
        movies[2] = new Movie("7732662121971858", "The Super Mario Bros. Movie", 2023, "Chris Pratt, Anya Taylor-Joy, Charlie Day, Jack Black, Keegan-Michael Key, Seth Rogen, Fred Armisen", 93);
        movies[3] = new Movie("2072889402166157", "Elemental", 2023, "Leah Lewis, Mamoudou Athie, Shila Ommi, Catherine O'Hara, Ronnie Del Carmen, Wendi McLendon-Covey", 109);
        
        movies[4] = new Movie("3445576201318719", "Taxi Driver", 1976, "Robert De Niro, Jodie Foster, Cybill Shepherd, Harvey Keitel, Peter Boyle, Leonard Harris, Albert Brooks", 114);
        movies[5] = new Movie("2421203991669441", "Pulp Fiction", 1994, "John Travolta, Samuel L. Jackson, Bruce Willis, Tim Roth, Ving Rhames, Uma Thurman", 154);
        movies[6] = new Movie("1346781828268946", "GoodFellas", 1990, "Robert De Niro, Ray Liotta, Joe Pesci, Lorraine Bracco, Paul Sorvino", 146);
        movies[7] = new Movie("3110164664783021", "City of God", 2002, "Alexandre Rodrigues, Leandro Firmino, Phellipe Haagensen, Douglas Silva, Alice Braga, Seu Jorge", 130);

        //Initializing the Room array with different rooms
    	rooms[0] = new Room("R1", "Standard Room");
    	rooms[1] = new Room("R2", "Standard Room");
    	rooms[2] = new Room("R3", "Standard Room");
    	rooms[3] = new Room("R4", "Standard Room");
    	rooms[4] = new Room("R5", "IMAX Room");
    	rooms[5] = new Room("R6", "IMAX Room");
    	rooms[6] = new Room("R7", "IMAX Room");
    	rooms[7] = new Room("R8", "IMAX Room");
    	rooms[8] = new Room("R9", "Kids Room");
    	rooms[9] = new Room("R10", "Kids Room");
    	
    	/*Initializing the Event array with different events*/
    	
    	//Gets the current date in a LocalDateTime object and the current month of the year in a YearMonth object
    	LocalDateTime currentDateTime = LocalDateTime.now();
    	YearMonth currentMonth = YearMonth.of(currentDateTime.getYear(), currentDateTime.getMonth());

    	//Declares a variable that refers to LocalDateTime object. This object contains the date and time of a screening
    	LocalDateTime screeningDateTime;
    	
    	/*Initializes the screeningDateTime with the date and time of the first event*/
    	
    	//Checks if the time is before 5pm and sets the date to the current date and the time to 5pm
    	if (currentDateTime.getHour() < 17) {
    		screeningDateTime = LocalDateTime.of(currentDateTime.getYear(), currentDateTime.getMonthValue(), currentDateTime.getDayOfMonth(), 17, 0, 0);
    	}
    	//Checks if the time is between 5pm and 8pm and sets the date to the current date and the time to 8pm
    	else if ((currentDateTime.getHour() >= 17) && (currentDateTime.getHour() < 20)) {
    		screeningDateTime = LocalDateTime.of(currentDateTime.getYear(), currentDateTime.getMonthValue(), currentDateTime.getDayOfMonth(), 20, 0, 0);
    	}
    	//Checks if the time is between 8pm and 11pm and sets the date to the current date and the time to 11pm
    	else if ((currentDateTime.getHour() >= 20) && (currentDateTime.getHour() < 23)) {
    		screeningDateTime = LocalDateTime.of(currentDateTime.getYear(), currentDateTime.getMonthValue(), currentDateTime.getDayOfMonth(), 23, 0, 0);
    	}
    	else {
    		//Checks if the current day is the last day of the month. If its not it sets the date to the next day of the current date and the time to 5pm
    		if (currentDateTime.getDayOfMonth() != currentMonth.lengthOfMonth()) {
    			screeningDateTime = LocalDateTime.of(currentDateTime.getYear(), currentDateTime.getMonthValue(), currentDateTime.getDayOfMonth() + 1, 17, 0, 0);
    		}
    		//If it is the last day of the month
    		else {
    			//Checks if it is the last day of the year. If it is not it sets the date to the first day of the next month and the time to 5pm
    			if (currentDateTime.getDayOfYear() != currentMonth.lengthOfYear()) {
    				screeningDateTime = LocalDateTime.of(currentDateTime.getYear(), currentDateTime.getMonthValue() + 1, 1, 17, 0, 0);
    			}
    			//If it is the last day of the year it sets the year to the next year, the date to January 1st and the time to 5pm
    			else {
    				screeningDateTime = LocalDateTime.of(currentDateTime.getYear() + 1, 1, 1, 17, 0, 0);
    			}
    		}
    	}
    	
    	//Iterating over the Event array 	
    	for (int i = 0; i < events.length; i++) {
    		/*Checks if 10 events have passed. For every screening time (5pm - 8pm - 11pm) we want to add one event to every room (10 total rooms).
    		 * If 10 events have passed then we change the time to the next screening time*/
    		if (((i + 1) % 10) == 0) {
    			//Checks if the time is 11pm which means it is the last screening of the day. If it is not it adds 3 hours to the time so it goes to the next screening time
    			if(screeningDateTime.getHour() != 23) {
        			screeningDateTime = screeningDateTime.withHour(screeningDateTime.getHour() + 3);
        		}
    			//If it is 11pm (last screening of the day)
        		else {
        			//Checks if the day is the last day of the month. If not, it changes the screeningDateTime to the next day
        			if (screeningDateTime.getDayOfMonth() != currentMonth.lengthOfMonth()) {
        				screeningDateTime = screeningDateTime.withDayOfMonth(screeningDateTime.getDayOfMonth() + 1);
        			}
        			//If it is the last day of the month
        			else {
        				//Checks if it is the last day of the year. If not, it changes the date to the first day of the next month
        				if (screeningDateTime.getDayOfYear() != currentMonth.lengthOfYear()) {
        					screeningDateTime = screeningDateTime.withMonth(screeningDateTime.getMonthValue() + 1);
            				screeningDateTime = screeningDateTime.withDayOfMonth(1);
        				}
        				//If it is the last day of the year, it changes the date to January 1st of the next year
        				else {
        					screeningDateTime = screeningDateTime.withYear(screeningDateTime.getYear() + 1);
        					screeningDateTime = screeningDateTime.withMonth(1);
        					screeningDateTime = screeningDateTime.withDayOfMonth(1);
        				}
        			}
        			//Sets the time to 5pm
        			screeningDateTime = screeningDateTime.withHour(17);
        		}
    		}
    		/*Checks if the i modulo 10 is below 8. With this for every 10 events we get the number of the room. If the number of the room
    		 * is <8 (regular-imax room) it generates a random number between 0-7 and uses this number as an index in the movies array to
    		 * get a random movie*/
    		if ((i % 10) < 8){
    			Random randomNumber = new Random();
    			int randomMovie = randomNumber.nextInt(8);
    			events[i] = new RegularShow(movies[randomMovie], screeningDateTime, rooms[i % 10]);
    		}
    		/*If the number of the room is >8 (kids room) it generates a random number between 0-3 and uses this number as an index in the movies 
    		 *array to get a random movie (it gets only kids movies)*/
    		else {
    			Random randomNumber = new Random();
    			int randomKidsMovie = randomNumber.nextInt(4);
    			events[i] = new Event(movies[randomKidsMovie], screeningDateTime, rooms[i % 10]);
    		}
    	} 
    	launch();
    }

}
 