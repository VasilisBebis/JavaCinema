package unipi.java.JavaCinema.core;

import java.time.LocalDateTime;

/*This class represents an event taking place in the movie theater*/
public class Event {

	//Fields
	private Movie movie;
	private LocalDateTime screeningDate;
	private Room room;
	
	//Constructor
	public Event(Movie movie, LocalDateTime screeningDate, Room room) {
		this.movie = movie;
		this.screeningDate = screeningDate;
		this.room = room;
	}
	
	//Setters & Getters
	public void setMovie(Movie movie) {
		this.movie = movie;
	}
	public Movie getMovie() {
		return this.movie;
	}
	
	public void setScreeningDate(LocalDateTime screeningDate) {
		this.screeningDate = screeningDate;
	}
	public LocalDateTime getScreeningDate() {
		return this.screeningDate;
	}
	
	public void setRoom(Room room) {
		this.room = room;
	}
	public Room getRoom() {
		return this.room;
	}
	
}
