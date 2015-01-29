package com.github.platan.varnishexec.example.junit;

import static com.google.common.base.Charsets.UTF_8;
import static org.junit.Assert.assertEquals;

import com.github.platan.varnishexec.VarnishCommand;
import com.github.platan.varnishexec.junit.VarnishResource;
import com.google.common.io.Resources;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.net.URL;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class IndexControllerJunitClassRuleTest {

    private static final VarnishCommand VARNISH_COMMAND = VarnishCommand.newBuilder()
            .withConfigFile("./src/test/etc/varnish/default.vcl").build();

    @ClassRule
    public static VarnishResource varnishResource = VarnishResource.build(VARNISH_COMMAND);

    @Test
    public void includeFragments() throws Exception {
        // given
        int port = varnishResource.getPort();
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
        int port = varnishResource.getPort();
        URL footerResourceUrl = new URL("http://localhost:" + port + "/footer");

        // when
        String content = Resources.toString(footerResourceUrl, UTF_8);

        // then
        String expectedContent = "</html>";
        assertEquals(expectedContent, content);
    }
}