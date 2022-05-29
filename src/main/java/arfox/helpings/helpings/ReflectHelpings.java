package arfox.helpings.helpings;

import arfox.helpings.lambda.ThrowingPass;

import java.lang.reflect.Field;

public class ReflectHelpings {
    public static <T> T getObjectField(Object object, String field, Class<T> tClass) {
        return ThrowingPass.of(() -> {
            Field objectField = object.getClass().getDeclaredField(field);
            objectField.setAccessible(true);

            return (T) objectField.get(object);
        });
    }
}
