package arfox.helpings.modules.exceptions;

public class PluginNotFoundException extends IllegalStateException {

    public PluginNotFoundException(String message) {
        super(message);
    }
}
