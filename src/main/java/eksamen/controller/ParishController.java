package eksamen.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import eksamen.model.Commune;
import eksamen.model.Parish;
import eksamen.repository.CommuneRepository;
import eksamen.repository.ParishRepository;

@RestController
@RequestMapping("/sogn")
public class ParishController {

    private ParishRepository parishRepository;
    private CommuneRepository communeRepository;
    private ObjectMapper objectMapper = new ObjectMapper();
    
    public ParishController(ParishRepository parishRepository, CommuneRepository communeRepository){
        this.parishRepository = parishRepository;
        this.communeRepository = communeRepository;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
        MethodArgumentNotValidException ex) {
            Map<String, String> errors = new HashMap<>();
            ex.getBindingResult().getAllErrors().forEach((error) -> {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();
                errors.put(fieldName, errorMessage);
        });
        return errors;
    }
    @PostMapping(consumes="application/json", produces="application/json")
    public ResponseEntity<String> create(@RequestBody Parish parish) throws JsonProcessingException {
        Optional<Commune> optinalCommune = communeRepository.findById(parish.getCommune().getCommuneCode());
        if(!optinalCommune.isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unkown Commune");
        }
        parishRepository.save(parish);
        String response = objectMapper.writeValueAsString(parish);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }



}
