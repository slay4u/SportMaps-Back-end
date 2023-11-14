package sport_maps.security.general;

import static sport_maps.commons.BaseController.BASE_URL;

public final class SecurityDefinedConst {
    public static final String[] ALL = {
            BASE_URL + "/auth/**"
    };

    public static final String[] GET = {
            // -- Swagger UI v3 (OpenAPI)
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/swagger-ui.html/**",
            "/swagger-ui/index.html",
            "/swagger-ui/index.html/**"
    };

    public static final String[] POST = {
            BASE_URL + "/news/**",
            BASE_URL + "/news/upload/**",
            BASE_URL + "/events/**",
            BASE_URL + "/events/upload/**",
            BASE_URL + "/coaches/**",
            BASE_URL + "/coaches/upload/**",
            BASE_URL + "/markers/add"
    };

    public static final String[] PUT = {
            BASE_URL + "/news/**",
            BASE_URL + "/events/**",
            BASE_URL + "/coaches/**",
            BASE_URL + "/news-comments/**",
            BASE_URL + "/event-comments/**",
            BASE_URL + "/forum-comments/**",
            BASE_URL + "/forums/**"
    };

    public static final String[] OPTIONS = {
            "/**"
    };

    public static final String[] DELETE = {
            BASE_URL + "/news/**",
            BASE_URL + "/events/**",
            BASE_URL + "/coaches/**",
            BASE_URL + "/news-comments/**",
            BASE_URL + "/event-comments/**",
            BASE_URL + "/forum-comments/**",
            BASE_URL + "/forums/**",
            BASE_URL + "/markers/delete"
    };
}
