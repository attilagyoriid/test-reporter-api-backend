package com.ericsson.eea.rv.testreporter.testreporter.mapper;

import com.ericsson.eea.rv.testreporter.testreporter.domain.User;
import com.ericsson.eea.rv.testreporter.testreporter.model.UserAccountDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserEntityDTOMapper {
    UserEntityDTOMapper INSTANCE = Mappers.getMapper(UserEntityDTOMapper.class);

    UserAccountDTO userEntityToUserAccountDTO(User userEntity);
    User userAccountDTOToUserEntity(UserAccountDTO userAccountDTO);
}
