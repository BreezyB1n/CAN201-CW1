import argparse
import json
import os
import struct
from socket import *
import hashlib
import time

from tqdm import tqdm

# packet最大限制
MAX_PACKET_SIZE = 20480

# Const Value
OP_SAVE, OP_DELETE, OP_GET, OP_UPLOAD, OP_DOWNLOAD, OP_BYE, OP_LOGIN, OP_ERROR = 'SAVE', 'DELETE', 'GET', 'UPLOAD', 'DOWNLOAD', 'BYE', 'LOGIN', "ERROR"
TYPE_FILE, TYPE_DATA, TYPE_AUTH, DIR_EARTH = 'FILE', 'DATA', 'AUTH', 'EARTH'
FIELD_OPERATION, FIELD_DIRECTION, FIELD_TYPE, FIELD_USERNAME, FIELD_PASSWORD, FIELD_TOKEN = 'operation', 'direction', 'type', 'username', 'password', 'token'
FIELD_KEY, FIELD_SIZE, FIELD_TOTAL_BLOCK, FIELD_MD5, FIELD_BLOCK_SIZE = 'key', 'size', 'total_block', 'md5', 'block_size'
FIELD_STATUS, FIELD_STATUS_MSG, FIELD_BLOCK_INDEX = 'status', 'status_msg', 'block_index'
DIR_REQUEST, DIR_RESPONSE = 'REQUEST', 'RESPONSE'

def _argparse():
    parse = argparse.ArgumentParser()
    # 输入学生id
    parse.add_argument('-id', default=2253963, action='store', required=False, dest="id",
                       help="Please enter your xjtlu student id number")
    parse.add_argument('-f', default='xjtlu.jpg', action='store', required=False, dest="f",
                       help="Please give your transition fire path.")
    parse.add_argument("-server_ip", default='127.0.0.1', action='store', required=False, dest="server_ip",
                       help="The IP address you wish to send the request to.")
    return parse.parse_args()

def make_packet(json_data, bin_data=None):
    j = json.dumps(dict(json_data), ensure_ascii=False)
    j_len = len(j)
    if bin_data is None:
        return struct.pack('!II', j_len, 0) + j.encode()
    else:
        return struct.pack('!II', j_len, len(bin_data)) + j.encode() + bin_data

def make_request_packet(operation, data_type, json_data, bin_data=None):
    json_data[FIELD_OPERATION] = operation
    json_data[FIELD_TYPE] = data_type
    json_data[FIELD_DIRECTION] = DIR_REQUEST
    return make_packet(json_data, bin_data)

def get_tcp_packet(conn):
    bin_data = b''
    while len(bin_data) < 8:
        data_rec = conn.recv(8)
        if data_rec == b'':
            time.sleep(0.01)
        if data_rec == b'':
            return None, None
        bin_data += data_rec
    data = bin_data[:8]
    bin_data = bin_data[8:]
    j_len, b_len = struct.unpack('!II', data)
    while len(bin_data) < j_len:
        data_rec = conn.recv(j_len)
        if data_rec == b'':
            time.sleep(0.01)
        if data_rec == b'':
            return None, None
        bin_data += data_rec
    j_bin = bin_data[:j_len]

    try:
        json_data = json.loads(j_bin.decode())
    except Exception as ex:
        return None, None

    bin_data = bin_data[j_len:]
    while len(bin_data) < b_len:
        data_rec = conn.recv(b_len)
        if data_rec == b'':
            time.sleep(0.01)
        if data_rec == b'':
            return None, None
        bin_data += data_rec
    return json_data, bin_data

def connect_to_server(server_ip, server_port, args):
    client_socket = socket(AF_INET, SOCK_STREAM)
    client_socket.connect((server_ip, server_port))

    username = str(args.id)
    password_md5 = hashlib.md5(username.encode()).hexdigest()
    user = {
        FIELD_USERNAME: username,
        FIELD_PASSWORD: password_md5
    }

    login_packet = make_request_packet(OP_LOGIN, TYPE_AUTH, user)

    client_socket.send(login_packet)

    json_data, json_bin = get_tcp_packet(client_socket)
    token = json_data[FIELD_TOKEN]
    print(f'Token: {token}')
    return token, client_socket

def save_file(token, client_socket, args):
    file_path = args.f
    size = os.path.getsize(file_path)
    save_request = {
        FIELD_TOKEN: token,
        FIELD_SIZE: size,
        FIELD_KEY: file_path
    }

    client_socket.send(make_request_packet(OP_SAVE, TYPE_FILE, save_request))
    json_data, bin_data = get_tcp_packet(client_socket)
    return json_data

def upload_file(token, client_socket, args):
    json_data = save_file(token, client_socket, args)

    file_path = args.f
    f = open(file_path, 'rb')
    image_data = f.read()
    f.close()

    if json_data['status'] == 200:
        try:
            progress_bar = tqdm(total=json_data[FIELD_TOTAL_BLOCK], desc=f'Uploading {file_path}', unit='block',
                                unit_scale=True)
            for i in range(json_data[FIELD_TOTAL_BLOCK]):
                start_index = i * MAX_PACKET_SIZE
                end_index = (i + 1) * MAX_PACKET_SIZE if i < json_data[FIELD_TOTAL_BLOCK] - 1 else len(image_data)
                segment_data = image_data[start_index:end_index]

                upload_request = {
                    FIELD_KEY: json_data[FIELD_KEY],
                    FIELD_SIZE: json_data[FIELD_SIZE],
                    FIELD_BLOCK_INDEX: i,
                    FIELD_TOKEN: token,
                }

                client_socket.send(make_request_packet(OP_UPLOAD, TYPE_FILE, upload_request, segment_data))
                json_data2, bin_data2 = get_tcp_packet(client_socket)

                if json_data2.get('status', 500) == 200:
                    progress_bar.update(1)
                else:
                    print(f'Block {i} upload failed with status code: {json_data2.get("status", "N/A")}')
                    break

                if i == json_data[FIELD_TOTAL_BLOCK] - 1:
                    progress_bar.close()
                    if FIELD_MD5 in json_data2.keys():
                        print(f'MD5: {json_data2[FIELD_MD5]}')
                    else:
                        print(f'file {file_path} upload failed {json_data2}')

        except Exception as e:
            print(f"An error occurred: {e}")

    else:
        print(f'The upload operation failed due to {json_data[FIELD_STATUS_MSG]}')


def main():
    args = _argparse()
    server_ip = args.server_ip
    server_port = 1379
    token, client_socket = connect_to_server(server_ip, server_port, args)
    upload_file(token, client_socket, args)

if __name__ == '__main__':
    main()