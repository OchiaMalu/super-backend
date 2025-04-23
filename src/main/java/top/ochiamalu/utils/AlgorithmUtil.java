package top.ochiamalu.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 标签相似度计算工具类
 *
 * @author ochiamalu
 * @date 2025/04/23
 */
public final class AlgorithmUtil {

    // 技术分类映射（标签全部小写）
    private static final Map<String, Set<String>> TECH_CATEGORIES;

    static {
        Map<String, Set<String>> categories = new HashMap<>();
        addCategory(categories, "frontend", "html", "css", "javascript", "react", "vue");
        addCategory(categories, "backend", "java", "spring", "python", "django", "nodejs");
        addCategory(categories, "database", "mysql", "mongodb", "postgresql", "redis");
        addCategory(categories, "devops", "docker", "kubernetes", "aws", "jenkins");
        TECH_CATEGORIES = Collections.unmodifiableMap(categories);
    }

    /**
     * 私有构造函数，防止实例化
     */
    private AlgorithmUtil() {
    }

    /**
     * 向技术分类映射中添加一个类别及其对应的标签
     *
     * @param map      技术分类映射
     * @param category 类别名称
     * @param tags     标签列表
     */
    private static void addCategory(Map<String, Set<String>> map, String category, String... tags) {
        Set<String> set = Arrays.stream(tags)
                .map(String::toLowerCase)
                .collect(Collectors.toCollection(HashSet::new));
        map.put(category.toLowerCase(), Collections.unmodifiableSet(set));
    }

    /**
     * 计算两个标签列表之间的相似度
     *
     * @param tagList1 第一个标签列表
     * @param tagList2 第二个标签列表
     * @return 两个标签列表之间的相似度（0.0到1.0之间）
     */
    public static double calculateSimilarity(List<String> tagList1, List<String> tagList2) {
        if (tagList1 == null || tagList2 == null || tagList1.isEmpty() || tagList2.isEmpty()) {
            return 0.0;
        }

        List<String> lower1 = normalizeTags(tagList1);
        List<String> lower2 = normalizeTags(tagList2);

        return hybridSimilarity(lower1, lower2);
    }

    /**
     * 将标签列表中的所有标签转换为小写并过滤掉空值
     *
     * @param tags 标签列表
     * @return 处理后的标签列表
     */
    private static List<String> normalizeTags(List<String> tags) {
        return tags.stream()
                .filter(Objects::nonNull)
                .map(String::toLowerCase)
                .collect(Collectors.toList());
    }

    /**
     * 使用混合相似度算法计算两个标签列表之间的相似度
     *
     * @param list1 第一个标签列表
     * @param list2 第二个标签列表
     * @return 混合相似度
     */
    private static double hybridSimilarity(List<String> list1, List<String> list2) {
        double jaccard = enhancedJaccard(list1, list2);
        double cosine = enhancedCosine(list1, list2);
        double semEdit = semanticEditDistance(list1, list2);

        int uniqueTags = getUniqueTagCount(list1, list2);
        double categoryOverlap = getCategoryOverlap(list1, list2);

        // 动态权重计算
        double weightJ = 0.4 * sigmoid(uniqueTags - 5);
        double weightC = 0.5 * sigmoid(uniqueTags - 3);
        double weightE = 0.3 * categoryOverlap;

        // 权重归一化
        double totalWeight = weightJ + weightC + weightE;
        if (totalWeight == 0) {
            return 0.0;
        }

        return (weightJ * jaccard + weightC * cosine + weightE * semEdit) / totalWeight;
    }

    /**
     * 计算两个标签列表中唯一标签的数量
     *
     * @param list1 第一个标签列表
     * @param list2 第二个标签列表
     * @return 唯一标签的数量
     */
    private static int getUniqueTagCount(List<String> list1, List<String> list2) {
        return (int) Stream.concat(list1.stream(), list2.stream())
                .distinct()
                .count();
    }

    /**
     * 使用增强的Jaccard相似度算法计算两个标签列表之间的相似度
     *
     * @param list1 第一个标签列表
     * @param list2 第二个标签列表
     * @return Jaccard相似度
     */
    private static double enhancedJaccard(List<String> list1, List<String> list2) {
        Set<String> union = new HashSet<>(list1);
        union.addAll(list2);

        if (union.isEmpty()) {
            return 0.0;
        }

        long intersection = list1.stream()
                .distinct()
                .filter(new HashSet<>(list2)::contains)
                .count();
        return (double) intersection / union.size();
    }

