package net.trajano.batik;

import java.io.File;

import org.apache.batik.apps.rasterizer.DestinationType;
import org.apache.batik.apps.rasterizer.SVGConverter;
import org.apache.batik.apps.rasterizer.SVGConverterException;
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
     * {@inheritDoc}
     */
    @Override
    public void execute() throws MojoExecutionException {
        final SVGConverter converter = new SVGConverter(
                new LoggingSvgConverterController(getLog()));
        converter.setDestinationType(mapMimeTypeToDestinationType(mimeType));
        converter
                .setSources(new String[] { "src/test/resources/net/trajano/batik/logo.svg" });
        converter.setDst(destDir);
        try {
            converter.execute();
        } catch (final SVGConverterException e) {
            // TODO add input file names and type
            throw new MojoExecutionException("Failed conversion", e);
        }
    }

    /**
     * Maps a MIME type string to the {@link DestinationType}.
     * 
     * @param mimeTypeString
     *            MIME type string
     * @return destination type
     * @throws MojoExecutionException
     */
    private DestinationType mapMimeTypeToDestinationType(final String mimeTypeString)
            throws MojoExecutionException {
        if ("image/png".equalsIgnoreCase(mimeTypeString)) {
            return DestinationType.PNG;
        } else if ("image/tiff".equalsIgnoreCase(mimeTypeString)) {
            return DestinationType.TIFF;
        } else if ("image/jpg".equalsIgnoreCase(mimeTypeString)) {
            return DestinationType.JPEG;
        } else if ("application/pdf".equalsIgnoreCase(mimeTypeString)) {
            return DestinationType.PDF;
        } else {
            // TODO use message resources
            throw new MojoExecutionException("Unsupported MIME type '"
                    + mimeTypeString + "'");
        }
    }
}
