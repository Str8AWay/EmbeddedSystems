#include <stdio.h>

int main(void)
{
    unsigned char foo = 127;
    printf("-foo = %d\t -foo + OFFSET = %d\n", -foo, -foo + 127);
    return 0;
}
