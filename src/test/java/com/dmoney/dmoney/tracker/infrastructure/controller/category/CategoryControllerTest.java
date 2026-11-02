package com.dmoney.dmoney.tracker.infrastructure.controller.category;

import com.dmoney.dmoney.auth.infrastructure.security.JwtAuthenticationFilter;
import com.dmoney.dmoney.auth.infrastructure.security.JwtTokenParser;
import com.dmoney.dmoney.shared.domain.exceptions.ValidationException;
import com.dmoney.dmoney.tracker.application.category.result.CategoryResult;
import com.dmoney.dmoney.tracker.application.category.result.SubcategoryResult;
import com.dmoney.dmoney.shared.domain.exceptions.ResourceAlreadyExistsException;
import com.dmoney.dmoney.shared.domain.exceptions.ResourceInUseException;
import com.dmoney.dmoney.shared.domain.exceptions.ResourceNotFoundException;
import com.dmoney.dmoney.tracker.application.category.usecase.*;
import org.junit.jupiter.api.Nested;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(CategoryController.class)
@AutoConfigureMockMvc(addFilters = false)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private JwtTokenParser jwtTokenParser;

    @MockBean
    private CreateCategoryUseCase createCategoryUseCase;

    @MockBean
    private FindAllCategoriesUseCase findAllCategoriesUseCase;

    @MockBean
    private FindCategoryByIdUseCase findCategoryByIdUseCase;

    @MockBean
    private DeleteCategoryUseCase deleteCategoryUseCase;

    @MockBean
    private AddSubcategoryUseCase addSubcategoryUseCase;

    @MockBean
    private EditCategoryUseCase editCategoryUseCase;

    @MockBean
    private EditSubcategoryUseCase editSubcategoryUseCase;

    @MockBean
    private FindAllSubcategoriesByCategoryIdUseCase findAllSubcategoriesByCategoryIdUseCase;

    @MockBean
    private DeleteSubcategoryUseCase deleteSubcategoryUseCase;

    @Nested
    class CreateCategoryControllerTests {
        @Test
        void cat01_returns_201() throws Exception {

            String json = """
                    {
                     "name": "Tech",
                     "description": "Tech description",
                     "type": "INCOME"
                    }
                    """;

            CategoryResult result =
                    new CategoryResult("1", "Tech", "Tech description", "INCOME",
                        null,
                        null);

            when(createCategoryUseCase.execute(any()))
                    .thenReturn(result);

            mockMvc.perform(post("/api/categories")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.name").value("Tech"))
                    .andExpect(jsonPath("$.description").value("Tech description"))
                    .andExpect(jsonPath("$.type").value("INCOME"));

            verify(createCategoryUseCase).execute(argThat(command ->
                    command.name().equals("Tech") &&
                            command.description().equals("Tech description")
            ));
        }

        @Test
        void cat02_rejects_blank_name() throws Exception {

            String json = """
                    {
                        "name": "",
                        "description": "Description",
                        "type": "INCOME"
                    }
                    """;

            mockMvc.perform(post("/api/categories")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.error").value("Bad Request"))
                    .andExpect(jsonPath("$.message").exists());
        }

        @Test
        void cat04_rejects_blank_type() throws Exception {

            String json = """
                    {
                        "name": "",
                        "description": "Description",
                        "type": ""
                    }
                    """;

            mockMvc.perform(post("/api/categories")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.error").value("Bad Request"))
                    .andExpect(jsonPath("$.message").exists());
        }

        @Test
        void should_return_400_when_json_is_malformed() throws Exception {

            String json = """
                    {
                        "name": "Tech"
                        "description": "test",
                        "type": "INCOME"
                    }
                    """;

            mockMvc.perform(post("/api/categories")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.message").value("Malformed JSON request"));
        }

        @Test
        void cat05_returns_409() throws Exception {

            String json = """
                    {
                        "name": "Books",
                        "description": "Category for books",
                        "type": "INCOME"
                    }
                    """;

            when(createCategoryUseCase.execute(any()))
                    .thenThrow(new ResourceAlreadyExistsException(
                            "Category",
                            "Name",
                            "Books"
                    ));

            mockMvc.perform(post("/api/categories")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isConflict());
        }

        @Test
        void cat02_rejects_missing_name() throws Exception {

            String json = """
                    {
                        "description": "Description",
                        "type": "INCOME"
                    }
                    """;

            mockMvc.perform(post("/api/categories")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class FindAllCategoriesControllerTests{
        @Test
        void cat06_returns_categories() throws Exception {

            List<CategoryResult> result = List.of(
                    new CategoryResult("1", "Tech", "Technology", "INCOME",
                        null,
                        null),
                    new CategoryResult("2", "Books", "Books category", "OUTCOME",
                        null,
                        null)
            );

            when(findAllCategoriesUseCase.execute())
                    .thenReturn(result);

            mockMvc.perform(get("/api/categories"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(2))
                    .andExpect(jsonPath("$[0].name").value("Tech"))
                    .andExpect(jsonPath("$[1].name").value("Books"));

            verify(findAllCategoriesUseCase).execute();
        }

        @Test
        void cat06_returns_empty_array() throws Exception {

            when(findAllCategoriesUseCase.execute())
                    .thenReturn(List.of());

            mockMvc.perform(get("/api/categories"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(0));

            verify(findAllCategoriesUseCase).execute();
        }

        @Test
        void should_return_500_when_unexpected_error_happens() throws Exception {

            when(findAllCategoriesUseCase.execute())
                    .thenThrow(new RuntimeException());

            mockMvc.perform(get("/api/categories"))
                    .andExpect(status().isInternalServerError());
        }
    }

    @Nested
    class findCategoryByIdControllerTests{
        @Test
        void cat07_returns_category() throws Exception{
            CategoryResult categoryResult =  new CategoryResult(
                    "1", "Tech", "Technology", "OUTCOME",
                    null,
                    null
            );

            when(findCategoryByIdUseCase.execute("1"))
                    .thenReturn(categoryResult);

            mockMvc.perform(get("/api/categories/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value("1"))
                    .andExpect(jsonPath("$.name").value("Tech"));

            verify(findCategoryByIdUseCase).execute("1");
        }

        @Test
        void cat07_returns_404() throws Exception{
            String anyId = "123";
            when(findCategoryByIdUseCase.execute(anyId))
                    .thenThrow(new ResourceNotFoundException(
                            "Category",
                            "id",
                            anyId
                    ));

            mockMvc.perform(get("/api/categories/123"))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    class DeleteCategoryControllerTests{
        @Test
        void cat09_returns_204() throws Exception{
            mockMvc.perform(delete("/api/categories/1"))
                    .andExpect(status().isNoContent());

            verify(deleteCategoryUseCase).execute("1");
        }

        @Test
        void cat09_returns_404() throws Exception{
            doThrow(new ResourceNotFoundException("Category", "id", "1"))
                    .when(deleteCategoryUseCase)
                    .execute("1");
            mockMvc.perform(delete("/api/categories/1"))
                    .andExpect(status().isNotFound());

            verify(deleteCategoryUseCase).execute("1");
        }

        @Test
        void cat11_returns_409_when_the_category_has_movements() throws Exception{
            doThrow(new ResourceInUseException("Category", "it still has movements"))
                    .when(deleteCategoryUseCase)
                    .execute("1");

            mockMvc.perform(delete("/api/categories/1"))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.status").value(409));
        }

        @Test
        void should_return_500_when_unexpected_error_occurs() throws Exception {

            doThrow(new RuntimeException("Unexpected error"))
                    .when(deleteCategoryUseCase)
                    .execute("1");

            mockMvc.perform(delete("/api/categories/1"))
                    .andExpect(status().isInternalServerError());
        }
    }

    @Nested
    class CreateSubcategoryControllerTests{
        @Test
        void cat12_returns_201() throws Exception {
            String json = """
                        {
                            "name": "Name",
                            "description": "Description"
                        }
                    """;
            SubcategoryResult subcategoryResult = new SubcategoryResult(
                    "1",
                    "2",
                    "Name",
                    "Description",
                    null,
                    null
            );

            when(addSubcategoryUseCase.execute(any()))
                    .thenReturn(subcategoryResult);

            mockMvc.perform(post("/api/categories/1/subcategories")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.categoryId").value("1"))
                    .andExpect(jsonPath("$.subcategoryId").value("2"))
                    .andExpect(jsonPath("$.subcategoryName").value("Name"))
                    .andExpect(jsonPath("$.subcategoryDescription").value("Description"));

            verify(addSubcategoryUseCase).execute(argThat(command ->
                    command.categoryId().equals("1") &&
                            command.name().equals("Name") &&
                            command.description().equals("Description")
            ));
        }

        @Test
        void cat12_rejects_missing_name() throws Exception{
            String json = """
                        {
                            "description": "Description"
                        }
                    """;

            mockMvc.perform(post("/api/categories/1/subcategories")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void should_return_400_when_malformed_json_request() throws Exception{
            String json = """
                        {
                            "name": "Name"
                            "description": "Description"
                        }
                    """;

            mockMvc.perform(post("/api/categories/1/subcategories")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void cat17_rejects_foreign_category_on_subcategory_create() throws Exception {
            String json = """
                        {
                            "name": "Name",
                            "description": "Description"
                        }
                    """;

            when(addSubcategoryUseCase.execute(any()))
                    .thenThrow(new ResourceNotFoundException("Category", "id", "1"));

            mockMvc.perform(post("/api/categories/1/subcategories")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isNotFound());

            verify(addSubcategoryUseCase).execute(argThat(command ->
                    command.categoryId().equals("1") &&
                            command.name().equals("Name") &&
                            command.description().equals("Description")
            ));
        }

        @Test
        void cat13_returns_409() throws Exception {
            String json = """
                        {
                            "name": "Name",
                            "description": "Description"
                        }
                    """;

            when(addSubcategoryUseCase.execute(any()))
                    .thenThrow(new ResourceAlreadyExistsException("Subcategory", "id", "1"));

            mockMvc.perform(post("/api/categories/1/subcategories")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isConflict());

            verify(addSubcategoryUseCase).execute(argThat(command ->
                    command.categoryId().equals("1") &&
                            command.name().equals("Name") &&
                            command.description().equals("Description")
            ));
        }

        @Test
        void should_return_500_when_unexpected_error() throws Exception{
            String json = """
                        {
                            "name": "Name",
                            "description": "Description"
                        }
                    """;

            when(addSubcategoryUseCase.execute(any()))
                    .thenThrow(new RuntimeException("Unexpected error"));

            mockMvc.perform(post("/api/categories/1/subcategories")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isInternalServerError());

            verify(addSubcategoryUseCase).execute(argThat(command ->
                    command.categoryId().equals("1") &&
                            command.name().equals("Name") &&
                            command.description().equals("Description")
            ));
        }
     }

     @Nested
    class EditCategoryControllerTests{
        @Test
         void cat08_returns_updated_category() throws Exception{
            String json = """
                        {
                            "name": "Changed Name",
                            "description": "Changed Description",
                            "type": "INCOME"
                        }
                    """;

            CategoryResult result = new CategoryResult(
                    "1",
                    "Changed Name",
                    "Changed Description",
                    "INCOME",
                    null,
                    null
            );

            when(editCategoryUseCase.execute(any()))
                    .thenReturn(result);

            mockMvc.perform(patch("/api/categories/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value("1"))
                    .andExpect(jsonPath("$.name").value("Changed Name"))
                    .andExpect(jsonPath("$.description").value("Changed Description"));

            verify(editCategoryUseCase).execute(argThat(command ->
                command.id().equals("1") &&
                        command.name().equals("Changed Name") &&
                        command.description().equals("Changed Description")
            ));
        }

         @Test
         void cat08_updates_only_description() throws Exception{
             String json = """
                        {
                            "description": "Changed Description",
                            "type": "INCOME"
                        }
                    """;

             CategoryResult result = new CategoryResult(
                     "1",
                     "Name",
                     "Changed Description",
                     "INCOME",
                    null,
                    null);

             when(editCategoryUseCase.execute(any()))
                     .thenReturn(result);

             mockMvc.perform(patch("/api/categories/1")
                             .contentType(MediaType.APPLICATION_JSON)
                             .content(json))
                     .andExpect(status().isOk())
                     .andExpect(jsonPath("$.id").value("1"))
                     .andExpect(jsonPath("$.name").value("Name"))
                     .andExpect(jsonPath("$.description").value("Changed Description"))
                     .andExpect(jsonPath("$.type").value("INCOME"));

             verify(editCategoryUseCase).execute(argThat(command ->
                     command.id().equals("1") &&
                             command.name() == null &&
                             command.description().equals("Changed Description")
             ));
         }

         @Test
         void cat08_rejects_blank_name() throws Exception{
             String json = """
                        {
                            "name": "",
                            "description": "Changed Description"
                        }
                    """;

             mockMvc.perform(patch("/api/categories/1")
                             .contentType(MediaType.APPLICATION_JSON)
                             .content(json))
                     .andExpect(status().isBadRequest());
         }

         @Test
         void should_return_400_when_json_is_malformed() throws Exception{
             String json = """
                        {
                            "name": ""
                            "description": "Changed Description"
                        }
                    """;

             mockMvc.perform(patch("/api/categories/1")
                             .contentType(MediaType.APPLICATION_JSON)
                             .content(json))
                     .andExpect(status().isBadRequest());
         }

         @Test
         void cat08_returns_404() throws Exception{
             String json = """
                        {
                            "name": "Changed Name",
                            "description": "Changed Description"
                        }
                    """;

             when(editCategoryUseCase.execute(any()))
                     .thenThrow(new ResourceNotFoundException("Category", "id", "1"));

             mockMvc.perform(patch("/api/categories/1")
                             .contentType(MediaType.APPLICATION_JSON)
                             .content(json))
                     .andExpect(status().isNotFound());

             verify(editCategoryUseCase).execute(argThat(command ->
                     command.id().equals("1") &&
                     command.name().equals("Changed Name") &&
                             command.description().equals("Changed Description")
             ));
         }

         @Test
         void cat05_returns_409_on_edit() throws Exception{
             String json = """
                        {
                            "name": "Changed Name",
                            "description": "Changed Description"
                        }
                    """;

             when(editCategoryUseCase.execute(any()))
                     .thenThrow(new ResourceAlreadyExistsException("Category", "id", "1"));

             mockMvc.perform(patch("/api/categories/1")
                             .contentType(MediaType.APPLICATION_JSON)
                             .content(json))
                     .andExpect(status().isConflict());

             verify(editCategoryUseCase).execute(argThat(command ->
                     command.id().equals("1") &&
                             command.name().equals("Changed Name") &&
                             command.description().equals("Changed Description")
             ));
         }

         @Test
         void should_return_500_when_unexpected_error() throws Exception{
             String json = """
                        {
                            "name": "Changed Name",
                            "description": "Changed Description"
                        }
                    """;

             when(editCategoryUseCase.execute(any()))
                     .thenThrow(new RuntimeException("Unexpected error"));

             mockMvc.perform(patch("/api/categories/1")
                             .contentType(MediaType.APPLICATION_JSON)
                             .content(json))
                     .andExpect(status().isInternalServerError());

             verify(editCategoryUseCase).execute(argThat(command ->
                     command.id().equals("1") &&
                             command.name().equals("Changed Name") &&
                             command.description().equals("Changed Description")
             ));
         }
     }

     @Nested
    class EditSubcategoryControllerTests{
        @Test
         void cat15_returns_updated_subcategory() throws Exception{
            String json = """
                        {
                        "name": "Changed Name",
                        "description": "Changed Description"
                        }
                    """;

            SubcategoryResult result = new SubcategoryResult(
                    "1",
                    "2",
                    "Changed Name",
                    "Changed Description",
                    null,
                    null
            );

            when(editSubcategoryUseCase.execute(any()))
                    .thenReturn(result);
            mockMvc.perform(patch("/api/categories/1/subcategories/2")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.categoryId").value("1"))
                    .andExpect(jsonPath("$.subcategoryId").value("2"))
                    .andExpect(jsonPath("$.subcategoryName").value("Changed Name"))
                    .andExpect(jsonPath("$.subcategoryDescription").value("Changed Description"));

            verify(editSubcategoryUseCase).execute(argThat(command ->
                    command.categoryId().equals("1") &&
                    command.subcategoryId().equals("2") &&
                    command.subcategoryName().equals("Changed Name") &&
                    command.subcategoryDescription().equals("Changed Description")));
        }

         @Test
         void cat15_updates_only_description() throws Exception{
             String json = """
                        {
                        "description": "Changed Description"
                        }
                    """;

             SubcategoryResult result = new SubcategoryResult(
                     "1",
                     "2",
                     "Name",
                     "Changed Description",
                     null,
                     null
             );

             when(editSubcategoryUseCase.execute(any()))
                     .thenReturn(result);
             mockMvc.perform(patch("/api/categories/1/subcategories/2")
                             .contentType(MediaType.APPLICATION_JSON)
                             .content(json))
                     .andExpect(status().isOk())
                     .andExpect(jsonPath("$.categoryId").value("1"))
                     .andExpect(jsonPath("$.subcategoryId").value("2"))
                     .andExpect(jsonPath("$.subcategoryName").value("Name"))
                     .andExpect(jsonPath("$.subcategoryDescription").value("Changed Description"));

             verify(editSubcategoryUseCase).execute(argThat(command ->
                     command.categoryId().equals("1") &&
                             command.subcategoryId().equals("2") &&
                             command.subcategoryName() == null &&
                             command.subcategoryDescription().equals("Changed Description")));
         }

         @Test
         void cat17_rejects_foreign_category_on_subcategory_edit() throws Exception{
             String json = """
                        {
                        "name": "Changed Name",
                        "description": "Changed Description"
                        }
                    """;

             when(editSubcategoryUseCase.execute(any()))
                     .thenThrow(new ResourceNotFoundException("Category", "id", "1"));

             mockMvc.perform(patch("/api/categories/1/subcategories/2")
                             .contentType(MediaType.APPLICATION_JSON)
                             .content(json))
                     .andExpect(status().isNotFound());


             verify(editSubcategoryUseCase).execute(argThat(command ->
                     command.categoryId().equals("1") &&
                             command.subcategoryId().equals("2") &&
                             command.subcategoryName().equals("Changed Name") &&
                             command.subcategoryDescription().equals("Changed Description")));
         }

         @Test
         void cat15_returns_404() throws Exception{
             String json = """
                        {
                        "name": "Changed Name",
                        "description": "Changed Description"
                        }
                    """;

             when(editSubcategoryUseCase.execute(any()))
                     .thenThrow(new ResourceNotFoundException("Subcategory", "id", "1"));

             mockMvc.perform(patch("/api/categories/1/subcategories/2")
                             .contentType(MediaType.APPLICATION_JSON)
                             .content(json))
                     .andExpect(status().isNotFound());


             verify(editSubcategoryUseCase).execute(argThat(command ->
                     command.categoryId().equals("1") &&
                             command.subcategoryId().equals("2") &&
                             command.subcategoryName().equals("Changed Name") &&
                             command.subcategoryDescription().equals("Changed Description")));
         }

         @Test
         void cat13_returns_409_on_edit() throws Exception{
             String json = """
                        {
                        "name": "Changed Name",
                        "description": "Changed Description"
                        }
                    """;

             when(editSubcategoryUseCase.execute(any()))
                     .thenThrow(new ResourceAlreadyExistsException("Subcategory", "id", "1"));

             mockMvc.perform(patch("/api/categories/1/subcategories/2")
                             .contentType(MediaType.APPLICATION_JSON)
                             .content(json))
                     .andExpect(status().isConflict());


             verify(editSubcategoryUseCase).execute(argThat(command ->
                     command.categoryId().equals("1") &&
                             command.subcategoryId().equals("2") &&
                             command.subcategoryName().equals("Changed Name") &&
                             command.subcategoryDescription().equals("Changed Description")));
         }

         @Test
         void cat15_rejects_blank_name() throws Exception{
             String json = """
                        {
                        "name": "",
                        "description": "Changed Description"
                        }
                    """;

             mockMvc.perform(patch("/api/categories/1/subcategories/2")
                             .contentType(MediaType.APPLICATION_JSON)
                             .content(json))
                     .andExpect(status().isBadRequest());
         }

         @Test
         void should_return_400_when_malformed_json() throws Exception{
             String json = """
                        {
                        "name": "Any name"
                        "description": "Changed Description"
                        }
                    """;

             mockMvc.perform(patch("/api/categories/1/subcategories/2")
                             .contentType(MediaType.APPLICATION_JSON)
                             .content(json))
                     .andExpect(status().isBadRequest());
         }

         @Test
         void should_return_500_when_unexpected_error() throws Exception{
             String json = """
                        {
                        "name": "Changed Name",
                        "description": "Changed Description"
                        }
                    """;

             when(editSubcategoryUseCase.execute(any()))
                     .thenThrow(new RuntimeException("Unexpected Error"));

             mockMvc.perform(patch("/api/categories/1/subcategories/2")
                             .contentType(MediaType.APPLICATION_JSON)
                             .content(json))
                     .andExpect(status().isInternalServerError());


             verify(editSubcategoryUseCase).execute(argThat(command ->
                     command.categoryId().equals("1") &&
                             command.subcategoryId().equals("2") &&
                             command.subcategoryName().equals("Changed Name") &&
                             command.subcategoryDescription().equals("Changed Description")));
         }
     }

     @Nested
    class FindAllSubcategoriesByCatIdControllerTests{
        @Test
        void cat14_returns_subcategories() throws Exception{
            List<SubcategoryResult> result = List.of(
                    new SubcategoryResult("1",
                            "2",
                            "Name1",
                            "Description1",
                        null,
                        null),
                    new SubcategoryResult("1",
                            "3",
                            "Name2",
                            "Description2",
                        null,
                        null)
            );

            when(findAllSubcategoriesByCategoryIdUseCase.execute(any()))
                    .thenReturn(result);

            mockMvc.perform(get("/api/categories/1/subcategories"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.[0].subcategoryId").value("2"))
                    .andExpect(jsonPath("$.[0].categoryId").value("1"))
                    .andExpect(jsonPath("$.[1].subcategoryId").value("3"))
                    .andExpect(jsonPath("$.[1].categoryId").value("1"));

            verify(findAllSubcategoriesByCategoryIdUseCase).execute("1");
        }

         @Test
         void cat14_returns_empty_array() throws Exception{
             List<SubcategoryResult> result = List.of();

             when(findAllSubcategoriesByCategoryIdUseCase.execute(any()))
                     .thenReturn(result);

             mockMvc.perform(get("/api/categories/1/subcategories"))
                     .andExpect(status().isOk())
                     .andExpect(jsonPath("$.size()").value(0));

             verify(findAllSubcategoriesByCategoryIdUseCase).execute("1");
         }

         @Test
         void should_return_500_when_unexpected_error() throws Exception{
             when(findAllSubcategoriesByCategoryIdUseCase.execute(any()))
                     .thenThrow(new RuntimeException("Unexpected error"));

             mockMvc.perform(get("/api/categories/1/subcategories"))
                     .andExpect(status().isInternalServerError());

             verify(findAllSubcategoriesByCategoryIdUseCase).execute("1");
         }
     }

     @Nested
    class DeleteSubcategoryControllerTest{
        @Test
         void cat16_returns_204() throws Exception{

            mockMvc.perform(delete("/api/categories/1/subcategories/2"))
                    .andExpect(status().isNoContent());

            verify(deleteSubcategoryUseCase).execute(argThat(command ->
                    command.categoryId().equals("1") &&
                    command.subcategoryId().equals("2")));
        }

        @Test
         void cat17_rejects_foreign_category_on_subcategory_delete() throws Exception{
            doThrow(new ResourceNotFoundException("Category", "id", "1"))
                    .when(deleteSubcategoryUseCase)
                    .execute(argThat(command ->
                            command.categoryId().equals("1") &&
                                    command.subcategoryId().equals("2")
                    ));

            mockMvc.perform(delete("/api/categories/1/subcategories/2"))
                    .andExpect(status().isNotFound());

            verify(deleteSubcategoryUseCase).execute(argThat(command ->
                    command.categoryId().equals("1") &&
                            command.subcategoryId().equals("2")));
        }

         @Test
         void cat16_returns_404() throws Exception{
             doThrow(new ResourceNotFoundException("Subcategory", "id", "1"))
                     .when(deleteSubcategoryUseCase)
                     .execute(argThat(command ->
                             command.categoryId().equals("1") &&
                                     command.subcategoryId().equals("2")
                     ));

             mockMvc.perform(delete("/api/categories/1/subcategories/2"))
                     .andExpect(status().isNotFound());

             verify(deleteSubcategoryUseCase).execute(argThat(command ->
                     command.categoryId().equals("1") &&
                             command.subcategoryId().equals("2")));
         }

         @Test
         void cat16_returns_409_when_the_subcategory_has_movements() throws Exception{
             doThrow(new ResourceInUseException("Subcategory", "it still has movements"))
                     .when(deleteSubcategoryUseCase)
                     .execute(argThat(command ->
                             command.categoryId().equals("1") &&
                                     command.subcategoryId().equals("2")
                     ));

             mockMvc.perform(delete("/api/categories/1/subcategories/2"))
                     .andExpect(status().isConflict())
                     .andExpect(jsonPath("$.status").value(409));
         }

         @Test
         void should_return_500_when_unexpected_error() throws Exception{

             doThrow(new RuntimeException("Unexpected error"))
                     .when(deleteSubcategoryUseCase)
                     .execute(argThat(command ->
                             command.categoryId().equals("1") &&
                                     command.subcategoryId().equals("2")
                     ));

             mockMvc.perform(delete("/api/categories/1/subcategories/2"))
                     .andExpect(status().isInternalServerError());

             verify(deleteSubcategoryUseCase).execute(argThat(command ->
                     command.categoryId().equals("1") &&
                             command.subcategoryId().equals("2")));
         }
     }
    @Test
    void should_return_400_when_validation_exception() throws Exception {
        when(createCategoryUseCase.execute(any()))
                .thenThrow(new ValidationException("Invalid data"));

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "name": "Tech",
                            "description": "desc",
                            "type": "INCOME"
                        }
                    """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid data"));
    }

    @Nested
    class CategoryVisualControllerTests {

        @Test
        void cat18_acceptsIconAndColorOnCreate() throws Exception {
            String json = """
                    {
                        "name": "Food",
                        "description": "Food",
                        "type": "OUTCOME",
                        "icon": "utensils",
                        "color": "amber"
                    }
                    """;

            when(createCategoryUseCase.execute(any()))
                    .thenReturn(new CategoryResult(
                            "1", "Food", "Food", "OUTCOME", "utensils", "amber"));

            mockMvc.perform(post("/api/categories")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.icon").value("utensils"))
                    .andExpect(jsonPath("$.color").value("amber"));

            verify(createCategoryUseCase).execute(argThat(command ->
                    "utensils".equals(command.icon()) &&
                            "amber".equals(command.color())
            ));
        }

        @Test
        void cat20_returnsNullIconAndColorWhenNotChosen() throws Exception {
            when(findCategoryByIdUseCase.execute("1"))
                    .thenReturn(new CategoryResult(
                            "1", "Food", "Food", "OUTCOME", null, null));

            mockMvc.perform(get("/api/categories/1"))
                    .andExpect(status().isOk())
                    .andExpect(content().string(containsString("\"icon\":null")))
                    .andExpect(content().string(containsString("\"color\":null")));
        }

        @Test
        void cat20_returnsNullIconAndColorInCategoryList() throws Exception {
            when(findAllCategoriesUseCase.execute())
                    .thenReturn(List.of(new CategoryResult(
                            "1", "Food", "Food", "OUTCOME", null, null)));

            mockMvc.perform(get("/api/categories"))
                    .andExpect(status().isOk())
                    .andExpect(content().string(containsString("\"icon\":null")))
                    .andExpect(content().string(containsString("\"color\":null")));
        }

        @Test
        void cat21_acceptsIconAndColorOnPatch() throws Exception {
            String json = """
                    {
                        "icon": "car",
                        "color": "blue"
                    }
                    """;

            when(editCategoryUseCase.execute(any()))
                    .thenReturn(new CategoryResult(
                            "1", "Food", "Food", "OUTCOME", "car", "blue"));

            mockMvc.perform(patch("/api/categories/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.icon").value("car"))
                    .andExpect(jsonPath("$.color").value("blue"));

            verify(editCategoryUseCase).execute(argThat(command ->
                    "car".equals(command.icon()) &&
                            "blue".equals(command.color())
            ));
        }

        @Test
        void cat21_rejectsIconLongerThan48() throws Exception {
            String json = "{\"icon\": \"" + "a".repeat(49) + "\"}";

            mockMvc.perform(patch("/api/categories/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void cat19_rejectsColorLongerThan32() throws Exception {
            String json = "{\"color\": \"" + "a".repeat(33) + "\"}";

            mockMvc.perform(patch("/api/categories/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void cat22_acceptsSubcategoryIconAndColor() throws Exception {
            String json = """
                    {
                        "name": "Groceries",
                        "description": "Groceries",
                        "icon": "carrot",
                        "color": "green"
                    }
                    """;

            when(addSubcategoryUseCase.execute(any()))
                    .thenReturn(new SubcategoryResult(
                            "1", "2", "Groceries", "Groceries", "carrot", "green"));

            mockMvc.perform(post("/api/categories/1/subcategories")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.icon").value("carrot"))
                    .andExpect(jsonPath("$.color").value("green"));

            verify(addSubcategoryUseCase).execute(argThat(command ->
                    "carrot".equals(command.icon()) &&
                            "green".equals(command.color())
            ));
        }

        @Test
        void cat23_returnsNullIconAndColorForSubcategory() throws Exception {
            when(findAllSubcategoriesByCategoryIdUseCase.execute("1"))
                    .thenReturn(List.of(new SubcategoryResult(
                            "1", "2", "Groceries", "Groceries", null, null)));

            mockMvc.perform(get("/api/categories/1/subcategories"))
                    .andExpect(status().isOk())
                    .andExpect(content().string(containsString("\"icon\":null")))
                    .andExpect(content().string(containsString("\"color\":null")));
        }
    }

}
