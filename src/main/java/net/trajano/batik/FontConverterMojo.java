package net.trajano.batik;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * Executes the Batik Font converter.
 */
@Mojo(name = "ttf2svg", defaultPhase = LifecyclePhase.GENERATE_RESOURCES, threadSafe = true)
public class FontConverterMojo extends AbstractMojo {
    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() throws MojoExecutionException {
        getLog().info("Hello, world.");
    }
}
