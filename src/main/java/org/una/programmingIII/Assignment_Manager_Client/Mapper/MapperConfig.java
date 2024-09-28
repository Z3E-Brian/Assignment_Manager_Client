package org.una.programmingIII.Assignment_Manager_Client.Mapper;

import org.modelmapper.ModelMapper;


public class MapperConfig<E, D> {

    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
