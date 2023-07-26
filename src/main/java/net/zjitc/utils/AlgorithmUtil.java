package net.zjitc.utils;


import net.zjitc.model.enums.ListTypeEnum;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static net.zjitc.model.enums.ListTypeEnum.ENGLISH;
import static net.zjitc.model.enums.ListTypeEnum.MIX;

/**
 * 算法工具
 *
 * @author OchiaMalu
 * @date 2023/07/26
 */
public class AlgorithmUtil {

    /**
     * 距离
     *
     * @param list1 list1
     * @param list2 用于
     * @return double
     * @throws IOException ioexception
     */
    public static double minDistance(List<String> list1, List<String> list2) throws IOException {
        List<String> resultList1 = list1.stream().map(String::toLowerCase).collect(Collectors.toList());
        List<String> resultList2 = list2.stream().map(String::toLowerCase).collect(Collectors.toList());
        int strType = AllUtils.getStrType(resultList1);
        int type = AllUtils.getStrType(resultList2);
        ListTypeEnum enumByValue = ListTypeEnum.getEnumByValue(strType);
        ListTypeEnum enumByValue1 = ListTypeEnum.getEnumByValue(type);
        if (enumByValue == MIX) {
            resultList1 = AllUtils.tokenize(resultList1);
        }
        if (enumByValue1 == MIX) {
            resultList2 = AllUtils.tokenize(resultList2);
        }
        double ikSorce = 0;
        if (enumByValue != ENGLISH && enumByValue1 != ENGLISH) {
            List<String> resultList3 = list1.stream().map(String::toLowerCase).collect(Collectors.toList());
            List<String> resultList4 = list2.stream().map(String::toLowerCase).collect(Collectors.toList());
            List<String> quotedList1 = resultList3.stream()
                    .map(str -> "\"" + str + "\"")
                    .collect(Collectors.toList());
            List<String> quotedList2 = resultList4.stream()
                    .map(str -> "\"" + str + "\"")
                    .collect(Collectors.toList());
            String tags1 = AllUtils.collectChineseChars(quotedList1);
            List<String> Ls = AllUtils.analyzeText(tags1);
            String tags2 = AllUtils.collectChineseChars(quotedList2);
            List<String> Ls2 = AllUtils.analyzeText(tags2);
            ikSorce = AllUtils.calculateJaccardSimilarity(Ls, Ls2);
        }
        int EditDistanceScore = AllUtils.calculateEditDistance(resultList1, resultList2);
        double maxEditDistance = Math.max(resultList1.size(), resultList2.size());
        double EditDistance = 1 - EditDistanceScore / maxEditDistance;
        double JaccardScore = AllUtils.calculateJaccardSimilarity(resultList1, resultList2);
        double similarityScore = AllUtils.cosineSimilarity(resultList1, resultList2);
        
        /**
         * 编辑距离 权重为0.5
         * Jaccard相似度算法（ik分词后使用Jaccard相似度算法） 权重为0.3
         *  余弦相似度 权重为0.2
         *
         */
        double totalScore = EditDistance * 0.5 + JaccardScore * 0.3 + similarityScore * 0.2 + ikSorce * 0.3;
        return totalScore;
    }
}
