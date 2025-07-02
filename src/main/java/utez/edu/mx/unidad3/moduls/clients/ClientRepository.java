package utez.edu.mx.unidad3.moduls.clients;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository <Client, Long> {
    List<Client> findAll();
    Client save (Client client); // Este metodo nos ayuda a guardar y actualizar.
    Optional <Client> findById(long id);

    @Modifying // Nos ayuda a modificar o eliminar datos directamente de la base de datos.
    @Query(value = "DELETE FROM client WHERE id = :1", nativeQuery = true)
    void delete(@Param("id") long id);

    Optional<Client> findByEmail(String email);
}
