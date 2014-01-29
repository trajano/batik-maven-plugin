package net.trajano.batik.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import net.trajano.batik.RasterizerMojo;

import org.apache.maven.plugin.testing.MojoRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * Tests the {@link RasterizerMojo}.
 */
public class RasterizerMojoTest {
    @Rule
    public MojoRule rule = new MojoRule();

    /**
     * @throws Exception
     */
    @Test
    public void testMojoGoal() throws Exception {
        final File testPom = new File(
                "src/test/resources/net/trajano/batik/rasterizer-pom.xml");
        assertTrue(testPom.exists());

        final RasterizerMojo mojo = (RasterizerMojo) rule.lookupMojo(
                "rasterizer", testPom);
        assertNotNull(mojo);
        mojo.execute();

        // TODO use BufferedImageReader to read the image and verify the color
        // of a pixel.
    }
}