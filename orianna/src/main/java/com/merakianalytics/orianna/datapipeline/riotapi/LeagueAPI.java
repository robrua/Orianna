package com.merakianalytics.orianna.datapipeline.riotapi;

import java.util.Iterator;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.merakianalytics.datapipelines.PipelineContext;
import com.merakianalytics.datapipelines.iterators.CloseableIterator;
import com.merakianalytics.datapipelines.iterators.CloseableIterators;
import com.merakianalytics.datapipelines.sources.Get;
import com.merakianalytics.datapipelines.sources.GetMany;
import com.merakianalytics.orianna.datapipeline.common.HTTPClient;
import com.merakianalytics.orianna.datapipeline.common.Utilities;
import com.merakianalytics.orianna.datapipeline.common.rates.RateLimiter;
import com.merakianalytics.orianna.types.common.Platform;
import com.merakianalytics.orianna.types.common.Queue;
import com.merakianalytics.orianna.types.common.Tier;
import com.merakianalytics.orianna.types.dto.league.LeagueList;
import com.merakianalytics.orianna.types.dto.league.LeaguePosition;
import com.merakianalytics.orianna.types.dto.league.SummonerLeagues;
import com.merakianalytics.orianna.types.dto.league.SummonerPositions;

public class LeagueAPI extends RiotAPI.Service {
    private static final Map<Tier, String> LEAGUE_LIST_ENDPOINTS = ImmutableMap.of(Tier.CHALLENGER, "lol/league/v3/challengerleagues/by-queue/",
        Tier.MASTER, "lol/league/v3/masterleagues/by-queue/");

    public LeagueAPI(final String key, final Map<Platform, RateLimiter> applicationRateLimiters, final RateLimiter.Type limitingType,
                     final double limitingShare, final HTTPClient client, final Configuration config) {
        super(key, applicationRateLimiters, limitingType, limitingShare, client, config);
    }

    @Get(LeagueList.class)
    public LeagueList getLeagueList(final Map<String, Object> query, final PipelineContext context) {
        final Platform platform = (Platform)query.get("platform");
        final Tier tier = (Tier)query.get("tier");
        final Queue queue = (Queue)query.get("queue");
        Utilities.checkNotNull(platform, "platform", tier, "tier", queue, "queue");

        if(!LEAGUE_LIST_ENDPOINTS.containsKey(tier)) {
            return null;
        }

        if(!Queue.RANKED.contains(queue)) {
            return null;
        }

        final String endpoint = LEAGUE_LIST_ENDPOINTS.get(tier) + queue;
        final LeagueList data = get(LeagueList.class, endpoint, platform, LEAGUE_LIST_ENDPOINTS.get(tier) + "/queue");

        data.setPlatform(platform.getTag());
        data.setSummonerId(-1L);
        return data;
    }

