/*
 * Copyright 2012, Asamm Software, s. r. o.
 *
 * This file is part of LocusAPI.
 *
 * LocusAPI is free software: you can redistribute it and/or modify
 * it under the terms of the Lesser GNU General Public License
 * as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * LocusAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Lesser GNU General Public License for more details.
 *
 * You should have received a copy of the Lesser GNU General Public
 * License along with LocusAPI. If not, see
 * <http://www.gnu.org/licenses/lgpl.html/>.
 */

package locus.api.objects.extra;

import java.io.IOException;
import java.util.ArrayList;

import locus.api.objects.Storable;
import locus.api.utils.DataReaderBigEndian;
import locus.api.utils.DataWriterBigEndian;
import locus.api.utils.Logger;

public class GeoDataStyle extends Storable {

    // tag for logger
    private static final String TAG = "GeoDataStyle";

    // style name - id in style tag
    private String mId;
    // style name - name tag inside style tag
    private String mName;

    // BALLON STYLE (not used yet)
    BalloonStyle balloonStyle;
    // ICON STYLE
    IconStyle iconStyle;
    // LABEL STYLE
    LabelStyle labelStyle;
    // LIST STYLE (not used yet)
    ListStyle listStyle;

    // line style system (line, polygon)
    private LineStyle mLineStyle;

    /**
     * Create new instance of style container.
     */
    public GeoDataStyle() {
        mId = "";
        mName = "";
        balloonStyle = null;
        iconStyle = null;
        labelStyle = null;
        listStyle = null;
        mLineStyle = null;
    }

    /**
     * Create new instance of style container with defined name.
     *
     * @param name name of style container
     */
    public GeoDataStyle(String name) {
        this();

        // set name
        if (name != null) {
            this.mName = name;
        }
    }

    //*************************************************
    // SETTERS & GETTERS
    //*************************************************

    // ID

    /**
     * Get unique ID of current style.
     *
     * @return style ID
     */
    public String getId() {
        return mId;
    }

    /**
     * Set new ID to current style.
     *
     * @param id new ID
     */
    public void setId(String id) {
        if (id == null) {
            id = "";
        }
        this.mId = id;
    }

    // NAME

    /**
     * Get current defined name for a style.
     *
     * @return name of style
     */
    public String getName() {
        return mName;
    }

    /**
     * Set new name for current style.
     *
     * @param name new name
     */
    public void setName(String name) {
        if (name == null) {
            name = "";
        }
        this.mName = name;
    }

    // ICON STYLE

    /**
     * Get defined style for icons.
     *
     * @return style for icons
     */
    public IconStyle getIconStyle() {
        return iconStyle;
    }

    /**
     * Get style Url for an icon.
     *
     * @return style Url/href parameter
     */
    public String getIconStyleIconUrl() {
        if (iconStyle == null) {
            return null;
        }
        return iconStyle.getIconHref();
    }

    public void setIconStyle(String iconUrl, float scale) {
        setIconStyle(iconUrl, COLOR_DEFAULT, 0.0f, scale);
    }

    public void setIconStyle(String iconUrl, int color, float heading, float scale) {
        iconStyle = new IconStyle();
        iconStyle.setIconHref(iconUrl);
        iconStyle.color = color;
        iconStyle.heading = heading;
        iconStyle.setScale(scale);
        // set hot spot
        setIconStyleHotSpot(HOTSPOT_BOTTOM_CENTER);
    }

    // definition of hotSpot of icon to bottom center
    public static final int HOTSPOT_BOTTOM_CENTER = 0;
    public static final int HOTSPOT_TOP_LEFT = 1;
    public static final int HOTSPOT_CENTER_CENTER = 2;

    public void setIconStyleHotSpot(int hotspot) {
        if (iconStyle == null) {
            Logger.logW(TAG, "setIconStyleHotSpot(" + hotspot + "), " +
                    "initialize IconStyle before settings hotSpot!");
            return;
        }

        if (hotspot == HOTSPOT_TOP_LEFT) {
            iconStyle.hotSpot = new KmlVec2(
                    0.0f, KmlVec2.Units.FRACTION, 1.0f, KmlVec2.Units.FRACTION);
        } else if (hotspot == HOTSPOT_CENTER_CENTER) {
            iconStyle.hotSpot = new KmlVec2(
                    0.5f, KmlVec2.Units.FRACTION, 0.5f, KmlVec2.Units.FRACTION);
        } else {
            // hotspot == HOTSPOT_BOTTOM_CENTER
            iconStyle.hotSpot = generateDefaultHotSpot();
        }
    }

