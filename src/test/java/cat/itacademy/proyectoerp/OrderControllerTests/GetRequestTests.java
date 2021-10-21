package cat.itacademy.proyectoerp.OrderControllerTests;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import cat.itacademy.proyectoerp.ProyectoErpApplication;
import cat.itacademy.proyectoerp.domain.Order;
import cat.itacademy.proyectoerp.repository.IOrderRepository;
import cat.itacademy.proyectoerp.security.entity.JwtLogin;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = ProyectoErpApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class GetRequestTests {
	
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	IOrderRepository orderRepository;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	@DisplayName("get all orders - success")
	void givenOrders_whenGetOrders_thenStatus200() throws Exception {
		String accessToken = obtainAdminAccessToken();
		String endPoint = "/api/orders";
		this.mockMvc.perform(get(endPoint)
				.header("Authorization", "Bearer " + accessToken).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	@DisplayName("get order by id - success")
	void givenOrderById_whenGetOrderById_thenStatus200() throws Exception {
		Order firstOrder = orderRepository.findAll().get(0);
		UUID id = firstOrder.getId();
		String accessToken = obtainAdminAccessToken();
		String endPoint = "/api/orders/{id}";
		
		this.mockMvc.perform(get(endPoint, id)
				.header("Authorization", "Bearer " + accessToken).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}
	
	@Test
	@DisplayName("get order by id - NoContent when non existent id ")
	void givenNonExistentId_whenGetOrderById_thenStatus204() throws Exception {
		UUID nonExtistentId = UUID.fromString("06d70bed-7424-43ab-954e-385fcd68997a");
		String accessToken = this.obtainAdminAccessToken();
		String endPoint = "/api/orders/{id}";
		
		this.mockMvc.perform(get(endPoint, nonExtistentId)
				.header("Authorization", "Bearer " + accessToken).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());
	}
	
	@Test
	@DisplayName("get order by id - BadRequest when wrong id format")
	void givenWrongIdFormat_whenGetOrderById_thenStatus400() throws Exception {
		String wrongIdFormat = "yertueriy";
		String accessToken = this.obtainAdminAccessToken();
		String endPoint = "/api/orders/{id}";
		
		this.mockMvc.perform(get(endPoint, wrongIdFormat)
				.header("Authorization", "Bearer " + accessToken).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("get order by id - BadRequest when id is space character(s)")
	void givenBlankId_whenGetOrderById_thenStatus500() throws Exception {
		String wrongIdFormat = "   ";
		String accessToken = this.obtainAdminAccessToken();
		String endPoint = "/api/orders/{id}";
		
		this.mockMvc.perform(get(endPoint, wrongIdFormat)
				.header("Authorization", "Bearer " + accessToken).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError());
	}
	
	
	
	
	
	//TODO new
	@Test
	@DisplayName("get order by client - Succes when placed from the same client")
	public void givenOrderBySameClient_thenStatus200() throws Exception { //TODO checkstatus
		String accessToken = obtainClientAccessToken("client@erp.com");
		UUID idClient = UUID.fromString("76a366c5-a80f-48a1-9135-9aad6a207701");
		String endPoint = "/api/orders/{idClient}";
		this.mockMvc.perform(get(endPoint, idClient)
				.header("Authorization", "Bearer " + accessToken).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}
	
	
	@Test
	@DisplayName("get order by client - BadRequest when placed from another client")
	public void givenOrderByOtherClient_thenStatus401() throws Exception { //TODO checkstatus
		String accessToken = obtainClientAccessToken("client@erp.com");
		UUID idClient = UUID.fromString("0aa0ed3d-d69c-4955-a265-be813c8bf8f3");
		String endPoint = "/api/orders/{idClient}";
		this.mockMvc.perform(get(endPoint, idClient)
				.header("Authorization", "Bearer " + accessToken).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}
	
	
	@Test
	@DisplayName("get order by client - Bad request when placed by an employee")
	public void givenOrderByAdmin_thenStatus200() throws Exception { //TODO checkstatus
		String accessToken = obtainAdminAccessToken();
		UUID idClient = UUID.fromString("76a366c5-a80f-48a1-9135-9aad6a207701");
		String endPoint = "/api/orders/{idClient}";
		this.mockMvc.perform(get(endPoint, idClient)
				.header("Authorization", "Bearer " + accessToken).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}
	
	
	
	
	
	
	
	
	
	
	//TODO finish new
	
	
	
	
	
	private String obtainAdminAccessToken() throws Exception {
		String testUsername = "admin@erp.com";
		String testPassword = "ReW9a0&+TP";
		JwtLogin jwtLogin = new JwtLogin(testUsername, testPassword);

		ResultActions resultPost = this.mockMvc.perform(post("/api/login")
				.content(new ObjectMapper().writeValueAsString(jwtLogin))
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		String resultString = resultPost.andReturn().getResponse().getContentAsString();
		return new JacksonJsonParser().parseMap(resultString).get("token").toString();
	}
	
	
	
	private String obtainClientAccessToken(String email) throws Exception {
		String testUsername = email;
		String testPassword = "ReW9a0&+TP";
		JwtLogin jwtLogin = new JwtLogin(testUsername, testPassword);
		
		ResultActions resultPost = this.mockMvc.perform(post("/api/login")
				.content(new ObjectMapper().writeValueAsString(jwtLogin))
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
		
		String resultString = resultPost.andReturn().getResponse().getContentAsString();
		return new JacksonJsonParser().parseMap(resultString).get("token").toString();
	}
	

}
