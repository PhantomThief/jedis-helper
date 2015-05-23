jedis-helper [![Build Status](https://travis-ci.org/PhantomThief/jedis-helper.svg)](https://travis-ci.org/PhantomThief/jedis-helper)
=======================

关于jedis的一些工具类的封装

## 基本使用

```xml
<dependency>
    <groupId>com.github.phantomthief</groupId>
    <artifactId>jedis-helper</artifactId>
    <version>0.1.1-SNAPSHOT</version>
</dependency>
```

```Java
// 声明
JedisHelper jedisHelper = JedisHelper.newBuilder(JedisPool::new).build();

// 使用
Collection<Integer> ids = Arrays.asList(1, 2, 3);

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
* JedisClusterHelper：对redis 3.0集群的封装，由于目前redis 3.0集群jedis客户端还没有支持pipeline，所以这个类提供了一个基于多线程的伪pipeline封装，用于不冲突的多操作的简化操作。