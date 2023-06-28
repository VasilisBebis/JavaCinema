package unipi.java.JavaCinema.core;

import java.time.LocalDateTime;
import java.util.Arrays;

/*This class represents a regular screening of a movie in the movie theater*/
public class RegularShow extends Event {

	//Fields
	private String[][] seatOccupancy = new String[getRoom().getNoOfRows()][getRoom().getNoOfColumns()];
	private int totalSeats;
	private int takenSeats;
	
	//Constructor
	public RegularShow(Movie movie, LocalDateTime screeningDate, Room room) {
		super(movie, screeningDate, room);
		//Filling every row of the seatOccupancy array with "0"
		for (String[] row : seatOccupancy) {
			Arrays.fill(row, "0");
		}
		//Total seats is the product of rows and columns
		this.totalSeats = getRoom().getNoOfRows() * getRoom().getNoOfColumns();
		this.takenSeats = 0;
	}
	
	public RegularShow(Movie movie, LocalDateTime screeningDate, Room room, String[][] seatOccupancy, int totalSeats, int takenSeats) {
		super(movie, screeningDate, room); //using the constructor of the superclass Event
		this.seatOccupancy = seatOccupancy;
		this.totalSeats = totalSeats;
		this.takenSeats = takenSeats;
	}
	
	
	
	//Setters & Getters
	public void setSeatOccupancy(String[][] seatOccupancy) {
		this.seatOccupancy = seatOccupancy;
	}
	public String[][] getSeatOccupancy() {
		return this.seatOccupancy;
	}
	
	public void setTotalSeats(int totalSeats) {
		this.totalSeats = totalSeats;
	}
	public int getTotalSeats() {
		return this.totalSeats;
	}
	
	public void setTakenSeats(int takenSeats) {
		this.takenSeats = takenSeats;
	}
	public int getTakenSeats() {
		return this.takenSeats;
	}
	
	/*This method takes as an input the id of a ticket, the row and the column of the seat and changes the seatOccupancy array accordingly. It also
	 * increases the number of taken seats by 1 */
	public void occupySeat(String id, int row, int column) {
		seatOccupancy[row][column] = id;
		takenSeats++;
	}
	
	/*This method takes as an input a ticket's id and frees the seat associated with it*/
	public void freeSeat(String id) {
		
		//Iterates over the seat occupancy array
		for (int rows = 0; rows < getRoom().getNoOfRows(); rows++) {
			for (int columns = 0; columns < getRoom().getNoOfColumns(); columns++) {
				//Finds the seat associated with the ticket id
				if (seatOccupancy[rows][columns].equals(id)) {
					seatOccupancy[rows][columns] = "0"; //Sets to "0" meaning the seat is not occupied
					break;
				}
			}
		}
	}
}