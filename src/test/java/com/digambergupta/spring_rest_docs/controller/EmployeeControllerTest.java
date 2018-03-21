package com.digambergupta.spring_rest_docs.controller;

import static java.util.Collections.singletonList;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.util.StringUtils.collectionToDelimitedString;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.digambergupta.spring_rest_docs.*;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoRestDocsApplication.class)
public class EmployeeControllerTest {

	@Rule
	public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private ObjectMapper objectMapper;

	private MockMvc mockMvc;

	@Before
	public void setUp() {

		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).apply(documentationConfiguration(this.restDocumentation)).alwaysDo(
				document("{method-name}", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()))).build();
	}

	@Test
	public void indexExample() throws Exception {
		this.mockMvc.perform(get("/")).andExpect(status().isOk()).andDo(
				document("index-example", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
						links(linkWithRel("employee").description("The employee resource")),
						responseFields(subsectionWithPath("links").description("Links to other resources")),
						responseHeaders(headerWithName("Content-Type").description("The Content-Type of the payload, e.g. `application/hal+json`"))));
	}

	@Test
	public void employeeGetExample() throws Exception {

		Map<String, Object> employee = new HashMap<>();
		employee.put("id", 1);
		employee.put("fistName", "Digamber");
		employee.put("lastName", "Gupta");
		employee.put("age", 25);
		employee.put("address", "Wilramstrasse 1, 81669 Munich");

		ConstraintDescriptions desc = new ConstraintDescriptions(Employees.class);

		this.mockMvc.perform(get("/employee").contentType(MediaTypes.HAL_JSON).content(this.objectMapper.writeValueAsString(employee))).andExpect(
				status().isOk()).andDo(document("employee-get-example", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
				requestFields(fieldWithPath("id").description("The id of the input" + collectionToDelimitedString(desc.descriptionsForProperty("id"), ". ")),
						fieldWithPath("fistName").description("The fistName of the employee"),
						fieldWithPath("lastName").description("The lastName of the employee"), fieldWithPath("age").description("The age of the employee"),
						fieldWithPath("address").description("An address of the employee"))));
	}

	@Test
	public void employeeCreateExample() throws Exception {
		Map<String, Object> employee = new HashMap<>();
		employee.put("id", 2);
		employee.put("fistName", "Eckard");
		employee.put("lastName", "Muehlich");
		employee.put("age", 30);
		employee.put("address", "Wilramstrasse 1, 81669 Munich");


		this.mockMvc.perform(post("/employee").contentType(MediaTypes.HAL_JSON).content(this.objectMapper.writeValueAsString(employee))).andExpect(status().isCreated())
				.andDo(document("employee-create-example", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
						requestFields(fieldWithPath("id").description("The id of the input"),
								fieldWithPath("fistName").description("The fistName of the employee"),
								fieldWithPath("lastName").description("The lastName of the employee"), fieldWithPath("age").description("The age of the employee"),
								fieldWithPath("address").description("An address of the employee"))));
	}

	@Test
	public void employeeDeleteExample() throws Exception {
		this.mockMvc.perform(delete("/employee/{id}", 2))
				.andExpect(status().isOk())
				.andDo(document("employee-delete-example", pathParameters(parameterWithName("id").description("The id of the employee to delete"))));
	}

	@Test
	public void employeePatchExample() throws Exception {
		Map<String, Object> employee = new HashMap<>();
		employee.put("address", "Arnulfstrasse 19, 89990 Munich");

		this.mockMvc.perform(patch("/employee/{id}", 2).contentType(MediaTypes.HAL_JSON)
				.content(this.objectMapper.writeValueAsString(employee)))
				.andExpect(status().isOk());
	}

	@Test
	public void save() {
	}

	@Test
	public void employeePutExample() throws Exception {
		Map<String, Object> employee = new HashMap<>();

		employee.put("age", 31);
		employee.put("address", "Karl-prise-plaz 1, 81669 Munich");


		this.mockMvc.perform(put("/employee/{id}", 2).contentType(MediaTypes.HAL_JSON)
				.content(this.objectMapper.writeValueAsString(employee)))
				.andExpect(status().isAccepted());
	}

}