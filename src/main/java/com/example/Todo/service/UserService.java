package com.example.Todo.service;

import com.example.Todo.model.UserEntity;
import com.example.Todo.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    // user 등록
    public UserEntity create(final UserEntity userEntity){
        if(userEntity==null || userEntity.getEmail()==null){
            throw new RuntimeException("Invalid arguments");
        }
        final String email = userEntity.getEmail();
        if(userRepository.existsByEmail(email)){    // 이메일이 존재하는지 검사
            throw new RuntimeException("Email already exists!");    // 존재하는 이메일
        }
        return userRepository.save(userEntity);
    }

    // 이메일과 비번으로 유저 조회
    public UserEntity getByCredentials(final String email, final String password){
        return userRepository.findByEmailAndPassword(email, password);
    }

}
