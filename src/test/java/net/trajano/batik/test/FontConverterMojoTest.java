package net.trajano.batik.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import net.trajano.batik.FontConverterMojo;
import net.trajano.batik.RasterizerMojo;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.testing.MojoRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * Tests the {@link RasterizerMojo}.
 */
public class FontConverterMojoTest {
    @Rule
    public MojoRule rule = new MojoRule();

    /**
     * Tests with just the dest directory set.
     * 
     * @throws Exception
     */
    @Test
    public void testDefault() throws Exception {
        final File testPom = new File(
                "src/test/resources/net/trajano/batik/default-pom.xml");
        assertTrue(testPom.exists());

        final FontConverterMojo mojo = (FontConverterMojo) rule.lookupMojo(
                "ttf2svg", testPom);
        assertNotNull(mojo);
        mojo.execute();
    }

    /**
     * @throws Exception
     */
    @Test(expected = MojoExecutionException.class)
    public void testInvalidFont() throws Exception {
        final File testPom = new File(
                "src/test/resources/net/trajano/batik/invalid-font-converter-pom.xml");
        assertTrue(testPom.exists());

        final FontConverterMojo mojo = (FontConverterMojo) rule.lookupMojo(
                "ttf2svg", testPom);
        assertNotNull(mojo);
        mojo.execute();
    }

    /**
     * @throws Exception
     */
    @Test
    public void testMojoGoal() throws Exception {
        final File testPom = new File(
                "src/test/resources/net/trajano/batik/font-converter-pom.xml");
        assertTrue(testPom.exists());

        final FontConverterMojo mojo = (FontConverterMojo) rule.lookupMojo(
                "ttf2svg", testPom);
        assertNotNull(mojo);
        mojo.execute();
    }

}