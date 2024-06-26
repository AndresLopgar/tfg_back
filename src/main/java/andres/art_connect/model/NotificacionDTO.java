package andres.art_connect.model;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificacionDTO {

    private Long id;
    private String contenido;
    private String tipoNotificacion;
    private LocalDateTime fechaNotificacion;
    private Long idUsuarioEmisor;
    private Long idUsuarioRemitente;
}

