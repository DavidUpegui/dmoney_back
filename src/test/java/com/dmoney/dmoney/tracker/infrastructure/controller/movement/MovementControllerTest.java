package com.dmoney.dmoney.tracker.infrastructure.controller.movement;

import com.dmoney.dmoney.auth.infrastructure.security.JwtAuthenticationFilter;
import com.dmoney.dmoney.auth.infrastructure.security.JwtTokenParser;
import com.dmoney.dmoney.shared.domain.exceptions.ResourceNotFoundException;
import com.dmoney.dmoney.tracker.application.movement.response.MovementResponse;
import com.dmoney.dmoney.tracker.application.movement.usecase.CreateMovementUseCase;
import com.dmoney.dmoney.tracker.application.movement.usecase.DeleteMovementUseCase;
import com.dmoney.dmoney.tracker.application.movement.usecase.EditMovementUseCase;
import com.dmoney.dmoney.tracker.application.movement.usecase.FindAllMovementsUseCase;
import com.dmoney.dmoney.tracker.application.movement.usecase.FindMovementByIdUseCase;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MovementController.class)
@AutoConfigureMockMvc(addFilters = false)
class MovementControllerTest {

    private static final String CATEGORY_ID = "20000000-0000-0000-0000-000000000001";
    private static final String SUBCATEGORY_ID = "30000000-0000-0000-0000-000000000001";
    private static final String MOVEMENT_ID = "50000000-0000-0000-0000-000000000001";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private JwtTokenParser jwtTokenParser;

    @MockBean
    private CreateMovementUseCase createMovementUseCase;

    @MockBean
    private FindAllMovementsUseCase findAllMovementsUseCase;

    @MockBean
    private FindMovementByIdUseCase findMovementByIdUseCase;

    @MockBean
    private EditMovementUseCase editMovementUseCase;

    @MockBean
    private DeleteMovementUseCase deleteMovementUseCase;

    private MovementResponse response(){
        return new MovementResponse(
                MOVEMENT_ID,
                CATEGORY_ID,
                SUBCATEGORY_ID,
                new BigDecimal("85.50"),
                "OUTCOME",
                "Arriendo",
                LocalDate.of(2026, 7, 14),
                List.of()
        );
    }

    @Nested
    class CreateMovementControllerTest {
        @Test
        void should_create_movement_and_return_201() throws Exception {
            String json = """
                    {
                        "categoryId": "%s",
                        "subcategoryId": "%s",
                        "amount": 85.50,
                        "type": "OUTCOME",
                        "description": "Arriendo",
                        "date": "2026-07-14",
                        "tags": []
                    }
                    """.formatted(CATEGORY_ID, SUBCATEGORY_ID);

            when(createMovementUseCase.execute(any())).thenReturn(response());

            mockMvc.perform(post("/api/movements")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isCreated())
                    .andExpect(header().exists("Location"))
                    .andExpect(jsonPath("$.movementId").value(MOVEMENT_ID))
                    .andExpect(jsonPath("$.amount").value(85.50))
                    .andExpect(jsonPath("$.type").value("OUTCOME"));

            verify(createMovementUseCase).execute(any());
        }

        @Test
        void should_return_400_when_amount_is_not_positive() throws Exception {
            String json = """
                    {
                        "categoryId": "%s",
                        "subcategoryId": "%s",
                        "amount": 0,
                        "type": "OUTCOME",
                        "description": "Arriendo",
                        "date": "2026-07-14",
                        "tags": []
                    }
                    """.formatted(CATEGORY_ID, SUBCATEGORY_ID);

            mockMvc.perform(post("/api/movements")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void should_return_400_when_required_fields_missing() throws Exception {
            String json = """
                    {
                        "amount": 85.50
                    }
                    """;

            mockMvc.perform(post("/api/movements")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class FindAllMovementsControllerTest {
        @Test
        void should_return_all_movements() throws Exception {
            when(findAllMovementsUseCase.execute()).thenReturn(List.of(response()));

            mockMvc.perform(get("/api/movements"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(1))
                    .andExpect(jsonPath("$[0].movementId").value(MOVEMENT_ID));

            verify(findAllMovementsUseCase).execute();
        }
    }

    @Nested
    class FindMovementByIdControllerTest {
        @Test
        void should_return_movement_when_found() throws Exception {
            when(findMovementByIdUseCase.execute(MOVEMENT_ID)).thenReturn(response());

            mockMvc.perform(get("/api/movements/" + MOVEMENT_ID))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.movementId").value(MOVEMENT_ID));

            verify(findMovementByIdUseCase).execute(MOVEMENT_ID);
        }

        @Test
        void should_return_404_when_movement_not_found() throws Exception {
            when(findMovementByIdUseCase.execute(MOVEMENT_ID))
                    .thenThrow(new ResourceNotFoundException("Movement", "id", MOVEMENT_ID));

            mockMvc.perform(get("/api/movements/" + MOVEMENT_ID))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    class EditMovementControllerTest {
        @Test
        void should_edit_movement() throws Exception {
            String json = """
                    {
                        "amount": 99.99
                    }
                    """;

            when(editMovementUseCase.execute(any())).thenReturn(response());

            mockMvc.perform(patch("/api/movements/" + MOVEMENT_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.movementId").value(MOVEMENT_ID));

            verify(editMovementUseCase).execute(argThat(command ->
                    command.id().equals(MOVEMENT_ID) &&
                            command.amount().equals(new BigDecimal("99.99"))
            ));
        }

        @Test
        void should_return_404_when_movement_not_found() throws Exception {
            String json = """
                    {
                        "amount": 99.99
                    }
                    """;

            when(editMovementUseCase.execute(any()))
                    .thenThrow(new ResourceNotFoundException("Movement", "id", MOVEMENT_ID));

            mockMvc.perform(patch("/api/movements/" + MOVEMENT_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isNotFound());
        }

        @Test
        void should_return_400_when_amount_is_not_positive() throws Exception {
            String json = """
                    {
                        "amount": -5
                    }
                    """;

            mockMvc.perform(patch("/api/movements/" + MOVEMENT_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class DeleteMovementControllerTest {
        @Test
        void should_delete_movement() throws Exception {
            mockMvc.perform(delete("/api/movements/" + MOVEMENT_ID))
                    .andExpect(status().isNoContent());

            verify(deleteMovementUseCase).execute(MOVEMENT_ID);
        }

        @Test
        void should_return_404_when_movement_not_found() throws Exception {
            doThrow(new ResourceNotFoundException("Movement", "id", MOVEMENT_ID))
                    .when(deleteMovementUseCase).execute(MOVEMENT_ID);

            mockMvc.perform(delete("/api/movements/" + MOVEMENT_ID))
                    .andExpect(status().isNotFound());
        }
    }
}
