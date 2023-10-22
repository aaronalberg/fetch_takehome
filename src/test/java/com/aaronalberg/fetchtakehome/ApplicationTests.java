package com.aaronalberg.fetchtakehome;

import com.aaronalberg.fetchtakehome.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	ReceiptDB receiptDB;

	@Test
	void contextLoads() {
	}

	@BeforeEach
	void setUp() {
		receiptDB.reset();
	}

	@Test
	void postReceipt1() throws Exception {
		assertThat(receiptDB.size()).isEqualTo(0);

		ProcessReceiptOutput output = postReceipt1Helper();

		assertThat(receiptDB.size()).isEqualTo(1);
		assertThat(receiptDB.getReceiptById(output.getId())).isNotNull();
	}

	@Test
	void getReceipt1() throws Exception {
		ProcessReceiptOutput postOutput = postReceipt1Helper();

		MockHttpServletResponse response = mockMvc.perform(get("/receipts/{id}/points", postOutput.getId()))
			.andExpect(status().isOk())
			.andReturn().getResponse();

		GetPointsOutput output = objectMapper.readValue(response.getContentAsString(), GetPointsOutput.class);
		assertThat(output.getPoints()).isEqualTo("28");
	}

	private ProcessReceiptOutput postReceipt1Helper() throws Exception {
		Item item1 = Item.builder().price("6.49").shortDescription("Mountain Dew 12PK").build();
		Item item2 = Item.builder().price("12.25").shortDescription("Emils Cheese Pizza").build();
		Item item3 = Item.builder().price("1.26").shortDescription("Knorr Creamy Chicken").build();
		Item item4 = Item.builder().price("3.35").shortDescription("Doritos Nacho Cheese").build();
		Item item5 = Item.builder().price("12.0").shortDescription("   Klarbrunn 12-PK 12 FL OZ  ").build();

		ReceiptInput input = ReceiptInput.builder()
			.retailer("Target")
			.purchaseDate("2022-01-01")
			.purchaseTime("13:01")
			.total("35.35")
			.items(List.of(item1, item2, item3, item4, item5))
			.build();

		MockHttpServletResponse response = mockMvc.perform(post("/receipts/process")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(input)))
			.andExpect(status().isOk())
			.andReturn().getResponse();


		return objectMapper.readValue(response.getContentAsString(), ProcessReceiptOutput.class);
	}

	@Test
	void postReceipt2() throws Exception {
		assertThat(receiptDB.size()).isEqualTo(0);

		ProcessReceiptOutput output = postReceipt2Helper();

		assertThat(receiptDB.size()).isEqualTo(1);
		assertThat(receiptDB.getReceiptById(output.getId())).isNotNull();
	}

	@Test
	void getReceipt2() throws Exception {
		ProcessReceiptOutput postOutput = postReceipt2Helper();

		MockHttpServletResponse response = mockMvc.perform(get("/receipts/{id}/points", postOutput.getId()))
			.andExpect(status().isOk())
			.andReturn().getResponse();

		GetPointsOutput output = objectMapper.readValue(response.getContentAsString(), GetPointsOutput.class);
		assertThat(output.getPoints()).isEqualTo("109");
	}

	private ProcessReceiptOutput postReceipt2Helper() throws Exception {
		Item item1 = Item.builder().price("2.25").shortDescription("Gatorade").build();
		Item item2 = Item.builder().price("2.25").shortDescription("Gatorade").build();
		Item item3 = Item.builder().price("2.25").shortDescription("Gatorade").build();
		Item item4 = Item.builder().price("2.25").shortDescription("Gatorade").build();

		ReceiptInput input = ReceiptInput.builder()
			.retailer("M&M Corner Market")
			.purchaseDate("2022-03-20")
			.purchaseTime("14:33")
			.total("9.00")
			.items(List.of(item1, item2, item3, item4))
			.build();

		MockHttpServletResponse response = mockMvc.perform(post("/receipts/process")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(input)))
			.andExpect(status().isOk())
			.andReturn().getResponse();


		return objectMapper.readValue(response.getContentAsString(), ProcessReceiptOutput.class);
	}

	@Test
	void invalidReceipt() throws Exception {
		ReceiptInput input = ReceiptInput.builder().retailer("BLAH").build();
		mockMvc.perform(post("/receipts/process")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(input)))
			.andExpect(status().isBadRequest());
	}

	@Test
	void noReceipt() throws Exception {
		mockMvc.perform(post("/receipts/process")
				.contentType("application/json"))
			.andExpect(status().isBadRequest());
	}

	@Test
	void receiptNotFound() throws Exception {
		mockMvc.perform(get("/receipts/{id}/points", "ID-5555"))
			.andExpect(status().isNotFound());
	}
}
