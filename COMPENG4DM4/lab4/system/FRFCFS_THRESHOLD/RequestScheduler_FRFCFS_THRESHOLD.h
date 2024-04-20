
#ifndef REQUESTSCHEDULER_FRFCFS_THRESHOLD_H
#define REQUESTSCHEDULER_FRFCFS_THRESHOLD_H

#define HIT_Threshold 10

#include "../../src/RequestScheduler.h"

namespace MCsim
{
	class RequestScheduler_FRFCFS_THRESHOLD : public RequestScheduler
	{
	private:
	public:
		RequestScheduler_FRFCFS_THRESHOLD(std::vector<RequestQueue *> &requestQueues, std::vector<CommandQueue *> &commandQueues, const std::map<unsigned int, bool> &requestorTable) : RequestScheduler(requestQueues, commandQueues, requestorTable) {}
		// Simple FR FCFS scheduler in the Request Queueu structure
		~RequestScheduler_FRFCFS_THRESHOLD() { countTable.clear();}
		std::map< unsigned int, std::map< unsigned int, unsigned int> > countTable;
		void requestSchedule()
		{
			
			for (size_t index = 0; index < requestQueue.size(); index++)
			{ // Loop over the queueing structure
			
				if (requestQueue[index]->getSize(false, 0) > 0)
				{
					scheduledRequest = scheduleFR(index); // Take the candidate request from the correspoding queue

					// get the first one in queue if it exceed thredhold
					if (scheduledRequest != NULL && isRowHit(scheduledRequest))
					{
						if (countTable.find(scheduledRequest->addressMap[Rank]) != countTable.end())
						{
							if (countTable[scheduledRequest->rank].find(scheduledRequest->addressMap[Bank]) != countTable[scheduledRequest->addressMap[Rank]].end())
							{
								if (countTable[scheduledRequest->addressMap[Rank]][scheduledRequest->addressMap[Bank]] >= HIT_Threshold)
								{
									scheduledRequest = requestQueue[index]->getRequest(0);
								}
							}
						}
					}

					if ((scheduledRequest != NULL) && (commandQueue[scheduledRequest->addressMap[Bank]]->getSize(true) == 0))
					{
						if (isSchedulable(scheduledRequest, isRowHit(scheduledRequest))) // Determine if the request target is an open row or not
						{
							//update counter
							if (isRowHit(scheduledRequest) && countTable[scheduledRequest->addressMap[Rank]][scheduledRequest->addressMap[Bank]]<HIT_Threshold)
								countTable[scheduledRequest->addressMap[Rank]][scheduledRequest->addressMap[Bank]]++;
							else
								countTable[scheduledRequest->addressMap[Rank]][scheduledRequest->addressMap[Bank]]=1;

							//TRACE_REQ("Rank: " << scheduledRequest->addressMap[Rank] <<" Bank: " << scheduledRequest->addressMap[Bank] << " Count: " << countTable[scheduledRequest->addressMap[Rank]][scheduledRequest->addressMap[Bank]]);

							updateRowTable(scheduledRequest->addressMap[Rank], scheduledRequest->addressMap[Bank], scheduledRequest->row); // Update the open row table for the device
							requestQueue[index]->removeRequest();																		   // Remove the request that has been choosed
						}
					}
				}
				scheduledRequest = NULL;
			}
		}
	};
} // namespace MCsim

#endif /* REQUESTSCHEDULER_FRFCFS_THRESHOLD_H */
