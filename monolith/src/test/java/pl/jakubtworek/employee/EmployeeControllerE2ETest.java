package pl.jakubtworek.employee;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import pl.jakubtworek.employee.dto.EmployeeDto;
import pl.jakubtworek.employee.dto.EmployeeRequest;
import pl.jakubtworek.employee.dto.Job;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EmployeeControllerE2ETest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DirtiesContext
    void testCreateEmployee() {
        // given
        EmployeeRequest request = new EmployeeRequest("John", "Doe", "COOK");

        // when
        ResponseEntity<EmployeeDto> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/employees",
                request,
                EmployeeDto.class
        );

        // then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody().getId());
    }

    @Test
    @DirtiesContext
    void testGetEmployees() {
        // given
        EmployeeRequest request1 = new EmployeeRequest("John", "Doe", "COOK");
        EmployeeRequest request2 = new EmployeeRequest("Jane", "Smith", "WAITER");
        restTemplate.postForEntity("http://localhost:" + port + "/employees", request1, EmployeeDto.class);
        restTemplate.postForEntity("http://localhost:" + port + "/employees", request2, EmployeeDto.class);

        // when
        ResponseEntity<EmployeeDto[]> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/employees",
                EmployeeDto[].class
        );

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<EmployeeDto> employees = Arrays.asList(response.getBody());
        assertEquals(2, employees.size());
    }

    @Test
    @DirtiesContext
    void testDeleteEmployee() {
        // Tworzenie pracownika
        EmployeeRequest request = new EmployeeRequest("John", "Doe", "COOK");
        ResponseEntity<EmployeeDto> createdResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/employees",
                request,
                EmployeeDto.class
        );
        Long employeeId = createdResponse.getBody().getId();

        // Usunięcie pracownika
        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                "http://localhost:" + port + "/employees/" + employeeId,
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());

        // Sprawdzenie, czy pracownik został usunięty
        ResponseEntity<EmployeeDto> getResponse = restTemplate.getForEntity(
                "http://localhost:" + port + "/employees/" + employeeId,
                EmployeeDto.class
        );

        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }

    @Test
    @DirtiesContext
    void testGetEmployeeById() {
        // given
        EmployeeRequest request = new EmployeeRequest("John", "Doe", "COOK");
        ResponseEntity<EmployeeDto> createdResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/employees",
                request,
                EmployeeDto.class
        );

        Long employeeId = createdResponse.getBody().getId();

        // when
        ResponseEntity<EmployeeDto> retrievedResponse = restTemplate.getForEntity(
                "http://localhost:" + port + "/employees/" + employeeId,
                EmployeeDto.class
        );

        // then
        assertEquals(HttpStatus.OK, retrievedResponse.getStatusCode());
        assertEquals("John", retrievedResponse.getBody().getFirstName());
        assertEquals("Doe", retrievedResponse.getBody().getLastName());
        assertEquals(Job.COOK, retrievedResponse.getBody().getJob());
    }

    @Test
    @DirtiesContext
    void testGetEmployeesByJob() {
        // given
        EmployeeRequest request1 = new EmployeeRequest("John", "Doe", "COOK");
        EmployeeRequest request2 = new EmployeeRequest("Jane", "Smith", "WAITER");
        restTemplate.postForEntity("http://localhost:" + port + "/employees", request1, EmployeeDto.class);
        restTemplate.postForEntity("http://localhost:" + port + "/employees", request2, EmployeeDto.class);

        // when
        ResponseEntity<EmployeeDto[]> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/employees/job?job=COOK",
                EmployeeDto[].class
        );

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<EmployeeDto> employees = Arrays.asList(response.getBody());
        assertEquals(1, employees.size());
        assertEquals("John", employees.get(0).getFirstName());
        assertEquals("Doe", employees.get(0).getLastName());
        assertEquals(Job.COOK, employees.get(0).getJob());
    }
}
