package All.Controller;

import All.Exceptions.AlreadyExistsException;
import All.Exceptions.ResourceNotFoundException;
import All.Model.Category;
import All.Response.ApiResponse;
import All.Service.Category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("${api.prefix}/categories")
@RequiredArgsConstructor
public class CategoryController {
    @Autowired
    private final CategoryService service;


    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllCategories(){
        try {
            return ResponseEntity
                    .ok(new ApiResponse("All Categories Returned!"
                            ,service.getAllCategories()));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(),
                    null));
        }}


    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addCategory(@RequestBody Category category){
        try {
            return ResponseEntity.ok(new ApiResponse("Category Saved",service.addCategory(category)));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage()
                    ,CONFLICT));
        }/*in Created you pass uri*/}


    @PostMapping("category/{id}/category")
    public ResponseEntity<ApiResponse> getCategoryById(@PathVariable("id")Long id){
        try {
            return ResponseEntity.ok(new ApiResponse("Found",service.getCategoryById(id)));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Not Found",NOT_FOUND));
        }}


    @PostMapping("/{name}/category")
    public ResponseEntity<ApiResponse> getCategoryByName(@PathVariable("name")String name){
        try {
            return ResponseEntity.ok(new ApiResponse("Found",service.getCategoryByName(name)));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Not Found",NOT_FOUND));
        }}











}
