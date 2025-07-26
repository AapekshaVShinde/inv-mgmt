package com.inventory.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CommonConstants {

    @UtilityClass
    public static class ApiPaths {
        public final String BASE_API_PATH = "/api";
        public final String BASE_API_VERSION = "/v1";
        public final String BASE_API_PATH_WITH_VERSION = BASE_API_PATH + BASE_API_VERSION;
    }

}
