package com.serob.cafe;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.serob.cafe.controllers.api.TableController;
import com.serob.cafe.entities.CafeTable;
import com.serob.cafe.entities.User;
import com.serob.cafe.repositories.TableRepository;
import com.serob.cafe.repositories.UserRepository;
import com.serob.cafe.services.RoleUserService;
import com.serob.cafe.utils.UserRole;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

@WebMvcTest(TableController.class)
public class TableControllerTest {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TableRepository tableRepo;

    @MockBean
    private RoleUserService roleService;

    @MockBean
    private UserRepository userRepo;

    private String mockRequestedTable(int number, User manager, TableRepository tableRepo) throws JsonProcessingException {
        CafeTable table = new CafeTable();
        table.setNumber(number);

        if (manager != null){
            table.setCreatedBy(manager);
        }

        //Mock repository save method
        if (tableRepo != null) {
            when(tableRepo.save(any(CafeTable.class))).thenReturn(table);
        }

        return UtilMethods.objectToRequestJson(table);
    }

    @Test
    public void testCreateTable() throws Exception {
        final String CREATE_URL = "/api/tables";
        ResponseEntity forbiddenResponse = ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        String correctRequestJson = mockRequestedTable(20, null, tableRepo);

        //not manager can not create a table
        when(roleService.mustBeManager(ArgumentMatchers.<Optional<User>>any())).thenReturn(forbiddenResponse);

        this.mockMvc.perform(post(CREATE_URL).contentType(APPLICATION_JSON_UTF8).content(correctRequestJson)
                .param("manager_id", "555"))
                .andExpect(status().isForbidden());

        //manager can create
        final Long MANAGER_ID = 1L;
        UtilMethods.mockCreatorUser(MANAGER_ID, UserRole.MANAGER, userRepo);
        when(roleService.mustBeManager(ArgumentMatchers.<Optional<User>>any())).thenReturn(null);

        this.mockMvc.perform(post(CREATE_URL).contentType(APPLICATION_JSON_UTF8).content(correctRequestJson)
                .param("manager_id", MANAGER_ID.toString()))
                .andExpect(status().isCreated())
                .andExpect(content().json(correctRequestJson));

    }
}
