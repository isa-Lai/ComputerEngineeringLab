
/*
 * Simulation_Run of the ALOHA Protocol
 * 
 * Copyright (C) 2014 Terence D. Todd Hamilton, Ontario, CANADA
 * todd@mcmaster.ca
 * 
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option)
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 * more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

/*******************************************************************************/

#include "packet_duration.h"
#include "math.h"
#include <stdio.h>

/*******************************************************************************/

double
get_packet_duration(void)
{
  return ((double) MEAN_PACKET_DURATION);
}

double
get_next_time_slot(double orig_time)
{
  double slot_len =(double)MEAN_PACKET_DURATION+2*(double)EPSILON;
  double current_slot_time = floor(orig_time/slot_len)*slot_len;
  //in avaibale sending range, send now
  if (orig_time<(current_slot_time+(double)EPSILON) && orig_time>(current_slot_time+slot_len-(double)EPSILON))
    {
    //printf("now %f, transmit %f, slotlen %f\n",orig_time,orig_time,slot_len );
    return orig_time;
    }
  //send at next slot
  //printf("now %f, transmit %f, slotlen %f\n",orig_time,current_slot_time+slot_len,slot_len );
  return (current_slot_time+slot_len);
}




