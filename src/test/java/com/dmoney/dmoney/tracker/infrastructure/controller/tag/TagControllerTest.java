package com.dmoney.dmoney.tracker.infrastructure.controller.tag;

import com.dmoney.dmoney.tracker.application.tag.result.TagResponse;
import com.dmoney.dmoney.tracker.application.tag.usecase.CreateTagUseCase;
import com.dmoney.dmoney.tracker.application.tag.usecase.DeleteTagUseCase;
import com.dmoney.dmoney.tracker.application.tag.usecase.EditTagUseCase;
import com.dmoney.dmoney.tracker.application.tag.usecase.FindAllTagsUseCase;
import com.dmoney.dmoney.tracker.application.tag.usecase.FindTagByIdUseCase;
import com.dmoney.dmoney.shared.domain.exceptions.ResourceAlreadyExistsException;
import com.dmoney.dmoney.shared.domain.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TagController.class)
public class TagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private  CreateTagUseCase createTagUseCase;

    @MockBean
    private  FindAllTagsUseCase findAllTagsUseCase;

    @MockBean
    private  FindTagByIdUseCase findTagById;

    @MockBean
    private  EditTagUseCase editTagUseCase;

    @MockBean
    private  DeleteTagUseCase deleteTagUseCase;

    @Nested
    class GetAllControllerTest{
        @Test
        void should_get_all_tags() throws Exception{
            TagResponse tag1 = new TagResponse(
                    UUID.randomUUID().toString(),
                    "Name1",
                    "Description1"
            );
            TagResponse tag2 = new TagResponse(
                    UUID.randomUUID().toString(),
                    "Name2",
                    "Description2"
            );
            List<TagResponse> tagList = List.of(tag1,tag2);

            when(findAllTagsUseCase.execute())
                    .thenReturn(tagList);

            mockMvc.perform(get("/api/tags"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].id").value(tag1.id()))
                    .andExpect(jsonPath("$[0].name").value(tag1.name()))
                    .andExpect(jsonPath("$[1].id").value(tag2.id()))
                    .andExpect(jsonPath("$[1].name").value(tag2.name()));

            verify(findAllTagsUseCase).execute();
        }

        @Test
        void should_get_empty_array_when_there_are_no_tags() throws Exception{
            List<TagResponse> tagList = List.of();

            when(findAllTagsUseCase.execute())
                    .thenReturn(tagList);

            mockMvc.perform(get("/api/tags"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(0));


            verify(findAllTagsUseCase).execute();
        }
    }

    @Nested
    class FindTagByIdControllerTest{

        @Test
        void should_return_found_tag() throws Exception{
            String id = "1";
            TagResponse founded = new TagResponse(
                    id,
                    "Name",
                    "Description"
            );

            when(findTagById.execute(id))
                    .thenReturn(founded);

            mockMvc.perform(get("/api/tags/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value("1"))
                    .andExpect(jsonPath("$.name").value("Name"))
                    .andExpect(jsonPath("$.description").value("Description"));
        }

        @Test
        void should_return_404_when_ag_not_found() throws Exception{
            String id = "1";
            TagResponse founded = new TagResponse(
                    id,
                    "Name",
                    "Description"
            );

            when(findTagById.execute(id))
                    .thenThrow(new ResourceNotFoundException("Tag", "id", id));

            mockMvc.perform(get("/api/tags/1"))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    class CreateTagControllerTest{
        @Test
        void should_create_the_tag() throws Exception{
            String json = """
                        {
                            "name": "Name",
                            "description": "Description"
                        }
                    """;

            TagResponse response = new TagResponse(
                    "1",
                    "Name",
                    "Description"

            );

            when(createTagUseCase.execute(any()))
                    .thenReturn(response);

            mockMvc.perform(post("/api/tags")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value("1"))
                    .andExpect(jsonPath("$.name").value("Name"))
                    .andExpect(jsonPath("$.description").value("Description"));

            verify(createTagUseCase).execute(argThat(command ->
                    command.name().equals("Name") && command.description().equals("Description")
            ));
        }

        @Test
        void should_return_409_when_tag_name_already_exists() throws Exception{
            String json = """
                        {
                            "name": "Existing Name",
                            "description": "Description"
                        }
                    """;


            when(createTagUseCase.execute(any()))
                    .thenThrow(new ResourceAlreadyExistsException("Tag", "Name", "Existing name"));

            mockMvc.perform(post("/api/tags")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.status").value(409))
                    .andExpect(jsonPath("$.message").exists());

            verify(createTagUseCase).execute(argThat(command ->
                    command.name().equals("Existing Name") && command.description().equals("Description")
            ));
        }

        @Test
        void should_return_400_when_name_is_blank() throws Exception{
            String json = """
                        {
                            "name": "",
                            "description": "Description"
                        }
                    """;

            mockMvc.perform(post("/api/tags")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.error").value("Bad Request"))
                    .andExpect(jsonPath("$.message").exists());
        }

        @Test
        void should_return_400_when_name_is_null()throws Exception{
            String json = """
                        {
                            "name": null,
                            "description": "Description"
                        }
                    """;

            mockMvc.perform(post("/api/tags")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.error").value("Bad Request"))
                    .andExpect(jsonPath("$.message").exists());
        }

        @Test
        void should_return_400_when_malformed_json()throws Exception{
            String json = """
                        {
                            "name": "Any"
                            "description": "Description"
                        }
                    """;

            mockMvc.perform(post("/api/tags")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.error").value("Bad Request"))
                    .andExpect(jsonPath("$.message").exists());
        }
    }

    @Nested
    class EditTagControllerTest{

        @Test
        void should_edit_a_tag() throws Exception{
            String json = """
                    {
                        "name": "New Name",
                        "description": "New Description"
                    }
                    """;

            TagResponse response = new TagResponse(
                    "1", "New Name", "New Description"
            );

            when(editTagUseCase.execute(any()))
                    .thenReturn(response);

            mockMvc.perform(patch("/api/tags/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value("1"))
                    .andExpect(jsonPath("$.name").value("New Name"))
                    .andExpect(jsonPath("$.description").value("New Description"));

            verify(editTagUseCase).execute(argThat(command ->
                    command.name().equals("New Name") &&
                    command.description().equals("New Description") &&
                    command.id().equals("1")));
        }

        @Test
        void should_return_409_when_tag_name_already_exists() throws Exception{
            String json = """
                    {
                        "name": "Existing Name",
                        "description": "New Description"
                    }
                    """;

            when(editTagUseCase.execute(any()))
                    .thenThrow(new ResourceAlreadyExistsException("Tag", "name", "Existing Name"));

            mockMvc.perform(patch("/api/tags/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.status").value(409))
                    .andExpect(jsonPath("$.message").exists());

            verify(editTagUseCase).execute(argThat(command ->
                    command.name().equals("Existing Name") &&
                            command.description().equals("New Description") &&
                            command.id().equals("1")));
        }

        @Test
        void should_return_404_when_tag_id_is_not_found() throws Exception{
            String json = """
                    {
                        "name": "New Name",
                        "description": "New Description"
                    }
                    """;

            when(editTagUseCase.execute(any()))
                    .thenThrow(new ResourceNotFoundException("Tag", "id", "1"));

            mockMvc.perform(patch("/api/tags/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value(404))
                    .andExpect(jsonPath("$.message").exists());

            verify(editTagUseCase).execute(argThat(command ->
                    command.name().equals("New Name") &&
                            command.description().equals("New Description") &&
                            command.id().equals("1")));
        }

        @Test
        void should_return_400_when_name_is_blank() throws Exception{
            String json = """
                    {
                        "name": "",
                        "description": "New Description"
                    }
                    """;

            mockMvc.perform(patch("/api/tags/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.message").exists());
        }

        @Test
        void should_return_400_when_json_is_malformed() throws Exception{
            String json = """
                    {
                        "name": "Malformed json"
                        "description": "New Description"
                    }
                    """;

            mockMvc.perform(patch("/api/tags/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.message").exists());
        }
    }

    @Nested
    class DeleteTagControllerTest{

        @Test
        void should_delete_tag() throws Exception{
            mockMvc.perform(delete("/api/tags/1"))
                    .andExpect(status().isNoContent());

            verify(deleteTagUseCase).execute("1");
        }

        @Test
        void should_return_404_when_tag_is_not_found() throws Exception{
            doThrow(new ResourceNotFoundException("Tag", "id", "1"))
                    .when(deleteTagUseCase)
                    .execute("1");

            mockMvc.perform(delete("/api/tags/1"))
                    .andExpect(status().isNotFound());

            verify(deleteTagUseCase).execute("1");
        }
    }
}
