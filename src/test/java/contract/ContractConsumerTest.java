package contract;

import com.example.controller.Order;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

public class ContractConsumerTest {

    private WireMockServer wireMockServer;

    @BeforeMethod
    void setUp() {
        // Pointing to the directory where stubs have been unpacked
        String stubsDirectory = "build/stubs/META-INF/com.saggu/service-provider/0.0.1-SNAPSHOT/";

        wireMockServer = new WireMockServer(
                WireMockConfiguration.options().port(9090).usingFilesUnderDirectory(stubsDirectory)
        );
        wireMockServer.start();
    }

    @Test
    void test() {
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Order> response = restTemplate.
                getForEntity("http://localhost:9090/orders/1",
                        Order.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getOrderId()).isEqualTo("1");
    }

    @AfterMethod
    void tearDown() {
        wireMockServer.stop();
    }
}
