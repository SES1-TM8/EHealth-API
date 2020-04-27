package xyz.jonmclean.EHealth.test.people;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PeopleService {
	
	@Autowired
	public PeopleRepository peopleRepository;
	
	@PostMapping("/people/add")
	@ResponseBody
	public People addPerson(@RequestParam("name") String name, @RequestParam("dob") long dob, @RequestParam("colour") String colour) {
		People people = new People(name, new Date(dob), colour);
		peopleRepository.save(people);
		return people;
	}
	
	@GetMapping("/people")
	@ResponseBody
	public List<People> getPeople() {
		Iterable<People> iterablePeople = peopleRepository.findAll();
		List<People> people = new ArrayList<People>();
		iterablePeople.forEach(people::add);
		return people;
	}
	
}
