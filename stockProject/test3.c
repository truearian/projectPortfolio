#include <stdio.h>
#include <string.h>

#define MAX_STOCKS 20
#define LEN_TICKER 10

void displayData(char ticker[][LEN_TICKER + 1], double *price, double *changePercent, int index);
int tickDisplay(char *userInput,char ticker[][LEN_TICKER + 1], int count);

/**
  *main
  *the main function is called at the start of the program
  *
  *@param void none
  *@return int 0, validating that the program ran correctly
  */
int main(void)
{
    char ticker[MAX_STOCKS][LEN_TICKER + 1] = {"GE", "AAL", "NCLH", "CCL", "BA", "F", "PFE", "AAPL", "BAC", "MRO", "WFC", "AMD", "INTC", "XOM"};
    double price[MAX_STOCKS] = {9.91, 13.02, 20.76, 17.77, 209.9, 8.96, 36.55, 119.29, 27.44, 5.78, 25.46, 83.02, 45.35, 38.13};
    double changePercent[MAX_STOCKS] = {2.38, 2.52, -5.89, -1.55, -0.07, 2.4, 1.42, -0.08, -0.4, 5.09, 1.68, -0.41, -0.4, -1.4};
    char option = 'D';
    double extPrice;
    int index = 0, shares;
    char userInput[LEN_TICKER + 1];

    printf("Welcome to Arian Tashakkor's Test 3 Program.\n");

    while(option != 'E')
    {
        printf("(D)isplay all stock data.\n(B)UY\n(E)xit\n");
        printf("Choose an option (D/B/E): ");
        scanf("\n%c", &option);

        switch(option)
        {
        case 'D':
            index = 0;
            for(index = 0; index < MAX_STOCKS - 6; index++)
            {
                displayData(ticker, price, changePercent, index);
            }
            break;
        case 'B':
            index = 0;
            printf("Enter the ticker to look for: ");
            scanf("\n%s", &userInput);
            index = tickDisplay(userInput, ticker, MAX_STOCKS);
            if(index != -1)
            {
                printf("Enter number of shares: ");
                scanf("%i", &shares);
                extPrice = price[index] / (double)shares;

                printf("Ticker: %5s | Price: $%6.2lf | # of Shares: %i | Extended Price: $%6.2lf\n", ticker[index], price[index], shares, extPrice);
            }else
            printf("ERROR! Ticker not found.\n");
            break;
        case 'E':
            break;
        default:
            printf("Choice invalid!\n");
            break;
        }
    }
    printf("Prepared by: Arian Tashakkor!");

    return 0;
}

/**
  *displayData
  *displays the data for a single ticker given the index
  *
  *@param ticker array, double price and changePercent, int index
  *@return none
  */
void displayData(char ticker[][LEN_TICKER + 1], double *price, double *changePercent, int index)
{
    printf("Ticker: %5s || Price: $%6.2lf || Percent Change: %4.2lf\n", ticker[index], price[index], changePercent[index]);
}

/**
  *tickDisplay
  *displays the ticker if the user input matches any ticker names
  *
  *@param char user input, ticker array, and count which is 20
  *@return found index of the ticker
  */
int tickDisplay(char *userInput,char ticker[][LEN_TICKER + 1], int count)
{
    int index, result, foundIndex = -1;

    for(index = 0; index < count -6; index++)
    {
        result = strcmp(ticker[index], userInput);
        if(result==0)
            foundIndex = index;
    }
    return foundIndex;
}
/*TEST DATA: its rushed sorryyyy
Welcome to Arian Tashakkor's Test 3 Program.
(D)isplay all stock data.
(B)UY
(E)xit
Choose an option (D/B/E): D
Ticker:    GE || Price: $  9.91 || Percent Change: 2.38
Ticker:   AAL || Price: $ 13.02 || Percent Change: 2.52
Ticker:  NCLH || Price: $ 20.76 || Percent Change: -5.89
Ticker:   CCL || Price: $ 17.77 || Percent Change: -1.55
Ticker:    BA || Price: $209.90 || Percent Change: -0.07
Ticker:     F || Price: $  8.96 || Percent Change: 2.40
Ticker:   PFE || Price: $ 36.55 || Percent Change: 1.42
Ticker:  AAPL || Price: $119.29 || Percent Change: -0.08
Ticker:   BAC || Price: $ 27.44 || Percent Change: -0.40
Ticker:   MRO || Price: $  5.78 || Percent Change: 5.09
Ticker:   WFC || Price: $ 25.46 || Percent Change: 1.68
Ticker:   AMD || Price: $ 83.02 || Percent Change: -0.41
Ticker:  INTC || Price: $ 45.35 || Percent Change: -0.40
Ticker:   XOM || Price: $ 38.13 || Percent Change: -1.40
(D)isplay all stock data.
(B)UY
(E)xit
Choose an option (D/B/E): B
Enter the ticker to look for: AMD
Enter number of shares: 10
Ticker:   AMD | Price: $ 83.02 | # of Shares: 10 | Extended Price: $  8.30
(D)isplay all stock data.
(B)UY
(E)xit
Choose an option (D/B/E): B
Enter the ticker to look for: MS
ERROR! Ticker not found.
(D)isplay all stock data.
(B)UY
(E)xit
Choose an option (D/B/E): 5
Choice invalid!
(D)isplay all stock data.
(B)UY
(E)xit
Choose an option (D/B/E): E
Prepared by: Arian Tashakkor!
Process returned 0 (0x0)   execution time : 24.594 s
Press any key to continue.

*/
