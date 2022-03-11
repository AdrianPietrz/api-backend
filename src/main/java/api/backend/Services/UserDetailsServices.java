package api.backend.Services;

import api.backend.Models.UserModel;
import api.backend.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDetailsServices implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel userModel;
        if(repository.findByUsername(username).isPresent()){
            userModel = repository.findByUsername(username).get();
        }
        else{
            return null;
        }

        return new User(userModel.getUsername(), userModel.getPassword(), new ArrayList<>());
    }
}