
/*
 * 
 * Simulation_Run of A Single Server Queueing System
 * 
 * Copyright (C) 2014 Terence D. Todd Hamilton, Ontario, CANADA,
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

/******************************************************************************/

#include <stdio.h>
#include "trace.h"
#include "main.h"
#include "output.h"
#include "packet_transmission.h"
#include "packet_arrival.h"

/******************************************************************************/

/*
 * This function will schedule the end of a packet transmission at a time given
 * by event_time. At that time the function "end_packet_transmission" (defined
 * in packet_transmissionl.c) is executed. A packet object is attached to the
 * event and is recovered in end_packet_transmission.c.
 */

long
schedule_end_packet_transmission_event(Simulation_Run_Ptr simulation_run,
				       double event_time,
				       Server_Ptr link, int bufferid)
{
  Event event;

  event.description = "Packet Xmt End";
  event.function = end_packet_transmission_event;
  event.attachment = (void *) link;
  event.bufferid = bufferid;

  return simulation_run_schedule_event(simulation_run, event, event_time);
}

/******************************************************************************/

/*
 * This is the event function which is executed when the end of a packet
 * transmission event occurs. It updates its collected data then checks to see
 * if there are other packets waiting in the fifo queue. If that is the case it
 * starts the transmission of the next packet.
 */

void
end_packet_transmission_event(Simulation_Run_Ptr simulation_run, void * link, int bufferid)
{
  Simulation_Run_Data_Ptr data;
  Packet_Ptr this_packet, next_packet;

  TRACE(printf("End Of Packet.\n"););

  data = (Simulation_Run_Data_Ptr) simulation_run_data(simulation_run);

  /* 
   * Packet transmission is finished. Take the packet off the data link.
   */

  this_packet = (Packet_Ptr) server_get(link);

  /* Collect statistics. */
  // reassign server 1 package to server 2 or 3
  double this_delay = simulation_run_get_time(simulation_run) - this_packet->arrive_time;
  int origid = this_packet->orig_buffid;
   //printf("%.5f %.5f ",this_packet->arrive_time, this_delay);
  if (bufferid == 0)
    {
    int assigned_server = uniform_generator()>(double)P12 ? 2 : 1;

    this_packet->arrive_time = simulation_run_get_time(simulation_run);
    this_packet->service_time = get_packet_transmission_time(assigned_server);
    this_packet->status = WAITING;

    //printf("redirect %d %d %.5f %.5f\n",bufferid,assigned_server, this_packet->arrive_time, this_packet->service_time);

    if(server_state(data->link[assigned_server]) == BUSY) {
      fifoqueue_put(data->buffer[assigned_server], (void*) this_packet);
    } else {
      start_transmission_on_link(simulation_run, this_packet, data->link[assigned_server], assigned_server);
    }
    }
  else 
    {
    //printf("finish %d %.5f\n",bufferid, simulation_run_get_time(simulation_run));

    data->number_of_packets_processed++;
    data->number_of_packets_processeds[origid]++;

    /* This packet is done ... give the memory back. */
    xfree((void *) this_packet);
    }
  data->accumulated_delay += this_delay;
  data->accumulated_delays[origid] += this_delay;

  /* Output activity blip every so often. */
  output_progress_msg_to_screen(simulation_run);

  /* 
   * See if there is are packets waiting in the buffer. If so, take the next one
   * out and transmit it immediately.
  */

  if(fifoqueue_size(data->buffer[bufferid]) > 0) {
    next_packet = (Packet_Ptr) fifoqueue_get(data->buffer[bufferid]);
    start_transmission_on_link(simulation_run, next_packet, link, bufferid);
  }
}

/*
 * This function ititiates the transmission of the packet passed to the
 * function. This is done by placing the packet in the server. The packet
 * transmission end event for this packet is then scheduled.
 */

void
start_transmission_on_link(Simulation_Run_Ptr simulation_run, 
			   Packet_Ptr this_packet,
			   Server_Ptr link, int bufferid)
{
  TRACE(printf("Start Of Packet.\n");)

  server_put(link, (void*) this_packet);
  this_packet->status = XMTTING;

  /* Schedule the end of packet transmission event. */
  schedule_end_packet_transmission_event(simulation_run,
	 simulation_run_get_time(simulation_run) + this_packet->service_time,
	 (void *) link, bufferid);
}

/*
 * Get a packet transmission time. For now it is a fixed value defined in
 * simparameters.h
 */

double
get_packet_transmission_time(int bufferid)
{
  double bitrate[3] = {LINK_BIT_RATE};
  return ((double) PACKET_LENGTH/bitrate[bufferid]);
}


