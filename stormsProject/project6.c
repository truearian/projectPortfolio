#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <ctype.h>

#define LEN_NAME 20
#define NUM_ELEMENTS 1000
#define ELEMENTS 493
#define LEN_LINE 50

//creates a typedef struct for the storms with the given columns
typedef struct
{
    char stormName[LEN_NAME + 1];
    int date;
    int time;
    double latitude;
    double longitude;
    int windSpeed;
    int pressure;
}storm_t;

void displayData(storm_t *aPtr);
int loadData (storm_t storms[], int capacity);
int optionSelect(void);
void displayLog(storm_t storms[], int capacity);
void displayDate(storm_t storms[], int capacity);
void exactName(storm_t storms[], int capacity);
void partialName(storm_t storms[], int capacity);
void upperCase(char *string);
void minWindspeed(storm_t storms[], int capacity);
void maxPressure(storm_t *aPtr, int capacity);
void credit(void);
void welcome(void);

/**
  *main
  *the main function is called at the start of the program
  *
  *@param void none
  *@return int 0, validating that the program ran correctly
  */
int main(void)
{
    welcome();
    //sets values for the data to 0
    storm_t storms[NUM_ELEMENTS] = {0};
    int index, count, option, numStart, numDisplay;

    //the int count = the value of index returned by loadData
    count = loadData(storms, NUM_ELEMENTS);

    //loop until the user chooses 10
    while (option != 10)
    {
        option = optionSelect();

        switch(option)
        {
        case 1:
            displayLog(storms, count);
            break;
        case 2:
            displayDate(storms, count);
            break;
        case 3:
            exactName(storms, count);
            break;
        case 4:
            partialName(storms, count);
            break;
        case 5:
            minWindspeed(storms, count);
            break;
        case 6:
            maxPressure(storms, count);
            break;
        case 10:
            break;
        }
        //skip a line
        printf("\n");
    }
    credit();

    return 0;
}

/**
  *loadData
  *this function loads the data from the file into a structure array
  *
  *@param the storm type def struct, and the capacity
  *@return index of the last record found (count)
  */
int loadData (storm_t storms[], int capacity)
{
    FILE *ptr;
    char tempLine[LEN_LINE + 1];
    char *startPtr, *endPtr;
    int index = 0;

    //open file
    ptr = fopen("StormsAL2019.txt", "r");
    if (ptr == NULL)
    {
        printf("Error in opening file\n");
    }
    else
    {
        //reads the file and loads the info in the correct column
        while(index < capacity && fgets(tempLine, sizeof(tempLine), ptr))
        {
            endPtr = strchr(tempLine, '\n');
            if (endPtr)
                *endPtr = '\0';

            startPtr = tempLine;
            endPtr = strchr(startPtr, '|');
            strncpy(storms[index].stormName, startPtr, endPtr - startPtr);

            startPtr = endPtr + 1;
            endPtr = strchr(startPtr, '|');
            storms[index].date = atoi(startPtr);

            startPtr = endPtr + 1;
            endPtr = strchr(startPtr, '|');
            storms[index].time = atoi(startPtr);

            startPtr = endPtr + 1;
            endPtr = strchr(startPtr, '|');
            storms[index].latitude = atof(startPtr);

            startPtr = endPtr + 1;
            endPtr = strchr(startPtr, '|');
            storms[index].longitude = atof(startPtr);

            startPtr = endPtr + 1;
            endPtr = strchr(startPtr, '|');
            storms[index].windSpeed = atoi(startPtr);

            startPtr = endPtr + 1;
            endPtr = strchr(startPtr, '\0');
            storms[index].pressure = atoi(startPtr);

            index++;
        }
        //closes file
        fclose(ptr);
    }
    return index;
}

/**
  *displayData
  *shows the data for a single storm
  *
  *@param pointer address of the storm struct
  *@return nothing
  */
void displayData(storm_t *aPtr)
{
    printf("%10s | DATE: %8i | TIME: %04i | LAT: %5.2lf | LONG: %5.2lf | WINDSPEED: %3i | PRESSURE: %4i\n",
           aPtr->stormName, aPtr->date, aPtr->time, aPtr->latitude, aPtr->longitude, aPtr->windSpeed, aPtr->pressure);
}

/**
  *optionSelect
  *shows the options the user can select, then asks for input
  *
  *@param void none
  *@return int option, the option the user chose
  */
