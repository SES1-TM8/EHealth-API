package xyz.jonmclean.EHealth.test;

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
public class MovieService {
	
	@Autowired
	MovieRepository movieRepository;
	
	@Autowired
	ActorRepository actorRepository;
	
	@PostMapping("/actor/create")
	@ResponseBody
	public void addActor(@RequestParam("name") String name, @RequestParam("birthdate") Long dob) {
		Actor actor = new Actor(name, new Date(dob));
		actorRepository.save(actor);
	}
	
	@PostMapping("/movie/create")
	@ResponseBody
	public void addMovie(@RequestParam("name") String name, @RequestParam("actors") List<Long> ids) {
		Iterable<Actor> iterableActors = actorRepository.findAllById(ids);
		List<Actor> actors = new ArrayList<Actor>();
		iterableActors.forEach(actors::add);
		
		Movie movie = new Movie(name);
		movie.setActors(actors);
		movieRepository.save(movie);
	}
	
	@GetMapping("/movies")
	@ResponseBody
	public List<Movie> getMovies() {
		Iterable<Movie> iterableMovies = movieRepository.findAll();
		List<Movie> movies = new ArrayList<Movie>();
		iterableMovies.forEach(movies::add);
		return movies;
	}
	
	@GetMapping("/actors")
	@ResponseBody
	public List<Actor> getActors() {
		Iterable<Actor> iterableActors = actorRepository.findAll();
		List<Actor> actors = new ArrayList<Actor>();
		iterableActors.forEach(actors::add);
		return actors;
	}
	
	
	
	
}
