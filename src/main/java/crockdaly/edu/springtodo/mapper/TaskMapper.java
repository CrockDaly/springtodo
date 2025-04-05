package crockdaly.edu.springtodo.mapper;

import crockdaly.edu.springtodo.dto.TaskDTO;
import crockdaly.edu.springtodo.entity.Task;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TaskMapper {

    @Mapping(target = "status", source = "status")
    TaskDTO toDto(Task task);

    @Mapping(target = "status", source = "status")
    Task toEntity(TaskDTO taskDTO);

    void updateEntityFromDto(TaskDTO taskDTO, @MappingTarget Task task);

}
