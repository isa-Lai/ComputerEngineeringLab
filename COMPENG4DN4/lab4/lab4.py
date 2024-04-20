#!/usr/bin/env python3

########################################################################

import socket
import argparse
import sys
import time
import struct
import ipaddress
import threading

########################################################################
# Multicast Address and Port
########################################################################

# MULTICAST_ADDRESS = "239.1.1.1"
MULTICAST_ADDRESS = "239.2.2.2"

MULTICAST_PORT = 2000

# Make them into a tuple.
MULTICAST_ADDRESS_PORT = (MULTICAST_ADDRESS, MULTICAST_PORT)

# Ethernet/Wi-Fi interface address
IFACE_ADDRESS = "192.168.0.186"

CMD = { "getdir": 1, "makeroom": 2, "deleteroom": 3}
CMD_FIELD_LEN = 1 # 1 byte commands sent from the client.
MSG_ENCODING = "utf-8"
########################################################################
# Echo Server class
########################################################################

class Server:
    
    HOSTNAME = "0.0.0.0" # socket.gethostname()
    PORT = 50000

    RECV_SIZE = 256
    BACKLOG = 10
    TIMEOUT = 2
    
    

    MESSAGE =  HOSTNAME + " multicast beacon: "
    MESSAGE_ENCODED = MESSAGE.encode('utf-8')

    TIMEOUT = 2

    def __init__(self):
        self.thread_list = []
        self.directory = {}
        self.create_listen_socket()
        self.process_connections_forever()

    def create_listen_socket(self):
        try:
            # Create an IPv4 TCP socket.
            self.socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

            # Get socket layer socket options.
            self.socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)

            # Bind socket to socket address, i.e., IP address and port.
            self.socket.bind( (Server.HOSTNAME, Server.PORT) )

            # Set socket to listen state.
            self.socket.listen(Server.BACKLOG)
            print("Chat Room Server Listening on port {} ...".format(Server.PORT))

        except Exception as msg:
            print(msg)
            sys.exit(1)

    def process_connections_forever(self):
        try:
            while True:
                new_client = self.socket.accept()

                # A new client has connected. Create a new thread and
                # have it process the client using the connection
                # handler function.
                new_thread = threading.Thread(target=self.connection_handler, args=(new_client,))

                # Record the new thread.
                self.thread_list.append(new_thread)

                # Start the new thread running.
                print("Starting serving thread: ", new_thread.name)
                new_thread.daemon = True
                new_thread.start()

        except Exception as msg:
            print(msg)
        except KeyboardInterrupt:
            print()
        finally:
            print("Closing server socket ...")
            self.socket.close()
            sys.exit(1)

    def connection_handler(self, client):
        connection, address_port = client
        pkt = b''
        print("-" * 72)
        print("Connection received from {}.".format(address_port))

        while True:
            # Receive bytes over the TCP connection. This will block
            # until "at least 1 byte or more" is available.
            
            # recvd = connection.recv(CMD_FIELD_LEN)
            recvd_bytes = connection.recv(Server.RECV_SIZE)
            print(recvd_bytes)
            recvd = recvd_bytes.decode(MSG_ENCODING).split()
            print(recvd)
            # If recv returns with zero bytes, the other end of the
            # TCP connection has closed (The other end is probably in
            # FIN WAIT 2 and we are in CLOSE WAIT.). If so, close the
            # server end of the connection and get the next client
            # connection.
            if len(recvd_bytes) == 0:
                print("Closing {} client connection ... ".format(address_port))
                connection.close()
                # Break will exit the connection_handler and cause the
                # thread to finish.
                break
            if(recvd[0]== "1"):
                cmd = int(recvd[0])
            elif(recvd[0]== "2"):
                cmd = int(recvd[0])
            elif(recvd[0]== "3"):
                cmd = int(recvd[0])
            else: break

            print(cmd)

            if cmd == CMD["getdir"]:
                # Get and parse directory infor into packet for transmission
                print("Received getdir command.")
                pkt = ""
                for entry in self.get_dir():
                    pkt += f"{entry[0]} {entry[1]} {entry[2]}\n"
                if pkt == "":
                    pkt = "There are currently no rooms available."
                pkt = pkt.encode(MSG_ENCODING)

            elif cmd == CMD["makeroom"]:
                print("Received makeroom command.")
                # recv_bytes = connection.recv(Server.RECV_SIZE)
                # recv_str = recv_bytes.decode(MSG_ENCODING)
                args = recvd
                self.create_room(args[1], args[2], args[3])
                pkt = "Room created"
                pkt = pkt.encode(MSG_ENCODING)

            elif cmd == CMD["deleteroom"]:
                # recv_bytes = connection.recv(Server.RECV_SIZE)
                # recv_str = recv_bytes.decode(MSG_ENCODING)
                args = recvd
                pkt = self.destroy_room(recvd[1])
                pkt = pkt.encode(MSG_ENCODING)
            try:
                # Send the packet to the connected client.
                if pkt != None:
                    connection.sendall(pkt)
                # print("Sent packet bytes: \n", pkt)
            except socket.error:
                # If the client has closed the connection, close the
                # socket on this end.
                print("Closing client connection ...")
                connection.close()
                return
                
            # Decode the received bytes back into strings. Then output
            # them.
            recvd_str = recvd_bytes.decode(MSG_ENCODING)
            print("Received: ", recvd_str)
                        # Send the received bytes back to the client.

    def get_dir(self):
        arr = []
        for room in self.directory.values():
            arr.append(room.get_info())
        return arr

    def create_room(self, name, address, port):
        self.directory[name] = ChatRoom(name, address, port)
    
    def destroy_room(self, name):
        try:
            if name not in self.directory:
                return "The room does not exist."
            else:
                self.directory.pop(name)
                return "Room deleted."
        except Exception as msg:
            print(msg)
            exit()

        

