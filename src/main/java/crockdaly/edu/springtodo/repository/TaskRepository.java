package crockdaly.edu.springtodo.repository;

import crockdaly.edu.springtodo.entity.Task;
import crockdaly.edu.springtodo.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByStatusOrderByStatusAsc(TaskStatus status);

    List<Task> findAllByOrderByDueDateAsc();

    List<Task> findAllByOrderByStatusAsc();
}
