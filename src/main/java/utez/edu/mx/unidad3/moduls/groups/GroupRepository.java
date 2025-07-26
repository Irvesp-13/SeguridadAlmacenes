package utez.edu.mx.unidad3.moduls.groups;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findAll();
    Group save(Group group); // Este metodo nos ayuda a guardar y actualizar.
    Optional <Group> findById(long id);

    @Modifying
    @Query(value = "DELETE FROM group WHERE id = :id", nativeQuery = true)
    void delete(@Param("id") long id);

    Optional<Group> findByName(String name);
}
