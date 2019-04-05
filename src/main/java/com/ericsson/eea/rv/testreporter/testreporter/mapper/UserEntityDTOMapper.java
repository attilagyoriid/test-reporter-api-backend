package com.ericsson.eea.rv.testreporter.testreporter.mapper;

import com.ericsson.eea.rv.testreporter.testreporter.domain.User;
import com.ericsson.eea.rv.testreporter.testreporter.model.UserAccountDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserEntityDTOMapper {
    UserEntityDTOMapper INSTANCE = Mappers.getMapper(UserEntityDTOMapper.class);
    @Mapping(target="image", source="userEntity.image")
    UserAccountDTO userEntityToUserAccountDTO(User userEntity);
    User userAccountDTOToUserEntity(UserAccountDTO userAccountDTO);
}
