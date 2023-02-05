package com.shravan.api.users.service.impl;

import com.shravan.api.users.client.AlbumsClient;
import com.shravan.api.users.dto.UserDto;
import com.shravan.api.users.model.AlbumResponseModel;
import com.shravan.api.users.model.UserEntity;
import com.shravan.api.users.repository.UserRepository;
import com.shravan.api.users.service.UserService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    //private final RestTemplate restTemplate;

    private final AlbumsClient albumsClient;
    private final Environment environment;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, AlbumsClient albumsClient, Environment environment) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.albumsClient = albumsClient;
        this.environment = environment;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());
        userDto.setEncryptedPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);


        UserEntity entity = userRepository.save(userEntity);

        return modelMapper.map(entity, UserDto.class);
    }

    @Override
    public UserDto getUserDetailsByEmail(String email) {
        UserEntity userDetails = userRepository.findByEmail(email);

        if (null == userDetails) throw new UsernameNotFoundException(email);

        return new ModelMapper().map(userDetails, UserDto.class);

    }

    @Override
    public UserDto getUserDetailsByUserId(String userId) {

        UserEntity userEntity = userRepository.findByUserId(userId);

        if (null == userId) throw new UsernameNotFoundException("User not found");


        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);

       /* String albumsUrl = String.format(Objects.requireNonNull(environment.getProperty("albums.url")), userId);
        ResponseEntity<List<AlbumResponseModel>> albumResponse = restTemplate.exchange(
                albumsUrl,
                HttpMethod.GET,
                null, new ParameterizedTypeReference<List<AlbumResponseModel>>() {
                });

        List<AlbumResponseModel> albumList = albumResponse.getBody();*/

        log.info("Before calling Albums Microservice");
        List<AlbumResponseModel> albumList = albumsClient.getAlbums(userId);
        log.info("After calling Albums Microservice");

        userDto.setAlbums(albumList);

        return userDto;

    }

    @Override
    public UserDetails loadUserByUsername(String userName) {

        UserEntity userDetails = userRepository.findByEmail(userName);
        if (null == userDetails) throw new UsernameNotFoundException(userName);
        return new User(
                userDetails.getEmail(),
                userDetails.getEncryptedPassword(),
                true,
                true,
                true,
                true,
                new ArrayList<>());
    }
}
