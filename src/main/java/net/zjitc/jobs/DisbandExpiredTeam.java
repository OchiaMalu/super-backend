package net.zjitc.jobs;

import net.zjitc.model.domain.Team;
import net.zjitc.service.TeamService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static net.zjitc.constants.RedissonConstant.DISBAND_EXPIRED_TEAM_LOCK;

public class DisbandExpiredTeam extends QuartzJobBean {
    @Resource
    private RedissonClient redissonClient;

    @Resource
    private TeamService teamService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        RLock lock = redissonClient.getLock(DISBAND_EXPIRED_TEAM_LOCK);
        try {
            if (lock.tryLock(0, -1, TimeUnit.MICROSECONDS)) {
                List<Team> teamList = teamService.list();
                ZoneId zoneId = ZoneId.systemDefault();
                for (Team team : teamList) {
                    Date time = team.getExpireTime();
                    if (time == null) {
                        continue;
                    }
                    Instant instant = time.toInstant();
                    LocalDateTime expireTime = LocalDateTime.ofInstant(instant, zoneId);
                    if (LocalDateTime.now().isAfter(expireTime)) {
                        teamService.deleteTeam(team.getId(), null, true);
                    }
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                System.out.println("unLock: " + Thread.currentThread().getId());
                lock.unlock();
            }
        }
    }
}
