package by.kurilo.lab3.tablecalculatemethod;

import by.kurilo.lab3.utils.BooleanAlgebraUtils;

import java.util.*;
import java.util.stream.Collectors;

import static by.kurilo.lab3.calculatemethod.CalculateMinimizerMethod.calculationMethod;
import static by.kurilo.lab3.calculatemethod.CalculateMinimizerMethod.getExpressions;
import static by.kurilo.lab3.utils.BooleanAlgebraUtils.*;

public class TableMethodMinimizer {



    public static String minimizeWithTableMethod(String expression, boolean isSDNF) {
        String operator = isSDNF ? "∨" : "∧";
        String processingExpression= calculationMethod(expression,operator,true,false);
        List<String> primeImplicants = parseExpression(processingExpression,operator);
        System.out.println("\nПростые импликанты: " + formatBracketed(primeImplicants, operator));

        System.out.println("\nТаблица покрытия:");
        printCoverageTable(primeImplicants, getExpressions());

        List<String> minimalCover = findMinimalCover(primeImplicants, getExpressions(), isSDNF);
        System.out.println("\nМинимальное покрытие: " + formatBracketed(minimalCover, operator));

        return formatBracketed(minimalCover, operator);
    }

    private static void printCoverageTable(List<String> implicants, List<String> terms) {
        System.out.printf("%-20s", "Импл\\Констит");
        for (String term : terms) {
            System.out.printf("%-10s", term);
        }
        System.out.println();

        for (String imp : implicants) {
            System.out.printf("%-20s", imp);
            for (String term : terms) {
                System.out.printf("%-10s", covers(imp, term) ? "X" : "");
            }
            System.out.println();
        }
    }

    private static boolean covers(String implicant, String term) {
        List<String> impParts = BooleanAlgebraUtils.divideExpressions(implicant);
        List<String> termParts = BooleanAlgebraUtils.divideExpressions(term);
        return new HashSet<>(termParts).containsAll(impParts);
    }

    private static List<String> findMinimalCover(List<String> implicants, List<String> terms, boolean isSDNF) {
        List<String> minimalCover = new ArrayList<>();
        Set<String> covered = new HashSet<>();
        for (String term : terms) {
            List<String> covering = new ArrayList<>();
            for (String imp : implicants) {
                if (compareExpressions(imp, term, true).equals("true")) covering.add(imp);
            }
            if (covering.size() == 1 && !minimalCover.contains(covering.getFirst())) {
                String imp = covering.getFirst();
                minimalCover.add(imp);
                for (String t : terms) {
                    if (compareExpressions(imp, t, true).equals("true")) covered.add(t);
                }
            }
        }
        List<String> remaining = new ArrayList<>();
        for (String term : terms) {
            if (!covered.contains(term)) remaining.add(term);
        }
        while (!remaining.isEmpty()) {
            String bestImp = null;
            int max = 0;

            for (String imp : implicants) {
                if (!minimalCover.contains(imp)) {
                    int count = 0;
                    for (String term : remaining) {
                        if (compareExpressions(imp, term, true).equals("true")) count++;
                    }
                    if (count > max) {
                        max = count;
                        bestImp = imp;
                    }
                }
            }
            if (bestImp != null) {
                minimalCover.add(bestImp);
                String finalBestImp = bestImp;
                remaining.removeIf(term -> compareExpressions(finalBestImp, term,true).equals("true"));
            } else {
                break;
            }
        }
        return minimalCover;
    }


}