class ChatRoom:

    def __init__(self, name, ip, port):
        self.port = int(port)
        self.ip = ip
        self.name = name

    def get_info(self):
        return (self.name, self.ip, self.port)

                


########################################################################
# Multicast Receiver 
########################################################################
#
# There are two things that we need to do:
#
# 1. Signal to the os that we want a multicast group membership, so
# that it will capture multicast packets arriving on the designated
# interface. This will also ensure that multicast routers will forward
# packets to us. Note that multicast is at layer 3, so ports do not
# come into the picture at this point.
#
# 2. Bind to the appopriate address/port (L3/L4) so that packets
# arriving on that interface will be properly filtered so that we
# receive packets to the designated address and port.
#
############################################
# 1. IP add multicast group membership setup
############################################
#
# Signal to the os that you want to join a particular multicast group
# address on a specified interface. Done via setsockopt function call.
# The multicast address and interface (address) are part of the add
# membership request that is passed to the lower layers.
#
# This is done via MULTICAST_ADDRESS from above and RX_IFACE_ADDRESS
# defined below.
#
# If you choose "0.0.0.0" for the Rx interface, the system will select
# the interface, which will probably work ok. In more complex
# situations, where, for example, you may have multiple network
# interfaces, you may have to specify the interface explicitly by
# using its address, as shown in the examples below.

# RX_IFACE_ADDRESS = "0.0.0.0"
# RX_IFACE_ADDRESS = "127.0.0.1"
RX_IFACE_ADDRESS = IFACE_ADDRESS 

#################################################
# 2. Multicast receiver bind (i.e., filter) setup
#################################################
#
# The receiver socket bind address. This is used at the IP/UDP level to
# filter incoming multicast receptions. Using "0.0.0.0" should work
# ok. Binding using the unicast address, e.g., RX_BIND_ADDRESS =
# "192.168.1.22", fails (Linux) since arriving packets don't carry this
# destination address.
# 

RX_BIND_ADDRESS = "0.0.0.0"
# RX_BIND_ADDRESS = MULTICAST_ADDRESS # Ok for Linux/MacOS, not for Windows 10.

# Receiver socket will bind to the following.
RX_BIND_ADDRESS_PORT = (RX_BIND_ADDRESS, MULTICAST_PORT)

########################################################################