    @SuppressWarnings("unchecked")
    @GetMany(LeagueList.class)
    public CloseableIterator<LeagueList> getManyLeagueList(final Map<String, Object> query, final PipelineContext context) {
        final Platform platform = (Platform)query.get("platform");
        final Tier tier = (Tier)query.get("tier");
        final Iterable<Queue> queues = (Iterable<Queue>)query.get("queues");
        Utilities.checkNotNull(platform, "platform", tier, "tier", queues, "queues");

        if(!LEAGUE_LIST_ENDPOINTS.containsKey(tier)) {
            return CloseableIterators.empty();
        }

        final Iterator<Queue> iterator = queues.iterator();
        return CloseableIterators.from(new Iterator<LeagueList>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public LeagueList next() {
                final Queue queue = iterator.next();

                if(!Queue.RANKED.contains(queue)) {
                    return null;
                }

                final String endpoint = LEAGUE_LIST_ENDPOINTS.get(tier) + queue;
                final LeagueList data = get(LeagueList.class, endpoint, platform, LEAGUE_LIST_ENDPOINTS.get(tier) + "/queue");

                data.setPlatform(platform.getTag());
                data.setSummonerId(-1L);
                return data;
            }
        });
    }

    @SuppressWarnings("unchecked")
    @GetMany(SummonerLeagues.class)
    public CloseableIterator<SummonerLeagues> getManySummonerLeagues(final Map<String, Object> query, final PipelineContext context) {
        final Platform platform = (Platform)query.get("platform");
        final Iterable<Number> summonerIds = (Iterable<Number>)query.get("summonerIds");
        Utilities.checkNotNull(platform, "platform", summonerIds, "summonerIds");

        final Iterator<Number> iterator = summonerIds.iterator();
        return CloseableIterators.from(new Iterator<SummonerLeagues>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public SummonerLeagues next() {
                final Number summonerId = iterator.next();

                final String endpoint = "lol/league/v3/leagues/by-summoner/" + summonerId;
                final SummonerLeagues data = get(SummonerLeagues.class, endpoint, platform, "lol/league/v3/leagues/by-summoner/summonerId");

                data.setSummonerId(summonerId.longValue());
                data.setPlatform(platform.getTag());
                for(final LeagueList list : data) {
                    list.setSummonerId(summonerId.longValue());
                    list.setPlatform(platform.getTag());
                }
                return data;
            }
        });
    }

    @SuppressWarnings("unchecked")
    @GetMany(SummonerPositions.class)
    public CloseableIterator<SummonerPositions> getManySummonerPositions(final Map<String, Object> query, final PipelineContext context) {
        final Platform platform = (Platform)query.get("platform");
        final Iterable<Number> summonerIds = (Iterable<Number>)query.get("summonerIds");
        Utilities.checkNotNull(platform, "platform", summonerIds, "summonerIds");

        final Iterator<Number> iterator = summonerIds.iterator();
        return CloseableIterators.from(new Iterator<SummonerPositions>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public SummonerPositions next() {
                final Number summonerId = iterator.next();

                final String endpoint = "lol/league/v3/positions/by-summoner/" + summonerId;
                final SummonerPositions data = get(SummonerPositions.class, endpoint, platform, "lol/league/v3/positions/by-summoner/summonerId");

                data.setSummonerId(summonerId.longValue());
                data.setPlatform(platform.getTag());
                for(final LeaguePosition position : data) {
                    position.setPlatform(platform.getTag());
                }
                return data;
            }
        });
    }

    @Get(SummonerLeagues.class)
    public SummonerLeagues getSummonerLeagues(final Map<String, Object> query, final PipelineContext context) {
        final Platform platform = (Platform)query.get("platform");
        final Number summonerId = (Number)query.get("summonerId");
        Utilities.checkNotNull(platform, "platform", summonerId, "summonerId");

        final String endpoint = "lol/league/v3/leagues/by-summoner/" + summonerId;
        final SummonerLeagues data = get(SummonerLeagues.class, endpoint, platform, "lol/league/v3/leagues/by-summoner/summonerId");

        data.setSummonerId(summonerId.longValue());
        data.setPlatform(platform.getTag());
        for(final LeagueList list : data) {
            list.setSummonerId(summonerId.longValue());
            list.setPlatform(platform.getTag());
        }
        return data;
    }

    @Get(SummonerPositions.class)
    public SummonerPositions getSummonerPositions(final Map<String, Object> query, final PipelineContext context) {
        final Platform platform = (Platform)query.get("platform");
        final Number summonerId = (Number)query.get("summonerId");
        Utilities.checkNotNull(platform, "platform", summonerId, "summonerId");

        final String endpoint = "lol/league/v3/positions/by-summoner/" + summonerId;
        final SummonerPositions data = get(SummonerPositions.class, endpoint, platform, "lol/league/v3/positions/by-summoner/summonerId");

        data.setSummonerId(summonerId.longValue());
        data.setPlatform(platform.getTag());
        for(final LeaguePosition position : data) {
            position.setPlatform(platform.getTag());
        }
        return data;
    }
}
