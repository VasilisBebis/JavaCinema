package unipi.java.JavaCinema.core;

import java.time.LocalDateTime;

/*This class represents a birthday party event in the movie theater*/
public class BirthdayParty extends Event implements PriceList {

	//Fields
	private String id;
	private LocalDateTime entryDate;
	private int noOfGuests;
	private String menuType;
	private boolean hasBirtdayCake;
	private double price;
	
	//Constructor
	public BirthdayParty(String id, Movie movie, LocalDateTime screeningDate, Room room, LocalDateTime entryDate, int noOfGuests, String menuType, boolean hasBirthdayCake) {
		super(movie, screeningDate, room); //using the constructor of the superclass Event
		this.id = id;
		this.entryDate = entryDate;
		this.noOfGuests = noOfGuests;
		this.menuType = menuType;
		this.hasBirtdayCake = hasBirthdayCake;
	}
	
	public BirthdayParty(String id, Movie movie, LocalDateTime screeningDate, Room room, LocalDateTime entryDate, int noOfGuests, String menuType, boolean hasBirthdayCake, double price) {
		super(movie, screeningDate, room); //using the constructor of the superclass Event
		this.id = id;
		this.entryDate = entryDate;
		this.noOfGuests = noOfGuests;
		this.menuType = menuType;
		this.hasBirtdayCake = hasBirthdayCake;
		this.price = price;
	}
	
	//Setters & Getters
	public void setId(String id) {
		this.id = id;
	}
	public String getId() {
		return this.id;
	}
	
	public void setEntryDate(LocalDateTime entryDate) {
		this.entryDate = entryDate;
	}
	public LocalDateTime getEntryDate() {
		return this.entryDate;
	}
	
	public void setNoOfGuests(int noOfGuests) {
		this.noOfGuests = noOfGuests;
	}
	public int getNoOfGuests() {
		return this.noOfGuests;
	}
	
	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}
	public String getMenuType() {
		return this.menuType;
	}
	
	public void setHasBirthdayCake(boolean hasBirthdayCake) {
		this.hasBirtdayCake = hasBirthdayCake;
	}
	public boolean getHasBirthdayCake() {
		return this.hasBirtdayCake;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}
	public double getPrice() {
		return this.price;
	}
	
	//Method that returns the total
	public double calculateTotalPrice() {
		double totalPrice;
		totalPrice = occupationPrice; //initialization of the totalPrice variable with the price for reserving the room for the birthday party (usage of the occupationPrice field of the PriceList interface)
		/*menuType getter is used to get the type of the menu
		 * if the menu is a full menu then
		 * 		multiply the amount of guests (using the noOfGuests getter) by the full menu price (menuPricePerPerson from PriceList) and add it to the total price
		 * else (the menu is only pop corn & soda)
		 * 		multiply the amount of guests (using the noOfGuests getter) by the snacks menu price (snacksPricePerPerson from PriceList) and add it to the total price
		 */
		if (getMenuType().equals("Full Menu")) {
			totalPrice += getNoOfGuests() * menuPricePerPerson;
		}
		else {
			totalPrice += getNoOfGuests() * snacksPricePerPerson;
		}
		/*Checks if the party has a birthday cake and if so, it adds the cake price to the total price*/
		if (getHasBirthdayCake()) {
			totalPrice += birthdayCakePrice; //birthdayCakePrice field from PriceList interface
		}
		return totalPrice; //return the calculated total price
	}
	
}
