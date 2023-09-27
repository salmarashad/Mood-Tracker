package manager;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;

import com.opencsv.*;
import com.opencsv.exceptions.CsvValidationException;

public class MoodEntryManager {

    public static int [] moodEntries = new int [6];
    public static HashMap<LocalDate, Integer> moodMap = new HashMap<>();
    public static MoodEntryManager manager = new MoodEntryManager();



    public static void main(String[] args) throws CsvValidationException, IOException {
    }

    public static void readCSV (String file) throws IOException, CsvValidationException {
        CSVParser customParser = new CSVParserBuilder()
                .withSeparator(',')
                .withIgnoreQuotations(true)
                .build();

        // Create the CSVReader with the custom parser
        CSVReader reader = new CSVReaderBuilder(new FileReader(file)).withCSVParser(customParser).build();

        String[] nextLine;
        reader.readNext();
        while ((nextLine = reader.readNext()) != null) {
            LocalDate date = LocalDate.parse(nextLine[0]);
            int value = Integer.parseInt(nextLine[1]);
            manager.addMoodEntry(value, date);
        }
        System.out.println(moodMap);
    }


    public static void writeCSV (String file, int value ) throws IOException {
            CSVWriter writer = new CSVWriter(new FileWriter(file, true));
            LocalDate date = LocalDate.now();
            String[] data = {date.toString() + "," + value};
            manager.addMoodEntry(value, date);
            writer.writeNext(data);
            writer.close();
    }


    private void addMoodEntry (int entry, LocalDate date) {
        if(moodMap.containsKey(date)){
            int curr = moodMap.get(date);
            moodEntries[curr]--;
            moodMap.remove(date);
        }
        switch (entry) {
            case 0-> {
                moodEntries[0]++ ;
                moodMap.put(date, entry);
                break;
            }
            case 1 -> {
                    moodEntries[1]++;
                moodMap.put(date, entry);
                break;
            }
            case 2 -> {
                moodEntries[2]++ ;
                moodMap.put(date, entry);
                break;
            }
            case 3 -> {
                moodEntries[3]++ ;
                moodMap.put(date, entry);
                break;
            }
            case 4 -> {
                moodEntries[4]++ ;
                moodMap.put(date, entry);
                break;
            }
            case 5 -> {
                moodEntries[5]++ ;
                moodMap.put(date, entry);
                break;
            }
            default -> System.out.println("incorrect entry");
        }
    }




}
