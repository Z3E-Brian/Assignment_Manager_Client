package org.una.programmingIII.Assignment_Manager_Client.Mapper;

import java.util.List;
import java.util.stream.Collectors;

public interface GenericMapper<E, D> {
    D convertToDTO(E entity);
    E convertToEntity(D dto);


    default List<D> convertToDTOList(List<E> entities) {
        return entities.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    default List<E> convertToEntityList(List<D> dtos) {
        return dtos.stream().map(this::convertToEntity).collect(Collectors.toList());
    }
}