package andres.art_connect.service;

import andres.art_connect.domain.Compania;
import andres.art_connect.domain.Usuario;
import andres.art_connect.model.CompaniaDTO;
import andres.art_connect.repos.CompaniaRepository;
import andres.art_connect.repos.UsuarioRepository;
import andres.art_connect.util.NotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class CompaniaService {

    private final CompaniaRepository companiaRepository;
    private final UsuarioRepository usuarioRepository;

    public CompaniaService(final CompaniaRepository companiaRepository,
            final UsuarioRepository usuarioRepository) {
        this.companiaRepository = companiaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<CompaniaDTO> findAll() {
        final List<Compania> companias = companiaRepository.findAll(Sort.by("id"));
        return companias.stream()
                .map(compania -> mapToDTO(compania, new CompaniaDTO()))
                .toList();
    }

    public CompaniaDTO get(final Long id) {
        return companiaRepository.findById(id)
                .map(compania -> mapToDTO(compania, new CompaniaDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final CompaniaDTO companiaDTO) {
        final Compania compania = new Compania();
        mapToEntity(companiaDTO, compania);
        return companiaRepository.save(compania).getId();
    }

    public void update(final Long id, final CompaniaDTO companiaDTO) {
        final Compania compania = companiaRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(companiaDTO, compania);
        companiaRepository.save(compania);
    }

    public void delete(final Long id) {
        companiaRepository.deleteById(id);
    }
    
    public CompaniaDTO getCompaniaByCreatorId(Long userId) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(userId);
        if (usuarioOptional.isEmpty()) {
            throw new NotFoundException("Usuario not found for id: " + userId);
        }
        Usuario usuario = usuarioOptional.get();

        Compania compania = companiaRepository.findFirstByIdCreador(usuario);
        if (compania == null) {
            throw new NotFoundException("Compania not found for creator id: " + userId);
        }
        return mapToDTO(compania, new CompaniaDTO());
    }

    private CompaniaDTO mapToDTO(Compania compania, CompaniaDTO companiaDTO) {
        companiaDTO.setId(compania.getId());
        companiaDTO.setNombre(compania.getNombre());
        companiaDTO.setDescripcion(compania.getDescripcion());
        companiaDTO.setFechaCreacion(compania.getFechaCreacion());
        companiaDTO.setMiembros(compania.getMiembros());
        companiaDTO.setFotoPerfil(compania.getFotoPerfil());
        companiaDTO.setIdCreador(compania.getIdCreador() == null ? null : compania.getIdCreador().getId());
        return companiaDTO;
    }

    private Compania mapToEntity(final CompaniaDTO companiaDTO, final Compania compania) {
        compania.setNombre(companiaDTO.getNombre());
        compania.setDescripcion(companiaDTO.getDescripcion());
        compania.setFechaCreacion(companiaDTO.getFechaCreacion());
        compania.setMiembros(companiaDTO.getMiembros());
        compania.setFotoPerfil(companiaDTO.getFotoPerfil());
        final Usuario idCreador = companiaDTO.getIdCreador() == null ? null : usuarioRepository.findById(companiaDTO.getIdCreador())
                .orElseThrow(() -> new NotFoundException("idCreador not found"));
        compania.setIdCreador(idCreador);
        return compania;
    }

    public boolean nombreExists(final String nombre) {
        return companiaRepository.existsByNombreIgnoreCase(nombre);
    }
}
