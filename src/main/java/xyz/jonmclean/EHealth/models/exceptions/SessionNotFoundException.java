package xyz.jonmclean.EHealth.models.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Session does not exist")
public class SessionNotFoundException extends Exception {
}
