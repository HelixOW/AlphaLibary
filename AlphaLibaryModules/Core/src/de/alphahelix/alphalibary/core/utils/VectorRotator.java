package de.alphahelix.alphalibary.core.utils;

import org.bukkit.util.Vector;

public class VectorRotator {

    private double xAxisCos, xAxisSin;
    private double yAxisCos, yAxisSin;
    private double zAxisCos, zAxisSin;

    private double angleCos, angleSin;

    public VectorRotator(double xRot, double yRot, double zRot) {
        double xangle = Math.toRadians(xRot);
        xAxisCos = Math.cos(xangle);
        xAxisSin = Math.sin(xangle);

        double yangle = Math.toRadians(yRot);
        yAxisCos = Math.cos(-yangle);
        yAxisSin = Math.sin(-yangle);

        double zangle = Math.toRadians(zRot);
        zAxisCos = Math.cos(zangle);
        zAxisSin = Math.sin(zangle);
    }

    public VectorRotator(double angle) {
        angleCos = Math.cos(Math.toRadians(angle));
        angleSin = Math.sin(Math.toRadians(angle));
    }

    public Vector rotateAroundPoint(Vector toRotate, Vector point) {
        double x = toRotate.getX() - point.getX();
        double y = toRotate.getY() - point.getY();
        double z = toRotate.getZ() - point.getZ();

        double nX = x * angleCos - y * angleSin - z * angleSin;
        double nY = y * angleCos - x * angleSin - z * angleSin;
        double nZ = z * angleCos - x * angleSin - y * angleSin;

        return new Vector(nX, nY, nZ);
    }

    public Vector rotateAroundX(Vector toRotate) {
        return rotateAroundAxisX(toRotate, xAxisCos, xAxisSin);
    }

    public Vector rotateAroundY(Vector toRotate) {
        return rotateAroundAxisX(toRotate, yAxisCos, yAxisSin);
    }

    public Vector rotateAroundZ(Vector toRotate) {
        return rotateAroundAxisX(toRotate, zAxisCos, zAxisSin);
    }

    private Vector rotateAroundAxisX(Vector v, double cos, double sin) {
        double y = v.getY() * cos - v.getZ() * sin;
        double z = v.getY() * sin + v.getZ() * cos;
        return v.setY(y).setZ(z);
    }

    private Vector rotateAroundAxisY(Vector v, double cos, double sin) {
        double x = v.getX() * cos + v.getZ() * sin;
        double z = v.getX() * -sin + v.getZ() * cos;
        return v.setX(x).setZ(z);
    }

    private Vector rotateAroundAxisZ(Vector v, double cos, double sin) {
        double x = v.getX() * cos - v.getY() * sin;
        double y = v.getX() * sin + v.getY() * cos;
        return v.setX(x).setY(y);
    }
}
