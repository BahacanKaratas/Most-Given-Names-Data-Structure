public class Name{


    //updated in a loop for 1 file
    int YEAR;
    String name;
    public String yearlyRank;
    double number;
    double yearlyPercentage; //will be calculated later


    //updated after reading all files
    double TOTALPERCENTAGE;
    double TOTALNUMBER;
    int LinkedListRank;

    int TOTALRANK;//will only be printed when requested a nameby searching the main linked list one more time

    public Name(String  assignYearlyRank,String assignName,int assignHowManyBabies,int YEAR){
        name=assignName;
        yearlyRank=assignYearlyRank;
        number=assignHowManyBabies;
        this.YEAR=YEAR;
    }
}
