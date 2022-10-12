package com.keycloak.test;

import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class LaunchTest {
    String url="https://keycloak.192.168.64.4.nip.io/";

    @Test
    public void validateStatusCode() {
        given().relaxedHTTPSValidation().when()
                .get(url).then().statusCode(200);

    }

}
