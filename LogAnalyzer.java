/**
 * Read web server data and analyse hourly access patterns.
 * 
 * @author David J. Barnes and Michael Kölling.
 * @version    2016.02.29
 * 
 * @author Andrew Riganati
 * @version 2013.03.31
 */
public class LogAnalyzer
{
    // Where to calculate the hourly access counts.
    private int[] hourCounts;
    //where to calculate the daily access counts.
    private int[] dayCounts;
    // Where to calculate the monthly access counts.
    private int[] monthCounts;
    // where to calculate the monthly average count.
    private double[] monthlyAverage;
    
    
    // Use a LogfileReader to access the data.
    private LogfileReader reader;

    private String filename;
    /**
     * Create an object to analyze hourly web accesses.
     */
    public LogAnalyzer(String filename)
    { 
        // Create the array object to hold the hourly
        // access counts.
        hourCounts = new int[24];
        dayCounts = new int[32];
        monthCounts = new int[13];
        monthlyAverage = new double[13];
        this.filename = filename;
        // Create the reader to obtain the data.
        reader = new LogfileReader();
    }
    
    /**
     *  Return the bumber of accesses recorded in the log file
     *  @return  number of accesses
     */
    public int numberOfAccesses()
    {
        int total = 0;
     
        for(int hour = 0; hour < hourCounts.length; hour++) 
        {
            total += hourCounts[hour];
        }
        return total;
    }
    
    /**
     * finds busiest hour of the year
     * @return  busiest hour of the year
     */
    public int busiestHour()
    {
        int maxHour = 0;
        
        for(int hour = 1; hour < hourCounts.length; hour++)
        {
            if(hourCounts[hour] > hourCounts[maxHour])
            {
                maxHour = hour;
            }
        }
        return maxHour;
    }
    
    /**
     * finds the quietest hour
     * @return the quietest hour
     */
    public int quietestHour()
    {
        int quietestHour = 0;
        
        for(int i = 0; i < hourCounts.length; i++)
        {
            if(hourCounts[i] > 0 && hourCounts[i] > quietestHour)
            {
                quietestHour = hourCounts[i];
            }
        }
        
        return quietestHour;
    }
    
    public int busiestTwoHours()
    {
        int greater = hourCounts[0];
        int fHour = 0;
        analyzeHourlyData();
        for(int i = 0; i < hourCounts.length - 1; i++)
        {
            if((hourCounts[i] + hourCounts[i+1]) > greater)
            {
                greater =  hourCounts[i] + hourCounts[i+1];
                fHour = i;
            }
        }
        return fHour;
    }
    
    /**
     * Analyze the hourly access data from the log file.
     */
    public void analyzeHourlyData()
    {
        while(reader.hasNext()) {
            LogEntry entry = reader.next();
            int hour = entry.getHour();
            hourCounts[hour]++;
        }
    }
    
     /**
     * Access daily access data from the log file
     */
     public void analyzeDailyData()
    {
        reader = new LogfileReader(filename);
        while(reader.hasNext())
        {
            LogEntry entry = reader.next();
            int day = entry.getDay();
            dayCounts[day]++;
        }
    }
    
    /**
     * Analyze the monthly access data from the log file.
     */
     public void analyzeMonthlyData()
    {
        reader = new LogfileReader(filename);
        while(reader.hasNext()) {
            LogEntry entry = reader.next();
            int month = entry.getMonth();
            monthCounts[month]++;
        }
    }
    
    public void averageAccessesPerMonth()
    {
        reader = new LogfileReader(filename);
        int year = 0;
        int yearCounter = 0;
        
        while(reader.hasNext()) {
            LogEntry entry = reader.next();
            
            if(year != entry.getYear()) {
                yearCounter++;
                year = entry.getYear();
            }
            int month = entry.getMonth();
            monthCounts[month]++;
        }
        
        for(int i=1; i < monthlyAverage.length; i++) {
            monthlyAverage[i] = (monthCounts[i] * 1.0) / yearCounter;
        }
    }
    
    /**
     * return the quietest day in the log file
     */
    public int quietestDay()
    {
        int lower = dayCounts[1];
        int day = 0;
        analyzeDailyData();
        for( int i = 1; i < dayCounts.length; i++) 
        {
            if(dayCounts[i] < lower) 
            {
                lower = dayCounts[i];
                day = i;
            }
        }
        return day;
    }
    
    /**
     * return the busiest day the log file.
     */
    public int busiestDay()
    {
        int greater = dayCounts[1];
        int day = 0;
        analyzeDailyData();
        for(int i = 1; i < dayCounts.length; i++)
        {
            greater = dayCounts[i];
            day = i;
        }
        return day;
    }
    
     public int quietestMonth() 
    {
        int lower = monthCounts[1];
        int month = 0;
        analyzeMonthlyData();
        for(int i=1; i < monthCounts.length; i++) {
            if(monthCounts[i] < lower) {
                lower = monthCounts[i];
                month = i;
            }
        }
        return month;
    }
    
     public int busiestMonth() 
     {
        int greater = monthCounts[1];
        int month = 0;
        analyzeMonthlyData();
        for(int i=1; i < monthCounts.length; i++) {
            if(monthCounts[i] > greater) {
                greater = monthCounts[i];
                month = i;
            }
        }
        return month;
    }
    
    /**
     * Print the hourly counts.
     * These should have been set with a prior
     * call to analyzeHourlyData.
     */
    public void printHourlyCounts()
    {
        System.out.println("Hr: Count");
        for(int hour = 0; hour < hourCounts.length; hour++) {
            System.out.println(hour + " " + hourCounts[hour]);
        }
    }
    
    /**
     * Print the daily counts.
     * these should have been set with a prior 
     * call to analyzeDailyData
     */
    public void printDailyCounts()
    {
        System.out.println("Day : Count");
        for(int day = 1; day < dayCounts.length; day++) 
        {
            System.out.println(day + " " + dayCounts[day]);
         
        }
    }
    
    /**
     * print the monthly counts. 
     * these should have been set with a prior
     * call to analyzeMonthlydata
     */
    public void printMonthlyCounts()
    {
        System.out.println("Month : Count");
        for(int month=1; month < monthCounts.length; month++) {
            System.out.println(month + " " + monthCounts[month]);
        }
    }
    
        public void printMonthlyAverage()
    {
        System.out.println("Month : Avg. Count");
        for(int month=1; month < monthlyAverage.length; month++) {
            System.out.println(month + ": " + monthlyAverage[month]);
        }
    }

    /**
     * Print the lines of data read by the LogfileReader
     */
    public void printData()
    {
        reader.printData();
    }
}
