#!/usr/bin/env python3

########################################################################
#
# Simple File Request/Download Protocol
#
########################################################################
#
# When the client connects to the server and wants to request a file
# download, it sends the following message: 1-byte GET command + 1-byte
# filename size field + requested filename, e.g., 

# ------------------------------------------------------------------
# | 1 byte GET command  | 1 byte filename size | ... file name ... |
# ------------------------------------------------------------------

# The server checks for the GET and then transmits the requested file.
# The file transfer data from the server is prepended by an 8 byte
# file size field as follows:

# -----------------------------------
# | 8 byte file size | ... file ... |
# -----------------------------------

# The server needs to have REMOTE_FILE_NAME defined as a text file
# that the client can request. The client will store the downloaded
# file using the filename LOCAL_FILE_NAME. This is so that you can run
# a server and client from the same directory without overwriting
# files.

########################################################################

import socket
import argparse
import time
import os
import threading

########################################################################

# Define all of the packet protocol field lengths.

CMD_FIELD_LEN            = 1 # 1 byte commands sent from the client.
FILENAME_SIZE_FIELD_LEN  = 1 # 1 byte file name size field.
FILESIZE_FIELD_LEN       = 8 # 8 byte file size field.
    
# Define a dictionary of commands. The actual command field value must
# be a 1-byte integer. For now, we only define the "GET" command,
# which tells the server to send a file.

CMD = {"GET" : 1,"PUT":2,"LIST":3,"BYE":4}

filename_dir = {}
# filenames = os.listdir(".")
# print(filenames)

MSG_ENCODING = "utf-8"
SOCKET_TIMEOUT = 60



########################################################################
# recv_bytes frontend to recv
########################################################################

# Call recv to read bytecount_target bytes from the socket. Return a
# status (True or False) and the received butes (in the former case).

def recv_bytes(sock, bytecount_target):
    # Be sure to timeout the socket if we are given the wrong
    # information.
    sock.settimeout(SOCKET_TIMEOUT)
    try:
        byte_recv_count = 0 # total received bytes
        recv_bytes = b''    # complete received message
        while byte_recv_count < bytecount_target:
            # Ask the socket for the remaining byte count.
            new_bytes = sock.recv(bytecount_target-byte_recv_count)
            # If ever the other end closes on us before we are done,
            # give up and return a False status with zero bytes.
            if not new_bytes:
                return(False, b'')
            byte_recv_count += len(new_bytes)
            recv_bytes += new_bytes
        # Turn off the socket timeout if we finish correctly.
        sock.settimeout(None)            
        return (True, recv_bytes)
    # If the socket times out, something went wrong. Return a False
    # status.
    except socket.timeout:
        sock.settimeout(None)        
        print("recv_bytes: Recv socket timeout!")
        return (False, b'')

########################################################################
# SERVER
########################################################################