    private static KmlVec2 generateDefaultHotSpot() {
        // HOTSPOT_BOTTOM_CENTER
        return new KmlVec2(0.5f, KmlVec2.Units.FRACTION,
                0.0f, KmlVec2.Units.FRACTION);
    }

    public void setIconStyleHotSpot(KmlVec2 vec2) {
        if (iconStyle == null || vec2 == null) {
            Logger.logW(TAG, "setIconStyleHotSpot(" + vec2 + "), " +
                    "initialize IconStyle before settings hotSpot or hotSpot is null!");
            return;
        }

        iconStyle.hotSpot = vec2;
    }

    // LABEL STYLE

    /**
     * Get current defined style of label.
     *
     * @return style of label
     */
    public LabelStyle getLabelStyle() {
        return labelStyle;
    }

    // LINE STYLE

    /**
     * Get current defined style for line and polygons.
     *
     * @return current line style
     */
    public LineStyle getLineStyle() {
        return mLineStyle;
    }

    /**
     * Set completely new style for lines.
     *
     * @param lineStyle new line style
     */
    public void setLineStyle(LineStyle lineStyle) {
        mLineStyle = lineStyle;
    }

    /**
     * Set parameters for style that draw a lines.
     *
     * @param color color of lines
     * @param width width of lines in pixels
     */
    public void setLineStyle(int color, float width) {
        // check if style exists
        if (mLineStyle == null) {
            mLineStyle = new LineStyle();
        }

        // set parameters
        mLineStyle.setColorBase(color);
        mLineStyle.setWidth(width);
    }

    /**
     * Set line style for drawing a polygons.
     *
     * @param color color of inner area
     */
    public void setPolyStyle(int color) {
        if (mLineStyle == null) {
            mLineStyle = new LineStyle();
            mLineStyle.setDrawBase(false);
        }
        mLineStyle.setDrawFill(true);
        mLineStyle.setColorFill(color);
    }

    //*************************************************
    // STYLES
    //*************************************************

    // shortcut to simple black color
    public static final int BLACK = 0xFF000000;
    // shortcut to simple white color
    public static final int WHITE = 0xFFFFFFFF;
    // default basic color
    public static final int COLOR_DEFAULT = WHITE;

    public static class BalloonStyle extends Storable {

        public enum DisplayMode {
            DEFAULT, HIDE
        }

        public int bgColor;
        public int textColor;
        public String text;
        public DisplayMode displayMode;

        public BalloonStyle() {
            bgColor = WHITE;
            textColor = BLACK;
            text = "";
            displayMode = DisplayMode.DEFAULT;
        }

        @Override
        protected int getVersion() {
            return 0;
        }

        @Override
        protected void readObject(int version, DataReaderBigEndian dr)
                throws IOException {
            bgColor = dr.readInt();
            textColor = dr.readInt();
            text = dr.readString();
            int mode = dr.readInt();
            if (mode < DisplayMode.values().length) {
                displayMode = DisplayMode.values()[mode];
            }
        }

        @Override
        protected void writeObject(DataWriterBigEndian dw) throws IOException {
            dw.writeInt(bgColor);
            dw.writeInt(textColor);
            dw.writeString(text);
            dw.writeInt(displayMode.ordinal());
        }
    }

    public static class IconStyle extends Storable {

        public int color;
        // scale of icon, where 1.0f means base no-scale value
        private float mScale;
        public float heading;
        @Deprecated // do not use directly
        public String iconHref;
        public KmlVec2 hotSpot;

        // temporary variables for Locus usage that are not serialized
        // and are for private Locus usage only
        public Object icon;
        public int iconW;
        public int iconH;
        public float scaleCurrent;

        /**
         * Default empty constructor.
         */
        public IconStyle() {
            color = COLOR_DEFAULT;
            mScale = 1.0f;
            heading = 0.0f;
            iconHref = null;
            hotSpot = generateDefaultHotSpot();

            icon = null;
            iconW = -1;
            iconH = -1;
            scaleCurrent = 1.0f;
        }

        // SCALE

        /**
         * Get current defined scale.
         *
         * @return scale value, base is 1.0f
         */
        public float getScale() {
            return mScale;
        }

        /**
         * Set new scale value.
         *
         * @param scale scale to set
         */
        public void setScale(float scale) {
            if (scale != 0.0f) {
                this.mScale = scale;
                this.scaleCurrent = scale;
            }
        }

        /**
         * Get link/id to image.
         *
         * @return href parameter
         */
        private String getIconHref() {
            return iconHref;
        }

