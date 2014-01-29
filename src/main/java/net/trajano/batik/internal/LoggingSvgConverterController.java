package net.trajano.batik.internal;

import java.io.File;
import java.util.List;
import java.util.Map;

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
     */
    @SuppressWarnings("all")
    public LoggingSvgConverterController(final Log mavenLogger) {
        this.mavenLogger = mavenLogger;
    }

    @Override
    public void onSourceTranscodingSuccess(final SVGConverterSource source,
            final File desFile) {
        // TODO use resources.
        mavenLogger.debug("transcoded " + source.getName() + " to " + desFile);
    }

    @Override
    public boolean proceedOnSourceTranscodingFailure(
            final SVGConverterSource arg0, final File arg1, final String arg2) {
        // TODO should make this configurable
        return false;
    }

    @Override
    public boolean proceedWithComputedTask(final Transcoder transcoder,
            @SuppressWarnings("rawtypes") final Map arg1,
            @SuppressWarnings("rawtypes") final List list1,
            @SuppressWarnings("rawtypes") final List list2) {
        return true;
    }

    @Override
    public boolean proceedWithSourceTranscoding(
            final SVGConverterSource source, final File desFile) {
        // TODO use resources.
        mavenLogger.info("about to transcode " + source.getName() + " to "
                + desFile);
        return true;
    }

}
