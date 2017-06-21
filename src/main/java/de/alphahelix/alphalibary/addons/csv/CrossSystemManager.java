package de.alphahelix.alphalibary.addons.csv;

import java.util.ArrayList;

public class CrossSystemManager {

    private static ArrayList<CrossSystemVariable> crossSystemVariables = new ArrayList<>();

    public static void addVar(CrossSystemVariable crossSystemVariable) {
        crossSystemVariables.add(crossSystemVariable);
    }

    public static boolean hasVar(String name) {
        return getVar(name) != null;
    }

    public static String getVar(String name) {
        for (CrossSystemVariable vars : crossSystemVariables) {
            if (vars.name().equals(name)) return vars.value();
        }
        return null;
    }
}
