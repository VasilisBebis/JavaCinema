package unipi.java.JavaCinema.core;

/*This interface contains price related fields and a method, which are used in Ticket class to calculate the total cost of the ticket
 * and in BirthdayParty class to calculate the total cost of the birthday party*/
public interface PriceList {

	int regularTicketPrice = 8;
	int kidTicketPrice = 6;
	int studentTicketPrice = 6;
	
	int occupationPrice = 120;
	int menuPricePerPerson = 6;
	int snacksPricePerPerson = 3;
	int birthdayCakePrice = 70;
	
	double calculateTotalPrice();
}
