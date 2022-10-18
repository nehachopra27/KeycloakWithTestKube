package com.keycloak.test;

import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class LaunchTest {
    static String url="https://keycloak.192.168.64.11.nip.io/";
    static String tokenUrl="realms/master/protocol/openid-connect/token";

    @Test
    public void validateStatusCode() {
        given().relaxedHTTPSValidation()
            .expect()
                    .statusCode(200)
            .when().get(url);

        }
    @Test
    public static void  validateWelcomeText() {
        given().relaxedHTTPSValidation().when().get(url)
                .then().assertThat()
                        .body("html.head.title", Matchers.containsString("Welcome to Keycloak"));

    }


}
