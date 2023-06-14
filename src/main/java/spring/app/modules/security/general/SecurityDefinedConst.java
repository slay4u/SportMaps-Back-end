package spring.app.modules.security.general;

public final class SecurityDefinedConst {

    // TODO: better naming for endpoints.
    // 1. Replace sport-maps with api (on both front and back)
    // 2. Consider using REST semantics for method naming, e.g.
    // "/sport-maps/v1/news/new/**" -> "/sport-maps/v1/news"
    // We already use POST method for this one. No need for /new.
    // Same for delete, like "/sport-maps/v1/news/delete/**" -> "/sport-maps/v1/news" with DELETE method.

    public static final String[] ALL = {
            "/sport-maps/v1/auth/**",
            "/sport-maps/v1/chat/**"
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
            "/sport-maps/v1/news/new/**",
            "/sport-maps/v1/news/photo/upload/**",
            "/sport-maps/v1/events/new/**",
            "/sport-maps/v1/events/photo/upload/**",
            "/sport-maps/v1/coaches/new/**",
            "/sport-maps/v1/coaches/photo/upload/**",
            "/sport-maps/v1/markers/add"
    };

    public static final String[] PUT = {
            "/sport-maps/v1/news/update/**",
            "/sport-maps/v1/events/update/**",
            "/sport-maps/v1/coaches/update/**",
            "/sport-maps/v1/news-comments/update/**",
            "/sport-maps/v1/event-comments/update/**",
            "/sport-maps/v1/forum-comments/update/**",
            "/sport-maps/v1/forums/update/**"
    };

    public static final String[] OPTIONS = {
            "/**"
    };

    public static final String[] DELETE = {
            "/sport-maps/v1/news/delete/**",
            "/sport-maps/v1/events/delete/**",
            "/sport-maps/v1/coaches/delete/**",
            "/sport-maps/v1/news-comments/delete/**",
            "/sport-maps/v1/event-comments/delete/**",
            "/sport-maps/v1/forum-comments/delete/**",
            "/sport-maps/v1/forums/delete/**",
            "/sport-maps/v1/markers/delete"
    };
}
