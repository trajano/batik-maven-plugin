package net.trajano.batik.internal;

import java.io.PrintStream;

import javax.annotation.Nullable;

import org.apache.batik.svggen.font.Font;
import org.apache.batik.svggen.font.SVGFont;

/**
 * {@link SVGFont} with the {@link SVGFont#main(String[])} method rewritten as a
 * function.
 * 
 * @author Archimedes Trajano
 */
public final class SvgFontUtil extends SVGFont {
    /**
     * Writes the font as an SVG file. This is a convenience method that passes
     * typical values that are used for the other parameters. Warnings are
     * suppressed to prevent Sonar from complaining about throwing an
     * {@link Exception}.
     * 
     * @param ps
     *            print stream to write to. (does not get closed)
     * @param font
     *            font to render
     * @param id
     *            ID of the font
     * @throws Exception
     */
    @SuppressWarnings("all")
    public static void writeFontAsSvg(final PrintStream ps, final Font font,
            final String id) throws Exception {
        writeFontAsSvg(ps, font, id, -1, -1, true, false);
    }

    /**
     * Writes the font as an SVG file. This is from a segment of code from
     * {@link SVGFont#main(String[])}. Warnings are suppressed to prevent Sonar
     * from complaining about throwing an {@link Exception}.
     * 
     * @param ps
     *            print stream to write to. (does not get closed)
     * @param font
     *            font to render
     * @param id
     *            ID of the font
     * @param first
     *            first character index to render, may be -1
     * @param last
     *            last character index to render, may be -1
     * @param autoRange
     *            find out the range automatically
     * @param forceAscii
     *            force ASCII range
     * @throws Exception
     */
    @SuppressWarnings("all")
    public static void writeFontAsSvg(final PrintStream ps, final Font font,
            @Nullable final String id, final int first, final int last,
            final boolean autoRange, final boolean forceAscii) throws Exception {
        writeSvgBegin(ps);
        writeSvgDefsBegin(ps);
        writeFontAsSVGFragment(ps, font, id, first, last, autoRange, forceAscii);
        writeSvgDefsEnd(ps);
        writeSvgEnd(ps);
    }

    /**
     * Prevent instantiation of utility class.
     */
    private SvgFontUtil() {

    }
}
