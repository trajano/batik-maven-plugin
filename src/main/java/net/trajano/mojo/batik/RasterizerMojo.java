package net.trajano.mojo.batik;

import static java.lang.String.format;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.batik.apps.rasterizer.DestinationType;
import org.apache.batik.apps.rasterizer.SVGConverter;
import org.apache.batik.apps.rasterizer.SVGConverterException;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.Scanner;
import org.sonatype.plexus.build.incremental.BuildContext;

import net.trajano.mojo.batik.internal.LoggingSvgConverterController;

/**
 * Executes the Batik rasterizer.
 */
@Mojo(name = "rasterizer", defaultPhase = LifecyclePhase.GENERATE_RESOURCES, threadSafe = true, requiresOnline = false)
public class RasterizerMojo extends AbstractMojo {

    /**
     * Resource bundle.
     */
    private static final ResourceBundle R = ResourceBundle.getBundle("META-INF/Messages");

    /**
     * Build context.
     */
    @Component
    private BuildContext buildContext;

    /**
     * The directory to write the rasterized SVG files.
     */
    @Parameter(defaultValue = "${project.build.directory}/generated-resources/batik", required = true)
    private File destDir;

    /**
     * Fail on error. Otherwise it will continue to the next file.
     */
    @Parameter(defaultValue = "true")
    private boolean failOnError;

    /**
     * In less than or equal to zero, the height is not constrained on the
     * output image.
     */
    @Parameter(defaultValue = "-1")
    private float height;

    /**
     * If less than or equal to zero, the maximum height does not have any
     * effect on the output image.
     */
    @Parameter(defaultValue = "-1")
    private float maxHeight;

    /**
     * If less than or equal to zero, the maximum width does not have any effect
     * on the output image.
     */
    @Parameter(defaultValue = "-1")
    private float maxWidth;
    /**
     * The MIME type of file to convert to. Valid values are
     * <code>image/png</code>, <code>image/jpeg</code>, <code>image/tiff</code>
     * and <code>application/pdf</code>.
     */
    @Parameter(defaultValue = "image/png", required = true)
    private String mimeType;

    /**
     * The Maven Project.
     */
    @Parameter(defaultValue = "${project}", readonly = true)
    private MavenProject project;

    /**
     * A list of SVG resources to import. {@link Resource} is used instead of
     * FileSet to allow for filtering in the future. The default is:
     *
     * <pre>
     * &lt;svgResources&gt;
     *     &lt;resource&gt;
     *         &lt;directory&gt;${basedir}/src/main/svg&lt;/directory&gt;
     *         &lt;filtering&gt;false&lt;/filtering&gt;
     *         &lt;includes&gt;
     *             &lt;include&gt;**\/\*.svg&lt;/include&gt;
     *         &lt;/includes&gt;
     *         &lt;excludes&gt;
     *         &lt;/excludes&gt;
     *     &lt;/resources&gt;
     * &lt;/svgResources&gt;
     * </pre>
     */
    @Parameter(required = false)
    private List<Resource> svgResources;

    /**
     * In less than or equal to zero, the width is not constrained on the output
     * image.
     */
    @Parameter(defaultValue = "-1")
    private float width;

    /**
     * Performs the rasterization.
     *
     * @throws MojoExecutionException
     *             thrown when there is a problem executing Mjo.
     */
    @Override
    public void execute() throws MojoExecutionException {
        final SVGConverter converter = new SVGConverter(new LoggingSvgConverterController(getLog(), failOnError));
        converter.setDestinationType(mapMimeTypeToDestinationType(mimeType));

        final List<String> unfilteredSourceFiles = new LinkedList<String>();
        final List<String> filteredSourceFiles = new LinkedList<String>();
        if (svgResources == null) {
            final Resource defaultSvgResource = new Resource();
            defaultSvgResource.setDirectory(new File(project.getBasedir(), "src/main/svg").getPath());
            defaultSvgResource.addInclude("**/*.svg");
            defaultSvgResource.setFiltering(false);
            svgResources = Collections.singletonList(defaultSvgResource);
        }
        for (final Resource resource : svgResources) {
            final File basedirectory = new File(resource.getDirectory()); // NOPMD
            if (!basedirectory.isDirectory()) { // NOPMD
                getLog().warn(format(R.getString("missingdir"), resource.getDirectory()));
                continue;
            }
            final Scanner scanner = buildContext.newScanner(basedirectory);
            scanner.setIncludes(resource.getIncludes().toArray(new String[0])); // NOPMD
            scanner.setExcludes(resource.getExcludes().toArray(new String[0])); // NOPMD
            scanner.scan();
            for (final String includedFile : scanner.getIncludedFiles()) {
                if (resource.isFiltering()) {
                    // TODO write out filtered file to batik folder and use the
                    // filtered file as input.
                    filteredSourceFiles.add(new File(basedirectory, // NOPMD
                            includedFile).toString());
                } else {
                    unfilteredSourceFiles.add(new File(basedirectory, // NOPMD
                            includedFile).toString());
                }
            }
        }
        converter.setDst(destDir);
        converter.setWidth(width);
        converter.setHeight(height);
        converter.setMaxWidth(maxWidth);
        converter.setMaxHeight(maxHeight);
        try {
            if (!filteredSourceFiles.isEmpty()) {
                converter.setSources(filteredSourceFiles.toArray(new String[0]));
                converter.execute();
            }
            if (!unfilteredSourceFiles.isEmpty()) {
                converter.setSources(unfilteredSourceFiles.toArray(new String[0]));
                converter.execute();
            }
        } catch (final SVGConverterException e) {
            throw new MojoExecutionException(R.getString("errorduringconversion"), e);
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
    private DestinationType mapMimeTypeToDestinationType(final String mimeTypeString) throws MojoExecutionException {
        if ("image/png".equalsIgnoreCase(mimeTypeString)) {
            return DestinationType.PNG;
        } else if ("image/tiff".equalsIgnoreCase(mimeTypeString)) {
            return DestinationType.TIFF;
        } else if ("image/jpeg".equalsIgnoreCase(mimeTypeString)) {
            return DestinationType.JPEG;
        } else if ("application/pdf".equalsIgnoreCase(mimeTypeString)) {
            return DestinationType.PDF;
        } else {
            throw new MojoExecutionException(String.format(R.getString("unsupportedmimetype"), mimeTypeString));
        }
    }
}
