package az.unibank.unitech;

import az.unibank.unitech.dto.request.*;
import az.unibank.unitech.dto.response.CommonResponse;
import az.unibank.unitech.dto.response.LoginResponse;
import az.unibank.unitech.entities.Account;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.devtools.remote.client.HttpHeaderInterceptor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static az.unibank.unitech.enums.ResponseEnum.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.OK;

//@ActiveProfiles("qa")
@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestMethodOrder(MethodOrderer.MethodName.class)
public class RestControllerTests {

    @Autowired
    TestRestTemplate restTemplate;

    public static String randomPinString;
    public static String authToken;
    public static String identifier1;
    public static String identifier2;

    @Test
    @Order(1)
    void a1_registerTest() {
        Random rand = new Random();
        int randomPin = 1000000 + rand.nextInt(8999999);
        RestControllerTests.randomPinString = String.valueOf(randomPin);

        ResponseEntity<CommonResponse> responseEntity = restTemplate
                .postForEntity("/api/users/public/register", new RegisterRequest(RestControllerTests.randomPinString, "p@ssword", "TEST"), CommonResponse.class);

        assertEquals(OK, responseEntity.getStatusCode());
    }

    @Test
    @Order(2)
    void a2_registerAgainTest() {
        ResponseEntity<CommonResponse> responseEntity = restTemplate
                .postForEntity("/api/users/public/register", new RegisterRequest(RestControllerTests.randomPinString, "p@ssword", "TEST"), CommonResponse.class);

        assertEquals(OK, responseEntity.getStatusCode());
        assertEquals(SAME_PIN_ERROR.getMessage(), responseEntity.getBody().getMessage());
    }

    @Test
    @Order(3)
    void a3_login() {
        ResponseEntity<CommonResponse> responseEntity = restTemplate
                .postForEntity("/api/users/public/login", new LoginRequest(RestControllerTests.randomPinString, "p@ssword"), CommonResponse.class);

        assertEquals(OK, responseEntity.getStatusCode());

        ObjectMapper objectMapper = new ObjectMapper();
        LoginResponse loginResponse = objectMapper.convertValue(responseEntity.getBody().getData(), LoginResponse.class);
        RestControllerTests.authToken = "Bearer " + loginResponse.getToken();
    }

    @Test
    @Order(4)
    void a4_loginBadWithCredentials() {
        ResponseEntity<CommonResponse> responseEntity = restTemplate
                .postForEntity("/api/users/public/login", new LoginRequest("bad_credential", "p@ssword"), CommonResponse.class);

        assertEquals(OK, responseEntity.getStatusCode());
        assertEquals(BAD_CREDENTIALS.getMessage(), responseEntity.getBody().getMessage());
    }


    @Test
    @Order(11)
    void b1_addAccountTest() {
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new HttpHeaderInterceptor("Authorization", RestControllerTests.authToken));
        restTemplate.getRestTemplate().setInterceptors(interceptors);

        ResponseEntity<CommonResponse> responseEntity = restTemplate
                .postForEntity("/api/accounts/addAccount", null, CommonResponse.class);

        assertEquals(OK, responseEntity.getStatusCode());
        assertEquals(SUCCESS.getMessage(), responseEntity.getBody().getMessage());

        System.out.println(">>>>" + responseEntity.getBody().getData());


        ObjectMapper objectMapper = new ObjectMapper();
        Account account = objectMapper.convertValue(responseEntity.getBody().getData(), Account.class);
        RestControllerTests.identifier1 = account.getIdentifier();

        System.out.println(RestControllerTests.identifier1);
    }

    @Test
    @Order(12)
    void b2_addSecondAccountTest() {
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new HttpHeaderInterceptor("Authorization", RestControllerTests.authToken));
        restTemplate.getRestTemplate().setInterceptors(interceptors);

        ResponseEntity<CommonResponse> responseEntity = restTemplate
                .postForEntity("/api/accounts/addAccount", null, CommonResponse.class);

        assertEquals(OK, responseEntity.getStatusCode());
        assertEquals(SUCCESS.getMessage(), responseEntity.getBody().getMessage());

        ObjectMapper objectMapper = new ObjectMapper();
        Account account = objectMapper.convertValue(responseEntity.getBody().getData(), Account.class);
        RestControllerTests.identifier2 = account.getIdentifier();
        System.out.println(identifier2);
    }

    @Test
    @Order(13)
    void b3_addBalanceToFirstTest() {
        ResponseEntity<CommonResponse> responseEntity = restTemplate
                .postForEntity("/api/accounts/addBalance", new AddBalanceRequest(RestControllerTests.identifier1, BigDecimal.valueOf(100)), CommonResponse.class);

        assertEquals(OK, responseEntity.getStatusCode());
        assertEquals(SUCCESS.getMessage(), responseEntity.getBody().getMessage());
    }


    @Test
    @Order(14)
    void b4_sendAmountToTheSameAccountTest() {
        ResponseEntity<CommonResponse> responseEntity = restTemplate
                .postForEntity("/api/transfers/makeTransfer", new TransferRequest(RestControllerTests.identifier1, RestControllerTests.identifier1, BigDecimal.valueOf(10)), CommonResponse.class);

        assertEquals(OK, responseEntity.getStatusCode());
        assertEquals(SAME_ACCOUNT.getMessage(), responseEntity.getBody().getMessage());
    }

    @Test
    @Order(15)
    void b5_sendAmountToOtherAccountTest() {
        ResponseEntity<CommonResponse> responseEntity = restTemplate
                .postForEntity("/api/transfers/makeTransfer", new TransferRequest(RestControllerTests.identifier1, RestControllerTests.identifier2, BigDecimal.valueOf(10)), CommonResponse.class);

        assertEquals(OK, responseEntity.getStatusCode());
        assertEquals(SUCCESS.getMessage(), responseEntity.getBody().getMessage());
    }

    @Test
    @Order(16)
    void b6_sendToMuchAmountToAccountTest() {
        ResponseEntity<CommonResponse> responseEntity = restTemplate
                .postForEntity("/api/transfers/makeTransfer", new TransferRequest(RestControllerTests.identifier1, RestControllerTests.identifier2, BigDecimal.valueOf(1000)), CommonResponse.class);

        assertEquals(OK, responseEntity.getStatusCode());
        assertEquals(INSUFFICIENT.getMessage(), responseEntity.getBody().getMessage());
    }


    @Test
    @Order(31)
    void c1_currencyRatesTest() {
        ResponseEntity<CommonResponse> responseEntity = restTemplate
                .postForEntity("/api/accounts/addAccount", new CurrencyRateRequest("USD", "AZN"), CommonResponse.class);

        assertEquals(OK, responseEntity.getStatusCode());
    }
}
