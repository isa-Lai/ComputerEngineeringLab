#!/usr/bin/python3

"""
Echo Client and Server Classes

T. D. Todd
McMaster University

to create a Client: "python EchoClientServer.py -r client" 
to create a Server: "python EchoClientServer.py -r server" 

or you can import the module into another file, e.g., 
import EchoClientServer
e.g., then do EchoClientserver.Server()

"""

########################################################################

import socket
import argparse
import sys

from cryptography.fernet import Fernet

########################################################################
# Echo Server class
########################################################################

class Server:

    # Set the server hostname used to define the server socket address
    # binding. Note that "0.0.0.0" or "" serves as INADDR_ANY. i.e.,
    # bind to all local network interfaces.
    # HOSTNAME = "192.168.1.22" # single interface    
    # HOSTNAME = "hornet"       # valid hostname (mapped to address/IF)
    # HOSTNAME = "localhost"    # local host (mapped to local address/IF)
    # HOSTNAME = "127.0.0.1"    # same as localhost
    HOSTNAME = "0.0.0.0"      # All interfaces.
    
    # Server port to bind the listen socket.
    PORT = 50000
    
    RECV_BUFFER_SIZE = 1024 # Used for recv.
    MAX_CONNECTION_BACKLOG = 10 # Used for listen.

    # We are sending text strings and the encoding to bytes must be
    # specified.
    MSG_ENCODING = "ascii" # ASCII text encoding.
    # MSG_ENCODING = "utf-8" # Unicode text encoding.

    # Create server socket address. It is a tuple containing
    # address/hostname and port.
    SOCKET_ADDRESS = (HOSTNAME, PORT)

    file_path = './course_grades_2024.csv'
    database = {}
    avg = {'Lab 1': 0, 'Lab 2': 0, 'Lab 3': 0, 'Lab 4': 0, 'Midterm': 0, 'Exam 1': 0, 'Exam 2': 0, 'Exam 3': 0, 'Exam 4': 0}
    command_map = {
        'GMA': 'Midterm',
        'GL1A': 'Lab 1',
        'GL2A': 'Lab 2',
        'GL3A': 'Lab 3',
        'GL4A': 'Lab 4',
        'GEA': 'Exam'
        }

    def __init__(self):
        self.key = ''
        self.read_database()
        self.create_listen_socket()
        self.process_connections_forever()

    def create_listen_socket(self):
        try:
            # Create an IPv4 TCP socket.
            self.socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

            # Set socket layer socket options. This one allows us to
            # reuse the socket address without waiting for any timeouts.
            self.socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)

            # Bind socket to socket address, i.e., IP address and port.
            self.socket.bind(Server.SOCKET_ADDRESS)

            # Set socket to listen state.
            self.socket.listen(Server.MAX_CONNECTION_BACKLOG)
            print("Listening on port {} ...".format(Server.PORT))
        except Exception as msg:
            print(msg)
            sys.exit(1)

    def process_connections_forever(self):
        try:
            while True:
                # Block while waiting for accepting incoming TCP
                # connections. When one is accepted, pass the new
                # (cloned) socket info to the connection handler
                # function. Accept returns a tuple consisting of a
                # connection reference and the remote socket address.
                self.connection_handler(self.socket.accept())
        except Exception as msg:
            print(msg)
        except KeyboardInterrupt:
            print()
        finally:
            # If something bad happens, make sure that we close the
            # socket.
            self.socket.close()
            sys.exit(1)

    def connection_handler(self, client):
        # Unpack the client socket address tuple.
        
        connection, address_port = client
        print("-" * 72)
        print("Connection received from {}.".format(address_port))
        # Output the socket address.
        print(client)

        while True:
            try:
                self.key = ''
                # Receive bytes over the TCP connection. This will block
                # until "at least 1 byte or more" is available.
                recvd_bytes = connection.recv(Server.RECV_BUFFER_SIZE)
            
                # If recv returns with zero bytes, the other end of the
                # TCP connection has closed (The other end is probably in
                # FIN WAIT 2 and we are in CLOSE WAIT.). If so, close the
                # server end of the connection and get the next client
                # connection.
                if len(recvd_bytes) == 0:
                    print("Closing client connection ... ")
                    connection.close()
                    break
                
                # Decode the received bytes back into strings. Then output
                # them.
                recvd_str = recvd_bytes.decode(Server.MSG_ENCODING)
                print("Received: ", recvd_str)
                self.id = recvd_str.split()[0]
                self.get_encrypt_key()
                # print(self.key)
                if self.key == '': 
                    response = 'Student ID not found.'
                    response = response.encode(Server.MSG_ENCODING)
                    connection.sendall(response)
                    connection.close()
                    print("Closing client connection ... ")
                    break
                else :
                    response = str(self.handle_command(recvd_str))

                # Send the received bytes back to the client. We are
                # sending back the raw data.
                    response = response.encode(Server.MSG_ENCODING)

                    encryption_key_bytes = self.key.encode('utf-8')
                    fernet = Fernet(encryption_key_bytes)
                    encrypted_message_bytes = fernet.encrypt(response)
                    connection.sendall(encrypted_message_bytes)

                print("Sent: ", response)
                # print("Sent(encrypted): ", encrypted_message_bytes)

            except KeyboardInterrupt:
                print()
                print("Closing client connection ... ")
                connection.close()
                break
    
    def handle_command(self, string1):
        id_string = string1.split()[0]
        cmd_string = string1.split()[1]
        # print(id_string)
        # print(cmd_string)
        try:
            if cmd_string.upper() == 'GG':
                print(f"Received 'GG' command from client")
                entry = self.search_database(id_string).copy()
                del entry["Name"]
                del entry["ID Number"]
                del entry["Key"]
                return entry
            else:
                print(f"Received " + cmd_string.upper() + " command from client")
                return self.search_average(cmd_string.upper())

        except Exception as e:
            print(f'Exception handling command "{cmd_string}": {e}')
            return "Server error while handling command."

    def read_database(self):
        count = 0
        
        print ("Data read from CSV file: " + Server.file_path)
        with open(Server.file_path, 'r') as file:
            # Read the first line to get column headers
            lineBuff = file.readline()
            print(lineBuff)
            headers = lineBuff.strip().split(',')
            
            # Iterate through each line in the file
            for line in file:
                count += 1
                print(line)
                # Split the line into values
                values = line.strip().split(',')

                # Summing up the scores for each category
                for i, header in enumerate(headers):
                    if header in self.avg:
                        self.avg[header] += float(values[i])

                # Use the ID number as the key for the database
                self.database[values[1]] = dict(zip(headers, values))

        # Calculate the averages
        for key in self.avg:
            self.avg[key] /= count
        
        #print(headers)
        #print(self.database)
        #print(self.avg)
            
    def get_encrypt_key(self):
        entry = self.search_database(self.id)
        if entry == None:
            print("User Not Found.")
        else:
            self.key = entry['Key']
            print("User Found.")
    
    def search_database(self, id_number):
        # Return the entry corresponding to the ID number
        return self.database.get(id_number)
    
    def search_average(self, cmd):
        # Command not found
        if cmd not in self.command_map:
            return -1

        if cmd == 'GEA':
            exam_keys = [key for key in self.avg if key.startswith('Exam')]
            return sum(self.avg[key] for key in exam_keys) / len(exam_keys)

        return self.avg[self.command_map[cmd]]


