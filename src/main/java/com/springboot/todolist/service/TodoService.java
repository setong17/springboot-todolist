package com.springboot.todolist.service;

import com.springboot.todolist.model.TodoEntity;
import com.springboot.todolist.persistence.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TodoService {

    @Autowired
    TodoRepository repository;

    public String testService() {

        // TodoEntity 생성
        TodoEntity entity = TodoEntity.builder()
                .title("My first todo item")
                .build();

        // TodoEntity 저장
        repository.save(entity);

        // TodoEntity 검색
        TodoEntity saveEntity = repository.findById(entity.getId()).get();

        return saveEntity.getTitle();
    }


    /**
     * TODO 리스트 저장
     * @param entity
     * @return
     */
    public List<TodoEntity> create(final TodoEntity entity) {
        // Validations
        validate(entity);

        // 저장
        repository.save(entity);

        log.info("Entity Id : {} is save.", entity.getId());

        return repository.findByUserId(entity.getUserId());
    }


    /**
     * Validation 체크
     * @param entity
     */
    private void validate(final TodoEntity entity) {
        if( entity == null ) {
            log.warn("Entity cannot be null.");
            throw new RuntimeException("Entity cannot be null.");
        }

        if( entity.getUserId() == null ) {
            log.warn("Unknown user.");
            throw new RuntimeException("Unknown user.");
        }
    }



}