int optionSelect(void)
{
    int option = 0;
    printf("1) Display the data for logs\n2) Search for data by date\n3) Search for data by storm name (exact match)\n4) Search for data by storm name (partial match)\n5) Search for logs with a user-entered minimum windspeed\n6) Search for logs with a user-entered maximum pressure\n10) Exit\n");
    do
    {
        printf("Choose an option (1-6 or 10) from above: ");
        scanf("%i", &option);
    } while(option != 1 && option != 2 && option != 3 && option != 4 && option != 5 && option != 6 && option != 10);//validates

    return option;
}

/**
  *displayLog
  *user picks the record # to start from, then inputs how many to display
  *
  *@param storms struct array, int capacity
  *@return void none
  */
void displayLog(storm_t storms[], int capacity)
{
    int count = 0, numDisplay, numStart, index;
    printf("Enter the log #(1 - 493) to start from: ");
    scanf("%i", &numStart);

    while (numStart < 1 || numStart > capacity)
    {
        printf("INVALID! Please input a number from 1 - 493: ");
        scanf("%i", &numStart);
    }

    printf("Enter how many logs to display (50 MAX): ");
    scanf("%i", &numDisplay);

    while (numDisplay < 1 || numDisplay > 50)
    {
        printf("INVALID! Please input a number from 1 - 50: ");
        scanf("%i", &numDisplay);
    }
    //shows data starting at the # the user inputed
    for (index = numStart - 1; index < capacity; index++)
    {
        //shows the amount of data the user inputed
        if(count <= numDisplay - 1)
        {
            displayData(&storms[index]);
            count++;
        }
    }
}

/**
  *displayDate
  *shows the user storms based on the date they entered.
  *
  *@param storms struct array, int capacity
  *@return void none
  */
void displayDate(storm_t storms[], int capacity)
{
    int userDate, index;
    printf("Enter the date (yyyymmdd) to search: ");
    scanf("%i", &userDate);

    //if userDate equals any date, it displays the storm data
    for (index = 0; index < capacity; index++)
    {
        if(userDate == storms[index].date)
        {
            displayData(&storms[index]);
        }
    }
}

/**
  *exactName
  *shows the user storms, and count of exact matches found based on the name they entered.
  *
  *@param storms struct array, int capacity
  *@return void none
  */
void exactName(storm_t storms[], int capacity)
{
    char userName[LEN_NAME];
    int index = 0, count = 0;

    printf("Enter the name to search for: ");
    scanf("%s", userName);
    upperCase(userName);

    //if the name entered is an exact match to any storm names, it displays
    for (index = 0; index < capacity; index++)
    {
        if(strcmp(storms[index].stormName, userName)== 0)
        {
            displayData(&storms[index]);
            count++;
        }
    }
    printf("%i exact matches found!\n", count);
}

/**
  *partialName
  *shows the user storms, and count of partial matches found based on the name they entered.
  *
  *@param storms struct array, int capacity
  *@return void none
  */
void partialName(storm_t storms[], int capacity)
{
    char partialName[LEN_NAME];
    int index = 0, count = 0, result;

    printf("Enter the name to search for: ");
    scanf("%s", partialName);
    upperCase(partialName);

    //if the name entered is a partial match to any storm names, it displays
    for (index = 0; index < capacity; index++)
    {
        if(strstr(storms[index].stormName, partialName))
        {
            displayData(&storms[index]);
            count++;
        }
    }
    printf("%i partial matches found!\n", count);
}

/**
  *upperCase
  *uppercases a pointer string
  *
  *@param char pointer string
  *@return void none
  */
void upperCase(char *string)
{
    int index = 0, count;
    int stringLength = strlen(string);
    count = stringLength;

    //uppercases the word
    for(index= 0; index < count; index++)
        string[index] = toupper(string[index]);
}

/**
  *minWindspeed
  *shows storms that have a greater windspeed than the user input
  *
  *@param storms struct array, int capacity
  *@return void none
  */
void minWindspeed(storm_t storms[], int capacity)
{
    int userSpeed, index = 0, count = 0;

    printf("Enter minimum wind speed to display: ");
    scanf("%i", &userSpeed);

    //shows storms that have a greater or equal to windspeed than the userSpeed
    for(index = 0; index < capacity; index++)
    {
        if(userSpeed <= storms[index].windSpeed)
        {
            displayData(&storms[index]);
            count++;
        }
    }
    printf("%i matches found!\n", count);
}

/**
  *maxPressure
  *shows storms that have less pressure than the user input.
  *
  *@param storms pointer array, int capacity
  *@return void none
  */
void maxPressure(storm_t *aPtr, int capacity)
{
    int pressureMax, count = 0, index = 0;

    printf("Enter maximum pressure to display: ");
    scanf("%i", &pressureMax);

    //shows storms that have a less than or equal to pressure than the pressureMax
    for(index = 0; index < capacity; index++)
    {
       if(pressureMax >= (aPtr + index)->pressure)
       {
           displayData(aPtr + index);
           count++;
       }
    }
    printf("%i matches found!\n", count);
}

