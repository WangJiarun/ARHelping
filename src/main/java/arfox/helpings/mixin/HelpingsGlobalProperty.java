package arfox.helpings.mixin;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.spongepowered.asm.service.IGlobalPropertyService;
import org.spongepowered.asm.service.IPropertyKey;

import java.util.HashMap;

public class HelpingsGlobalProperty implements IGlobalPropertyService {

    private final HashMap<String, IPropertyKey> keyMap;
    private final HashMap<IPropertyKey, Object> propertyMap;

    private record KeyImpl(String key) implements IPropertyKey {
        @Override
        public int hashCode() {
            return new HashCodeBuilder().append(key()).hashCode();
        }
    }

    public HelpingsGlobalProperty() {
        keyMap = Maps.newHashMap();
        propertyMap = Maps.newHashMap();
    }

    @Override
    public IPropertyKey resolveKey(String s) {
        return keyMap.computeIfAbsent(s, KeyImpl::new);
    }

    @Override
    public <T> T getProperty(IPropertyKey iPropertyKey) {
        return getProperty(iPropertyKey, null);
    }

    @Override
    public void setProperty(IPropertyKey iPropertyKey, Object o) {
        propertyMap.put(iPropertyKey, o);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getProperty(IPropertyKey iPropertyKey, T t) {
        return (T) propertyMap.getOrDefault(iPropertyKey, t);
    }

    @Override
    public String getPropertyString(IPropertyKey iPropertyKey, String s) {
        return getProperty(iPropertyKey, s);
    }
}
