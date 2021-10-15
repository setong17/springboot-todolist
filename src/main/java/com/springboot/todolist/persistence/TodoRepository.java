package com.springboot.todolist.persistence;

import com.springboot.todolist.model.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<TodoEntity, String> {

    //@Query("select t from Todo t where t.userId = ?1")
    List<TodoEntity> findByUserId(String userId);

}
