package net.trajano.batik;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import net.trajano.batik.internal.LoggingSvgConverterController;

import org.apache.batik.apps.rasterizer.DestinationType;
import org.apache.batik.apps.rasterizer.SVGConverter;
import org.apache.batik.apps.rasterizer.SVGConverterException;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.DirectoryScanner;

/**
 * Executes the Batik rasterizer.
 */
@Mojo(name = "rasterizer", defaultPhase = LifecyclePhase.GENERATE_RESOURCES, threadSafe = true, requiresOnline = false)
public class RasterizerMojo extends AbstractMojo {
    /**
     * Default SVG resources.
     */
    private static final List<Resource> DEFAULT_SVG_RESOURCES;

    static {
        final Resource defaultSvgResource = new Resource();
        defaultSvgResource.setDirectory("src/main/svg");
        defaultSvgResource.addInclude("**/*.svg");
        defaultSvgResource.setFiltering(false);
        DEFAULT_SVG_RESOURCES = Collections.singletonList(defaultSvgResource);
    }

    /**
     * The directory to write the rasterized SVG files.
     */
    @Parameter(defaultValue = "${project.build.directory}/generated-resources/batik", required = true)
    private File destDir;

    /**
     * The MIME type of file to convert to. Valid values are
     * <code>image/png</code>, <code>image/jpeg</code>, <code>image/tiff</code>
     * and <code>application/pdf</code>.
     */
    @Parameter(defaultValue = "image/png", required = true)
    private String mimeType;

    /**
     * A list of SVG resources to import. {@link Resource} is used instead of
     * FileSet to allow for filtering in the future. The default is:
     * 
     * <pre>
     * &lt;svgResources>
     *     &lt;resource>
     *         &lt;directory>${basedir}/src/main/svg&lt;/directory>
     *         &lt;filtering>false&lt;/filtering>
     *         &lt;includes>
     *             &lt;include>**\/\*.svg&lt;/include>
     *         &lt;/includes>
     *         &lt;excludes>
     *         &lt;/excludes>
     *     &lt;/resources>
     * &lt;/svgResources>
     * </pre>
     */
    @Parameter(required = false)
    private List<Resource> svgResources;

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() throws MojoExecutionException {
        final SVGConverter converter = new SVGConverter(
                new LoggingSvgConverterController(getLog()));
        converter.setDestinationType(mapMimeTypeToDestinationType(mimeType));

        final List<String> unfilteredSourceFiles = new LinkedList<String>();
        final List<String> filteredSourceFiles = new LinkedList<String>();
        final DirectoryScanner scanner = new DirectoryScanner();
        if (svgResources == null) {
            svgResources = DEFAULT_SVG_RESOURCES;
        }
        for (final Resource resource : svgResources) {
            scanner.setBasedir(resource.getDirectory());
            scanner.setIncludes(resource.getIncludes().toArray(new String[0]));
            scanner.setExcludes(resource.getExcludes().toArray(new String[0]));
            scanner.scan();
            for (final String includedFile : scanner.getIncludedFiles()) {
                if (resource.isFiltering()) {
                    // TODO write out filtered file to batik folder and use the
                    // filtered file as input.
                    filteredSourceFiles.add(new File(resource.getDirectory(),
                            includedFile).toString());
                } else {
                    unfilteredSourceFiles.add(new File(resource.getDirectory(),
                            includedFile).toString());
                }
            }
        }
        converter.setDst(destDir);
        try {
            if (!filteredSourceFiles.isEmpty()) {
                converter
                        .setSources(filteredSourceFiles.toArray(new String[0]));
                converter.execute();
            }
            if (!unfilteredSourceFiles.isEmpty()) {
                converter.setSources(unfilteredSourceFiles
                        .toArray(new String[0]));
                converter.execute();
            }
        } catch (final SVGConverterException e) {
            // TODO add input file names and type from resource
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
    private DestinationType mapMimeTypeToDestinationType(
            final String mimeTypeString) throws MojoExecutionException {
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
