package crockdaly.edu.springtodo.controller;

import crockdaly.edu.springtodo.dto.TaskDTO;
import crockdaly.edu.springtodo.model.TaskStatus;
import crockdaly.edu.springtodo.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/todo-list/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        List<TaskDTO> tasks = taskService.getAllTasks();

        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@Valid @RequestBody TaskDTO taskDTO) {
        TaskDTO createdTask = taskService.createTask(taskDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id, @Valid @RequestBody TaskDTO taskDTO) {
        TaskDTO updatedTask = taskService.updateTask(id, taskDTO);

        return ResponseEntity.status(HttpStatus.OK).body(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{status}")
    public ResponseEntity<List<TaskDTO>> filterByStatus(@PathVariable TaskStatus status) {
        List<TaskDTO> filteredTasks = taskService.filterByStatus(status);

        return new ResponseEntity<>(filteredTasks, HttpStatus.OK);
    }

    @GetMapping("/sort/status")
    public ResponseEntity<List<TaskDTO>> sortByStatus() {
        List<TaskDTO> tasks = taskService.sortByStatus();

        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @GetMapping("/sort/due-date")
    public ResponseEntity<List<TaskDTO>> sortByDueDate() {
        List<TaskDTO> tasks = taskService.sortByDueDate();

        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

}
