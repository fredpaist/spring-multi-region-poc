package ee.fredpaist.multiregion.configuration;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RegionContext {

    protected static final ThreadLocal<String> threadLocal = new InheritableThreadLocal<>() {
        @Override
        protected String initialValue() {
            try {
                return String.class.getDeclaredConstructor().newInstance();
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        protected String childValue(String parentValue) {
            return parentValue;
        }
    };

    public static void setRegionId(String regionId) {
        threadLocal.set(regionId);
    }

    public static String getRegionId() {
        return threadLocal.get();
    }

    public static void clear() {
        threadLocal.remove();
    }
}
