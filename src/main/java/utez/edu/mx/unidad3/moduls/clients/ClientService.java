package utez.edu.mx.unidad3.moduls.clients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utez.edu.mx.unidad3.utils.APIResponse;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ClientService {
    @Autowired
    private ClientRepository clientRepository;

    // Consultar todos los clientes
    @Transactional(readOnly = true)
    public APIResponse findAll() {
        List<Client> list = new ArrayList<>();
        list = clientRepository.findAll();

        return new APIResponse("Operacion exitosa", list, false, HttpStatus.OK);
    }

    // Consultar un cliente por ID
    @Transactional(readOnly = true)
    public APIResponse findById(Long id) {
        try {
            Client found = clientRepository.findById(id).orElse(null);
            if (found == null) {
                return new APIResponse("El cliente no existe", true, HttpStatus.BAD_REQUEST);
            }
            return new APIResponse("Operacion exitosa", found,  false, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new APIResponse("No se pudo consultar al cliente", true, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Crear un cliente
    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public APIResponse saveClient(Client payload) {
        try {
            if (clientRepository.findByEmail((payload.getEmail())).isPresent()) {
                return new APIResponse("El correo ya existe", true, HttpStatus.BAD_REQUEST);
            }
            clientRepository.save(payload);
            return new APIResponse ("Operacion exitosa", false, HttpStatus.CREATED);
        } catch (Exception ex) {
            return new APIResponse("Error al crear el cliente", true, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Actualizar un cliente
    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public APIResponse updateClient(Client payload) {
        try {
            Client found = clientRepository.findById(payload.getId()).orElse(null);
            if (clientRepository.findByEmail((payload.getEmail())).isPresent()) {
                return new APIResponse("El usuario no existe", true, HttpStatus.BAD_REQUEST);
            }
            clientRepository.save(payload);
            return new APIResponse ("Operacion exitosa", false, HttpStatus.OK);
        } catch (NullPointerException nullex) {
            nullex.printStackTrace();
            return new APIResponse("No se aceptan valores nulos", true, HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new APIResponse("Error al actualizar el cliente", true, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Eliminar un cliente
    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public APIResponse deleteClient(Client payload) {
        try {
            Client found = clientRepository.findById(payload.getId()).orElse(null);
            if (found == null) {
                return new APIResponse("El usuario no existe", true, HttpStatus.BAD_REQUEST);
            }
            clientRepository.deleteById(found.getId());
            return new APIResponse("Operacion exitosa", false, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new APIResponse("Error al eliminar el cliente", true, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
