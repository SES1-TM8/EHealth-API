package xyz.jonmclean.EHealth.test;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PeopleService {
	
	@Autowired
	public PeopleRepository peopleRepo;
	
	@PostMapping("/people/add")
	@ResponseBody
	public People add(@RequestParam("name") String name, @RequestParam("dob") long timestamp, @RequestParam("favouriteColour") String favouriteColour) {
		People people = new People();
		people.setName(name);
		people.setDob(new Date(timestamp));
		people.setFavouriteColour(favouriteColour);
		
		peopleRepo.save(people);
		
		return people;
	}
	
	
	
}
