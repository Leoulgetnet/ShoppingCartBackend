package All.Service.UserService;

import All.Dto.UserDto;
import All.Exceptions.AlreadyExistsException;
import All.Exceptions.ResourceNotFoundException;
import All.Model.User;
import All.Repository.UserRepository;
import All.Request.CreateUserRequest;
import All.Request.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    @Autowired
    private final UserRepository repo;


    private final ModelMapper mapper;


    @Override
    public User getUserById(Long userId) {
        return repo.findById(userId)
                 .orElseThrow(()->new ResourceNotFoundException("User Not Found!"));
    }

    @Override
    public User createUser(CreateUserRequest request) {
        return Optional.of(request)
                .filter(user->!repo.existsByEmail(request.getEmail()))
                .map(req->{

                     User user=changeToUser(req);
                     return repo.save(user);
                })
                .orElseThrow(()->new AlreadyExistsException("User Already Existed with this email "+request.getEmail()));
    }

    @Override
    public User updateUser(UserUpdateRequest request, Long userid) {
        return repo.findById(userid).map(existingUser->{
                existingUser.setFirstName(request.getFirstName());
                existingUser.setLastName(request.getLastName());
                return repo.save(existingUser);
        }).orElseThrow(()->new ResourceNotFoundException("User Not Found"));
    }

    @Override
    public void deleteUser(Long userId) {
      repo.findById(userId).ifPresentOrElse(
              repo::delete,()->new ResourceNotFoundException("User Not Found")
);
    }

    @Override
    public User changeToUser(CreateUserRequest request){
        return mapper.map(request,User.class);
    }
    @Override
    public UserDto chageToUserDto(User user){
        return mapper.map(user,UserDto.class);
    }
}
