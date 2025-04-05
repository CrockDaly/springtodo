package crockdaly.edu.springtodo.service;


import crockdaly.edu.springtodo.dto.TaskDTO;
import crockdaly.edu.springtodo.model.TaskStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskService {
    List<TaskDTO> getAllTasks();
    TaskDTO createTask(TaskDTO taskDTO);
    TaskDTO updateTask(Long id,TaskDTO taskDTO);
    void deleteTask(Long id);
    List<TaskDTO> filterByStatus(TaskStatus status);
    List<TaskDTO> sortByDueDate();
    List<TaskDTO> sortByStatus();
}
