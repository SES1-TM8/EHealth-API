package xyz.jonmclean.EHealth.test;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.jonmclean.EHealth.models.exceptions.UserNotFoundException;

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
	
	@GetMapping("/people/all")
	@ResponseBody
	public List<People> getAllPeople() {
		Iterable<People> iterablePeople = peopleRepo.findAll();
		List<People> people = new ArrayList<People>();
		iterablePeople.forEach(people::add);
		return people;
	}
	
	@GetMapping("/people/get/{id}")
	@ResponseBody
	public People getPerson(@PathVariable("id") long personId) throws UserNotFoundException {
		Optional<People> optionalPerson = peopleRepo.findById(personId);
		
		if(!optionalPerson.isPresent()) {
			throw new UserNotFoundException();
		}
		
		People people = optionalPerson.get();
		return people;
	}
	
}
