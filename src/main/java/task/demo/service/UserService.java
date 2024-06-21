package task.demo.service;

import task.demo.dto.UserDto;
import task.demo.model.User;

public interface UserService {
    User createUser(UserDto userDto);
    User updateUser(UserDto userDto, Long id);
    void deleteUser(Long id);
    User getCurrentUser();
    void blockUser(Long id);
    void unblockUser(Long id);
}