/**
  *welcome
  *welcomes the user
  *
  *@param void none
  *@return void none
  */
void welcome(void)
{
    printf("Welcome to Arian Tashakkor's Project 6.\n\n");
}

/**
  *credit
  *credits me (Arian)
  *
  *@param void none
  *@return void none
  */
void credit(void)
{
    printf("The data was provided by Arian Tashakkor.\n");
}

/*
Test data and Expected results:
#   Input   Input #2        Expected Result:        Result:
1   1   60  5               3 CHANTAL AND 2 DORIAN  3 CHANTAL AND 2 DORIAN
2   2       20190822        CHANTAL                 CHANTAL
3   3       barry           25 BARRY                25 BARRY
4   4       je              45 JERRY                45 JERRY
5   5       100             42 matches              42 matches
6   6       920             5 matches found         5 matches found
*/

/*TEST CASE 1
Welcome to Arian Tashakkor's Project 6.

1) Display the data for logs
2) Search for data by date
3) Search for data by storm name (exact match)
4) Search for data by storm name (partial match)
5) Search for logs with a user-entered minimum windspeed
6) Search for logs with a user-entered maximum pressure
10) Exit
Choose an option (1-6 or 10) from above: 1
Enter the log #(1 - 493) to start from: 60
Enter how many logs to display (50 MAX): 5
   CHANTAL | DATE: 20190826 | TIME:  600 | LAT: 36.00 | LONG: -44.00 | WINDSPEED:  15 | PRESSURE: 1014
   CHANTAL | DATE: 20190826 | TIME: 1200 | LAT: 36.00 | LONG: -45.00 | WINDSPEED:  15 | PRESSURE: 1014
   CHANTAL | DATE: 20190826 | TIME: 1800 | LAT: 36.00 | LONG: -44.00 | WINDSPEED:  15 | PRESSURE: 1014
    DORIAN | DATE: 20190824 | TIME:  600 | LAT: 10.00 | LONG: -46.00 | WINDSPEED:  25 | PRESSURE: 1011
    DORIAN | DATE: 20190824 | TIME: 1200 | LAT: 10.00 | LONG: -47.00 | WINDSPEED:  30 | PRESSURE: 1010

1) Display the data for logs
2) Search for data by date
3) Search for data by storm name (exact match)
4) Search for data by storm name (partial match)
5) Search for logs with a user-entered minimum windspeed
6) Search for logs with a user-entered maximum pressure
10) Exit
Choose an option (1-6 or 10) from above: 10

The data was provided by Arian Tashakkor.

Process returned 0 (0x0)   execution time : 14.553 s
Press any key to continue.

*/

/*TEST CASE 2
Welcome to Arian Tashakkor's Project 6.

1) Display the data for logs
2) Search for data by date
3) Search for data by storm name (exact match)
4) Search for data by storm name (partial match)
5) Search for logs with a user-entered minimum windspeed
6) Search for logs with a user-entered maximum pressure
10) Exit
Choose an option (1-6 or 10) from above: 2
Enter the date (yyyymmdd) to search: 20190822
   CHANTAL | DATE: 20190822 | TIME:    0 | LAT: 39.00 | LONG: -48.00 | WINDSPEED:  30 | PRESSURE: 1007
   CHANTAL | DATE: 20190822 | TIME:  600 | LAT: 39.00 | LONG: -46.00 | WINDSPEED:  30 | PRESSURE: 1007
   CHANTAL | DATE: 20190822 | TIME: 1200 | LAT: 38.00 | LONG: -44.00 | WINDSPEED:  30 | PRESSURE: 1007
   CHANTAL | DATE: 20190822 | TIME: 1800 | LAT: 38.00 | LONG: -42.00 | WINDSPEED:  30 | PRESSURE: 1007

1) Display the data for logs
2) Search for data by date
3) Search for data by storm name (exact match)
4) Search for data by storm name (partial match)
5) Search for logs with a user-entered minimum windspeed
6) Search for logs with a user-entered maximum pressure
10) Exit
Choose an option (1-6 or 10) from above: 10

The data was provided by Arian Tashakkor.

Process returned 0 (0x0)   execution time : 19.266 s
Press any key to continue.

*/

