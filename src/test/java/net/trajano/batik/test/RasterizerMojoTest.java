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
    public void testExplicitMimeJpg() throws Exception {
        final File testPom = new File(
                "src/test/resources/net/trajano/batik/rasterizer-pom-jpg.xml");
        assertTrue(testPom.exists());

        final RasterizerMojo mojo = (RasterizerMojo) rule.lookupMojo(
                "rasterizer", testPom);
        assertNotNull(mojo);
        mojo.execute();

        // TODO use BufferedImageReader to read the image and verify the color
        // of a pixel.
    }

    /**
     * @throws Exception
     */
    @Test
    public void testExplicitMimePdf() throws Exception {
        final File testPom = new File(
                "src/test/resources/net/trajano/batik/rasterizer-pom-pdf.xml");
        assertTrue(testPom.exists());

        final RasterizerMojo mojo = (RasterizerMojo) rule.lookupMojo(
                "rasterizer", testPom);
        assertNotNull(mojo);
        mojo.execute();
    }

    /**
     * @throws Exception
     */
    @Test
    public void testExplicitMimePng() throws Exception {
        final File testPom = new File(
                "src/test/resources/net/trajano/batik/rasterizer-pom-png.xml");
        assertTrue(testPom.exists());

        final RasterizerMojo mojo = (RasterizerMojo) rule.lookupMojo(
                "rasterizer", testPom);
        assertNotNull(mojo);
        mojo.execute();

        // TODO use BufferedImageReader to read the image and verify the color
        // of a pixel.
    }

    /**
     * @throws Exception
     */
    @Test
    public void testExplicitMimeTiff() throws Exception {
        final File testPom = new File(
                "src/test/resources/net/trajano/batik/rasterizer-pom-tiff.xml");
        assertTrue(testPom.exists());

        final RasterizerMojo mojo = (RasterizerMojo) rule.lookupMojo(
                "rasterizer", testPom);
        assertNotNull(mojo);
        mojo.execute();

        // TODO use BufferedImageReader to read the image and verify the color
        // of a pixel.
    }
}
