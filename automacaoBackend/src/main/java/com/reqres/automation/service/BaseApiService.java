package com.reqres.automation.service;

import com.reqres.automation.util.ReportManager;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.stereotype.Service;

import static io.restassured.RestAssured.given;

/**
 * Implementação base para serviços de API
 * Implementa o princípio de Responsabilidade Única (S) do SOLID
 */
@Service
public class BaseApiService implements IApiService {

    @Override
    public Response get(RequestSpecification spec, String endpoint) {
        Response response = given()
                .spec(spec)
                .when()
                .get(endpoint)
                .then()
                .extract()
                .response();
        
        ReportManager.attachResponseBody(response);
        return response;
    }

    @Override
    public Response post(RequestSpecification spec, String endpoint, Object payload) {
        if (payload != null) {
            ReportManager.attachRequestBody(payload.toString());
        }
        
        Response response = given()
                .spec(spec)
                .body(payload)
                .when()
                .post(endpoint)
                .then()
                .extract()
                .response();
        
        ReportManager.attachResponseBody(response);
        return response;
    }

    @Override
    public Response put(RequestSpecification spec, String endpoint, Object payload) {
        if (payload != null) {
            ReportManager.attachRequestBody(payload.toString());
        }
        
        Response response = given()
                .spec(spec)
                .body(payload)
                .when()
                .put(endpoint)
                .then()
                .extract()
                .response();
        
        ReportManager.attachResponseBody(response);
        return response;
    }

    @Override
    public Response patch(RequestSpecification spec, String endpoint, Object payload) {
        if (payload != null) {
            ReportManager.attachRequestBody(payload.toString());
        }
        
        Response response = given()
                .spec(spec)
                .body(payload)
                .when()
                .patch(endpoint)
                .then()
                .extract()
                .response();
        
        ReportManager.attachResponseBody(response);
        return response;
    }

    @Override
    public Response delete(RequestSpecification spec, String endpoint) {
        Response response = given()
                .spec(spec)
                .when()
                .delete(endpoint)
                .then()
                .extract()
                .response();
        
        ReportManager.attachResponseBody(response);
        return response;
    }
} 