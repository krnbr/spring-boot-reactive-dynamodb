package in.neuw.learn.service;

import in.neuw.learn.db.entities.Address;
import in.neuw.learn.db.entities.UserEntity;
import in.neuw.learn.db.repositories.UserRepository;
import in.neuw.learn.model.UserData;
import in.neuw.learn.model.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class TestDataService {

    @Autowired
    private UserRepository userRepository;

    public Mono<ServiceResponse> getUserById(final String id) {
        return userRepository.getById(id).map(ServiceResponse::new);
    }

    public Mono<ServiceResponse> saveUser(final UserData userData) {
        UserEntity u = new UserEntity();
        u.setId(UUID.randomUUID().toString());
        u.setName(userData.getName());
        Address address = new Address();
        address.setAddress(userData.getAddress());
        address.setCity(userData.getCity());
        address.setState(userData.getState());
        address.setCountry(userData.getCountry());
        u.setAddress(address);
        return userRepository.save(u).map(ServiceResponse::new);
    }

}
