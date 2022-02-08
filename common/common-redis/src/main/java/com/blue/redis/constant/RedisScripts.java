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
    TOKEN_BUCKET_RATE_LIMITER("redis.replicate_commands()\n" +
            "local tokens_key = KEYS[1]\n" +
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
            "local tokens = math.min(capacity, last_tokens+(delta*rate))\n" +
            "local allowed = tokens >= 1\n" +
            "if allowed then\n" +
            "  tokens = tokens - 1\n" +
            "end\n" +
            "\n" +
            "redis.call(\"setex\", tokens_key, ttl, tokens)\n" +
            "redis.call(\"setex\", timestamp_key, ttl, now)\n" +
            "\n" +
            "return allowed"),


    /**
     * leaky bucket limiter script
     */
    LEAKY_BUCKET_RATE_LIMITER("redis.replicate_commands()\n" +
            "local tokens_key = KEYS[1]\n" +
            "\n" +
            "local allowed_tokens = tonumber(redis.call(\"incr\", tokens_key))\n" +
            "if allowed_tokens == 1 then\n" +
            "  redis.call(\"pexpire\", tokens_key, tonumber(ARGV[2]))\n" +
            "end\n" +
            "\n" +
            "return tonumber(ARGV[1]) >= allowed_tokens"),


    /**
     * unrepeatable validator script
     */
    UNREPEATABLE_VALIDATION("redis.replicate_commands()\n" +
            "local key = KEYS[1]\n" +
            "\n" +
            "local v = redis.call(\"get\", key)\n" +
            "if v ~= nil then\n" +
            "  redis.call(\"del\", key)\n" +
            "end\n" +
            "\n" +
            "return v == ARGV[1]"),


    /**
     * repeatable validator script
     */
    REPEATABLE_UNTIL_SUCCESS_OR_TIMEOUT_VALIDATION("redis.replicate_commands()\n" +
            "local key = KEYS[1]\n" +
            "\n" +
            "local allowed = ARGV[1] == redis.call(\"get\", key)\n" +
            "if allowed then\n" +
            "  redis.call(\"del\", key)\n" +
            "end\n" +
            "\n" +
            "return allowed");

    public final String str;

    RedisScripts(String str) {
        this.str = str;
    }

}
