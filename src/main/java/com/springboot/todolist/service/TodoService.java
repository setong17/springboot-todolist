package com.springboot.todolist.service;

import com.springboot.todolist.model.TodoEntity;
import com.springboot.todolist.persistence.TodoRepository;
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
     * TODO 리스트 조회
     * @param userId
     * @return
     */
    public List<TodoEntity> retrieve(final String userId) {
        return repository.findByUserId(userId);
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
     * TODO 리스트 수정
     * @param entity
     * @return
     */
    public List<TodoEntity> update(final TodoEntity entity) {

        validate(entity);

        // 기존 등록된 데이터 조회
        final Optional<TodoEntity> original = repository.findById(entity.getId());

        original.ifPresent(todo -> {
            // 조회된 데이터가 있으면 값을 새로운 값으로 변경
            todo.setTitle(entity.getTitle());
            todo.setDone(entity.isDone());

            // 저장 (update)
            repository.save(todo);
        });

        return retrieve(entity.getUserId());
    }


    public List<TodoEntity> delete(final TodoEntity entity) {

        validate(entity);

        try {

            // 삭제
            repository.delete(entity);

        } catch (Exception e) {
            log.error("error deleting entity ", entity.getId(), e);
            throw new RuntimeException("error deleting entity " + entity.getId());
        }

        return retrieve(entity.getUserId());
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