class Server:

    HOSTNAME = "0.0.0.0"

    PORT = 50001
    UDP_PORT = 50000
    RECV_SIZE = 1024
    BACKLOG = 5

    SCAN_CMD = "SERVICE DISCOVERY"
    SCAN_CMD_ENCODED = SCAN_CMD.encode(MSG_ENCODING)
    
    MSG = "Yuying/Frank/Peter's File Sharing Service"
    MSG_ENCODED = MSG.encode(MSG_ENCODING)



    FILE_NOT_FOUND_MSG = "Error: Requested file is not available!"

    # This is the file that the client will request using a GET.
    # REMOTE_FILE_NAME = "greek.txt"
    # REMOTE_FILE_NAME = "twochars.txt"
    # REMOTE_FILE_NAME = "ocanada_greek.txt"
    REMOTE_FILE_NAME = os.getcwd()+"/server/ocanada_english.txt"

    def __init__(self):
        self.create_listen_socket()
        thread1 = threading.Thread(target = self.receive_forever_UDP)
        thread1.start()
        thread2 = threading.Thread(target = self.process_connections_forever)
        thread2.start()

    def create_listen_socket(self):
        try:
            #Create the UDP server listen socket in the usual way.
            self.udp_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
            self.udp_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
            self.udp_socket.bind( ("0.0.0.0", Server.UDP_PORT) )

            # Create the TCP server listen socket in the usual way.
            self.socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            self.socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
            self.socket.bind((Server.HOSTNAME, Server.PORT))
            self.socket.listen(Server.BACKLOG)
            print("Listening on port {} ...".format(Server.PORT))
        except Exception as msg:
            print(msg)
            exit()

    def receive_forever_UDP(self):
        while True:
            try:
                print(Server.MSG, "listening on port {} ...".format(Server.UDP_PORT))
                recvd_bytes, address = self.udp_socket.recvfrom(Server.RECV_SIZE)

                print("Received: ", recvd_bytes.decode('utf-8'), " Address:", address)
            
                # Decode the received bytes back into strings.
                recvd_str = recvd_bytes.decode(MSG_ENCODING)

                # Check if the received packet contains a service scan
                # command.
                if Server.SCAN_CMD in recvd_str:
                    # Send the service advertisement message back to
                    # the client.
                    self.udp_socket.sendto(Server.MSG_ENCODED, address)
            except KeyboardInterrupt:
                print()
                exit()

    def process_connections_forever(self):
        try:
            while True:
                self.connection_handler(self.socket.accept())
        except KeyboardInterrupt:
            print()
        finally:
            self.socket.close()

    def connection_handler(self, client):
        connection, address = client
        print("-" * 72)
        print("Connection received from {}.".format(address))

        while True:
            ################################################################
            # Process a connection and see if the client wants a file that
            # we have.
            
            # Read the command and see if it is a GET command.
            status, cmd_field = recv_bytes(connection, CMD_FIELD_LEN)
            # If the read fails, give up.
            if not status:
                print("Closing connection ...")
                connection.close()
                return
            # Convert the command to our native byte order.
            cmd = int.from_bytes(cmd_field, byteorder='big')
            #print(cmd)
            # Give up if we don't get a GET command.
            if cmd == CMD["GET"]:
                # print("GET command not received. Closing connection ...")
                # connection.close()
                # return

                # GET command is good. Read the filename size (bytes).
                status, filename_size_field = recv_bytes(connection, FILENAME_SIZE_FIELD_LEN)
                if not status:
                    print("Closing connection ...")            
                    connection.close()
                    return
                filename_size_bytes = int.from_bytes(filename_size_field, byteorder='big')
                if not filename_size_bytes:
                    print("Connection is closed!")
                    connection.close()
                    return
                
                print('Filename size (bytes) = ', filename_size_bytes)

                # Now read and decode the requested filename.
                status, filename_bytes = recv_bytes(connection, filename_size_bytes)
                if not status:
                    print("Closing connection ...")            
                    connection.close()
                    return
                if not filename_bytes:
                    print("Connection is closed!")
                    connection.close()
                    return
            

                filename = filename_bytes.decode(MSG_ENCODING)
                print('Requested filename = ', filename)

                ################################################################
                # See if we can open the requested file. If so, send it.
                
                # If we can't find the requested file, shutdown the connection
                # and wait for someone else.
                try:
                    file = open(os.getcwd()+"/server/"+filename, 'rb').read()
                except FileNotFoundError:
                    print(Server.FILE_NOT_FOUND_MSG)
                    connection.close()                   
                    return

                # Encode the file contents into bytes, record its size and
                # generate the file size field used for transmission.
                #file_bytes = file.encode(MSG_ENCODING)
                file_size_bytes = len(file)
                file_size_field = file_size_bytes.to_bytes(FILESIZE_FIELD_LEN, byteorder='big')

                # Create the packet to be sent with the header field.
                pkt = file_size_field + file
                
                try:
                    # Send the packet to the connected client.
                    connection.sendall(pkt)
                    print("Sending file: ", filename)
                    print("file size field: ", file_size_field.hex(), "\n")
                    # time.sleep(20)
                except socket.error:
                    # If the client has closed the connection, close the
                    # socket on this end.
                    print("Closing client connection ...")
                    connection.close()
                    return
                
            #---------------------------------------------------------------------------------#
            #PUT Command

            elif cmd == CMD["PUT"]:
                # PUT command is good. Read the filename size (bytes).
                status, filename_size_field = recv_bytes(connection, FILENAME_SIZE_FIELD_LEN)
                if not status:
                    print("Closing connection ...")            
                    connection.close()
                    return
                filename_size_bytes = int.from_bytes(filename_size_field, byteorder='big')
                if not filename_size_bytes:
                    print("Connection is closed!")
                    connection.close()
                    return
                
                print('Filename size (bytes) = ', filename_size_bytes)

                # Now read and decode the received filename.
                status, filename_bytes = recv_bytes(connection, filename_size_bytes)
                if not status:
                    print("Closing connection ...")            
                    connection.close()
                    return
                if not filename_bytes:
                    print("Connection is closed!")
                    connection.close()
                    return
                
                status, filesize_bytes = recv_bytes(connection, FILESIZE_FIELD_LEN)
                if not status:
                    print("Closing connection ...")            
                    connection.close()
                    return
                if not filename_bytes:
                    print("Connection is closed!")
                    connection.close()
                    return
                
                filesize = int.from_bytes(filesize_bytes,byteorder='big')


                filename = filename_bytes.decode(MSG_ENCODING)


                status, file_bytes = recv_bytes(connection, filesize)
                if not status:
                    print("Closing connection ...")
                    connection.close()
                    return
                if not file_bytes:
                    print("Closing connection ...")
                    connection.close()
                    return
                #file_data = file_bytes.decode(MSG_ENCODING)

                print('Received filename = ', filename)

                with open(os.getcwd()+"/server/"+filename, 'wb') as f:
                    f.write(file_bytes)
                print(file_bytes)
                f.close()
                
            #---------------------------------------------------------------------------------#
            #LIST Command    
            
            elif cmd == CMD["LIST"]:
                try:
                # LIST command is good. Read the filename size (bytes).
                    print("list rec")
                    filenames = os.listdir(os.getcwd()+"/server/")
                    file_list = ""
                    print (len(filenames))
                    for i in range(len(filenames)):
                        if filenames[i].endswith(".txt"):
                            file_list += filenames[i] + "\n"

                    file_bytes = file_list.encode(MSG_ENCODING)

                    print(file_bytes)
                    file_size_bytes = len(file_bytes)
                    print(file_size_bytes)
                    file_size_field = file_size_bytes.to_bytes(FILENAME_SIZE_FIELD_LEN, byteorder='big')
                    print(file_size_field)
                    pkt = file_size_field + file_bytes
                    connection.sendall(pkt)
                    print(pkt)
                    

                except socket.error:
                    # If the client has closed the connection, close the
                    # socket on this end.
                    print("Closing client connection ...")
                    connection.close()
                    return

            #---------------------------------------------------------------------------------#
            #BYE Command
            elif cmd == CMD["BYE"]:
                print("Connection is closed!")
                connection.close()
                return
            
            else:
                print("Valid command not received. Closing connection ...")
                connection.close()
                return
