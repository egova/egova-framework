package com.egova.minio;

import io.minio.MinioClient;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 随机选择算法
 *
 * @author 奔波儿灞
 * @since 1.0
 */
public class RandomMinioClientRule implements Rule<MinioClient> {

    @Override
    public MinioClient choose(List<MinioClient> clients) {
        if (clients.size() == 1) {
            return clients.get(0);
        }
        int index = ThreadLocalRandom.current().nextInt(clients.size());
        return clients.get(index);
    }

}