        /**
         * Set link/id to image.
         *
         * @param iconHref href to image
         */
        public void setIconHref(String iconHref) {
            if (iconHref == null) {
                iconHref = "";
            }
            this.iconHref = iconHref;
        }

        // STORABLE PART

        @Override
        protected int getVersion() {
            return 0;
        }

        @Override
        protected void readObject(int version, DataReaderBigEndian dr)
                throws IOException {
            color = dr.readInt();
            mScale = dr.readFloat();
            heading = dr.readFloat();
            iconHref = dr.readString();
            hotSpot = KmlVec2.read(dr);
        }

        @Override
        protected void writeObject(DataWriterBigEndian dw) throws IOException {
            dw.writeInt(color);
            dw.writeFloat(mScale);
            dw.writeFloat(heading);
            dw.writeString(iconHref);
            hotSpot.write(dw);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("IconStyle [");
            sb.append("color:").append(color);
            sb.append(", scale:").append(mScale);
            sb.append(", heading:").append(heading);
            sb.append(", iconHref:").append(iconHref);
            sb.append(", hotSpot:").append(hotSpot);

            sb.append(", icon:").append(icon);
            sb.append(", iconW:").append(iconW);
            sb.append(", iconH:").append(iconH);
            sb.append(", scaleCurrent:").append(scaleCurrent);
            sb.append("]");
            return sb.toString();
        }
    }

    /**
     * Container for label style.
     */
    public static class LabelStyle extends Storable {

        // color of label
        private int mColor;
        // scale of label
        private float mScale;

        public LabelStyle() {
            mColor = COLOR_DEFAULT;
            mScale = 1.0f;
        }

        /**
         * Get color defined for a label.
         *
         * @return color of label
         */
        public int getColor() {
            return mColor;
        }

        /**
         * Set new color to this label.
         *
         * @param color new color
         */
        public void setColor(int color) {
            this.mColor = color;
        }

        /**
         * Get scale defined for a label.
         *
         * @return scale of label, where 1.0 equals 100%
         */
        public float getScale() {
            return mScale;
        }

        /**
         * Set new scale to this label.
         *
         * @param scale new scale value
         */
        public void setScale(float scale) {
            if (scale < 0.0f) {
                scale = 0.0f;
            }
            this.mScale = scale;
        }

        // STORABLE

        @Override
        protected int getVersion() {
            return 0;
        }

        @Override
        protected void readObject(int version, DataReaderBigEndian dr)
                throws IOException {
            mColor = dr.readInt();
            mScale = dr.readFloat();
        }

        @Override
        protected void writeObject(DataWriterBigEndian dw) throws IOException {
            dw.writeInt(mColor);
            dw.writeFloat(mScale);
        }
    }

    public static class ListStyle extends Storable {

        public enum ListItemType {
            CHECK, CHECK_OFF_ONLY, CHECK_HIDE_CHILDREN, RADIO_FOLDER
        }

        public static class ItemIcon {

            public enum State {
                OPEN, CLOSED, ERROR, FETCHING0, FETCHING1, FETCHING2
            }

            public State state = State.OPEN;
            public String href = "";
        }

        public ListItemType listItemType;
        public int bgColor;
        public ArrayList<ItemIcon> itemIcons;

        public ListStyle() {
            listItemType = ListItemType.CHECK;
            bgColor = WHITE;
            itemIcons = new ArrayList<>();
        }

        @Override
        protected int getVersion() {
            return 0;
        }

        @Override
        protected void readObject(int version, DataReaderBigEndian dis)
                throws IOException {
            int style = dis.readInt();
            if (style < ListStyle.ListItemType.values().length) {
                listItemType = ListStyle.ListItemType.values()[style];
            }
            bgColor = dis.readInt();
            int itemsCount = dis.readInt();
            for (int i = 0; i < itemsCount; i++) {
                ListStyle.ItemIcon itemIcon = new ListStyle.ItemIcon();
                int iconStyle = dis.readInt();
                if (iconStyle < ListStyle.ItemIcon.State.values().length) {
                    itemIcon.state = ListStyle.ItemIcon.State.values()[iconStyle];
                }
                itemIcon.href = dis.readString();
                itemIcons.add(itemIcon);
            }
        }

        @Override
        protected void writeObject(DataWriterBigEndian dw) throws IOException {
            dw.writeBoolean(true);
            dw.writeInt(listItemType.ordinal());
            dw.writeInt(bgColor);
            dw.writeInt(itemIcons.size());
            for (ListStyle.ItemIcon itemIcon : itemIcons) {
                dw.writeInt(itemIcon.state.ordinal());
                dw.writeString(itemIcon.href);
            }
        }
    }

