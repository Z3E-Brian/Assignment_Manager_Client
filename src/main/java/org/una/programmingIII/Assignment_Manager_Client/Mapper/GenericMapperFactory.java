package org.una.programmingIII.Assignment_Manager_Client.Mapper;

import org.modelmapper.ModelMapper;

public class GenericMapperFactory {

    private final ModelMapper modelMapper;
    public GenericMapperFactory() {
        this.modelMapper = new ModelMapper();
    }

    public <E, D> GenericMapper<E, D> createMapper(Class<E> entityClass, Class<D> dtoClass) {
        return new GenericMapperImplementation<>(entityClass, dtoClass, modelMapper);
    }
}
