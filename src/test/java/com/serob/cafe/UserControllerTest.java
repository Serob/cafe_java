package com.serob.cafe;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.serob.cafe.controllers.api.UserController;
import com.serob.cafe.entities.User;
import com.serob.cafe.repositories.UserRepository;
import com.serob.cafe.utils.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository repository;

    /**
     * Represents a user who is created and saved
     * @return String json representation of the User
     * @throws JsonProcessingException
     */
    private String mockRequestedUser(String email, UserRole role, String name, String password, UserRepository repository) throws JsonProcessingException {
        User user = new User();
        user.setEmail(email);
        user.setRole(role);
        user.setName(name);
        user.setPassword(password);

        //Mock repository save method
        if (repository != null) {
            when(repository.save(any(User.class))).thenReturn(user);
        }

        return UtilMethods.objectToRequestJson(user);
    }

    @Test
    public void testUserCreation() throws Exception {
        final String CREATE_URL = "/api/users";
        final Long MANAGER_ID = 1L;
        final Long WAITER_ID = 2L;

        //A user hwo can create another user
        UtilMethods.mockCreatorUser(MANAGER_ID, UserRole.MANAGER, repository);

        //A user hwo can NOT create another user
        UtilMethods.mockCreatorUser(WAITER_ID, UserRole.WAITER, repository);

        //Define expected object
        String wrongRequestJson = mockRequestedUser("mail.com", UserRole.WAITER, null, null, repository);

        //check expectations
        //Manager can not create wrong entity
        this.mockMvc.perform(post(CREATE_URL).contentType(APPLICATION_JSON_UTF8).content(wrongRequestJson)
                .param("manager_id", MANAGER_ID.toString()))
                .andExpect(status().isBadRequest());

        String correctRequestJson = mockRequestedUser("asd@mail.com", UserRole.WAITER, "testName", "testPass", repository);

        //Manager can create valid entity
        this.mockMvc.perform(post(CREATE_URL).contentType(APPLICATION_JSON_UTF8).content(correctRequestJson)
                .param("manager_id", MANAGER_ID.toString()))
                .andExpect(status().isCreated())
                .andExpect(content().json(correctRequestJson));

        //Waiter can NOT create valid entity
        this.mockMvc.perform(post(CREATE_URL).contentType(APPLICATION_JSON_UTF8).content(correctRequestJson)
                .param("manager_id", WAITER_ID.toString()))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testLogin() throws Exception {
        final String LOGIN_URL = "/api/users/login";
        final String ACTUAL_EMAIL = "test@gmail.com";
        final String ACTUAL_PASSWORD = "testPass";

        User actualUser = new User();
        actualUser.setEmail(ACTUAL_EMAIL);
        actualUser.setPassword(ACTUAL_PASSWORD);
        when(repository.findByEmail(ACTUAL_EMAIL)).thenReturn(actualUser);


        String wrongPassRequestJson = mockRequestedUser(ACTUAL_EMAIL, UserRole.MANAGER, null, "wrongPass", null);
        this.mockMvc.perform(post(LOGIN_URL).contentType(APPLICATION_JSON_UTF8).content(wrongPassRequestJson))
                .andExpect(status().isUnauthorized());

        String wrongEmailRequestJson = mockRequestedUser("wrong@mail.com", UserRole.MANAGER, null,  ACTUAL_PASSWORD, null);
        this.mockMvc.perform(post(LOGIN_URL).contentType(APPLICATION_JSON_UTF8).content(wrongEmailRequestJson))
                .andExpect(status().isUnauthorized());

        String correctRequestJson = mockRequestedUser(ACTUAL_EMAIL, UserRole.MANAGER, null,  ACTUAL_PASSWORD, null);
        this.mockMvc.perform(post(LOGIN_URL).contentType(APPLICATION_JSON_UTF8).content(correctRequestJson))
                .andExpect(status().isOk());
    }
}
