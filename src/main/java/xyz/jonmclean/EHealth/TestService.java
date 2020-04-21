package xyz.jonmclean.EHealth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestService {
	
	@GetMapping("/hello/{name}")
	@ResponseBody
	public String hello(@PathVariable("name") String name) {
		return "Hello " + name;
	}
	
	@GetMapping("/pong")
	@ResponseBody
	public PongResponse pong() {
		return new PongResponse();
	}
	
}
