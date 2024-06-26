package andres.art_connect.rest;

import andres.art_connect.model.PublicacionDTO;
import andres.art_connect.service.PublicacionService;
import andres.art_connect.util.ReferencedException;
import andres.art_connect.util.ReferencedWarning;
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
@RequestMapping(value = "/api/publicaciones", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin("*")
public class PublicacionResource {

    private final PublicacionService publicacionService;

    public PublicacionResource(final PublicacionService publicacionService) {
        this.publicacionService = publicacionService;
    }

    @GetMapping
    public ResponseEntity<List<PublicacionDTO>> getAllPublicacions() {
        return ResponseEntity.ok(publicacionService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublicacionDTO> getPublicacion(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(publicacionService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createPublicacion(
            @RequestBody @Valid final PublicacionDTO publicacionDTO) {
        final Long createdId = publicacionService.create(publicacionDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Long> updatePublicacion(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final PublicacionDTO publicacionDTO) {
        publicacionService.update(id, publicacionDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/delete/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deletePublicacion(@PathVariable(name = "id") final Long id) {
        publicacionService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<PublicacionDTO>> getAllPublicacionesByIdUsuario(@PathVariable(name = "idUsuario") final Long idUsuario) {
        List<PublicacionDTO> publicaciones = publicacionService.getAllPublicacionesByIdUsuario(idUsuario);
        return ResponseEntity.ok(publicaciones);
    }

    @GetMapping("/compania/{idCompania}")
    public ResponseEntity<List<PublicacionDTO>> getAllPublicacionesByIdCompania(@PathVariable(name = "idCompania") final Long idCompania) {
        List<PublicacionDTO> publicaciones = publicacionService.getAllPublicacionesByIdCompania(idCompania);
        return ResponseEntity.ok(publicaciones);
    }


}
