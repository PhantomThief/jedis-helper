jedis-helper
=======================
[![Build Status](https://travis-ci.org/PhantomThief/jedis-helper.svg)](https://travis-ci.org/PhantomThief/jedis-helper)
[![Coverage Status](https://coveralls.io/repos/github/PhantomThief/jedis-helper/badge.svg?branch=master)](https://coveralls.io/github/PhantomThief/jedis-helper?branch=master)
[![Total alerts](https://img.shields.io/lgtm/alerts/g/PhantomThief/jedis-helper.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/PhantomThief/jedis-helper/alerts/)
[![Language grade: Java](https://img.shields.io/lgtm/grade/java/g/PhantomThief/jedis-helper.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/PhantomThief/jedis-helper/context:java)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.phantomthief/jedis-helper)](https://search.maven.org/artifact/com.github.phantomthief/jedis-helper/)

关于jedis的一些工具类的封装

## 基本使用

```Java
// 声明
JedisHelper jedisHelper = JedisHelper.newBuilder(JedisPool::new).build();

// 使用
Collection<Integer> ids = Arrays.asList(1, 2, 3);

// pipeline调用
jedisHelper.pipeline(p -> {
    p.zadd("key1", 1D, "value1");
    p.zadd("key2", 1D, "value2");
});

// pipeline调用
Map<Integer, Map<String, String>> result1 = jedisHelper.pipeline(ids, (p, id) -> p.hmget("key_" + id));

// pipeline调用+转码
Map<Integer, Long> result2 = jedisHelper.pipeline(ids, (p, id) -> p.get("key2_" + id), NumberUtils::toLong);

// 单次命令调用
jedisHelper.get().get("key"); // 直接调用，不需要先从池中获得Jedis，再return回去

// Scan调用
Stream<Tuple> stream = jedisHelper.zscan("zsetKey");
```

## 高级使用

* ShardBitSet：把一个大的bitset打散成多个小key存储，减少内存开销，并在一定程度上提高性能（不过计算整个命名空间上的bitcount以及迭代遍历所有bitset性能会降低）。