#include <arpa/inet.h>
#include <netinet/in.h>
#include <stdio.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>

#define BUFLEN 512
#define PORT 8000

#define SRV_IP "192.168.2.11"


void diep(char *s)
{
    perror(s);
    exit(1);
}

int main(void)
{
    struct sockaddr_in si_other;
    int s, slen=sizeof(si_other);
    int i = 0;
    char buf[BUFLEN];
    if ((s=socket(AF_INET, SOCK_DGRAM, IPPROTO_UDP))==-1)
        diep("socket");
    memset((char *) &si_other, 0, sizeof(si_other));
    si_other.sin_family = AF_INET;
    si_other.sin_port = htons(PORT);
    if (inet_aton(SRV_IP, &si_other.sin_addr)==0) {
        fprintf(stderr, "inet_aton() failed\n");
        exit(1);
    }
    while(1) {
        if (getchar()) {
            printf("Sending stop packet %d\n", i);
            sprintf(buf, "STOP");
            if (sendto(s, buf, BUFLEN, 0, (struct sockaddr *)&si_other, slen)==-1)
                diep("sendto()");
        }
        i++;
    }
    close(s);
    return 0;
}
