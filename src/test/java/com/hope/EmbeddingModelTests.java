package com.com;

import com.hope.util.VectorDistanceUtils;
import org.junit.jupiter.api.Test;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class EmbeddingModelTests {

    @Autowired
    private OpenAiEmbeddingModel embeddingModel;

    /**
     * 简单向量化：把一句话转成向量并打印维度
     */
    @Test
    public void contextLoads() {
        float[] vector = embeddingModel.embed("吉首大学是大专");
        System.out.println("向量维度 = " + vector.length);
        System.out.println("前 10 维 = " + Arrays.toString(Arrays.copyOf(vector, 10)));
    }

    /**
     * 批量向量化 + 欧氏/余弦距离计算
     */
    @Test
    public void testEmbedding() {
        /* ---------- 1. 测试文本 ---------- */
//        String query = "global conflicts";
        String query = "全球冲突";
        String[] texts = new String[]{
                "哈马斯称加沙下阶段停火谈判仍在进行 以方尚未做出承诺",
                "土耳其、芬兰、瑞典与北约代表将继续就瑞典“入约”问题进行谈判",
                "日本航空基地水井中检测出有机氟化物超标",
                "国家游泳中心（水立方）：恢复游泳、嬉水乐园等水上项目运营",
                "我国首次在空间站开展舱外辐射生物学暴露实验",
        };

        /* ---------- 2. 向量化 ---------- */
        float[] queryVector = embeddingModel.embed(query);
        List<float[]> textVectors = embeddingModel.embed(Arrays.asList(texts));

        /* ---------- 3. 欧氏距离 ---------- */
        System.out.println("========== 欧氏距离 ==========");
        System.out.printf("Query vs Query: %.6f%n",
                VectorDistanceUtils.euclideanDistance(queryVector, queryVector));
        for (int i = 0; i < textVectors.size(); i++) {
            double dist = VectorDistanceUtils.euclideanDistance(queryVector, textVectors.get(i));
            System.out.printf("Query vs 文本[%d]: %.6f  -> %s%n", i, dist, texts[i]);
        }

        /* ---------- 4. 余弦距离 ---------- */
        System.out.println("========== 余弦距离 ==========");
        System.out.printf("Query vs Query: %.6f%n",
                VectorDistanceUtils.cosineDistance(queryVector, queryVector));
        for (int i = 0; i < textVectors.size(); i++) {
            double dist = VectorDistanceUtils.cosineDistance(queryVector, textVectors.get(i));
            System.out.printf("Query vs 文本[%d]: %.6f  -> %s%n", i, dist, texts[i]);
        }
    }
}