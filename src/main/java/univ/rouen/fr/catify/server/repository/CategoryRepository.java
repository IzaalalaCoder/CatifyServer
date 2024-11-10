package univ.rouen.fr.catify.server.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import univ.rouen.fr.catify.server.entity.Category;
import java.util.Date;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    boolean existsByName(String name);

    Category findByName(String name);

    @Query("SELECT c FROM Category c " +
            "WHERE (:root IS NULL OR (c.parent IS NULL AND :root = true) OR (c.parent IS NOT NULL AND :root = false)) " +
            "AND (:afterDate IS NULL OR c.dateOfCreation > :afterDate) " +
            "AND (:beforeDate IS NULL OR c.dateOfCreation < :beforeDate)")
    Page<Category> findCategoriesByFilters(@Param("root") Boolean root,
                                           @Param("afterDate") Date afterDate,
                                           @Param("beforeDate") Date beforeDate,
                                           Pageable pageable);
}
