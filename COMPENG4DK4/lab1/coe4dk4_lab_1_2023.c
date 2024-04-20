
/*
 *
 * Simulation of Single Server Queueing System
 * 
 * Copyright (C) 2014 Terence D. Todd Hamilton, Ontario, CANADA,
 * todd@mcmaster.ca
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

/*******************************************************************************/

#include <stdio.h>
#include "simlib.h"
#include<windows.h>

/*******************************************************************************/

/*
 * Simulation Parameters
 */

//#define RANDOM_SEED 400268588
#define NUMBER_TO_SERVE 50e6
//#define NUMBER_TO_SERVE 10000

#define SERVICE_TIME 6
#define ARRIVAL_RATE 0.02

#define BLIP_RATE 10000

// mode define
#define MD1 1
//#define MM1 1
//#define QUEUEMAX 1

//#define MAX_QUEUE_SIZE 5

/*******************************************************************************/

/*
 * main() uses various simulation parameters and creates a clock variable to
 * simulate real time. A loop repeatedly determines if the next event to occur
 * is a customer arrival or customer departure. In either case the state of the
 * system is updated and statistics are collected before the next
 * iteration. When it finally reaches NUMBER_TO_SERVE customers, the program
 * outputs some statistics such as mean delay.
 */

int main()
{
  double result_delay[10][10];
  double reject_rate[10][10];
  int random_nums[10] = {400268588,400247013,1,234867,125,12458,98673,34,60982,34580};
  int max_queue_size[6] = {1, 3,5, 7, 9, 11};

  double arraival_rates[10];
  for (int i = 0; i<10; i++)
    {
    #if defined(QUEUEMAX)
    arraival_rates[i] = 0.2+(double)i*0.2;
    #else
    // change this for different question
    arraival_rates[i] = 0.2+(double)i*0.01;
    #endif
    }

  #if defined(QUEUEMAX)
  printf("Set Queue Max Mode\n");
  #endif
  #if defined(MD1) 
  printf("MD1\n");
  #endif
  #if defined(MM1) 
  printf("MM1\n");
  #endif

  for (int i = 0; i<10; i++)
    {
    /* Set the seed of the random number generator. */
    random_generator_initialize(random_nums[i]);
    #if defined(QUEUEMAX)
    for (int k = 0; k<6; k++)
      {
      int MAX_QUEUE_SIZE = max_queue_size[k];
    #else
    for (int k = 0; k<10; k++)
      {
    #endif
      #if defined(MD1) || defined(MM1)
      double arraival_rate = arraival_rates[k];
      printf("\nAR = %f\n", arraival_rate);
      #else
      double arraival_rate = 0.3;
      printf("\nsize = %d\n", MAX_QUEUE_SIZE);
      #endif

      
      double clock = 0; /* Clock keeps track of simulation time. */

      /* System state variables. */
      int number_in_system = 0;
      double next_arrival_time = 0;
      double next_departure_time = 0;
      int rejected_customer = 0;

      /* Data collection variables. */
      long int total_served = 0;
      long int total_arrived = 0;

      double total_busy_time = 0;
      double integral_of_n = 0;
      double last_event_time = 0;



      /* Process customers until we are finished. */
      while (total_served < NUMBER_TO_SERVE) {
        //Sleep(1000);
        // service time set
        #if defined(MD1)
        double next_service_time = SERVICE_TIME;
        #else
        double next_service_time = exponential_generator((double) SERVICE_TIME);
        #endif

        // ar set
        double next_AR = 1/arraival_rate;
        //double next_AR = exponential_generator((double) 1/arraival_rate);

      //printf("%f, %f\n",next_arrival_time,next_departure_time);
        /* Test if the next event is a customer arrival or departure. */
      if(number_in_system == 0 || next_arrival_time < next_departure_time) {

        /*
          * A new arrival is occurring.
          */

        clock = next_arrival_time;
        next_arrival_time = clock + next_AR;

        /* Update our statistics. */
        integral_of_n += number_in_system * (clock - last_event_time);
        last_event_time = clock;

        #ifdef QUEUEMAX
        // reject
        if (number_in_system >= MAX_QUEUE_SIZE)
            {
            rejected_customer++;
            total_arrived++;
            continue;
            }
        #endif

        number_in_system++;
        total_arrived++;
        //printf("a %f %f %d %ld\n",clock,next_arrival_time,number_in_system,total_arrived);
        /* If this customer has arrived to an empty system, start its
      service right away. */
        if(number_in_system == 1) next_departure_time = clock + next_service_time;

      } else {
        /*
          * A customer departure is occuring. 
          */

        clock = next_departure_time;

        /* Update our statistics. */
        integral_of_n += number_in_system * (clock - last_event_time);
        last_event_time = clock;

        number_in_system--;
        total_served++;
        total_busy_time += next_service_time;

        /* 
          * If there are other customers waiting, start one in service
          * right away.
          */

        if(number_in_system > 0) next_departure_time = clock + next_service_time;
        //printf("d %f %f %d %ld\n",clock,next_departure_time,number_in_system,total_served);
        /* 
          * Every so often, print an activity message to show we are active. 
          */

        if (total_served % BLIP_RATE == 0)
          printf("Customers served = %ld (Total arrived = %ld)\r",
            total_served, total_arrived);
      }

      }



      /* Output final result_delays. */
      //printf("\nUtilization = %f\n", total_busy_time/clock);
      //printf("Fraction served = %f\n", (double) total_served/total_arrived);
      //printf("Mean number in system = %f\n", integral_of_n/clock);
      printf("Mean delay = %f\n", integral_of_n/total_served);
      printf("reject rate =%d %ld %f\n",rejected_customer,total_arrived, (double)rejected_customer/(double)total_arrived);
      result_delay[k][i] = integral_of_n/total_served;
      reject_rate[k][i] = (double)rejected_customer/(double)total_arrived;
      }
    }

  //analysis
  for (int i = 0; i<10; i++)
    {
    printf(", %d",random_nums[i]);
    }
  printf(", avg \n");
  #ifdef QUEUEMAX
  for (int i = 0; i<6; i++)
    {
    printf("%d ,",max_queue_size[i]);
    double totalRJ = 0;
    for (int k = 0; k<10; k++)
      {
      printf("%f ,",reject_rate[i][k]);
      totalRJ += reject_rate[i][k];
      }
    printf("%f \n",totalRJ/(double)10);
    }
  printf("\n\n");

  for (int i = 0; i<6; i++)
    {
    printf("%d ,",max_queue_size[i]);
    double totalDelay = 0;
    for (int k = 0; k<10; k++)
      {
      printf("%f ,",result_delay[i][k]);
      totalDelay += result_delay[i][k];
      }
    printf("%f \n",totalDelay/(double)10);
    }
  return 0;
  #else
  for (int i = 0; i<10; i++)
    {
    printf("%f ,",arraival_rates[i]);
    double totalDelay = 0;
    for (int k = 0; k<10; k++)
      {
      printf("%f ,",result_delay[i][k]);
      totalDelay += result_delay[i][k];
      }
    printf("%f \n",totalDelay/(double)10);
    }
  return 0;
  #endif

}






