import http.client
import json
import ssl
import os
import base64
import random
import time
from distutils.command.check import check

from cryptography.hazmat.primitives import serialization as crypto_serialization
from cryptography.hazmat.primitives import hashes
from cryptography.hazmat.primitives.asymmetric import rsa, padding
from cryptography.hazmat.backends import default_backend as crypto_default_backend
import cryptography.x509 as crypto_cert

# Device common name
common_name = 'patient_device_1'
patient_id = 'Rafaelo Rafaelic'

# Private and public key file names
private_key_path = 'key.priv'
public_key_path = 'key.pub'

# Certificate file name
certificate_file_path = 'device_999018678866013.crt'

# certificate_secret = 'sadpotato'

# Host of endpoint
api_hospital_root = 'localhost:9002'

# Endpoints
endpoint_register = '/api/devices/register'
endpoint_message = '/api/devices/message'


def generate_keys():
    key = rsa.generate_private_key(
        backend=crypto_default_backend(),
        public_exponent=65537,
        key_size=2048
    )

    private_key = key.private_bytes(
        crypto_serialization.Encoding.PEM,
        crypto_serialization.PrivateFormat.PKCS8,
        crypto_serialization.NoEncryption())

    public_key = key.public_key().public_bytes(
        crypto_serialization.Encoding.PEM,
        crypto_serialization.PublicFormat.SubjectPublicKeyInfo
    )

    file_private = open(private_key_path, 'w')
    file_private.write(bytes.decode(private_key, 'utf-8'))
    file_private.close()

    file_public = open(public_key_path, 'w')
    file_public.write(bytes.decode(public_key, 'utf-8'))
    file_public.close()

    return private_key, public_key


def read_keys():
    file_private = open(private_key_path, 'rb')
    private_key = crypto_serialization.load_pem_private_key(file_private.read(), password=None)

    file_private.close()

    file_public = open(public_key_path, 'rb')
    public_key = crypto_serialization.load_pem_public_key(file_public.read())
    file_public.close()

    private_key = private_key.private_bytes(
        crypto_serialization.Encoding.PEM,
        crypto_serialization.PrivateFormat.PKCS8,
        crypto_serialization.NoEncryption())

    public_key = public_key.public_bytes(
        crypto_serialization.Encoding.PEM,
        crypto_serialization.PublicFormat.SubjectPublicKeyInfo
    )

    return private_key, public_key


def establish_connection():
    context = ssl.create_default_context()
    context.check_hostname = True
    context.protocol = ssl.PROTOCOL_TLS
    context.verify_mode = ssl.CERT_REQUIRED

    context.load_cert_chain(certfile=certificate_file_path, keyfile=private_key_path)

    # Create a connection to submit HTTP requests
    connection = http.client.HTTPSConnection(api_hospital_root, context=context)
    return connection


def send_data(connection, data, endpoint):
    request_headers = {
        'Content-Type': 'application/json'
    }

    connection.request(method="POST", url=endpoint, headers=request_headers, body=json.dumps(data))

    response = connection.getresponse()
    data = response.read()
    data = data.decode('utf-8')
    if response.status != 200:
        raise Exception(data)


def register(private_key, public_key):
    data_types = ['blood_pressure_upper', 'blood_pressure_lower', 'temperature', 'heartrate']
    data_types.sort()
    data = {
        'commonName': common_name,
        'patientName': patient_id,
        'parameters': { type: '' for type in data_types}
    }
    private_key = crypto_serialization.load_pem_private_key(private_key, password=None)
    data['signature'] = private_key.sign(
        data=bytes(common_name + patient_id + ''.join(data_types), 'utf-8'),
        padding=padding.PKCS1v15(),
        algorithm =hashes.SHA1()
    )
    data['signature'] = base64.b64encode(data['signature']).decode('utf-8')

    connection = establish_connection()
    send_data(connection, data, endpoint_register)
    return connection, data['signature']

def update_device(connection, signature):
    blood_pressure_upper = random.randint(100, 160)
    blood_pressure_lower = random.randint(60, 100)
    heartrate = random.randint(40, 120)
    temperature = random.randint(350, 410)/10
    data = {
        'commonName': common_name,
        'parameters': {
            'blood_pressure_upper': blood_pressure_upper,
            'blood_pressure_lower': blood_pressure_lower,
            'heartrate': heartrate,
            'temperature': temperature
        },
        'patientName': patient_id,
        'signature': signature
    }
    connection = establish_connection()
    print("Sent data for patient " + str(patient_id) + " - blood pressure upper: " + str(blood_pressure_upper) + ", blood pressure lower: "
          + str(blood_pressure_lower) + ", heartrate: " + str(heartrate) + ", temperature: " + str(temperature))
    send_data(connection, data, endpoint_message)

if __name__ == '__main__':
    if (not os.path.isfile(private_key_path)) or (not os.path.isfile(public_key_path)):
        private_key, public_key = generate_keys()
    else:
        private_key, public_key = read_keys()
    if not os.path.isfile(certificate_file_path):
        print("Certificate not found! Check with admin to register a certificate with name " + certificate_file_path + " to enable communication!")
    else:
        try:
            connection, signature = register(private_key, public_key)
            while True:
                update_device(connection, signature)
                time.sleep(5)
        except Exception as e:
            print("Exception occurred! Cause - " + str(e))
