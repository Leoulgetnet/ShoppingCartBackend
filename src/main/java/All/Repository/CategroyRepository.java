package All.Repository;

import All.Model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategroyRepository extends JpaRepository<Category,Long> {
    Category findByName(String name);
    Boolean existsByName(String name);
}
