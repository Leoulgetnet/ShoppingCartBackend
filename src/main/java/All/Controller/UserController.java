package All.Controller;
import All.Dto.UserDto;
import All.Exceptions.AlreadyExistsException;
import All.Exceptions.ProductNotFoundException;
import All.Exceptions.ResourceNotFoundException;
import All.Model.User;
import All.Request.CreateUserRequest;
import All.Request.UserUpdateRequest;
import All.Response.ApiResponse;
import All.Service.UserService.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Controller
@RequestMapping("${api.prefix}/userID")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    IUserService service;

    @GetMapping("/{userId}/user")
    public ResponseEntity<ApiResponse>
    getUserById(@PathVariable Long userId){
        try {
            UserDto user=service.chageToUserDto(service.getUserById(userId));
            return ResponseEntity.ok(new ApiResponse("Success",user));
        } catch (ProductNotFoundException e) {
           return ResponseEntity.status(NOT_FOUND)
                   .body(new ApiResponse(e.getMessage(),null));
        }}
 @PostMapping("/add")
    public ResponseEntity<ApiResponse> createUser
         (@RequestBody CreateUserRequest request){
        try {
            User user=service.createUser(request);
            return  ResponseEntity.ok(new ApiResponse("Success",user));
        } catch (AlreadyExistsException e) {
            return ResponseEntity
                    .status(NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(),null));
        }
    }

    @PostMapping("/{userId}/update")
    public ResponseEntity<ApiResponse>
    updateUser(@RequestBody UserUpdateRequest request,@PathVariable Long userId){
        try {
            User user= service.updateUser(request,userId);
            return ResponseEntity.ok(new ApiResponse("Success",user));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(),null));
        }
    }

    @DeleteMapping("/{userId}/delete")
    public ResponseEntity<ApiResponse>
    deleteUser(@PathVariable Long userId){
        try {
            service.deleteUser(userId);
            return ResponseEntity.ok(new ApiResponse("Delete User Success",null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Not Found",null));
        }
    }









}