    /**
     * 使用增强的余弦相似度算法计算两个标签列表之间的相似度
     *
     * @param list1 第一个标签列表
     * @param list2 第二个标签列表
     * @return 余弦相似度
     */
    private static double enhancedCosine(List<String> list1, List<String> list2) {
        // 构建词袋
        Set<String> allTerms = new HashSet<>(list1);
        allTerms.addAll(list2);

        // 计算词频
        Map<String, Long> freq1 = list1.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        Map<String, Long> freq2 = list2.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        // 计算点积和模长
        double dotProduct = 0, norm1 = 0, norm2 = 0;
        for (String term : allTerms) {
            double v1 = freq1.getOrDefault(term, 0L);
            double v2 = freq2.getOrDefault(term, 0L);
            dotProduct += v1 * v2;
            norm1 += v1 * v1;
            norm2 += v2 * v2;
        }

        return (norm1 == 0 || norm2 == 0) ? 0 : dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }

    /**
     * 使用语义编辑距离算法计算两个标签列表之间的相似度
     *
     * @param list1 第一个标签列表
     * @param list2 第二个标签列表
     * @return 语义编辑距离
     */
    private static double semanticEditDistance(List<String> list1, List<String> list2) {
        if (list1.isEmpty() || list2.isEmpty()) {
            return 0.0;
        }

        // 类别相似度
        double catSim = calculateCategorySimilarity(list1, list2);

        // 编辑距离相似度
        double editSim = calculateEditSimilarity(list1, list2);

        return 0.6 * catSim + 0.4 * editSim;
    }

    /**
     * 计算两个标签列表之间的类别相似度
     *
     * @param list1 第一个标签列表
     * @param list2 第二个标签列表
     * @return 类别相似度
     */
    private static double calculateCategorySimilarity(List<String> list1, List<String> list2) {
        long commonPairs = list1.stream()
                .flatMap(t1 -> list2.stream()
                        .filter(t2 -> areInSameCategory(t1, t2)))
                .count();
        return commonPairs / (double) (list1.size() * list2.size());
    }

    /**
     * 计算两个标签列表之间的编辑距离相似度
     *
     * @param list1 第一个标签列表
     * @param list2 第二个标签列表
     * @return 编辑距离相似度
     */
    private static double calculateEditSimilarity(List<String> list1, List<String> list2) {
        Set<String> allTags = new HashSet<>(list1);
        allTags.addAll(list2);

        List<String> sortedTags = new ArrayList<>(allTags);
        double distance = calculateHammingDistance(list1, list2, sortedTags);

        return 1 - (distance / allTags.size());
    }

    /**
     * 计算两个标签列表之间的汉明距离
     *
     * @param l1        第一个标签列表
     * @param l2        第二个标签列表
     * @param allTags   所有标签的集合
     * @return 汉明距离
     */
    private static double calculateHammingDistance(List<String> l1, List<String> l2, List<String> allTags) {
        int distance = 0;
        for (String tag : allTags) {
            boolean inL1 = l1.contains(tag);
            boolean inL2 = l2.contains(tag);
            if (inL1 != inL2) {
                distance++;
            }
        }
        return distance;
    }

    /**
     * 判断两个标签是否属于同一类别
     *
     * @param tag1 第一个标签
     * @param tag2 第二个标签
     * @return 如果两个标签属于同一类别，返回true；否则返回false
     */
    private static boolean areInSameCategory(String tag1, String tag2) {
        for (Set<String> category : TECH_CATEGORIES.values()) {
            if (category.contains(tag1) && category.contains(tag2)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 计算两个标签列表之间的类别重叠度
     *
     * @param list1 第一个标签列表
     * @param list2 第二个标签列表
     * @return 类别重叠度
     */
    private static double getCategoryOverlap(List<String> list1, List<String> list2) {
        Set<String> cats1 = getCategories(list1);
        Set<String> cats2 = getCategories(list2);

        if (cats1.isEmpty() && cats2.isEmpty()) {
            return 0.0;
        }

        Set<String> intersection = new HashSet<>(cats1);
        intersection.retainAll(cats2);

        Set<String> union = new HashSet<>(cats1);
        union.addAll(cats2);

        return (double) intersection.size() / union.size();
    }

    /**
     * 获取标签列表中所有标签所属的类别
     *
     * @param tags 标签列表
     * @return 类别集合
     */
    private static Set<String> getCategories(List<String> tags) {
        Set<String> categories = new HashSet<>();
        for (String tag : tags) {
            for (Map.Entry<String, Set<String>> entry : TECH_CATEGORIES.entrySet()) {
                if (entry.getValue().contains(tag)) {
                    categories.add(entry.getKey());
                    break;
                }
            }
        }
        return categories;
    }

    /**
     * 计算Sigmoid函数的值
     *
     * @param x 输入值
     * @return Sigmoid函数的值
     */
    private static double sigmoid(double x) {
        return 1 / (1 + Math.exp(-x));
    }
}
