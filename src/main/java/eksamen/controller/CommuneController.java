package eksamen.controller;

import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eksamen.model.Commune;
import eksamen.repository.CommuneRepository;

@RestController
@RequestMapping("/kommune")
public class CommuneController {

    private CommuneRepository communeRepository;
    private ObjectMapper objectMapper = new ObjectMapper();
    
    public CommuneController(CommuneRepository communeRepository){
        this.communeRepository = communeRepository;
    }
    

    @GetMapping(produces = "application/json")
    public ResponseEntity<String> findAll() throws JsonProcessingException{
        Iterable<Commune> communes = communeRepository.findAll();
        return ResponseEntity.ok(objectMapper.writeValueAsString(communes));
    }



}
