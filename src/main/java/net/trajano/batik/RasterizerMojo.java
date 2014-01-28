package net.trajano.batik;

import java.io.File;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Executes the Batik rasterizer.
 */
@Mojo(name = "rasterizer", defaultPhase = LifecyclePhase.GENERATE_RESOURCES, threadSafe = true, requiresOnline = false)
public class RasterizerMojo extends AbstractMojo {
    /**
     * The directory to write the rasterized SVG files.
     */
    @Parameter(defaultValue = "${project.build.directory}/generated-resources/batik", required = true)
    private File destDir;

    /**
     * The type of file to convert to. Valid values are <code>png</code>,
     * <code>jpeg</code>, <code>tiff</code> or <code>pdf</code>.
     */
    @Parameter(defaultValue = "png", required = true)
    private String destType;

    /**
     * The directory containing the SVG files.
     */
    @Parameter(defaultValue = "${basedir}/src/main/svg", required = true)
    private File srcDir;
    /**
     * The file pattern for inclusion of SVG files.
     */
    @Parameter(defaultValue = "**/*.svg", required = true)
    private String srcIncludes;

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() throws MojoExecutionException {
        getLog().info(destDir.toString());
    }
}
