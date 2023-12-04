package sport_maps.security.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sport_maps.security.dao.RefreshTokenDao;
import sport_maps.security.dao.VerificationTokenDao;

import java.time.Instant;

@Service
@Transactional
public class TokensPurgeTask {
    private final VerificationTokenDao verificationTokenDao;
    private final RefreshTokenDao refreshTokenDao;

    public TokensPurgeTask(VerificationTokenDao verificationTokenDao, RefreshTokenDao refreshTokenDao) {
        this.verificationTokenDao = verificationTokenDao;
        this.refreshTokenDao = refreshTokenDao;
    }

    @Scheduled(cron = "${task.purge}")
    public void purgeExpired() {
        verificationTokenDao.deleteAllByExpiryDateLessThan(Instant.now());
        refreshTokenDao.deleteAllByExpiryDateLessThan(Instant.now());
    }
}
