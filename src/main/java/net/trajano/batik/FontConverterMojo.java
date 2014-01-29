package net.trajano.batik;

import java.io.File;
import java.util.Collections;
import java.util.List;

import org.apache.batik.svggen.font.SVGFont;
import org.apache.maven.model.FileSet;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.DirectoryScanner;

/**
 * Executes the Batik Font converter.
 */
@Mojo(name = "ttf2svg", defaultPhase = LifecyclePhase.GENERATE_RESOURCES, threadSafe = true)
public class FontConverterMojo extends AbstractMojo {
    /**
     * Default font filesets.
     */
    private static final List<FileSet> DEFAULT_FONT_FILESETS;

    static {
        final FileSet defaultFontFileSet = new FileSet();
        defaultFontFileSet.setDirectory("src/main/ttf");
        defaultFontFileSet.addInclude("**/*.ttf");
        DEFAULT_FONT_FILESETS = Collections.singletonList(defaultFontFileSet);
    }

    /**
     * The directory to write the SVG files converted from TTFs.
     */
    @Parameter(defaultValue = "${project.build.directory}/generated-resources/ttf2svg", required = true)
    private File destDir;

    /**
     * A list of font file sets to import. Filtering is not supported on fonts.
     * The default is:
     * 
     * <pre>
     * &lt;fontFileSets>
     *     &lt;fileSet>
     *         &lt;directory>${basedir}/src/main/ttf&lt;/directory>
     *         &lt;includes>
     *             &lt;include>**\/\*.ttf&lt;/include>
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
        destDir.mkdirs();
        final DirectoryScanner scanner = new DirectoryScanner();
        if (fontFileSets == null) {
            fontFileSets = DEFAULT_FONT_FILESETS;
        }
        for (final FileSet fileSet : fontFileSets) {
            scanner.setBasedir(fileSet.getDirectory());
            scanner.setIncludes(fileSet.getIncludes().toArray(new String[0]));
            scanner.setExcludes(fileSet.getExcludes().toArray(new String[0]));
            scanner.scan();
            for (final String includedFile : scanner.getIncludedFiles()) {

                SVGFont.main(new String[] {
                        new File(fileSet.getDirectory(), includedFile)
                                .toString(),
                        "-o",
                        new File(destDir, includedFile.substring(0,
                                includedFile.lastIndexOf('.')) + ".svg")
                                .toString() });
            }
        }

    }
}
