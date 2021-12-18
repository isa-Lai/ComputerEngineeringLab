#include <stdio.h>
#include <stdlib.h>
#include "student.h"

//Question 1 definitions and prototypes
char *my_strcat(const char * const str1, const char * const str2);


//Question 2 definitions and prototypes
student **create_class_list(char *filename, int *sizePtr);
int find(int idNo, student **list, int size);
void input_grades(char *filename, student **list, int size);
void compute_final_course_grades(student **list, int size);
void output_final_course_grades(char *filename, student **list, int size);

void withdraw(int idNo, student **list, int *sizePtr);
void destroy_list(student **list, int *sizePtr);


//Question 3 definitions and prototypes
char **read_words(const char *input_filename, int *nPtr);
void sort_words(char **words, int size);
void sort2_words(char **words, int size);
// below two functions are examples of utility functions that you might need while implementing your sort..
// you do *NOT* have to implement them if you do not need to 
// feel free to add your own functions if you need them 
int compare_str(const char *str1,const char *str2);
void swap(char **str1, char **str2);
