package com.example.Todo.service;

import com.example.Todo.model.TodoEntity;
import com.example.Todo.persistence.TodoRepository;
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

    public Optional<TodoEntity>update(final TodoEntity entity){
        validate(entity);
        if(repository.existsById(entity.getId()))
            repository.save(entity);
        else
            throw new RuntimeException("Unknown id");
        return repository.findById(entity.getId());
    }

    public Optional<TodoEntity> updateTodo(final TodoEntity entity){
        validate(entity);
        // 테이블 id에 해당하는 데이터셋을 가져오기
        final Optional<TodoEntity> original = repository.findById(entity.getId());
        // original에 담긴 내용을 todo에 할당하고 title, done의 값 변경
        original.ifPresent(todo->{
            todo.setTitle(entity.getTitle());
            todo.setDone(entity.isDone());
            repository.save(todo);
        });
        return repository.findById(entity.getId());
    }

    public String delete(final String id){
        if(repository.existsById(id))
            repository.deleteById(id);
        else
            throw new RuntimeException("id does not exist");
        return "Deleted";
    }
}
