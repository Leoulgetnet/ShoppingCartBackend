package All.Service.Product;

import All.Dto.ImageDto;
import All.Dto.ProductDto;
import All.Exceptions.ProductNotFoundException;
import All.Model.Category;
import All.Model.Image;
import All.Model.Product;
import All.Repository.CategroyRepository;
import All.Repository.ProductRepository;
import All.Request.AddProductRequest;
import All.Request.ProductUpdateRequest;
import All.Service.Image.ImageService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    @Autowired
    ProductRepository repo;
    @Autowired
    CategroyRepository repoCategory;
    @Autowired
    ImageService imageservice;
    @Autowired
    ModelMapper mapper;

    @Override
    public Product addProduct(AddProductRequest request) {

        /*Checks if the category is found in the db
        * if yes,set it as the new product category
        * if no , save it as a new category
        * the set as the new product new category
        * */

        String categoryName=request.getCategory().getName();
/*if it is null the orElseGet will get executed also in the case os Optional.Of()*/
        Category category= Optional.ofNullable(repoCategory.findByName(categoryName))
                .orElseGet(()->{
                    Category newCategory=new Category(categoryName);
                    return repoCategory.save(newCategory);
                });


        request.setCategory(category);
        return repo.save(createProduct(request,category));
    }


    private Product updateExistingProduct(Product existingProduct,
                                          ProductUpdateRequest request){
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());
        existingProduct.setDescription(request.getDescription());
        Category category=repoCategory.findByName(request.getCategory().getName());
        request.setCategory(category);
        return existingProduct;}

/*U will get the existing product by Id*/

    @Override
    public Product updateProduct(ProductUpdateRequest request, Long productId) {
        return repo.findById(productId)
                .map(existingProduct->updateExistingProduct(existingProduct,request))
                /*Anything which is returned */
                .map(repo::save)
                .orElseThrow(()->new ProductNotFoundException("Product Not found"));}

    private Product createProduct(AddProductRequest request, Category category){
        return new Product(
                category,
                request.getInventory(),
                request.getPrice(),
                request.getDescription(),
                request.getBrand(),
                request.getName()
        );}



    @Override
    public Product getProductById(Long id) {
        return (Product) repo.findById(id).orElseThrow(()->new ProductNotFoundException("Product Not Found"));
    }

    /***********Focus More*********************/
    @Override
    public void deleteProductById (Long id) {
        repo.findById(id).ifPresentOrElse(repo::delete,
                ()->new ProductNotFoundException("Product Not Found"));
    }



    @Override
    public List<Product> getAllProducts() {
        return repo.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return repo.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return repo.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return repo.findByCategoryNameAndBrand(category,brand);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return repo.findByName(name);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String category, String name) {

        return repo.findByBrandAndName(category,name);
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {

        return Long.valueOf(repo.countByBrandAndName(brand,name));
    }

    public ProductDto convertToDto(Product product){
        List<Image> images=imageservice.getImageByProductId(Long.valueOf(product.getId()));
        List<ImageDto> imageDtos=images.stream().map(image->mapper.map(image,ImageDto.class))
                .collect(Collectors.toList());
        ProductDto productDto=mapper.map(product,ProductDto.class);
        productDto.setImages(imageDtos);
        return productDto;
    }

    @Override
    public List<ProductDto> getConvertedProducts(List<Product> product){
        return product.stream().map(this::convertToDto).toList();}



}
