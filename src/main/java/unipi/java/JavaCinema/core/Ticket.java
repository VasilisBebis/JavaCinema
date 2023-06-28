package unipi.java.JavaCinema.core;

import java.time.LocalDateTime;

/*This class represents a ticket for a screening in the movie theater*/
public class Ticket implements PriceList {

	//Fields
	private String id;
	private RegularShow screening;
	private LocalDateTime dateOfIssuance;
	private String ticketType;
	private double price;
	
	//Constructor
	
	public Ticket(String id, RegularShow screening, LocalDateTime dateOfIssuance, String ticketType) {
		this.id = id;
		this.screening = screening;
		this.dateOfIssuance = dateOfIssuance;
		this.ticketType = ticketType;
	}
	
	public Ticket(String id, RegularShow screening, LocalDateTime dateOfIssuance, String ticketType, double price) {
		this.id = id;
		this.screening = screening;
		this.dateOfIssuance = dateOfIssuance;
		this.ticketType = ticketType;
		this.price = price;
	}
	
	//Setters & Getters
	public void setId(String id) {
		this.id = id;
	}
	public String getId() {
		return this.id;
	}
	
	public void setScreening(RegularShow screening) {
		this.screening = screening;
	}
	public RegularShow getScreening() {
		return this.screening;
	}
	
	public void setDateOfIssuance(LocalDateTime dateOfIssuance) {
		this.dateOfIssuance = dateOfIssuance;
	}
	public LocalDateTime getDateOfIssuance() {
		return this.dateOfIssuance;
	}
	
	public void setTicketType(String ticketType) {
		this.ticketType = ticketType;
	}
	public String getTicketType() {
		return this.ticketType;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}
	public double getPrice() {
		return this.price;
	}
	
	//Method that returns the total price of the ticket
	public double calculateTotalPrice() {
		/* ticketType getter is used to get the type of the ticket
		 * if ticket type is regular then
		 * 		return the price of the regular ticket using the regularTicketPrice field of the PriceList interface
		 * else if ticket type is kid then
		 * 		return the price of the kid ticket using the kidTicketPrice field of the PriceList interface
		 * else (the ticket is student) then
		 * 		return the price of the student ticket using the studentTicketPrice field of the PriceList interface
		 */
		if (getTicketType().equals("Regular")) {
			return regularTicketPrice;
		}
		else if (getTicketType().equals("Kid")) {
			return kidTicketPrice;
		}
		else {
			return studentTicketPrice;
		}
	}
}
