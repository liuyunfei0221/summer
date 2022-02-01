package com.blue.redis.constant;

/**
 * redis script str
 *
 * @author liuyunfei
 * @date 2021/8/25
 * @apiNote
 */
@SuppressWarnings("SpellCheckingInspection")
public enum RedisScripts {

    /**
     * token bucket limiter script
     */
    TOKEN_BUCKET_RATE_LIMITER("local tokens_key = KEYS[1]\n" +
            "local timestamp_key = KEYS[2]\n" +
            "\n" +
            "local rate = tonumber(ARGV[1])\n" +
            "local capacity = tonumber(ARGV[2])\n" +
            "local now = tonumber(ARGV[3])\n" +
            "\n" +
            "local fill_time = capacity/rate\n" +
            "local ttl = math.floor(fill_time*2)\n" +
            "\n" +
            "local last_tokens = tonumber(redis.call(\"get\", tokens_key))\n" +
            "if last_tokens == nil then\n" +
            "  last_tokens = capacity\n" +
            "end\n" +
            "\n" +
            "local last_refreshed = tonumber(redis.call(\"get\", timestamp_key))\n" +
            "if last_refreshed == nil then\n" +
            "  last_refreshed = 0\n" +
            "end\n" +
            "\n" +
            "local delta = math.max(0, now-last_refreshed)\n" +
            "local filled_tokens = math.min(capacity, last_tokens+(delta*rate))\n" +
            "local allowed = filled_tokens >= 1\n" +
            "local new_tokens = filled_tokens\n" +
            "local allowed_num = 0\n" +
            "if allowed then\n" +
            "  new_tokens = filled_tokens - 1\n" +
            "  allowed_num = 1\n" +
            "end\n" +
            "\n" +
            "if ttl > 0 then\n" +
            "  redis.call(\"setex\", tokens_key, ttl, new_tokens)\n" +
            "  redis.call(\"setex\", timestamp_key, ttl, now)\n" +
            "end\n" +
            "\n" +
            "return allowed_num", "token bucket limiter script"),


    /**
     * limiter script
     */
    RATE_LIMITER("local tokens_key = KEYS[1]\n" +
            "local timestamp_key = KEYS[2]\n" +
            "\n" +
            "local rate = tonumber(ARGV[1])\n" +
            "local capacity = tonumber(ARGV[2])\n" +
            "local now = tonumber(ARGV[3])\n" +
            "\n" +
            "local fill_time = capacity/rate\n" +
            "local ttl = math.floor(fill_time*2)\n" +
            "\n" +
            "local last_tokens = tonumber(redis.call(\"get\", tokens_key))\n" +
            "if last_tokens == nil then\n" +
            "  last_tokens = capacity\n" +
            "end\n" +
            "\n" +
            "local last_refreshed = tonumber(redis.call(\"get\", timestamp_key))\n" +
            "if last_refreshed == nil then\n" +
            "  last_refreshed = 0\n" +
            "end\n" +
            "\n" +
            "local delta = math.max(0, now-last_refreshed)\n" +
            "local filled_tokens = math.min(capacity, last_tokens+(delta*rate))\n" +
            "local allowed = filled_tokens >= 1\n" +
            "local new_tokens = filled_tokens\n" +
            "local allowed_num = 0\n" +
            "if allowed then\n" +
            "  new_tokens = filled_tokens - 1\n" +
            "  allowed_num = 1\n" +
            "end\n" +
            "\n" +
            "if ttl > 0 then\n" +
            "  redis.call(\"setex\", tokens_key, ttl, new_tokens)\n" +
            "  redis.call(\"setex\", timestamp_key, ttl, now)\n" +
            "end\n" +
            "\n" +
            "return allowed_num", "limiter script"),


    /**
     * validator script
     */
    VALIDATION("local key = KEYS[1]\n" +
            "\n" +
            "local v = redis.call(\"get\", key)\n" +
            "if v ~= nil then\n" +
            "  redis.call(\"del\", key)\n" +
            "end\n" +
            "\n" +
            "return v == ARGV[1]", "validator script");

    public final String str;

    public final String disc;

    RedisScripts(String str, String disc) {
        this.str = str;
        this.disc = disc;
    }
}