/*TEST CASE 3
Welcome to Arian Tashakkor's Project 6.

1) Display the data for logs
2) Search for data by date
3) Search for data by storm name (exact match)
4) Search for data by storm name (partial match)
5) Search for logs with a user-entered minimum windspeed
6) Search for logs with a user-entered maximum pressure
10) Exit
Choose an option (1-6 or 10) from above: 3
Enter the name to search for: barr
0 exact matches found!

1) Display the data for logs
2) Search for data by date
3) Search for data by storm name (exact match)
4) Search for data by storm name (partial match)
5) Search for logs with a user-entered minimum windspeed
6) Search for logs with a user-entered maximum pressure
10) Exit
Choose an option (1-6 or 10) from above: 3
Enter the name to search for: barry
     BARRY | DATE: 20190710 | TIME: 1200 | LAT: 28.00 | LONG: -86.00 | WINDSPEED:  25 | PRESSURE: 1009
     BARRY | DATE: 20190710 | TIME: 1800 | LAT: 28.00 | LONG: -87.00 | WINDSPEED:  30 | PRESSURE: 1009
     BARRY | DATE: 20190711 | TIME:    0 | LAT: 27.00 | LONG: -87.00 | WINDSPEED:  30 | PRESSURE: 1008
     BARRY | DATE: 20190711 | TIME:  600 | LAT: 27.00 | LONG: -88.00 | WINDSPEED:  35 | PRESSURE: 1007
     BARRY | DATE: 20190711 | TIME: 1200 | LAT: 27.00 | LONG: -88.00 | WINDSPEED:  35 | PRESSURE: 1005
     BARRY | DATE: 20190711 | TIME: 1800 | LAT: 27.00 | LONG: -88.00 | WINDSPEED:  40 | PRESSURE: 1005
     BARRY | DATE: 20190712 | TIME:    0 | LAT: 27.00 | LONG: -89.00 | WINDSPEED:  45 | PRESSURE: 1001
     BARRY | DATE: 20190712 | TIME:  600 | LAT: 27.00 | LONG: -89.00 | WINDSPEED:  45 | PRESSURE: 1001
     BARRY | DATE: 20190712 | TIME: 1200 | LAT: 28.00 | LONG: -90.00 | WINDSPEED:  50 | PRESSURE:  998
     BARRY | DATE: 20190712 | TIME: 1800 | LAT: 28.00 | LONG: -90.00 | WINDSPEED:  55 | PRESSURE:  993
     BARRY | DATE: 20190713 | TIME:    0 | LAT: 28.00 | LONG: -91.00 | WINDSPEED:  60 | PRESSURE:  993
     BARRY | DATE: 20190713 | TIME:  600 | LAT: 28.00 | LONG: -91.00 | WINDSPEED:  60 | PRESSURE:  993
     BARRY | DATE: 20190713 | TIME: 1200 | LAT: 29.00 | LONG: -91.00 | WINDSPEED:  65 | PRESSURE:  993
     BARRY | DATE: 20190713 | TIME: 1500 | LAT: 29.00 | LONG: -92.00 | WINDSPEED:  65 | PRESSURE:  993
     BARRY | DATE: 20190713 | TIME: 1800 | LAT: 29.00 | LONG: -92.00 | WINDSPEED:  60 | PRESSURE:  996
     BARRY | DATE: 20190714 | TIME:    0 | LAT: 30.00 | LONG: -92.00 | WINDSPEED:  50 | PRESSURE:  999
     BARRY | DATE: 20190714 | TIME:  600 | LAT: 31.00 | LONG: -93.00 | WINDSPEED:  40 | PRESSURE: 1003
     BARRY | DATE: 20190714 | TIME: 1200 | LAT: 31.00 | LONG: -93.00 | WINDSPEED:  35 | PRESSURE: 1005
     BARRY | DATE: 20190714 | TIME: 1800 | LAT: 32.00 | LONG: -93.00 | WINDSPEED:  35 | PRESSURE: 1007
     BARRY | DATE: 20190715 | TIME:    0 | LAT: 33.00 | LONG: -93.00 | WINDSPEED:  25 | PRESSURE: 1008
     BARRY | DATE: 20190715 | TIME:  600 | LAT: 33.00 | LONG: -93.00 | WINDSPEED:  25 | PRESSURE: 1008
     BARRY | DATE: 20190715 | TIME: 1200 | LAT: 34.00 | LONG: -93.00 | WINDSPEED:  20 | PRESSURE: 1008
     BARRY | DATE: 20190715 | TIME: 1800 | LAT: 35.00 | LONG: -93.00 | WINDSPEED:  20 | PRESSURE: 1008
     BARRY | DATE: 20190716 | TIME:    0 | LAT: 36.00 | LONG: -93.00 | WINDSPEED:  15 | PRESSURE: 1009
     BARRY | DATE: 20190716 | TIME:  600 | LAT: 37.00 | LONG: -92.00 | WINDSPEED:  15 | PRESSURE: 1010
25 exact matches found!

1) Display the data for logs
2) Search for data by date
3) Search for data by storm name (exact match)
4) Search for data by storm name (partial match)
5) Search for logs with a user-entered minimum windspeed
6) Search for logs with a user-entered maximum pressure
10) Exit
Choose an option (1-6 or 10) from above: 10

The data was provided by Arian Tashakkor.

Process returned 0 (0x0)   execution time : 21.397 s
Press any key to continue.

*/

