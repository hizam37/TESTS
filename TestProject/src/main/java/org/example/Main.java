package org.example;
import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {


        String OutputFile = "добавьте свое местоположение, в котором вы хотите сохранить свой файл.";

        StringBuilder stringBuilder = new StringBuilder();

        BufferedReader bufferedReader = new BufferedReader(new FileReader(args[0]));

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(OutputFile));

        List<Set<String>> numberOfGroups = new ArrayList<>();

        List<Map<String, Integer>> positionOfNumbers = new ArrayList<>();

        String line = bufferedReader.readLine();

        while (line != null) {

            String[] columns = getColumns(line);

            Integer numOfGroup = null;

            for (int i = 0; i < Math.min(positionOfNumbers.size(), columns.length); i++) {

                Integer numOfGroup2 = positionOfNumbers.get(i).get(columns[i]);

                if (numOfGroup2 != null) {

                    if (numOfGroup == null) {

                        numOfGroup = numOfGroup2;

                    } else if (!numOfGroup.equals(numOfGroup2)) {

                        for (String numbersOfGroup : numberOfGroups.get(numOfGroup2)) {

                            numberOfGroups.get(numOfGroup).add(numbersOfGroup);

                            for (int ii = 0; ii < getColumns(numbersOfGroup).length; ii++) {

                                if (getColumns(numbersOfGroup)[ii].isEmpty()) {

                                    continue;

                                }

                                if (ii < positionOfNumbers.size()) {

                                    positionOfNumbers.get(ii).put(getColumns(numbersOfGroup)[ii], numOfGroup);

                                } else {

                                    HashMap<String, Integer> map = new HashMap<>();

                                    map.put(getColumns(numbersOfGroup)[ii], numOfGroup);

                                    positionOfNumbers.add(map);

                                }

                            }

                        }

                        numberOfGroups.set(numOfGroup2, new HashSet<>());

                    }
                }
            }

            if (numOfGroup == null) {

                if (Arrays.stream(columns).anyMatch(s -> !s.isEmpty())) {

                    numberOfGroups.add(new HashSet<>(List.of(line)));

                    for (int ii = 0; ii < columns.length; ii++) {

                        if (columns[ii].isEmpty()) {

                            continue;

                        }

                        if (ii < positionOfNumbers.size()) {

                            positionOfNumbers.get(ii).put(columns[ii], numberOfGroups.size() - 1);

                        } else {

                            HashMap<String, Integer> map = new HashMap<>();

                            map.put(columns[ii], numberOfGroups.size() - 1);

                            positionOfNumbers.add(map);

                        }
                    }
                }
            } else {

                numberOfGroups.get(numOfGroup).add(line);

                for (int ii = 0; ii < columns.length; ii++) {

                    if (columns[ii].isEmpty()) {

                        continue;

                    }

                    if (ii < positionOfNumbers.size()) {

                        positionOfNumbers.get(ii).put(columns[ii], numOfGroup);

                    } else {

                        HashMap<String, Integer> map = new HashMap<>();

                        map.put(columns[ii], numOfGroup);

                        positionOfNumbers.add(map);

                    }
                }
            }

            line = bufferedReader.readLine();

        }


        stringBuilder.append("Результирующее количество групп, содержащих более одного элемента ").append(numberOfGroups.stream().filter(s -> s.size() > 1).count());

        numberOfGroups.sort(Comparator.comparingInt(s -> -s.size()));

        int iterationOfGroups = 0;

        for (Set<String> perGroup : numberOfGroups) {

            iterationOfGroups++;

            stringBuilder.append("\n").append("Группа ").append(iterationOfGroups).append("\n");

            for (String setsOfNumbers : perGroup) {

                stringBuilder.append(setsOfNumbers).append("\n");

            }
        }

        bufferedWriter.write(stringBuilder.toString());

        bufferedWriter.close();

        bufferedReader.close();

    }


    private static String[] getColumns(String line) {
        for (int i = 1; i < line.length() - 1; i++) {
            if (line.charAt(i - 1) != ';' && line.charAt(i + 1) != ';' && line.charAt(i) == '"') {
                return new String[0];
            }
        }
        return line.replaceAll("\"", "").split(";");
    }
}