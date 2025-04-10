package crockdaly.edu.springtodo.service;


import crockdaly.edu.springtodo.dto.TaskDTO;
import crockdaly.edu.springtodo.entity.Task;
import crockdaly.edu.springtodo.mapper.TaskMapper;
import crockdaly.edu.springtodo.model.TaskStatus;
import crockdaly.edu.springtodo.repository.TaskRepository;
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

    @Mock
    private TaskMapper taskMapper;

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
        when(taskMapper.toEntity(taskDTO)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toDto(task)).thenReturn(taskDTO);

        TaskDTO createdTaskDTO = taskService.createTask(taskDTO);

        verify(taskMapper).toEntity(taskDTO);
        verify(taskRepository).save(task);
        verify(taskMapper).toDto(task);
        assertNotNull(createdTaskDTO);
        assertEquals(taskDTO.getTitle(), createdTaskDTO.getTitle());
    }

    @Test
    void testUpdateTask() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toDto(task)).thenReturn(taskDTO);

        TaskDTO updatedTaskDTO = taskService.updateTask(1L, taskDTO);

        verify(taskRepository).findById(1L);
        verify(taskRepository).save(task);
        verify(taskMapper).toDto(task);
        assertEquals(taskDTO.getTitle(), updatedTaskDTO.getTitle());
    }

    @Test
    void testUpdateTaskNotFound_ThrowsResponseStatusException() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> taskService.updateTask(1L, taskDTO));
        verify(taskRepository).findById(1L);
        verify(taskMapper, never()).toDto(any());
    }

    @Test
    void deleteTask() {
        when(taskRepository.existsById(1L)).thenReturn(true);

        taskService.deleteTask(1L);

        verify(taskRepository).deleteById(1L);
    }

    @Test
    void deleteTaskNotFound_ThrowsResponseStatusException() {
        when(taskRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> taskService.deleteTask(1L));
        verify(taskRepository, never()).deleteById(anyLong());
    }

    @Test
    void testFilterByStatus() {
        when(taskRepository.findAllByStatusOrderByStatusAsc(TaskStatus.TODO)).thenReturn(List.of(task));
        when(taskMapper.toDto(task)).thenReturn(taskDTO);

        List<TaskDTO> result = taskService.filterByStatus(TaskStatus.TODO);

        verify(taskRepository).findAllByStatusOrderByStatusAsc(TaskStatus.TODO);
        verify(taskMapper).toDto(task);
        assertEquals(1, result.size());
    }

    @Test
    void testFilterByStatusNotFound_ThrowsResponseStatusException() {
        when(taskRepository.findAllByStatusOrderByStatusAsc(TaskStatus.TODO)).thenReturn(List.of());

        assertThrows(ResponseStatusException.class, () -> taskService.filterByStatus(TaskStatus.TODO));
    }

    @Test
    void sortByDueDate() {
        when(taskRepository.findAllByOrderByDueDateAsc()).thenReturn(List.of(task));
        when(taskMapper.toDto(task)).thenReturn(taskDTO);

        List<TaskDTO> result = taskService.sortByDueDate();

        verify(taskRepository).findAllByOrderByDueDateAsc();
        verify(taskMapper).toDto(task);
        assertEquals(1, result.size());
    }

    @Test
    void sortByStatus() {
        when(taskRepository.findAllByOrderByStatusAsc()).thenReturn(List.of(task));
        when(taskMapper.toDto(task)).thenReturn(taskDTO);

        List<TaskDTO> result = taskService.sortByStatus();

        verify(taskRepository).findAllByOrderByStatusAsc();
        verify(taskMapper).toDto(task);
        assertEquals(1, result.size());
    }

    @Test
    void testGetAllTasks() {
        when(taskRepository.findAll()).thenReturn(List.of(task));
        when(taskMapper.toDto(task)).thenReturn(taskDTO);

        List<TaskDTO> result = taskService.getAllTasks();

        verify(taskRepository).findAll();
        verify(taskMapper).toDto(task);
        assertEquals(1, result.size());
    }

}