/*TEST CASE 4
Welcome to Arian Tashakkor's Project 6.

1) Display the data for logs
2) Search for data by date
3) Search for data by storm name (exact match)
4) Search for data by storm name (partial match)
5) Search for logs with a user-entered minimum windspeed
6) Search for logs with a user-entered maximum pressure
10) Exit
Choose an option (1-6 or 10) from above: 4
Enter the name to search for: je
     JERRY | DATE: 20190917 | TIME:    0 | LAT: 12.00 | LONG: -42.00 | WINDSPEED:  25 | PRESSURE: 1009
     JERRY | DATE: 20190917 | TIME:  600 | LAT: 12.00 | LONG: -43.00 | WINDSPEED:  30 | PRESSURE: 1007
     JERRY | DATE: 20190917 | TIME: 1200 | LAT: 13.00 | LONG: -44.00 | WINDSPEED:  30 | PRESSURE: 1007
     JERRY | DATE: 20190917 | TIME: 1800 | LAT: 13.00 | LONG: -45.00 | WINDSPEED:  30 | PRESSURE: 1007
     JERRY | DATE: 20190918 | TIME:    0 | LAT: 13.00 | LONG: -46.00 | WINDSPEED:  30 | PRESSURE: 1006
     JERRY | DATE: 20190918 | TIME:  600 | LAT: 14.00 | LONG: -47.00 | WINDSPEED:  35 | PRESSURE: 1005
     JERRY | DATE: 20190918 | TIME: 1200 | LAT: 14.00 | LONG: -48.00 | WINDSPEED:  45 | PRESSURE: 1002
     JERRY | DATE: 20190918 | TIME: 1800 | LAT: 14.00 | LONG: -49.00 | WINDSPEED:  50 | PRESSURE: 1000
     JERRY | DATE: 20190919 | TIME:    0 | LAT: 15.00 | LONG: -51.00 | WINDSPEED:  55 | PRESSURE:  998
     JERRY | DATE: 20190919 | TIME:  600 | LAT: 15.00 | LONG: -52.00 | WINDSPEED:  60 | PRESSURE:  996
     JERRY | DATE: 20190919 | TIME: 1200 | LAT: 16.00 | LONG: -53.00 | WINDSPEED:  70 | PRESSURE:  990
     JERRY | DATE: 20190919 | TIME: 1800 | LAT: 17.00 | LONG: -55.00 | WINDSPEED:  80 | PRESSURE:  981
     JERRY | DATE: 20190920 | TIME:    0 | LAT: 17.00 | LONG: -56.00 | WINDSPEED:  90 | PRESSURE:  976
     JERRY | DATE: 20190920 | TIME:  600 | LAT: 18.00 | LONG: -58.00 | WINDSPEED:  85 | PRESSURE:  983
     JERRY | DATE: 20190920 | TIME: 1200 | LAT: 18.00 | LONG: -59.00 | WINDSPEED:  75 | PRESSURE:  990
     JERRY | DATE: 20190920 | TIME: 1800 | LAT: 19.00 | LONG: -61.00 | WINDSPEED:  65 | PRESSURE:  991
     JERRY | DATE: 20190921 | TIME:    0 | LAT: 20.00 | LONG: -62.00 | WINDSPEED:  60 | PRESSURE:  993
     JERRY | DATE: 20190921 | TIME:  600 | LAT: 21.00 | LONG: -63.00 | WINDSPEED:  55 | PRESSURE:  995
     JERRY | DATE: 20190921 | TIME: 1200 | LAT: 21.00 | LONG: -64.00 | WINDSPEED:  55 | PRESSURE:  998
     JERRY | DATE: 20190921 | TIME: 1800 | LAT: 22.00 | LONG: -65.00 | WINDSPEED:  55 | PRESSURE:  998
     JERRY | DATE: 20190922 | TIME:    0 | LAT: 23.00 | LONG: -66.00 | WINDSPEED:  55 | PRESSURE:  998
     JERRY | DATE: 20190922 | TIME:  600 | LAT: 24.00 | LONG: -66.00 | WINDSPEED:  55 | PRESSURE:  998
     JERRY | DATE: 20190922 | TIME: 1200 | LAT: 25.00 | LONG: -66.00 | WINDSPEED:  55 | PRESSURE:  997
     JERRY | DATE: 20190922 | TIME: 1800 | LAT: 26.00 | LONG: -66.00 | WINDSPEED:  55 | PRESSURE:  994
     JERRY | DATE: 20190923 | TIME:    0 | LAT: 27.00 | LONG: -67.00 | WINDSPEED:  55 | PRESSURE:  993
     JERRY | DATE: 20190923 | TIME:  600 | LAT: 27.00 | LONG: -67.00 | WINDSPEED:  55 | PRESSURE:  992
     JERRY | DATE: 20190923 | TIME: 1200 | LAT: 27.00 | LONG: -68.00 | WINDSPEED:  55 | PRESSURE:  991
     JERRY | DATE: 20190923 | TIME: 1800 | LAT: 28.00 | LONG: -68.00 | WINDSPEED:  55 | PRESSURE:  991
     JERRY | DATE: 20190924 | TIME:    0 | LAT: 28.00 | LONG: -68.00 | WINDSPEED:  55 | PRESSURE:  991
     JERRY | DATE: 20190924 | TIME:  600 | LAT: 29.00 | LONG: -68.00 | WINDSPEED:  50 | PRESSURE:  991
     JERRY | DATE: 20190924 | TIME: 1200 | LAT: 30.00 | LONG: -69.00 | WINDSPEED:  45 | PRESSURE:  991
     JERRY | DATE: 20190924 | TIME: 1800 | LAT: 30.00 | LONG: -69.00 | WINDSPEED:  45 | PRESSURE:  993
     JERRY | DATE: 20190925 | TIME:    0 | LAT: 31.00 | LONG: -68.00 | WINDSPEED:  40 | PRESSURE:  996
     JERRY | DATE: 20190925 | TIME:  600 | LAT: 31.00 | LONG: -68.00 | WINDSPEED:  40 | PRESSURE:  997
     JERRY | DATE: 20190925 | TIME: 1200 | LAT: 31.00 | LONG: -67.00 | WINDSPEED:  35 | PRESSURE:  999
     JERRY | DATE: 20190925 | TIME: 1800 | LAT: 32.00 | LONG: -66.00 | WINDSPEED:  30 | PRESSURE: 1000
     JERRY | DATE: 20190926 | TIME:    0 | LAT: 32.00 | LONG: -64.00 | WINDSPEED:  25 | PRESSURE: 1001
     JERRY | DATE: 20190926 | TIME:  600 | LAT: 32.00 | LONG: -63.00 | WINDSPEED:  25 | PRESSURE: 1003
     JERRY | DATE: 20190926 | TIME: 1200 | LAT: 33.00 | LONG: -62.00 | WINDSPEED:  25 | PRESSURE: 1005
     JERRY | DATE: 20190926 | TIME: 1800 | LAT: 34.00 | LONG: -62.00 | WINDSPEED:  25 | PRESSURE: 1005
     JERRY | DATE: 20190927 | TIME:    0 | LAT: 34.00 | LONG: -61.00 | WINDSPEED:  25 | PRESSURE: 1006
     JERRY | DATE: 20190927 | TIME:  600 | LAT: 35.00 | LONG: -60.00 | WINDSPEED:  25 | PRESSURE: 1006
     JERRY | DATE: 20190927 | TIME: 1200 | LAT: 35.00 | LONG: -59.00 | WINDSPEED:  25 | PRESSURE: 1006
     JERRY | DATE: 20190927 | TIME: 1800 | LAT: 35.00 | LONG: -58.00 | WINDSPEED:  25 | PRESSURE: 1006
     JERRY | DATE: 20190928 | TIME:    0 | LAT: 35.00 | LONG: -58.00 | WINDSPEED:  20 | PRESSURE: 1007
45 partial matches found!

1) Display the data for logs
2) Search for data by date
3) Search for data by storm name (exact match)
4) Search for data by storm name (partial match)
5) Search for logs with a user-entered minimum windspeed
6) Search for logs with a user-entered maximum pressure
10) Exit
Choose an option (1-6 or 10) from above: 10

The data was provided by Arian Tashakkor.

Process returned 0 (0x0)   execution time : 14.698 s
Press any key to continue.

*/

