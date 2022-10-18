package com.keycloak.test;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.ResponseSpecification;
import org.junit.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static io.restassured.RestAssured.given;

public class RealmTests {

    String url="https://keycloak.192.168.64.11.nip.io/";
    String realm="master";
    String realmPath="admin/realms/"+realm;
    String tokenUrl="realms/master/protocol/openid-connect/token";
    String userPath=realmPath+"/users";
    String clientsPath=realmPath+"/clients";
    String accessToken;

    @BeforeTest
    public void validatetestTokenStatusCodeRceived(){
        Response response=given().relaxedHTTPSValidation().baseUri(url).basePath(tokenUrl)
                .contentType("application/x-www-form-urlencoded")
                .param("grant_type","password")
                .param("client_id","admin-cli")
                .param("username","admin")
                .param("password","admin").when()
                .post();
        accessToken = response.jsonPath().getString("access_token");
        Assert.assertEquals(200, response.getStatusCode());
    }
    @Test
    public void validateAdminUserExists()
    {
        Response response=given().relaxedHTTPSValidation().baseUri(url)
                .basePath(userPath).header("Authorization","Bearer "+accessToken)
                .get();
        Assert.assertTrue(response.getBody().asString().contains("\"username\":\"admin\""));
    }
    @Test
    public void validateAleastOneClientExists()
    {
        Response response = given().relaxedHTTPSValidation().baseUri(url)
                .basePath(clientsPath).header("Authorization","Bearer "+accessToken)
                .when()
                    .get()
                .then()
                .log().ifValidationFails()
                    .assertThat().
                    statusCode(200).extract().response();
        JsonPath jsonPathEvaluator= response.jsonPath();
        List allClients =  jsonPathEvaluator.getList("$");
        Assert.assertNotEquals(0,allClients.size());
        System.out.println("Number of clients:" + allClients.size());
    }
    @Test
    public void validateRealmDisplayNameIsKeycloak()
    {
        Response response = given().relaxedHTTPSValidation().baseUri(url)
                .basePath(realmPath).header("Authorization","Bearer "+accessToken)
                .when()
                .get()
                .then()
                .log().ifValidationFails()
                .assertThat().
                statusCode(200).extract().response();
        Assert.assertEquals("Keycloak",response.jsonPath().getString("displayName"));
    }
}
