package top.slomo.concurrency.example.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cache")
public class CacheController {

    @Autowired
    private RedisClient redisClient;

    @RequestMapping("/set")
    public String set(@RequestParam("k") String key, @RequestParam("v") String value) {
        redisClient.set(key, value);
        return "success";
    }

    @RequestMapping("/get/{k}")
    public String get(@PathVariable("k") String key) {
        return redisClient.get(key);
    }
}
