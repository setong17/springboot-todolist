package com.springboot.todolist.controller;

import com.springboot.todolist.dto.ResponseDTO;
import com.springboot.todolist.dto.TodoDTO;
import com.springboot.todolist.model.TodoEntity;
import com.springboot.todolist.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("todo")
public class TodoConrtoller {

    @Autowired
    TodoService todoService;

    @GetMapping("/test")
    public ResponseEntity<?> testTodo() {

        String str = todoService.testService();

        List<String> list = new ArrayList<>();
        list.add(str);

        ResponseDTO<String> response = ResponseDTO.<String>builder()
                .data(list)
                .build();

        return ResponseEntity.ok().body(response);
    }


    @GetMapping
    public ResponseEntity<?> retrieveTodoList(@AuthenticationPrincipal String userId) {

        //String temporaryUserId = "temporary-user";

        // TODO 리스트 조회
        List<TodoEntity> todoList = todoService.retrieve(userId);

        List<TodoDTO> dtoList = todoList.stream().map(TodoDTO::new).collect(Collectors.toList());

        ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder()
                .data(dtoList)
                .build();

        return ResponseEntity.ok().body(response);
    }


    @PostMapping
    public ResponseEntity<?> createTodo(
            @AuthenticationPrincipal String userId
            , @RequestBody TodoDTO dto) {

        try {

            //String temporaryUserId = "temporary-user";

            // TodoEntity 변환
            TodoEntity entity = TodoDTO.toEntity(dto);

            // id를 null로 초기화
            entity.setId(null);

            // 임시 사용자 아이디 설정
            entity.setUserId(userId);

            // 데이터 저장 - Todo 엔티티 생성
            List<TodoEntity> entityList = todoService.create(entity);

            // 자바 스트림 이용 - 리턴된 엔티티 리스트를 TodoDTO로 변환
            List<TodoDTO> dtoList = entityList.stream().map(TodoDTO::new).collect(Collectors.toList());

            // 변환된 TodoDTO 리스트를 이용해 ResponseDTO를 초기화 한다.
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder()
                    .data(dtoList)
                    .build();

            return ResponseEntity.ok().body(response);

        } catch (Exception e) {
            String error = e.getMessage();

            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder()
                    .error(error)
                    .build();

            return ResponseEntity.badRequest().body(response);
        }
    }


    @PutMapping
    public ResponseEntity<?> udpateTodo(@AuthenticationPrincipal String userId, @RequestBody TodoDTO dto) {

        //String temporaryUserId = "temporary-user";

        TodoEntity entity = TodoDTO.toEntity(dto);

        entity.setUserId(userId);

        List<TodoEntity> todoList = todoService.update(entity);

        List<TodoDTO> dtoList = todoList.stream().map(TodoDTO::new).collect(Collectors.toList());

        ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder()
                .data(dtoList)
                .build();

        return ResponseEntity.ok().body(response);
    }


    @DeleteMapping
    public ResponseEntity<?> deleteTodo(@AuthenticationPrincipal String userId, @RequestBody TodoDTO dto) {

        //String temporaryUserId = "temporary-user";

        TodoEntity entity = TodoDTO.toEntity(dto);

        entity.setUserId(userId);

        try {

            List<TodoEntity> todoList = todoService.delete(entity);

            List<TodoDTO> dtoList = todoList.stream().map(TodoDTO::new).collect(Collectors.toList());

            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder()
                    .data(dtoList)
                    .build();

            return ResponseEntity.ok().body(response);

        } catch (Exception e) {
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder()
                    .error(e.getMessage())
                    .build();

            return ResponseEntity.badRequest().body(response);
        }

    }

}