/*TEST CASE 5
Welcome to Arian Tashakkor's Project 6.

1) Display the data for logs
2) Search for data by date
3) Search for data by storm name (exact match)
4) Search for data by storm name (partial match)
5) Search for logs with a user-entered minimum windspeed
6) Search for logs with a user-entered maximum pressure
10) Exit
Choose an option (1-6 or 10) from above: 5
Enter minimum wind speed to display: 100
    DORIAN | DATE: 20190830 | TIME: 1800 | LAT: 24.00 | LONG: -70.00 | WINDSPEED: 100 | PRESSURE:  968
    DORIAN | DATE: 20190831 | TIME:    0 | LAT: 25.00 | LONG: -71.00 | WINDSPEED: 115 | PRESSURE:  949
    DORIAN | DATE: 20190831 | TIME:  600 | LAT: 25.00 | LONG: -72.00 | WINDSPEED: 120 | PRESSURE:  947
    DORIAN | DATE: 20190831 | TIME: 1200 | LAT: 25.00 | LONG: -73.00 | WINDSPEED: 125 | PRESSURE:  944
    DORIAN | DATE: 20190831 | TIME: 1800 | LAT: 26.00 | LONG: -74.00 | WINDSPEED: 130 | PRESSURE:  942
    DORIAN | DATE: 20190901 | TIME:    0 | LAT: 26.00 | LONG: -74.00 | WINDSPEED: 135 | PRESSURE:  939
    DORIAN | DATE: 20190901 | TIME:  600 | LAT: 26.00 | LONG: -75.00 | WINDSPEED: 145 | PRESSURE:  934
    DORIAN | DATE: 20190901 | TIME: 1200 | LAT: 26.00 | LONG: -76.00 | WINDSPEED: 155 | PRESSURE:  927
    DORIAN | DATE: 20190901 | TIME: 1640 | LAT: 26.00 | LONG: -77.00 | WINDSPEED: 160 | PRESSURE:  910
    DORIAN | DATE: 20190901 | TIME: 1800 | LAT: 26.00 | LONG: -77.00 | WINDSPEED: 160 | PRESSURE:  910
    DORIAN | DATE: 20190902 | TIME:    0 | LAT: 26.00 | LONG: -77.00 | WINDSPEED: 155 | PRESSURE:  914
    DORIAN | DATE: 20190902 | TIME:  215 | LAT: 26.00 | LONG: -77.00 | WINDSPEED: 155 | PRESSURE:  914
    DORIAN | DATE: 20190902 | TIME:  600 | LAT: 26.00 | LONG: -78.00 | WINDSPEED: 145 | PRESSURE:  916
    DORIAN | DATE: 20190902 | TIME: 1200 | LAT: 26.00 | LONG: -78.00 | WINDSPEED: 135 | PRESSURE:  927
    DORIAN | DATE: 20190902 | TIME: 1800 | LAT: 26.00 | LONG: -78.00 | WINDSPEED: 125 | PRESSURE:  938
    DORIAN | DATE: 20190903 | TIME:    0 | LAT: 26.00 | LONG: -78.00 | WINDSPEED: 115 | PRESSURE:  944
    DORIAN | DATE: 20190903 | TIME:  600 | LAT: 27.00 | LONG: -78.00 | WINDSPEED: 105 | PRESSURE:  950
    DORIAN | DATE: 20190903 | TIME: 1200 | LAT: 27.00 | LONG: -78.00 | WINDSPEED: 100 | PRESSURE:  954
    DORIAN | DATE: 20190905 | TIME:    0 | LAT: 30.00 | LONG: -79.00 | WINDSPEED: 100 | PRESSURE:  955
    DORIAN | DATE: 20190905 | TIME:  600 | LAT: 31.00 | LONG: -79.00 | WINDSPEED: 100 | PRESSURE:  958
    DORIAN | DATE: 20190905 | TIME: 1200 | LAT: 32.00 | LONG: -79.00 | WINDSPEED: 100 | PRESSURE:  958
  HUMBERTO | DATE: 20190918 | TIME:    0 | LAT: 31.00 | LONG: -71.00 | WINDSPEED: 100 | PRESSURE:  952
  HUMBERTO | DATE: 20190918 | TIME:  600 | LAT: 31.00 | LONG: -70.00 | WINDSPEED: 105 | PRESSURE:  951
  HUMBERTO | DATE: 20190918 | TIME: 1200 | LAT: 32.00 | LONG: -68.00 | WINDSPEED: 105 | PRESSURE:  951
  HUMBERTO | DATE: 20190918 | TIME: 1800 | LAT: 32.00 | LONG: -67.00 | WINDSPEED: 110 | PRESSURE:  950
  HUMBERTO | DATE: 20190919 | TIME:    0 | LAT: 33.00 | LONG: -65.00 | WINDSPEED: 110 | PRESSURE:  950
  HUMBERTO | DATE: 20190919 | TIME:  300 | LAT: 33.00 | LONG: -64.00 | WINDSPEED: 110 | PRESSURE:  950
  HUMBERTO | DATE: 20190919 | TIME:  600 | LAT: 34.00 | LONG: -62.00 | WINDSPEED: 105 | PRESSURE:  951
   LORENZO | DATE: 20190926 | TIME: 1200 | LAT: 15.00 | LONG: -39.00 | WINDSPEED: 105 | PRESSURE:  955
   LORENZO | DATE: 20190926 | TIME: 1800 | LAT: 16.00 | LONG: -40.00 | WINDSPEED: 115 | PRESSURE:  947
   LORENZO | DATE: 20190927 | TIME:    0 | LAT: 17.00 | LONG: -41.00 | WINDSPEED: 125 | PRESSURE:  937
   LORENZO | DATE: 20190927 | TIME:  600 | LAT: 18.00 | LONG: -41.00 | WINDSPEED: 125 | PRESSURE:  937
   LORENZO | DATE: 20190927 | TIME: 1200 | LAT: 18.00 | LONG: -42.00 | WINDSPEED: 120 | PRESSURE:  943
   LORENZO | DATE: 20190927 | TIME: 1800 | LAT: 19.00 | LONG: -43.00 | WINDSPEED: 110 | PRESSURE:  948
   LORENZO | DATE: 20190928 | TIME:    0 | LAT: 20.00 | LONG: -44.00 | WINDSPEED: 105 | PRESSURE:  952
   LORENZO | DATE: 20190928 | TIME:  600 | LAT: 21.00 | LONG: -44.00 | WINDSPEED: 100 | PRESSURE:  957
   LORENZO | DATE: 20190928 | TIME: 1200 | LAT: 22.00 | LONG: -44.00 | WINDSPEED: 105 | PRESSURE:  957
   LORENZO | DATE: 20190928 | TIME: 1800 | LAT: 22.00 | LONG: -45.00 | WINDSPEED: 115 | PRESSURE:  951
   LORENZO | DATE: 20190929 | TIME:    0 | LAT: 23.00 | LONG: -45.00 | WINDSPEED: 130 | PRESSURE:  936
   LORENZO | DATE: 20190929 | TIME:  300 | LAT: 24.00 | LONG: -45.00 | WINDSPEED: 140 | PRESSURE:  925
   LORENZO | DATE: 20190929 | TIME:  600 | LAT: 24.00 | LONG: -45.00 | WINDSPEED: 130 | PRESSURE:  933
   LORENZO | DATE: 20190929 | TIME: 1200 | LAT: 25.00 | LONG: -44.00 | WINDSPEED: 110 | PRESSURE:  945
42 matches found!

1) Display the data for logs
2) Search for data by date
3) Search for data by storm name (exact match)
4) Search for data by storm name (partial match)
5) Search for logs with a user-entered minimum windspeed
6) Search for logs with a user-entered maximum pressure
10) Exit
Choose an option (1-6 or 10) from above: 10

The data was provided by Arian Tashakkor.

Process returned 0 (0x0)   execution time : 11.785 s
Press any key to continue.

*/

