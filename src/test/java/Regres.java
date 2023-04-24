import io.restassured.http.ContentType;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
public class Regres {
    public static final String BASE_URL = "https://reqres.in/api/users";

    @Test
    void singleUserCheckSchemeTest(){
        given()
                .log().uri()
                .when()
                .get(BASE_URL + "/2")
                .then()
                .log().all()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemes/singleUserResponceScheme.json"));

    }

    @Test
    void checkSuccessCreateUser(){

        String body = "{ \"name\": \"morpheus\", \"job\": \"leader\" }";

        given()
                .log().body()
                .body(body)
                .contentType(JSON)
                .when()
                .post(BASE_URL + "/2")
                .then()
                .log().all()
                .statusCode(201)
                .body("name", is("morpheus")).body("job", is("leader"));
    }

    @Test
    void changeMorpheusJobTest(){

        String body = "{ \"name\": \"morpheus\", \"job\": \"QA automation\" }";

        String expectedJob = "QA automation";

        String actualJob = given()
                .body(body)
                .contentType(JSON)
                .log().uri()
                .log().body()
                .when()
                .put(BASE_URL + "/2")
                .then()
                .log().all()
                .statusCode(200)
                .extract().path("job");

        assertEquals(expectedJob, actualJob);

    }

    @Test
    void checkResponseForInvalidJson(){

        String body = "{ \"name\": \"morpheus\", \"job : 200 }";

        given()
                .body(body)
                .contentType(JSON)
                .log().body()
                .when()
                .patch(BASE_URL + "/2")
                .then()
                .log().all()
                .statusCode(400);
    }

    @Test
    void checkStatusCodeForDelete(){
        given()
                .log().all()
                .when()
                .delete(BASE_URL + "/2")
                .then()
                .log().all()
                .statusCode(204);
    }
}

