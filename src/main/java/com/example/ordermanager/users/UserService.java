package com.example.ordermanager.users;

import com.example.ordermanager.configuration.email.EmailSenderService;
import com.example.ordermanager.configuration.handler.InternalServerErrorException;
import com.example.ordermanager.configuration.handler.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;
import static java.util.Objects.isNull;

@Service
public class UserService {

    private static final Logger errorLog = LogManager.getLogger("errorLogger");

    @Autowired
    UserRepository userRepository;

    @Transactional
    public User create(CreateUserRequest request){
        try {
            User user = new User(request);
            userRepository.save(user);

            return user;
        } catch (Exception e){
            errorLog.error(e.getMessage());
            throw new InternalServerErrorException(e.getMessage(), e);
        }
    }

    @Transactional
    public User update(long userId, CreateUserRequest request){
        try {
            User user = userRepository.findById(userId).get();

            if (isNull(user)) throw new NotFoundException("User not found");

            user.setEmail(request.getEmail());
            user.setName(request.getName());
            userRepository.save(user);

            return user;
        } catch (NotFoundException e){
            errorLog.error(e.getMessage());
            throw new NotFoundException(e.getMessage(), e);
        } catch (Exception e){
            errorLog.error(e.getMessage());
            throw new InternalServerErrorException(e.getMessage(), e);
        }
    }

    public User getUser(long userID){
        try {
            User user = userRepository.findById(userID).get();

            if(isNull(user)) throw new NotFoundException("User not found");

            return user;
        } catch (NotFoundException e){
            errorLog.error(e.getMessage());
            throw new NotFoundException(e.getMessage(), e);
        } catch (Exception e){
            errorLog.error(e.getMessage());
            throw new InternalServerErrorException(e.getMessage(), e);
        }
    }

    @Transactional
    public void delete(long userId){
        try {
            User user = userRepository.findById(userId).get();

            if (isNull(user)) throw new NotFoundException("User not found");

            userRepository.delete(user);
        } catch (NotFoundException e){
            errorLog.error(e.getMessage());
            throw new NotFoundException(e.getMessage(), e);
        } catch (Exception e){
            throw new InternalServerErrorException(e.getMessage(), e);
        }
    }

    public List<User> getAll() {
        try {
            List<User> users = userRepository.findAll();

            if (users.isEmpty()) throw new NotFoundException("No user found");

            return users;
        } catch (NotFoundException e){
            errorLog.error(e.getMessage());
            throw new NotFoundException(e.getMessage(), e);
        } catch (Exception e){
            throw new InternalServerErrorException(e.getMessage(), e);
        }
    }
}
