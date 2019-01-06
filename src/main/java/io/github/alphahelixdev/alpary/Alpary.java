package io.github.alphahelixdev.alpary;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.alphahelixdev.alpary.addons.AddonCore;
import io.github.alphahelixdev.alpary.annotations.AnnotationHandler;
import io.github.alphahelixdev.alpary.utilities.UUIDFetcher;
import io.github.alphahelixdev.helius.Helius;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

public class Alpary extends JavaPlugin {

    private static Alpary instance;
    private final Reflections reflections = new Reflections();
    private final GsonBuilder gsonBuilder = new GsonBuilder();
    private final AnnotationHandler annotationHandler = new AnnotationHandler();
    private UUIDFetcher uuidFetcher;

    public static Alpary getInstance() {
        return Alpary.instance;
    }

    @Override
    public void onEnable() {
        Helius.main(new String[0]);
        Alpary.instance = this;

        this.uuidFetcher = new UUIDFetcher();
        new AddonCore().enable();

        this.annotationHandler.registerListeners();
        this.annotationHandler.createSingletons();
    }

    public Reflections reflections() {
        return this.reflections;
    }

    public GsonBuilder gsonBuilder() {
        return this.gsonBuilder;
    }

    public Gson gson() {
        return this.gsonBuilder.create();
    }

    public UUIDFetcher uuidFetcher() {
        return this.uuidFetcher;
    }

    public AnnotationHandler annotationHandler() {
        return this.annotationHandler;
    }
}