########################################################################
# CLIENT
########################################################################

class Client:
    input_value = 0
    RECV_SIZE = 10
    UDP_PORT = 50000
    SCAN_TIMEOUT = 5
    BROADCAST_ADDRESS = "255.255.255.255"
    # BROADCAST_ADDRESS = "192.168.1.255"    

    ADDRESS_PORT = (BROADCAST_ADDRESS, UDP_PORT)

    # Define the local file name where the downloaded file will be
    # saved.
    DOWNLOADED_FILE_NAME = "filedownload.txt"
    SERVICE_DISCOVERY_MSG = "SERVICE DISCOVERY"

    socket_connected = False

    def __init__(self):
        self.prompt_user_forever()

    def prompt_user_forever(self):
        try:
            while True:
                self.get_file()
        except Exception as msg:
            print(msg)
            exit()
        
    def get_socket(self):
        try:
            self.socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        except Exception as msg:
            print(msg)
            exit()

    def connect_to_server(self, hostname, port):
        try:
            self.socket.connect((hostname, int(port)))
            print("Connect to the file sharing service at " + hostname + " " + port)
            self.socket_connected = True
        except Exception as msg:
            print(msg)
            exit()
    
    def scan(self):
        udp_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        udp_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        udp_socket.setsockopt(socket.SOL_SOCKET, socket.SO_BROADCAST, 1)
        udp_socket.settimeout(Client.SCAN_TIMEOUT)
        encoded_msg = Client.SERVICE_DISCOVERY_MSG.encode(MSG_ENCODING)
        udp_socket.sendto(encoded_msg, Client.ADDRESS_PORT)

        try:
            response, addr = udp_socket.recvfrom(1024)
            decoded_response = response.decode(MSG_ENCODING)
            # If a FS server responds, print out the details so that
            # we can connect to its file sharing port.
            print(decoded_response, " found at ", addr)
        except socket.timeout:
            print("No services found.")


    def llist(self):
        filename = os.listdir(os.getcwd()+"/client/")
        print(filename)


    def get_file(self):

        ################################################################
        # Generate a file transfer request to the server
        
        # Create the packet cmd field.
        input_text = input("Please input command: ").split()
        
        
        if input_text[0] == "get":
            self.input_value = 1
        elif input_text[0] == "put":
            self.input_value = 2
        elif input_text[0] == "rlist":
            self.input_value = 3
        elif input_text[0] == "bye":
            self.input_value = 4
        elif input_text[0] == "scan":
            self.input_value = 0
            self.scan()
        elif input_text[0] == "llist":
            self.input_value = 0
            self.llist()
        elif input_text[0] == "Connect":
            self.input_value = 0
            if (len(input_text) < 2):
                print("hostname or port not specified")
                return
            self.get_socket()
            self.connect_to_server(input_text[1], input_text[2])
        else:
            print("The command is invalid.")
            return
        
        if (self.input_value != 0 and self.socket_connected == False):
            print("Socket connection not established yet. Please use Connect first.")
            return
        
        if self.input_value ==1:
            cmd_field = CMD["GET"].to_bytes(CMD_FIELD_LEN, byteorder='big')
            file_name = input_text[1]
            file_path = os.getcwd()+"/client/"
            Client.DOWNLOADED_FILE_NAME = file_path + file_name
            if not file_name:
                print("File name is empty.")
                self.socket.close()
                self.socket_connected = False
                return
            # Create the packet filename field.
            filename_field_bytes = file_name.encode(MSG_ENCODING)

            # Create the packet filename size field.
            filename_size_field = len(filename_field_bytes).to_bytes(FILENAME_SIZE_FIELD_LEN, byteorder='big')

            # Create the packet.
            print("CMD field: ", cmd_field.hex())
            print("Filename_size_field: ", filename_size_field.hex())
            print("Filename field: ", filename_field_bytes.hex())
            
            pkt = cmd_field + filename_size_field + filename_field_bytes

            # Send the request packet to the server.
            self.socket.sendall(pkt)

            ################################################################
            # Process the file transfer repsonse from the server
            
            # Read the file size field returned by the server.
            status, file_size_bytes = recv_bytes(self.socket, FILESIZE_FIELD_LEN)
            if not status:
                print("Closing connection ...")            
                self.socket.close()
                self.socket_connected = False
                return

            print("File size bytes = ", file_size_bytes.hex())
            if len(file_size_bytes) == 0:
                self.socket.close()
                self.socket_connected = False
                return

            # Make sure that you interpret it in host byte order.
            file_size = int.from_bytes(file_size_bytes, byteorder='big')
            print("File size = ", file_size)

            # self.socket.settimeout(4)                                  
            status, recvd_bytes_total = recv_bytes(self.socket, file_size)
            if not status:
                print("Closing connection ...")            
                self.socket.close()
                self.socket_connected = False
                return
            # print("recvd_bytes_total = ", recvd_bytes_total)
            # Receive the file itself.
            try:
                # Create a file using the received filename and store the
                # data.
                print("Received {} bytes. Creating file: {}" \
                    .format(len(recvd_bytes_total), Client.DOWNLOADED_FILE_NAME))

                with open(Client.DOWNLOADED_FILE_NAME, 'wb') as f:
                    #recvd_file = recvd_bytes_total.decode(MSG_ENCODING)
                    f.write(recvd_bytes_total)
                print(recvd_bytes_total)
            except KeyboardInterrupt:
                print()
                exit(1)

        
        elif self.input_value == 2:
            file_name = input_text[1]
            if not file_name:
                print("File name is empty.")
                self.socket.close()
                self.socket_connected = False
                return
            cmd_field = CMD["PUT"].to_bytes(CMD_FIELD_LEN, byteorder='big')

            # Create the packet filename field.
            filename_field_bytes = file_name.encode(MSG_ENCODING)

            # Create the packet filename size field.
            filename_size_field = len(filename_field_bytes).to_bytes(FILENAME_SIZE_FIELD_LEN, byteorder='big')

            try: 
                file_path = os.getcwd()+"/client/"+file_name
            except Exception as msg:
                print(msg)
                self.socket.close()
                self.socket_connected = False
                return
            
            file_data = open(file_path,'rb').read()
            #file_data_bytes = file_data.encode(MSG_ENCODING)
            file_size_field = len(file_data).to_bytes(FILESIZE_FIELD_LEN, byteorder='big')

            pkt = cmd_field + filename_size_field + filename_field_bytes + file_size_field + file_data

            try:
                self.socket.sendall(pkt)
                print("Sending file: ",file_name)
            except Exception as msg:
                print(msg)
                self.socket.close()
                self.socket_connected = False
                return
            
        elif self.input_value == 3:
            cmd_field = CMD["LIST"].to_bytes(CMD_FIELD_LEN, byteorder='big')

            self.socket.sendall(cmd_field)
            status, recvd_filename_size = recv_bytes(self.socket, FILENAME_SIZE_FIELD_LEN)
            recvd_filename_size = int.from_bytes(recvd_filename_size, byteorder='big')
            # print(status,' ',recvd_filename_size)
            # recvd_filename_size = int(self.socket.recv(FILENAME_SIZE_FIELD_LEN).decode(MSG_ENCODING))
            
            if recvd_filename_size > 0:
                recvd_filenames = self.socket.recv(recvd_filename_size).decode(MSG_ENCODING)
                print(recvd_filenames)
            else:
                print("receive data failed")
                self.socket.close()
                self.socket_connected = False
                return
            

        elif self.input_value == 4:
            cmd_field = self.input_value.to_bytes(CMD_FIELD_LEN, byteorder='big')
            self.socket.sendall(cmd_field)
            print("The connection is closed")
            self.socket.close()
            self.socket_connected = False
            return
            
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