    // DEPRECATED CONTAINERS

    /**
     * Style of lines.
     */
    @Deprecated
    public static class LineStyleOld extends Storable {

        /**
         * Special color style for a lines.
         */
        private enum ColorStyle {
            // simple coloring
            SIMPLE,
            // coloring by speed value
            BY_SPEED,
            // coloring (relative) by altitude value
            BY_ALTITUDE,
            // coloring (relative) by accuracy
            BY_ACCURACY,
            // coloring (relative) by speed change
            BY_SPEED_CHANGE,
            // coloring (relative) by slope
            BY_SLOPE_REL,
            // coloring (relative) by heart rate value
            BY_HRM,
            // coloring (relative) by cadence
            BY_CADENCE,
            // coloring (absolute) by slope
            BY_SLOPE_ABS
        }

        /**
         * Used units for line width.
         */
        private enum Units {
            PIXELS, METRES
        }

        /**
         * Type how line is presented to user.
         */
        private enum LineType {
            NORMAL, DOTTED, DASHED_1, DASHED_2, DASHED_3,
            SPECIAL_1, SPECIAL_2, SPECIAL_3,
            ARROW_1, ARROW_2, ARROW_3,
            CROSS_1, CROSS_2
        }

        // KML styles
        public int color;
        // width of line [px | m]
        private float mWidth;
        int gxOuterColor;
        float gxOuterWidth;
        /**
         * Not used. Instead of using this parameter, use {@link #mWidth} and define
         * {@link #units} to Units.METRES
         */
        @Deprecated
        float gxPhysicalWidth;
        boolean gxLabelVisibility;

        // Locus extension
        ColorStyle colorStyle;
        public Units units;
        LineType lineType;
        boolean drawOutline;
        int colorOutline;

        // temporary helper to convert old widths. Because of this, we increased version of object to V2,
        // so it's clear if value is in DPI or PX values.
        private int mObjectVersion;

        /**
         * Create new line style object.
         */
        public LineStyleOld() {
            mObjectVersion = getVersion();
            color = COLOR_DEFAULT;
            mWidth = 1.0f;
            gxOuterColor = COLOR_DEFAULT;
            gxOuterWidth = 0.0f;
            gxPhysicalWidth = 0.0f;
            gxLabelVisibility = false;

            // Locus extension
            colorStyle = ColorStyle.SIMPLE;
            units = Units.PIXELS;
            lineType = LineType.NORMAL;
            drawOutline = false;
            colorOutline = WHITE;
        }

        // WIDTH

        /**
         * Get width of line in units defined by {@link #units} parameter.
         *
         * @return line width
         */
        public float getWidth() {
            return mWidth;
        }

        /**
         * Set line width in units defined by {@link #units} parameter.
         *
         * @param width line width
         */
        public void setWidth(float width) {
            this.mWidth = width;
        }

        // WIDTH UNITS

        /**
         * Get line units.
         *
         * @return line units
         */
        public Units getUnits() {
            return units;
        }

        /**
         * Define line units.
         *
         * @param units line units
         */
        public void setUnits(Units units) {
            this.units = units;
        }

        // STORABLE

        @Override
        protected int getVersion() {
            return 2;
        }

        @Override
        protected void readObject(int version, DataReaderBigEndian dr)
                throws IOException {
            mObjectVersion = version;
            color = dr.readInt();
            mWidth = dr.readFloat();
            gxOuterColor = dr.readInt();
            gxOuterWidth = dr.readFloat();
            gxPhysicalWidth = dr.readFloat();
            gxLabelVisibility = dr.readBoolean();

            int cs = dr.readInt();
            if (cs < ColorStyle.values().length) {
                colorStyle = ColorStyle.values()[cs];
            }
            int un = dr.readInt();
            if (un < Units.values().length) {
                units = Units.values()[un];
            }
            int lt = dr.readInt();
            if (lt < LineType.values().length) {
                lineType = LineType.values()[lt];
            }

            // V1

            if (version >= 1) {
                drawOutline = dr.readBoolean();
                colorOutline = dr.readInt();
            }
        }

        @Override
        protected void writeObject(DataWriterBigEndian dw) throws IOException {
            // no write task is done
        }
    }

    @Deprecated
    public static class PolyStyleOld extends Storable {

        public int color;
        public boolean fill;
        public boolean outline;

