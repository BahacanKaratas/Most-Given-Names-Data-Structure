

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[]args){
        /*xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx*/
        //READ THE ARGUMENTS GIVEN WITH COMMAND LINE
        //find the names that will be searching
        //remaining all the arguments are files
        ArrayList<String> femaleNames=new ArrayList<>();
        ArrayList<String> maleNames=new ArrayList<>();
        ArrayList<String> fileNames=new ArrayList<>();
        System.out.println();
        for(int i=0;i<args.length;i++){
            if(args[i].equals("-f")){
                if(args[i+1].equals("-f") || args[i+1].equals("-m")){
                    System.out.println("Illegal arguments are given (flag after a flag)");
                    System.exit(0);
                }
                femaleNames.add(args[i+1]);
                i=i+1;
            }
            else{
                if(args[i].equals("-m")){
                    if(args[i+1].equals("-f") || args[i+1].equals("-m")){
                        System.out.println("Illegal arguments are given (flag after a flag)");
                        System.exit(0);
                    }
                    maleNames.add(args[i+1]);
                    i=i+1;
                }
                else{
                    fileNames.add(args[i]);
                }
            }
        }
        for(int i=0;i<fileNames.size();i++){
            String searchThis=fileNames.get(i);
            if(searchThis.contains("*")){
                for(int x=0;x<10;x++){
                    String addThisToFileNames=fileNames.get(i).replace("*",x+"");
                    fileNames.add(addThisToFileNames);
                }
            }
        }
        for(int i=0;i<fileNames.size();i++){
            String searchThis=fileNames.get(i);
            if(searchThis.contains("*")){
               fileNames.remove(i);
            }
        }
        fileNames.sort(String::compareTo);
        System.out.println("My program will be searching these files");
        for(int i=0;i<fileNames.size();i++){
            String searchThis=fileNames.get(i);
            System.out.print(searchThis+" ");
        }
        System.out.println("");
        System.out.println("");

        /*xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx*/
        //2 MAIN LINKED LIST HOLDING NAME ARRAY SINCE THERE CAN BE MORE THAN 1 PERSON WITH THAT NAME
        DoublyLinkedList<ArrayList<Name>> ladiesMainLinkedList=new DoublyLinkedList<>();
        DoublyLinkedList<ArrayList<Name>> gentlemenMainLinkedList=new DoublyLinkedList<>();

        //Other structures and varibles
        double ALLTOTALMALE=0;
        double ALLTOTALFEMALE=0;

        /*xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx*/

        /*xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx*/
        //for one file;
        int countLoop=0;
        while(countLoop<fileNames.size()){
            ArrayList<Integer> arrayListForMaleYearAndTotalNumber=new ArrayList<>(2);;
            arrayListForMaleYearAndTotalNumber.add(0,0);
            arrayListForMaleYearAndTotalNumber.add(1,0);
            ArrayList<Integer> arrayListForFemaleYearAndTotalNumber=new ArrayList<>(2);
            arrayListForFemaleYearAndTotalNumber.add(0,0);
            arrayListForFemaleYearAndTotalNumber.add(1,0);

            int year=0; //calculate this with integer parse
            year=Integer.parseInt(fileNames.get(countLoop).substring(5,9));
            String fileName=fileNames.get(countLoop);
            countLoop++;
            Scanner inputStream=null;
            try {
                inputStream=new  Scanner(new File(fileName));
            } catch (FileNotFoundException e) {
                System.out.println("CANNOT FIND THE FILE/FILE CANNOT BE OPENED--Filename:"+fileNames.get(countLoop-1));
                continue;
            }
            while(inputStream.hasNextLine()){ //reading 1 file and adding to linkedlists

                String[] theCurrentLineInTheCurrentFile=inputStream.nextLine().split(",");

                Name male=new Name(theCurrentLineInTheCurrentFile[0],theCurrentLineInTheCurrentFile[1],Integer.parseInt(theCurrentLineInTheCurrentFile[2]),year);
                Name female=new Name(theCurrentLineInTheCurrentFile[0],theCurrentLineInTheCurrentFile[3],Integer.parseInt(theCurrentLineInTheCurrentFile[4]),year);
                //add year and add up al the numbers to the additional arraylist
                arrayListForFemaleYearAndTotalNumber.set(0,year);
                arrayListForFemaleYearAndTotalNumber.set(1,arrayListForFemaleYearAndTotalNumber.get(1)+Integer.parseInt(theCurrentLineInTheCurrentFile[4]));
                arrayListForMaleYearAndTotalNumber.set(0,year);
                arrayListForMaleYearAndTotalNumber.set(1,arrayListForMaleYearAndTotalNumber.get(1)+Integer.parseInt(theCurrentLineInTheCurrentFile[2]));

                //add this names to the MAIN LINKED LIST WITHOUT SORTING since sorting will be done later

                //for men//////////////////////////////////////////////////////////////////////////////////////////
                boolean notFoundMale=true;
                DoublyLinkedList.Node<ArrayList<Name>>  walkMen=gentlemenMainLinkedList.header;
                while (walkMen.getNext()!=gentlemenMainLinkedList.trailer){
                    walkMen=walkMen.getNext();
                    if(walkMen.getElement().get(0).name.equals(theCurrentLineInTheCurrentFile[1])){//look if male name exists
                        walkMen.getElement().add(male);
                        notFoundMale=false;
                    }
                }
                //walkmen is right before the spot of trailer
                if(notFoundMale){//add the new male name
                    ArrayList<Name> addThis=new ArrayList<>();
                    addThis.add(male);
                    gentlemenMainLinkedList.addLast(addThis);
                }
                //////////////////////////////////////////////////////////////////////////////////////////

                //for women//////////////////////////////////////////////////////////////////////////////////////////
                boolean notFoundFemale=true;
                DoublyLinkedList.Node<ArrayList<Name>>  walkFemale=ladiesMainLinkedList.header;
                while (walkFemale.getNext()!=ladiesMainLinkedList.trailer){
                    walkFemale=walkFemale.getNext();
                    if(walkFemale.getElement().get(0).name.equals(theCurrentLineInTheCurrentFile[3])){//look if female name exists
                        walkFemale.getElement().add(female);
                        notFoundFemale=false;
                    }
                }
                //walkfemale is right before the spot of trailer
                if(notFoundFemale){//add the new female name
                    ArrayList<Name> addThis=new ArrayList<>();
                    addThis.add(female);
                    ladiesMainLinkedList.addLast(addThis);
                }
                //////////////////////////////////////////////////////////////////////////////////////////
            }// 2 Main Linked lists have been updated with current years names and their data
            //2 auxilary arraylists is updated and now after the sorting of main linked lists, will be used to calculate
            //yearly percentages

            //sort 2 linked lists

            //sort Men

            DoublyLinkedList.Node<ArrayList<Name>> currentMen=null,indexMen=null;
            ArrayList<Name> tempMen;
            if(gentlemenMainLinkedList.header==null){
                //do nothing
            }
            else{
                for(currentMen=gentlemenMainLinkedList.header.getNext();currentMen.next!=gentlemenMainLinkedList.trailer;currentMen=currentMen.next){
                    for(indexMen=currentMen.next;indexMen!=gentlemenMainLinkedList.trailer;indexMen=indexMen.next){
                        if(currentMen.getElement().get(0).name.compareTo(indexMen.getElement().get(0).name)>0){
                            tempMen=currentMen.getElement();
                            currentMen.element=indexMen.getElement();
                            indexMen.element=tempMen;
                        }
                    }
                }
            }
            ///////////////////////////////////////////////////////////////////////////////////////////////////
            //sort women
            DoublyLinkedList.Node<ArrayList<Name>> currentwomen=null,indexwomen=null;
            ArrayList<Name> tempwomen;
            if(ladiesMainLinkedList.header==null){
                //do nothing
            }
            else{
                for(currentwomen=ladiesMainLinkedList.header.getNext();currentwomen.next!=ladiesMainLinkedList.trailer;currentwomen=currentwomen.next){
                    for(indexwomen=currentwomen.next;indexwomen!=ladiesMainLinkedList.trailer;indexwomen=indexwomen.next){
                        if(currentwomen.getElement().get(0).name.compareTo(indexwomen.getElement().get(0).name)>0){
                            tempwomen=currentwomen.getElement();
                            currentwomen.element=indexwomen.getElement();
                            indexwomen.element=tempwomen;
                        }
                    }
                }
            }
            ///////////////////////////////////////////////////////////////////////////////////////////////////
            //now that we have 2 sorted link lists, I will walk over them and update their Yearly percentage
            //update men
            DoublyLinkedList.Node<ArrayList<Name>> walk;
            walk=gentlemenMainLinkedList.header.getNext();

            while(walk.getNext()!=gentlemenMainLinkedList.trailer){
                for(int i=0;i<walk.getElement().size();i++){
                    if(walk.getElement().get(i).YEAR==arrayListForMaleYearAndTotalNumber.get(0)){
                        walk.getElement().get(i).yearlyPercentage=walk.getElement().get(i).number/arrayListForMaleYearAndTotalNumber.get(1);
                    }
                }
                walk=walk.getNext();
            }
            //update women
            DoublyLinkedList.Node<ArrayList<Name>> walkW;
            walkW=ladiesMainLinkedList.header.getNext();
            while(walkW.getNext()!=ladiesMainLinkedList.trailer){
                for(int i=0;i<walkW.getElement().size();i++){
                    if(walkW.getElement().get(i).YEAR==arrayListForFemaleYearAndTotalNumber.get(0)){
                        walkW.getElement().get(i).yearlyPercentage=walkW.getElement().get(i).number/arrayListForFemaleYearAndTotalNumber.get(1);
                    }
                }
                walkW=walkW.getNext();
            }
            ALLTOTALFEMALE+=arrayListForFemaleYearAndTotalNumber.get(1);
            ALLTOTALMALE+=arrayListForMaleYearAndTotalNumber.get(1);
        }


        //End of operations for 1 file
        /*xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx*/

        /*xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx*/
        /*ALLFILE OPERATIONS: all we have to do is update TOTALNUMBER and TOTALPERCENTAGE and LINKED LIST RANK in 2 main LinkedLists*/
        //update women
        DoublyLinkedList.Node<ArrayList<Name>> walkWALL=ladiesMainLinkedList.header.getNext();
        int count=0;
        while(walkWALL.getNext()!=ladiesMainLinkedList.trailer){
            int totalForThisParticularName=0;
            for(int i=0;i<walkWALL.getElement().size();i++){
                totalForThisParticularName+=walkWALL.getElement().get(i).number;
            }
            for(int i=0;i<walkWALL.getElement().size();i++){
                walkWALL.getElement().get(i).TOTALNUMBER=totalForThisParticularName;
                walkWALL.getElement().get(i).TOTALPERCENTAGE= walkWALL.getElement().get(i).TOTALNUMBER/ALLTOTALFEMALE;

                walkWALL.getElement().get(i).LinkedListRank=count;
            }
            walkWALL=walkWALL.getNext();
            count++;
        }
        DoublyLinkedList.Node<ArrayList<Name>> walkALL;
        walkALL=gentlemenMainLinkedList.header.getNext();
        count=0;
        while(walkALL.getNext()!=gentlemenMainLinkedList.trailer){
            int totalForThisParticularName=0;
            for(int i=0;i<walkALL.getElement().size();i++){
                totalForThisParticularName+=walkALL.getElement().get(i).number;
            }
            for(int i=0;i<walkALL.getElement().size();i++){
                walkALL.getElement().get(i).TOTALNUMBER=totalForThisParticularName;
                walkALL.getElement().get(i).TOTALPERCENTAGE= walkALL.getElement().get(i).TOTALNUMBER/ALLTOTALMALE;
                walkALL.getElement().get(i).LinkedListRank=count;
            }
            walkALL=walkALL.getNext();
            count++;
        }
        /*xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx*/
        /*HALF POINT: We are done with reading and storing elements. Now we have to print info we need to print.*/
        //search and print the males
        System.out.println("");
        for(int i=0;i<maleNames.size();i++){
            DoublyLinkedList.Node<ArrayList<Name>> finalWalkMale=gentlemenMainLinkedList.header.getNext();
            DoublyLinkedList.Node<ArrayList<Name>> temp = null;
            ArrayList<Double> allTheTotalNumbers=new ArrayList<>();
            double compareThis=0;
            int countHowManyLarge=0;
            int initalRank=1;
            int LinkedListRank=1;
            int print=1;
            while(finalWalkMale.getNext()!=gentlemenMainLinkedList.trailer.next){
                allTheTotalNumbers.add(finalWalkMale.getElement().get(0).TOTALNUMBER);
                System.out.println(finalWalkMale.getElement().get(0).name);
                System.out.println(print);
                print++;
                if(finalWalkMale.getElement().get(0).name.equals(maleNames.get(i))){
                    System.out.println("Linked List Rank:"+LinkedListRank);
                    temp=finalWalkMale;
                    for(int x=0;x<finalWalkMale.getElement().size();x++){

                        System.out.println(finalWalkMale.getElement().get(x).YEAR);
                        System.out.println(maleNames.get(i)+": "+finalWalkMale.getElement().get(x).yearlyRank+", "+(int)finalWalkMale.getElement().get(x).number+", "+finalWalkMale.getElement().get(x).yearlyPercentage);
                        System.out.println();
                        compareThis=finalWalkMale.getElement().get(x).TOTALNUMBER;
                    }
                }
                finalWalkMale=finalWalkMale.getNext();
                LinkedListRank++;
            }
            for(int search=0;search<allTheTotalNumbers.size();search++){
                if(allTheTotalNumbers.get(search)>compareThis){
                    initalRank++;
                }
            }
            if(temp==null){
                System.out.println("-----------------------------------------");
                System.out.println("Name cannot be found: "+maleNames.get(i));
                System.out.println("-----------------------------------------");
                continue;
            }
            System.out.println("Total");
            System.out.println(temp.getElement().get(0).name+":, "+initalRank+", "+(int)temp.getElement().get(0).TOTALNUMBER+", "+temp.getElement().get(0).TOTALPERCENTAGE);
            System.out.println();
        }
        System.out.println("");
        for(int i=0;i<femaleNames.size();i++){
            DoublyLinkedList.Node<ArrayList<Name>> finalWalkfemale=ladiesMainLinkedList.header.getNext();
            ArrayList<Double> allTheTotalNumbers2=new ArrayList<>();
            DoublyLinkedList.Node<ArrayList<Name>> temp = null;
            double compareThis=0;
            int countHowManyLarge=0;
            int initalRank=1;
            int LinkedListRank=1;
            while(finalWalkfemale.getNext()!=ladiesMainLinkedList.trailer.next){
                allTheTotalNumbers2.add(finalWalkfemale.getElement().get(0).TOTALNUMBER);
                if(finalWalkfemale.getElement().get(0).name.equals(femaleNames.get(i))){
                    System.out.println("Linked List Rank:"+LinkedListRank);
                    temp=finalWalkfemale;
                    for(int x=0;x<finalWalkfemale.getElement().size();x++){

                        System.out.println(finalWalkfemale.getElement().get(x).YEAR);
                        System.out.println(femaleNames.get(i)+": "+finalWalkfemale.getElement().get(x).yearlyRank+", "+(int)finalWalkfemale.getElement().get(x).number+", "+finalWalkfemale.getElement().get(x).yearlyPercentage);
                        System.out.println();
                        compareThis=finalWalkfemale.getElement().get(x).TOTALNUMBER;
                    }
                }
                finalWalkfemale=finalWalkfemale.getNext();
                LinkedListRank++;
            }
            for(int search=0;search<allTheTotalNumbers2.size();search++){
                if(allTheTotalNumbers2.get(search)>compareThis){
                    initalRank++;
                }
            }
            if(temp==null){
                System.out.println("-----------------------------------------");
                System.out.println("Name cannot be found: "+femaleNames.get(i));
                System.out.println("-----------------------------------------");
                continue;
            }
            System.out.println("Total");
            System.out.println(temp.getElement().get(0).name+":, "+initalRank+", "+(int)temp.getElement().get(0).TOTALNUMBER+", "+temp.getElement().get(0).TOTALPERCENTAGE);
            System.out.println();
        }
        /*xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx*/
    }
}
