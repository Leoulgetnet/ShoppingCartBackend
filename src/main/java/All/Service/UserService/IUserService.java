package All.Service.UserService;

import All.Dto.UserDto;
import All.Model.User;
import All.Request.CreateUserRequest;
import All.Request.UserUpdateRequest;

public interface IUserService {
    User getUserById(Long userId);
    User createUser(CreateUserRequest request);
    User updateUser(UserUpdateRequest request, Long userid);
    void deleteUser(Long userId);

    User changeToUser(CreateUserRequest request);

    UserDto chageToUserDto(User user);
}
