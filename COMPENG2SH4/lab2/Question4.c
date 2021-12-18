#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "Questions.h"

void letter_freq(const char word[], int freq[]){
	//access to each element in word
	int i = 0;
	while (word[i]!='\0'){
		int address = word[i];
		if (address <=90 && address>=65){
			freq[address-65]+=1; //upper case situation
		}
		else if(address <=122 && address>=97){
			freq[address-97]+=1; //lower case situation
		}
		i++;
	}
}
