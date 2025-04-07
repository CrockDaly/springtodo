package crockdaly.edu.springtodo;


import crockdaly.edu.springtodo.dto.TaskDTO;
import crockdaly.edu.springtodo.entity.Task;
import crockdaly.edu.springtodo.model.TaskStatus;
import crockdaly.edu.springtodo.repository.TaskRepository;
import crockdaly.edu.springtodo.service.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    private Task task;
    private TaskDTO taskDTO;

    @BeforeEach
    void setUp() {
        task = new Task();
        task.setTitle("Test Title");
        task.setDescription("Test Description");
        task.setDueDate(LocalDate.parse("2025-12-12"));
        task.setStatus(TaskStatus.DONE);

        taskDTO = new TaskDTO();
        taskDTO.setTitle("Test Title");
        taskDTO.setDescription("Test Description");
        taskDTO.setDueDate(LocalDate.parse("2025-12-12"));
        taskDTO.setStatus(TaskStatus.DONE);
    }

    @Test
    void testCreateTask() {
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskDTO createdTaskDTO = taskService.createTask(taskDTO);

        verify(taskRepository, times(1)).save(any(Task.class));
        assertNotNull(createdTaskDTO);
        assertEquals("Test Title", createdTaskDTO.getTitle());
        assertEquals("Test Description", createdTaskDTO.getDescription());
        assertEquals(LocalDate.parse("2025-12-12"), createdTaskDTO.getDueDate());
        assertEquals(TaskStatus.DONE, createdTaskDTO.getStatus());
    }

    @Test
    void testUpdateTask() {

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskDTO updatedTaskDTO = taskService.updateTask(1L, taskDTO);

        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(any(Task.class));
        assertNotNull(updatedTaskDTO);
        assertEquals("Test Title", updatedTaskDTO.getTitle());
        assertEquals("Test Description", updatedTaskDTO.getDescription());
        assertEquals(LocalDate.parse("2025-12-12"), updatedTaskDTO.getDueDate());
        assertEquals(TaskStatus.DONE, updatedTaskDTO.getStatus());
    }

    @Test
    void testUpdateTaskNotFound_ThrowsResponseStatusException() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> taskService.updateTask(1L, taskDTO));
    }

    @Test
    void deleteTask() {
        when(taskRepository.existsById(1L)).thenReturn(true);

        taskService.deleteTask(1L);

        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteTaskNotFound_ThrowsResponseStatusException() {
        when(taskRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> taskService.deleteTask(1L));
    }

    @Test
    void testFilterByStatus() {
        when(taskRepository.findAllByStatusOrderByStatusAsc(TaskStatus.TODO)).thenReturn(List.of(task));

        List<TaskDTO> tasks = taskService.filterByStatus(TaskStatus.TODO);

        assertNotNull(tasks);
        assertEquals(1, tasks.size());
        assertEquals("Test Title", tasks.get(0).getTitle());
    }

    @Test
    void testFilterByStatusNotFound_ThrowsResponseStatusException() {
        when(taskRepository.findAllByStatusOrderByStatusAsc(TaskStatus.TODO)).thenReturn(List.of());

        assertThrows(ResponseStatusException.class, () ->
                taskService.filterByStatus(TaskStatus.TODO));
    }

    @Test
    void sortByDueDate() {
        when(taskRepository.findAllByOrderByDueDateAsc()).thenReturn(List.of(task));

        List<TaskDTO> tasks = taskService.sortByDueDate();

        assertNotNull(tasks);
        assertEquals(1, tasks.size());
        assertEquals(LocalDate.parse("2025-12-12"), tasks.get(0).getDueDate());
    }

    @Test
    void sortByStatus() {
        when(taskRepository.findAllByOrderByStatusAsc()).thenReturn(List.of(task));

        List<TaskDTO> tasks = taskService.sortByStatus();

        assertNotNull(tasks);
        assertEquals(1, tasks.size());
        assertEquals(TaskStatus.DONE, tasks.get(0).getStatus());
    }

    @Test
    void testGetAllTasks() {
        when(taskRepository.findAll()).thenReturn(List.of(task));

        List<TaskDTO> tasks = taskService.getAllTasks();

        assertNotNull(tasks);
        assertEquals(1, tasks.size());
        assertEquals("Test Title", tasks.get(0).getTitle());
    }


}
