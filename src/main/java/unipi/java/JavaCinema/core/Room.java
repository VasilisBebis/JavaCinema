package unipi.java.JavaCinema.core;

/*This class represents a room of the movie theater*/
public class Room {

	//Fields
	private String id;
	private String description;
	private int noOfRows;
	private int noOfColumns;
	
	//Constructor
	
	public Room(String id, String description) {
		this.id = id;
		this.description = description;
		this.noOfRows = 10;
		this.noOfColumns = 15;
	}
	
	public Room(String id, String description, int noOfRows, int noOfColumns) {
		this.id = id;
		this.description = description;
		this.noOfRows = noOfRows;
		this.noOfColumns = noOfColumns;
	}
	
	//Setters & Getters
	public void setId(String id) {
		this.id = id;
	}
	public String getId() {
		return this.id;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDescription() {
		return this.description;
	}
	
	public void setNoOfRows(int noOfRows) {
		this.noOfRows = noOfRows;
	}
	public int getNoOfRows() {
		return this.noOfRows;
	}
	
	public void setNoOfColumns(int noOfColumns) {
		this.noOfColumns = noOfColumns;
	}
	public int getNoOfColumns() {
		return this.noOfColumns;
	}
	
	
}
