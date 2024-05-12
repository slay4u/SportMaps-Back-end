package sport_maps;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import sport_maps.coach.controller.CoachController;
import sport_maps.comments.controller.EventCommentController;
import sport_maps.comments.controller.ForumCommentController;
import sport_maps.comments.controller.NewsCommentController;
import sport_maps.mail.EmailService;
import sport_maps.nef.controller.EventController;
import sport_maps.nef.controller.ForumController;
import sport_maps.nef.controller.NewsController;
import sport_maps.security.controller.AuthenticationController;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AppTest {
    @Test
    void contextLoads(ApplicationContext context) {
        assertThat(context).isNotNull();
    }

    @Test
    void hasAuthControllerConfigured(ApplicationContext context) {
        assertThat(context.getBean(AuthenticationController.class)).isNotNull();
    }

    @Test
    void hasCoachControllerConfigured(ApplicationContext context) {
        assertThat(context.getBean(CoachController.class)).isNotNull();
    }

    @Test
    void hasForumControllerConfigured(ApplicationContext context) {
        assertThat(context.getBean(ForumController.class)).isNotNull();
    }

    @Test
    void hasEventControllerConfigured(ApplicationContext context) {
        assertThat(context.getBean(EventController.class)).isNotNull();
    }

    @Test
    void hasNewsControllerConfigured(ApplicationContext context) {
        assertThat(context.getBean(NewsController.class)).isNotNull();
    }

    @Test
    void hasNewsCommentControllerConfigured(ApplicationContext context) {
        assertThat(context.getBean(NewsCommentController.class)).isNotNull();
    }

    @Test
    void hasForumCommentControllerConfigured(ApplicationContext context) {
        assertThat(context.getBean(ForumCommentController.class)).isNotNull();
    }

    @Test
    void hasEventCommentControllerConfigured(ApplicationContext context) {
        assertThat(context.getBean(EventCommentController.class)).isNotNull();
    }

    @Test
    void hasEmailServiceConfigured(ApplicationContext context) {
        assertThat(context.getBean(EmailService.class)).isNotNull();
    }
}
