package utils;

import java.util.List;

public class TestUtils {

    public static long generateNonExistentId(List<Integer> existingIds) {
        long nonExistentId = 999999L; // Default high value
        if (existingIds != null && !existingIds.isEmpty()) {
            // Find the max ID and add 1 to ensure it's unique
            nonExistentId = existingIds.stream()
                    .mapToInt(Integer::intValue)
                    .max()
                    .getAsInt() + 1;
        }
        return nonExistentId;
    }
}
