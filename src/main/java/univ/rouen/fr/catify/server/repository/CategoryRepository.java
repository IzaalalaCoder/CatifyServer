package univ.rouen.fr.catify.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import univ.rouen.fr.catify.server.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
