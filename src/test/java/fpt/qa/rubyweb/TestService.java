package fpt.qa.rubyweb;

import java.util.Date;
import com.fpt.ruby.model.MovieTicket;
import com.fpt.ruby.service.mongo.MovieTicketService;

public class TestService {
	//@Autowired 
	
	public static void main(String[] args) {
		
		MovieTicketService movieTicketService = new MovieTicketService();
		MovieTicket movieTicket = new MovieTicket();
		movieTicket.setCinema("Lotte Cinema Landmark");
		movieTicket.setMovie("Lucy");
		Date date1 = new Date();
		
		date1.setHours(20);
		
		date1.setMinutes(0);
		date1.setSeconds(0);
		movieTicket.setDate(date1);

		movieTicket.setCity("Ha Noi");
		
		movieTicket.setType("2D");
		movieTicketService.delete(movieTicket);
		
		
		
		
		System.out.println(movieTicketService.existedInDb(movieTicket));
		//movieTicketService.save(movieTicket);
		System.out.println(movieTicketService.existedInDb(movieTicket));
		
		
		
	}
}
