package xyz.jonmclean.EHealth.models.exceptions.patient;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class PatientMedicareNumberInUseException extends Exception {

}
