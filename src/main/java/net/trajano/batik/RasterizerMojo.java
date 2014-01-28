package net.trajano.batik;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.batik.apps.rasterizer.Main;
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
     * The MIME type of file to convert to. Valid values are
     * <code>image/png</code>, <code>image/jpeg</code>, <code>image/tiff</code>
     * or <code>application/pdf</code>.
     */
    @Parameter(defaultValue = "image/png", required = true)
    private String mimeType;

    /**
     * The directory containing the SVG files.
     */
    @Parameter(defaultValue = "${basedir}/src/main/svg", required = true)
    private File srcDir;

    /**
     * Include filters on the source files. Default is <code>**\/\*.svg</code>.
     */
    @Parameter(required = false)
    private String[] srcIncludes;

    /**
     * Build arguments with a given inputfile added to the end.
     * 
     * @param inputFile
     *            input file
     * @return argument list for rasterizer.
     */
    public String[] buildArgumentsForFile(final File inputFile) {
        final List<String> args = new ArrayList<String>();
        args.add("-d");
        args.add(destDir.toString());
        args.add("-m");
        args.add(mimeType);
        args.add(inputFile.toString());
        return args.toArray(new String[0]);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() throws MojoExecutionException {
        final File inputFile = new File(".");
        Main.main(buildArgumentsForFile(inputFile));
    }
}
