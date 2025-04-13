package All.Controller;

import All.Dto.ProductDto;
import All.Exceptions.ProductNotFoundException;
import All.Exceptions.ResourceNotFoundException;
import All.Model.Product;
import All.Request.AddProductRequest;
import All.Request.ProductUpdateRequest;
import All.Response.ApiResponse;
import All.Service.Product.ProductService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.rmi.ServerError;
import java.util.List;

import static org.apache.logging.log4j.ThreadContext.isEmpty;
import static org.springframework.boot.actuate.metrics.http.Outcome.SERVER_ERROR;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {
    @Autowired
    private final ProductService service;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllProducts(){
        try {
            List<Product> products=service.getAllProducts();

            return ResponseEntity.ok(new ApiResponse("Success",service.getConvertedProducts(products)));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }}

    @GetMapping("product/{Id}/product")
    public ResponseEntity<ApiResponse> getProductsById(@PathVariable("Id") Long id){
        ProductDto productDto=service.convertToDto(service.getProductById(id));
        try {
            return ResponseEntity.ok(new ApiResponse("Success",productDto));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));}}


    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest product){
        try {
            return ResponseEntity.ok(new ApiResponse("Saved Successfully",service.addProduct(product)));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),SERVER_ERROR));
        }}

    @PutMapping("/products/productsId/updated")
    public ResponseEntity<ApiResponse> updateProduct(@RequestBody ProductUpdateRequest product
            ,@PathVariable("id") Long id){
        try {
            return ResponseEntity.ok(new ApiResponse("Updated Successfully",service.updateProduct(product,id)));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }}


    @DeleteMapping("/product/{id}/delete")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long id){
        try {
            service.deleteProductById(id);
            return ResponseEntity.ok(new ApiResponse("",id));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }}

    @GetMapping("/by/brand-and-name")
    public ResponseEntity<ApiResponse> getProductByBrandAndName(@RequestParam String brand,@RequestParam String productName){
        try {
            List<ProductDto> productDtos=service.getConvertedProducts(service.getProductsByBrandAndName(brand,productName));
            if(productDtos.isEmpty()){
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Not Found",NOT_FOUND));}
            else{
                return ResponseEntity.ok(new ApiResponse("Returned",productDtos));
            }
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Not found",INTERNAL_SERVER_ERROR));
        }}


    @GetMapping("products/by/category-and-brand")
    public ResponseEntity<ApiResponse> getProductByCategoryAndBrand(@PathVariable String category,@PathVariable String brand){
        try {
            if(service.getProductsByCategoryAndBrand(category,brand).isEmpty()){
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Not Found",NOT_FOUND));}
            else{
                return ResponseEntity.ok(new ApiResponse("Returned",service.getProductsByCategoryAndBrand(category,brand)));
            }
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error Occured",INTERNAL_SERVER_ERROR));
        }}



    @GetMapping("/products/by-brand")
    public ResponseEntity<ApiResponse> getProductByBrand(@PathVariable String brand){
        try {
            if(service.getProductsByBrand(brand).isEmpty()){
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Not Found",null));}

                return ResponseEntity.ok(new ApiResponse("Returned",service.getProductsByBrand(brand)));

        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Error Occured",null));
        }}

    @GetMapping("product/{category}/all/products")
    public ResponseEntity<ApiResponse> getProductByCategory(@PathVariable String category){
        try {
            if(service.getProductsByCategory(category).isEmpty()){
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Not Found",null));}
            else{
                return ResponseEntity.ok(new ApiResponse("Returned",service.getProductsByCategory(category)));
            }
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Error Occured",null));
        }}


    @GetMapping("/product/count/by-brand/and-name")
    public ResponseEntity<ApiResponse> countProductsByBrandAndName(@RequestParam String brand,@RequestParam String name){
        try {

                return ResponseEntity.ok(new ApiResponse("Product Counted!",service.countProductsByBrandAndName(brand,name)));


        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse(e.getMessage(),null));
        }
    }



























}
