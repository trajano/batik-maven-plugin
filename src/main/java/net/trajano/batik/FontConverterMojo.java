package net.trajano.batik;

import java.io.File;
import java.util.List;

import org.apache.batik.svggen.font.SVGFont;
import org.apache.maven.model.FileSet;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Executes the Batik Font converter.
 */
@Mojo(name = "ttf2svg", defaultPhase = LifecyclePhase.GENERATE_RESOURCES, threadSafe = true)
public class FontConverterMojo extends AbstractMojo {
    /**
     * The directory to write the rasterized SVG files.
     */
    @Parameter(defaultValue = "${project.build.directory}/generated-resources/batik", required = true)
    private File destDir;

    /**
     * A list of font file sets to import. Filtering is not supported on fonts.
     * The default is:
     * 
     * <pre>
     * &lt;fontFileSets>
     *     &lt;fileSet>
     *         &lt;directory>${basedir}/src/main/svg&lt;/directory>
     *         &lt;includes>
     *             &lt;include>**\/\*.svg&lt;/include>
     *         &lt;/includes>
     *         &lt;excludes>
     *         &lt;/excludes>
     *     &lt;/fileSet>
     * &lt;/fontFileSets>
     * </pre>
     */
    @Parameter(required = false)
    private List<FileSet> fontFileSets;

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() throws MojoExecutionException {
        SVGFont.main(new String[0]);
    }
}
