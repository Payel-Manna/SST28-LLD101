package com.example.map;

/**
 * CURRENT STATE (BROKEN ON PURPOSE):
 * Each marker owns a private MarkerStyle created via 'new'.
 * This means identical styles are duplicated across thousands of markers.
 *
 * TODO (student):
 * - Store intrinsic state as a shared MarkerStyle obtained from MarkerStyleFactory.
 * - Keep only extrinsic state here: lat, lng, label.
 */
//Extrinsic=>unique
public class MapMarker {

    private final double lat;
    private final double lng;
    private final String label;

    // BROKEN: style is created per marker; should be shared
    private final MarkerStyle style;
     //Instead of MapMarker(lat,lng,label,shape,color,size,filled) pass style obj
    public MapMarker(double lat, double lng, String label,MarkerStyle style) {
        this.lat = lat;
        this.lng = lng;
        this.label = label;
        this.style=style;

        // // BROKEN: per-marker allocation
        // this.style = new MarkerStyle(shape, color, size, filled);
    }

    public double getLat() { return lat; }
    public double getLng() { return lng; }
    public String getLabel() { return label; }
    public MarkerStyle getStyle() { return style; }
}
