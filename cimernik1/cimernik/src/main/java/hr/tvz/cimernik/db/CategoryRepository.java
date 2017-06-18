package hr.tvz.cimernik.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hr.tvz.cimernik.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer>{

}
