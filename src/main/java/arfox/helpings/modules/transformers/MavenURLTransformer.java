package arfox.helpings.modules.transformers;

import arfox.helpings.modules.Module;
import arfox.helpings.modules.exceptions.MalformedMavenURLException;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class MavenURLTransformer extends URLTransformer {

    public static final String MAVEN_CENTRAL = "https://repo1.maven.org/maven2/";

    @Override
    public List<Module> transform(JavaPlugin plugin, String url) {
        return null;
    }

    @Override
    public String getIdentifier() {
        return "maven";
    }

    public static MavenInfo toMavenUrl(String url) {
        String[] parts = url.split(":");

        int readIndex = parts.length - 3;

        String groupId = parts[readIndex];
        String artifactId = parts[readIndex + 1];
        String version = parts[readIndex + 2];

        return switch (parts.length) {
            case 5 -> MavenInfo.of(reformatBaseUrl(parts[0] + ":" + parts[1]), groupId, artifactId, version);
            case 3 -> MavenInfo.of(MAVEN_CENTRAL, groupId, artifactId, version);
            default -> throw new MalformedMavenURLException("The URL \"%s\" is not recognized as a legal maven artifact.".formatted(url));
        };
    }

    public static String reformatBaseUrl(String url) {
        return url.equals("central") ? MAVEN_CENTRAL : (url.endsWith("/") ? url : (url + "/"));
    }

    public record MavenInfo(String url, String name) {

        public static MavenInfo of(String baseUrl, String groupId, String artifactId, String version) {
            String url = baseUrl + (groupId.replace('.', '/') + "/" + artifactId + "/" + version + "/");
            String name = "%s-%s.jar".formatted(artifactId, version);

            return new MavenInfo(url, name);
        }
    }
}
