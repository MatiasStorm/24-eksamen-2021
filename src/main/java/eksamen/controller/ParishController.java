package eksamen.controller;

import java.util.Optional;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @GetMapping(produces = "application/json")
    public ResponseEntity<String> findAll() throws JsonProcessingException{
        Iterable<Parish> parishes = parishRepository.findAll();
        return ResponseEntity.ok(objectMapper.writeValueAsString(parishes));
    }

    @GetMapping(path="/{parishCode}", produces = "application/json")
    public ResponseEntity<String> findById(@PathVariable int parishCode) throws JsonProcessingException{
        Optional<Parish> optionalParish = parishRepository.findById(parishCode);
        if(!optionalParish.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Could not find parish");
        }
        return ResponseEntity.ok(objectMapper.writeValueAsString(optionalParish.get()));
    }

    @PutMapping(path="/{parishCode}", produces = "application/json", consumes="application/json")
    public ResponseEntity<String> update(@PathVariable int parishCode, @RequestBody Parish parish) throws JsonProcessingException{
        Optional<Commune> optinalCommune = communeRepository.findById(parish.getCommune().getCommuneCode());
        if(!optinalCommune.isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unkown Commune");
        }
        parishRepository.save(parish);
        String response = objectMapper.writeValueAsString(parish);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping(path="/{parishCode}", produces = "application/json")
    public ResponseEntity<String> deleteById(@PathVariable Integer parishCode){
        Optional<Parish> optionalParish = parishRepository.findById(parishCode);
        if(!optionalParish.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parish doesn't exists.");
        }
        parishRepository.deleteById(parishCode);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Successfully deleted");
    }
}









