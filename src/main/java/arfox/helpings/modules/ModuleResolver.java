package arfox.helpings.modules;

import arfox.helpings.helpings.FileHelpings;
import arfox.helpings.lambda.ThrowingFunction;
import arfox.helpings.lambda.ThrowingIgnore;
import arfox.helpings.modules.transformers.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class ModuleResolver {
    public static final HashMap<String, URLTransformer> TRANSFORMERS = Maps.newHashMap();

    public static final String MODULES_FOLDER = "./bundler/modules/";

    private final ArrayList<Module> installStreams;

    public ModuleResolver(JavaPlugin plugin, InputStream stream) {
        HashMap<String, ?> rawURLs = new Gson().fromJson(new InputStreamReader(stream), ModuleDeclarationFile.class).modules;
        installStreams = rawURLs.keySet().stream().map(key -> {
            Object value = rawURLs.get(key);
            boolean advanced = !(value instanceof String);
            String transformer = (value instanceof String) ? ((String) value) : getAdvancedParameter(value, "transformer", String.class);

            if (advanced && hasAdvancedParameter(value, "passIf") && ThrowingIgnore.ifThrow(() -> Class.forName(getAdvancedParameter(value, "passIf", String.class)), false)) {
                Bukkit.getLogger().info("Passing module: %s cuz class %s is loaded.".formatted(key, getAdvancedParameter(value, "passIf", String.class)));
                return Lists.<Module>newArrayList();
            }

            return ModuleResolver.TRANSFORMERS.get(transformer).transform(plugin, key);
        }).flatMap(Collection::stream).collect(Collectors.toCollection(ArrayList::new));
    }

    private boolean hasAdvancedParameter(Object map, String parameter) {
        return ((Map) map).containsKey(parameter);
    }

    private <T> T getAdvancedParameter(Object map, String parameter, Class<T> tClass) {
        return ((Map<?, T>) map).get(parameter);
    }

    public List<URL> installModule() {
        FileHelpings.createFolder(Path.of(MODULES_FOLDER));

        return installStreams.stream().map(ThrowingFunction.of(module -> {
            Path path = Path.of(ModuleResolver.MODULES_FOLDER, module.name());

            if (Files.notExists(path)) {
                Bukkit.getLogger().info("Installing Module %s".formatted(module.name()));
                Files.copy(module.stream(), path);
            }

            return path.toUri().toURL();
        })).collect(Collectors.toList());
    }

    public static class ModuleDeclarationFile {
        public HashMap<String, ?> modules;
    }

    public static void registerTransformer(URLTransformer transformer) {
        Bukkit.getLogger().info("Registering Module URL Transformer: %s".formatted(transformer.getClass().getName()));
        ModuleResolver.TRANSFORMERS.put(transformer.getIdentifier(), transformer);
    }

    static {
        registerTransformer(new DirectURLTransformer());
        registerTransformer(new InternalURLTransformer());
        registerTransformer(new MavenDirectURLTransformer());
        registerTransformer(new MavenURLTransformer());
    }
}
