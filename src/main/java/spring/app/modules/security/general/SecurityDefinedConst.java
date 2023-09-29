package spring.app.modules.security.general;

public final class SecurityDefinedConst {

    // TODO: better naming for endpoints.
    // 1. Replace sport-maps with api (on both front and back)
    // 2. Consider using REST semantics for method naming, e.g.
    // "/api/v1/news/new/**" -> "/api/v1/news"
    // We already use POST method for this one. No need for /new.
    // Same for delete, like "/api/v1/news/delete/**" -> "/api/v1/news" with DELETE method.

    public static final String[] ALL = {
            "/api/v1/auth/**",
            "/api/v1/chat/**"
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
            "/api/v1/news/new/**",
            "/api/v1/news/photo/upload/**",
            "/api/v1/events/new/**",
            "/api/v1/events/photo/upload/**",
            "/api/v1/coaches/new/**",
            "/api/v1/coaches/photo/upload/**",
            "/api/v1/markers/add"
    };

    public static final String[] PUT = {
            "/api/v1/news/update/**",
            "/api/v1/events/update/**",
            "/api/v1/coaches/update/**",
            "/api/v1/news-comments/update/**",
            "/api/v1/event-comments/update/**",
            "/api/v1/forum-comments/update/**",
            "/api/v1/forums/update/**"
    };

    public static final String[] OPTIONS = {
            "/**"
    };

    public static final String[] DELETE = {
            "/api/v1/news/delete/**",
            "/api/v1/events/delete/**",
            "/api/v1/coaches/delete/**",
            "/api/v1/news-comments/delete/**",
            "/api/v1/event-comments/delete/**",
            "/api/v1/forum-comments/delete/**",
            "/api/v1/forums/delete/**",
            "/api/v1/markers/delete"
    };
}
