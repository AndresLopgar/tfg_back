package andres.art_connect.service;

import andres.art_connect.domain.*;
import andres.art_connect.model.UsuarioDTO;
import andres.art_connect.repos.*;
import andres.art_connect.util.NotFoundException;
import andres.art_connect.util.ReferencedWarning;

import java.io.IOException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PublicacionRepository publicacionRepository;
    private final CompaniaRepository companiaRepository;
    private final ComentarioRepository comentarioRepository;
    private final AmistadRepository amistadRepository;

    public UsuarioService(final UsuarioRepository usuarioRepository,
                          final PublicacionRepository publicacionRepository,
                          final CompaniaRepository companiaRepository,
                          final ComentarioRepository comentarioRepository,
                          final AmistadRepository amistadRepository) {
        this.usuarioRepository = usuarioRepository;
        this.publicacionRepository = publicacionRepository;
        this.companiaRepository = companiaRepository;
        this.comentarioRepository = comentarioRepository;
        this.amistadRepository = amistadRepository;
    }

    public List<UsuarioDTO> findAll() {
        final List<Usuario> usuarios = usuarioRepository.findAll(Sort.by("id"));
        return usuarios.stream()
                .map(usuario -> mapToDTO(usuario, new UsuarioDTO()))
                .toList();
    }

    public UsuarioDTO get(final Long id) {
        return usuarioRepository.findById(id)
                .map(usuario -> mapToDTO(usuario, new UsuarioDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final UsuarioDTO usuarioDTO){
        final Usuario usuario = new Usuario();
        mapToEntity(usuarioDTO, usuario);
        return usuarioRepository.save(usuario).getId();
    }

    public void update(final Long id, final UsuarioDTO usuarioDTO){
        final Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(usuarioDTO, usuario);
        usuarioRepository.save(usuario);
    }

    public void delete(final Long id) {
        usuarioRepository.deleteById(id);
    }

    private UsuarioDTO mapToDTO(final Usuario usuario, final UsuarioDTO usuarioDTO) {
        usuarioDTO.setId(usuario.getId());
        usuarioDTO.setNombre(usuario.getNombre());
        usuarioDTO.setContrasena(usuario.getContrasena());
        usuarioDTO.setCorreoElectronico(usuario.getCorreoElectronico());
        usuarioDTO.setEdad(usuario.getEdad());
        usuarioDTO.setProfesion(usuario.getProfesion());
        usuarioDTO.setFechaRegistro(usuario.getFechaRegistro());
        usuarioDTO.setFotoPerfil(usuario.getFotoPerfil());
        usuarioDTO.setTipoUsuario(usuario.getTipoUsuario());
        usuarioDTO.setCompaniaSeguida(usuario.getCompaniaSeguida());
        usuarioDTO.setPublicacionesLiked(usuario.getPublicacionesLiked() != null ? usuario.getPublicacionesLiked() : new int[0]);
        return usuarioDTO;
    }

    private void mapToEntity(final UsuarioDTO usuarioDTO, final Usuario usuario) {
        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setContrasena(usuarioDTO.getContrasena());
        usuario.setCorreoElectronico(usuarioDTO.getCorreoElectronico());
        usuario.setEdad(usuarioDTO.getEdad());
        usuario.setProfesion(usuarioDTO.getProfesion());
        usuario.setFechaRegistro(usuarioDTO.getFechaRegistro());
        usuario.setFotoPerfil(usuarioDTO.getFotoPerfil());
        usuario.setTipoUsuario(usuarioDTO.getTipoUsuario());
        usuario.setCompaniaSeguida(usuarioDTO.getCompaniaSeguida());
        usuario.setPublicacionesLiked(usuarioDTO.getPublicacionesLiked() != null ? usuarioDTO.getPublicacionesLiked() : new int[0]);
    }



    public boolean nombreExists(final String nombre) {
        return usuarioRepository.existsByNombreIgnoreCase(nombre);
    }

    public boolean contrasenaExists(final String contrasena) {
        return usuarioRepository.existsByContrasenaIgnoreCase(contrasena);
    }

    public boolean correoElectronicoExists(final String correoElectronico) {
        return usuarioRepository.existsByCorreoElectronicoIgnoreCase(correoElectronico);
    }

    public void updateCompaniaSeguida(Long userId, Long nuevaCompaniaSeguida) {
        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + userId));

        usuario.setCompaniaSeguida(nuevaCompaniaSeguida);
        usuarioRepository.save(usuario);
    }
    
    public int[] findPublicacionesLikedByUserId(Long userId) {
        return usuarioRepository.findPublicacionesLikedByUserId(userId);
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Publicacion idUsuarioPublicacion = publicacionRepository.findFirstByIdUsuario(usuario);
        if (idUsuarioPublicacion != null) {
            referencedWarning.setKey("usuario.publicacion.idUsuario.referenced");
            referencedWarning.addParam(idUsuarioPublicacion.getId());
            return referencedWarning;
        }
        final Compania idCreadorCompania = companiaRepository.findFirstByIdCreador(usuario);
        if (idCreadorCompania != null) {
            referencedWarning.setKey("usuario.compania.idCreador.referenced");
            referencedWarning.addParam(idCreadorCompania.getId());
            return referencedWarning;
        }
        final Comentario idUsuarioComentario = comentarioRepository.findFirstByIdUsuario(usuario);
        if (idUsuarioComentario != null) {
            referencedWarning.setKey("usuario.comentario.idUsuario.referenced");
            referencedWarning.addParam(idUsuarioComentario.getId());
            return referencedWarning;
        }
        final Amistad idSeguidorAmistad = amistadRepository.findFirstByIdSeguidor(usuario);
        if (idSeguidorAmistad != null) {
            referencedWarning.setKey("usuario.amistad.idSeguidor.referenced");
            referencedWarning.addParam(idSeguidorAmistad.getId());
            return referencedWarning;
        }
        final Amistad idSeguidoAmistad = amistadRepository.findFirstByIdSeguido(usuario);
        if (idSeguidoAmistad != null) {
            referencedWarning.setKey("usuario.amistad.idSeguido.referenced");
            referencedWarning.addParam(idSeguidoAmistad.getId());
            return referencedWarning;
        }
        return null;
    }
}
