package andres.art_connect.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Publicacion {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String contenido;

    @Column
    private LocalDateTime fechaPublicacion;

    @Column
    private Boolean meGusta;

    @Column
    private Long numMeGustas;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_id")
    private Usuario idUsuario;
    
    @Column
    private Long idCompania;

    @OneToMany(mappedBy = "idPublicacion", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comentario> comentarios;

}