class Client:

    RECV_SIZE = 256

    RECV_BUFFER_SIZE = 1024

    SERVER_HOSTNAME = "localhost"

    roomdir = {}
    def __init__(self):
        self.TCPconnected = False
        self.name = "DefaultName"
        self.promp_input_forever()
        

    def promp_input_forever(self):
        while True:
            try:
                self.get_input()
            except Exception as msg:
                print(msg)
                continue
            except KeyboardInterrupt:
                exit()


    def get_input(self):
        input_val = input("Please input command: ")
        input_text = input_val.split()
        
        
        if input_text[0] == "connect":
            self.get_TCPsocket()
            self.connectTCP_to_server()
        elif input_text[0] == "bye":
            if not self.TCPconnected:
                print("not yet connect")
                return
            self.TCPsocket.close()
            self.TCPconnected = False
        elif input_text[0] == "name":
            self.name = input_text[1]
            print("name changed to: " + self.name)
        elif input_text[0] == "chat":
            self.handle_chat(input_text[1])
        elif input_text[0] == "getdir":
            if not self.TCPconnected:
                print("not yet connect")
                return
            
            dir = self.send_receive_TCP("1")
            self.handle_getdir(dir)
        elif input_text[0] == "makeroom":
            if not self.TCPconnected:
                print("not yet connect")
                return
            
            self.send_receive_TCP("2 "+input_text[1]+" "+input_text[2]+" "+input_text[3])
        elif input_text[0] == "deleteroom":
            if not self.TCPconnected:
                print("not yet connect")
                return
            self.send_receive_TCP("3 "+input_text[1])
        else:
            print("The command is invalid.")
            return
        
    def handle_chat(self, room_name):
        print(room_name)
        addr_port = self.roomdir[room_name]
        print(addr_port)
        self.chat_addr = addr_port
        #create UDP receive socket, thread0=UDP recv forever
        #create UDP send multicast socket, thread1 = UDP send input forever
        self.flag_start = True
        if(self.flag_start):
            self.create_udp_recv_socket(addr_port)
            print("-"*60)
            run_event = threading.Event()
            run_event.set()
            msg = self.name + " has joined the chat."
            thread2 = threading.Thread(target = self.udp_recv, args =(run_event,))
            thread2.start()
            try:
                while 1:
                    sendmsg = self.name + ": " + input("\n" + self.name + ": ")
                    sendmsg_encode = sendmsg.encode(MSG_ENCODING)
                    self.udp_socket.sendto(sendmsg_encode, self.chat_addr)
            except KeyboardInterrupt:
                print("attempting exit chat room")
                run_event.clear()
                thread2.join()
                print("successfully exit chat room")



    def create_udp_recv_socket(self,addr_port):
        try:
            self.udp_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
            self.udp_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)

            bind_address = (RX_BIND_ADDRESS,addr_port[1])
            self.udp_socket.bind(bind_address)
            print("Chat Room Directory Server listening on port", addr_port[1])

            multicast_group_bytes = socket.inet_aton(addr_port[0])
            print("Multicast Group: ", addr_port[0])

            # Set up the interface to be used.
            multicast_if_bytes = socket.inet_aton(RX_IFACE_ADDRESS)

            # Form the multicast request.
            multicast_request = multicast_group_bytes + multicast_if_bytes

            print("Adding membership (address/interface): ", addr_port[0],"/", RX_IFACE_ADDRESS)
            self.udp_socket.setsockopt(socket.IPPROTO_IP, socket.IP_ADD_MEMBERSHIP, multicast_request)
            self.udp_socket.settimeout(0.5) 
        except Exception as msg:
            print(msg)
            return


    def udp_recv(self, run_event):
        while run_event.is_set():
            try:
                recvd_bytes, address = self.udp_socket.recvfrom(Client.RECV_SIZE)
                recvd_bytes_decoded = recvd_bytes.decode(MSG_ENCODING)
                sender_name, msg = recvd_bytes_decoded.split(":", 1)  # Splitting to get sender's name
                if sender_name.strip() != self.name:  # Filtering out own messages
                    print("\n" + recvd_bytes_decoded)
            except socket.timeout:
                # No data received, loop back and check if run_event is still set
                continue
            except Exception as msg:
                print(msg)
                return

    def handle_getdir(self, dir):
        if (dir == "There are currently no rooms available."):
            return
        try:
            #print(dir)
            self.roomdir.clear()
            val = dir.splitlines()
            for element in val:
                recvd = element.split()
                self.roomdir[recvd[0]] = (recvd[1],int(recvd[2])) #example
            print(self.roomdir)
            # store dir to local
        except Exception as msg:
            print(msg)
            exit()
            
    def get_TCPsocket(self):
        try:
            self.TCPsocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        except Exception as msg:
            print(msg)
            exit()

    def connectTCP_to_server(self):
        try:
            self.TCPsocket.connect((Client.SERVER_HOSTNAME, Server.PORT))
            print("Connected to \"{}\" on port {}".format(Client.SERVER_HOSTNAME, Server.PORT))
            self.TCPconnected = True
        except Exception as msg:
            print(msg)
            exit()

    def send_receive_TCP(self, input_text):
        self.TCPsocket.sendall(input_text.encode(MSG_ENCODING))
        try:
            # Receive and print out text. The received bytes objects
            # must be decoded into string objects.
            recvd_bytes = self.TCPsocket.recv(Client.RECV_BUFFER_SIZE)

            # recv will block if nothing is available. If we receive
            # zero bytes, the connection has been closed from the
            # other end. In that case, close the connection on this
            # end and exit.
            if len(recvd_bytes) == 0:
                print("Closing server connection ... ")
                self.TCPsocket.close()
                return ""

            recvd_bytes = recvd_bytes.decode(MSG_ENCODING)
            print("Received: ", recvd_bytes)
            return recvd_bytes

        except Exception as msg:
            print(msg)
            sys.exit(1)



########################################################################
# Process command line arguments if run directly.
########################################################################

if __name__ == '__main__':
    roles = {'client': Client,'server': Server}
    parser = argparse.ArgumentParser()

    parser.add_argument('-r', '--role',
                        choices=roles, 
                        help='server or client role',
                        required=True, type=str)

    args = parser.parse_args()
    roles[args.role]()

########################################################################