########################################################################
# Echo Client class
########################################################################

class Client:

    # Set the server to connect to. If the server and client are running
    # on the same machine, we can use the current hostname.
    # SERVER_HOSTNAME = socket.gethostname()
    # SERVER_HOSTNAME = "192.168.1.22"
    SERVER_HOSTNAME = "localhost"
    
    
    # Try connecting to the compeng4dn4 echo server. You need to change
    # the destination port to 50007 in the connect function below.
    # SERVER_HOSTNAME = 'compeng4dn4.mooo.com'

    RECV_BUFFER_SIZE = 1024 # Used for recv.    
    # RECV_BUFFER_SIZE = 5 # Used for recv.    

    file_path = './course_grades_2024.csv'
    key = ""
    command_map = {
        'GMA': 'Midterm',
        'GL1A': 'Lab 1',
        'GL2A': 'Lab 2',
        'GL3A': 'Lab 3',
        'GL4A': 'Lab 4',
        'GEA': 'Exam'
        }

    def __init__(self):
        self.cmd_promt() #main of client
    
    def cmd_promt(self):
        try:
            while True:
                self.get_console_input()
                self.get_socket()
                self.connect_to_server()
                self.connection_send()
                self.connection_receive()
                self.socket.close()
        except Exception as msg:
            print(msg)
            sys.exit(1)

    def get_socket(self):
        try:
            # Create an IPv4 TCP socket.
            self.socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

            # Allow us to bind to the same port right away.            
            self.socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
            # Bind the client socket to a particular address/port.
            # self.socket.bind((Server.HOSTNAME, 40000))
                
        except Exception as msg:
            print(msg)
            sys.exit(1)

    def connect_to_server(self):
        try:
            # Connect to the server using its socket address tuple.
            self.socket.connect((Client.SERVER_HOSTNAME, Server.PORT))
            print("Connected to \"{}\" on port {}".format(Client.SERVER_HOSTNAME, Server.PORT))
        except Exception as msg:
            print(msg)
            sys.exit(1)

    def get_console_input(self):
        # In this version we keep prompting the user until a non-blank
        # line is entered, i.e., ignore blank lines.
        while True:
            input_text = input("Input Student ID CMD: ").split()
            if len(input_text) < 2:
                print("Info is missing. Either ID or CMD not received")
                continue
            
            self.id = input_text[0]
            self.cmd = input_text[1]
            print("Command entered: " + self.cmd)

            if (self.cmd == "GG"):
                print("Getting Grades:")
            elif self.cmd not in self.command_map.keys():
                print("CMD not valid, please use:")
                print(self.command_map)
                continue
            else:
                print("Fetching " + self.command_map[self.cmd] + " average:")

            if self.id != "":
                break


    def get_encrypt_key(self):
        with open(Client.file_path, 'r') as file:
            # Iterate through each line in the file
            for line in file:
                # Split the line into values
                values = line.strip().split(',')

                # Found this student
                if values[1] == self.id:
                    self.key = values[2]

                    break
        if self.key == "":
            print("Student ID not found.")

    
    def send_console_input_forever(self):
        while True:
            try:
                self.connection_send()
                self.connection_receive()
                self.get_console_input()
            except (KeyboardInterrupt, EOFError):
                print()
                print("Closing server connection")
                # If we get and error or keyboard interrupt, make sure
                # that we close the socket.
                self.socket.close()
                sys.exit(1)
                
    def connection_send(self):
        try:
            # Send string objects over the connection. The string must
            # be encoded into bytes objects first.
            send = self.id + " " + self.cmd
            self.socket.sendall(send.encode(Server.MSG_ENCODING))
        except Exception as msg:
            print(msg)
            sys.exit(1)

    def connection_receive(self):
        try:
            # Receive and print out text. The received bytes objects
            # must be decoded into string objects.
            recvd_bytes = self.socket.recv(Client.RECV_BUFFER_SIZE)

            # recv will block if nothing is available. If we receive
            # zero bytes, the connection has been closed from the
            # other end. In that case, close the connection on this
            # end and exit.
            if len(recvd_bytes) == 0:
                print("Closing server connection ... ")
                self.socket.close()
                sys.exit(1)

            self.get_encrypt_key()

            if recvd_bytes.decode(Server.MSG_ENCODING) == 'Student ID not found.':
                print("Received: User not found.")
            else:
                encryption_key_bytes = self.key.encode('utf-8')
                fernet = Fernet(encryption_key_bytes)
                recvd_bytes = fernet.decrypt(recvd_bytes)
                print("Received: ", recvd_bytes.decode(Server.MSG_ENCODING))
            
            print("-" * 72)

        except Exception as msg:
            print(msg)
            sys.exit(1)

########################################################################
# Process command line arguments if this module is run directly.
########################################################################

# When the python interpreter runs this module directly (rather than
# importing it into another file) it sets the __name__ variable to a
# value of "__main__". If this file is imported from another module,
# then __name__ will be set to that module's name.

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






