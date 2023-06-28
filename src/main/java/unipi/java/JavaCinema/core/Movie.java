package unipi.java.JavaCinema.core;

/*This class represents a movie that is being played in the movie theater*/
public class Movie {

	//Fields 
	private String id;
	private String name;
	private int yearOfProduction;
	private String actors;
	private int runningTime;
	
	//Constructor
	public Movie(String id, String name, int yearOfProduction, String actors, int runningTime) {
		this.id = id;
		this.name = name;
		this.yearOfProduction = yearOfProduction;
		this.actors = actors;
		this.runningTime = runningTime;
	}
	
	//Setters & Getters
	public void setId(String id) {
		this.id = id;
	}
	public String getId() {
		return this.id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return this.name;
	}
	
	public void setYearOfProduction(int yearOfProduction) {
		this.yearOfProduction = yearOfProduction;
	}
	public int getYearOfProduction() {
		return this.yearOfProduction;
	}
	
	public void setActors(String actors) {
		this.actors = actors;
	}
	public String getActors() {
		return this.actors;
	}
	
	public void setRunningTime(int runningTime) {
		this.runningTime = runningTime;
	}
	public int getRunningTime() {
		return this.runningTime;
	}
}