        public PolyStyleOld() {
            color = COLOR_DEFAULT;
            fill = true;
            outline = true;
        }

        @Override
        protected int getVersion() {
            return 0;
        }

        @Override
        protected void readObject(int version, DataReaderBigEndian dis)
                throws IOException {
            color = dis.readInt();
            fill = dis.readBoolean();
            outline = dis.readBoolean();
        }

        @Override
        protected void writeObject(DataWriterBigEndian dw) throws IOException {
            // no write task is done
        }
    }

    //*************************************************
    // STORABLE
    //*************************************************

    @Override
    protected int getVersion() {
        return 2;
    }

    @Override
    protected void readObject(int version, DataReaderBigEndian dr)
            throws IOException {
        // read core
        mId = dr.readString();
        mName = dr.readString();

        // ignore old version, not compatible anymore
        if (version == 0) {
            return;
        }

        // balloon style
        LineStyleOld lineStyleOld = null;
        PolyStyleOld polyStyleOld = null;
        try {
            if (dr.readBoolean()) {
                balloonStyle = Storable.read(BalloonStyle.class, dr);
            }
            if (dr.readBoolean()) {
                iconStyle = Storable.read(IconStyle.class, dr);
            }
            if (dr.readBoolean()) {
                labelStyle = Storable.read(LabelStyle.class, dr);
            }
            if (dr.readBoolean()) {
                lineStyleOld = Storable.read(LineStyleOld.class, dr);
            }
            if (dr.readBoolean()) {
                listStyle = Storable.read(ListStyle.class, dr);
            }
            if (dr.readBoolean()) {
                polyStyleOld = Storable.read(PolyStyleOld.class, dr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // convert old style to new system
        mLineStyle = convertToNewLineStyle(lineStyleOld, polyStyleOld);

        // V2
        if (version >= 2) {
            if (dr.readBoolean()) {
                mLineStyle = new LineStyle();
                mLineStyle.read(dr);
            }
        }
    }

    @Override
    protected void writeObject(DataWriterBigEndian dw) throws IOException {
        // write core
        dw.writeString(mId);
        dw.writeString(mName);

        // balloon style
        if (balloonStyle == null) {
            dw.writeBoolean(false);
        } else {
            dw.writeBoolean(true);
            balloonStyle.write(dw);
        }

        // icon style
        if (iconStyle == null) {
            dw.writeBoolean(false);
        } else {
            dw.writeBoolean(true);
            iconStyle.write(dw);
        }

        // label style
        if (labelStyle == null) {
            dw.writeBoolean(false);
        } else {
            dw.writeBoolean(true);
            labelStyle.write(dw);
        }

        // line style (removed)
        dw.writeBoolean(false);

        // list style
        if (listStyle == null) {
            dw.writeBoolean(false);
        } else {
            dw.writeBoolean(true);
            listStyle.write(dw);
        }

        // poly style (removed)
        dw.writeBoolean(false);

        // V2
        if (mLineStyle == null) {
            dw.writeBoolean(false);
        } else {
            dw.writeBoolean(true);
            mLineStyle.write(dw);
        }
    }

    /**
     * Convert old line & poly styles to new system.
     *
     * @param ls old line style container
     * @param ps old poly style container
     * @return new converted style
     */
    private LineStyle convertToNewLineStyle(LineStyleOld ls, PolyStyleOld ps) {
        // check objects
        if (ls == null && ps == null) {
            return null;
        }

        // convert to new container
        LineStyle lsNew = new LineStyle();

        // re-use line style
        if (ls != null) {
            lsNew.setDrawBase(true);
            lsNew.setColorBase(ls.color);
            if (ls.lineType == GeoDataStyle.LineStyleOld.LineType.NORMAL) {
                lsNew.setDrawSymbol(false);
            } else {
                lsNew.setDrawSymbol(true);
                lsNew.setSymbol(LineStyle.Symbol.valueOf(ls.lineType.name()));
            }
            lsNew.setColoring(LineStyle.Coloring.valueOf(ls.colorStyle.name()));
            lsNew.setWidth(ls.getWidth());
            lsNew.setUnits(LineStyle.Units.valueOf(ls.units.name()));
            lsNew.setDrawOutline(ls.drawOutline);
            lsNew.setColorOutline(ls.colorOutline);
        } else {
            lsNew.setDrawBase(false);
        }

        // re-use poly style
        if (ps != null) {
            lsNew.setDrawFill(ps.fill);
            lsNew.setColorFill(ps.color);
        } else {
            lsNew.setDrawFill(false);
        }

        // return new style
        return lsNew;
    }
}

