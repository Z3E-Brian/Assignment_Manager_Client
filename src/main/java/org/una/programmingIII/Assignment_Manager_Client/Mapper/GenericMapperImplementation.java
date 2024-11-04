package org.una.programmingIII.Assignment_Manager_Client.Mapper;

import org.modelmapper.ModelMapper;


public class GenericMapperImplementation<E, D> implements GenericMapper<E, D> {


    private final Class<E> entityClass;
    private final Class<D> dtoClass;
    private final ModelMapper modelMapper;


    public GenericMapperImplementation(Class<E> entityClass, Class<D> dtoClass, ModelMapper modelMapper) {
        this.entityClass = entityClass;
        this.dtoClass = dtoClass;
        this.modelMapper = modelMapper;
    }


    @Override
    public D convertToDTO(E entity) {
        return modelMapper.map(entity, dtoClass);
    }


    @Override
    public E convertToEntity(D dto) {
        return modelMapper.map(dto, entityClass);
    }
}
