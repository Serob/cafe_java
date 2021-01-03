package com.serob.cafe;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.serob.cafe.entities.User;
import com.serob.cafe.repositories.UserRepository;
import com.serob.cafe.utils.UserRole;

import java.util.Optional;

import static org.mockito.Mockito.when;

public class UtilMethods {
    static String objectToRequestJson(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(obj);
        return requestJson;
    }

    /**
     * Represents a user who creates another user.
     */
    public static User mockCreatorUser(Long id, UserRole role, UserRepository repository){
        User user = new User();
        user.setRole(role);
        if (repository != null) {
            when(repository.findById(id)).thenReturn(Optional.of(user));
//            when(roleService.mustBeManager(id)).thenReturn()
        }
        return user;
    }
}
