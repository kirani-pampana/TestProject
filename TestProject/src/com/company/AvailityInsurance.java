package com.company;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AvailityInsurance {
    public static void main(String[] args) throws IOException {
        // provide complete path for parent directory

        String mainDir = "/Users/kirani/Documents/TestWorkspace/TestProject/src/TestData";

        List<File> csvFiles = getallfiles(mainDir);
        List<File> finalFiles = new ArrayList<>();

        for (File file: csvFiles) {
            List<Avality> avality = readFromCsv(file.getAbsolutePath());

            for (Avality b : avality) {
                File csvOutputFile = new File(mainDir + "//" + b.getInsuranceCompany()+".csv");
                PrintWriter pw = null;
                if (csvOutputFile.exists()) {
                    pw = new PrintWriter(new FileOutputStream(csvOutputFile, true));
                }
                else {
                    pw = new PrintWriter(csvOutputFile);
                    finalFiles.add(csvOutputFile);
                }
                pw.append(b.toString()+"\n");
                pw.close();
            }
        }


        // Trigger sorting method to sort as per column positions of the Last Name in asc order
        for (File file : finalFiles) {
            sortingFile(file);

        }

        // Trigger method to remove rows having dups values of UserId & Company and retain higher value of versionId
        for (File file : finalFiles) {
            duplicateRemoval(file);

        }

    }// Main ends here



    public static void sortingFile(File Fl) {

        String line = "";
        List<List<String>> readList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(Fl))) {
            while ((line = br.readLine()) != null) {
                readList.add(Arrays.asList(line.split(",")));

            }
            readList.sort(new Comparator<List<String>>() {
                @Override
                public int compare(List<String> o1, List<String> o2) {
                    return o1.get(2).compareTo(o2.get(2));
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void duplicateRemoval(File Fl) throws IOException {

        List<String[]> rowList = new ArrayList<String[]>();
        BufferedReader reader = null;
        reader = new BufferedReader(new FileReader(Fl));

        String[] currLineSplitted;
        while (reader.ready()) {
            currLineSplitted = reader.readLine().split(",");
            rowList.add(currLineSplitted);
        }

        Set<String[]> s = new TreeSet<String[]>(new Comparator<String[]>() {

            @Override
            public int compare(String[] o1, String[] o2) {
                int cmp = 0;

                // 0 position is userid, 4th position is company and 3 position is versionId
                if (o1[0].equals(o2[0]) && o1[4].equals(o2[4]) && ((o1[3]).compareTo(o2[3])) > 0) {

                    cmp = (o1[3]).compareTo(o2[3]);
                }
                return cmp;
            }
        });

        s.addAll(rowList);
    }

    public static List<File> getallfiles(String directoryName) {

        File directory = new File(directoryName);

        List<File> resultList = new ArrayList<File>();

        // get all the files from a directory
        File[] fList = directory.listFiles();
        resultList.addAll(Arrays.asList(fList));
        for (File file : fList) {
            if (file.isDirectory()) {
                resultList.addAll(getallfiles(file.getAbsolutePath()));
            }
        }
        resultList = resultList.parallelStream().filter(c -> c.getName().contains("csv")).collect(Collectors.toList());
        return resultList;
    }

    public String convertToCSV(String[] data) {
        return Stream.of(data).collect(Collectors.joining(","));
    }

    private static Avality createAvality(String[] metadata) {
        String userId = metadata[0];
        String firstName = metadata[1];
        String lastName = metadata[2];
        String version = metadata[3];
        String insuranceCompany = metadata[4];

        // create and return Avality of this metadata
        return new Avality(userId, firstName, lastName, version, insuranceCompany);
    }

    private static List<Avality> readFromCsv(String fileName) {

        List<Avality> avalityFile = new ArrayList<>();
        Path pathToFile = Paths.get(fileName);

        // create an instance of BufferedReader
        try (BufferedReader br = Files.newBufferedReader(pathToFile, StandardCharsets.US_ASCII)) {

            // read the first line from the text file
            String headerLine = br.readLine();
            String line  = br.readLine();

            // loop until all lines are read
            while (line != null) {
                String[] attributes = line.split(",");

                Avality avality = createAvality(attributes);

                // adding book into ArrayList
                avalityFile.add(avality);

                line = br.readLine();
            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return avalityFile;
    }
}

class Avality {

    private String userId;
    private String firstName;
    private String lastName;
    private String version;
    private String insuranceCompany;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getInsuranceCompany() {
        return insuranceCompany;
    }

    public void setInsuranceCompany(String insuranceCompany) {
        this.insuranceCompany = insuranceCompany;
    }

    public Avality(String userId, String firstName, String lastName, String version, String insuranceCompany) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.version = version;
        this.insuranceCompany = insuranceCompany;
    }

    @Override
    public String toString() {
        return userId + "," + firstName + "," + lastName + "," + version + "," + insuranceCompany;
    }

}
