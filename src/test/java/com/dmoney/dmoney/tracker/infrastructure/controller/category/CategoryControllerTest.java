package com.dmoney.dmoney.tracker.infrastructure.controller.category;

import com.dmoney.dmoney.tracker.application.category.*;
import com.dmoney.dmoney.tracker.application.category.result.CategoryResult;
import com.dmoney.dmoney.tracker.application.category.result.SubcategoryResult;
import com.dmoney.dmoney.tracker.exceptions.CategoryAlreadyExistsException;
import com.dmoney.dmoney.tracker.exceptions.CategoryNotFoundException;
import com.dmoney.dmoney.tracker.exceptions.SubcategoryAlreadyExistsException;
import com.dmoney.dmoney.tracker.exceptions.SubcategoryNotFoundException;
import com.dmoney.dmoney.tracker.infrastructure.persistence.category.CategoryEntity;
import org.junit.jupiter.api.Nested;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

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
        void should_create_category_and_return_201() throws Exception {

            String json = """
                    {
                     "name": "Tech",
                     "description": "Tech description"
                    }
                    """;

            CategoryResult result =
                    new CategoryResult("1", "Tech", "Tech description");

            when(createCategoryUseCase.execute(any()))
                    .thenReturn(result);

            mockMvc.perform(post("/api/categories")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.name").value("Tech"))
                    .andExpect(jsonPath("$.description").value("Tech description"));

            verify(createCategoryUseCase).execute(argThat(command ->
                    command.name().equals("Tech") &&
                            command.description().equals("Tech description")
            ));
        }

        @Test
        void should_return_400_when_name_is_blank() throws Exception {

            String json = """
                    {
                        "name": "",
                        "description": "Description"
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
                        "description": "test"
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
        void should_return_409_when_category_already_exists() throws Exception {

            String json = """
                    {
                        "name": "Books",
                        "description": "Category for books"
                    }
                    """;

            when(createCategoryUseCase.execute(any()))
                    .thenThrow(new CategoryAlreadyExistsException(
                            "Name",
                            "Books"
                    ));

            mockMvc.perform(post("/api/categories")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isConflict());
        }

        @Test
        void should_return_400_when_name_is_missing() throws Exception {

            String json = """
                    {
                        "description": "Description"
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
        void should_return_all_categories() throws Exception {

            List<CategoryResult> result = List.of(
                    new CategoryResult("1", "Tech", "Technology"),
                    new CategoryResult("2", "Books", "Books category")
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
        void should_return_empty_list_when_no_categories_exist() throws Exception {

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
        void should_return_the_category_if_found() throws Exception{
            CategoryResult categoryResult =  new CategoryResult(
                    "1", "Tech", "Technology"
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
        void should_return_404_if_category_not_found() throws Exception{
            String anyId = "123";
            when(findCategoryByIdUseCase.execute(anyId))
                    .thenThrow(new CategoryNotFoundException(
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
        void should_delete_category_by_id() throws Exception{
            mockMvc.perform(delete("/api/categories/1"))
                    .andExpect(status().isNoContent());

            verify(deleteCategoryUseCase).execute("1");
        }

        void should_return_404_when_category_is_not_found() throws Exception{
            doThrow(new CategoryNotFoundException("id", "1"))
                    .when(deleteCategoryUseCase)
                    .execute("1");
            mockMvc.perform(delete("/api/categories/1"))
                    .andExpect(status().isNoContent());

            verify(deleteCategoryUseCase).execute("1");
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
        void should_create_a_subcategory() throws Exception {
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
                    "Description"
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
        void should_return_400_when_name_is_not_provided() throws Exception{
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
        void should_throw_404_when_category_is_not_found() throws Exception {
            String json = """
                        {
                            "name": "Name",
                            "description": "Description"
                        }
                    """;

            when(addSubcategoryUseCase.execute(any()))
                    .thenThrow(new CategoryNotFoundException("id", "1"));

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
        void should_throw_409_when_subcategory_already_exists() throws Exception {
            String json = """
                        {
                            "name": "Name",
                            "description": "Description"
                        }
                    """;

            when(addSubcategoryUseCase.execute(any()))
                    .thenThrow(new SubcategoryAlreadyExistsException("id", "1"));

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
         void should_edit_category() throws Exception{
            String json = """
                        {
                            "name": "Changed Name",
                            "description": "Changed Description"
                        }
                    """;

            CategoryResult result = new CategoryResult(
                    "1",
                    "Changed Name",
                    "Changed Description");

            when(editCategoryUseCase.execute(any()))
                    .thenReturn(result);

            mockMvc.perform(patch("/api/categories/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                    .andExpect(status().isCreated())
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
         void should_edit_category_when_name_is_null() throws Exception{
             String json = """
                        {
                            "description": "Changed Description"
                        }
                    """;

             CategoryResult result = new CategoryResult(
                     "1",
                     "Name",
                     "Changed Description");

             when(editCategoryUseCase.execute(any()))
                     .thenReturn(result);

             mockMvc.perform(patch("/api/categories/1")
                             .contentType(MediaType.APPLICATION_JSON)
                             .content(json))
                     .andExpect(status().isCreated())
                     .andExpect(jsonPath("$.id").value("1"))
                     .andExpect(jsonPath("$.name").value("Name"))
                     .andExpect(jsonPath("$.description").value("Changed Description"));

             verify(editCategoryUseCase).execute(argThat(command ->
                     command.id().equals("1") &&
                             command.name() == null &&
                             command.description().equals("Changed Description")
             ));
         }

         @Test
         void should_return_400_when_the_name_is_blank() throws Exception{
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
         void should_return_404_when_category_is_not_found() throws Exception{
             String json = """
                        {
                            "name": "Changed Name",
                            "description": "Changed Description"
                        }
                    """;

             when(editCategoryUseCase.execute(any()))
                     .thenThrow(new CategoryNotFoundException("id", "1"));

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
         void should_return_409_when_category_already_exists() throws Exception{
             String json = """
                        {
                            "name": "Changed Name",
                            "description": "Changed Description"
                        }
                    """;

             when(editCategoryUseCase.execute(any()))
                     .thenThrow(new CategoryAlreadyExistsException("id", "1"));

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
         void should_edit_subcategory() throws Exception{
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
                    "Changed Description"
            );

            when(editSubcategoryUseCase.execute(any()))
                    .thenReturn(result);
            mockMvc.perform(patch("/api/categories/1/subcategories/2")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isCreated())
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
         void should_edit_subcategory_without_name() throws Exception{
             String json = """
                        {
                        "description": "Changed Description"
                        }
                    """;

             SubcategoryResult result = new SubcategoryResult(
                     "1",
                     "2",
                     "Name",
                     "Changed Description"
             );

             when(editSubcategoryUseCase.execute(any()))
                     .thenReturn(result);
             mockMvc.perform(patch("/api/categories/1/subcategories/2")
                             .contentType(MediaType.APPLICATION_JSON)
                             .content(json))
                     .andExpect(status().isCreated())
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
         void should_return_404_when_category_not_found() throws Exception{
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
                     "Changed Description"
             );

             when(editSubcategoryUseCase.execute(any()))
                     .thenThrow(new CategoryNotFoundException("id", "1"));

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
         void should_return_404_when_subcategory_not_found() throws Exception{
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
                     "Changed Description"
             );

             when(editSubcategoryUseCase.execute(any()))
                     .thenThrow(new SubcategoryNotFoundException("id", "1"));

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
         void should_return_409_when_subcategory_already_exists() throws Exception{
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
                     "Changed Description"
             );

             when(editSubcategoryUseCase.execute(any()))
                     .thenThrow(new SubcategoryAlreadyExistsException("id", "1"));

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
         void should_return_400_when_name_is_blank() throws Exception{
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
         void should_return_500_when_unexpected_error() throws Exception{
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
                     "Changed Description"
             );

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
        void should_get_all_subcategories_by_category_id() throws Exception{
            List<SubcategoryResult> result = List.of(
                    new SubcategoryResult("1",
                            "2",
                            "Name1",
                            "Description1"),
                    new SubcategoryResult("1",
                            "3",
                            "Name2",
                            "Description2")
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
         void should_get_empty_array_when_no_subcategories_found() throws Exception{
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
             List<SubcategoryResult> result = List.of();

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
         void should_delete_subcategory() throws Exception{

            mockMvc.perform(delete("/api/categories/1/subcategories/2"))
                    .andExpect(status().isNoContent());

            verify(deleteSubcategoryUseCase).execute(argThat(command ->
                    command.categoryId().equals("1") &&
                    command.subcategoryId().equals("2")));
        }

        @Test
         void should_return_404_when_category_not_found() throws Exception{
            doThrow(new CategoryNotFoundException("id", "1"))
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
         void should_return_404_when_subcategory_not_found() throws Exception{
             doThrow(new SubcategoryNotFoundException("id", "1"))
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

     @Nested
    class NullPointerControllerTest{
        @Test
         void should_return_500_when_null_pointer_exception() throws Exception{
            String json = """
                        {
                            "name": "null",
                            "description" : "Description"
                        }
                    """;

            when(createCategoryUseCase.execute(any()))
                    .thenThrow(new NullPointerException());

            mockMvc.perform(post("/api/categories")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                    .andExpect(status().isInternalServerError());
        }
     }

}
