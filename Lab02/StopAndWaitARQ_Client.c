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
*/

enum state1 {WAITING_FOR_NEW_LINE,SENDING_A_PACKET};
enum state2 {NA,SENDING_FRAME,AWAITING_ACKNOWLEDGEMENT};

enum state1 state_1=WAITING_FOR_NEW_LINE;
enum state2 state_2=NA;
int state_3_seqNumber=0;
int state_4_nextCharIndex=0;



void mainLoop(){
	switch(state_1){
		case WAITING_FOR_NEW_LINE:{
			getline(&sendLine, &len, stdin);
			printf("Read line: %s\n",sendLine );
			state_1=SENDING_A_PACKET;
			state_2=SENDING_FRAME;
			state_4_nextCharIndex=0;
			break;
		}

		case SENDING_A_PACKET:{
			switch(state_2){
				case SENDING_FRAME:{
					char packet[8];
					packet[0]=(char)(state_3_seqNumber+'0');
					int i,j;
					for(i=0;i<6;i++){
						if(sendLine[i]!='\0'){
							packet[i+1]=sendLine[state_4_nextCharIndex+i];
						}
						else{
							for(j=i;j<7;j++){
								packet[j+1]='\0';
							}
							break;
						}
						packet[7]=0;
					}
					printf("Created packet: %s\n",packet);

					sendto(sockfd,packet,8,0,(struct sockaddr*)&serverAddress,sizeof(serverAddress));
					printf("Sent packet: %s\n",packet);


					state_2=AWAITING_ACKNOWLEDGEMENT;
					break;
				}
				case AWAITING_ACKNOWLEDGEMENT:{
					n=recvfrom(sockfd,receiveLine,1000,0,NULL,NULL);
					if(receiveLine[0]=='a'&&receiveLine[1]=='c'&&receiveLine[2]=='k'&&receiveLine[3]==(char)('0'+state_3_seqNumber)){
						state_3_seqNumber=(state_3_seqNumber+1)%2;
						state_4_nextCharIndex+=6;
						state_2=SENDING_FRAME;
						printf("Acked\n");
						if(state_4_nextCharIndex>=strlen(sendLine)){
							printf("End of packet\n");

							state_1=WAITING_FOR_NEW_LINE;
						}
					}
					else{
						printf("Not ack\n");
						state_2=SENDING_FRAME;
					}
					break;
				}
			}
			break;
		}
	}
}

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
	timeout.tv_sec = 1;
	timeout.tv_usec = 0;
	setsockopt(sockfd, SOL_SOCKET, SO_RCVTIMEO,&timeout,sizeof(timeout));

	while(TRUE){
		mainLoop();
	}
	


	return 0;
}