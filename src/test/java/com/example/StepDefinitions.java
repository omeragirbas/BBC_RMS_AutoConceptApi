package com.example;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class StepDefinitions {

    private static final String BASE_URI = "https://testapi.io/api/RMSTest";
    private static Response response;
    private static String lastEndpoint = "";

    @Given("make a GET request to endpoint {string}")
    public void makeAGetRequestToEndpoint(String endpoint) {
        String fullUrl = BASE_URI + endpoint;
        if (!endpoint.equals(lastEndpoint)) { // Prevent duplicate requests for the same endpoint
            response = RestAssured.get(fullUrl);
            response.prettyPrint();
            lastEndpoint = endpoint;
        } else {
            System.out.println("Reusing response for endpoint: " + fullUrl);
        }
    }

    @Then("the response status code should be {int}")
    public void theResponseStatusCodeShouldBe(int statusCode) {
        assertEquals("HTTP status code validation failed.", statusCode, response.getStatusCode());
    }

    @Then("the response time should be less than {int} milliseconds")
    public void theResponseTimeShouldBeLessThan(int time) {
        assertTrue("Response time exceeded the limit.", response.getTime() < time);
    }

    @Then("the id field for all items in the data array should not be null or empty")
    public void theIdFieldForAllItemsInTheDataArrayShouldNotBeNullOrEmpty() {
        JsonPath jsonPath = new JsonPath(response.getBody().asString());
        String channelIds=jsonPath.getString("schedule.channel.id");
        List <String> elementIds=jsonPath.getList("schedule.elements.id");
        List <String> episodeIds=jsonPath.getList("schedule.elements.episode.id");
        List<String>versionIds=jsonPath.getList("schedule.elements.episode.versions.id");
        List<String>masterBrandIds=jsonPath.getList("schedule.elements.episode.master_brand.id");

        checkNotNullOrEmpty(channelIds, "Channel IDs");
        checkNotNullOrEmpty(elementIds, "Element IDs");
        checkNotNullOrEmpty(episodeIds, "Episode IDs");
        checkNotNullOrEmpty(versionIds, "Version IDs");
        checkNotNullOrEmpty(masterBrandIds, "Version IDs");
    }

    private static void checkNotNullOrEmpty(List<String> list, String listName) {
        if (list == null || list.contains(null) || list.contains("")) {
            throw new IllegalArgumentException(listName + " contains null or empty values!");
        }
        System.out.println(listName + " validation passed.");
    }

    private static void checkNotNullOrEmpty(String value, String valueName) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException(valueName + " is null or empty!");
        }
        System.out.println(valueName + " validation passed.");
    }

    @Then("the type in episode for every item should always be {string}")
    public void theTypeInEpisodeForEveryItemShouldAlwaysBe(String expectedType) {
        JsonPath jsonPath = new JsonPath(response.getBody().asString());
        List <String> episodeTypes =jsonPath.getList("schedule.elements.episode.type");
        System.out.println("episodeTypes = " + episodeTypes);
        for (String episodeType : episodeTypes) {
            assertEquals(expectedType, episodeType);
        }
    }

    @Then("the title field in episode should not be null or empty for any schedule item")
    public void theTitleFieldInEpisodeShouldNotBeNullOrEmptyForAnyScheduleItem() {
        JsonPath jsonPath = new JsonPath(response.getBody().asString());

        List<String> episodeTitles = jsonPath.getList("schedule.elements.episode.title");
        for (String title : episodeTitles) {
            assertNotNull("Title should not be null", title);
            assertFalse("Title should not be empty", title.trim().isEmpty());
        }
    }

    @Then("only one episode in the list should have live field in episode as true")
    public void onlyOneEpisodeInTheListShouldHaveLiveFieldInEpisodeAsTrue() {
        List<Boolean> liveFields = response.jsonPath().getList("schedule.elements.episode.live");
        int liveTrueCount = 0;
        for (Boolean isLive : liveFields) {
            if (Boolean.TRUE.equals(isLive)) {
                liveTrueCount++;
            }
        }
        System.out.println("Number of episodes with live=true: " + liveTrueCount);
        assertEquals("Expected exactly one episode with live=true", 1, liveTrueCount);
    }

    @Then("the transmission_start date should be before the transmission_end date")
    public void theTransmissionStartDateShouldBeBeforeTheTransmissionEndDate() {
        List<String> transmissionStarts = response.jsonPath().getList("schedule.elements.transmission_start");
        List<String> transmissionEnds = response.jsonPath().getList("schedule.elements.transmission_end");

        for (int i = 0; i < transmissionStarts.size(); i++) {
            assertTrue("Transmission start date is not before the end date.",
                    transmissionStarts.get(i).compareTo(transmissionEnds.get(i)) < 0);
        }
    }

    @Then("the response headers should contain the current Date value")
    public void theResponseHeadersShouldContainTheCurrentDateValue() {
        String responseDate = response.getHeader("Date");
        assertNotNull("Date header is missing.", responseDate);

        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy");
        String currentDate = sdf.format(new Date());
        assertTrue("Date header does not match the current date.", responseDate.contains(currentDate));
    }

    @Then("the response should have error properties {string} and {string}")
    public void theResponseShouldHaveErrorProperties(String prop1, String prop2) {
        JsonPath jsonResponse = response.jsonPath();

        Map<String, Object> errorObject = jsonResponse.getMap("error");

        assertTrue("Error object does not contain '" + prop1 + "'.", errorObject.containsKey(prop1));
        assertTrue("Error object does not contain '" + prop2 + "'.", errorObject.containsKey(prop2));
    }
}