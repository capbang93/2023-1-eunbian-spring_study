package com.example.Todo.controller;

import com.example.Todo.dto.ResponseDTO;
import com.example.Todo.dto.TodoDTO;
import com.example.Todo.model.TodoEntity;
import com.example.Todo.service.TodoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("todo")
public class TodoController {
    @Autowired
    TodoService service;

    @PostMapping
    public ResponseEntity<?> createTodo(@RequestBody TodoDTO dto){
        try{
            log.info("Log: createTodo entrance");
            // dto를 이용해 테이블에 저장하기 위한 엔티티 생성
            TodoEntity entity = TodoDTO.toEntity(dto);

            // entity userId를 임시로 저장
            entity.setUserId("temporary-userId");

            // service.create를 통해 repository에 엔티티 저장
            Optional<TodoEntity> entities = service.create(entity);
            log.info("Log: service.create ok!");

            // entities를 dtos 스트림으로 변환
            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
            log.info("Log: entities => dtos ok!");

            // Response DTO 생성
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
            log.info("Log: responseDto ok!");

            // HTTP Status 200 상태로 response 전송
            return ResponseEntity.ok().body(response);
        }
        catch (Exception e){
            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }


    @GetMapping
    public ResponseEntity<?>retrieveTodoList(){
        String temporaryUserId = "temporary-userId";
        List<TodoEntity> entities = service.retrieve(temporaryUserId);
        List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

        ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
        return ResponseEntity.ok().body(response);
    }

}
