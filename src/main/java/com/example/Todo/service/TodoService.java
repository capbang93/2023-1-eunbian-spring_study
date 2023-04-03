package com.example.Todo.service;

import com.example.Todo.model.TodoEntity;
import com.example.Todo.persistence.TodoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TodoService {
    @Autowired
    TodoRepository repository;

    public void validate(final TodoEntity entity){
        if(entity == null){
            log.warn("Entity cannot be null.");
            throw new RuntimeException("Entity cannot be null.");
        }
        if(entity.getUserId()==null){
            log.warn("Unknown user");
            throw new RuntimeException("Unknown user");
        }
    }
    public Optional<TodoEntity> create(final TodoEntity entity){
        validate(entity);
        repository.save(entity);
        return repository.findById(entity.getId());
    }

    public List<TodoEntity> retrieve(final String userId){
        return repository.findByUserId(userId);
    }

}
