package crockdaly.edu.springtodo.service;


import crockdaly.edu.springtodo.dto.TaskDTO;
import crockdaly.edu.springtodo.entity.Task;
import crockdaly.edu.springtodo.model.TaskStatus;
import crockdaly.edu.springtodo.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    public Task toEntity(TaskDTO taskDTO) {
        Task task = new Task();
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setDueDate(taskDTO.getDueDate());
        task.setStatus(taskDTO.getStatus());

        return task;
    }

    public TaskDTO toDto(Task task) {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTitle(task.getTitle());
        taskDTO.setDescription(task.getDescription());
        taskDTO.setDueDate(task.getDueDate());
        taskDTO.setStatus(task.getStatus());

        return taskDTO;
    }

    @Override
    public TaskDTO createTask(TaskDTO taskDTO) {
        Task task = toEntity(taskDTO);
        task.setStatus(Optional.ofNullable(taskDTO.getStatus()).orElse(TaskStatus.TODO));

        return toDto(taskRepository.save(task));
    }

    @Override
    public TaskDTO updateTask(Long id, TaskDTO taskDTO) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));

        existingTask.setTitle(taskDTO.getTitle());
        existingTask.setDescription(taskDTO.getDescription());
        existingTask.setDueDate(taskDTO.getDueDate());
        existingTask.setStatus(taskDTO.getStatus());

        taskRepository.save(existingTask);
        return toDto(existingTask);
    }

    @Override
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
        }
        taskRepository.deleteById(id);
    }

    @Override
    public List<TaskDTO> filterByStatus(TaskStatus status) {
        List<Task> tasks = taskRepository.findAllByStatusOrderByStatusAsc(status);

        if (tasks.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No tasks found with status: " + status);
        }

        return tasks.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> sortByDueDate() {
       return taskRepository.findAllByOrderByDueDateAsc().stream()
               .map(this::toDto)
               .collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> sortByStatus() {
        return taskRepository.findAllByOrderByStatusAsc().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
