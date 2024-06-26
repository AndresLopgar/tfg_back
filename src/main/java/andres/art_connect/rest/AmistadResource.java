package andres.art_connect.rest;

import andres.art_connect.model.AmistadDTO;
import andres.art_connect.service.AmistadService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/amistades", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin("*")
public class AmistadResource {

    private final AmistadService amistadService;

    public AmistadResource(final AmistadService amistadService) {
        this.amistadService = amistadService;
    }

    @GetMapping
    public ResponseEntity<List<AmistadDTO>> getAllAmistades() {
        return ResponseEntity.ok(amistadService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AmistadDTO> getAmistad(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(amistadService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createAmistad(@RequestBody @Valid final AmistadDTO amistadDTO) {
        final Long createdId = amistadService.create(amistadDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Long> updateAmistad(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final AmistadDTO amistadDTO) {
        amistadService.update(id, amistadDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/delete/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteAmistad(@PathVariable(name = "id") final Long id) {
        amistadService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/seguidor/{idSeguidor}")
    public ResponseEntity<List<AmistadDTO>> getAmistadesBySeguidor(@PathVariable(name = "idSeguidor") final Long idSeguidor) {
        List<AmistadDTO> amistades = amistadService.findBySeguidor(idSeguidor);
        return ResponseEntity.ok(amistades);
    }

    @GetMapping("/seguido/{idSeguido}")
    public ResponseEntity<List<AmistadDTO>> getAmistadesBySeguido(@PathVariable(name = "idSeguido") final Long idSeguido) {
        List<AmistadDTO> amistades = amistadService.findBySeguido(idSeguido);
        return ResponseEntity.ok(amistades);
    }
}
