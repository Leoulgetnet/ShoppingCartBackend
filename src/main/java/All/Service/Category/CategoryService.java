package All.Service.Category;

import All.Exceptions.AlreadyExistsException;
import All.Exceptions.CategoryNotFoundException;
import All.Exceptions.ProductNotFoundException;
import All.Model.Category;
import All.Repository.CategroyRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CategoryService implements ICategoryService {
    @Autowired
CategroyRepository repo;

    @Override
    public Category getCategoryById(Long id) {
        return repo.findById(id).
                orElseThrow(()->new CategoryNotFoundException("Category Not Found"));
    }

    @Override
    public Category getCategoryByName(String name) {
        return repo.findByName(name);
    }

    @Override
    public List<Category> getAllCategories() {
        return repo.findAll();
    }

    @Override
    public Category addCategory(Category category) {
        return Optional.of(category)
                .filter(c->!repo.existsByName(c.getName()))

                /*Vice versa relation ship
                * here filter will pass a data if the condition is right
                * like if the condition is right it will pass the category
                * to the map method
                * */
                .map(repo::save)
                .orElseThrow(()->new AlreadyExistsException("Already Existed Exception"));}

    @Override
    public Category updateCategory(Category category,Long id) {
        return Optional.ofNullable(getCategoryById(id))
                .map(c->{
                    c.setName(category.getName());
                    return repo.save(c);})
                .orElseThrow(()->new CategoryNotFoundException(category.getName()+"Not Found"));
    }

    @Override
    public void deleteCategoryById(Long id) {
repo.findById(id)
        .ifPresentOrElse(repo::delete,()->new CategoryNotFoundException("Category Not Found"));
    }


}
