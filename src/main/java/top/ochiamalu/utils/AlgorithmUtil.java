package top.ochiamalu.utils;

import java.util.*;

/**
 * 标签匹配算法工具类
 *
 * @author OchiaMalu
 * @date 2023/06/22
 */
public final class AlgorithmUtil {
    private AlgorithmUtil() {
    }

    /**
     * 计算两组标签的综合相似度分数
     *
     * @param tagList1 标签列表1
     * @param tagList2 标签列表2
     * @return 相似度分数 (分数越高表示越相似)
     */
    public static double calculateSimilarity(List<String> tagList1, List<String> tagList2) {
        if (tagList1 == null || tagList2 == null || tagList1.isEmpty() || tagList2.isEmpty()) {
            return 0.0;
        }

        // 计算标签集合相似度 (权重0.7)
        double setBasedSimilarity = calculateSetBasedSimilarity(tagList1, tagList2) * 0.7;

        // 计算标签位置相似度 (权重0.3)
        double positionSimilarity = calculatePositionSimilarity(tagList1, tagList2) * 0.3;

        return setBasedSimilarity + positionSimilarity;
    }

    /**
     * 计算基于集合的相似度（不考虑顺序）
     */
    private static double calculateSetBasedSimilarity(List<String> tagList1, List<String> tagList2) {
        Set<String> set1 = new HashSet<>(tagList1);
        Set<String> set2 = new HashSet<>(tagList2);
        
        // 计算交集大小
        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);
        int commonTags = intersection.size();
        
        // 计算并集大小
        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);
        int totalTags = union.size();
        
        // Jaccard相似度
        return commonTags / (double) totalTags;
    }

    /**
     * 计算基于位置的相似度（考虑相同标签的位置接近程度）
     */
    private static double calculatePositionSimilarity(List<String> tagList1, List<String> tagList2) {
        if (tagList1.isEmpty() || tagList2.isEmpty()) {
            return 0.0;
        }

        double totalSimilarity = 0.0;
        int matchCount = 0;

        // 为每个标签创建位置映射
        Map<String, List<Integer>> posMap1 = createPositionMap(tagList1);
        Map<String, List<Integer>> posMap2 = createPositionMap(tagList2);

        // 计算共同标签的位置相似度
        Set<String> commonTags = new HashSet<>(posMap1.keySet());
        commonTags.retainAll(posMap2.keySet());

        for (String tag : commonTags) {
            List<Integer> positions1 = posMap1.get(tag);
            List<Integer> positions2 = posMap2.get(tag);
            
            // 计算位置差异
            double posSimilarity = calculatePositionMatchScore(positions1, positions2,
                    tagList1.size(), tagList2.size());
            
            totalSimilarity += posSimilarity;
            matchCount++;
        }

        return matchCount == 0 ? 0.0 : totalSimilarity / matchCount;
    }

    /**
     * 创建标签位置映射
     */
    private static Map<String, List<Integer>> createPositionMap(List<String> tagList) {
        Map<String, List<Integer>> positionMap = new HashMap<>();
        for (int i = 0; i < tagList.size(); i++) {
            String tag = tagList.get(i);
            positionMap.computeIfAbsent(tag, k -> new ArrayList<>()).add(i);
        }
        return positionMap;
    }

    /**
     * 计算位置匹配分数
     */
    private static double calculatePositionMatchScore(List<Integer> positions1, List<Integer> positions2,
                                                    int length1, int length2) {
        // 标准化位置到0-1范围
        double normalizedPos1 = positions1.get(0) / (double) (length1 - 1);
        double normalizedPos2 = positions2.get(0) / (double) (length2 - 1);
        
        // 计算位置差异（值越小表示位置越接近）
        return 1.0 - Math.abs(normalizedPos1 - normalizedPos2);
    }
}
