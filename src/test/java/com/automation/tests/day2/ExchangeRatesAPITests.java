package com.automation.tests.day2;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;
public class ExchangeRatesAPITests {

    private String baseURI = "http://api.openrates.io/";

    @Test
    public void test1(){
        Response response = given().baseUri(baseURI+"latest/").get();
        System.out.println(response.getBody().prettyPrint());
        assertEquals(200,response.getStatusCode());
    }

    // verify that data is coming as json
    @Test
    public void test2(){
        Response response = given().get(baseURI+"latest");
        assertEquals(200,response.getStatusCode());
        // we can do like that
        assertEquals("application/json",response.getContentType());
        // or like this
        assertEquals("application/json",response.getHeader("Content-Type"));
    }

    // GET https://api.exchangeratesapi.io/latest?base=USD HTTP/1.1
    // base is a query parameter that will ask web service to change currency from eu to something else
    @Test
    public void test3(){
        // TASK: get currency exchange rate for dollar. By default it's euro.
   //     Response response = given().get(baseURI+"latest?base=USD");
   // or we can do like this for passing parameters
        Response response = given().baseUri(baseURI).basePath("latest").queryParam("base","USD").get();
        assertEquals(200,response.getStatusCode());
        System.out.println(response.prettyPrint());
    }

// TASK: verify that response body, for latest currency rates contains today's date (2020-01-23) (yyyy-MM-dd)
    @Test
    public void test4(){
        Response response = given().baseUri(baseURI+"latest").queryParam("base","GBP").get();

        String todaysDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        System.out.println("Today is : "+ todaysDate);

        assertEquals(200,response.getStatusCode());
        assertTrue(response.getBody().asString().contains(todaysDate));
    }

    // let's get currency exchange rate for year 2000
    // GET https://api.exchangeratesapi.io/history?start_at=2018-01-01&end_at=2018-09-01&base=USD&symbols=ILS,JPY

    @Test
    public void test5(){
        Response response = given().baseUri(baseURI + "history").
                queryParam("start_at","2000-01-01").
                queryParam("end_at","2000-12-31").
                queryParam("base","USD").
                queryParam("symbols","EUR","GBP").get();

        System.out.println(response.prettyPrint());
    }

    /**
     * Given request parameter "base" is "USD"
     * When user sends request to "api.openrates.io"
     * Then response code should be 200
     * And response body must contain "base": "USD"
     */

    @Test
    public void test6(){
        Response response = given().baseUri(baseURI+"latest").queryParam("base","USD").get();
        System.out.println(response.prettyPrint());
        assertEquals(200,response.getStatusCode());
        assertTrue(response.getBody().asString().contains("\"base\": \"USD\""));
    }

}
