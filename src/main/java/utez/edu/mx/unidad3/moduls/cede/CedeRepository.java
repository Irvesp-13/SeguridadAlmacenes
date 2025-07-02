package utez.edu.mx.unidad3.moduls.cede;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CedeRepository extends JpaRepository<Cede, Long> {
    List<Cede> findAll();
    Optional<Cede> findById(Long id);
    Optional<Cede> findByClave(String clave);
    Cede save (Cede cede);

    @Modifying
    @Query(value = "DELETE FROM cede WHERE id = :id", nativeQuery = true)
    void deleteById(@Param("id") Long id);
}