/*TEST CASE 6
Welcome to Arian Tashakkor's Project 6.

1) Display the data for logs
2) Search for data by date
3) Search for data by storm name (exact match)
4) Search for data by storm name (partial match)
5) Search for logs with a user-entered minimum windspeed
6) Search for logs with a user-entered maximum pressure
10) Exit
Choose an option (1-6 or 10) from above: 6
Enter maximum pressure to display: 920
    DORIAN | DATE: 20190901 | TIME: 1640 | LAT: 26.00 | LONG: -77.00 | WINDSPEED: 160 | PRESSURE:  910
    DORIAN | DATE: 20190901 | TIME: 1800 | LAT: 26.00 | LONG: -77.00 | WINDSPEED: 160 | PRESSURE:  910
    DORIAN | DATE: 20190902 | TIME:    0 | LAT: 26.00 | LONG: -77.00 | WINDSPEED: 155 | PRESSURE:  914
    DORIAN | DATE: 20190902 | TIME:  215 | LAT: 26.00 | LONG: -77.00 | WINDSPEED: 155 | PRESSURE:  914
    DORIAN | DATE: 20190902 | TIME:  600 | LAT: 26.00 | LONG: -78.00 | WINDSPEED: 145 | PRESSURE:  916
5 matches found!

1) Display the data for logs
2) Search for data by date
3) Search for data by storm name (exact match)
4) Search for data by storm name (partial match)
5) Search for logs with a user-entered minimum windspeed
6) Search for logs with a user-entered maximum pressure
10) Exit
Choose an option (1-6 or 10) from above: 10

The data was provided by Arian Tashakkor.

Process returned 0 (0x0)   execution time : 12.240 s
Press any key to continue.

*/
