package utez.edu.mx.unidad3.moduls.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utez.edu.mx.unidad3.moduls.events.dto.TypeRequestDto;
import utez.edu.mx.unidad3.utils.APIResponse;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TypeService {

    @Autowired
    private TypeRepository typeRepository;

    // Crear un nuevo tipo
    public APIResponse createType(TypeRequestDto typeRequestDto) {
        try {
            // Verificar que no exista un tipo con el mismo nombre
            if (typeRepository.existsByName(typeRequestDto.getName())) {
                return new APIResponse("Ya existe un tipo de evento con el nombre: " + typeRequestDto.getName(), true, HttpStatus.CONFLICT);
            }

            Type type = new Type(typeRequestDto.getName(), typeRequestDto.getDescription());
            Type savedType = typeRepository.save(type);

            return new APIResponse("Tipo de evento creado exitosamente", savedType, HttpStatus.CREATED);
        } catch (Exception e) {
            return new APIResponse("Error interno del servidor: " + e.getMessage(), true, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener todos los tipos
    public APIResponse getAllTypes() {
        try {
            List<Type> types = typeRepository.findAll();
            return new APIResponse("Tipos de eventos obtenidos exitosamente", types, HttpStatus.OK);
        } catch (Exception e) {
            return new APIResponse("Error interno del servidor: " + e.getMessage(), true, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener tipo por ID
    public APIResponse getTypeById(Long id) {
        try {
            Optional<Type> typeOpt = typeRepository.findById(id);
            if (typeOpt.isEmpty()) {
                return new APIResponse("Tipo de evento no encontrado", true, HttpStatus.NOT_FOUND);
            }

            return new APIResponse("Tipo de evento obtenido exitosamente", typeOpt.get(), HttpStatus.OK);
        } catch (Exception e) {
            return new APIResponse("Error interno del servidor: " + e.getMessage(), true, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener tipo por nombre
    public APIResponse getTypeByName(String name) {
        try {
            Optional<Type> typeOpt = typeRepository.findByName(name);
            if (typeOpt.isEmpty()) {
                return new APIResponse("Tipo de evento no encontrado con el nombre: " + name, true, HttpStatus.NOT_FOUND);
            }

            return new APIResponse("Tipo de evento obtenido exitosamente", typeOpt.get(), HttpStatus.OK);
        } catch (Exception e) {
            return new APIResponse("Error interno del servidor: " + e.getMessage(), true, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Actualizar tipo
    public APIResponse updateType(Long id, TypeRequestDto typeRequestDto) {
        try {
            Optional<Type> typeOpt = typeRepository.findById(id);
            if (typeOpt.isEmpty()) {
                return new APIResponse("Tipo de evento no encontrado", true, HttpStatus.NOT_FOUND);
            }

            // Verificar que no exista otro tipo con el mismo nombre (excluyendo el actual)
            Optional<Type> existingType = typeRepository.findByName(typeRequestDto.getName());
            if (existingType.isPresent() && !existingType.get().getId().equals(id)) {
                return new APIResponse("Ya existe otro tipo de evento con el nombre: " + typeRequestDto.getName(), true, HttpStatus.CONFLICT);
            }

            Type type = typeOpt.get();
            type.setName(typeRequestDto.getName());
            type.setDescription(typeRequestDto.getDescription());

            Type updatedType = typeRepository.save(type);
            return new APIResponse("Tipo de evento actualizado exitosamente", updatedType, HttpStatus.OK);
        } catch (Exception e) {
            return new APIResponse("Error interno del servidor: " + e.getMessage(), true, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Eliminar tipo
    public APIResponse deleteType(Long id) {
        try {
            Optional<Type> typeOpt = typeRepository.findById(id);
            if (typeOpt.isEmpty()) {
                return new APIResponse("Tipo de evento no encontrado", true, HttpStatus.NOT_FOUND);
            }

            Type type = typeOpt.get();

            // Verificar si el tipo tiene eventos asociados
            if (type.getEvents() != null && !type.getEvents().isEmpty()) {
                return new APIResponse("No se puede eliminar el tipo porque tiene eventos asociados", true, HttpStatus.CONFLICT);
            }

            typeRepository.delete(type);
            return new APIResponse("Tipo de evento eliminado exitosamente", true, HttpStatus.OK);
        } catch (Exception e) {
            return new APIResponse("Error interno del servidor: " + e.getMessage(), true, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Buscar tipos por descripción
    public APIResponse searchTypesByDescription(String description) {
        try {
            // Implementar búsqueda en el repositorio
            List<Type> types = typeRepository.findAll().stream()
                .filter(type -> type.getDescription() != null &&
                       type.getDescription().toLowerCase().contains(description.toLowerCase()))
                .toList();

            return new APIResponse("Búsqueda completada exitosamente", types, HttpStatus.OK);
        } catch (Exception e) {
            return new APIResponse("Error interno del servidor: " + e.getMessage(), true, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
