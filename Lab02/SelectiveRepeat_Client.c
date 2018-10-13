/* 	Stop And wait CLIENT
	CO323 Lab02
	E/14/158 gihanchanaka@gmail.com
	15-02-2018
*/

#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <ctype.h>
#include <sys/time.h>

#define TRUE 1
#define FALSE 0


char* sendLine;
static char receiveLine[1000];
int sockfd,n;
struct sockaddr_in serverAddress;
size_t len;
/*
	Frame
	8 char
	c0	c1	c2 c3 c4 c5 c6	c7
	Seq	|<------DATA---->| '\0'
	No
	0-7     The sequence number can take from 0 to 7
*/


int main(int argc,char** argv){

	
	if(argc!=2){
		printf("usage: %s <IP address>\n",argv[0] );
		return -1;
	}

	sockfd=socket(AF_INET,SOCK_DGRAM,0);
	bzero(&serverAddress,sizeof(serverAddress));
	serverAddress.sin_family=AF_INET;
	serverAddress.sin_addr.s_addr=inet_addr(argv[1]);
	serverAddress.sin_port=htons(32000);
	
	struct timeval timeout;
	timeout.tv_sec = 0;
	timeout.tv_usec = 1000;
	setsockopt(sockfd, SOL_SOCKET, SO_RCVTIMEO,&timeout,sizeof(timeout));


	getline(&sendLine, &len, stdin);
	printf("Read line: %s\n",sendLine );

	int length=strlen(sendLine);
	int frames=(length/6);
	if(length%6!=0)frames++;

	printf("Total length: %d and frames: %d\n",length,frames );

	char ** toSend=(char **)malloc(sizeof(char *)*frames);

	int f;
	for(f=0;f<frames;f++){
		toSend[f]=(char *)malloc(sizeof(char)*8);
		toSend[f][0]=(char)('0'+f%8);
		toSend[f][7]='\0';
		int cc;
		for(cc=0;cc<6;cc++)toSend[f][cc+1]=sendLine[6*f+cc];

	}


	struct timeval* sendTime=(struct timeval*)malloc(sizeof(struct timeval)*frames);
	for(f=0;f<frames;f++)gettimeofday(&sendTime[f],0);


	int* acked=(int *)malloc(sizeof(int)*frames);
	for(f=0;f<frames;f++)acked[f]=0;


	int lastSent=0;
	int lastAcked=-1;


	while(lastAcked<frames){
		n=recvfrom(sockfd,receiveLine,1000,0,NULL,NULL);
		if(n>0){
			receiveLine[n]='\0';
			if(receiveLine[0]=='A' & receiveLine[1]=='C' & receiveLine[2]=='K'){
				int no=(int)(receiveLine[3]-'0');
				int kk;
				for(kk=1;kk<=8;kk++){
					if((lastAcked+kk)%8==no){
						acked[lastAcked+kk]=1;
						break;
					}
				}
			}
			if(acked[lastAcked+1]){
				lastAcked++;
				printf("Last acked %d\n",lastAcked );
				if(lastAcked==frames-1){
					printf("Complete!\n");
					break;
				}
			}
		}



		int ff;
		for(ff=1;ff<=8;ff++){
			if(lastAcked+ff >=frames) break;
			else{
				if(!acked[lastAcked+ff]){
					struct timeval t;
					gettimeofday(&t,0);
					if(t.tv_usec-sendTime[lastAcked+ff].tv_usec>1000){
						sendto(sockfd,toSend[lastAcked+ff],8,0,(struct sockaddr*)&serverAddress,sizeof(serverAddress));
						gettimeofday(&sendTime[lastAcked+ff],0);
					}
				}
			}

		}
	}



	





	return 0;
}