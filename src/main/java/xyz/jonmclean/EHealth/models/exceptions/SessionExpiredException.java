package xyz.jonmclean.EHealth.models.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "Session token has expired")
public class SessionExpiredException extends Exception {

}