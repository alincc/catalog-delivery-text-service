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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class, RibbonClientConfiguration.class})
@WebIntegrationTest("server.port: 0")
public class IntegrationTest {

    @Value("${local.server.port}")
    int port;

    @Autowired
    ILoadBalancer loadBalancer;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    private MockWebServer mockWebServer;
    RestTemplate template = new TestRestTemplate();

    @Before
    public void setup() throws Exception {
        String searchResponse = IOUtils.toString(getClass().getResourceAsStream("/searchindex.json"));
        String structure = IOUtils.toString(getClass().getResourceAsStream("/struct/urn_nbn_no-nb_digibok_2014062307158.xml"));
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        mockWebServer = new MockWebServer();
        final Dispatcher dispatcher = new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest recordedRequest) throws InterruptedException {
                if (recordedRequest.getPath().startsWith("/search")) {
                    return new MockResponse().setBody(searchResponse).setResponseCode(200).setHeader("Content-Type", "application/hal+json; charset=utf-8");
                } else if (recordedRequest.getPath().equals("/catalog/metadata/d87d999b6bca07c553089032d889cd7d/struct")) {
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
        URI uri = new URI("http://localhost:" + port + "/alto/URN:NBN:no-nb_digibok_2014062307158?pages=6&pageSelection=id");
        byte[] execute = new TestRestTemplate().execute(uri, HttpMethod.GET, new RequestCallback() {
            @Override
            public void doWithRequest(ClientHttpRequest request) throws IOException {
                HttpHeaders headers = request.getHeaders();
                headers.add("Accept",MediaType.APPLICATION_OCTET_STREAM_VALUE);
            }
        }, new ResponseExtractor<byte[]>() {
            @Override
            public byte[] extractData(ClientHttpResponse response) throws IOException {
                if (!response.getStatusCode().is2xxSuccessful()) {
                    throw new RuntimeException("Expected 200 got " + response.getStatusCode());
                }
                return IOUtils.toByteArray(response.getBody());
            }
        });
        assertNotNull(execute);
    }

    @Test
    public void getAltosAsTextTest() throws Exception {
        URI uri = new URI("http://localhost:" + port + "/text/URN:NBN:no-nb_digibok_2014062307158?pages=6&pageSelection=id");
        byte[] execute = new TestRestTemplate().execute(uri, HttpMethod.GET, new RequestCallback() {
            @Override
            public void doWithRequest(ClientHttpRequest request) throws IOException {
                HttpHeaders headers = request.getHeaders();
                headers.add("Accept",MediaType.APPLICATION_OCTET_STREAM_VALUE);
            }
        }, new ResponseExtractor<byte[]>() {
            @Override
            public byte[] extractData(ClientHttpResponse response) throws IOException {
                if (!response.getStatusCode().is2xxSuccessful()) {
                    throw new RuntimeException("Expected 200 got " + response.getStatusCode());
                }
                return IOUtils.toByteArray(response.getBody());
            }
        });
        assertNotNull(execute);
    }

    @Test
    public void whenAltoIsFoundResponseShouldBeOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/alto/URN:NBN:no-nb_digibok_2014062307158/URN:NBN:no-nb_digibok_2014062307158_0006"))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    }

    @Test
    public void whenAltoIsNotFoundResponseShouldBeNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/alto/URN:NBN:no-nb_digibok_2014062307158/URN:NBN:no-nb_digibok_2014062307158_0060"))
                .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();
    }

    @Test
    public void getTextTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/text/URN:NBN:no-nb_digibok_2014062307158/URN:NBN:no-nb_digibok_2014062307158_0006"))
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
