package net.trajano.batik.internal;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.batik.apps.rasterizer.SVGConverterController;
import org.apache.batik.apps.rasterizer.SVGConverterSource;
import org.apache.batik.transcoder.Transcoder;
import org.apache.maven.plugin.logging.Log;

/**
 * An {@link SVGConverterController} that logs events to the Maven logs.
 * 
 * @author Archimedes
 */
public class LoggingSvgConverterController implements SVGConverterController {
    /**
     * Resource bundle.
     */
    private final ResourceBundle bundle = ResourceBundle
            .getBundle("net/trajano/batik/internal/Messages");

    /**
     * Fail on error.
     */
    private final boolean failOnError;

    /**
     * Maven log to write events to. Warnings are suppressed to prevent Sonar
     * from warning about the logger variable name.
     */
    @SuppressWarnings("all")
    private final Log mavenLogger;

    /**
     * Creates the controller. Warnings are suppressed to prevent Sonar from
     * warning about the logger variable name.
     * 
     * @param mavenLogger
     *            logger
     * @param failOnError
     *            fail on error.
     */
    @SuppressWarnings("all")
    public LoggingSvgConverterController(final Log mavenLogger,
            final boolean failOnError) {
        this.mavenLogger = mavenLogger;
        this.failOnError = failOnError;
    }

    /**
     * Invoked when the rasterizer successfully transcoded the input source.
     * 
     * @param source
     *            SVG source to convert
     * @param dest
     *            destination file
     */
    @Override
    public void onSourceTranscodingSuccess(final SVGConverterSource source,
            final File dest) {
        mavenLogger.info(String.format(bundle.getString("transcoded"),
                source.getName(), dest));
    }

    /**
     * Invoked when the rasterizer got an error while transcoding the input
     * source. The controller should return true if the transcoding process
     * should continue on other sources and it should return false if it should
     * not.
     * 
     * @param source
     *            SVG source to convert
     * @param dest
     *            destination file
     * @param errorCode
     *            see the {@link SVGConverter} error code descriptions.
     * @return true if the transcoding process should continue on other sources
     *         and it should return false if it should not.
     */
    @Override
    public boolean proceedOnSourceTranscodingFailure(
            final SVGConverterSource source, final File dest,
            final String errorCode) {
        mavenLogger.error(String.format(bundle.getString("transcodefailure"),
                source.getName(), dest, errorCode));
        return !failOnError;
    }

    /**
     * Invoked when the rasterizer has computed the exact description of what it
     * should do. The controller should return true if the transcoding process
     * should proceed or false otherwise.
     * 
     * @param transcoder
     *            Transcoder which will be used
     * @param hints
     *            set of hints that were set on the transcoder
     * @param sources
     *            list of SVG sources it will convert.
     * @param dest
     *            list of destination file it will use
     * @return true if the transcoding process should proceed or false
     *         otherwise.
     */
    @Override
    public boolean proceedWithComputedTask(final Transcoder transcoder,
            @SuppressWarnings("rawtypes") final Map hints,
            @SuppressWarnings("rawtypes") final List sources,
            @SuppressWarnings("rawtypes") final List dest) {
        return true;
    }

    /**
     * Invoked when the rasterizer is about to start transcoding of a given
     * source. The controller should return true if the source should be
     * transcoded and false otherwise.
     * 
     * @param source
     *            SVG source to convert
     * @param dest
     *            destination file
     * @return true if the source should be transcoded and false otherwise.
     */
    @Override
    public boolean proceedWithSourceTranscoding(
            final SVGConverterSource source, final File dest) {
        mavenLogger.info(String.format(bundle.getString("abouttotranscode"),
                source.getName(), dest));
        return true;
    }

}
