package no.nb.microservices.catalogdeliverytext.it;

import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import com.squareup.okhttp.mockwebserver.Dispatcher;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;
import no.nb.microservices.catalogdeliverytext.Application;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.net.URI;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class, RibbonClientConfiguration.class})
@WebIntegrationTest("server.port: 0")
public class AltoControllerIT {

    @Value("${local.server.port}")
    int port;

    @Autowired
    ILoadBalancer loadBalancer;

    @Autowired
    WebApplicationContext context;

    MockMvc mockMvc;
    MockWebServer mockWebServer;
    RestTemplate rest = new TestRestTemplate();

    @Before
    public void setup() throws Exception {
        String searchResponse = IOUtils.toString(getClass().getResourceAsStream("/searchindex.json"));
        String structure = IOUtils.toString(getClass().getResourceAsStream("/struct/urn_nbn_no-nb_digibok_2014062307158.xml"));
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        mockWebServer = new MockWebServer();
        rest = new TestRestTemplate();
        final Dispatcher dispatcher = new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest recordedRequest) throws InterruptedException {
                if (recordedRequest.getPath().startsWith("/catalog/v1/search")) {
                    return new MockResponse().setBody(searchResponse).setResponseCode(200).setHeader("Content-Type", "application/hal+json; charset=utf-8");
                } else if (recordedRequest.getPath().equals("/catalog/v1/metadata/d87d999b6bca07c553089032d889cd7d/struct")) {
                    return new MockResponse().setBody(structure).setResponseCode(200).setHeader("Content-Type", "application/xml; charset=utf-8");
                }
                return new MockResponse().setResponseCode(404);
            }
        };
        mockWebServer.setDispatcher(dispatcher);
        mockWebServer.start();

        BaseLoadBalancer baseLoadBalancer = (BaseLoadBalancer) loadBalancer;
        baseLoadBalancer.setServersList(Arrays.asList(new Server(mockWebServer.getHostName(), mockWebServer.getPort())));
    }

    @After
    public void tearDown() throws Exception {
        mockWebServer.shutdown();
    }

    @Test
    public void getAltosTest() throws Exception {
        URI uri = new URI("http://localhost:" + port + "/catalog/v1/contenttext/alto/URN:NBN:no-nb_digibok_2014062307158?pages=6&pageSelection=id");
        ResponseEntity<ByteArrayResource> response = rest.getForEntity(uri, ByteArrayResource.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(933, response.getBody().contentLength());
    }

    @Test
    public void getAltosAsTextTest() throws Exception {
        URI uri = new URI("http://localhost:" + port + "/catalog/v1/contenttext/text/URN:NBN:no-nb_digibok_2014062307158?pages=6&pageSelection=id");
        ResponseEntity<ByteArrayResource> response = rest.getForEntity(uri, ByteArrayResource.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(7, response.getBody().contentLength());
    }

    @Test
    public void whenAltoIsFoundResponseShouldBeOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/catalog/v1/contenttext/alto/URN:NBN:no-nb_digibok_2014062307158/URN:NBN:no-nb_digibok_2014062307158_0006"))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    }

    @Test
    public void whenAltoIsNotFoundResponseShouldBeNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/catalog/v1/contenttext/alto/URN:NBN:no-nb_digibok_2014062307158/URN:NBN:no-nb_digibok_2014062307158_0060"))
                .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();
    }

    @Test
    public void getTextTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/catalog/v1/contenttext/text/URN:NBN:no-nb_digibok_2014062307158/URN:NBN:no-nb_digibok_2014062307158_0006"))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    }
}

@Configuration
class RibbonClientConfiguration {
    @Bean
    public ILoadBalancer ribbonLoadBalancer() {
        return new BaseLoadBalancer();
    }
}
