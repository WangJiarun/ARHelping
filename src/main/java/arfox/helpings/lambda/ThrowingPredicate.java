package arfox.helpings.lambda;

import java.util.function.Consumer;
import java.util.function.Predicate;

@FunctionalInterface
public interface ThrowingPredicate<T> {
    boolean accept(T in) throws Throwable;

    static <T> Predicate<T> of(ThrowingPredicate<T> throwing) {
        return of(throwing, false);
    }

    static <T> Predicate<T> of(ThrowingPredicate<T> throwing, boolean def) {
        return (T) -> {
            try {
                return throwing.accept(T);
            } catch (Throwable e) {
                e.printStackTrace();
            }

            return def;
        };
    }
}
