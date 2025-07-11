package com.bytebites.restaurantservice.controller;

import com.bytebites.restaurantservice.config.AbstractIntegrationTest;
import com.bytebites.restaurantservice.dto.RestaurantRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class RestaurantControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "RESTAURANT_OWNER")
    void createRestaurant_ValidInput_ReturnsCreatedRestaurant() throws Exception {
        RestaurantRequest request = RestaurantRequest.builder()
                .name("Test Restaurant")
                .address("123 Test St")
                .phoneNumber("+1234567890")
                .email("test@restaurant.com")
                .build();

        mockMvc.perform(post("/api/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test Restaurant"))
                .andExpect(jsonPath("$.address").value("123 Test St"))
                .andExpect(jsonPath("$.phoneNumber").value("+1234567890"))
                .andExpect(jsonPath("$.email").value("test@restaurant.com"));
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void getRestaurant_Unauthorized_ReturnsForbidden() throws Exception {
        mockMvc.perform(get("/api/restaurants/{id}", "123e4567-e89b-12d3-a456-426614174000"))
                .andExpect(status().isForbidden());
    }
}