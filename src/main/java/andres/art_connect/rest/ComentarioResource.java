package andres.art_connect.rest;

import andres.art_connect.model.ComentarioDTO;
import andres.art_connect.service.ComentarioService;
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
@RequestMapping(value = "/api/comentarios", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin("*")
public class ComentarioResource {

    private final ComentarioService comentarioService;

    public ComentarioResource(final ComentarioService comentarioService) {
        this.comentarioService = comentarioService;
    }

    @GetMapping
    public ResponseEntity<List<ComentarioDTO>> getAllComentarios() {
        return ResponseEntity.ok(comentarioService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComentarioDTO> getComentario(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(comentarioService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createComentario(
            @RequestBody @Valid final ComentarioDTO comentarioDTO) {
        final Long createdId = comentarioService.create(comentarioDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Long> updateComentario(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final ComentarioDTO comentarioDTO) {
        comentarioService.update(id, comentarioDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/delete/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteComentario(@PathVariable(name = "id") final Long id) {
        comentarioService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/publicacion/{idPublicacion}")
    public ResponseEntity<List<ComentarioDTO>> getAllComentariosByPublicacionId(@PathVariable(name = "idPublicacion") final Long idPublicacion) {
        return ResponseEntity.ok(comentarioService.findAllByPublicacionId(idPublicacion));
    }

}
