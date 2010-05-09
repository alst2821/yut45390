/***
*  Program to print hex masks for bit operations
*
*/
#include <stdio.h>
int main()
{
    unsigned int bitp;
    unsigned int mask;
    for (bitp = 0; bitp < 16; bitp++)
    {
        mask = (1 << bitp);
        printf("bit position %u \tmask 0X%04x\n", bitp, mask);
    }
    return 0;
}
