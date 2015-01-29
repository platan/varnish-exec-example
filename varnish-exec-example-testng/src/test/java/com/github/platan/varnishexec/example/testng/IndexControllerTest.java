package com.github.platan.varnishexec.example.junit;

import static com.google.common.base.Charsets.UTF_8;
import static org.testng.AssertJUnit.assertEquals;

import com.github.platan.varnishexec.VarnishCommand;
import com.github.platan.varnishexec.VarnishExecs;
import com.github.platan.varnishexec.VarnishProcess;
import com.google.common.io.Resources;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.URL;

@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
@TestExecutionListeners(inheritListeners = false, listeners = {
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class})
public class IndexControllerTest extends AbstractTestNGSpringContextTests {

    private static VarnishProcess varnishProcess;

    @BeforeClass
    public static void setUp() {
        varnishProcess = VarnishExecs.start(VarnishCommand.newBuilder()
                .withConfigFile("./src/test/etc/varnish/default.vcl").build());
    }

    @AfterClass
    public static void tearDown() {
        varnishProcess.kill();
    }

    @Test
    public void includeFragments() throws Exception {
        // given
        int port = varnishProcess.getPort();
        URL indexResourceUrl = new URL("http://localhost:" + port);

        // when
        String content = Resources.toString(indexResourceUrl, UTF_8);

        // then
        String expectedContent = "<html>\n" +
                "<head>\n" +
                "    <title>Hello World!</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <p>Hello World!</p>\n" +
                "</body>\n" +
                "</html>";
        assertEquals(expectedContent, content);
    }

    @Test
    public void returnFooter() throws Exception {
        // given
        int port = varnishProcess.getPort();
        URL footerResourceUrl = new URL("http://localhost:" + port + "/footer");

        // when
        String content = Resources.toString(footerResourceUrl, UTF_8);

        // then
        String expectedContent = "</html>";
        assertEquals(expectedContent, content);
    }
}