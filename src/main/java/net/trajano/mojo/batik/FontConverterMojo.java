package net.trajano.mojo.batik;

import static java.lang.String.format;

import java.io.File;
import java.io.PrintStream;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.batik.svggen.font.Font;
import org.apache.maven.model.FileSet;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.Scanner;
import org.sonatype.plexus.build.incremental.BuildContext;

import net.trajano.mojo.batik.internal.SvgFontUtil;

/**
 * Executes the Batik Font converter.
 */
@Mojo(name = "ttf2svg", defaultPhase = LifecyclePhase.GENERATE_RESOURCES, threadSafe = true)
public class FontConverterMojo extends AbstractMojo {

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
     * The directory to write the SVG files converted from TTFs.
     */
    @Parameter(defaultValue = "${project.build.directory}/generated-resources/ttf2svg", required = true)
    private File destDir;

    /**
     * A list of font file sets to import. Filtering is not supported on fonts.
     * The default is:
     *
     * <pre>
     * &lt;fontFileSets&gt;
     *     &lt;fileSet&gt;
     *         &lt;directory&gt;${basedir}/src/main/ttf&lt;/directory&gt;
     *         &lt;includes&gt;
     *             &lt;include&gt;**\/\*.ttf&lt;/include&gt;
     *         &lt;/includes&gt;
     *         &lt;excludes&gt;
     *         &lt;/excludes&gt;
     *     &lt;/fileSet&gt;
     * &lt;/fontFileSets&gt;
     * </pre>
     */
    @Parameter(required = false)
    private List<FileSet> fontFileSets;

    /**
     * The Maven Project.
     */
    @Parameter(defaultValue = "${project}", readonly = true)
    private MavenProject project;

    /**
     * Performs the conversion.
     *
     * @throws MojoExecutionException
     *             thrown when there is a problem executing Mjo.
     */
    @Override
    public void execute() throws MojoExecutionException {

        destDir.mkdirs();
        if (fontFileSets == null) {
            final FileSet defaultFontFileSet = new FileSet();
            defaultFontFileSet.setDirectory(new File(project.getBasedir(), "src/main/ttf").getPath());
            defaultFontFileSet.addInclude("**/*.ttf");
            fontFileSets = Collections.singletonList(defaultFontFileSet);
        }
        for (final FileSet fileSet : fontFileSets) {
            final String directory = fileSet.getDirectory();
            final File baseDirectory = new File(directory); // NOPMD
            if (!baseDirectory.isDirectory()) { // NOPMD
                getLog().warn(format(R.getString("missingdir"), directory));
                continue;
            }
            final Scanner scanner = buildContext.newScanner(baseDirectory);
            scanner.setIncludes(fileSet.getIncludes().toArray(new String[0])); // NOPMD
            scanner.setExcludes(fileSet.getExcludes().toArray(new String[0])); // NOPMD
            scanner.scan();
            for (final String includedFile : scanner.getIncludedFiles()) {

                final File inputFile = new File(baseDirectory, // NOPMD
                        includedFile);
                final String basename = includedFile.substring(0, includedFile.lastIndexOf('.'));
                final File svgFile = new File(destDir, basename + ".svg"); // NOPMD
                try {
                    final PrintStream ps = new PrintStream(// NOPMD
                            buildContext.newFileOutputStream(svgFile));
                    SvgFontUtil.writeFontAsSvg(ps, Font.create(inputFile.toString()), basename);
                    ps.close();
                } catch (final Exception e) {
                    throw new MojoExecutionException(String.format(R.getString("failedtorender"), inputFile), e);
                }
            }
        }

    }
}
