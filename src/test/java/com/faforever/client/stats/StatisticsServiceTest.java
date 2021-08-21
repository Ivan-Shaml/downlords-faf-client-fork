package com.faforever.client.stats;

import com.faforever.client.api.FafApiAccessor;
import com.faforever.client.builders.LeaderboardBeanBuilder;
import com.faforever.client.builders.PlayerBeanBuilder;
import com.faforever.client.domain.LeaderboardBean;
import com.faforever.client.domain.PlayerBean;
import com.faforever.client.mapstruct.LeaderboardMapper;
import com.faforever.client.mapstruct.MapperSetup;
import com.faforever.client.test.ElideMatchers;
import com.faforever.client.test.ServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import reactor.core.publisher.Flux;

import static com.faforever.commons.api.elide.ElideNavigator.qBuilder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class StatisticsServiceTest extends ServiceTest {

  @Mock
  private FafApiAccessor fafApiAccessor;

  private StatisticsService instance;
  private LeaderboardBean leaderboard;
  private final LeaderboardMapper leaderboardMapper = Mappers.getMapper(LeaderboardMapper.class);

  @BeforeEach
  public void setUp() throws Exception {
    MapperSetup.injectMappers(leaderboardMapper);
    when(fafApiAccessor.getMaxPageSize()).thenReturn(10000);
    leaderboard = LeaderboardBeanBuilder.create().defaultValues().get();
    instance = new StatisticsService(fafApiAccessor, leaderboardMapper);
  }

  @Test
  public void testGetStatisticsForPlayer() throws Exception {
    PlayerBean player = PlayerBeanBuilder.create().defaultValues().username("junit").get();
    when(fafApiAccessor.getMany(any())).thenReturn(Flux.empty());
    instance.getRatingHistory(player, leaderboard);
    verify(fafApiAccessor).getMany(argThat(
        ElideMatchers.hasFilter(qBuilder().intNum("gamePlayerStats.player.id").eq(player.getId()).and()
            .intNum("leaderboard.id").eq(leaderboard.getId()))
    ));
    verify(fafApiAccessor).getMany(argThat(ElideMatchers.hasPageSize(10000)));
  }
}
