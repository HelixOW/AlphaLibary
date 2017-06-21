package de.alphahelix.alphalibary.addons.core;

import de.alphahelix.alphalibary.addons.core.exceptions.InvalidAddonDescriptionException;
import org.bukkit.plugin.PluginAwareness;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.AbstractConstruct;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class AddonDescriptionFile {

    private static final ThreadLocal<Yaml> YAML = new ThreadLocal<Yaml>() {
        @Override
        protected Yaml initialValue() {
            return new Yaml(new SafeConstructor() {
                {
                    this.yamlConstructors.put(null, new AbstractConstruct() {
                        @Override
                        public Object construct(final Node node) {
                            if (!node.getTag().startsWith("!@")) {
                                return SafeConstructor.undefinedConstructor.construct(node);
                            }
                            return new PluginAwareness() {
                                @Override
                                public String toString() {
                                    return node.toString();
                                }
                            };
                        }
                    });

                    for (final PluginAwareness.Flags flag : PluginAwareness.Flags.values()) {
                        this.yamlConstructors.put(new Tag("!@" + flag.name()), new AbstractConstruct() {
                            @Override
                            public PluginAwareness.Flags construct(final Node node) {
                                return flag;
                            }
                        });
                    }
                }
            });
        }
    };

    private String name, main, description, author, version;
    private List<String> depends = new ArrayList<>();

    public AddonDescriptionFile(InputStream stream) throws InvalidAddonDescriptionException {
        this.loadMap(this.asMap(YAML.get().load(stream)));
    }

    private Map<?, ?> asMap(Object object) throws InvalidAddonDescriptionException {
        if (object instanceof Map)
            return (Map<?, ?>) object;
        else
            throw new InvalidAddonDescriptionException((new StringBuilder()).append(object).append(" is not properly structured.").toString());
    }

    private void loadMap(Map<?, ?> map) throws InvalidAddonDescriptionException {
        try {
            this.name = map.get("name").toString();
            if (!this.name.matches("^[A-Za-z0-9 _.-]+$"))
                throw new InvalidAddonDescriptionException((new StringBuilder("name '")).append(this.name).append("' contains invalid characters.")
                        .toString());
            this.name = this.name.replace(' ', '_');
        } catch (NullPointerException ex) {
            throw new InvalidAddonDescriptionException(ex, "name is not defined");
        } catch (ClassCastException ex) {
            throw new InvalidAddonDescriptionException(ex, "name is of wrong type");
        }
        try {
            this.version = map.get("version").toString();
        } catch (NullPointerException ex) {
            throw new InvalidAddonDescriptionException(ex, "version is not defined");
        } catch (ClassCastException ex) {
            throw new InvalidAddonDescriptionException(ex, "version is of wrong type");
        }
        try {
            this.main = map.get("main").toString();
        } catch (NullPointerException ex) {
            throw new InvalidAddonDescriptionException(ex, "main is not defined");
        } catch (ClassCastException ex) {
            throw new InvalidAddonDescriptionException(ex, "main is of wrong type");
        }

        if (map.get("description") != null)
            this.description = map.get("description").toString();

        if (map.get("depends") != null)
            this.depends = Arrays.asList(map.get("depends").toString().split(", "));

        if (map.get("author") != null)
            this.author = map.get("author").toString();
        else
            this.author = "Unknown";
    }

    public String getName() {
        return name;
    }

    public String getMain() {
        return main;
    }

    public String getDescription() {
        return description;
    }

    public String getAuthor() {
        return author;
    }

    public String getVersion() {
        return version;
    }
}
