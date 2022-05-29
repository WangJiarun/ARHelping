package arfox.helpings.lambda;

import java.util.function.Function;

@FunctionalInterface
public interface ThrowingFunction<T, F> {
    F accept(T in) throws Throwable;

    static <T, F> Function<T, F> of(ThrowingFunction<T, F> throwing) {
        return (T) -> {
            try {
                return throwing.accept(T);
            } catch (Throwable e) {
                e.printStackTrace();
            }

            return null;
        };
    }
}
