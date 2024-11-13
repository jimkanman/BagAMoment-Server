package com.jkm.jimkanman.util;

import com.jkm.jimkanman.domain.Point;
import org.springframework.stereotype.Component;

@Component
public interface GeometryUtil {
    Point convertToPoint(String address);
}
